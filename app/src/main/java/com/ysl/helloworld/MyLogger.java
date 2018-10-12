package com.ysl.helloworld;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2018/9/28.
 */

public class MyLogger {

    public void getLogger() {

        Logger.d("hello_d");
        Logger.e("hello_e");
        Logger.w("hello_w");
        Logger.v("hello_v");
        Logger.wtf("hello_wtf");
        // 打印json格式
//        String json = createJson().toString();
//        Logger.json(json);
//        // 打印xml格式
//        Logger.xml(XML_CONTENT);
//        // 打印自定义级别、tag、信息等格式日志
//        Logger.log(DEBUG, "tag", "message", throwable);
    }


    // 创建json数据
    public static JSONObject createJson() {
        try {
            JSONObject person = new JSONObject();
            person.put("phone", "12315");
            JSONObject address = new JSONObject();
            address.put("country", "china");
            address.put("province", "fujian");
            address.put("city", "xiamen");
            person.put("address", address);
            person.put("married", true);
            return person;
        } catch (JSONException e) {
            Logger.e(e, "create json error occured");
        }
        return null;
    }
}
