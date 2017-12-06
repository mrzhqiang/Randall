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
      List<Link> listGame = new ArrayList<>();
      Elements linkElements = bodyElement.select("a[href]");
      for (Element linkElement : linkElements) {
        String text = linkElement.text();
        if (text.contains("地狱之门")) {
          String href = linkElement.attr("href");
          if (href.contains(baseUrl)) {
            String suffix = linkElement.nextSibling().toString();
            if (suffix.contains("(")) {
              text += suffix;
            }
            listGame.add(Link.create(text, href));
          } else {
            builder.lastGame(Link.create(text, href));
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
