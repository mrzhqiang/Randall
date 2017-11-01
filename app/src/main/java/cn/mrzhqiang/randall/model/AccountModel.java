package cn.mrzhqiang.randall.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import cn.mrzhqiang.randall.RandallApp;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.data.LoginPage;
import cn.mrzhqiang.randall.data.RegisterPage;
import cn.mrzhqiang.randall.db.Db;
import cn.mrzhqiang.randall.net.Randall;
import com.squareup.sqlbrite.BriteDatabase;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import retrofit2.Response;
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

  private static final String QUERY_LIST = "SELECT * FROM "
      + Account.NAME
      + " WHERE "
      + Account.COL_STATUS
      + " >? "
      + " ORDER BY "
      + Account.COL_UPDATED
      + " DESC";
  private static final String QUERY_USERNAME =
      "SELECT * FROM " + Account.NAME + " WHERE " + Account.COL_USERNAME + " = ?";

  @Inject Randall randall;
  @Inject BriteDatabase db;

  private final CompositeSubscription subscription = new CompositeSubscription();

  public AccountModel() {
    RandallApp.appComponent().inject(this);
  }

  // 网络相关的有，验证账户、登录账户/角色，等等。

  /** 添加账户到本地 */
  public void addAccount(@NonNull String username, @NonNull String password,
      @NonNull Subscriber<Long> subscriber) {
    Observable.just(Account.create(username, password))
        .subscribeOn(Schedulers.io())
        .map(new Func1<Account, Long>() {
          @Override public Long call(Account account) {
            if (checkExists(account.username())) {
              throw new RuntimeException("账号已经存在");
            }

            return db.insert(Account.NAME, new Account.Builder(account).build());
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  /** 从本地删除一个账户，这里应该是一个延迟操作，可以撤销 */
  public void deleteAccount(long id, @NonNull Subscriber<Integer> subscriber) {
    Observable.just(String.valueOf(id))
        .subscribeOn(Schedulers.io())
        .map(new Func1<String, Integer>() {
          @Override public Integer call(String id) {
            return db.delete(Account.NAME, Account.COL_ID + "=?", id);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  /** 更换账户密码 */
  public void changePassword(final long id, @NonNull String password,
      @NonNull Subscriber<Integer> subscriber) {
    Observable.just(password).subscribeOn(Schedulers.io()).map(new Func1<String, Integer>() {
      @Override public Integer call(String password) {
        return db.update(Account.NAME, new Account.Builder().password(password).build(),
            Account.COL_ID + "=?", String.valueOf(id));
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
  }

  /** 更新账户状态 */
  public void updateStatus(final long id, Account.Status status,
      @NonNull Subscriber<Integer> subscriber) {
    Observable.just(status).subscribeOn(Schedulers.io()).map(new Func1<Account.Status, Integer>() {
      @Override public Integer call(Account.Status status) {
        return db.update(Account.NAME, new Account.Builder().status(status).build(),
            Account.COL_ID + "=?", String.valueOf(id));
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
  }

  /** 查询数据库是否存在账户列表，将以订阅的方式保持监听，一旦账户数量清零，则应该执行对应操作 */
  public void queryList(Subscriber<List<Account>> subscriber) {
    String delete = String.valueOf(Account.Status.DELETE.ordinal());
    subscription.add(db.createQuery(Account.NAME, QUERY_LIST, delete)
        .mapToList(Account.MAPPER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber));
  }

  /** 取消这个主题订阅，应该在onPause中调用 */
  public void cancel() {
    subscription.unsubscribe();
  }

  /** 检测账号是否已存在 */
  private boolean checkExists(String mUsername) {
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

  /** 生成随机密码的随机位数：6--15 */
  public int generatePasswordSize() {
    return (int) (6 + Math.random() * 10);
  }
}
