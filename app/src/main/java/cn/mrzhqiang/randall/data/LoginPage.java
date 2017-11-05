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
 * 登录页面
 */
public final class LoginPage {
  private static final String TAG = "LoginPage";

  @CheckResult @WorkerThread public static LoginPage parse(Document document) {
    LoginPage homePage = new LoginPage();

    Element body = document.body();
    Elements form = body.getElementsByTag("form");
    homePage.fromNodes(form);

    return homePage;
  }

  @Nullable public static String findPath(Element loginElement) {
    String href = loginElement.attr("href");
    String[] paths = href.split("\\?");
    return paths.length > 0 ? paths[0] : null;
  }

  @Nullable public static Map<String, String> findQueryMap(Element loginElement) {
    String href = loginElement.attr("href");
    String[] paths = href.split("\\?");
    if (paths.length > 1) {
      String query = paths[1];

      // TODO 假若多出来类似 & 这样的路径，那么需要考虑先分割 &
      // limit为2，防止temp数组下标越界
      String[] temp = query.split("=", 2);
      Map<String, String> queryMap = new HashMap<>();
      // TODO 如果=分割的字符串数组有问题，那么需要提升limit阀值
      queryMap.put(temp[0], temp[1]);
      return queryMap;
    }
    return null;
  }

  private String path;
  private Map<String, String> queryMap;
  private Element input1;
  private Element input2;

  private LoginPage() {
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
    if (path != null && path.contains(".")) {
      // 去掉.及前面的字符串
      path = path.substring(path.indexOf(".")+1);
    }
    if (paths.length > 1) {
      String query = paths[1];
      String[] temp = query.split("=", 2);
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
          continue;
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
