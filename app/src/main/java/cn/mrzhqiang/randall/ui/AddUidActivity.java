package cn.mrzhqiang.randall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import cn.mrzhqiang.helper.AccountHelper;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.data.Home;
import cn.mrzhqiang.randall.databinding.ActivityAddUidBinding;
import cn.mrzhqiang.randall.model.AccountModel;
import cn.mrzhqiang.randall.model.RandallModel;
import cn.mrzhqiang.randall.net.Result;
import com.squareup.picasso.Picasso;

/**
 * 添加账号，其一、便捷注册；其二、保存书签。
 */
public class AddUidActivity extends AppCompatActivity {

  @BindingAdapter("gameLogo") public static void showGameLogo(ImageView view, String path) {
    if (path != null && path.length() > 0) {
      Picasso.with(view.getContext()).load(path).fit().noFade().noPlaceholder().into(view);
    }
  }

  @BindingAdapter("error")
  public static void showEditTextError(TextInputLayout layout, String error) {
    layout.setError(error);
    if (error != null) {
      layout.requestFocus();
    }
  }

  public final ObservableField<String> gamePath = new ObservableField<>();
  public final ObservableField<String> usernameError = new ObservableField<>();
  public final ObservableField<String> username = new ObservableField<>();
  public final ObservableField<String> passwordError = new ObservableField<>();
  public final ObservableField<String> password = new ObservableField<>();
  public final ObservableField<String> serverName = new ObservableField<>();
  public final ObservableField<String> gameInfo = new ObservableField<>();

  public final ObservableInt logoVisibility = new ObservableInt(View.GONE);
  public final ObservableInt retryVisibility = new ObservableInt(View.GONE);
  public final ObservableInt loadVisibility = new ObservableInt(View.GONE);

  public final ObservableBoolean passwordEnabled = new ObservableBoolean(false);

  public final CompoundButton.OnCheckedChangeListener randomPassword =
      new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          passwordEnabled.set(!isChecked);
          if (isChecked) {
            password.set(AccountHelper.createPassword(generatePasswordSize()));
          } else {
            password.set("");
          }
        }
      };

  public final View.OnClickListener loadHome = new View.OnClickListener() {
    @Override public void onClick(View v) {
      showHome();
    }
  };

  public final View.OnClickListener chooseServer = new View.OnClickListener() {
    @Override public void onClick(View v) {
      Context context = v.getContext();
      if (home == null || home.serverList == null) {
        if (loadVisibility.get() == View.VISIBLE) {
          Toast.makeText(context, "正在加载主页数据", Toast.LENGTH_SHORT).show();
        } else if (retryVisibility.get() == View.VISIBLE) {
          Toast.makeText(context, "需要重新加载数据", Toast.LENGTH_SHORT).show();
        }
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

  public final View.OnClickListener register = new View.OnClickListener() {
    @Override public void onClick(View v) {
      // 注册
    }
  };

  public final View.OnClickListener login = new View.OnClickListener() {
    @Override public void onClick(View v) {
      final Context context = v.getContext();
      // 判断数据是否有效
      if (home == null || home.serverList == null) {
        if (loadVisibility.get() == View.VISIBLE) {
          Toast.makeText(context, "正在加载主页数据", Toast.LENGTH_SHORT).show();
        } else if (retryVisibility.get() == View.VISIBLE) {
          Toast.makeText(context, "需要重新加载数据", Toast.LENGTH_SHORT).show();
        }
        return;
      }
      // 先重置
      usernameError.set(null);
      passwordError.set(null);

      String mUsername = username.get();
      if (!checkUsername(mUsername)) {
        usernameError.set("账号无效，请输入准确的长度");
        return;
      }

      if (accountModel.checkExists(mUsername)) {
        usernameError.set("账号已存在，无法重复添加");
        return;
      }

      String mPassword = password.get();
      if (!checkPassword(mPassword)) {
        passwordError.set("密码无效，请输入准确的长度");
        return;
      }

      final Account account = new Account();
      account.username = username.get();
      account.password = password.get();
      account.alias = serverName.get();
      accountModel.addAccount(account, new Result<Boolean>() {
        @Override public void onSuccessful(Boolean result) {
          if (result) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
          }
        }

        @Override public void onFailed(String message) {
          Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
        }
      });
    }
  };

  private final AccountModel accountModel = new AccountModel();
  private final RandallModel randallModel = new RandallModel();

  private Home home;
  private int index = 0;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAddUidBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_uid);
    binding.setAddUid(this);

    setSupportActionBar(binding.toolbar);

    username.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        usernameError.set(null);
      }
    });
    password.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
      @Override public void onPropertyChanged(Observable sender, int propertyId) {
        passwordError.set(null);
      }
    });
    // 当前默认勾选随机密码，因此在这里设置初始值
    password.set(AccountHelper.createPassword(generatePasswordSize()));

    showHome();
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
    // 取消所有操作，追求极致用户体验
    randallModel.exitModel();
  }

  private void showHome() {
    randallModel.loadHome(new Result<Home>() {
      @Override public void onStart() {
        logoVisibility.set(View.GONE);
        retryVisibility.set(View.GONE);
        loadVisibility.set(View.VISIBLE);
      }

      @Override public void onSuccessful(Home result) {
        logoVisibility.set(View.VISIBLE);
        loadVisibility.set(View.GONE);

        home = result;
        gamePath.set(home.gamePath.absUrl("src"));
        if (home.serverList.size() > 0) {
          serverName.set(home.serverList.get(index).text());
        }
        gameInfo.set(home.gameInfo.toString());
      }

      @Override public void onFailed(String message) {
        Toast.makeText(AddUidActivity.this, message, Toast.LENGTH_SHORT).show();
        retryVisibility.set(View.VISIBLE);
        loadVisibility.set(View.GONE);
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

  /** 生成随机密码的随机位数：6--15 */
  private int generatePasswordSize() {
    return (int) (6 + Math.random() * 10);
  }
}
