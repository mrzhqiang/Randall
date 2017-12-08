package com.github.mrzhqiang.randall.ui.adapters;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;

public class TextInputLayoutBindingAdapter {
  @BindingAdapter("error")
  public static void setTextInputError(TextInputLayout layout, String error) {
    layout.setError(error);
    if (error != null) {
      layout.requestFocus();
    }
  }
}
