package com.ysl.netphoto;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ysl.helloworld.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Administrator on 2018/9/25.
 */

public class NetPhotoRequest {
//    public void requestPhoto() {
//        new Retrofit.Builder()
//                .baseUrl("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&step_word=&hs=0&pn=1&spn=0&di=184596677650&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1109917053%2C4211270766&os=3997212110%2C3574701787&simid=3287285331%2C221812639&adpicid=0&lpn=0&ln=1868&fr=&fmq=1537863450065_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01f09e577b85450000012e7e182cf0.jpg%401280w_1l_2o_100sh.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bzv55s_z%26e3Bv54_z%26e3BvgAzdH3Fo56hAzdH3FZMTvxMzIzODQ%3D_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0&islist=&querylist=")
////                .addConverterFactory()
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//                .create(INetPhoto.class)
//                .getCall()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Drawable>() {
//                    @Override
//                    public void accept(Drawable drawable) throws Exception {
////                        ((ImageView)findViewById(R.id.iv)).setBackground(drawable);
//                    }
//                });
//    }
}
