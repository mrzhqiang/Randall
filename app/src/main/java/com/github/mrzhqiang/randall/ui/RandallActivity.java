package com.github.mrzhqiang.randall.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityRandallBinding;
import com.github.mrzhqiang.randall.ui.adapters.RandallAdapter;
import com.github.mrzhqiang.randall.viewmodel.ItemAccountViewModel;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.model.AccountModel;
import com.github.mrzhqiang.smith.net.Result;
import java.util.List;

public class RandallActivity extends AppCompatActivity {

  public final RandallAdapter adapter = new RandallAdapter();
  public final RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
  public final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

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
        openAddAccount();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onResume() {
    super.onResume();

    // 对账户进行监听
    accountModel.queryList(new Result<List<Account>>() {
      @Override public void onSuccessful(List<Account> result) {
        if (result.size() == 0) {
          openWelcome();
          return;
        }

        adapter.updateData(result);
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

  @Override public void onBackPressed() {
    moveTaskToBack(false);
  }

  private void openWelcome() {
    Intent intent = new Intent(this, WelcomeActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    startActivity(intent);
  }

  private void openAddAccount() {
    Intent intent = new Intent(this, AddAccountActivity.class);
    startActivity(intent);
  }
}
