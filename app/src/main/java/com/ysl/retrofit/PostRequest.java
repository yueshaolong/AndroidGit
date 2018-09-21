package com.ysl.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/9/21.
 */

public class PostRequest {
    public void request(){
        new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IPostRequest.class)
                .getCall("i love you")
                .enqueue(new Callback<PostBean>() {
                    @Override
                    public void onResponse(Call<PostBean> call, Response<PostBean> response) {
                        System.out.println("请求成功："+response);
                        System.out.println("请求成功："+response.body());
                        System.out.println("请求成功："+response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<PostBean> call, Throwable t) {
                        System.out.println("请求失败！");
                    }
                });
    }
}
