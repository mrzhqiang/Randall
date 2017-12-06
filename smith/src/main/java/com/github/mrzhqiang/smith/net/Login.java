package com.github.mrzhqiang.smith.net;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.github.mrzhqiang.smith.net.html.Link;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue public abstract class Login implements Parcelable {

  public abstract String title();

  @Nullable
  public abstract Link lastGame();

  @Nullable
  public abstract List<Link> listGame();

  public static Login create(String title, Link lastGame, List<Link> listGame) {
    return builder().title(title).lastGame(lastGame).listGame(listGame).build();
  }

  public static Builder builder() {
    return new AutoValue_Login.Builder();
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder title(String title);

    public abstract Builder lastGame(Link lastGame);

    public abstract Builder listGame(List<Link> listGame);

    public abstract Login build();
  }
}