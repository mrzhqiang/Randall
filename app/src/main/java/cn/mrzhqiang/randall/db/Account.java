package cn.mrzhqiang.randall.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import rx.functions.Func1;

/**
 * 账户表及数据
 *
 * @author mrZQ
 */
@AutoValue public abstract class Account implements Parcelable {
  public static final String TABLE = "account";

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String STATUS = "status";

  public abstract String username();
  public abstract String password();
  public abstract Status status();

  public static Account create(String username, String password) {
    return new AutoValue_Account(username, password, Status.DEFAULT);
  }

  public static final Func1<Cursor, Account> MAPPER = new Func1<Cursor, Account>() {
    @Override public Account call(Cursor cursor) {
      String username = Db.decode(Db.getString(cursor, USERNAME));
      String password = Db.decode(Db.getString(cursor, PASSWORD));
      Account.Status status = Status.values()[Db.getInt(cursor, STATUS)];
      return new AutoValue_Account(username, password, status);
    }
  };

  public enum Status {
    DELETE("已删除"), INVALID("无效"), DEFAULT("未认证"), AVAILABLE("正常"),;

    final String value;

    Status(String value) {
      this.value = value;
    }

    @Override public String toString() {
      return value;
    }
  }

  public static final class Builder {
    private final ContentValues values = new ContentValues();

    public Builder(Account account) {
      values.put(USERNAME, Db.encode(account.username()));
      password(account.password());
      status(account.status());
    }

    public Builder password(@NonNull String password) {
      values.put(PASSWORD, Db.encode(password));
      return this;
    }

    public Builder status(Account.Status status) {
      values.put(STATUS, status.ordinal());
      return this;
    }

    public ContentValues build() {
      return values;
    }
  }
}
