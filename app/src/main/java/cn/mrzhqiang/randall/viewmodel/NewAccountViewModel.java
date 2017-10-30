package cn.mrzhqiang.randall.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.data.Home;
import cn.mrzhqiang.randall.model.AccountModel;
import cn.mrzhqiang.randall.model.RandallModel;
import cn.mrzhqiang.randall.net.Result;
import com.squareup.picasso.Picasso;

/**
 * 新的账号：先添加到本地数据库，同时验证账号密码是否有效；当不存在时，可选注册
 */
public final class NewAccountViewModel {

  @BindingAdapter("gameLogo") public static void showGameLogo(ImageView view, String path) {
    if (path != null && path.length() > 0) {
      Picasso.with(view.getContext()).load(path).fit().noFade().noPlaceholder().into(view);
    }
  }

  public final InputViewModel inputUsername = new InputViewModel(InputViewModel.Type.USERNAME);
  public final InputViewModel inputPassword = new InputViewModel(InputViewModel.Type.PASSWORD);

  public final LoadingViewModel loading = new LoadingViewModel() {
    @Override public void onRetry(Context context) {
      super.onRetry(context);
      loadHome(context);
    }
  };

  public final ObservableField<String> logoPath = new ObservableField<>();
  public final ObservableField<String> serverName = new ObservableField<>();
  public final ObservableField<String> gameInfo = new ObservableField<>();

  public final ObservableBoolean chooseEnabled = new ObservableBoolean(false);
  public final ObservableBoolean addEnabled = new ObservableBoolean(false);

  public final View.OnClickListener chooseServer = new View.OnClickListener() {
    @Override public void onClick(View v) {
      Context context = v.getContext();
      new AlertDialog.Builder(context).setTitle("选择服务器")
          .setSingleChoiceItems(home.asNameList(), index, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              index = which;
              serverName.set(home.serverName(index));
              dialog.dismiss();
            }
          })
          .show();
    }
  };

  public final View.OnClickListener addAccount = new View.OnClickListener() {
    @Override public void onClick(View v) {
      final Context context = v.getContext();

      // 先重置错误提示
      inputUsername.showError(null);
      inputPassword.showError(null);

      String username = inputUsername.takeText();
      if (!checkUsername(username)) {
        inputUsername.showError("请输入7-15位数字或字母");
        return;
      }

      String password = inputPassword.takeText();
      if (!checkPassword(password)) {
        inputPassword.showError("请输入6-15位数字或字母");
        return;
      }

      accountModel.addAccount(username, password, new Result<Long>() {
        @Override public void onSuccessful(Long result) {
          if (result > 0) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
            // 确认是否使用默认别名
          } else if (result == -1) {
            // 提示是否注册
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

  private Home home;
  private int index = 0;

  /** 加载主页数据 */
  public void loadHome(final Context context) {
    randallModel.loadHome(new Result<Home>() {
      @Override public void onSuccessful(Home result) {
        if (result == null || result.emptyServer()) {
          loading.loadFailed();
          Toast.makeText(context, "数据异常，请重试", Toast.LENGTH_SHORT).show();
          return;
        }

        home = result;
        logoPath.set(home.logoPath());
        serverName.set(home.serverName(index));
        gameInfo.set(home.gameInfo());

        chooseEnabled.set(true);
        addEnabled.set(true);
        loading.loadSuccessful();
      }

      @Override public void onFailed(String message) {
        super.onFailed(message);
        Toast.makeText(context, "加载失败：" + message, Toast.LENGTH_SHORT).show();
        loading.loadFailed();
      }
    });
  }

  /** 取消请求，交让出资源给其他页面 */
  public void cancelRequest() {
    randallModel.subscriptions.unsubscribe();
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
