package com.github.mrzhqiang.smith.model;

import android.content.ContentValues;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import cn.mrzhqiang.helper.AccountHelper;
import com.github.mrzhqiang.smith.BaseApp;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.db.Db;
import com.github.mrzhqiang.smith.db.DbException;
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
  private static final String TAG = "AccountModel";

  private static final String SELECT_ALL = "SELECT * FROM " + TABLE;

  @Inject BriteDatabase db;
  @Inject Smith smith;
  @Inject Gson gson;

  private final Set<Subscription> subscriptions = new HashSet<>();

  public AccountModel() {
    BaseApp.appComponent().inject(this);
  }

  @AnyThread public void create(Account account, @NonNull Result<Account> result) {
    subscriptions.add(Observable.just(account)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnNext(this::insertIfNewAccount)
        .flatMap(this::registerOrLogin)
        .doOnNext(this::updateByLogin)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread
  public void create(Account account, int start, int count, @NonNull Result<List<Account>> result) {
    subscriptions.add(Observable.from(AccountHelper.autoUsernames(account.username(), start, count))
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .map(name -> Account.create(name, account.password(), null))
        .doOnNext(this::insertIfNewAccount)
        .flatMap(this::registerOrLogin)
        .toList()
        .doOnNext(logins -> {
          BriteDatabase.Transaction transaction = db.newTransaction();
          try {
            for (Account a : logins) {
              updateByLogin(a);
            }
            transaction.markSuccessful();
          } finally {
            transaction.end();
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void queryList(Result<List<Account>> result) {
    subscriptions.add(db.createQuery(TABLE, SELECT_ALL)
        .mapToList(MAP)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void cancelAll() {
    for (Subscription subscription : subscriptions) {
      if (!subscription.isUnsubscribed()) {
        subscription.unsubscribe();
      }
    }
    subscriptions.clear();
  }

  @WorkerThread private void insertIfNewAccount(Account account) {
    try {
      ContentValues values = new ContentValues();
      values.put(Account.USERNAME, account.username());
      values.put(Account.PASSWORD, Db.encode(account.password()));
      db.insert(TABLE, values);
    } catch (Exception e) {
      throw new DbException("创建[" + account.username() + "]失败，账号已存在");
    }
  }

  @WorkerThread private Observable<Account> registerOrLogin(Account account) {
    String username = account.username();
    String password = account.password();
    return smith.getLogin(username, password)
        .flatMap(login -> login.script() != null ? smith.getLogin(login.script())
            : Observable.just(login))
        .map(login -> Account.create(username, password, gson.toJson(login)));
  }

  @WorkerThread private void updateByLogin(Account account) {
    ContentValues values = new ContentValues();
    values.put(Account.DATA, account.data());
    try {
      db.update(Account.TABLE, values, Account.USERNAME + "=?", account.username());
    } catch (Exception e) {
      Log.e(TAG, "更新[" + account.username() + "]的数据字段出错：" + e.getMessage());
    }
  }
}
