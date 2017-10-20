package cn.mrzhqiang.randall.net.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import org.jsoup.nodes.Document;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Jsoup解析工厂，自动将网页内容解析为Jsoup对象，目前仅限于response
 */
public final class JsoupConverterFactory extends Converter.Factory {

  public static JsoupConverterFactory create() {
    return new JsoupConverterFactory();
  }

  private JsoupConverterFactory() {
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    if (type == Document.class) {
      return DocumentResponseBodyConverter.INSTANCE;
    }
    return null;
  }
}
