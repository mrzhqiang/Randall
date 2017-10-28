package cn.mrzhqiang.randall.viewmodel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
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

  public final InputViewModel inputUsername = new InputViewModel();
  public final InputViewModel inputPassword = new InputViewModel();

  public final LoadingViewModel loading = new LoadingViewModel() {
    @Override public void onRetry(Context context) {
      showHome(context);
    }
  };

  public final ObservableField<String> logoPath = new ObservableField<>();
  public final ObservableField<String> serverName = new ObservableField<>();
  public final ObservableField<String> gameInfo = new ObservableField<>();

  public final View.OnClickListener chooseServer = new View.OnClickListener() {
    @Override public void onClick(View v) {
      Context context = v.getContext();
      if (home == null || home.serverList == null) {
        Toast.makeText(context, "正在加载主页数据", Toast.LENGTH_SHORT).show();
        return;
      }

      new AlertDialog.Builder(context).setTitle("选择服务器")
          .setSingleChoiceItems(home.asNameList(), index, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              index = which;
              serverName.set(home.serverList.get(index).text());
              dialog.dismiss();
            }
          })
          .show();
    }
  };

  public final View.OnClickListener addAccount = new View.OnClickListener() {
    @Override public void onClick(View v) {
      final Context context = v.getContext();
      // 判断数据是否有效
      if (home == null || home.serverList == null) {
        Toast.makeText(context, "正在加载主页数据", Toast.LENGTH_SHORT).show();
        return;
      }
      // 先重置错误提示
      inputUsername.showError(null);
      inputPassword.showError(null);

      String username = inputUsername.takeText();
      if (!checkUsername(username)) {
        inputUsername.showError("账号无效，请输入准确的长度");
        return;
      }

      String password = inputPassword.takeText();
      if (!checkPassword(password)) {
        inputPassword.showError("密码无效，请输入准确的长度");
        return;
      }

      final Account account = new Account();
      account.username = username;
      account.password = password;
      account.alias = serverName.get();
      accountModel.addAccount(account, new Result<Long>() {
        @Override public void onSuccessful(Long result) {
          if (result > 0) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
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

  public NewAccountViewModel() {
    inputUsername.initUsername();
    inputPassword.initPassword();
    inputUsername.text.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        inputUsername.showError(null);
      }
    });
    inputPassword.text.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        inputPassword.showError(null);
      }
    });
  }

  public void showHome(final Context context) {
    randallModel.loadHome(new Result<Home>() {
      @Override public void onSuccessful(Home result) {
        loading.loadSuccessful();

        home = result;
        logoPath.set(home.gamePath.absUrl("src"));
        if (home.serverList.size() > 0) {
          serverName.set(home.serverList.get(index).text());
        }
        gameInfo.set(home.gameInfo.toString());
      }

      @Override public void onFailed(String message) {
        loading.loadFailed();

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void exit() {
    randallModel.cancelSubscriber();
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

  /** 生成随机密码的随机位数：6--15 */
  private int generatePasswordSize() {
    return (int) (6 + Math.random() * 10);
  }

}
