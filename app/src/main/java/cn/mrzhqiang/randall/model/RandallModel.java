package cn.mrzhqiang.randall.model;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import cn.mrzhqiang.randall.RandallApp;
import cn.mrzhqiang.randall.data.Home;
import cn.mrzhqiang.randall.net.Randall;
import com.squareup.sqlbrite.BriteDatabase;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 兰达尔模块，加载并解析网页内容
 */
public final class RandallModel {
  private static final String TAG = "RandallModel";

  @Inject Randall randall;
  @Inject BriteDatabase db;

  public RandallModel() {
    RandallApp.appComponent().inject(this);
  }

  public void loadHome(@NonNull Action1<Home> action1) {
    randall.home().unsubscribeOn(Schedulers.io()).map(new Func1<Document, Home>() {
      @Override public Home call(Document document) {
        if (document != null) {
          // TODO 存数据库，界面显示刷新按钮。检测有值，不显示进度条；刷新时，再显示。
          return Home.parse(document);
        }
        return null;
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        Log.e(TAG, "加载主页时，发生错误：" + throwable.getMessage());
      }
    });
  }
}
