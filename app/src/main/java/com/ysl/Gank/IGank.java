package com.ysl.Gank;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2018/9/21.
 */

public interface IGank {
    @GET("api/data/Android/10/{page}")
    Call<GankBean> getCall(@Path("page") int page);
}
