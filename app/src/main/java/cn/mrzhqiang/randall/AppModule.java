package cn.mrzhqiang.randall;

import android.app.Application;

import cn.mrzhqiang.randall.db.DbModule;
import cn.mrzhqiang.randall.net.NetModule;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
    DbModule.class, NetModule.class,
}) final class AppModule {
  private final Application application;

  AppModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton Application provideApplication() {
    return application;
  }
}