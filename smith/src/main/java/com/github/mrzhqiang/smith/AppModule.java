package com.github.mrzhqiang.smith;

import android.app.Application;
import com.github.mrzhqiang.smith.db.DbModule;
import com.github.mrzhqiang.smith.net.NetModule;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(includes = {
    DbModule.class, NetModule.class,
}) final class AppModule {
  private final SmithApp app;

  AppModule(SmithApp app) {
    this.app = app;
  }

  @Provides @Singleton Application provideApplication() {
    return app;
  }

  @Provides @Named("debug") boolean provideDebug() {
    return app.debug();
  }

  @Provides @Named("baseUrl") String provideBaseUrl() {
    return app.baseUrl();
  }
}