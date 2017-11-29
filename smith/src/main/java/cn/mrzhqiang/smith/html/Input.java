package cn.mrzhqiang.smith.html;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Input implements Parcelable {

  public abstract String type();

  public abstract String name();

  public abstract String value();

  public abstract String maxLength();

  public static Input create(String type, String name, String value, String maxLength) {
    return new AutoValue_Input(type, name, value, maxLength);
  }

  public enum Type {
    TEXT("text"), HIDDEN("hidden"), SUBMIT("submit"),;

    final String value;

    Type(String value) {
      this.value = value;
    }

    public boolean check(Input input) {
      return value.equals(input.type());
    }

    @Override public String toString() {
      return value;
    }
  }
}
