package com.github.mrzhqiang.randall.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityAddAccountBinding;
import com.github.mrzhqiang.randall.viewmodel.EditAccountViewModel;

/**
 * 添加账号页面，先添加到本地，然后去进行网络验证：
 * 如果不存在，告知并提示是否注册；如果存在，则跳转到主页
 *
 * @author mrZQ
 */

public class AddAccountActivity extends AppCompatActivity {

  public final EditAccountViewModel editAccountVM = new EditAccountViewModel();

  public final View.OnClickListener clickAddAccount = v -> editAccountVM.addAccount(v.getContext());

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAddAccountBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_add_account);
    binding.setActivity(this);

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

  @Override protected void onStop() {
    super.onStop();
    // 停止状态下，不应该有任何后台操作，后面再看看这样做是否有意义
    editAccountVM.cancelAll();
  }
}
