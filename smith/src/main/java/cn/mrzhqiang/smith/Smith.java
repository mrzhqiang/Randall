package cn.mrzhqiang.smith;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import cn.mrzhqiang.smith.haowanba.Game;
import cn.mrzhqiang.smith.haowanba.Home;
import cn.mrzhqiang.smith.haowanba.Login;
import cn.mrzhqiang.smith.html.Form;
import cn.mrzhqiang.smith.html.Input;
import cn.mrzhqiang.smith.html.Link;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class Smith {
  private final String baseUrl;

  private Smith(Builder builder) {
    this.baseUrl = builder.baseUrl;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  @WorkerThread @NonNull public Home parseHome(@NonNull String html) {
    Document document = Jsoup.parse(html, baseUrl);
    String title = document.title();
    Elements elements = document.body().select("a[href]");
    List<Link> links = new ArrayList<>(elements.size());
    for (Element element : elements) {
      String text = element.text();
      String href = element.absUrl("href");
      links.add(Link.create(text, href));
    }
    return Home.create(title, links);
  }

  @WorkerThread @NonNull public Login parseLogin(@NonNull String html) {
    Document document = Jsoup.parse(html, baseUrl);
    String title = document.title();

    Element bodyElement = document.body();
    // 表单通常只有一个标签
    Element formElement = bodyElement.getElementsByTag("form").first();
    String action = formElement.absUrl("action");
    String method = formElement.attr("method");
    Elements inputElements = formElement.select("input");
    List<Input> inputList = new ArrayList<>(inputElements.size());
    for (Element inputElement : inputElements) {
      String type = inputElement.attr("type");
      String name = inputElement.attr("name");
      String value = inputElement.attr("value");
      String maxLength = inputElement.attr("maxlength");
      inputList.add(Input.create(type, name, value, maxLength));
    }
    Form form = Form.create(action, method, inputList);

    Link link = null;
    Elements linkElements = bodyElement.select("a[href]");
    for (Element linkElement : linkElements) {
      if (linkElement.hasText()) {
        String text = linkElement.text();
        if (Login.Type.SIGN_UP.check(text) || Login.Type.SIGN_IN.check(text)) {
          String href = linkElement.absUrl("href");
          link = Link.create(text, href);
          break;
        }
      }
    }
    return Login.create(title, form, link);
  }

  // TODO 服务器区页面

  // TODO 进入游戏页面

  // TODO 角色管理页面

  // TODO 提示错误页面

  @WorkerThread @NonNull public Game parseGame(@NonNull String html) {
    Document document = Jsoup.parse(html, baseUrl);
    String title = document.title();
    String body = document.body().toString();
    return Game.create(title, body);
  }

  public static class Builder {
    private String baseUrl;

    public Builder() {
    }

    public Builder(Smith smith) {
      baseUrl = smith.baseUrl;
    }

    public Builder baseUrl(String value) {
      baseUrl = value;
      return this;
    }

    public Smith build() {
      return new Smith(this);
    }
  }
}
