package cn.mrzhqiang.randall.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.databinding.ActivityRandallBinding;

/**
 * 兰达尔主页，展示当前游戏内容
 */
public class RandallActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityRandallBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_randall);
    binding.setRandall(this);

    setSupportActionBar(binding.toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(false);
      actionBar.setDisplayHomeAsUpEnabled(false);
    }

    if (savedInstanceState == null) {
      Intent intent = new Intent(this, WelcomeActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
      startActivity(intent);
      finish();
    }
  }
}
