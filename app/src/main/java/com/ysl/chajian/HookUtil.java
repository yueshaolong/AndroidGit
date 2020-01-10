package com.ysl.chajian;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookUtil {

    public static final String CHAJIANACT = "chajianact";
    public static void hookStartAct(){
        try {
            //反射获取IActivityManager的class对象
            Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");

            //反射获取ActivityManager，获取IActivityManagerSingleton属性
            Class<?> activityManagerClass = Class.forName("android.app.ActivityManager");
            Field iActivityManagerSingletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
            iActivityManagerSingletonField.setAccessible(true);
            Object singleton = iActivityManagerSingletonField.get(null);//因为此属性是静态的，所以传null

            //反射Singleton，获取mInstance属性
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            Object mInstance = mInstanceField.get(singleton);//参数是调用它的对象，IActivityManagerSingleton

            //找到hook点：Instrumentation$execStartActivity$1668行
            //使用动态代理方式，重写startActivity方法
            Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),//类加载器ClassLoader
                    new Class[]{iActivityManagerClass},//被代理的接口类，这里是IActivityManager
                    new InvocationHandler() {
                        /**
                         *动态代理的实现
                         * @param proxy  代理对象
                         * @param method  被代理的对象的所有方法
                         * @param args 方法中传入的参数集合
                         * @return 代理对象
                         * @throws Throwable
                         */
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            //在这里面找到startActivity方法，并替换参数列表里的intent
                            if ("startActivity".equals(method.getName())) {
                                int index = -1;
                                for (int i = 0; i < args.length; i++) {
                                    if (args[i] instanceof Intent) {
                                        index = i;
                                        break;
                                    }
                                }
                                if (index >= 0) {
                                    Intent intent = (Intent) args[index];
                                    Intent proxyIntent = new Intent();
                                    proxyIntent.setComponent(new ComponentName("com.ysl.helloworld",
                                            "com.ysl.chajian.ProxyActivity"));
                                    proxyIntent.putExtra(CHAJIANACT, intent);
                                    args[index] = proxyIntent;
                                }
                            }
                            //第一个参数是调用次方法的对象,Singleton$mInstance，第二个参数是方法所需参数
                            return method.invoke(mInstance, args);
                        }
                    });

            //使用代理对象替换mInstance；因为mInstance就是源码中的ActivityManager.getService()，
            // 就是mInstance调用的startActivity方法
            //第一个参数，被修改的对象;第二个参数，此属性新的值
            mInstanceField.set(singleton, proxyInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookHander(){
        try {
            //找到hook点是发送handler的地方

            //反射获取ActivityThread对象
            //private static volatile ActivityThread sCurrentActivityThread;
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object activityThread = sCurrentActivityThreadField.get(null);

            //反射获取mH的对象
            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Object mH = mHField.get(activityThread);

            //定义callback对象替换原来的空对象，并在callback里面进行intent的替换
            //反射获取属性mCallback，并设置它的值不为空
            Class<?> handlerClass = Class.forName("android.os.Handler");
            Field mCallbackField = handlerClass.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean handleMessage(Message msg) {
                    Log.e("---->", "handleMessage: "+msg.toString());
                    switch (msg.what) {
                        case 100:
                            try {
                                Class<?> activityClientRecordClass = msg.obj.getClass();
                                Field intentField = activityClientRecordClass.getDeclaredField("intent");
                                intentField.setAccessible(true);
                                Intent proxyIntent = (Intent) intentField.get(msg.obj);

                                Intent intent = proxyIntent.getParcelableExtra(CHAJIANACT);
                                Log.e("---->", "handleMessage: 拿到插件intent："+intent.toString());
                                proxyIntent.setComponent(intent.getComponent());
//                                intentField.set(msg.obj, intent);//是否可以替换为这句
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 146:
                            Pair<IBinder, ActivityOptions> pair = (Pair<IBinder, ActivityOptions>) msg.obj;
//                            onNewActivityOptions(pair.first, pair.second);
                            try {
                                Class<?> activityClientRecordClass = pair.second.getClass();
                                Field intentField = activityClientRecordClass.getDeclaredField("intent");
                                intentField.setAccessible(true);
                                Intent proxyIntent = (Intent) intentField.get(msg.obj);

                                Intent intent = proxyIntent.getParcelableExtra(CHAJIANACT);
                                Log.e("---->", "handleMessage: 拿到插件intent："+intent.toString());
                                proxyIntent.setComponent(intent.getComponent());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 159:
                            try {
                                Class<?> transactionClass = Class.forName("android.app.servertransaction.ClientTransaction");
                                Field mActivityTokenField = transactionClass.getDeclaredField("mActivityToken");
                                mActivityTokenField.setAccessible(true);
                                IBinder mActivityToken = (IBinder) mActivityTokenField.get(msg.obj);

                                Field mActivitiesField = activityThreadClass.getDeclaredField("mActivities");
                                mActivitiesField.setAccessible(true);
                                ArrayMap<IBinder, Object> arrayMap = (ArrayMap<IBinder, Object>) mActivitiesField.get(activityThread);
                                Object o = arrayMap.get(mActivityToken);

                                Class<?> activityClientRecordClass = o.getClass();
                                Field intentField = activityClientRecordClass.getDeclaredField("intent");
                                intentField.setAccessible(true);
                                Intent proxyIntent = (Intent) intentField.get(msg.obj);

                                Intent intent = proxyIntent.getParcelableExtra(CHAJIANACT);
                                Log.e("---->", "handleMessage: 拿到插件intent："+intent.toString());
                                proxyIntent.setComponent(intent.getComponent());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
