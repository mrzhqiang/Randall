package cn.mrzhqiang.randall.net;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Wml源码转换工厂，省去对Retrofit的String解析依赖
 */
public final class WmlStringConverterFactory extends Converter.Factory {

  public static WmlStringConverterFactory create() {
    return new WmlStringConverterFactory();
  }

  private WmlStringConverterFactory() {
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    if (type == String.class) {
      return StringResponseBodyConverter.INSTANCE;
    }
    return null;
  }
}
