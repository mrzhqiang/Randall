package com.github.mrzhqiang.randall.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityWelcomeBinding;

/**
 * 欢迎页面
 *
 * @author mrZQ
 */

public class WelcomeActivity extends AppCompatActivity {

  public final View.OnClickListener clickCreate = v -> openLogin();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityWelcomeBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_welcome);
    binding.setActivity(this);

    setSupportActionBar(binding.toolbar);
  }

  public void openLogin() {
    Intent intent = new Intent(this, LoginActivity.class);
    // 跳转时不需要动画，让页面过度得更快
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    startActivity(intent);
  }

  @Override public void onBackPressed() {
    // true表示不是根活动，也可以移到后台
    moveTaskToBack(true);
  }
}
