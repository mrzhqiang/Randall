package com.github.mrzhqiang.smith.net.html;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue public abstract class Link implements Parcelable {

  public abstract String text();

  public abstract String suffix();

  public abstract String href();

  public String showContent() {
    if ("".equals(suffix())) {
      return text();
    }
    return text() + suffix();
  }

  public static Link create(String text, String suffix, String href) {
    return new AutoValue_Link(text, suffix, href);
  }

  public static Link empty() {
    return create("", "", "");
  }

  public static TypeAdapter<Link> typeAdapter(Gson gson) {
    return new AutoValue_Link.GsonTypeAdapter(gson);
  }
}
