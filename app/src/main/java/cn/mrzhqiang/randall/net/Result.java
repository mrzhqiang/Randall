package cn.mrzhqiang.randall.net;

import android.util.Log;
import rx.Subscriber;

/**
 * 网络回调的统一处理
 */
public abstract class Result<T> extends Subscriber<T> {
  private static final String TAG = "Result";

  @Override public void onCompleted() {
    // no-op
  }

  @Override public void onError(Throwable e) {
    Log.d(TAG, "请求错误：" + e.getMessage());
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
