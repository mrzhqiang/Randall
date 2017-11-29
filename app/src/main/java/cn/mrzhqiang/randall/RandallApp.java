package cn.mrzhqiang.randall;

import android.app.Application;

/**
 * 应用
 *
 * @author mrZQ
 */
public final class RandallApp extends Application {

  private static RandallApp app;
  private AppComponent appcomponent;

  @Override public void onCreate() {
    super.onCreate();
    app = this;
    appcomponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
  }

  public static AppComponent appComponent() {
    return app.appcomponent;
  }
}
