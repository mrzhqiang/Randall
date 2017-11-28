package cn.mrzhqiang.randall.html;

import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@AutoValue public abstract class Form {

  public abstract String action();

  public abstract String method();

  public abstract List<Input> inputList();

  public Map<String, String> submit(String username, String password) {
    Map<String, String> submit = new HashMap<>();
    for (Input input : inputList()) {
      if (input.checkType(Input.Type.HIDDEN)) {
        submit.put(input.name(), input.value());
      } else if (input.checkType(Input.Type.TEXT)) {
        String name = input.name();
        switch (name) {
          case "username":
          case "name":
            username = checkLength(input, username);
            submit.put(name, username);
            break;
          case "password":
          case "pwd":
            password = checkLength(input, password);
            submit.put(name, password);
            break;
          default:
            break;
        }
      }
    }
    return submit;
  }

  private String checkLength(Input input, String value) {
    String maxLength = input.maxLength();
    if (maxLength != null && maxLength.length() > 0) {
      // maxLength应当包括自身
      int length = Math.min(Integer.parseInt(maxLength) + 1, value.length());
      // （startIndex, endIndex]
      value = value.substring(0, length);
    }
    return value;
  }

  public static Form from(Element element) {
    if (element == null) {
      return null;
    }
    String action = element.absUrl("action");
    String method = element.attr("method");
    Elements inputs = element.select("input");
    List<Input> inputList = new ArrayList<>(inputs.size());
    for (Element input : inputs) {
      inputList.add(Input.from(input));
    }
    return Form.create(action, method, inputList);
  }

  private static Form create(String action, String method, List<Input> inputList) {
    return new AutoValue_Form(action, method, inputList);
  }
}
