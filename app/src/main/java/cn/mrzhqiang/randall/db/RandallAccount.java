package cn.mrzhqiang.randall.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import rx.functions.Func1;

/**
 * 账户：用户名、密码以及状态，包括数据库相关
 */
@AutoValue abstract public class RandallAccount implements Parcelable {
  ///////////////////////////////////////////////////////////////////////////
  // 账户相关
  ///////////////////////////////////////////////////////////////////////////

  /** 账号，目前只支持地狱之门，因此作为主键 */
  abstract public String username();

  /** 密码 */
  abstract public String password();

  /** 状态：已删除、无效、默认、离线、在线 */
  abstract public Status status();

  public static RandallAccount create(String username, String password, Status status) {
    return new AutoValue_RandallAccount(username, password, status);
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
  public static final Func1<Cursor, RandallAccount> MAPPER = new Func1<Cursor, RandallAccount>() {
    @Override public RandallAccount call(Cursor cursor) {
      String username = Db.decode(Db.getString(cursor, RandallAccount.COL_USERNAME));
      String password = Db.decode(Db.getString(cursor, RandallAccount.COL_PASSWORD));
      RandallAccount.Status status = Status.values()[Db.getInt(cursor, RandallAccount.COL_STATUS)];
      return new AutoValue_RandallAccount(username, password, status);
    }
  };

  /** 更新账户信息的构造器 */
  public static final class Builder {
    private final ContentValues values = new ContentValues();

    public Builder() {
    }

    public Builder(RandallAccount account) {
      values.put(COL_USERNAME, Db.encode(account.username()));
      password(account.password());
      status(account.status());
    }

    public Builder password(@NonNull String password) {
      values.put(COL_PASSWORD, Db.encode(password));
      return this;
    }

    public Builder status(RandallAccount.Status status) {
      values.put(COL_STATUS, status.ordinal());
      return this;
    }

    public ContentValues build() {
      values.put(COL_UPDATED, System.currentTimeMillis());
      return values;
    }
  }
}
