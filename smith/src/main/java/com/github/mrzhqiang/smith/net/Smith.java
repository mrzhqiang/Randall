package com.github.mrzhqiang.smith.net;

import android.support.annotation.WorkerThread;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface Smith {

  /** 注册/登陆接口 */
  @WorkerThread @GET("/cardh.php?action=register") Observable<Login> getLogin(
      @Query("username") String username, @Query("password") String password);

  /** 通过注册时返回的链接进行登陆 */
  @WorkerThread @GET Observable<Login> getLogin(@Url String scripUrl);

  /* 暂时不使用全能模式，因为目前只有一款游戏适配 */
  /*@GET("/") Observable<Home> home();

  @GET Observable<Login> login(@Url String loginLink);

  @FormUrlEncoded @GET Observable<Game> submit(@Url String url, @QueryMap Map<String, String> input);

  @GET() Observable<Game> url(@Url String url);*/
}
