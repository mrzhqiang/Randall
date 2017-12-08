package com.github.mrzhqiang.randall.ui;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityLoginBinding;
import com.github.mrzhqiang.randall.viewmodel.EditAccountViewModel;

public class LoginActivity extends AppCompatActivity {

  public final ObservableInt editVisibility = new ObservableInt(View.VISIBLE);
  public final ObservableBoolean checkAdvanced = new ObservableBoolean(false);

  public final EditAccountViewModel editAccountVM = new EditAccountViewModel();

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

  @Override protected void onDestroy() {
    super.onDestroy();
    editAccountVM.cancelAll();
    checkAdvanced.removeOnPropertyChangedCallback(advancedCallback);
  }
}
