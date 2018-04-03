package com.github.mrzhqiang.randall.di;

import android.content.Context;
import cn.mrzhqiang.logger.BuildConfig;
import cn.mrzhqiang.logger.Log;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Module(
    includes = NetworkModule.OkHttpModule.class
) final class NetworkModule {

  @Singleton
  @Provides Retrofit provideRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl("http://haowanba.com")
        //.addConverterFactory(SmithConverterFactory.create(baseUrl))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();
  }

  @Singleton
  @Provides OkHttp3Downloader provideOkHttpDownloader(
      @Named("picassoClient") OkHttpClient client) {
    return new OkHttp3Downloader(client);
  }

  @Singleton
  @Provides Picasso providePicasso(Context context, OkHttp3Downloader downloader) {
    return new Picasso.Builder(context.getApplicationContext()).downloader(downloader)
        .loggingEnabled(BuildConfig.DEBUG)
        .indicatorsEnabled(BuildConfig.DEBUG)
        .listener((picasso1, uri, exception) -> Log.d("Picasso",
            String.format(Locale.getDefault(), "Load Image: %s, failed: %s.", uri,
                exception.getMessage())))
        .build();
  }

  @Module
  static final class OkHttpModule {
    private static final String CACHE_DIR = "ok_http_cache";
    private static final int CACHE_MAX_SIZE = 50 * 1024 * 1024;

    @Singleton
    @Provides Authenticator provideAuthenticator() {
      return (route, response) -> {
        String auth = response.request().header("Authorization");
        if (auth == null) {
          // FIXME 模拟一个权限，留待未来完善
          String credential = Credentials.basic("ApiKey", "null", Charset.defaultCharset());
          return response.request().newBuilder().header("Authorization", credential).build();
        }
        // Token过期且刷新失败
        return null;
      };
    }

    @Singleton
    @Provides Cache provideCache(Context context) {
      File file = new File(context.getApplicationContext().getCacheDir(), CACHE_DIR);
      if (!file.exists()) {
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
      }
      return new Cache(file, CACHE_MAX_SIZE);
    }

    @Singleton
    @Provides
    @Named("picassoCache") Cache providePicassoCache(Context context) {
      return OkHttp3Downloader.createDefaultCache(context.getApplicationContext());
    }

    @Singleton
    @Provides HttpLoggingInterceptor provideHttpLoggingInterceptor() {
      HttpLoggingInterceptor logger =
          new HttpLoggingInterceptor(message -> Log.d("Network", message));
      logger.setLevel(BODY);
      return logger;
    }

    @Singleton
    @Provides OkHttpClient provideOkHttpClient(Authenticator authenticator, Cache cache,
        HttpLoggingInterceptor interceptor) {
      return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
          .writeTimeout(15, TimeUnit.SECONDS)
          .readTimeout(15, TimeUnit.SECONDS)
          .authenticator(authenticator)
          .cache(cache)
          .addInterceptor(interceptor)
          .build();
    }

    @Singleton
    @Provides
    @Named("picassoClient") OkHttpClient providePicassoClient(
        @Named("picassoCache") Cache cache, HttpLoggingInterceptor interceptor) {
      return new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
          .writeTimeout(10, TimeUnit.SECONDS)
          .readTimeout(10, TimeUnit.SECONDS)
          .cache(cache)
          .addInterceptor(interceptor)
          .build();
    }
  }
}

