package cn.mrzhqiang.randall.data;

import android.support.annotation.CheckResult;
import android.support.annotation.WorkerThread;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * 主页，由网页内容经由Jsoup解析后归档
 */
public final class Home {
  private static final String TAG = "Home";

  @CheckResult @WorkerThread public static Home parse(Document document) {
    Home home = new Home();

    Element body = document.body();
    Elements bodyList = body.children();
    Element first = bodyList.first();
    home.fromNode(first);

    return home;
  }

  private Element register;
  private Element login;
  private Node gamePath;
  private List<Element> serverList;
  private Node gameInfo;

  private Home() {
  }

  public String logoPath() {
    if (gamePath != null) {
      return gamePath.absUrl("src");
    }
    return null;
  }

  public boolean emptyServer() {
    return serverList == null || serverList.size() == 0;
  }

  public String serverName(int index) {
    if (serverList != null && serverList.size() > 0) {
      if (index < serverList.size()) {
        return serverList.get(index).text();
      }
    }
    return null;
  }

  public String gameInfo() {
    if (gameInfo != null) {
      return gameInfo.toString();
    }
    return null;
  }

  public String[] asNameList() {
    if (serverList != null && serverList.size() > 0) {
      String[] names = new String[serverList.size()];
      for (int i = 0; i < serverList.size(); i++) {
        names[i] = serverList.get(i).text();
      }
      return names;
    }
    return null;
  }

  private void fromNode(Node node) {
    String nodeName = node.nodeName();

    switch (nodeName) {
      case "p":
        for (Node child : node.childNodes()) {
          fromNode(child);
        }
        break;
      case "a":
        Element a = (Element) node;
        String text = a.text();
        if ("注册".equals(text)) {
          register = a;
        } else if ("登录".equals(text)) {
          login = a;
        } else if (text.startsWith("[")) {
          if (serverList == null) {
            serverList = new ArrayList<>();
          }
          serverList.add(a);
        } else {
          Log.d(TAG, "其他链接：" + a.toString());
        }
        break;
      case "img":
        String src = node.attr("src");
        if (src.endsWith(".gif")) {
          gamePath = node;
        } else {
          Log.d(TAG, "其他图片：" + node.toString());
        }
        break;
      case "br":
        Log.d(TAG, "出现一个换行符号");
        break;
      case "#text":
        String content = node.toString();
        if (content.startsWith("荣唐科技")) {
          gameInfo = node;
        }
        break;
      default:
        Log.d(TAG, "其他节点：" + node.toString());
        break;
    }
  }
}
