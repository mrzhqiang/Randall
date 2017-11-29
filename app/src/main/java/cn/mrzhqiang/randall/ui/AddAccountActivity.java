package cn.mrzhqiang.randall.ui;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.databinding.ActivityAddAccountBinding;
import cn.mrzhqiang.randall.ui.model.AccountModel;
import cn.mrzhqiang.randall.ui.model.RandallModel;
import cn.mrzhqiang.randall.net.Result;
import cn.mrzhqiang.randall.ui.viewmodel.EditAccountViewModel;
import cn.mrzhqiang.randall.ui.viewmodel.LoadingViewModel;
import com.squareup.picasso.Picasso;

/**
 * 新账号：其一、添加本地；其二、快速注册。
 */
public class AddAccountActivity extends AppCompatActivity {

  @BindingAdapter("gameLogo") public static void showGameLogo(ImageView view, String path) {
    if (path != null && path.length() > 0) {
      Picasso.with(view.getContext()).load(path).fit().noFade().noPlaceholder().into(view);
    }
  }

  public final EditAccountViewModel editAccountVM = new EditAccountViewModel();

  public final LoadingViewModel loadingVM = new LoadingViewModel() {
    @Override public void clickRetry() {
      super.clickRetry();
      loadHome();
    }
  };

  public final ObservableField<String> next = new ObservableField<>("下一步");
  public final ObservableField<String> logoPath = new ObservableField<>();
  public final ObservableField<String> gameInfo = new ObservableField<>();

  public final ObservableBoolean nextEnabled = new ObservableBoolean(true);

  public final View.OnClickListener nextAccount = new View.OnClickListener() {
    @Override public void onClick(View v) {
      final Context context = v.getContext();

      // 先重置错误提示
      editAccountVM.usernameError.set(null);
      editAccountVM.passwordError.set(null);

      final String username = editAccountVM.username.get();
      if (username == null || !editAccountVM.checkUsername(username)) {
        editAccountVM.usernameError.set("请输入7-15位数字或字母");
        return;
      }

      final String password = editAccountVM.password.get();
      if (password == null || !editAccountVM.checkPassword(password)) {
        editAccountVM.passwordError.set("请输入6-15位数字或字母");
        return;
      }

      // TODO 可以先插入数据，然后拿账号密码登录，根据返回值修改对应状态
      // 如果账号没有注册，返回-1，这里提示是否注册
      // （如果勾选了注册账号则直接调用注册接口——这只是一个设想，实现有难度）
      // 如果账号注册失败，则显示相关提示，不再有其他逻辑
      // 如果注册成功，提示是否登录：是则继续登录；否则关闭对话框
      accountModel.addAccount(username, password, new Result<Long>() {
        @Override public void onStart() {
          next.set("请稍等..");
          nextEnabled.set(false);
        }

        @Override public void onSuccessful(Long result) {
          if (result > 0) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
          }
        }

        @Override public void onFailed(String message) {
          Toast.makeText(context, "添加失败：" + message, Toast.LENGTH_SHORT).show();
          next.set("下一步");
          nextEnabled.set(true);
        }
      });
    }
  };

  private final AccountModel accountModel = new AccountModel();
  private final RandallModel randallModel = new RandallModel();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAddAccountBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_add_account);
    binding.setAddAccount(this);

    setSupportActionBar(binding.toolbar);

    // 加载主页数据
    loadHome();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    accountModel.cancel();
    randallModel.subscriptions.unsubscribe();
  }

  public void loadHome() {
    final Context context = this;

  }
}
