package com.github.mrzhqiang.smith.net;

import android.support.annotation.NonNull;
import com.github.mrzhqiang.smith.net.html.Link;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Haowanba源码转换为java对象的工厂
 *
 * @author mrZQ
 */
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
      String html = body.string();
      Document document = Jsoup.parse(html, baseUrl);
      String title = document.title();
      builder.title(title);
      Element bodyElement = document.body();
      Element scriptElement = bodyElement.selectFirst("script");
      if (scriptElement != null) {
        String dataScript = scriptElement.data();
        if (!dataScript.isEmpty()) {
          // 账号密码正确，跳转一下
          dataScript = dataScript.split("=", 2)[1].replace("'", "").replace(";", "");
          dataScript = baseUrl + "/" + dataScript;
          builder.script(dataScript);
        }
      } else {
        Node registerNode = bodyElement.child(0).childNode(0);
        if ("#text".equals(registerNode.nodeName())) {
          // 注册失败，通常是账号已存在，但密码不对；如果账号密码正确，会有一个跳转的script
          builder.title(registerNode.toString());
        }
      }
      List<Link> listGame = new ArrayList<>();
      Elements linkElements = bodyElement.select("a[href]");
      for (int i = 0; i < linkElements.size(); i++) {
        Element linkElement = linkElements.get(i);
        String text = linkElement.text();
        if (text.contains("地狱之门")) {
          String href = linkElement.attr("href");
          Link.Builder linkBuilder = Link.builder().text(text).href(href);
          if (href.contains(baseUrl)) {
            if (i == 0) {
              // 说明是新注册用户
              linkBuilder.suffix("(推荐登陆)");
              builder.lastGame(linkBuilder.build());
              builder.title("注册成功");
            } else {
              String suffix = linkElement.nextSibling().toString();
              if (suffix.contains("(")) {
                linkBuilder.suffix(suffix);
              }
              listGame.add(linkBuilder.build());
            }
          } else {
            linkBuilder.suffix("(推荐登陆)");
            builder.lastGame(linkBuilder.build());
            builder.title("登陆成功");
          }
        }
      }
      builder.listGame(listGame);
      return builder.build();
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
