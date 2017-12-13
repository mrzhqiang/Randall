package com.github.mrzhqiang.smith.net;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Game implements Parcelable {

  public abstract String title();

  public abstract String body();

  public abstract String script();

  public static Game create(String title, String body, String script) {
    return new AutoValue_Game(title, body, script);
  }
}
