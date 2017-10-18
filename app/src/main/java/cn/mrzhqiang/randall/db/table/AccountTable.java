package cn.mrzhqiang.randall.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.db.Db;
import cn.mrzhqiang.randall.db.DbTable;
import java.util.Date;
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
      account.uid = Db.getLong(cursor, AccountTable.COL_UID);
      account.username = Db.decode(Db.getString(cursor, AccountTable.COL_USERNAME));
      account.password = Db.decode(Db.getString(cursor, AccountTable.COL_PASSWORD));
      account.alias = Db.getString(cursor, AccountTable.COL_ALIAS);
      account.status = Account.from(Db.getInt(cursor, AccountTable.COL_STATUS));
      account.isAvailable = Db.getBoolean(cursor, AccountTable.COL_IS_AVAILABLE);
      account.updated = Db.getDate(cursor, AccountTable.COL_UPDATED);
      account.created = Db.getDate(cursor, AccountTable.COL_CREATED);
      return account;
    }
  };

  /** 这个方法在插入新账户时使用，要更新这个表的对应字段，请使用Builder类 */
  public static ContentValues toContentValues(@NonNull Account account) {
    return new Builder().uid(account.uid)
        .username(account.username)
        .password(account.password)
        .alias(account.alias())
        .status(account.status)
        .type(account.isAvailable)
        .updated(account.updated)
        .created(account.created)
        .build();
  }

  ///////////////////////////////////////////////////////////////////////////
  // 表的相关信息
  ///////////////////////////////////////////////////////////////////////////
  public static final String NAME = "account";

  public static final String COL_ID = "_id";

  public static final String COL_UID = "_uid";
  public static final String COL_USERNAME = "username";
  public static final String COL_PASSWORD = "password";

  public static final String COL_ALIAS = "_alias";
  public static final String COL_STATUS = "status";

  public static final String COL_IS_AVAILABLE = "type";
  public static final String COL_UPDATED = "updated";
  public static final String COL_CREATED = "created";

  public static final String SQL_CREATE = "CREATE TABLE "
      + NAME
      + " ("
      + COL_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COL_UID
      + " INTEGER UNIQUE NOT NULL, "
      + COL_USERNAME
      + " varchar(15) UNIQUE NOT NULL, "
      + COL_PASSWORD
      + " varchar(15) NOT NULL, "
      + COL_ALIAS
      + " TEXT, "
      + COL_STATUS
      + " INTEGER NOT NULL, "
      + COL_IS_AVAILABLE
      + " INTEGER DEFAULT " + Db.BOOLEAN_TRUE + ", "
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

  /** 将账户对象转为ContentValues类，基本上用于更新数据 */
  public static class Builder {
    private final ContentValues values = new ContentValues();

    public Builder uid(long uid) {
      values.put(COL_UID, uid);
      return this;
    }

    public Builder username(@NonNull String username) {
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
      values.put(COL_STATUS, status.code());
      return this;
    }

    public Builder type(boolean isAvailable) {
      values.put(COL_IS_AVAILABLE, isAvailable ? Db.BOOLEAN_TRUE : Db.BOOLEAN_FALSE);
      return this;
    }

    public Builder updated(Date updated) {
      if (updated == null) {
        updated = new Date();
      }
      values.put(COL_UPDATED, updated.getTime());
      return this;
    }

    public Builder created(Date created) {
      if (created == null) {
        created = new Date();
      }
      values.put(COL_CREATED, created.getTime());
      return this;
    }

    public ContentValues build() {
      return values;
    }
  }
}
