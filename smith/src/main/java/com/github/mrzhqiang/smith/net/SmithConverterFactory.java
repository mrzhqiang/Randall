package com.github.mrzhqiang.smith.net;

import android.support.annotation.NonNull;
import com.github.mrzhqiang.smith.net.html.Link;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import retrofit2.Converter;
import retrofit2.Retrofit;

final class SmithConverterFactory extends Converter.Factory {

  public static SmithConverterFactory create(String baseUrl) {
    return new SmithConverterFactory(baseUrl);
  }

  private final String baseUrl;

  private SmithConverterFactory(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    if (type == Login.class) {
      return new LoginConverter(baseUrl);
    }
    if (type == Game.class) {
      return new GameConverter();
    }
    return null;
  }

  private static class LoginConverter implements Converter<ResponseBody, Login> {

    final String baseUrl;
    final Login.Builder builder;

    LoginConverter(String baseUrl) {
      this.baseUrl = baseUrl;
      builder = Login.builder();
    }

    @Override public Login convert(@NonNull ResponseBody body) throws IOException {
      Element bodyElement = Jsoup.parse(body.string(), baseUrl).body();

      Element scriptElement = bodyElement.selectFirst("script");
      if (scriptElement != null) {
        String dataScript = scriptElement.data();
        if (!dataScript.isEmpty()) {
          // 注册接口出现跳转：账号密码正确
          dataScript = dataScript.split("=", 2)[1].replace("'", "").replace(";", "");
          dataScript = baseUrl + "/" + dataScript;
          return builder.script(dataScript)
              .lastGame(Link.empty())
              .listGame(Collections.emptyList())
              .build();
        }
      }

      String format = "(%s)";
      Node registerNode = bodyElement.child(0).childNode(0);
      if ("#text".equals(registerNode.nodeName())) {
        return builder.script("")
            .lastGame(Link.create("无效", String.format(format, registerNode.toString()), ""))
            .listGame(Collections.emptyList())
            .build();
      }

      Elements linkElements = bodyElement.select("a[href]");
      List<Link> listGame = new ArrayList<>();
      for (int i = 0; i < linkElements.size(); i++) {
        Element linkElement = linkElements.get(i);
        String text = linkElement.text();
        if (text.contains("地狱之门")) {
          String href = linkElement.attr("href");
          String suffix = linkElement.nextSibling().toString();
          if (href.contains(baseUrl)) {
            if (!suffix.contains("(")) {
              suffix = String.format(format, "上次登陆");
            }
            listGame.add(Link.create(text, suffix, href));
          } else {
            if (!suffix.contains("(")) {
              suffix = String.format(format, "推荐登陆");
            }
            listGame.add(0, Link.create(text, suffix, href));
          }
        }
      }
      return builder.script("").lastGame(listGame.remove(0)).listGame(listGame).build();
    }
  }

  private static class GameConverter implements Converter<ResponseBody, Game> {
    @Override public Game convert(@NonNull ResponseBody value) throws IOException {
      String html = value.string();
      Document document = Jsoup.parse(html);
      String title = document.title();
      String body = document.body().toString();
      return Game.create(title, body);
    }
  }
}
