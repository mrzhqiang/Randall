package cn.mrzhqiang.randall.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.databinding.ActivityWelcomeBinding;

/**
 * 欢迎页面，告知此应用功能
 */
public class WelcomeActivity extends AppCompatActivity {

  public final View.OnClickListener addUid = new View.OnClickListener() {
    @Override public void onClick(View v) {
      // 这样写的话，可以搬到任何位置
      Context context = v.getContext();
      Intent intent = new Intent(context, AddUidActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
      context.startActivity(intent);
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityWelcomeBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_welcome);
    binding.setWelcome(this);

    setSupportActionBar(binding.toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowHomeEnabled(false);
      actionBar.setDisplayHomeAsUpEnabled(false);
    }
  }
}
