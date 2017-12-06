package com.github.mrzhqiang.smith.net;

import android.support.annotation.WorkerThread;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 来自 <a href="http://haowanba.com">好玩吧</a> 的接口
 *
 * @author mrZQ
 */
public interface Smith {

  @WorkerThread @GET("/cardh.php?action=register") Observable<Login> getRegister(
      @Query("username") String username, @Query("password") String password);

  @WorkerThread @GET("/cardh.php?action=passport") Observable<Login> getLogin(
      @Query("name") String username, @Query("pwd") String password);


  /* 暂时不使用全能模式，因为目前只有一款游戏适配 */
  /*@GET("/") Observable<Home> home();

  @GET Observable<Login> login(@Url String loginLink);

  @FormUrlEncoded @GET Observable<Game> submit(@Url String url, @QueryMap Map<String, String> input);

  @GET() Observable<Game> url(@Url String url);*/
}
