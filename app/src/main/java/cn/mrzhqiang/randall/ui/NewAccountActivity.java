package cn.mrzhqiang.randall.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.databinding.ActivityAddUidBinding;
import cn.mrzhqiang.randall.viewmodel.NewAccountViewModel;

/**
 * 新账号：其一、添加本地；其二、快速注册
 */
public class NewAccountActivity extends AppCompatActivity {

  private NewAccountViewModel viewModel;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new NewAccountViewModel();

    ActivityAddUidBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_uid);
    binding.setNewAccountVM(viewModel);

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
    viewModel.showHome(this);
  }

  @Override protected void onPause() {
    super.onPause();
    // 取消所有操作，追求极致用户体验
    viewModel.exit();
  }
}
