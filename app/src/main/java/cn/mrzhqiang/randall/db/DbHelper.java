package cn.mrzhqiang.randall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import cn.mrzhqiang.logger.Log;
import cn.mrzhqiang.randall.data.Account;

/**
 * 数据库辅助工具
 */
public final class DbHelper extends SQLiteOpenHelper {
  private static final String TAG = "DbHelper";

  private static final String DB_NAME = "randall.db";
  private static final int VERSION = 1;

  /** 账户表创建语句 */
  private static final String CREATE_ACCOUNT = "CREATE TABLE "
      + Account.NAME
      + " ("
      + Account.COL_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + Account.COL_USERNAME
      + " varchar(15) UNIQUE NOT NULL, "
      + Account.COL_PASSWORD
      + " varchar(15) NOT NULL, "
      + Account.COL_STATUS
      + " INTEGER DEFAULT 0, "
      + Account.COL_UPDATED
      + " INTEGER NOT NULL"
      + ");";

  public DbHelper(@NonNull Context context) {
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
