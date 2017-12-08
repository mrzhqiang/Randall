package com.github.mrzhqiang.smith.db;

import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.Date;
import rx.functions.Func1;

@AutoValue public abstract class Account implements Parcelable {
  public static final String TABLE = "account";

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String STATUS = "status";
  public static final String ALIAS = "alias";
  public static final String UPDATED = "updated";

  public abstract String username();

  public abstract String password();

  public abstract Status status();

  @Nullable public abstract String alias();

  public abstract Date updated();

  public static Account create(String username, String password, Status status,
      String alias, Date updated) {
    return builder().username(username)
        .password(password)
        .status(status)
        .alias(alias)
        .updated(updated)
        .build();
  }

  public static Builder builder() {
    return new AutoValue_Account.Builder();
  }

  public static Builder builder(Account account) {
    return builder().username(account.username())
        .password(account.password())
        .status(account.status())
        .alias(account.alias())
        .updated(account.updated());
  }

  public static TypeAdapter<Account> typeAdapter(Gson gson) {
    return new AutoValue_Account.GsonTypeAdapter(gson);
  }

  public static final Func1<Cursor, Account> MAP = cursor -> {
    String username = Db.getString(cursor, USERNAME);
    String password = Db.decode(Db.getString(cursor, PASSWORD));
    Status status = Status.values()[Db.getInt(cursor, STATUS)];
    String alias = Db.getString(cursor, ALIAS);
    Date updated = new Date(Db.getLong(cursor, UPDATED));
    return create(username, password, status, alias, updated);
  };

  public enum Status {
    DELETE("已删除"), INVALID("无效"), DEFAULT("未验证"), AVAILABLE("正常"),;

    final String value;

    Status(String value) {
      this.value = value;
    }

    @Override public String toString() {
      return value;
    }
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder username(String username);

    public abstract Builder password(String password);

    public abstract Builder status(Status status);

    public abstract Builder alias(String alias);

    public abstract Builder updated(Date updated);

    public abstract Account build();
  }
}
