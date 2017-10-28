package cn.mrzhqiang.randall.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.db.Db;
import cn.mrzhqiang.randall.db.DbTable;
import java.util.List;
import rx.functions.Func1;

/**
 * 账户表，包含字段和创建SQL语句，升级语句
 */
public final class AccountTable implements DbTable {

  /** 提供给BriteDatabase映射字段使用 */
  public static final Func1<Cursor, Account> MAPPER = new Func1<Cursor, Account>() {
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

  /** 这个方法在插入新账户时使用，要更新这个表的对应字段，请使用Builder类 */
  public static ContentValues toContentValues(@NonNull Account account) {
    return new Builder().username(account.username)
        .password(account.password)
        .alias(account.alias())
        .status(account.status)
        .create(System.currentTimeMillis())
        .build();
  }

  ///////////////////////////////////////////////////////////////////////////
  // 表的相关信息
  ///////////////////////////////////////////////////////////////////////////
  public static final String NAME = "account";

  public static final String COL_ID = "_id";

  public static final String COL_USERNAME = "username";
  public static final String COL_PASSWORD = "password";

  public static final String COL_ALIAS = "_alias";// 别名，默认是上次登录服务器名，可以自己定义
  public static final String COL_STATUS = "status";

  public static final String COL_UPDATED = "updated";
  public static final String COL_CREATED = "created";

  public static final String SQL_CREATE = "CREATE TABLE "
      + NAME
      + " ("
      + COL_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COL_USERNAME
      + " varchar(15) UNIQUE NOT NULL, "
      + COL_PASSWORD
      + " varchar(15) NOT NULL, "
      + COL_ALIAS
      + " TEXT, "
      + COL_STATUS
      + " INTEGER DEFAULT 0, "
      + COL_UPDATED
      + " INTEGER NOT NULL, "
      + COL_CREATED
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

    private Builder username(@NonNull String username) {
      values.put(COL_USERNAME, Db.encode(username));
      return this;
    }

    public Builder password(@NonNull String password) {
      values.put(COL_PASSWORD, Db.encode(password));
      return this;
    }

    public Builder alias(@NonNull String alias) {
      values.put(COL_ALIAS, alias);
      return this;
    }

    public Builder status(Account.Status status) {
      values.put(COL_STATUS, status.ordinal());
      return this;
    }

    private Builder create(long created) {
      values.put(COL_CREATED, created);
      return this;
    }

    public ContentValues build() {
      values.put(COL_UPDATED, System.currentTimeMillis());
      return values;
    }
  }
}
