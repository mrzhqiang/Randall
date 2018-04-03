package com.github.mrzhqiang.randall.db.table;

public final class Account {
  public static final String TABLE = "account";

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String _UID = "_uid";
  public static final String COOKIE = "cookie";

  public final String username;
  public final String password;
  public final long uid;
  public final String cookie;

  public Account(String username, String password, long uid, String cookie) {
    this.username = username;
    this.password = password;
    this.uid = uid;
    this.cookie = cookie;
  }
}
