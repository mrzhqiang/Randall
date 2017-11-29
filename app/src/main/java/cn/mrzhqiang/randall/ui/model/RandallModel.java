package cn.mrzhqiang.randall.ui.model;

import cn.mrzhqiang.randall.RandallApp;
import cn.mrzhqiang.randall.net.Randall;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;
import rx.subscriptions.CompositeSubscription;

/**
 * 兰达尔模块，加载并解析网页内容
 */
public final class RandallModel {
  private static final String TAG = "RandallModel";

  @Inject Randall randall;
  @Inject BriteDatabase db;

  public final CompositeSubscription subscriptions = new CompositeSubscription();

  public RandallModel() {
    RandallApp.appComponent().inject(this);
  }

}
