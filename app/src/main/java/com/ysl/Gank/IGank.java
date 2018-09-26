package com.ysl.Gank;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2018/9/21.
 */

public interface IGank {
    //这里需要注意的是，{}作为取代块一定不能取代参数
    //它会报异常：URL query string "a={city}" must not have replace block. For dynamic query parameters use @Query.
    //翻译：URL查询字符串“= {城市}”必须没有取代块。动态查询参数使用@Query。
    //所以，{}取代块只能替换url而不能替换参数,参数应该用@query
    @GET("api/data/Android/10/{page}")
    Call<GankBean> getCall(@Path("page") int page);
}
