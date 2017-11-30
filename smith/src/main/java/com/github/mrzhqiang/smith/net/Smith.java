package com.github.mrzhqiang.smith.net;

import java.util.Map;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 来自 <a href="http://haowanba.com">好玩吧</a> 的接口
 *
 * @author mrZQ
 */
public interface Smith {
  @GET("/") Observable<Home> home();

  @GET Observable<Login> login(@Url String loginLink);

  @FormUrlEncoded @GET Observable<Game> submit(@Url String url, @QueryMap Map<String, String> input);

  @GET() Observable<Game> url(@Url String url);
}
