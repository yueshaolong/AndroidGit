package com.ysl.helloworld;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.DragEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.ysl.Gank.GankRequest;
import com.ysl.netphoto.INetPhoto;
import com.ysl.netphoto.ParamsBean;
import com.ysl.netphoto.WeatherDataBean;
import com.ysl.retrofit.GetRequest;
import com.ysl.retrofit.PostRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Logger logger = Logger.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityPermissionsDispatcher.needWithPermissionCheck(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //普通用法
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //RxBinding用法
        Disposable subscribe2 = RxView.clicks(fab)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(MainActivity.this, "hello，RxBinding", Toast.LENGTH_SHORT).show();
                    }
                });
        Disposable subscribe1 = RxView.longClicks(fab)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(MainActivity.this, "2222222222", Toast.LENGTH_LONG).show();
                    }
                });
        //拖拽监听
        Disposable subscribe = RxView.drags(fab)
                .subscribe(new Consumer<DragEvent>() {
                    @Override
                    public void accept(DragEvent dragEvent) throws Exception {
                        Toast.makeText(MainActivity.this, "被拖拽了", Toast.LENGTH_LONG).show();
                    }
                });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        verifyStoragePermissions(this);

        configLogger();
        for (int i = 0; i < 10000; i++) {
            logger.info("这是测试日志！！！"+i);
        }


        requestPhoto();//Retrofit和RxJava简单使用
