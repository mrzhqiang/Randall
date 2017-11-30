package com.github.mrzhqiang.smith.net.html;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoValue public abstract class Form implements Parcelable {

  public abstract String action();

  public abstract String method();

  public abstract List<Input> inputList();

  public static Form create(String action, String method, List<Input> inputList) {
    return new AutoValue_Form(action, method, inputList);
  }

  public Map<String, String> submit(@NonNull String username, @NonNull String password) {
    Map<String, String> submit = new HashMap<>();
    for (Input input : inputList()) {
      if (Input.Type.HIDDEN.check(input)) {
        submit.put(input.name(), input.value());
      } else if (Input.Type.TEXT.check(input)) {
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
        }
      } else if (Input.Type.SUBMIT.check(input)) {
        submit.put("next", input.value());
      }
    }
    return submit;
  }

  private String checkLength(Input input, String value) {
    String maxLength = input.maxLength();
    if (maxLength != null && maxLength.length() > 0) {
      // maxLength应当包括自身，而substring的作用域是（startIndex, endIndex]
      int length = Math.min(Integer.parseInt(maxLength) + 1, value.length());
      value = value.substring(0, length);
    }
    return value;
  }
}
