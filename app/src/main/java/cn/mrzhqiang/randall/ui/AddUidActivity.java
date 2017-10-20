package cn.mrzhqiang.randall.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.data.Home;
import cn.mrzhqiang.randall.databinding.ActivityAddUidBinding;
import cn.mrzhqiang.randall.model.AccountModel;
import cn.mrzhqiang.randall.model.RandallModel;
import com.squareup.picasso.Picasso;
import rx.functions.Action1;

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
  }

  public final ObservableField<String> gamePath = new ObservableField<>();
  public final ObservableField<String> usernameError = new ObservableField<>();
  public final ObservableField<String> username = new ObservableField<>();
  public final ObservableField<String> passwordError = new ObservableField<>();
  public final ObservableField<String> password = new ObservableField<>();
  public final ObservableField<String> serverName = new ObservableField<>();
  public final ObservableField<String> gameInfo = new ObservableField<>();

  public final ObservableInt logoVisibility = new ObservableInt(View.GONE);
  public final ObservableInt loadVisibility = new ObservableInt(View.VISIBLE);

  public final View.OnClickListener chooseServer = new View.OnClickListener() {
    @Override public void onClick(View v) {
      Context context = v.getContext();
      if (mHome == null || mHome.serverList == null) {
        Toast.makeText(context, "请等待数据加载完成或手动刷新", Toast.LENGTH_SHORT).show();
        return;
      }

      new AlertDialog.Builder(context).setTitle("选择服务器")
          .setSingleChoiceItems(mHome.asNameList(), index, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              index = which;
              serverName.set(mHome.serverList.get(index).text());
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

  private final AccountModel accountModel = new AccountModel();
  private final RandallModel randallModel = new RandallModel();

  private Home mHome;
  private int index = 0;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAddUidBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_uid);
    binding.setAddUid(this);

    setSupportActionBar(binding.toolbar);

    // TODO 应该检测数据库是否存在记录，以此设定是否自动加载，或显示刷新菜单按钮
    randallModel.loadHome(new Action1<Home>() {
      @Override public void call(Home home) {
        loadVisibility.set(View.GONE);
        logoVisibility.set(View.VISIBLE);
        mHome = home;
        loadData();
      }
    });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void loadData() {
    gamePath.set(mHome.gamePath.absUrl("src"));
    if (mHome.serverList.size() > 0) {
      serverName.set(mHome.serverList.get(index).text());
    }
    gameInfo.set(mHome.gameInfo.toString());
  }
}
