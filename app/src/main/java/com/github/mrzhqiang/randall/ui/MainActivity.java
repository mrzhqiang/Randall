package com.github.mrzhqiang.randall.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.github.mrzhqiang.randall.R;
import com.github.mrzhqiang.randall.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  public final BottomNavigationView.OnNavigationItemSelectedListener itemSelected =
      this::itemSelected;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.setMain(this);

    setSupportActionBar(binding.toolbar);

    AccountManager accountManager = AccountManager.get(this);
    Account[] accounts = accountManager.getAccountsByType("Randall");


    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, RandallFragment.newInstance())
          .commit();
    }
  }

  private boolean itemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.navigation_main:
        showRandall();
        return true;
      case R.id.navigation_bookmark:
        showBookmark();
        return true;
      case R.id.navigation_me:
        showMe();
        return true;
      default:
        return false;
    }
  }

  private void showRandall() {
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, RandallFragment.newInstance())
        .commit();
  }

  private void showBookmark() {
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, BookmarkFragment.newInstance())
        .commit();
  }

  private void showMe() {
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, MeFragment.newInstance())
        .commit();
  }

}
