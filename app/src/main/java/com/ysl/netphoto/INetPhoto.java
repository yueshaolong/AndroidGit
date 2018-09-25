package com.ysl.netphoto;

import com.ysl.netphoto.WeatherDataBean.ResultBean.DataBean.RealtimeBean.WeatherBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
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

    @GET("onebox/weather/query?")
    Observable<WeatherDataBean> getWeather(@QueryMap Map<String, String> params);

    @GET("onebox/weather/query?cityname=深圳")
    Observable<WeatherDataBean> getWeather(@Query("key") String key);

    @GET("onebox/weather/query?")
    Observable<WeatherDataBean> getWeather(@Query("cityname") String cityname, @Query("key") String key);

}
