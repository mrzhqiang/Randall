package cn.mrzhqiang.randall.viewmodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.View;

/**
 * 加载动画，以及点击重试
 */
public class LoadingViewModel {

  public final ObservableInt retryVisibility = new ObservableInt(View.GONE);
  public final ObservableInt loadVisibility = new ObservableInt(View.VISIBLE);

  public final View.OnClickListener retry = new View.OnClickListener() {
    @Override public void onClick(View v) {
      retryVisibility.set(View.GONE);
      loadVisibility.set(View.VISIBLE);
      onRetry(v.getContext());
    }
  };

  public void loadFailed() {
    retryVisibility.set(View.VISIBLE);
    loadVisibility.set(View.GONE);
  }

  public void loadSuccessful() {
    loadVisibility.set(View.GONE);
  }

  public void onRetry(Context context) {
    // no-op
  }
}
