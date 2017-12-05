package com.github.mrzhqiang.randall.ui;

import android.content.Context;
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

  public final View.OnClickListener clickAdd = v -> {
    // 首先需要的是点击的这个组件的上下文，这样才不至于内存泄漏
    Context context = v.getContext();
    Intent intent = new Intent(context, LoginActivity.class);
    // 跳转时不需要动画，让画面转换得更快
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    context.startActivity(intent);
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityWelcomeBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_welcome);
    binding.setActivity(this);

    setSupportActionBar(binding.toolbar);
  }

}
