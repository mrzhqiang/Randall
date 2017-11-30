package com.github.mrzhqiang.smith.net;

import android.os.Parcelable;
import com.github.mrzhqiang.smith.net.html.Form;
import com.github.mrzhqiang.smith.net.html.Link;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Login implements Parcelable {

  public abstract String title();

  public abstract Form form();

  public abstract Link link();

  public static Login create(String title, Form form, Link link) {
    return new AutoValue_Login(title, form, link);
  }

  public enum Type {
    SIGN_IN("登录>"), SIGN_UP("注册"),;

    final String value;

    Type(String value) {
      this.value = value;
    }

    public boolean check(String text) {
      return value.equals(text);
    }

    @Override public String toString() {
      return value;
    }
  }
}