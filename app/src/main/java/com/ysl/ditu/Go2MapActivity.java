package com.ysl.ditu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ysl.helloworld.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Go2MapActivity extends AppCompatActivity {
    private static final String TAG = "dt";
    //1.百度地图包名
    public static final String BAIDUMAP_PACKAGENAME = "com.baidu.BaiduMap";
    //2.高德地图包名
    public static final String AUTONAVI_PACKAGENAME = "com.autonavi.minimap";
    //3.腾讯地图包名
    public static final String QQMAP_PACKAGENAME = "com.tencent.map";
    /**
     * 参数的key
     * 高德的坐标系 "gd_lng" (高德_经度)、"gd_lat"（纬度）、"destination"（目的地名称）
     */
    private static final String GCJO2_LNG = "gd_lng";
    private static final String GCJO2_LAT = "gd_lat";
    private static final String DESTINATION = "destination";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2Map();
            }
        });
    }

    private void go2Map() {
        List<String> packages = checkInstalledPackage(this,MAP_PACKAGES);
        if (packages.size() == 0) {
            Toast.makeText(this, "请安装地图应用", Toast.LENGTH_LONG).show();
        } else {
            Map<String, String> arg = new HashMap();
            arg.put(DESTINATION,"天安门");
            for (String packageName : packages) {
                switch (packageName) {
                    case AUTONAVI_PACKAGENAME:
//                        arg.put(GCJO2_LNG,"116.39752865");
//                        arg.put(GCJO2_LAT,"39.90873221");
                        arg.put(GCJO2_LNG,"113.26924183333334");
                        arg.put(GCJO2_LAT,"34.805176");
                        double[] doubles = GPSUtil.gps84_To_Gcj02(34.805176, 113.26924183333334);
                        arg.put(GCJO2_LNG,doubles[1]+"");
                        arg.put(GCJO2_LAT,doubles[0]+"");
                        invokeAuToNaveMap(this, arg);
                        return;
                    case QQMAP_PACKAGENAME:
                        arg.put(GCJO2_LNG,"116.39752865");
                        arg.put(GCJO2_LAT,"39.90873221");
                        invokeQQMap(this, arg);
                        return;
                    case BAIDUMAP_PACKAGENAME:
                        arg.put(GCJO2_LNG,"116.39752865");
                        arg.put(GCJO2_LAT,"39.90873221");
                        invokeBaiDuMap(this, arg);
                        return;
                }
            }
        }
    }

    /**
     * 调用百度地图----------------
     *
     * @param context 上下文对象
     * @param arg     参数
     */
    private static void invokeBaiDuMap(Context context, Map arg) {

        try {
            Uri uri = Uri.parse("baidumap://map/geocoder?" +
                    "location=" + arg.get(GCJO2_LAT) + "," + arg.get(GCJO2_LNG) +
                    "&name=" + arg.get(DESTINATION) + //终点的显示名称
                    "&coord_type=gcj02");//坐标 （百度同样支持他自己的db0911的坐标，但是高德和腾讯不支持）
            Intent intent = new Intent();
            intent.setPackage(BAIDUMAP_PACKAGENAME);
            intent.setData(uri);

            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    /**
     * 调用高德地图
     *
     * @param context 上下文对象s
     * @param arg     经纬度参数map
     */
    private static void invokeAuToNaveMap(Context context, Map arg) {

        try {
            Uri uri = Uri.parse("androidamap://route?sourceApplication={com.ysl.myapplication}" +
                    "&dlat=" + arg.get(GCJO2_LAT)//终点的纬度
                    + "&dlon=" + arg.get(GCJO2_LNG)//终点的经度
                    + "&dname=" + arg.get(DESTINATION)//终点的显示名称
                    + "&dev=0&m=0&t=0");
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            intent.addCategory("android.intent.category.DEFAULT");

            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    /**
     * 调用腾讯地图
     *
     * @param context 上下文对象s
     * @param arg     经纬度参数map
     */
    private static void invokeQQMap(Context context, Map arg) {
        try {
            Uri uri = Uri.parse("qqmap://map/routeplan?type=drive" +
                    "&to=" + arg.get(DESTINATION)//终点的显示名称 必要参数
                    + "&tocoord=" + arg.get(GCJO2_LAT) + "," + arg.get(GCJO2_LNG)//终点的经纬度
                    + "&referer={com.ysl.myapplication}");
            Intent intent = new Intent();
            intent.setData(uri);

            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static final String[] MAP_PACKAGES = {AUTONAVI_PACKAGENAME, BAIDUMAP_PACKAGENAME, QQMAP_PACKAGENAME};
    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param packageNames 可变参数 String[]
     * @return 目标软件中已安装的列表
     */
    public static List<String> checkInstalledPackage(Context sContext, String... packageNames) {
        //获取packageManager
        final PackageManager packageManager = sContext.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储
        List<String> newPackageNames = new ArrayList<>();
        int count = packageNames.length;

        if (packageInfos != null && packageInfos.size() > 0) {

            outermost:for (String packageName : packageNames) {
                for (int i = 0; i < packageInfos.size(); i++) {
                    String packageInfo = packageInfos.get(i).packageName;
                    if (packageInfo.contains(packageName)) {
                        newPackageNames.add(packageName);
                        if (newPackageNames.size() == count) {
                            break outermost;//这里使用了循环标记，跳出外层循环
                        }
                    }
                }
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return newPackageNames;
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789abcdef";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * 16进制转换成为string类型字符串
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
//            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
//        char[] chars = "0123456789ABCDEF".toCharArray();
        char[] chars = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }
    public void addition_isCorrect() {
//        System.out.println(hexStringToString("31313331312e3331373136"));
//        String s = hexStringToString("31313331312e3331373136");
        System.out.println(hexStringToString("333935392e363239333900"));
        String s = hexStringToString("333935392e363239333900");
        int i = s.indexOf(".");
        String s1 = s.substring(0, i - 2);
        String s2 = s.substring(i - 2);
        System.out.println(s1+"----"+s2);
        System.out.println(Integer.parseInt(s1)+Double.parseDouble(s2)/60.0f);

        System.out.println(str2HexStr("你好"));
        System.out.println(hexStr2Str("e4bda0e5a5bd"));
        System.out.println(hexStringToString("e4bda0e5a5bd"));
    }
}