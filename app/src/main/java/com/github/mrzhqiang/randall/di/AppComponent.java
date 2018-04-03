package com.github.mrzhqiang.randall.di;

import android.app.Application;
import com.github.mrzhqiang.randall.RandallApp;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton @Component(modules = {
    ApplicationModule.class,
    AndroidSupportInjectionModule.class,
    DatabaseModule.class,
    NetworkModule.class,
})
public interface AppComponent extends AndroidInjector<RandallApp> {

  @Component.Builder
  interface Builder {
    @BindsInstance
    AppComponent.Builder application(Application application);

    AppComponent build();
  }
}
