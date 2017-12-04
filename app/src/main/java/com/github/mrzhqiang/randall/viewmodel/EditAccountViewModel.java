package com.github.mrzhqiang.randall.viewmodel;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;
import cn.mrzhqiang.helper.AccountHelper;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.model.AccountModel;
import com.github.mrzhqiang.smith.net.Result;

/**
 * 编辑账号：可输入的用户名和密码
 *
 * @author mrZQ
 */
public final class EditAccountViewModel {

  public final ObservableField<String> usernameError = new ObservableField<>();
  public final ObservableField<String> username = new ObservableField<>();
  public final ObservableField<String> passwordError = new ObservableField<>();
  public final ObservableField<String> password = new ObservableField<>();

  public final ObservableBoolean passwordEnabled = new ObservableBoolean(true);

  public final ObservableBoolean randomChecked = new ObservableBoolean(false);
  public final ObservableInt randomVisibility = new ObservableInt(View.GONE);

  public final View.OnClickListener clickRandom = v -> autoPassword();

  private final AccountModel accountModel = new AccountModel();

  public EditAccountViewModel() {
    username.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable observable, int i) {
        String value = username.get();
        if (checkUsername(value)) {
          usernameError.set(null);
        }
      }
    });
    password.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable observable, int i) {
        String value = password.get();
        if (checkPassword(value)) {
          passwordError.set(null);
        }
      }
    });
    randomChecked.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        boolean value = randomChecked.get();
        passwordEnabled.set(!value);
        randomVisibility.set(value ? View.VISIBLE : View.GONE);
        if (value) {
          autoPassword();
        }
      }
    });
  }

  public void addAccount(final Context context) {
    // 先重置错误提示
    usernameError.set(null);
    passwordError.set(null);

    if (!checkUsername(username.get())) {
      usernameError.set("请输入7-15位数字或字母");
      return;
    }

    if (!checkPassword(password.get())) {
      passwordError.set("请输入6-15位数字或字母");
      return;
    }

    accountModel.addAccount(username.get(), password.get(), new Result<Account>() {
      @Override public void onSuccessful(Account result) {
        if (result != null) {
          Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
        }
      }

      @Override public void onFailed(String message) {
        Toast.makeText(context, "添加失败：" + message, Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void cancelAll() {
    accountModel.cancelAll();
  }

  /** 检查账户是否有效 */
  private boolean checkUsername(String username) {
    if (username == null) {
      return false;
    }
    int length = username.length();
    return length >= 7 && length <= 15;
  }

  /** 检查密码是否有效 */
  private boolean checkPassword(String password) {
    if (password == null) {
      return false;
    }
    int length = password.length();
    return length >= 6 && length <= 15;
  }

  private void autoPassword() {
    // [6,16)
    int size = (int) (6 + Math.random() * 10);
    password.set(AccountHelper.createPassword(size));
  }
}