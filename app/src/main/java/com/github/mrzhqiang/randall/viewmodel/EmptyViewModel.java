package com.github.mrzhqiang.randall.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

/**
 * 内容为空时的提示
 */
public final class EmptyViewModel {

  @BindingAdapter("emptyIcon")
  public static void loadIconFromPath(ImageView view, @DrawableRes int resourceId) {
    if (resourceId != 0) {
      view.setImageResource(resourceId);
    }
  }

  public final ObservableInt visibility = new ObservableInt(View.VISIBLE);
  public final ObservableField<String> hint = new ObservableField<>();
  public final ObservableInt icon = new ObservableInt();

  public void show() {
    visibility.set(View.VISIBLE);
  }

  public void hide() {
    visibility.set(View.GONE);
  }

  public void setHint(String hint) {
    this.hint.set(hint);
  }

  public void setIcon(@DrawableRes int resourceId) {
    icon.set(resourceId);
  }

}
