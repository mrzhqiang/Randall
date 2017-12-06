package com.github.mrzhqiang.randall.ui;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityLoginBinding;
import com.github.mrzhqiang.randall.viewmodel.EditAccountViewModel;

/**
 * 登陆/注册页面
 *
 * @author mrZQ
 */

public class LoginActivity extends AppCompatActivity {

  public final ObservableBoolean checkAdvanced = new ObservableBoolean(false);

  public final EditAccountViewModel editAccountVM = new EditAccountViewModel();

  public final View.OnClickListener clickRegister = v -> editAccountVM.register(v.getContext());

  public final View.OnClickListener clickLogin = v -> editAccountVM.login(v.getContext());

  private final Observable.OnPropertyChangedCallback advancedCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override public void onPropertyChanged(Observable sender, int propertyId) {
          boolean value = checkAdvanced.get();
          editAccountVM.advanced(value);
        }
      };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    binding.setLogin(this);

    setSupportActionBar(binding.toolbar);

    checkAdvanced.addOnPropertyChangedCallback(advancedCallback);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override protected void onStop() {
    super.onStop();
    // 停止状态下，不应该有任何后台操作，后面再看看这样做是否有意义
    editAccountVM.cancelAll();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    checkAdvanced.removeOnPropertyChangedCallback(advancedCallback);
  }
}
