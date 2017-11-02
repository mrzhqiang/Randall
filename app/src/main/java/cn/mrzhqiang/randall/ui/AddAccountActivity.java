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
import cn.mrzhqiang.randall.data.HomePage;
import cn.mrzhqiang.randall.data.LoginPage;
import cn.mrzhqiang.randall.databinding.ActivityAddAccountBinding;
import cn.mrzhqiang.randall.model.AccountModel;
import cn.mrzhqiang.randall.model.RandallModel;
import cn.mrzhqiang.randall.net.Result;
import cn.mrzhqiang.randall.viewmodel.EditAccountViewModel;
import cn.mrzhqiang.randall.viewmodel.LoadingViewModel;
import com.squareup.picasso.Picasso;

/**
 * 新账号：其一、添加本地；其二、快速注册
 */
public class AddAccountActivity extends AppCompatActivity {

  @BindingAdapter("gameLogo") public static void showGameLogo(ImageView view, String path) {
    if (path != null && path.length() > 0) {
      Picasso.with(view.getContext()).load(path).fit().noFade().noPlaceholder().into(view);
    }
  }

  public final EditAccountViewModel editAccountVM = new EditAccountViewModel();

  public final LoadingViewModel loadingVM = new LoadingViewModel() {
    @Override public void onRetry(Context context) {
      super.onRetry(context);
      loadHome(context);
    }
  };

  public final ObservableField<String> next = new ObservableField<>("下一步");
  public final ObservableField<String> logoPath = new ObservableField<>();
  public final ObservableField<String> gameInfo = new ObservableField<>();

  public final ObservableBoolean nextEnabled = new ObservableBoolean(false);

  public final View.OnClickListener nextAccount = new View.OnClickListener() {
    @Override public void onClick(View v) {
      final Context context = v.getContext();

      // 先重置错误提示
      editAccountVM.usernameError.set(null);
      editAccountVM.passwordError.set(null);

      final String username = editAccountVM.username.get();
      if (username == null || !checkUsername(username)) {
        editAccountVM.usernameError.set("请输入7-15位数字或字母");
        return;
      }

      final String password = editAccountVM.password.get();
      if (password == null || !checkPassword(password)) {
        editAccountVM.passwordError.set("请输入6-15位数字或字母");
        return;
      }

      // TODO 可以先插入数据，然后拿账号密码登录，根据返回值修改对应状态
      // 如果账号没有注册，返回-1，这里提示是否注册
      // （如果勾选了注册账号则直接调用注册接口——这只是一个设想，实现有难度）
      // 如果账号注册失败，则显示相关提示，不再有其他逻辑
      // 如果注册成功，提示是否登录：是则继续登录；否则关闭对话框
      accountModel.addAccount(username, password, new Result<Long>() {
        @Override public void onSuccessful(Long result) {
          if (result > 0) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();

            // 确认是否使用默认别名
          }
        }

        @Override public void onFailed(String message) {
          Toast.makeText(context, "添加失败：" + message, Toast.LENGTH_SHORT).show();
        }
      });
    }
  };

  private final AccountModel accountModel = new AccountModel();
  private final RandallModel randallModel = new RandallModel();

  private HomePage homePage;
  private LoginPage loginPage;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAddAccountBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_add_account);
    binding.setAddAccount(this);

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

  @Override protected void onResume() {
    super.onResume();
    loadHome(this);
  }

  @Override protected void onPause() {
    super.onPause();
    randallModel.subscriptions.unsubscribe();
  }

  /** 加载主页数据 */
  public void loadHome(final Context context) {
    randallModel.loadHome(new Result<HomePage>() {
      @Override public void onSuccessful(HomePage result) {
        if (result == null || result.emptyServer()) {
          loadingVM.loadFailed();
          Toast.makeText(context, "数据异常，请重试", Toast.LENGTH_SHORT).show();
          return;
        }

        loadingVM.loadSuccessful();

        homePage = result;
        logoPath.set(homePage.logoPath());
        gameInfo.set(homePage.gameInfo());
        editAccountVM.update(homePage.getServerList());

        nextEnabled.set(true);

        // 打开登录页面；TODO 注册页面应该提供一个按钮，点击按钮则加载注册页面
        randallModel.loadLogin(homePage.getLogin(), new Result<LoginPage>() {
          @Override public void onSuccessful(LoginPage result) {
            loginPage = result;
          }
        });
      }

      @Override public void onFailed(String message) {
        super.onFailed(message);
        Toast.makeText(context, "加载失败：" + message, Toast.LENGTH_SHORT).show();
        loadingVM.loadFailed();
      }
    });
  }

  /** 检查账户是否有效 */
  private boolean checkUsername(String username) {
    int length = username.length();
    return length >= 7 && length <= 15;
  }

  /** 检查密码是否有效 */
  private boolean checkPassword(String password) {
    int length = password.length();
    return length >= 6 && length <= 15;
  }
}
