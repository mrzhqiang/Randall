package com.github.mrzhqiang.randall.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityLoginBinding;
import com.github.mrzhqiang.smith.model.SmithModel;
import com.github.mrzhqiang.smith.net.Game;
import com.github.mrzhqiang.smith.net.Login;
import com.github.mrzhqiang.smith.net.Result;

public class LoginActivity extends AppCompatActivity {

  public final ObservableField<String> text = new ObservableField<>();

  private final SmithModel smithModel = new SmithModel();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    binding.setLogin(this);

    setSupportActionBar(binding.toolbar);

    Intent intent = getIntent();
    Login login = intent.getParcelableExtra("login");
    smithModel.login(login, new Result<Game>() {
      @Override public void onSuccessful(Game result) {
        text.set(result.body());
      }
    });
  }
}
