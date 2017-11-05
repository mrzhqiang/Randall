package cn.mrzhqiang.randall.viewmodel;

import android.databinding.ObservableInt;
import android.view.View;

/**
 * 加载中视图模型：如果加载失败，则显示点击重试；加载成功则直接消失。
 */
public class LoadingViewModel {

  public final ObservableInt visibility = new ObservableInt(View.GONE);
  public final ObservableInt loadVisibility = new ObservableInt(View.GONE);
  public final ObservableInt retryVisibility = new ObservableInt(View.GONE);

  public void loading() {
    visibility.set(View.VISIBLE);
    loadVisibility.set(View.VISIBLE);
  }

  public void loadSuccessful() {
    visibility.set(View.GONE);
    loadVisibility.set(View.GONE);
  }

  public void loadFailed() {
    retryVisibility.set(View.VISIBLE);
    loadVisibility.set(View.GONE);
  }

  public void clickRetry() {
    retryVisibility.set(View.GONE);
    loadVisibility.set(View.VISIBLE);
  }

}
