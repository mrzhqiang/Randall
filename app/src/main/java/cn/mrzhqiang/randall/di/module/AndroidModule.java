package cn.mrzhqiang.randall.di.module;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 提供各种各样的系统管理器
 * <P>
 * Created by mrZQ on 2017/10/12.
 */
@Module public final class AndroidModule {

  @Provides AudioManager provideAudioManager(Application application) {
    return (AudioManager) application.getSystemService(Context.AUDIO_SERVICE);
  }

  @Provides SensorManager provideSensorManager(Application application) {
    return (SensorManager) application.getSystemService(Context.SENSOR_SERVICE);
  }

  @Provides Sensor provideSensorAccelerometer(SensorManager sensorManager) {
    return sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  @Provides ConnectivityManager provideConnectivityManager(Application application) {
    return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Provides InputMethodManager provideInputMethodManager(Application application) {
    return (InputMethodManager) application.getSystemService(Context.INPUT_METHOD_SERVICE);
  }

  @Provides NotificationManager provideNotificationManger(Application application) {
    return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
  }

  @Provides NotificationCompat.Builder provideNotificationCompat(Application application) {
    return new NotificationCompat.Builder(application);
  }

  @Provides NotificationManagerCompat provideNotificationManagerCompat(Application application) {
    return NotificationManagerCompat.from(application);
  }
}
