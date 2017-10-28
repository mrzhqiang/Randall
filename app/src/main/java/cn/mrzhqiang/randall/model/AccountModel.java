package cn.mrzhqiang.randall.model;

import android.database.Cursor;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import cn.mrzhqiang.randall.RandallApp;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.db.Db;
import cn.mrzhqiang.randall.db.table.AccountTable;
import cn.mrzhqiang.randall.net.Randall;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 账户模块，包含所有与之相关的网络/本地逻辑
 */
public final class AccountModel {
  private static final String TAG = "AccountModel";

  private static final String QUERY_ACCOUNT_LIST = "SELECT * FROM "
      + AccountTable.NAME
      + " WHERE "
      + AccountTable.COL_STATUS
      + " >?"
      + " ORDER BY "
      + AccountTable.COL_CREATED
      + " DESC";
  private static final String QUERY_USERNAME =
      "SELECT * FROM " + AccountTable.NAME + " WHERE " + AccountTable.COL_USERNAME + " = ?";

  @Inject Randall randall;
  @Inject BriteDatabase db;

  private final CompositeSubscription subscription = new CompositeSubscription();

  public AccountModel() {
    RandallApp.appComponent().inject(this);
  }

  // 网络相关的有，验证账户、登录账户/角色，等等。

  /** 添加账户到本地 */
  public void addAccount(@NonNull Account account, @NonNull Subscriber<Long> subscriber) {
    Observable.just(account).subscribeOn(Schedulers.io()).map(new Func1<Account, Long>() {
      @Override public Long call(Account account) {
        if (checkExists(account.username)) {
          throw new RuntimeException("账号已经存在");
        }
        // TODO 做一次网络验证，以确定status
        return db.insert(AccountTable.NAME, AccountTable.toContentValues(account));
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
  }

  /** 从本地删除一个账户，这里应该是一个延迟操作，可以撤销 */
  public void deleteAccount(long id, @NonNull Subscriber<Integer> subscriber) {
    Observable.just(id).subscribeOn(Schedulers.io()).map(new Func1<Long, Integer>() {
      @Override public Integer call(Long id) {
        return db.delete(AccountTable.NAME, AccountTable.COL_ID + "=?", String.valueOf(id));
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
  }

  /** 更新账号的相关信息，*/
  public void updateAccount(final long id, @NonNull AccountTable.Builder builder,
      @NonNull Subscriber<Integer> subscriber) {
    Observable.just(builder)
        .subscribeOn(Schedulers.io())
        .map(new Func1<AccountTable.Builder, Integer>() {
          @Override public Integer call(AccountTable.Builder builder) {
            return db.update(AccountTable.NAME, builder.build(), AccountTable.COL_ID + "=?",
                String.valueOf(id));
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  /** 查询数据库是否存在账户列表，将以订阅的方式保持监听，一旦账户数量清零，则应该执行对应操作 */
  public void queryAccountList(Subscriber<List<Account>> subscriber) {
    String delete = String.valueOf(Account.Status.DELETE.ordinal());
    subscription.add(db.createQuery(AccountTable.NAME, QUERY_ACCOUNT_LIST, delete)
        .mapToList(AccountTable.MAPPER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber));
  }

  /** 取消这个主题订阅，应该在onPause中调用 */
  public void cancelSubscriber() {
    subscription.unsubscribe();
  }

  /** 检测账号是否已存在 */
  @WorkerThread @CheckResult private boolean checkExists(String mUsername) {
    try {
      Cursor cursor = db.query(QUERY_USERNAME, Db.encode(mUsername));
      if (cursor != null) {
        try {
          if (cursor.getCount() > 0) {
            return true;
          }
        } finally {
          cursor.close();
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "检查账号出错：" + e.getMessage());
    }
    return false;
  }

}
