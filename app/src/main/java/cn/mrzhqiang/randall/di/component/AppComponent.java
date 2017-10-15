package cn.mrzhqiang.randall.di.component;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import cn.mrzhqiang.randall.di.module.AppModule;
import cn.mrzhqiang.randall.ui.LoginActivity;
import dagger.Component;

@Singleton @Component(modules = AppModule.class) public interface AppComponent {
  // create return method
  Picasso picasso();

  // add inject method
  void inject(LoginActivity loginActivity);
}
