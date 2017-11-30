package com.github.mrzhqiang.smith.net.html;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Link implements Parcelable {

  public abstract String text();

  public abstract String href();

  public static Link create(String text, String href) {
    return new AutoValue_Link(text, href);
  }
}
