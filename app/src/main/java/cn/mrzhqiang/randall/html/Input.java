package cn.mrzhqiang.randall.html;

import com.google.auto.value.AutoValue;
import org.jsoup.nodes.Element;

@AutoValue
public abstract class Input {

  public abstract String type();

  public abstract String name();

  public abstract String value();

  public abstract String maxLength();

  boolean checkType(Type type) {
    return type().equalsIgnoreCase(type.value);
  }

  private static Input create(String type, String name, String value, String maxLength) {
    return new AutoValue_Input(type, name, value, maxLength);
  }

  static Input from(Element input) {
    String type = input.attr("type");
    String name = input.attr("name");
    String value = input.attr("value");
    String maxLength = input.attr("maxlength");
    return create(type, name, value, maxLength);
  }

  public enum Type {
    TEXT("text"),
    HIDDEN("hidden"),
    @SuppressWarnings("unused") SUBMIT("submit"),
    ;

    final String value;

    Type(String value) {
      this.value = value;
    }

    @Override public String toString() {
      return value;
    }
  }

}
