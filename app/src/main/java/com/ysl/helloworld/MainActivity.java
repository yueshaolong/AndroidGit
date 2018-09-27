package com.ysl.helloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.ysl.Gank.GankRequest;
import com.ysl.netphoto.INetPhoto;
import com.ysl.netphoto.ParamsBean;
import com.ysl.netphoto.WeatherDataBean;
import com.ysl.retrofit.GetRequest;
import com.ysl.retrofit.PostRequest;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        RxView.clicks(fab)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Toast.makeText(MainActivity.this, "hello，RxBinding", Toast.LENGTH_SHORT).show();
            }
        });
        RxView.longClicks(fab)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(MainActivity.this, "2222222222", Toast.LENGTH_LONG).show();
                    }
                });
        //拖拽监听
        RxView.drags(fab)
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

        Logger.addLogAdapter(new AndroidLogAdapter());

        requestWeather();
        requestPhoto();
        observableZip();
    }

    private void observableZip() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG){
            //显示日志
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }else {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        Observable.zip(
                /*new Builder()
                        .baseUrl("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(new OkHttpClient.Builder().addInterceptor(logInterceptor).build())
                        .build()
                        .create(INetPhoto.class).getCall(),//第一个Observable对象*/
                new Builder()
                        .baseUrl("http://op.juhe.cn/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(new OkHttpClient.Builder().addInterceptor(new MyInterceptor()).build())
                        .build()
                        .create(INetPhoto.class).getWeather("北京","4ea58de8a7573377cec0046f5e2469d5"),//第一个Observable对象
                new Builder()
                        .baseUrl("http://op.juhe.cn/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(new OkHttpClient.Builder().addInterceptor(new MyInterceptor()).build())
                        .build()
                        .create(INetPhoto.class).getWeather2("1"),//第二个Observable对象
                new BiFunction<WeatherDataBean, WeatherDataBean, String>() {
                    @Override
                    public String apply(WeatherDataBean weatherDataBean, WeatherDataBean weatherDataBean2) throws Exception {
                        return weatherDataBean.toString()+"---"+weatherDataBean2.toString();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                //这里需要注意的是，网络请求在非ui线程。如果返回结果是依赖于Rxjava的，则需要变换线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("----->"+s);
                    }
                });
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

    public void requestPhoto() {
        new Retrofit.Builder()
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
                        ((ImageView)findViewById(R.id.iv)).setImageBitmap(bitmap);
                    }
                });
    }

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

        new Builder()
                .baseUrl("http://op.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(new OkHttpClient.Builder().addInterceptor(logInterceptor).build())
//                .client(new OkHttpClient.Builder().addInterceptor(new MyInterceptor()).build())
                .client(new OkHttpClient.Builder().addNetworkInterceptor(logInterceptor).build())
                .build()
                .create(INetPhoto.class)
//                .getWeather("郑州", "4ea58de8a7573377cec0046f5e2469d5")
//                .getWeather1("北京", "4ea58de8a7573377cec0046f5e2469d5")
//                .getWeather("4ea58de8a7573377cec0046f5e2469d5")
//                .getWeather(params)
//                .getWeather()
//                .getWeather(requestBody0,requestBody1)
//                .getWeather(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(paramsBean)))
//                .getWeather1("4ea58de8a7573377cec0046f5e2469d5")
                .getWeather2("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherDataBean>() {
                    @Override
                    public void accept(WeatherDataBean weatherDataBean) throws Exception {
                        System.out.println("---->"+weatherDataBean);
                        ((TextView)findViewById(R.id.tv)).setText(weatherDataBean.toString());
                    }
                });
    }

    public void setImage(){
        Observable.just(getResources().getDrawable(R.mipmap.new_add_icon))
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
                        ((ImageView)findViewById(R.id.iv)).setBackground(drawable);
                    }
                });
    }

    public static void request() {
        new GankRequest().request();
        new PostRequest().request();
        new GetRequest().request();
    }

    public static void main(String[] args) {
        fromJust();
    }

    public static void fromJust() {
        String[] names = new String[]{"a","b","c"};

        Observable.just(2,8, 9, 3,4)
                .toMultimap(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "key" + integer;
                    }
                }, new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "value" + integer;
                    }
                })
                .subscribe(new Consumer<Map<String, Collection<String>>>() {
                    @Override
                    public void accept(Map<String, Collection<String>> stringCollectionMap) throws Exception {
                        System.out.println("onNext : toMultimap : " + stringCollectionMap.toString());
                    }
                });

        /*Observable.just(2,8, 9, 3,4)
                .toMap(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "key" + integer;
                    }
                }, new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "value"+integer;
                    }
                })
                .subscribe(new Consumer<Map<String, String>>() {
                               @Override
                               public void accept(Map<String, String> stringStringMap) throws Exception {
                                   System.out.println("onNext : toMap : " + stringStringMap.toString());
                               }
                           }
                        *//*new Consumer<Map<String, Integer>>() {
                    @Override
                    public void accept(Map<String, Integer> stringIntegerMap) throws Exception {
                        System.out.println("onNext : toMap : " + stringIntegerMap.toString());
                    }
                }*//*);*/

        /*Observable.just(2,8, 9, 3,4)
                .toSortedList(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer integer, Integer t1) {
                        return t1 - integer;
                    }
                })
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println("onNext : toList : " + integers.toString());
                    }
                });*/

        /*Observable.just(2,3,4)
                .toList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println("onNext : toList : " + integers.toString());
                    }
                });*/

        /*Observable.just(2,3,4)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : doOnNext : 准备发射");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : doOnNext : " + integer);
                    }
                });*/

        /*Observable.just(2,3,4)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("onNext : count : " + aLong);
                    }
                });*/

        /*Observable.just(2,3,4,5)
                .collect(new Callable<List<Integer>>() {
                    @Override
                    public List<Integer> call() throws Exception {
                        return new ArrayList<Integer>();
                    }
                }, new BiConsumer<List<Integer>, Integer>() {
                    @Override
                    public void accept(List<Integer> integers, Integer integer) throws Exception {
                        integers.add(integer);
                    }
                })
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println("onNext : collect : " + integers.toString());
                    }
                });*/

        /*Observable.just(2,3,4,5)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer+integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : reduce : " + integer);
                    }
                });*/

        /*Observable.just(2,3,4,5)//TODO 未通过验证
                .skipUntil(Observable.just(5))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : skipUntil : " + integer);
                    }
                });*/

        /*Observable.just(2,3,4,5)
                .skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 10;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : skipWhile : " + integer);
                    }
                });*/

        /*Observable.just(2,3,4,5)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer == 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : takeUntil : " + integer);
                    }
                });*/

        /*Observable.just(2,3,4,5)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer <= 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : takeWhile : " + integer);
                    }
                });*/

        /*Observable.empty()
                .switchIfEmpty(Observable.just(2,3,4))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object integer) throws Exception {
                        System.out.println("onNext : switchIfEmpty : " + integer);
                    }
                });*/

        /*Observable<Integer> observable1=Observable.create(new ObservableOnSubscribe<Integer>() {//TODO 未通过验证
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    emitter.onError(e);
                }
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        });

        Observable<Integer> observable2=Observable.create(new ObservableOnSubscribe<Integer>() {
                                                              @Override
                                                              public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                                                  emitter.onNext(3);
                                                                  emitter.onNext(4);
                                                                  emitter.onComplete();
                                                              }
                                                          });

        Observable.amb(observable1, observable2).subscribe();*/
        
        /*Observable.just(5,6,4,8798)
                .isEmpty()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        System.out.println("onNext : isEmpty : " + aBoolean);
                    }
                });*/

        /*Observable.sequenceEqual(Observable.just(2,4,5),Observable.just(2,3,4,5))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        System.out.println("onNext : sequenceEqual : " + aBoolean);
                    }
                });*/

        /*Observable.just(7,8,65,23,42)
                .contains(8).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                System.out.println("onNext : contains : " + aBoolean);
            }
        });*/

        /*Observable.just(7,8,65,23,42)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 100;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                System.out.println("onNext : all : " + aBoolean);
            }
        });*/

        /*Observable.just(3,4,5,6,3,3,4,9)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : distinct : " + integer);
                    }
                });*/

        /*Observable.just(3,4,5,6,3,3,4,9)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : distinct : " + integer);
                    }
                });*/

        /*Observable.just(30,4,50,6)
                .skipLast(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : skipLast : " + integer);
                    }
                });*/

        /*Observable.just(30,4,50,6)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : skip : " + integer);
                    }
                });*/

        /*Observable.just(30,4,5,6)
                .first(0)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : first : " + integer);
                    }
                });*/

        /*Observable.just(3,4,5,6)
                .last(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : last : " + integer);
                    }
                });*/

        /*Observable.just(3,4,5,6,3,4,5,6,3,4)
                .takeLast(2)//发射前三个数据项
                .takeLast(100, TimeUnit.MILLISECONDS)//发射100ms内的数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : takeLast : " + integer);
                    }
                });*/

        /*Observable.just(3,4,5,6,3,4,5,6,3,4)
                .take(10)//发射前三个数据项
                .take(100, TimeUnit.MILLISECONDS)//发射100ms内的数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : take : " + integer);
                    }
                });*/

        /*Observable.just(1,2,"w", "s")
                .ofType(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : ofType : " + s);
                    }
                });*/

        /*Observable.just(7,8,65,23,42)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 10;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("onNext : filter : " + integer);
            }
        });*/

        /*String[] aStrings = {"A1", "A2", "A3", "A4"};
        String[] bStrings = {"B1", "B2", "B3"};

        Observable<String> aObservable = Observable.fromArray(aStrings);
        Observable<String> bObservable = Observable.fromArray(bStrings);

        Observable.merge(bObservable, aObservable)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : merge : " + s);
                    }
                });*/

        /*Observable<Integer> observable1=Observable.just(1,2,3,4);
        Observable<Integer> observable2=Observable.just(4,5,6);*/

        /*Observable.concat(observable2, observable1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : concat : " + integer);
                    }
                });*/
        /*Observable.just(1,2,3,4,5)
                .startWith(observable2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : startWith : " + integer);
                    }
                });*/


        /*Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hahahhah";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("onNext : fromCallable : " + s);
            }
        });*/

        /*Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                return Observable.just("hello");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("onNext : defer : " + s);
            }
        });*/

        /*Observable.range(2,5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : range : " + integer);
                    }
                });*/

        /*Observable.interval(1, TimeUnit.SECONDS)//TODO 未通过测试
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("onNext : interval : " + aLong);
                    }
                });*/
        /*Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("onNext : timer : " + aLong);
                        Log.e("a","onNext : timer : " + aLong);
                    }
                });*/

        /*Observable.never();//创建一个什么都不做的Observable,直接调用onCompleted。
        Observable.error(new RuntimeException());//创建一个什么都不做直接通知错误的Observable,直接调用onError。这里可以自定义异常
        Observable.empty();//创建一个什么都不做直接通知完成的Observable*/
        /*Observable.fromArray(names)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.print("onNext : fromArray : " + s + "\n");
                    }
                });*/
        /*Observable.just(names)
                .subscribe(new Consumer<String[]>() {
                    @Override
                    public void accept(String[] strings) throws Exception {
                        System.out.print("onNext : just : " + Arrays.toString(strings) + "\n");
                    }
                });*/
        /*Observable.just("ad", "bgf", "dsf")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.print("onNext : just3 : " + s + "\n");
                    }
                });*/
    }

    public static void observer(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("hahads");
                emitter.onComplete();
            }
        }).subscribe(/*new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext : " + s );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onNext : onComplete" );
            }
        }*/
                new Subject<String>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext : " + s );
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onNext : onComplete" );
                    }
                });
    }
}
