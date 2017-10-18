package cn.mrzhqiang.randall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import cn.mrzhqiang.logger.Log;
import cn.mrzhqiang.randall.db.table.AccountTable;

/**
 * 数据库辅助工具
 */
public final class DbHelper extends SQLiteOpenHelper {
  private static final String TAG = "DbHelper";

  private static final String DB_NAME = "randall.db";
  private static final int VERSION = 1;

  private static final DbTable[] DB_TABLES = {
      new AccountTable(),

  };

  public DbHelper(@NonNull Context context) {
    super(context, DB_NAME, null /* factory */, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    Log.i(TAG, "Creating database");
    // 开启外键功能
    db.execSQL("PRAGMA foreign_keys=ON;");

    for (DbTable table : DB_TABLES) {
      String createSql = table.getCreateSql();
      if (!TextUtils.isEmpty(createSql)) {
        Log.d(TAG, "ExecSql: " + createSql);
        db.execSQL(createSql);
      }
    }
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i(TAG, "Upgrading schema from " + oldVersion + " to " + newVersion);

    for (int i = oldVersion; i < newVersion; i++) {
      for (DbTable table : DB_TABLES) {
        for (String upSql : table.getUpgrade().get(i)) {
          Log.d(TAG, "ExecSQL: " + upSql);
          db.execSQL(upSql);
        }
      }
    }
  }
}
