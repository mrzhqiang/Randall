package com.github.mrzhqiang.smith.net;

import android.support.annotation.WorkerThread;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 来自 <a href="http://haowanba.com">好玩吧</a> 的接口
 *
 * @author mrZQ
 */
public interface Smith {

  /** 注册的接口也可以用来登陆，所以不用担心重复注册的问题 */
  @WorkerThread @GET("/cardh.php?action=register") Observable<Login> getLogin(
      @Query("username") String username, @Query("password") String password);

  /** 直接用注册接口登陆，可能需要中转一下，试试这个 */
  @WorkerThread @GET Observable<Login> getLogin(@Url String scripUrl);

  /* 暂时不使用全能模式，因为目前只有一款游戏适配 */
  /*@GET("/") Observable<Home> home();

  @GET Observable<Login> login(@Url String loginLink);

  @FormUrlEncoded @GET Observable<Game> submit(@Url String url, @QueryMap Map<String, String> input);

  @GET() Observable<Game> url(@Url String url);*/
}
