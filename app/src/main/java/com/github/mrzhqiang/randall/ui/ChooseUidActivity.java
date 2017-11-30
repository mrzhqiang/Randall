package com.github.mrzhqiang.randall.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityChooseUidBinding;

/**
 * 主页，展示账户列表
 */
public class ChooseUidActivity extends AppCompatActivity {
  private static final String TAG = "ChooseUidActivity";

  public final View.OnClickListener addUid = new View.OnClickListener() {
    @Override public void onClick(View v) {
      Toast.makeText(v.getContext(), "跳转添加账号页面", Toast.LENGTH_SHORT).show();
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityChooseUidBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_choose_uid);
    binding.setViewModel(this);
  }
}
