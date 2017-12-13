package com.github.mrzhqiang.randall.viewmodel;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.mrzhqiang.helper.AccountHelper;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.net.Login;

public final class EditAccountViewModel {

  public final ObservableField<String> usernameText = new ObservableField<>();
  public final ObservableField<String> passwordText = new ObservableField<>();
  public final ObservableField<String> usernameError = new ObservableField<>();
  public final ObservableField<String> passwordError = new ObservableField<>();

  public final ObservableBoolean usernameEnabled = new ObservableBoolean(true);
  public final ObservableBoolean passwordEnabled = new ObservableBoolean(true);

  private final Observable.OnPropertyChangedCallback usernameCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override public void onPropertyChanged(Observable sender, int propertyId) {
          String username = usernameText.get();
          if (checkUsername(username)) {
            usernameError.set(null);
          }
        }
      };
  private final Observable.OnPropertyChangedCallback passwordCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override public void onPropertyChanged(Observable sender, int propertyId) {
          String password = passwordText.get();
          if (checkPassword(password)) {
            passwordError.set(null);
          }
        }
      };

  public EditAccountViewModel() {
    usernameText.addOnPropertyChangedCallback(usernameCallback);
    passwordText.addOnPropertyChangedCallback(passwordCallback);
  }

  public void disabled() {
    usernameEnabled.set(false);
    passwordEnabled.set(false);
  }

  public void enabled() {
    usernameEnabled.set(true);
    passwordEnabled.set(true);
  }

  public void randomPassword(int size) {
    passwordText.set(AccountHelper.createPassword(size));
  }

  public void setAccount(@NonNull Account account) {
    usernameText.set(account.username());
    passwordText.set(account.password());
    usernameEnabled.set(false);
  }

  @Nullable public Account getAccount() {
    usernameError.set(null);
    passwordError.set(null);
    String username = usernameText.get();
    String password = passwordText.get();
    if (!checkUsername(username)) {
      usernameError.set("请输入7-15位数字或字母");
      return null;
    }
    if (!checkPassword(password)) {
      passwordError.set("请输入6-15位数字或字母");
      return null;
    }
    return Account.create(username, password, "", Login.EMPTY);
  }

  public void cancelAll() {
    usernameText.removeOnPropertyChangedCallback(usernameCallback);
    passwordText.removeOnPropertyChangedCallback(passwordCallback);
  }

  private boolean checkUsername(String username) {
    if (username == null) {
      return false;
    }
    int length = username.length();
    return length >= 7 && length <= 15;
  }

  private boolean checkPassword(String password) {
    if (password == null) {
      return false;
    }
    int length = password.length();
    return length >= 6 && length <= 15;
  }
}