package cn.mrzhqiang.randall.net;

import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * html/wml内容解析为Jsoup的Document对象
 */
final class StringResponseBodyConverter implements Converter<ResponseBody, String> {

  static final StringResponseBodyConverter INSTANCE = new StringResponseBodyConverter();

  @Override public String convert(@NonNull ResponseBody value) throws IOException {
    return value.string();
  }
}
