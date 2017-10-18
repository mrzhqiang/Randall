package cn.mrzhqiang.randall.model;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 账户模块，包含所有与之相关的网络/本地逻辑
 */
public final class AccountModel {

  private static final String QUERY_ACCOUNT_LIST = "SELECT * FROM "
      + AccountTable.NAME
      + " WHERE "
      + AccountTable.COL_IS_AVAILABLE
      + " =?"
      + " ORDER BY "
      + AccountTable.COL_CREATED
      + " DESC";

  @Inject Randall randall;
  @Inject BriteDatabase db;

  public AccountModel() {
    RandallApp.appComponent().inject(this);
  }

  // 网络相关的有，验证账户、登录账户/角色，等等。

  /** 添加账户到本地，没有做网络验证 */
  public void addAccount(@NonNull Account account, @NonNull Action1<Boolean> action1) {
    Observable.just(account).subscribeOn(Schedulers.io()).map(new Func1<Account, Boolean>() {
      @Override public Boolean call(Account account) {
        long id = db.insert(AccountTable.NAME, AccountTable.toContentValues(account));
        if (id > 0) {
          account.id = id;
          return true;
        }
        return false;
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(action1);
  }

  /** 从本地删除一个账户，这里应该是一个延迟操作，可以撤销 */
  public void deleteAccount(@NonNull Account account, @NonNull Action1<Boolean> action1) {
    Observable.just(account).subscribeOn(Schedulers.io()).map(new Func1<Account, Boolean>() {
      @Override public Boolean call(Account account) {
        if (account.id > 0) {
          int row =
              db.delete(AccountTable.NAME, AccountTable.COL_ID + "=?", String.valueOf(account.id));
          if (row > 0) {
            return true;
          }
        }
        return false;
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(action1);
  }

  public void updateAccount(@NonNull Account account, @NonNull Action1<Boolean> action1) {
    Observable.just(account).subscribeOn(Schedulers.io()).map(new Func1<Account, Boolean>() {
      @Override public Boolean call(Account account) {
        if (account.id > 0) {
          long row = db.update(AccountTable.NAME,
              new AccountTable.Builder().password(account.password)
                  .alias(account.alias)
                  .updated(account.updated)
                  .status(account.status)
                  .build(), AccountTable.COL_ID + "=?", String.valueOf(account.id));
          return row == 1;
        }
        return false;
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(action1);
  }

  /** 以订阅的方式查询数据库是否存在账户列表 */
  @CheckResult
  public Subscription queryAccountList(Action1<List<Account>> action1) {
    String isAvailable = String.valueOf(Db.BOOLEAN_TRUE);
    return db.createQuery(AccountTable.NAME, QUERY_ACCOUNT_LIST, isAvailable)
        .mapToList(AccountTable.MAPPER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(action1);
  }
}
