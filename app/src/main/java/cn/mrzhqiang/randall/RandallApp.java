package cn.mrzhqiang.randall;

import android.app.Application;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;

import cn.mrzhqiang.randall.data.wml.home.Randall;
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

        try {
            InputStream inputStream = getAssets().open("home.xml");
            Serializer serializer = new Persister();
            Randall randall = serializer.read(Randall.class, inputStream);
            System.out.println(randall);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppComponent appComponent() {
        return appcomponent;
    }
}
