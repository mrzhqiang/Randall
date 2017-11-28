package cn.mrzhqiang.randall.haowanba;

import cn.mrzhqiang.randall.html.Link;
import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AutoValue
public abstract class Home {

  public abstract String title();

  public abstract List<Link> links();

  public static Home from(Document document) {
    String title = document.title();
    Elements elements = document.body().select("a[href]");
    List<Link> links = new ArrayList<>(elements.size());
    for (Element link : elements) {
      links.add(Link.from(link));
    }
    return create(title, links);
  }

  private static Home create(String title, List<Link> links) {
    return new AutoValue_Home(title, links);
  }
}