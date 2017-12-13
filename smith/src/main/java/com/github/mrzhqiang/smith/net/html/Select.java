package com.github.mrzhqiang.smith.net.html;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import java.util.Collections;
import java.util.List;

@AutoValue public abstract class Select implements Parcelable {
  public static final Select EMPTY = empty();

  public abstract String name();

  public abstract String value();

  public abstract List<Option> optionList();

  public static Select empty() {
    return create("", "", Collections.emptyList());
  }

  public static Select create(String name, String value, List<Option> optionList) {
    return new AutoValue_Select(name, value, optionList);
  }
}
