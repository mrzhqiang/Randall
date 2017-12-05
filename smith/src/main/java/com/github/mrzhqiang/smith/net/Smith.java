package com.github.mrzhqiang.smith.net;

import android.database.Observable;
import com.github.mrzhqiang.smith.net.html.Register;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 来自 <a href="http://haowanba.com">好玩吧</a> 的接口
 *
 * @author mrZQ
 */
public interface Smith {

  @GET("/cardh.php?action=passport") Observable<Login> login(@Query("name") String username,
      @Query("pwd") String password);

  @GET("/cardh.php?action=register") Observable<Register> register(
      @Query("username") String username, @Query("password") String password);


  /* 暂时不使用全能模式，因为目前只有一款游戏适配 */
  /*@GET("/") Observable<Home> home();

  @GET Observable<Login> login(@Url String loginLink);

  @FormUrlEncoded @GET Observable<Game> submit(@Url String url, @QueryMap Map<String, String> input);

  @GET() Observable<Game> url(@Url String url);*/
}
