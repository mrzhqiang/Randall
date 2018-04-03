package com.github.mrzhqiang.randall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import cn.mrzhqiang.logger.Log;
import com.github.mrzhqiang.randall.db.table.Bookmark;
import com.github.mrzhqiang.randall.db.table.Server;
import com.github.mrzhqiang.randall.db.table.Account;

public final class DbHelper extends SQLiteOpenHelper {
  private static final String TAG = "DbHelper";

  private static final String DB_NAME = "randall.db";
  private static final int VERSION = 1;

  private static final String PRIMARY_KEY_AUTO_ID = BaseColumns._ID + " int PRIMARY KEY AUTOINCREMENT, ";
  private static final String CREATED = "created";
  private static final String UPDATED = "updated";

  public DbHelper(@NonNull Context context) {
    super(context, DB_NAME, null, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    Log.i(TAG, "Creating database.");
    // 手动开启外键
    //db.execSQL("PRAGMA foreign_keys=ON;");

    // 服务器表
    db.execSQL("CREATE TABLE " + Server.TABLE + " ("
        + PRIMARY_KEY_AUTO_ID
        + Server.NAME + " varchar(8) NOT NULL UNIQUE, "
        + Server.GAME_ID + " varchar(4) NOT NULL, "
        + Server.GAME_URL + " varchar(36) NOT NULL, "
        + Server.COLOR + " varchar(2), "
        + UPDATED + " int NOT NULL, "
        + CREATED + " int NOT NULL)"
    );

    // 账号表
    db.execSQL("CREATE TABLE " + Account.TABLE + " ("
        + PRIMARY_KEY_AUTO_ID
        + Account.USERNAME + " varchar(16) NOT NULL UNIQUE, "
        + Account.PASSWORD + " varchar(16) NOT NULL, "
        + Account._UID + " int, "
        + Account.COOKIE + " text, "
        + UPDATED + " int NOT NULL, "
        + CREATED + " int NOT NULL)"
    );

    // 书签表
    db.execSQL("CREATE TABLE " + Bookmark.TABLE
        + PRIMARY_KEY_AUTO_ID
        + Bookmark.SERVER_ID + " int NOT NULL REFERENCES "
        + Server.TABLE + "(" + BaseColumns._ID + "), "
        + Bookmark.ACCOUNT_ID + " int NOT NULL REFERENCES "
        + Account.TABLE + "(" + BaseColumns._ID + "), "
        + Bookmark.DESCRIPTION + " text, "
        + UPDATED + " int NOT NULL, "
        + CREATED + " int NOT NULL)"
    );

    // 针对书签表建立索引
    db.execSQL("CREATE INDEX bookmark_server_id ON "
        + Bookmark.TABLE + " (" + Bookmark.SERVER_ID + ")"
    );
    db.execSQL("CREATE INDEX bookmark_user_id ON "
        + Bookmark.TABLE + " (" + Bookmark.ACCOUNT_ID + ")"
    );
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i(TAG, "Upgrading schema from " + oldVersion + " to " + newVersion);
    // TODO 升级语句
  }
}
