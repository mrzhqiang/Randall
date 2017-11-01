package cn.mrzhqiang.randall.net;

import java.util.Map;
import org.jsoup.nodes.Document;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 来自 <a href="http://haowanba.com">好玩吧</a> 的接口
 * <p>
 * Created by mrZQ on 2017/4/5.
 */
public interface Randall {

  String BASE_URL = "http://haowanba.com";

  /** 主页，通常用来获得最新数据：注册/登录的path、服务器列表 */
  @GET("/") Observable<Document> home();

  /** 路径，打开注册/登录页面，获得最新相关接口 */
  @GET("/{path}") Observable<Document> path(@Path("path") String path,
      @QueryMap Map<String, String> queryMap);

  //http://haowanba.com/cardh.php?username=zq12355&password=123555&url=&action=register&_do=&exuid=
  @GET("/cardh.php") Observable<Document> newAccount(@QueryMap Map<String, String> queryMap);

  //    Set-Cookie:JSESSIONID=DEF2DD9692EDCE8B1D7AA75927241FA2
  //    Cookie:JSESSIONID=DEF2DD9692EDCE8B1D7AA75927241FA2

  //    http://haowanba.com/cardh.php?name=28741114&pwd=123455&url=0&exuid=&action=passport&_do=&time=20170408113808
  @GET("/cardh.php") Call<String> login(@Query("name") String username,
      @Query("pwd") String password, @Query("url") String url, @Query("exuid") String exuid,
      @Query("action") String action, @Query("_do") String _do, @Query("time") String time);
}
