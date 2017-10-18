package cn.mrzhqiang.randall.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import cn.mrzhqiang.randall.R;
import cn.mrzhqiang.randall.data.Account;
import cn.mrzhqiang.randall.databinding.ActivityRandallBinding;
import cn.mrzhqiang.randall.model.AccountModel;
import java.util.List;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 兰达尔主页，展示当前游戏内容
 */
public class RandallActivity extends AppCompatActivity {

  private final AccountModel accountModel = new AccountModel();

  private Subscription accountSub;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityRandallBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_randall);
    binding.setRandall(this);

    setSupportActionBar(binding.toolbar);

    // FIXME 应该使用查询语句直接查询，这种方式有可能出现未知问题
    accountSub = accountModel.queryAccountList(new Action1<List<Account>>() {
      @Override public void call(List<Account> accounts) {
        if (accounts == null || accounts.size() == 0) {
          Intent intent = new Intent(RandallActivity.this, WelcomeActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
          startActivity(intent);
          finish();
          return;
        }

        // TODO 展示账户列表
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (!accountSub.isUnsubscribed()) {
      accountSub.unsubscribe();
    }
  }
}
