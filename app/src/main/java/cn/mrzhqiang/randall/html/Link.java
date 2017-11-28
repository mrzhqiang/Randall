package cn.mrzhqiang.randall.html;

import com.google.auto.value.AutoValue;
import org.jsoup.nodes.Element;

@AutoValue
public abstract class Link {

  public abstract String text();

  public abstract String href();

  public static Link from(Element link) {
    String text = link.text();
    String href = link.absUrl("href");
    return Link.create(text, href);
  }

  private static Link create(String text, String href) {
    return new AutoValue_Link(text, href);
  }

}
