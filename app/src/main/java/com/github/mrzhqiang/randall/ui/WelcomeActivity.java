package com.github.mrzhqiang.randall.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

  public final View.OnClickListener clickAdd = v -> openAddAccount();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityWelcomeBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_welcome);
    binding.setWelcome(this);

    setSupportActionBar(binding.toolbar);
  }

  @Override public void onBackPressed() {
    // true表示不是根活动，也可以移到后台
    moveTaskToBack(true);
  }

  public void openAddAccount() {
    Intent intent = new Intent(this, AddAccountActivity.class);
    // 跳转时不需要动画，让页面过度得更快
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    startActivity(intent);
  }
}
