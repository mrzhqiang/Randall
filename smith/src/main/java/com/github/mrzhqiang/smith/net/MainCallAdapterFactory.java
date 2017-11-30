package com.github.mrzhqiang.smith.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 节省Randall接口返回类型为Observable的调度到主线程写法
 *
 * @author mrZQ.
 */
final class MainCallAdapterFactory extends CallAdapter.Factory {

  public static CallAdapter.Factory create() {
    return new MainCallAdapterFactory();
  }

  @Nullable @Override
  public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations,
      @NonNull Retrofit retrofit) {
    // 当然，返回类型不是Observable类型，不需要这样做
    if (getRawType(returnType) != Observable.class) {
      return null;
    }

    // Look up the next call adapter which would otherwise be used if this one was not present.
    //noinspection unchecked returnType checked above to be Observable.
    final CallAdapter<Object, Observable<?>> delegate =
        (CallAdapter<Object, Observable<?>>) retrofit.nextCallAdapter(this, returnType,
            annotations);
    return new CallAdapter<Object, Object>() {
      @Override public Object adapt(@NonNull Call<Object> call) {
        // Delegate to get the normal Observable...
        Observable<?> o = delegate.adapt(call);
        // 这里省略了Model调用Randall接口时，在IO线程上取消请求，以及观察在主线程上的写法
        return o.unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
      }

      @Override public Type responseType() {
        return delegate.responseType();
      }
    };
  }
}
