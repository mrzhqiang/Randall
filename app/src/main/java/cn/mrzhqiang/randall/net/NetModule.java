package cn.mrzhqiang.randall.net;

import android.app.Application;
import android.net.Uri;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;

import cn.mrzhqiang.logger.Log;
import cn.mrzhqiang.randall.BuildConfig;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;

/**
 * 提供网络相关依赖
 *
 * @author mrZQ
 */
@Module(includes = {
    OkHttpModule.class,
}) public final class NetModule {

  /**
   * 提供OkHttp3下载器给Picasso使用
   */
  @Provides @Singleton OkHttp3Downloader provideOkHttpDownloader(
      @Named("picasso") OkHttpClient client) {
    return new OkHttp3Downloader(client);
  }

  /**
   * 构建Picasso单例，通常不需要依赖注入这个实例，按原先的方式使用静态方法即可
   */
  @Provides @Singleton Picasso providePicasso(Application application,
      OkHttp3Downloader downloader) {
    return new Picasso.Builder(application).downloader(downloader)
        .loggingEnabled(BuildConfig.DEBUG)
        .indicatorsEnabled(BuildConfig.DEBUG)// 是否开启左上角标识符
        .listener(new Picasso.Listener() {
          @Override public void onImageLoadFailed(Picasso picasso1, Uri uri, Exception exception) {
            Log.d("Picasso", String.format(Locale.getDefault(), "Load Image: %s , failed:%s.", uri,
                exception.getMessage()));
          }
        })
        .build();
  }

  /**
   * 提供Retrofit单例，去构建响应的后台Api接口实例
   */
  @Provides @Singleton Retrofit provideRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl(Randall.BASE_URL)// 暂时这样写
        .addConverterFactory(WmlStringConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(client)
        .build();
  }

  @Provides @Singleton Randall provideRandall(Retrofit retrofit) {
    return retrofit.create(Randall.class);
  }
}
