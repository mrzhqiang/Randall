package com.github.mrzhqiang.smith;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import cn.mrzhqiang.logger.Log;
import com.squareup.picasso.Picasso;

public abstract class SmithApp extends Application {

  private static AppComponent appcomponent;

  public abstract boolean debug();

  public abstract String baseUrl();

  @Override public void onCreate() {
    super.onCreate();

    // 在创建依赖注入接口之前，先定义日志模式
    Log.debug(debug());
    appcomponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    Picasso.setSingletonInstance(appcomponent.picasso());

    if (debug()) {
      registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
        @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
          Log.d("Smith", "onActivityCreated：" + activity.getClass().getSimpleName());
        }

        @Override public void onActivityStarted(Activity activity) {
          Log.d("Smith", "onActivityStarted：" + activity.getClass().getSimpleName());
        }

        @Override public void onActivityResumed(Activity activity) {
          Log.d("Smith", "onActivityResumed：" + activity.getClass().getSimpleName());
        }

        @Override public void onActivityPaused(Activity activity) {
          Log.d("Smith", "onActivityPaused：" + activity.getClass().getSimpleName());
        }

        @Override public void onActivityStopped(Activity activity) {
          Log.d("Smith", "onActivityStopped：" + activity.getClass().getSimpleName());
        }

        @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
          Log.d("Smith", "onActivitySaveInstanceState：" + activity.getClass().getSimpleName());
        }

        @Override public void onActivityDestroyed(Activity activity) {
          Log.d("Smith", "onActivityDestroyed：" + activity.getClass().getSimpleName());
        }
      });
    }
  }

  public static AppComponent appComponent() {
    return appcomponent;
  }
}
