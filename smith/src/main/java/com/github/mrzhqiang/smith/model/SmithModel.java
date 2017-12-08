package com.github.mrzhqiang.smith.model;

import com.github.mrzhqiang.smith.BaseApp;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;
import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

public final class SmithModel {
  private static final String TAG = "SmithModel";

  @Inject Retrofit net;
  @Inject BriteDatabase db;

  public final CompositeSubscription subscriptions = new CompositeSubscription();

  public SmithModel() {
    BaseApp.appComponent().inject(this);
  }

}
