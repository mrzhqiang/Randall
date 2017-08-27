package cn.mrzhqiang.randall.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        AndroidModule.class,
})
public final class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides @Singleton Application provideApplication() {
        return application;
    }

}
