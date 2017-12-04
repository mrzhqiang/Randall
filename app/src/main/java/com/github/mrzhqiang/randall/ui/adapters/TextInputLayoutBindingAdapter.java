package com.github.mrzhqiang.randall.ui.adapters;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;

/**
 * 文字输入布局的绑定适配
 *
 * @author mrZQ
 */

public class TextInputLayoutBindingAdapter {
  @BindingAdapter("error")
  public static void setTextInputError(TextInputLayout layout, String error) {
    layout.setError(error);
    if (error != null) {
      layout.requestFocus();
    }
  }
}
