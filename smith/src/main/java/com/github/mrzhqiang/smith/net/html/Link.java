package com.github.mrzhqiang.smith.net.html;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Link implements Parcelable {

  public abstract String text();

  @Nullable
  public abstract String suffix();

  public abstract String href();

  public static Builder builder() {
    return new AutoValue_Link.Builder();
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder text(String text);

    public abstract Builder suffix(String suffix);

    public abstract Builder href(String href);

    public abstract Link build();
  }
}
