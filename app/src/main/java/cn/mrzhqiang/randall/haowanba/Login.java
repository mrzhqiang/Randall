package cn.mrzhqiang.randall.haowanba;

import cn.mrzhqiang.randall.html.Form;
import cn.mrzhqiang.randall.html.Link;
import com.google.auto.value.AutoValue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AutoValue
public abstract class Login {

  public abstract String title();

  public abstract Form form();

  public abstract Link link();

  public boolean isLogin() {
    return link().text().equals("登录>");
  }

  public static Login from(Document document) {
    String title = document.title();
    Element body = document.body();

    Elements elements = body.getElementsByTag("form");
    // 表单通常只有一个标签
    Form form = Form.from(elements.first());

    Elements links = body.select("a[href]");
    Link link = null;
    for (Element element : links) {
      if (element.hasText()) {
        String s = element.text();
        if (s.equals("注册")) {
          link = Link.from(element);
          title = "登录";
        } else if (s.equals("登录>")) {
          link = Link.from(element);
          title = "注册";
        }
      }
    }
    return Login.create(title, form, link);
  }

  private static Login create(String title, Form form, Link link) {
    return new AutoValue_Login(title, form, link);
  }
}