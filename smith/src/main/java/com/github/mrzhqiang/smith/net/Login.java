package com.github.mrzhqiang.smith.net;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.net.html.Link;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.List;

@AutoValue public abstract class Login implements Parcelable {

  public abstract String title();

  @Nullable public abstract Link lastGame();

  @Nullable public abstract List<Link> listGame();

  @Nullable public abstract String script();

  @Nullable public abstract Account account();

  public String asItems() {
    Account a = account();
    if (a != null) {
      return a.username() + ":" + title();
    }
    return title();
  }

  public static Login create(String title, Link lastGame, List<Link> listGame, String script,
      Account account) {
    return builder().title(title)
        .lastGame(lastGame)
        .listGame(listGame)
        .script(script)
        .account(account)
        .build();
  }

  public static TypeAdapter<Login> typeAdapter(Gson gson) {
    return new AutoValue_Login.GsonTypeAdapter(gson);
  }

  public static Builder builder() {
    return new AutoValue_Login.Builder();
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder title(String title);

    public abstract Builder lastGame(Link lastGame);

    public abstract Builder listGame(List<Link> listGame);

    public abstract Builder script(String script);

    public abstract Builder account(Account account);

    public abstract Login build();
  }
}