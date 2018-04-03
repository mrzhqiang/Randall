package com.github.mrzhqiang.randall.db;

import android.database.Cursor;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Base64;

public final class DbUtil {
  public static final int BOOLEAN_FALSE = 0;
  public static final int BOOLEAN_TRUE = 1;

  @NonNull
  @CheckResult
  public static String getString(@NonNull Cursor cursor, @NonNull String columnName) {
    return cursor.getString(cursor.getColumnIndex(columnName));
  }

  @CheckResult
  public static boolean getBoolean(@NonNull Cursor cursor, @NonNull String columnName) {
    return getInt(cursor, columnName) == BOOLEAN_TRUE;
  }

  @CheckResult
  public static long getLong(@NonNull Cursor cursor, @NonNull String columnName) {
    return cursor.getLong(cursor.getColumnIndex(columnName));
  }

  @CheckResult
  public static int getInt(@NonNull Cursor cursor, @NonNull String columnName) {
    return cursor.getInt(cursor.getColumnIndex(columnName));
  }

  @NonNull
  @CheckResult
  public static String encode(@NonNull String string) {
    return new String(Base64.encode(string.getBytes(), Base64.DEFAULT));
  }

  @NonNull
  @CheckResult
  public static String decode(@NonNull String string) {
    return new String(Base64.decode(string.getBytes(), Base64.DEFAULT));
  }

  private DbUtil() {
    throw new AssertionError("No instance.");
  }
}
