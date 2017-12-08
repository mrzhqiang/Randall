package com.github.mrzhqiang.smith.net;

import android.util.Log;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import rx.Subscriber;

public abstract class Result<T> extends Subscriber<T> {
  private static final String TAG = "Result";

  @Override public void onCompleted() {
    // no-op
  }

  @Override public void onError(Throwable e) {
    String message = e.getMessage();
    if (e instanceof ConnectException) {
      message = "连接异常";
    }
    if (e instanceof SocketTimeoutException) {
      message = "网络超时";
    }
    if (e instanceof UnknownHostException) {
      message = "连接不上服务器";
    }
    onFailed(message);
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