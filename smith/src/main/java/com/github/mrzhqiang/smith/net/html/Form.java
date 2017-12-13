package com.github.mrzhqiang.smith.net.html;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue public abstract class Form implements Parcelable {

  public abstract String action();

  public abstract String method();

  public abstract List<Input> inputList();

  public abstract List<Select> selectList();

  public static Form create(String action, String method, List<Input> inputList,
      List<Select> selectList) {
    return new AutoValue_Form(action, method, inputList, selectList);
  }
}
