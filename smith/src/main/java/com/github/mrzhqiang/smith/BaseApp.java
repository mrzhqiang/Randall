package com.github.mrzhqiang.smith;

import android.app.Application;
import cn.mrzhqiang.logger.Log;

/**
 * 应用，初始化日志和依赖注入的地方
 *
 * @author mrZQ
 */
public abstract class BaseApp extends Application {

  /** 注意：多进程的Application应当考虑其他方式提供依赖注入接口实现 */
  private static AppComponent appcomponent;

  /**
   * 通过实现应用层的Application来判断是否处于调试模式，库层的{@link BuildConfig#DEBUG debug}永远是false
   *
   * @return 返回true代表日志、网络、图片、数据库等都处于调试模式；返回false则处于发布模式，不会有任何调试信息
   */
  public abstract boolean debug();

  /**
   * 通过应用层传递来的baseUrl来初始化Retrofit实例，这样可以防止域名变化而导致这个库的改动
   *
   * @return 类似http://haowanba.com
   */
  public abstract String baseUrl();

  @Override public void onCreate() {
    super.onCreate();

    // 在创建依赖注入接口之前，先定义日志模式
    Log.debug(debug());
    appcomponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
  }

  public static AppComponent appComponent() {
    return appcomponent;
  }
}
