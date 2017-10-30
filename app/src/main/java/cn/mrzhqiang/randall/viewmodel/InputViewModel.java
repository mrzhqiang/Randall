package cn.mrzhqiang.randall.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;

/**
 * 单个输入框
 */
public class InputViewModel {

  public enum Type {
    USERNAME, PASSWORD,
  }

  private static final String DIGITS =
      "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

  @BindingAdapter("error")
  public static void setTextInputError(TextInputLayout layout, String error) {
    layout.setError(error);
    if (error != null) {
      layout.requestFocus();
    }
  }

  @BindingAdapter("passwordToggleEnabled")
  public static void setPasswordToggleEnabled(TextInputLayout layout, boolean enabled) {
    layout.setPasswordVisibilityToggleEnabled(enabled);
  }

  /* TextInputLayout */
  public final ObservableBoolean counterEnabled = new ObservableBoolean(false);
  public final ObservableInt counterMaxLength = new ObservableInt(0);
  public final ObservableField<String> error = new ObservableField<>(null);
  public final ObservableBoolean errorEnabled = new ObservableBoolean(false);
  public final ObservableBoolean passwordToggleEnabled = new ObservableBoolean(false);
  /* TextInputEditText */
  public final ObservableField<String> digits = new ObservableField<>();
  public final ObservableBoolean enabled = new ObservableBoolean(true);
  public final ObservableField<String> hint = new ObservableField<>("请输入");
  public final ObservableInt inputType = new ObservableInt(InputType.TYPE_CLASS_TEXT);
  public final ObservableField<String> text = new ObservableField<>();

  public InputViewModel(Type type) {
    switch (type) {
      case USERNAME:
        initUsername();
        break;
      case PASSWORD:
        initPassword();
        break;
      default:
        break;
    }
  }

  public String takeText() {
    return text.get();
  }

  public void showError(String value) {
    error.set(value);
  }

  public void changeEnabled(boolean value) {
    enabled.set(value);
  }

  private void initUsername() {
    counterEnabled.set(true);
    counterMaxLength.set(15);
    errorEnabled.set(true);
    digits.set(DIGITS);
    hint.set("用户名/uid");
    inputType.set(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
  }

  private void initPassword() {
    errorEnabled.set(true);
    passwordToggleEnabled.set(true);
    digits.set(DIGITS);
    hint.set("密码(6–15位数字或字母)");
    inputType.set(InputType.TYPE_TEXT_VARIATION_PASSWORD);
  }
}
