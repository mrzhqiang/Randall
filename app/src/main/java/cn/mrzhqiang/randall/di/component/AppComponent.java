package cn.mrzhqiang.randall.di.component;

import javax.inject.Singleton;

import cn.mrzhqiang.randall.di.module.AppModule;
import cn.mrzhqiang.randall.ui.LoginActivity;
import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    // add inject method
    void inject(LoginActivity loginActivity);
}
