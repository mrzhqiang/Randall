package cn.mrzhqiang.randall.net.converter;

import android.support.annotation.NonNull;
import cn.mrzhqiang.randall.net.Randall;
import java.io.IOException;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import retrofit2.Converter;

/**
 * html/wml内容解析为Jsoup的Document对象
 */
final class DocumentResponseBodyConverter implements Converter<ResponseBody, Document> {

  static final DocumentResponseBodyConverter INSTANCE = new DocumentResponseBodyConverter();

  @Override public Document convert(@NonNull ResponseBody value) throws IOException {
    String string = value.string();
    if (string != null) {
      return Jsoup.parse(string, Randall.BASE_URL);
    }
    return null;
  }
}
