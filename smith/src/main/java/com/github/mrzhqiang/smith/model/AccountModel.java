package com.github.mrzhqiang.smith.model;

import android.content.ContentValues;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import cn.mrzhqiang.helper.AccountHelper;
import com.github.mrzhqiang.smith.SmithApp;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.db.Db;
import com.github.mrzhqiang.smith.db.DbException;
import com.github.mrzhqiang.smith.net.Login;
import com.github.mrzhqiang.smith.net.Result;
import com.github.mrzhqiang.smith.net.Smith;
import com.google.gson.Gson;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.github.mrzhqiang.smith.db.Account.*;

public final class AccountModel {
  private static final String SELECT_ALL = "SELECT * FROM " + TABLE;

  @Inject BriteDatabase db;
  @Inject Smith smith;
  @Inject Gson gson;

  private static final Set<Subscription> SUBSCRIPTIONS = new HashSet<>();

  public AccountModel() {
    SmithApp.appComponent().inject(this);
  }

  @AnyThread public void create(Account account, @NonNull Result<Account> result) {
    SUBSCRIPTIONS.add(Observable.just(account)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnNext(this::insertIfNewAccount)
        .flatMap(this::registerOrLogin)
        .doOnNext(this::update)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread
  public void create(Account account, int start, int count, @NonNull Result<List<Account>> result) {
    SUBSCRIPTIONS.add(Observable.from(AccountHelper.autoUsernames(account.username(), start, count))
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .map(name -> Account.create(name, account.password(), "", Login.EMPTY))
        .doOnNext(this::insertIfNewAccount)
        .flatMap(this::registerOrLogin)
        .toList()
        .doOnNext(accounts -> {
          BriteDatabase.Transaction transaction = db.newTransaction();
          try {
            for (Account a : accounts) {
              update(a);
            }
            transaction.markSuccessful();
          } finally {
            transaction.end();
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @WorkerThread private void insertIfNewAccount(Account account) {
    try {
      ContentValues values = new ContentValues();
      values.put(Account.USERNAME, account.username());
      values.put(Account.PASSWORD, Db.encode(account.password()));
      values.put(Account.ALIAS, account.alias());
      values.put(Account.LOGIN, gson.toJson(account.login()));
      db.insert(TABLE, values);
    } catch (Exception e) {
      throw new DbException("创建[" + account.username() + "]失败，账号已存在");
    }
  }

  @WorkerThread private Observable<Account> registerOrLogin(Account account) {
    String username = account.username();
    String password = account.password();
    String alias = account.alias();
    return smith.getLogin(username, password)
        .flatMap(login -> "".equals(login.script()) ? Observable.just(login)
            : smith.getLogin(login.script()))
        .map(login -> Account.create(username, password, alias, login));
  }

  @WorkerThread private Boolean update(Account account) {
    try {
      ContentValues values = new ContentValues();
      values.put(Account.PASSWORD, Db.encode(account.password()));
      values.put(Account.ALIAS, account.alias());
      values.put(Account.LOGIN, gson.toJson(account.login()));
      int raw = db.update(Account.TABLE, values, Account.USERNAME + "=?", account.username());
      return raw > 0;
    } catch (Exception e) {
      throw new DbException("更新[" + account.username() + "]出错");
    }
  }

  @AnyThread public void delete(Account account, @NonNull Result<Boolean> result) {
    SUBSCRIPTIONS.add(Observable.just(account)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .map(this::delete)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @WorkerThread private Boolean delete(Account account) {
    try {
      int raw = db.delete(Account.TABLE, Account.USERNAME + "=?", account.username());
      return raw > 0;
    } catch (Exception e) {
      throw new DbException("删除[" + account.username() + "]出错");
    }
  }

  @AnyThread public void update(Account account, @NonNull Result<Boolean> result) {
    SUBSCRIPTIONS.add(Observable.just(account)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .map(this::update)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void queryList(Result<List<Account>> result) {
    SUBSCRIPTIONS.add(db.createQuery(TABLE, SELECT_ALL).mapToList(cursor -> {
      String username = Db.getString(cursor, USERNAME);
      String password = Db.decode(Db.getString(cursor, PASSWORD));
      String alias = Db.getString(cursor, ALIAS);
      Login login = gson.fromJson(Db.getString(cursor, LOGIN), Login.class);
      return Account.create(username, password, alias, login);
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(result));
  }

  @AnyThread public void cancelAll() {
    for (Subscription subscription : SUBSCRIPTIONS) {
      if (!subscription.isUnsubscribed()) {
        subscription.unsubscribe();
      }
    }
    SUBSCRIPTIONS.clear();
  }
}
