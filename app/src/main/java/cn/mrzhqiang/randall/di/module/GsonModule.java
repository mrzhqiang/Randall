package cn.mrzhqiang.randall.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 提供各种各样的单例Gson
 * <p>
 * Created by mrZQ on 2017/10/12.
 */
@Module public final class GsonModule {
  private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  @Provides @Singleton Gson provideGson() {
    return new Gson();
  }

  @Provides @Singleton @Named("net") Gson provideNetGson() {
    return new GsonBuilder().setDateFormat(DATE_FORMAT)
        .setPrettyPrinting()
        .serializeNulls()
        .disableHtmlEscaping()
                /*.setLenient()*/
        .create();
  }

  @Provides @Singleton @Named("db") Gson provideDbGson() {
    return new GsonBuilder().setDateFormat(DATE_FORMAT)
        .setPrettyPrinting()
        .serializeNulls()
        .create();
  }
}
