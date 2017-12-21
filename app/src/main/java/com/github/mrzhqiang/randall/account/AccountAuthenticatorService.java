package com.github.mrzhqiang.randall.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AccountAuthenticatorService extends Service {

  private AccountAuthenticator accountAuthenticator;

  @Override public void onCreate() {
    super.onCreate();

    accountAuthenticator = new AccountAuthenticator(this);
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return accountAuthenticator.getIBinder();
  }
}
