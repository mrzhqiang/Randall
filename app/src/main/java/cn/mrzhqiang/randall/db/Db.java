package cn.mrzhqiang.randall.db;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import java.util.Date;

/**
 * 数据库工厂方法
 */
public final class Db {
  public static final int BOOLEAN_FALSE = 0;
  public static final int BOOLEAN_TRUE = 1;

  public static boolean checkBoolean(int value) {
    return value == BOOLEAN_TRUE;
  }

  public static boolean getBoolean(Cursor cursor, String columnName) {
    return checkBoolean(getInt(cursor, columnName));
  }

  public static int getInt(Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndex(columnName));
  }

  public static double getDouble(Cursor cursor, String columnName) {
    return cursor.getDouble(cursor.getColumnIndex(columnName));
  }

  public static long getLong(Cursor cursor, String columnName) {
    return cursor.getLong(cursor.getColumnIndex(columnName));
  }

  public static String getString(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndex(columnName));
  }

  public static Date getDate(Cursor cursor, String columnName) {
    return new Date(cursor.getLong(cursor.getColumnIndex(columnName)));
  }

  public static String encode(@NonNull String string) {
    return new String(Base64.encode(string.getBytes(), Base64.DEFAULT));
  }

  public static String decode(@NonNull String string) {
    return new String(Base64.decode(string.getBytes(), Base64.DEFAULT));
  }

  private Db() {
    throw new AssertionError("No instances.");
  }
}
