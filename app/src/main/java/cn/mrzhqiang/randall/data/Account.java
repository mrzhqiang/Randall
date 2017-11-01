package cn.mrzhqiang.randall.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import cn.mrzhqiang.randall.db.Db;
import com.google.auto.value.AutoValue;
import rx.functions.Func1;

/**
 * 账户：用户名、密码以及状态，包括数据库相关
 */
@AutoValue abstract public class Account implements Parcelable {
  ///////////////////////////////////////////////////////////////////////////
  // 账户类相关
  ///////////////////////////////////////////////////////////////////////////

  /** 数据库id */
  abstract public long id();

  /** 账号 */
  abstract public String username();

  /** 密码 */
  abstract public String password();

  /** 状态：已删除、无效、默认、离线、在线 */
  abstract public Status status();

  /** 创建新账户 */
  public static Account create(String username, String password) {
    return new AutoValue_Account(-1, username, password, Status.DEFAULT);
  }

  /** 状态枚举 */
  public enum Status {
    DELETE("已删除"), INVALID("无效"), DEFAULT("未认证"), OFFLINE("离线"), ONLINE("在线"),;

    final String value;

    Status(String value) {
      this.value = value;
    }

    @Override public String toString() {
      return value;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // 数据库相关
  ///////////////////////////////////////////////////////////////////////////
  /** 表名字 */
  public static final String NAME = "account";

  /** 账户ID */
  public static final String COL_ID = "_id";
  /** 账号 */
  public static final String COL_USERNAME = "username";
  /** 密码 */
  public static final String COL_PASSWORD = "password";
  /** 状态 */
  public static final String COL_STATUS = "status";
  /** 更新时间 */
  public static final String COL_UPDATED = "updated";

  /** 提供给BriteDatabase映射字段使用 */
  public static final Func1<Cursor, Account> MAPPER = new Func1<Cursor, Account>() {
    @Override public Account call(Cursor cursor) {
      long id = Db.getLong(cursor, Account.COL_ID);
      String username = Db.decode(Db.getString(cursor, Account.COL_USERNAME));
      String password = Db.decode(Db.getString(cursor, Account.COL_PASSWORD));
      Account.Status status = Status.values()[Db.getInt(cursor, Account.COL_STATUS)];
      return new AutoValue_Account(id, username, password, status);
    }
  };

  /** 更新账户信息的构造器 */
  public static final class Builder {
    private final ContentValues values = new ContentValues();

    public Builder() {
    }

    public Builder(Account account) {
      values.put(COL_USERNAME, Db.encode(account.username()));
      password(account.password());
      status(account.status());
    }

    public Builder password(@NonNull String password) {
      values.put(COL_PASSWORD, Db.encode(password));
      return this;
    }

    public Builder status(Account.Status status) {
      values.put(COL_STATUS, status.ordinal());
      return this;
    }

    public ContentValues build() {
      values.put(COL_UPDATED, System.currentTimeMillis());
      return values;
    }
  }
}
