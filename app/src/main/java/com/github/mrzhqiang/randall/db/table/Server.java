package com.github.mrzhqiang.randall.db.table;

public final class Server {
  public static final String TABLE = "server";

  public static final String NAME = "name";
  public static final String GAME_ID = "game_id";
  public static final String GAME_URL = "game_url";
  public static final String COLOR = "color";

  public final String name;
  public final String gameId;
  public final String gameURL;
  public final String color;

  public Server(String name, String gameId, String gameURL, String color) {
    this.name = name;
    this.gameId = gameId;
    this.gameURL = gameURL;
    this.color = color;
  }
}
