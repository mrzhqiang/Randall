package com.github.mrzhqiang.randall.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityRandallBinding;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.model.AccountModel;
import com.github.mrzhqiang.smith.net.Result;
import java.util.List;

/**
 * 兰达尔主页
 *
 * @author mrZQ
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

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_randall, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.edit_account:
        openLoginActivity();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onResume() {
    super.onResume();

    final Context context = this;
    // 对账户进行监听
    accountModel.queryList(new Result<List<Account>>() {
      @Override public void onSuccessful(List<Account> result) {
        if (result.size() == 0) {
          Intent intent = new Intent(context, WelcomeActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
          startActivity(intent);
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
        Toast.makeText(RandallActivity.this, message, Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override protected void onPause() {
    super.onPause();
    accountModel.cancelAll();
  }

  private void openLoginActivity() {
    Intent intent = new Intent(this, LoginActivity.class);
    if (accountList.size() > 0) {
      intent.putExtra("account", accountList.get(0));
    }
    startActivity(intent);
  }
}
