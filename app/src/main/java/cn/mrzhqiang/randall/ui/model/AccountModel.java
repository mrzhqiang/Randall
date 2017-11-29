package cn.mrzhqiang.randall.ui.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import cn.mrzhqiang.randall.RandallApp;
import cn.mrzhqiang.randall.db.Account;
import cn.mrzhqiang.randall.db.Db;
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
  }

  /** 从本地删除一个账户，这里应该是一个延迟操作，可以撤销 */
  public void deleteAccount(long id, @NonNull Subscriber<Integer> subscriber) {
  }

  /** 更换账户密码 */
  public void changePassword(final long id, @NonNull String password,
      @NonNull Subscriber<Integer> subscriber) {
  }

  /** 更新账户状态 */
  public void updateStatus(final long id, Account.Status status,
      @NonNull Subscriber<Integer> subscriber) {
  }

  /** 查询数据库是否存在账户列表，将以订阅的方式保持监听，一旦账户数量清零，则应该执行对应操作 */
  public void queryList(Subscriber<List<Account>> subscriber) {
  }

  /** 取消这个主题订阅，应该在onPause中调用 */
  public void cancel() {
    subscription.unsubscribe();
  }

  /** 检测账号是否已存在 */
  private boolean checkExists(String mUsername) {
    return false;
  }

  /** 生成随机密码的随机位数：6--15 */
  public int generatePasswordSize() {
    return (int) (6 + Math.random() * 10);
  }
}
