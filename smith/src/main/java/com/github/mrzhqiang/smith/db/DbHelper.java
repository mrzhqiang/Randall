package com.github.mrzhqiang.smith.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import cn.mrzhqiang.logger.Log;

import static com.github.mrzhqiang.smith.db.Account.*;

final class DbHelper extends SQLiteOpenHelper {
  private static final String TAG = "DbHelper";

  private static final String DB_NAME = "smith.db";
  private static final int VERSION = 1;

  private static final String CREATE_ACCOUNT = "CREATE TABLE "
      + TABLE
      + " ("
      + USERNAME
      + " char(15) NOT NULL PRIMARY KEY,"
      + PASSWORD
      + " char(15) NOT NULL,"
      + STATUS
      + " INTEGER NOT NULL DEFAULT 0,"
      + ALIAS
      + " char(20),"
      + UPDATED
      + " INTEGER NOT NULL DEFAULT 0"
      + ")";

  DbHelper(@NonNull Context context) {
    super(context, DB_NAME, null /* factory */, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    Log.i(TAG, "Creating database");
    // 开启外键功能
    //db.execSQL("PRAGMA foreign_keys=ON;");

    db.execSQL(CREATE_ACCOUNT);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i(TAG, "Upgrading schema from " + oldVersion + " to " + newVersion);
  }
}