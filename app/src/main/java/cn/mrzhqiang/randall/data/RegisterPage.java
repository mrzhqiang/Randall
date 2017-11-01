package cn.mrzhqiang.randall.data;

import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 注册页面
 */
public final class RegisterPage {
  private static final String TAG = "RegisterPage";

  @CheckResult @WorkerThread public static RegisterPage parse(Document document) {
    RegisterPage registerPage = new RegisterPage();

    Element body = document.body();
    Elements form = body.getElementsByTag("form");
    registerPage.fromNodes(form);

    return registerPage;
  }

  @Nullable public static String findPath(Element registerPath) {
    String href = registerPath.attr("href");
    String[] paths = href.split("\\?");
    return paths.length > 0 ? paths[0] : null;
  }

  @Nullable public static Map<String, String> findQueryMap(Element registerPath) {
    String href = registerPath.attr("href");
    String[] paths = href.split("\\?");
    if (paths.length > 1) {
      String query = paths[1];
      String[] temp = query.split("=");
      // TODO 假若链接多出来类似 & 这样的路径，那么需要考虑分割 &
      if (temp.length > 0) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(temp[0], temp[1]);
        return queryMap;
      }
    }
    return null;
  }

  private String path;
  private Map<String, String> queryMap;
  private Element input1;
  private Element input2;

  private RegisterPage() {
  }

  public String path() {
    return path;
  }

  public Map<String, String> queryMap() {
    queryMap.put(input1.attr("name"), input1.attr("value"));
    queryMap.put(input2.attr("name"), input2.attr("value"));
    return queryMap;
  }

  public void input(String value1, String value2) {
    input1.attr("value", value1);
    input2.attr("value", value2);
  }

  private void fromNodes(Elements form) {
    String action = form.attr("action");
    String[] paths = action.split("\\?");
    path = paths.length > 0 ? paths[0] : null;
    if (paths.length > 1) {
      String query = paths[1];
      String[] temp = query.split("=");
      // TODO 假若链接多出来类似 & 这样的路径，那么需要考虑分割 &
      if (temp.length > 0) {
        queryMap = new HashMap<>();
        queryMap.put(temp[0], temp[1]);
      }
    }
    Elements inputs = form.select("input");
    for (int i = 0; i < inputs.size(); i++) {
      Element input = inputs.get(i);
      String type = input.attr("type");
      if ("text".equals(type)) {
        if (input1 == null) {
          input1 = input;
        }
        if (input2 == null) {
          input2 = input;
        }
        // TODO 若输入参数有改动，在这里修改
      } else if ("hidden".equals(type)) {
        String key = input.attr("name");
        String value = input.attr("value");
        queryMap.put(key, value == null ? "" : value);
      }
    }
  }
}
