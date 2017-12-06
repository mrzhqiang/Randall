package com.github.mrzhqiang.randall.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;
import cn.mrzhqiang.helper.AccountHelper;
import com.github.mrzhqiang.randall.ui.RandallActivity;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.model.AccountModel;
import com.github.mrzhqiang.smith.net.Login;
import com.github.mrzhqiang.smith.net.Result;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑账号：可输入的用户名和密码，高级功能：范围建号+随机密码
 *
 * @author mrZQ
 */
public final class EditAccountViewModel {

  public final ObservableField<String> usernameText = new ObservableField<>();
  public final ObservableField<String> passwordText = new ObservableField<>();
  public final ObservableField<String> usernameError = new ObservableField<>();
  public final ObservableField<String> passwordError = new ObservableField<>();

  public final ObservableField<String> startIndex = new ObservableField<>();
  public final ObservableField<String> createCount = new ObservableField<>();

  public final ObservableBoolean usernameEnabled = new ObservableBoolean(true);
  public final ObservableBoolean passwordEnabled = new ObservableBoolean(true);

  public final ObservableBoolean batchChecked = new ObservableBoolean(false);
  public final ObservableBoolean randomChecked = new ObservableBoolean(false);

  public final ObservableInt loadingVisibility = new ObservableInt(View.GONE);
  public final ObservableInt inputVisibility = new ObservableInt(View.VISIBLE);
  public final ObservableInt advancedVisibility = new ObservableInt(View.GONE);
  public final ObservableInt createVisibility = new ObservableInt(View.INVISIBLE);
  public final ObservableInt startVisibility = new ObservableInt(View.INVISIBLE);
  public final ObservableInt randomVisibility = new ObservableInt(View.INVISIBLE);

  public final View.OnClickListener clickRandom = v -> autoPassword();

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
  private final Observable.OnPropertyChangedCallback batchCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override public void onPropertyChanged(Observable sender, int propertyId) {
          boolean value = batchChecked.get();
          usernameEnabled.set(!value);
          randomChecked.set(value);
          startVisibility.set(value ? View.VISIBLE : View.INVISIBLE);
          createVisibility.set(value ? View.VISIBLE : View.INVISIBLE);
          startIndex.set("0");
          createCount.set("2");
        }
      };
  private final Observable.OnPropertyChangedCallback randomCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override public void onPropertyChanged(Observable sender, int propertyId) {
          boolean value = randomChecked.get();
          passwordEnabled.set(!value);
          randomVisibility.set(value ? View.VISIBLE : View.INVISIBLE);
          if (value) {
            autoPassword();
          }
        }
      };

  private final AccountModel accountModel = new AccountModel();

  public EditAccountViewModel() {
    usernameText.addOnPropertyChangedCallback(usernameCallback);
    passwordText.addOnPropertyChangedCallback(passwordCallback);
    batchChecked.addOnPropertyChangedCallback(batchCallback);
    randomChecked.addOnPropertyChangedCallback(randomCallback);
  }

  public void advanced(boolean isShow) {
    advancedVisibility.set(isShow ? View.VISIBLE : View.GONE);
    if (!isShow) {
      // 隐藏的时候，将所有的选中状态归位
      batchChecked.set(false);
      randomChecked.set(false);
    }
  }

  public void login(Context context) {
    // 清空错误提示
    usernameError.set(null);
    passwordError.set(null);

    String username = usernameText.get();
    String password = passwordText.get();
    if (!checkUsername(username)) {
      usernameError.set("请输入7-15位数字或字母");
      return;
    }

    if (!checkPassword(password)) {
      passwordError.set("请输入6-15位数字或字母");
      return;
    }

    boolean batch = batchChecked.get();
    // 非批量创建，则按照正常流程走一走
    if (!batch) {
      showLoading();
      Account account = Account.create(username, password, Account.Status.DEFAULT);
      accountModel.create(account, new Result<Login>() {
        @Override public void onSuccessful(Login result) {
          if (result.lastGame() != null) {
            hideLoading();
            Intent intent = new Intent(context, RandallActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
          } else {
            onFailed("登陆失败：" + result);
          }
        }

        @Override public void onFailed(String message) {
          super.onFailed(message);
          hideLoading();
          Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
      });
      return;
    }

    // 开始批量创建的流程
    int start = Integer.parseInt(startIndex.get());
    int count = Integer.parseInt(createCount.get());
    if (start < 0) {
      Toast.makeText(context, "错误的起始编号：" + start + ", 请设置0-99的整数", Toast.LENGTH_SHORT).show();
      return;
    }
    if (count < 2) {
      Toast.makeText(context, "错误的批量创建：" + count + ", 请设置2-99的整数", Toast.LENGTH_SHORT).show();
      return;
    }
    // 用户名不区分大小写，但密码区分
    String[] names = AccountHelper.autoUsernames(username, start, count);
    List<Account> accounts = new ArrayList<>();
    boolean random = randomChecked.get();
    for (String name : names) {
      // 如果用户名符合要求，才进行账户的创建，这样避免了很多麻烦
      username = name;
      if (checkUsername(username)) {
        // 如果是随机密码，则每个账号的密码都随机一下；否则全部都一样
        if (random) {
          autoPassword();
        }
        password = passwordText.get();
        accounts.add(Account.create(username, password, Account.Status.DEFAULT));
      }
    }
    showLoading();
    accountModel.create(accounts, new Result<List<Login>>() {
      @Override public void onSuccessful(List<Login> result) {
        if (result.size() > 0) {
          hideLoading();
          Intent intent = new Intent(context, RandallActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          context.startActivity(intent);
        } else {
          onFailed("批量创建无效");
        }
      }

      @Override public void onFailed(String message) {
        super.onFailed(message);
        hideLoading();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void cancelAll() {
    usernameText.removeOnPropertyChangedCallback(usernameCallback);
    passwordText.removeOnPropertyChangedCallback(passwordCallback);
    batchChecked.removeOnPropertyChangedCallback(batchCallback);
    randomChecked.removeOnPropertyChangedCallback(randomCallback);
    accountModel.cancelAll();
  }

  private void showLoading() {
    loadingVisibility.set(View.VISIBLE);
    inputVisibility.set(View.GONE);
  }

  private void hideLoading() {
    loadingVisibility.set(View.GONE);
    inputVisibility.set(View.VISIBLE);
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

  private void autoPassword() {
    // [6,16)
    int size = (int) (6 + Math.random() * 10);
    passwordText.set(AccountHelper.createPassword(size));
  }
}