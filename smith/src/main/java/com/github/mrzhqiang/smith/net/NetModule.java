package com.github.mrzhqiang.smith.net;

import android.app.Application;
import android.net.Uri;
import android.support.annotation.NonNull;
import cn.mrzhqiang.logger.Log;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

/**
 * 提供网络相关依赖
 *
 * @author mrZQ
 */
@Module(includes = {
    NetModule.OkHttpModule.class,
}) public final class NetModule {

  @Provides @Singleton Retrofit provideRetrofit(OkHttpClient client,
      @Named("baseUrl") String baseUrl) {
    return new Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(SmithConverterFactory.create(baseUrl))
        .addCallAdapterFactory(MainCallAdapterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(client)
        .build();
  }

  @Provides @Singleton OkHttp3Downloader provideOkHttpDownloader(
      @Named("picasso") OkHttpClient client) {
    return new OkHttp3Downloader(client);
  }

  @Provides @Singleton Picasso providePicasso(Application application, OkHttp3Downloader downloader,
      @Named("debug") boolean debug) {
    return new Picasso.Builder(application).downloader(downloader)
        .loggingEnabled(debug)
        .indicatorsEnabled(debug)
        .listener((picasso1, uri, exception) -> Log.d("Picasso",
            String.format(Locale.getDefault(), "Load Image: %s, failed: %s.", uri,
                exception.getMessage())))
        .build();
  }

  @Module public static final class OkHttpModule {
    private static final String CACHE_DIR = "ok_http_cache";
    private static final int CACHE_MAX_SIZE = 50 * 1024 * 1024;

    @Provides @Singleton Authenticator provideAuthenticator() {
      return (route, response) -> {
        String auth = response.request().header("Authorization");
        if (auth == null) {
          // FIXME 模拟一个权限，留待未来完善
          String credential = Credentials.basic("ApiKey", "null");
          return response.request().newBuilder().header("Authorization", credential).build();
        }
        // Token过期且刷新失败
        return null;
      };
    }

    @Provides @Singleton Cache provideCache(Application application) {
      File file = new File(application.getCacheDir(), CACHE_DIR);
      if (!file.exists()) {
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
      }
      return new Cache(file, CACHE_MAX_SIZE);
    }

    @Named("picasso") @Provides @Singleton Cache providePicassoCache(Application application) {
      return OkHttp3Downloader.createDefaultCache(application);
    }

    @Provides @Singleton HttpLoggingInterceptor provideHttpLoggingInterceptor() {
      HttpLoggingInterceptor logger =
          new HttpLoggingInterceptor(message -> Log.d("Network", message));
      logger.setLevel(BODY);
      return logger;
    }

    @Provides @Singleton OkHttpClient provideOkHttpClient(Authenticator authenticator, Cache cache,
        HttpLoggingInterceptor interceptor) {
      return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
          .writeTimeout(15, TimeUnit.SECONDS)
          .readTimeout(15, TimeUnit.SECONDS)
          .authenticator(authenticator)
          .cache(cache)
          .addInterceptor(interceptor)
          .build();
    }

    @Named("picasso") @Provides @Singleton OkHttpClient providePicassoClient(
        @Named("picasso") Cache cache, HttpLoggingInterceptor interceptor) {
      return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
          .writeTimeout(15, TimeUnit.SECONDS)
          .readTimeout(15, TimeUnit.SECONDS)
          .cache(cache)
          .addInterceptor(interceptor)
          .build();
    }
  }
}
