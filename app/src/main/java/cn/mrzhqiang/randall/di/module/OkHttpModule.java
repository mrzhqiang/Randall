package cn.mrzhqiang.randall.di.module;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Logger;

import android.app.Application;
import android.support.annotation.NonNull;

import com.jakewharton.picasso.OkHttp3Downloader;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import cn.mrzhqiang.logger.Log;
import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;

@Module public final class OkHttpModule {
  private static final String CACHE_DIR = "ok_http_cache";
  private static final int CACHE_MAX_SIZE = 50 * 1024 * 1024; // 50Mb

  /**
   * 认证器。如果返回null，则将通知403无权限。
   */
  @Provides @Singleton Authenticator provideAuthenticator() {
    return new Authenticator() {
      @Override public Request authenticate(@NonNull Route route, @NonNull Response response)
          throws IOException {
        String auth = response.request().header("Authorization");
        if (auth == null) {
          // 模拟一个权限，留待未来完善
          String credential = Credentials.basic("ApiKey", "null");
          return response.request().newBuilder().header("Authorization", credential).build();
        }
        // Token过期且刷新失败
        return null;
      }
    };
  }

  /**
   * 缓存，通过Application拿到程序内部缓存目录，从而在程序被卸载时，自动清理缓存空间
   */
  @Provides @Singleton Cache provideCache(Application application) {
    File file = new File(application.getCacheDir(), CACHE_DIR);
    if (!file.exists()) {
      //noinspection ResultOfMethodCallIgnored
      file.mkdirs();
    }
    return new Cache(file, CACHE_MAX_SIZE);
  }

  /**
   * 缓存，这里是图片加载器的缓存，以备随时清空
   */
  @Named("picasso") @Provides @Singleton Cache providePicassoCache(Application application) {
    return OkHttp3Downloader.createDefaultCache(application);
  }

  /**
   * 网络请求日志拦截器
   */
  @Provides @Singleton HttpLoggingInterceptor provideHttpLoggingInterceptor() {
    HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new Logger() {
      @Override public void log(@NonNull String message) {
        Log.d("Network", message);
      }
    });
    logger.setLevel(BODY);
    return logger;
  }

  /**
   * 通过以上参数实例，得到一个网络请求客户端，单例模式
   */
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

  /**
   * 给Picasso使用的OkHttpClinet
   */
  @Named("picasso") @Provides @Singleton OkHttpClient providePicassoClient(
      @Named("picasso") Cache cache, HttpLoggingInterceptor interceptor) {
    return new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .cache(cache)
        .addInterceptor(interceptor)
        .build();
  }

  /**
   * 通过网络请求客户端单例，得到共享连接池的可重新配置的新客户端
   */
  @Provides OkHttpClient.Builder provideCustomOkHttpClient(OkHttpClient client) {
    return client.newBuilder();
  }
}
