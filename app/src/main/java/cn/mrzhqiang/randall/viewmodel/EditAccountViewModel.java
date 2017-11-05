package cn.mrzhqiang.randall.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import java.util.List;
import org.jsoup.nodes.Element;

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
      if (serverList == null || serverList.size() == 0) {
        Toast.makeText(context, "数据异常，无服务器列表", Toast.LENGTH_SHORT).show();
        return;
      }
      if (index < 0 || index >= serverList.size()) {
        // 下标越界的防范措施
        index = 0;
      }
      new AlertDialog.Builder(context).setTitle("服务器列表")
          .setSingleChoiceItems(asNameList(), index, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              index = which;
              serverName.set(serverList.get(index).text());
              dialog.dismiss();
            }
          })
          .show();
    }
  };
  /*Check*/
  public final CompoundButton.OnCheckedChangeListener registerChanged =
      new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          register = isChecked;
        }
      };

  private List<Element> serverList;
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

  public void update(@NonNull List<Element> serverList) {
    this.serverList = serverList;
    serverName.set(serverList.get(index).text());
  }

  public String serverPath() {
    // TODO
    return null;
  }

  public boolean isRegister() {
    return register;
  }

  /** 将服务器名字转为字符串数组，必须保证列表不为null以及大小不为0 */
  private String[] asNameList() {
    String[] names = new String[serverList.size()];
    for (int i = 0; i < serverList.size(); i++) {
      names[i] = serverList.get(i).text();
    }
    return names;
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
