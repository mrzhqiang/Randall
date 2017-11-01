package cn.mrzhqiang.randall.model;

import android.support.annotation.NonNull;
import cn.mrzhqiang.randall.RandallApp;
import cn.mrzhqiang.randall.data.HomePage;
import cn.mrzhqiang.randall.data.LoginPage;
import cn.mrzhqiang.randall.data.RegisterPage;
import cn.mrzhqiang.randall.net.Randall;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.Map;
import javax.inject.Inject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rx.Scheduler;
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

  public final CompositeSubscription subscriptions = new CompositeSubscription();

  public RandallModel() {
    RandallApp.appComponent().inject(this);
  }

  public void loadHome(@NonNull Subscriber<HomePage> subscriber) {
    subscriptions.add(
        randall.home().unsubscribeOn(Schedulers.io()).map(new Func1<Document, HomePage>() {
          @Override public HomePage call(Document document) {
            if (document != null) {
              return HomePage.parse(document);
            }
            return null;
          }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
  }

  public void loadRegister(@NonNull Element registerPath,
      @NonNull Subscriber<RegisterPage> subscriber) {
    String path = RegisterPage.findPath(registerPath);
    Map<String, String> queryMap = RegisterPage.findQueryMap(registerPath);
    subscriptions.add(randall.path(path, queryMap)
        .unsubscribeOn(Schedulers.io())
        .map(new Func1<Document, RegisterPage>() {
          @Override public RegisterPage call(Document document) {
            if (document != null) {
              return RegisterPage.parse(document);
            }
            return null;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber));
  }

  public void loadLogin(@NonNull Element loginPath, @NonNull Subscriber<LoginPage> subscriber) {
    String path = LoginPage.findPath(loginPath);
    Map<String, String> queryMap = LoginPage.findQueryMap(loginPath);
    subscriptions.add(randall.path(path, queryMap)
        .unsubscribeOn(Schedulers.io())
        .map(new Func1<Document, LoginPage>() {
          @Override public LoginPage call(Document document) {
            if (document != null) {
              return LoginPage.parse(document);
            }
            return null;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber));
  }
}
