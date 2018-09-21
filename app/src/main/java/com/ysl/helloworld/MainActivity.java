package com.ysl.helloworld;

import android.os.Bundle;
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
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.ysl.Gank.GankRequest;
import com.ysl.retrofit.GetRequest;
import com.ysl.retrofit.PostRequest;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.Subject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //普通用法
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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

    public static void main(String[] args) {
        new GankRequest().request();
    }

    private static void request() {
        new PostRequest().request();
        new GetRequest().request();
    }

    private static void fromJust() {
        String[] names = new String[]{"a","b","c"};
        Observable.fromArray(names)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.print("onNext : fromArray : " + s + "\n");
                    }
                });
        Observable.just(names)
                .subscribe(new Consumer<String[]>() {
                    @Override
                    public void accept(String[] strings) throws Exception {
                        System.out.print("onNext : just : " + Arrays.toString(strings) + "\n");
                    }
                });
    }
}
