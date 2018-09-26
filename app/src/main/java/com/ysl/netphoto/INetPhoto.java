package com.ysl.netphoto;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2018/9/25.
 */
//http://pic41.nipic.com/20140509/4746986_145156378323_2.jpg
//https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=867852534,1215232602&fm=26&gp=0.jpg
//http://icon.nipic.com/BannerPic/20180925/original/20180925085745_1.jpg
//http://d.hiphotos.baidu.com/image/h%3D300/sign=0defb42225381f3081198ba999004c67/6159252dd42a2834a75bb01156b5c9ea15cebf2f.jpg
//http://op.juhe.cn/onebox/weather/query?cityname=深圳&key=4ea58de8a7573377cec0046f5e2469d5

public interface INetPhoto {
//    @GET("detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&step_word=&hs=0&pn=1&spn=0&di=184596677650&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1109917053%2C4211270766&os=3997212110%2C3574701787&simid=3287285331%2C221812639&adpicid=0&lpn=0&ln=1868&fr=&fmq=1537863450065_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01f09e577b85450000012e7e182cf0.jpg%401280w_1l_2o_100sh.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bzv55s_z%26e3Bv54_z%26e3BvgAzdH3Fo56hAzdH3FZMTvxMzIzODQ%3D_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0&islist=&querylist=")
    @GET("u=867852534,1215232602&fm=26&gp=0.jpg")
    Observable<ResponseBody> getCall();


    @GET("onebox/weather/query?cityname=深圳&key=4ea58de8a7573377cec0046f5e2469d5")
    Observable<WeatherDataBean> getWeather();

    @GET("onebox/weather/query?cityname=深圳")
    Observable<WeatherDataBean> getWeather(@Query("key") String key);

    //@query的作用就相当于拼接字符串：cityname=上海&key=4ea58de8a7573377cec0046f5e2469d5
    @GET("onebox/weather/query?")
    Observable<WeatherDataBean> getWeather(@Query("cityname") String cityname, @Query("key") String key);

    //QueryMap 参数集合
    @GET("onebox/weather/query?")
    Observable<WeatherDataBean> getWeather(@QueryMap Map<String, String> params);

    //函数也可以声明为发送form-encoded（表单形式）和multipart（多部分）数据。
    //当函数有@FormUrlEncoded注解的时候，将会发送form-encoded数据，
    //每个键-值对都要被含有名字的@Field注解和提供值的对象所标注(这他妈是绕口令吗？)
    //每个键值对的写法都是用注解@field标识的，表单形式的数据
    @FormUrlEncoded
    @POST("onebox/weather/query?")
    Observable<WeatherDataBean> getWeather1(@Field("cityname") String cityname, @Field("key") String key);

    //可以通过@Body注解指定一个对象作为Http请求的请求体
    @POST("onebox/weather/query?")//TODO 未测试通过
    Observable<WeatherDataBean> getWeather(@Body RequestBody requestBody);

    //当函数有@Multipart注解的时候，将会发送multipart数据，
    // Parts都使用@Part注解进行声明
    //Multipart parts要使用Retrofit的众多转换器之一或者实现RequestBody来处理自己的序列化。
    //这个可以用于传文件,可以改变传值的编码，默认utf_8
    @Multipart
    @POST("onebox/weather/query?")
    Observable<WeatherDataBean> getWeather(@Part("cityname") RequestBody requestBody0, @Part("key") RequestBody requestBody1);
}
