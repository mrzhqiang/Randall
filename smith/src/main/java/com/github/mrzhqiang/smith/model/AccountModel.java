package com.github.mrzhqiang.smith.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import cn.mrzhqiang.helper.AccountHelper;
import com.github.mrzhqiang.smith.BaseApp;
import com.github.mrzhqiang.smith.db.Account;
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

  public void addAccount(Account account, @NonNull Result<Boolean> result) {
    subscription.add(
        Observable.just(account).subscribeOn(Schedulers.io()).map(new Func1<Account, Boolean>() {
          @Override public Boolean call(Account account) {
            Account.Builder builder = new Account.Builder(account);
            return db.insert(Account.TABLE, builder.build()) > 0;
          }
        }).subscribe(result));
  }

  public void deleteAccount(Account account, @NonNull Result<Boolean> result) {
    subscription.add(Observable.just(account).map(new Func1<Account, Boolean>() {
      @Override public Boolean call(Account account) {
        return db.delete(Account.TABLE, Account.USERNAME + "=?", account.username()) > 0;
      }
    }).subscribe(result));
  }

  public void updateAccount(Account account, @NonNull Result<Boolean> result) {
    subscription.add(Observable.just(account).map(new Func1<Account, Boolean>() {
      @Override public Boolean call(Account account) {
        Account.Builder builder =
            new Account.Builder().password(account.password()).status(account.status());
        return db.update(Account.TABLE, builder.build(), Account.USERNAME + "=?",
            account.username()) > 0;
      }
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

  public String generatePassword(int length) {
    return AccountHelper.createPassword(length);
  }
}
