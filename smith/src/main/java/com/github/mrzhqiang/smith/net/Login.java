package com.github.mrzhqiang.smith.net;

import android.os.Parcelable;
import com.github.mrzhqiang.smith.net.html.Link;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.Collections;
import java.util.List;

@AutoValue public abstract class Login implements Parcelable {

  public static final Login EMPTY = empty();

  public abstract Link lastGame();

  public abstract List<Link> listGame();

  public abstract String script();

  public static TypeAdapter<Login> typeAdapter(Gson gson) {
    return new AutoValue_Login.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_Login.Builder();
  }

  private static Login empty() {
    return builder().script("").lastGame(Link.empty()).listGame(Collections.emptyList()).build();
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder lastGame(Link lastGame);

    public abstract Builder listGame(List<Link> listGame);

    public abstract Builder script(String script);

    public abstract Login build();
  }
}