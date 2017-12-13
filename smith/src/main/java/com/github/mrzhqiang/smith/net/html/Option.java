package com.github.mrzhqiang.smith.net.html;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Option implements Parcelable {
  public abstract String value();

  public abstract String text();

  public static Option create(String value, String text) {
    return new AutoValue_Option(value, text);
  }
}
