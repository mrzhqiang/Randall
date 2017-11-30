package com.github.mrzhqiang.smith.net;

import android.os.Parcelable;
import com.github.mrzhqiang.smith.net.html.Link;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue public abstract class Home implements Parcelable {

  public abstract String title();

  public abstract List<Link> links();

  public static Home create(String title, List<Link> links) {
    return new AutoValue_Home(title, links);
  }
}