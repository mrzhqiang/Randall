package cn.mrzhqiang.smith.haowanba;

import android.os.Parcelable;
import cn.mrzhqiang.smith.html.Form;
import cn.mrzhqiang.smith.html.Link;
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