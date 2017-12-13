package com.github.mrzhqiang.smith.model;

import android.support.annotation.NonNull;
import com.github.mrzhqiang.smith.SmithApp;
import com.github.mrzhqiang.smith.net.Game;
import com.github.mrzhqiang.smith.net.Login;
import com.github.mrzhqiang.smith.net.Result;
import com.github.mrzhqiang.smith.net.Smith;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public final class SmithModel {
  private static final String TAG = "SmithModel";

  @Inject Smith smith;
  @Inject BriteDatabase db;

  private static final Set<Subscription> SUBSCRIPTIONS = new HashSet<>();

  public SmithModel() {
    SmithApp.appComponent().inject(this);
  }

  public void login(Login login, @NonNull Result<Game> result) {
    Observable.just(login)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .flatMap(login1 -> smith.getHref(login1.lastGame().href()).flatMap(game -> {
          if ("".equals(game.script())) {
            return Observable.just(game);
          }
          return smith.getHref(game.script());
        }))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result);
  }
}
