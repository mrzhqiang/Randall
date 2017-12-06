package com.github.mrzhqiang.smith.model;

import android.database.Cursor;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.github.mrzhqiang.smith.BaseApp;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.net.Login;
import com.github.mrzhqiang.smith.net.Result;
import com.github.mrzhqiang.smith.net.Smith;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.github.mrzhqiang.smith.db.Account.Status.AVAILABLE;
import static com.github.mrzhqiang.smith.db.Account.Status.INVALID;

/**
 * 账户模块，包含所有与之相关的网络/本地逻辑
 *
 * @author mrZQ
 */
public final class AccountModel {
  private static final String TAG = "AccountModel";

  @Inject BriteDatabase db;
  @Inject Smith smith;

  private final CompositeSubscription subscription = new CompositeSubscription();

  public AccountModel() {
    BaseApp.appComponent().inject(this);
  }

  @AnyThread public void create(Account account, @NonNull Result<Login> result) {
    subscription.add(Observable.just(account)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnNext(this::newAdd)
        .flatMap(this::loginOrRegister)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void create(List<Account> accounts, @NonNull Result<List<Login>> result) {
    subscription.add(Observable.from(accounts)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnNext(this::newAdd)
        .flatMap(this::loginOrRegister)
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void queryList(Result<List<Account>> result) {
    subscription.add(db.createQuery(Account.TABLE, Account.QUERY_LIST)
        .mapToList(Account.MAPPER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void cancelAll() {
    subscription.unsubscribe();
  }

  @WorkerThread private void newAdd(Account account) {
    if (checkExists(account.username())) {
      Log.w(TAG, "账号已存在：" + account);
      return;
    }
    try {
      db.insert(Account.TABLE, new Account.Builder().username(account.username())
          .password(account.password())
          .status(account.status())
          .build());
    } catch (Exception ignore) {
      Log.w(TAG, "ignore [" + account.username() + "] when new add");
    }
  }

  @WorkerThread private boolean checkExists(String username) {
    try {
      Cursor cursor = db.query(Account.QUERY_LIST + " WHERE " + Account.USERNAME + "=?", username);
      if (cursor != null && cursor.moveToNext()) {
        cursor.close();
        return true;
      }
    } catch (Exception ignore) {
      Log.w(TAG, "ignore [" + username + "] when check exists.");
    }
    return false;
  }

  @WorkerThread private Observable<Login> loginOrRegister(Account account) {
    return smith.getLogin(account.username(), account.password()).flatMap(login -> {
      Observable<Login> observable = Observable.just(login);
      if (login.lastGame() == null) {
        observable = smith.getRegister(account.username(), account.password());
      }
      return observable.doOnNext(login1 -> updateByLogin(account, login1));
    });
  }

  @WorkerThread private void updateByLogin(Account account, Login login) {
    Account.Builder builder = new Account.Builder().password(account.password()).status(AVAILABLE);
    if (login.lastGame() == null) {
      builder.status(INVALID);
    }
    try {
      db.update(Account.TABLE, builder.build(), Account.USERNAME + "=?", account.username());
    } catch (Exception ignore) {
      Log.w(TAG, "ignore [" + account.username() + "] when update by login");
    }
  }
}
