package com.github.mrzhqiang.smith.db;

import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import rx.functions.Func1;

@AutoValue public abstract class Account implements Parcelable {
  public static final String TABLE = "account";

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String DATA = "data";

  public abstract String username();

  public abstract String password();

  @Nullable public abstract String data();

  public static Account create(String username, String password, String data) {
    return new AutoValue_Account(username, password, data);
  }

  public static final Func1<Cursor, Account> MAP = cursor -> {
    String username = Db.getString(cursor, USERNAME);
    String password = Db.decode(Db.getString(cursor, PASSWORD));
    String data = Db.getString(cursor, DATA);
    return create(username, password, data);
  };
}
