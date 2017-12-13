package com.github.mrzhqiang.smith.net;

import android.support.annotation.WorkerThread;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface Smith {

  String MALE = "male";
  String FEMALE = "female";
  String PIKE_FIGHTER = "fighter.pike";
  String PIKE_archer = "archer.pike";
  String PIKE_SORCERER = "sorcerer.pike";

  /** 注册/登陆接口 */
  @WorkerThread @GET("/cardh.php?action=register") Observable<Login> getLogin(
      @Query("username") String username, @Query("password") String password);

  /** 通过注册时返回的链接进行登陆 */
  @WorkerThread @GET Observable<Login> getLogin(@Url String scripUrl);

  @WorkerThread @GET Observable<Game> getHref(@Url String href);

  @WorkerThread @GET Observable<Game> getRole(@Url String gameUrl, @Query("name") String name,
      @Query("sex") String sex, @Query("profession") String profession);

  /* 暂时不使用全能模式，因为目前只有一款游戏适配 */
  /*@GET("/") Observable<Home> home();

  @GET Observable<Login> login(@Url String loginLink);

  @FormUrlEncoded @GET Observable<Game> submit(@Url String url, @QueryMap Map<String, String> input);

  @GET() Observable<Game> url(@Url String url);*/
}
