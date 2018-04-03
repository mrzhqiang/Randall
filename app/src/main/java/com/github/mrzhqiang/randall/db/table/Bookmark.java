package com.github.mrzhqiang.randall.db.table;

public final class Bookmark {
  public static final String TABLE = "bookmark";

  public static final String ACCOUNT_ID = "account_id";
  public static final String SERVER_ID = "server_id";
  public static final String DESCRIPTION = "description";

  public final long userId;
  public final long serverId;
  public final String description;

  public Bookmark(long userId, long serverId, String description) {
    this.userId = userId;
    this.serverId = serverId;
    this.description = description;
  }
}
