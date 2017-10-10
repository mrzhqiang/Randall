package cn.mrzhqiang.randall;

import android.app.Application;

import cn.mrzhqiang.randall.di.component.AppComponent;
import cn.mrzhqiang.randall.di.component.DaggerAppComponent;
import cn.mrzhqiang.randall.di.module.AppModule;

/**
 * <p>
 * Created by mrZQ on 2017/8/27.
 */
public class RandallApp extends Application {

    private static AppComponent appcomponent;

    @Override public void onCreate() {
        super.onCreate();
        appcomponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent appComponent() {
        return appcomponent;
    }
}
