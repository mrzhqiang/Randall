package cn.mrzhqiang.randall.model;

import android.support.annotation.NonNull;
import cn.mrzhqiang.randall.RandallApp;
import cn.mrzhqiang.randall.data.Home;
import cn.mrzhqiang.randall.net.Randall;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;
import org.jsoup.nodes.Document;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 兰达尔模块，加载并解析网页内容
 */
public final class RandallModel {
  private static final String TAG = "RandallModel";

  @Inject Randall randall;
  @Inject BriteDatabase db;

  private final CompositeSubscription subscriptions = new CompositeSubscription();

  public RandallModel() {
    RandallApp.appComponent().inject(this);
  }

  public void loadHome(@NonNull Subscriber<Home> subscriber) {
    subscriptions.add(
        randall.home().unsubscribeOn(Schedulers.io()).map(new Func1<Document, Home>() {
          @Override public Home call(Document document) {
            if (document != null) {
              return Home.parse(document);
            }
            return null;
          }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
  }

  public void exitModel() {
    subscriptions.unsubscribe();
  }
}
