package cn.mrzhqiang.randall.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 * 编辑账号
 */
public final class EditAccountViewModel {

  @BindingAdapter("error")
  public static void setTextInputError(TextInputLayout layout, String error) {
    layout.setError(error);
    if (error != null) {
      layout.requestFocus();
    }
  }

  public final ObservableField<String> usernameError = new ObservableField<>();
  public final ObservableField<String> username = new ObservableField<>();
  public final ObservableField<String> passwordError = new ObservableField<>();
  public final ObservableField<String> password = new ObservableField<>();
  public final ObservableField<String> serverName = new ObservableField<>();

  /* TextInputEditText */
  public final ObservableField<String> digits = new ObservableField<>();
  public final ObservableBoolean enabled = new ObservableBoolean(true);
  public final ObservableField<String> hint = new ObservableField<>("请输入");
  public final ObservableInt inputType = new ObservableInt(InputType.TYPE_CLASS_TEXT);
  public final ObservableField<String> text = new ObservableField<>();

  public final View.OnClickListener chooseServer = new View.OnClickListener() {
    @Override public void onClick(View v) {
      Context context = v.getContext();
      new AlertDialog.Builder(context).setTitle("选择服务器")
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

  private String[] asNameList() {
    if (serverList != null && serverList.size() > 0) {
      String[] names = new String[serverList.size()];
      for (int i = 0; i < serverList.size(); i++) {
        names[i] = serverList.get(i).text();
      }
      return names;
    }
    return null;
  }
}
