package com.github.mrzhqiang.smith.db;

import android.os.Parcelable;
import com.github.mrzhqiang.smith.net.Login;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Account implements Parcelable {
  public static final String TABLE = "account";

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String ALIAS = "alias";
  public static final String LOGIN = "login";

  public abstract String username();

  public abstract String password();

  public abstract String alias();

  public abstract Login login();

  public String showName() {
    if ("".equals(alias())) {
      return username();
    }
    return alias() + "(" + username() + ")";
  }

  public static Account create(String username, String password, String alias, Login login) {
    return builder().username(username).password(password).alias(alias).login(login).build();
  }

  public static Builder builder() {
    return new AutoValue_Account.Builder();
  }

  public static Builder builder(Account account) {
    return new AutoValue_Account.Builder().username(account.username())
        .password(account.password())
        .alias(account.alias())
        .login(account.login());
  }

  @AutoValue.Builder public abstract static class Builder {

    public abstract Builder username(String username);

    public abstract Builder password(String password);

    public abstract Builder alias(String alias);

    public abstract Builder login(Login login);

    public abstract Account build();
  }
}
