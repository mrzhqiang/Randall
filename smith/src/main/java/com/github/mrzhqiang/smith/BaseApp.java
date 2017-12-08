package com.github.mrzhqiang.smith;

import android.app.Application;
import cn.mrzhqiang.logger.Log;
import com.squareup.picasso.Picasso;

public abstract class BaseApp extends Application {

  private static AppComponent appcomponent;

  public abstract boolean debug();

  public abstract String baseUrl();

  @Override public void onCreate() {
    super.onCreate();

    // 在创建依赖注入接口之前，先定义日志模式
    Log.debug(debug());
    appcomponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    Picasso.setSingletonInstance(appcomponent.picasso());
  }

  public static AppComponent appComponent() {
    return appcomponent;
  }
}
