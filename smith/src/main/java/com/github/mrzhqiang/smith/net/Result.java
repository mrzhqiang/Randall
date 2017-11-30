package com.github.mrzhqiang.smith.net;

import android.util.Log;
import java.net.UnknownHostException;
import rx.Subscriber;

/**
 * 网络回调的统一处理，应用层只需要知道这个类即可，不关心底层依赖
 *
 * @author mrZQ
 */
public abstract class Result<T> extends Subscriber<T> {
  private static final String TAG = "Result";

  @Override public void onCompleted() {
    // no-op
  }

  @Override public void onError(Throwable e) {
    if (e instanceof UnknownHostException) {
      onFailed("链接异常，请检查网络");
      return;
    }
    onFailed(e.getMessage());
  }

  @Override public void onNext(T t) {
    if (t == null) {
      onFailed("非法数据，请重试");
      return;
    }
    onSuccessful(t);
  }

  abstract public void onSuccessful(T result);

  public void onFailed(String message) {
    Log.e(TAG, "this: " + this.getClass().getSimpleName() + ", failed: " + message);
  }
}