package com.github.mrzhqiang.smith.model;

import android.content.ContentValues;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.github.mrzhqiang.smith.BaseApp;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.db.Db;
import com.github.mrzhqiang.smith.db.DbException;
import com.github.mrzhqiang.smith.net.Login;
import com.github.mrzhqiang.smith.net.Result;
import com.github.mrzhqiang.smith.net.Smith;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.github.mrzhqiang.smith.db.Account.*;
import static com.github.mrzhqiang.smith.db.Account.Status.AVAILABLE;
import static com.github.mrzhqiang.smith.db.Account.Status.INVALID;

public final class AccountModel {
  private static final String TAG = "AccountModel";

  private static final String SELECT_ALL = "SELECT * FROM " + TABLE;

  @Inject BriteDatabase db;
  @Inject Smith smith;

  private final Set<Subscription> subscriptions = new HashSet<>();

  public AccountModel() {
    BaseApp.appComponent().inject(this);
  }

  @AnyThread public void create(Account account, @NonNull Result<Login> result) {
    subscriptions.add(Observable.just(account)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnNext(this::insertIfNewAccount)
        .flatMap(this::registerOrLogin)
        .doOnNext(this::updateByLogin)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void create(List<Account> accounts, @NonNull Result<List<Login>> result) {
    subscriptions.add(Observable.from(accounts)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnNext(this::insertIfNewAccount)
        .flatMap(this::registerOrLogin)
        .toList()
        .doOnNext(logins -> {
          BriteDatabase.Transaction transaction = db.newTransaction();
          try {
            for (Login login : logins) {
              updateByLogin(login);
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
    subscriptions.add(db.createQuery(TABLE, SELECT_ALL + " ORDER BY " + UPDATED + " DESC")
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
      values.put(Account.STATUS, account.status().ordinal());
      values.put(Account.ALIAS, account.alias());
      values.put(Account.UPDATED, account.updated().getTime());
      db.insert(TABLE, values);
    } catch (Exception e) {
      throw new DbException("创建[" + account.username() + "]失败");
    }
  }

  @WorkerThread private Observable<Login> registerOrLogin(Account account) {
    return smith.getLogin(account.username(), account.password())
        .flatMap(login -> login.script() != null ? smith.getLogin(login.script())
            : Observable.just(login))
        .map(login -> {
          Builder builder = Account.builder(account);
          if (login.lastGame() == null) {
            builder.status(INVALID);
          } else {
            builder.status(AVAILABLE);
          }
          return Login.builder()
              .title(login.title())
              .lastGame(login.lastGame())
              .listGame(login.listGame())
              .script(login.script())
              .account(builder.updated(new Date()).build())
              .build();
        });
  }

  @WorkerThread private void updateByLogin(Login login) {
    Account a = login.account();
    if (a == null) return;
    ContentValues values = new ContentValues();
    values.put(STATUS, a.status().ordinal());
    values.put(ALIAS, "兰达尔-" + a.hashCode());
    values.put(UPDATED, a.updated().getTime());
    try {
      db.update(TABLE, values, USERNAME + "=?", a.username());
    } catch (Exception e) {
      Log.e(TAG, "更新账号[" + a.username() + "]出现异常：" + e.getMessage());
    }
  }
}
