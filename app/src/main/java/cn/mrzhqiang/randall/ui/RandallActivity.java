package cn.mrzhqiang.randall.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.databinding.ActivityRandallBinding;
import cn.mrzhqiang.randall.model.AccountModel;
import java.util.List;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 兰达尔主页，展示当前游戏内容
 */
public class RandallActivity extends AppCompatActivity {

  public final ObservableArrayList<Account> accountList = new ObservableArrayList<>();

  private final AccountModel accountModel = new AccountModel();

  private Subscription accountSub;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //对账户进行监听
    accountSub = accountModel.queryAccountList(new Action1<List<Account>>() {
      @Override public void call(List<Account> accounts) {
        if (isFinishing()) {
          return;
        }
        if (accounts.size() == 0) {
          Intent intent = new Intent(RandallActivity.this, WelcomeActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
          startActivity(intent);
          finish();
          return;
        }

        accountList.clear();
        accountList.addAll(accounts);

        // 这里是测试方法
        StringBuilder builder = new StringBuilder();
        for (Account account : accounts) {
          builder.append(account.toString());
        }
        new AlertDialog.Builder(RandallActivity.this).setMessage(builder.toString()).show();
      }
    });

    ActivityRandallBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_randall);
    binding.setRandall(this);

    setSupportActionBar(binding.toolbar);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.randall, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_add_uid:
        Intent intent = new Intent(this, AddUidActivity.class);
        startActivity(intent);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    accountSub.unsubscribe();
  }
}
