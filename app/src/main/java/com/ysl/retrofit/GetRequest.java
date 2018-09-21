package com.ysl.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/9/21.
 */


public class GetRequest {
    public void request(){
        /*Retrofit retrofit = */new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IGetRequest.class)
                .getCall()
                .enqueue(new Callback<GetBean>() {
                    @Override
                    public void onResponse(Call<GetBean> call, Response<GetBean> response) {
                        System.out.println("请求成功："+response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<GetBean> call, Throwable t) {
                        System.out.println("请求失败！");
                    }
                });
    }
}