//        setImage();//RxJava的简单使用
//        requestWeather();//Retrofit和RxJava结合使用，到远端请求数据
//        observableZip();//同时请求多个数据，可以打包使用
//        request();//Retrofit简单使用
    }

    public void setOkHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        //同步请求
        try {
            okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }



    public String getLogDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()+ "/app日志/";
    }
    public void configLogger() {
        File dir = new File(getLogDirectory());
        if (!dir.exists()) {
            try {
                //按照指定的路径创建文件夹
                dir.mkdirs();
            } catch (Exception e) {
                System.out.println(""+e);
            }
        }
        File file = new File(getLogDirectory() + "/log.txt");
        if (!file.exists()) {
            try {
                //在指定的文件夹中创建文件
                file.createNewFile();
            } catch (Exception e) {
                System.out.println(""+e);
            }
        }

        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(getLogDirectory() + "log.txt");
        logConfigurator.setRootLevel(Level.ALL);
        logConfigurator.setFilePattern("%d %-5p [%t][%c{2}]-[%l] %m%n");
        logConfigurator.setUseLogCatAppender(true);
        logConfigurator.setMaxFileSize(1024 * 1);
        logConfigurator.setMaxBackupSize(2);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission Granted
//                    System.out.println("申请好权限了！");
//                    configLogger();
//                } else {
//                    System.out.println("权限被拒接！");
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
    public void verifyStoragePermissions(Activity activity) {
        //检测是否有写的权限
        int permission = ActivityCompat.checkSelfPermission(activity,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }else {
            System.out.println("申请好权限了...");
            configLogger();
            logger.debug("这是测试日志！！！debug");
            logger.info("这是测试日志！！！info");
            logger.warn("这是测试日志！！！warn");
            logger.error("这是测试日志！！！error");
        }
    }

    /**
     * Retrofit和RxJava简单使用
     */
    public void requestPhoto() {
        Disposable subscribe = new Retrofit.Builder()
//                .baseUrl("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/")
                .baseUrl("https://upload-images.jianshu.io/upload_images/")
//                .addConverterFactory()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(INetPhoto.class)
                .getCall1()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        ((ImageView) findViewById(R.id.iv)).setBackground(new BitmapDrawable(bitmap));
                    }
                });
    }

    /**
     * RxJava的简单使用
     */
    public void setImage(){
        Disposable subscribe = Observable.just(getResources().getDrawable(R.mipmap.new_add_icon))
                /*.map(new Function<Drawable, Bitmap>() {
                    @Override
                    public Bitmap apply(Drawable drawable) throws Exception {
                        return ((BitmapDrawable)drawable).getBitmap();
                    }
                })*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Drawable>() {
                    @Override
                    public void accept(Drawable drawable) throws Exception {
                        ((ImageView) findViewById(R.id.iv0)).setBackground(drawable);
                    }
                });
    }

    /**
     * Retrofit和RxJava结合使用，到远端请求数据
     */
    public void requestWeather() {
        Map<String, String> params = new HashMap<>();
        params.put("cityname", "武汉");
        params.put("key", "4ea58de8a7573377cec0046f5e2469d5");

        ParamsBean paramsBean = new ParamsBean();
        paramsBean.setCityname("武汉");
        paramsBean.setKey("4ea58de8a7573377cec0046f5e2469d5");
        System.out.println(new Gson().toJson(paramsBean));
        System.out.println(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(paramsBean)).toString());

        RequestBody requestBody0 = RequestBody.create(MediaType.parse("UTF-8"), "北京");
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("UTF-8"), "4ea58de8a7573377cec0046f5e2469d5");

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG){
            //显示日志
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        Disposable subscribe = new Retrofit.Builder()
                .baseUrl("http://op.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(new OkHttpClient.Builder().addInterceptor(logInterceptor).build())
//                .client(new OkHttpClient.Builder().addInterceptor(new MyInterceptor()).build())
                .client(new OkHttpClient.Builder().addNetworkInterceptor(logInterceptor).build())
                .build()
                .create(INetPhoto.class)
                .getWeather("武汉", "4ea58de8a7573377cec0046f5e2469d5")
//                .getWeather1("北京", "4ea58de8a7573377cec0046f5e2469d5")
//                .getWeather("4ea58de8a7573377cec0046f5e2469d5")
//                .getWeather(params)
//                .getWeather()
//                .getWeather(requestBody0,requestBody1)
//                .getWeather(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(paramsBean)))
//                .getWeather1("4ea58de8a7573377cec0046f5e2469d5")
//                .getWeather2("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherDataBean>() {
                    @Override
                    public void accept(WeatherDataBean weatherDataBean) throws Exception {
                        System.out.println("---->" + weatherDataBean);
                        ((TextView) findViewById(R.id.tv)).setText(weatherDataBean.toString());
                    }
                });
    }

    /**
     * 同时请求多个数据，可以打包使用
     */
    public void observableZip() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG){
            //显示日志
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        Disposable subscribe = Observable.zip(
                /*new Builder()
                        .baseUrl("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(new OkHttpClient.Builder().addInterceptor(logInterceptor).build())
                        .build()
                        .create(INetPhoto.class).getCall(),//第一个Observable对象*/
                new Retrofit.Builder()
                        .baseUrl("http://op.juhe.cn/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(new OkHttpClient.Builder().addInterceptor(new MyInterceptor()).build())
                        .build()
                        .create(INetPhoto.class).getWeather("北京", "4ea58de8a7573377cec0046f5e2469d5"),//第一个Observable对象
                new Retrofit.Builder()
                        .baseUrl("http://op.juhe.cn/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(new OkHttpClient.Builder().addInterceptor(new MyInterceptor()).build())
                        .build()
                        .create(INetPhoto.class).getWeather2("1"),//第二个Observable对象
                new BiFunction<WeatherDataBean, WeatherDataBean, String>() {
                    @Override
                    public String apply(WeatherDataBean weatherDataBean, WeatherDataBean weatherDataBean2) throws Exception {
                        return weatherDataBean.toString() + "---" + weatherDataBean2.toString();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                //这里需要注意的是，网络请求在非ui线程。如果返回结果是依赖于Rxjava的，则需要变换线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("----->" + s);
                        ((TextView) findViewById(R.id.tv)).setText(s);
                    }
                });
    }

    /**
     * Retrofit简单使用
     */
    public void request() {
        new GankRequest().request();
        new PostRequest().request();//Retrofit Post请求
        new GetRequest().request();//Retrofit Get请求
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void need() {
        Toast.makeText(this, "need", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void show(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("此APP需要以下权限，下一步将请求权限")
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//继续执行请求
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();//取消执行请求
            }
        })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void denide() {
        Toast.makeText(this, "已拒绝一个或以上权限", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void ask() {
        Toast.makeText(this, "已拒绝一个或以上权限，并不再询问", Toast.LENGTH_SHORT).show();
    }
}
