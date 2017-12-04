package com.github.mrzhqiang.smith.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.github.mrzhqiang.smith.BaseApp;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.db.DbException;
import com.github.mrzhqiang.smith.net.Result;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 账户模块，包含所有与之相关的网络/本地逻辑
 *
 * @author mrZQ
 */
public final class AccountModel {
  private static final String TAG = "AccountModel";

  @Inject Retrofit net;
  @Inject BriteDatabase db;

  private final CompositeSubscription subscription = new CompositeSubscription();

  public AccountModel() {
    BaseApp.appComponent().inject(this);
  }

  public void addAccount(String username, String password, @NonNull Result<Account> result) {
    Account account = Account.create(username, password, Account.Status.DEFAULT);
    subscription.add(Observable.just(account)
        // 在IO线程上执行
        .subscribeOn(Schedulers.io())
        // 优先添加到本地数据库
        .doOnNext(account1 -> {
          long id = db.insert(Account.TABLE, new Account.Builder(account1).build());
          // 只有成功的插入才会通知其他订阅者所以这里抛出异常即可
          if (id == -1) {
            throw new DbException("新建账号失败");
          }
        }).flatMap((Func1<Account, Observable<Account>>) account12 -> {
          // TODO 基于给定的Account实例，去验证这个账户，如果不存在，直接注册；如果存在，再看结果
          return null;
        }).subscribe(result));
  }

  public void deleteAccount(Account account, @NonNull Result<Boolean> result) {
    subscription.add(Observable.just(account)
        .map(account1 -> db.delete(Account.TABLE, Account.USERNAME + "=?", account1.username()) > 0)
        .subscribe(result));
  }

  public void updateAccount(Account account, @NonNull Result<Boolean> result) {
    subscription.add(Observable.just(account).map(account1 -> {
      Account.Builder builder =
          new Account.Builder().password(account1.password()).status(account1.status());
      return db.update(Account.TABLE, builder.build(), Account.USERNAME + "=?", account1.username())
          > 0;
    }).subscribe(result));
  }

  public void queryList(Result<List<Account>> result) {
    subscription.add(db.createQuery(Account.TABLE, Account.QUERY_LIST)
        .mapToList(Account.MAPPER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result));
  }

  public void cancelAll() {
    subscription.unsubscribe();
  }

  @WorkerThread public boolean checkExists(String username) {
    try {
      Cursor cursor = db.query(Account.QUERY_LIST + " WHERE " + Account.USERNAME + "=?", username);
      if (cursor != null && cursor.moveToNext()) {
        cursor.close();
        return true;
      }
    } catch (Exception ignore) {
      Log.w(TAG, "Ignore a checkExists exception");
    }
    return false;
  }
}
