package cn.mrzhqiang.randall.ui;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.databinding.ActivityAddUidBinding;
import cn.mrzhqiang.randall.model.AccountModel;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 添加账号，其一、便捷注册；其二、保存书签。
 */
public class AddUidActivity extends AppCompatActivity {

  @BindingAdapter("error")
  public static void showEditTextError(TextInputLayout layout, String error) {
    layout.setError(error);
  }

  public final ObservableField<String> username = new ObservableField<>();
  public final ObservableField<String> password = new ObservableField<>();
  public final ObservableField<String> usernameError = new ObservableField<>();
  public final ObservableField<String> passwordError = new ObservableField<>();
  public final ObservableField<String> serverName = new ObservableField<>();

  public final View.OnClickListener chooseServer = new View.OnClickListener() {
    @Override public void onClick(View v) {
      // 弹窗显示服务器列表
    }
  };

  public final View.OnClickListener login = new View.OnClickListener() {
    @Override public void onClick(View v) {
      // 登录
      final Context context = v.getContext();
      final Account account = new Account();
      account.username = username.get();
      account.password = password.get();
      account.alias = serverName.get();
      accountModel.addAccount(account, new Action1<Boolean>() {
        @Override public void call(Boolean aBoolean) {
          if (aBoolean) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  };

  public final View.OnClickListener register = new View.OnClickListener() {
    @Override public void onClick(View v) {
      // 注册
    }
  };

  private final AccountModel accountModel = new AccountModel();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAddUidBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_uid);
    binding.setAddUid(this);

    setSupportActionBar(binding.toolbar);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
