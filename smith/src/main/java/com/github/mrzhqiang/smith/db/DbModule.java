package com.github.mrzhqiang.smith.db;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import cn.mrzhqiang.logger.Log;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
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
    return new SqlBrite.Builder().logger(message -> Log.d("Database", message)).build();
  }

  @Provides @Singleton BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper,
      @Named("debug") boolean debug) {
    BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    db.setLoggingEnabled(debug);
    return db;
  }
}