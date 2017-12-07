package com.github.mrzhqiang.smith.model;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

  private final Set<Subscription> subscriptions = new HashSet<>();

  public AccountModel() {
    BaseApp.appComponent().inject(this);
  }

  @AnyThread public void create(Account account, @NonNull Result<Login> result) {
    subscriptions.add(Observable.just(account)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnNext(this::newAdd)
        .flatMap(this::loginOrRegister)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void create(List<Account> accounts, @NonNull Result<List<Login>> result) {
    subscriptions.add(Observable.from(accounts)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .doOnNext(this::newAdd)
        .flatMap(this::loginOrRegister)
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  @AnyThread public void queryList(Result<List<Account>> result) {
    subscriptions.add(db.createQuery(Account.TABLE, Account.QUERY_LIST)
        .mapToList(Account.MAPPER)
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

  @WorkerThread private void newAdd(Account account) {
    /*if (checkExists(account.username())) {
      Log.w(TAG, "账号已存在：" + account);
      return;
    }*/
    try {
      db.insert(Account.TABLE, new Account.Builder().username(account.username())
          .password(account.password())
          .status(account.status())
          .build());
    } catch (Exception ignore) {
      Log.w(TAG, "ignore [" + account.username() + "] when new add");
    }
  }

  /*@WorkerThread private boolean checkExists(String username) {
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
  }*/

  @WorkerThread private Observable<Login> loginOrRegister(Account account) {
    return smith.getLogin(account.username(), account.password())
        .flatMap(this::scriptLogin)
        // 批量创建的话，延迟500ms不过分
        .delay(500, TimeUnit.MILLISECONDS)
        .map(login1 -> Login.builder(login1).account(account).build())
        // 间隔300ms让数据库轻松点
        .delay(300, TimeUnit.MILLISECONDS)
        .doOnNext(this::updateByLogin);
  }

  @WorkerThread private Observable<Login> scriptLogin(Login login) {
    Observable<Login> observable = Observable.just(login);
    if (login.script() != null) {
      observable = smith.getLogin(login.script());
    }
    return observable;
  }

  @WorkerThread private void updateByLogin(Login login) {
    Account account = login.account();
    if (account == null) return;
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
