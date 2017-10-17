package cn.mrzhqiang.randall.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.databinding.ActivityOtherGameBinding;
import cn.mrzhqiang.randall.viewmodel.EmptyViewModel;

/**
 * 其他游戏，意在兼容所有手机网页游戏
 */
public class OtherGameActivity extends AppCompatActivity {

  public final EmptyViewModel empty = new EmptyViewModel();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityOtherGameBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_other_game);
    binding.setOtherGame(this);

    setSupportActionBar(binding.toolbar);

    empty.setHint("对其他游戏的支持，请等待下一次版本更新");
    empty.setIcon(R.drawable.ic_empty_wait_update);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
