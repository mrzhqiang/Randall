package cn.mrzhqiang.randall.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;
import cn.mrzhqiang.randall.db.Db;
import cn.mrzhqiang.randall.db.DbTable;
import java.util.List;

/**
 * 服务器表，由于新区及合区时间的不确定性，数据应该由某个按钮提供更新
 */
public final class ServerTable implements DbTable {

  /** 提供给BriteDatabase映射字段使用 *//*
  public static final Func1<Cursor, Server> MAPPER = new Func1<Cursor, Server>() {
    @Override public Account call(Cursor cursor) {
      Account account = new Account();
      account.id = Db.getLong(cursor, AccountTable.COL_ID);
      account.username = Db.decode(Db.getString(cursor, AccountTable.COL_USERNAME));
      account.password = Db.decode(Db.getString(cursor, AccountTable.COL_PASSWORD));
      account.alias = Db.getString(cursor, AccountTable.COL_ALIAS);
      account.status = Account.from(Db.getInt(cursor, AccountTable.COL_STATUS));
      return account;
    }
  };

  *//** 这个方法在插入新账户时使用，要更新这个表的对应字段，请使用Builder类 *//*
  public static ContentValues toContentValues(@NonNull Account account) {
    return new Builder().username(account.username)
        .password(account.password)
        .alias(account.alias())
        .status(account.status)
        .create(System.currentTimeMillis())
        .build();
  }*/

  ///////////////////////////////////////////////////////////////////////////
  // 表属性
  ///////////////////////////////////////////////////////////////////////////
  public static final String NAME = "server";

  public static final String COL_ID = "_id";
  public static final String COL_NAME = "name";
  public static final String COL_PATH = "path";
  public static final String COL_ALIAS = "_alias";
  public static final String COL_UPDATED = "updated";

  public static final String SQL_CREATE = "CREATE TABLE "
      + NAME
      + " ("
      + COL_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COL_NAME
      + " TEXT UNIQUE NOT NULL, "
      + COL_PATH
      + " TEXT NOT NULL, "
      + COL_ALIAS
      + " TEXT, "
      + COL_UPDATED
      + " INTEGER NOT NULL"
      + ");";


  @NonNull @Override public String getCreateSql() {
    return SQL_CREATE;
  }

  @NonNull @Override public LongSparseArray<List<String>> getUpgrade() {
    return new LongSparseArray<>();
  }

  /** 更新账户信息的构造器 */
  public static class Builder {
    private final ContentValues values = new ContentValues();

    private Builder name(@NonNull String name) {
      values.put(COL_NAME, name);
      return this;
    }

    public Builder path(@NonNull String path) {
      values.put(COL_PATH, path);
      return this;
    }

    public Builder alias(@NonNull String alias) {
      values.put(COL_ALIAS, alias);
      return this;
    }

    public ContentValues build() {
      values.put(COL_UPDATED, System.currentTimeMillis());
      return values;
    }
  }
}
