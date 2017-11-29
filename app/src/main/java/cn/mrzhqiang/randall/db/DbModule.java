package cn.mrzhqiang.randall.db;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import cn.mrzhqiang.logger.BuildConfig;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import cn.mrzhqiang.logger.Log;
import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

/**
 * 提供数据库相关依赖
 *
 * @author mrZQ
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
    db.setLoggingEnabled(BuildConfig.DEBUG);
    return db;
  }
}
