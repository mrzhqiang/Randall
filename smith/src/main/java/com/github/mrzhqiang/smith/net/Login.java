package com.github.mrzhqiang.smith.net;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.net.html.Link;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue public abstract class Login implements Parcelable {

  public abstract String title();

  @Nullable public abstract Link lastGame();

  @Nullable public abstract List<Link> listGame();

  @Nullable public abstract String script();

  @Nullable public abstract String uid();

  @Nullable public abstract Account account();

  public String asItems() {
    Account a = account();
    if (a != null) {
      return a.username() + ":" + title();
    }
    return title();
  }

  public static Builder builder() {
    return new AutoValue_Login.Builder();
  }

  public static Builder builder(Login login) {
    return new AutoValue_Login.Builder().title(login.title())
        .lastGame(login.lastGame())
        .listGame(login.listGame())
        .script(login.script());
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder title(String title);

    public abstract Builder lastGame(Link lastGame);

    public abstract Builder listGame(List<Link> listGame);

    public abstract Builder script(String script);

    public abstract Builder account(Account account);

    public abstract Builder uid(String uid);

    public abstract Login build();
  }
}