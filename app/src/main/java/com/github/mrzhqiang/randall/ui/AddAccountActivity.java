package com.github.mrzhqiang.randall.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityAddAccountBinding;
import com.github.mrzhqiang.randall.viewmodel.EditAccountViewModel;
import com.github.mrzhqiang.smith.db.Account;
import com.github.mrzhqiang.smith.model.AccountModel;
import com.github.mrzhqiang.smith.net.Result;
import java.util.List;

public class AddAccountActivity extends AppCompatActivity {

  public final EditAccountViewModel editAccountVM = new EditAccountViewModel();

  public final ObservableBoolean checkAdvanced = new ObservableBoolean(false);
  public final ObservableBoolean batchChecked = new ObservableBoolean(false);
  public final ObservableBoolean randomChecked = new ObservableBoolean(false);

  public final ObservableBoolean batchEnabled = new ObservableBoolean(false);
  public final ObservableBoolean randomEnabled = new ObservableBoolean(false);
  public final ObservableBoolean addEnabled = new ObservableBoolean(true);

  public final ObservableInt loadingVisibility = new ObservableInt(View.GONE);
  public final ObservableInt advancedVisibility = new ObservableInt(View.GONE);

  public final ObservableField<String> startIndex = new ObservableField<>();
  public final ObservableField<String> createCount = new ObservableField<>();

  public final View.OnClickListener clickRandom = v -> autoPassword();
  public final View.OnClickListener clickAdd = v -> addAccount(v.getContext());

  private final Observable.OnPropertyChangedCallback advancedCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override public void onPropertyChanged(Observable sender, int propertyId) {
          boolean value = checkAdvanced.get();
          advancedVisibility.set(value ? View.VISIBLE : View.GONE);
          if (value) {
            batchChecked.set(false);
            randomChecked.set(false);
          }
        }
      };
  private final Observable.OnPropertyChangedCallback batchCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override public void onPropertyChanged(Observable sender, int propertyId) {
          boolean value = batchChecked.get();
          batchEnabled.set(value);
          if (!value) {
            startIndex.set(null);
            createCount.set(null);
          }
        }
      };
  private final Observable.OnPropertyChangedCallback randomCallback =
      new Observable.OnPropertyChangedCallback() {
        @Override public void onPropertyChanged(Observable sender, int propertyId) {
          boolean value = randomChecked.get();
          randomEnabled.set(value);
          if (value) {
            autoPassword();
          }
        }
      };

  private final AccountModel accountModel = new AccountModel();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityAddAccountBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_add_account);
    binding.setAddAccount(this);

    setSupportActionBar(binding.toolbar);

    batchChecked.addOnPropertyChangedCallback(batchCallback);
    randomChecked.addOnPropertyChangedCallback(randomCallback);
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
    checkAdvanced.removeOnPropertyChangedCallback(advancedCallback);
    randomChecked.removeOnPropertyChangedCallback(randomCallback);
    batchChecked.removeOnPropertyChangedCallback(batchCallback);
    accountModel.cancelAll();
    editAccountVM.cancelAll();
  }

  private void autoPassword() {
    // [6,16)
    int size = (int) (6 + Math.random() * 10);
    editAccountVM.randomPassword(size);
  }

  private void showLoading() {
    loadingVisibility.set(View.VISIBLE);
    editAccountVM.disabled();
    addEnabled.set(false);
  }

  private void hideLoading() {
    loadingVisibility.set(View.GONE);
    editAccountVM.enabled();
    addEnabled.set(true);
  }

  private void addAccount(Context context) {
    Account account = editAccountVM.getAccount();
    if (account == null) {
      return;
    }
    boolean batch = batchChecked.get();
    if (!batch) {
      showLoading();
      accountModel.create(account, new Result<Account>() {
        @Override public void onSuccessful(Account result) {
          hideLoading();
          Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(context, RandallActivity.class);
          context.startActivity(intent);
        }

        @Override public void onFailed(String message) {
          super.onFailed(message);
          hideLoading();
          Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
      });
    } else {
      int start = Integer.parseInt(startIndex.get());
      int count = Integer.parseInt(createCount.get());
      if (start < 0) {
        Toast.makeText(context, "错误的起始编号：" + start + ", 请设置0-99的整数", Toast.LENGTH_SHORT).show();
        return;
      }
      if (count < 2) {
        Toast.makeText(context, "错误的批量创建：" + count + ", 请设置2-99的整数", Toast.LENGTH_SHORT).show();
        return;
      }
      showLoading();
      accountModel.create(account, start, count, new Result<List<Account>>() {
        @Override public void onSuccessful(List<Account> result) {
          if (result.size() > 0) {
            hideLoading();
            String[] items = new String[result.size()];
            for (int i = 0; i < result.size(); i++) {
              items[i] = (i + 1) + "-" + result.get(i).username();
            }
            new AlertDialog.Builder(context).setTitle("批量创建")
                .setItems(items, null)
                .setPositiveButton("完成", (dialog, which) -> {
                  dialog.dismiss();
                  Intent intent = new Intent(context, RandallActivity.class);
                  context.startActivity(intent);
                })
                .setNegativeButton("继续", null)
                .show();
          } else {
            onFailed("批量创建无效");
          }
        }

        @Override public void onFailed(String message) {
          super.onFailed(message);
          hideLoading();
          Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
      });
    }
  }
}
