package com.ysl.netphoto;

import retrofit2.http.Field;

/**
 * Created by Administrator on 2018/9/26.
 */

public class ParamsBean {
    public String cityname;
    public String key;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ParamsBean{" +
                "cityname='" + cityname + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
