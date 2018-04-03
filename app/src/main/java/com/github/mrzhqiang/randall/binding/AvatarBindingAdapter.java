package com.github.mrzhqiang.randall.binding;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import cn.mrzhqiang.helper.NameHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class AvatarBindingAdapter {
  private static final String TAG = "AvatarBindingAdapter";

  @BindingAdapter("nameToAvatar")
  public static void setAvatarByUsername(ImageView view, String username) {
    Observable.just(username)
        .subscribeOn(Schedulers.io())
        .filter(s -> !TextUtils.isEmpty(s))
        .map(s -> {
          DisplayMetrics metrics = view.getResources().getDisplayMetrics();
          return NameHelper.get(s, ((int) (48 * metrics.density)));
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(view::setImageBitmap,
            throwable -> Log.e(TAG, "通过 " + username + " 获取头像出错。", throwable));
  }
}
