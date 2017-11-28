package cn.mrzhqiang.randall.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.databinding.ActivityWelcomeBinding;

/**
 * 欢迎页面，提供简单的注册/登录模块
 * @author mrZQ
 */
public class WelcomeActivity extends AppCompatActivity {

  public final ObservableField<String> openText = new ObservableField<>("正在加载");
  public final ObservableBoolean openEnable = new ObservableBoolean(false);

  public final View.OnClickListener clickOpen = new View.OnClickListener() {
    @Override public void onClick(View v) {
      // 这样写的话，可以搬到任何位置
      Context context = v.getContext();
      Intent intent = new Intent(context, AddAccountActivity.class);
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

    binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        // TODO 重新加载数据
      }
    });
  }

}
