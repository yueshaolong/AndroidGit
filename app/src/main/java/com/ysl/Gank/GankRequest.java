package com.ysl.Gank;

import com.ysl.retrofit.GetBean;
import com.ysl.retrofit.IGetRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/9/21.
 */

public class GankRequest {
    public void request(){
        new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IGank.class)
                .getCall(3)
                .enqueue(new Callback<GankBean>() {
                    @Override
                    public void onResponse(Call<GankBean> call, Response<GankBean> response) {
                        System.out.println("请求成功："+response.body().getResults().get(0).getDesc());
                    }

                    @Override
                    public void onFailure(Call<GankBean> call, Throwable t) {
                        System.out.println("请求失败！");
                    }
                });
    }
}
