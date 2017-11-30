package com.github.mrzhqiang.smith.net;

import android.support.annotation.NonNull;
import com.github.mrzhqiang.smith.net.html.Form;
import com.github.mrzhqiang.smith.net.html.Input;
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
    if (type == Home.class) {
      return new HomeConverter(baseUrl);
    }
    if (type == Login.class) {
      return new LoginConverter(baseUrl);
    }
    if (type == Game.class) {
      return new GameConverter();
    }
    return null;
  }

  private static class HomeConverter implements Converter<ResponseBody, Home> {

    final String baseUrl;

    HomeConverter(String baseUrl) {
      this.baseUrl = baseUrl;
    }

    @Override public Home convert(@NonNull ResponseBody value) throws IOException {
      String html = value.string();
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
  }

  private static class LoginConverter implements Converter<ResponseBody, Login> {

    final String baseUrl;

    LoginConverter(String baseUrl) {
      this.baseUrl = baseUrl;
    }

    @Override public Login convert(@NonNull ResponseBody body) throws IOException {
      String html = body.string();
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
