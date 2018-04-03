package com.github.mrzhqiang.randall.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.github.mrzhqiang.randall.BuildConfig;
import com.github.mrzhqiang.randall.db.DbHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import rx.schedulers.Schedulers;

@Module final class DatabaseModule {
  @Singleton
  @Provides SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
    return new DbHelper(context.getApplicationContext());
  }

  @Singleton
  @Provides
  SqlBrite provideSqlBrite() {
    return new SqlBrite.Builder().logger(message -> Log.d("Database", message)).build();
  }

  @Singleton
  @Provides
  BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
    BriteDatabase briteDatabase = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    briteDatabase.setLoggingEnabled(BuildConfig.DEBUG);
    return briteDatabase;
  }
}
