package com.github.mrzhqiang.smith.db;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Base64;

/**
 * 数据库常量及便捷方法
 *
 * @author mrZQ
 */
public final class Db {
  public static final int BOOLEAN_FALSE = 0;
  public static final int BOOLEAN_TRUE = 1;

  public static String getString(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndex(columnName));
  }

  public static boolean getBoolean(Cursor cursor, String columnName) {
    return getInt(cursor, columnName) == BOOLEAN_TRUE;
  }

  public static long getLong(Cursor cursor, String columnName) {
    return cursor.getLong(cursor.getColumnIndex(columnName));
  }

  public static int getInt(Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndex(columnName));
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
