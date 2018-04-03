package com.github.mrzhqiang.randall;

import cn.mrzhqiang.logger.Log;
import com.github.mrzhqiang.randall.di.DaggerAppComponent;
import com.squareup.picasso.Picasso;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import javax.inject.Inject;

public final class RandallApp extends DaggerApplication {

  @Inject Picasso picasso;

  @Override protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    return DaggerAppComponent.builder().application(this).build();
  }

  @Override public void onCreate() {
    super.onCreate();
    Log.debug(BuildConfig.DEBUG);
    Picasso.setSingletonInstance(picasso);
  }
}
