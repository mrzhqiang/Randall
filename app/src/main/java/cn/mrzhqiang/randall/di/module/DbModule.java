package cn.mrzhqiang.randall.di.module;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Arrays;

import javax.inject.Singleton;

import cn.mrzhqiang.logger.Log;
import cn.mrzhqiang.randall.db.DbHelper;
import cn.mrzhqiang.randall.db.DbTable;
import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

/**
 * 提供数据库相关实例
 * <p>
 * Created by mrZQ on 2017/10/12.
 */
@Module public final class DbModule {

  @Provides @Singleton SQLiteOpenHelper provideOpenHelper(Application application) {
    return new DbHelper(application);
  }

  @Provides @Singleton SqlBrite provideSqlBrite() {
    return new SqlBrite.Builder().logger(new SqlBrite.Logger() {
      @Override public void log(String message) {
        Log.d("Database", message);
      }
    }).build();
  }

  @Provides @Singleton BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
    BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    db.setLoggingEnabled(true);
    return db;
  }
}
