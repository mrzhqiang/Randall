package cn.mrzhqiang.randall.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.databinding.ActivityRandallBinding;
import cn.mrzhqiang.randall.db.Account;
import cn.mrzhqiang.randall.ui.model.AccountModel;
import cn.mrzhqiang.randall.net.Result;
import java.util.List;

/**
 * 兰达尔主页，展示当前游戏内容
 */
public class RandallActivity extends AppCompatActivity {

  public final ObservableArrayList<Account> accountList = new ObservableArrayList<>();

  private final AccountModel accountModel = new AccountModel();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityRandallBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_randall);
    binding.setRandall(this);

    setSupportActionBar(binding.toolbar);
  }

  @Override protected void onResume() {
    super.onResume();
    // 对账户进行监听
    accountModel.queryList(new Result<List<Account>>() {
      @Override public void onSuccessful(List<Account> result) {
        if (isFinishing()) {
          return;
        }
        if (result.size() == 0) {
          Intent intent = new Intent(RandallActivity.this, WelcomeActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
          startActivity(intent);
          finish();
          return;
        }

        accountList.clear();
        accountList.addAll(result);

        // 这里是测试方法
        StringBuilder builder = new StringBuilder();
        for (Account account : result) {
          builder.append(account.toString());
        }
        new AlertDialog.Builder(RandallActivity.this).setMessage(builder.toString()).show();
      }

      @Override public void onFailed(String message) {
        // TODO 做一些处理
        Toast.makeText(RandallActivity.this, message, Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override protected void onPause() {
    super.onPause();
    accountModel.cancel();
  }
}
