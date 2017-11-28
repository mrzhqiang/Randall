package cn.mrzhqiang.randall.haowanba;

import com.google.auto.value.AutoValue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@AutoValue
public abstract class Game {

  public abstract String title();

  public abstract Element body();

  public static Game create(String title, Element body) {
    return new AutoValue_Game(title, body);
  }

  public static Game from(Document document) {
    String title = document.title();
    Element body = document.body();
    return create(title, body);
  }
}
