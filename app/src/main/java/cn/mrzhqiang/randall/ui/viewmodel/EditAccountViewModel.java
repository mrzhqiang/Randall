package cn.mrzhqiang.randall.ui.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.CompoundButton;

/**
 * 编辑账号：可输入的用户名和密码，可选择的服务器列表，可勾选的是否注册选项
 */
public final class EditAccountViewModel {

  @BindingAdapter("error")
  public static void setTextInputError(TextInputLayout layout, String error) {
    layout.setError(error);
    if (error != null) {
      layout.requestFocus();
    }
  }

  /*Input*/
  public final ObservableField<String> usernameError = new ObservableField<>();
  public final ObservableField<String> username = new ObservableField<>();
  public final ObservableField<String> passwordError = new ObservableField<>();
  public final ObservableField<String> password = new ObservableField<>();
  /*Choose*/
  public final ObservableField<String> serverName = new ObservableField<>();
  public final View.OnClickListener chooseServer = new View.OnClickListener() {
    @Override public void onClick(View v) {
      Context context = v.getContext();

    }
  };
  /*Check*/
  public final CompoundButton.OnCheckedChangeListener registerChanged =
      new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          register = isChecked;
        }
      };

  private int index = 0;
  private boolean register = false;

  public EditAccountViewModel() {
    username.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable observable, int i) {
        String value = username.get();
        if (value != null && checkUsername(value)) {
          usernameError.set(null);
        }
      }
    });
    password.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable observable, int i) {
        String value = password.get();
        if (value != null && checkPassword(value)) {
          passwordError.set(null);
        }
      }
    });
  }

  public String serverPath() {
    // TODO
    return null;
  }

  public boolean isRegister() {
    return register;
  }

  /** 检查账户是否有效 */
  public boolean checkUsername(String username) {
    int length = username.length();
    return length >= 7 && length <= 15;
  }

  /** 检查密码是否有效 */
  public boolean checkPassword(String password) {
    int length = password.length();
    return length >= 6 && length <= 15;
  }
}
