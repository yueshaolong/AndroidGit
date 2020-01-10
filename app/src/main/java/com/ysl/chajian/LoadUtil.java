package com.ysl.chajian;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.PathClassLoader;

public class LoadUtil {

    public static void loadChaJian(Context context) {
        try {
            //反射获取BaseDexClassLoader，并拿到DexPathList属性
            Class<?> aClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = aClass.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            //反射获取到DexPathList对象, 拿到dexElements属性
            Class<?> dexPathList = Class.forName("dalvik.system.DexPathList");
            Field dexElementsField = dexPathList.getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);

            //先获取插件的classloader
            PathClassLoader pathClassLoader = new PathClassLoader(
                    "/sdcard/chajianc-debug.apk", context.getClassLoader());
            //通过classloader获取到真实的DexPathList对象
            Object pluginPathList = pathListField.get(pathClassLoader);
            //拿到Elements数组
            Object[] pliginElements = (Object[]) dexElementsField.get(pluginPathList);

            //获取宿主的classloader
            PathClassLoader mainPathClassLoader = (PathClassLoader) context.getClassLoader();
            Object hostPathList = pathListField.get(mainPathClassLoader);
            Object[] hostElements = (Object[]) dexElementsField.get(hostPathList);

            //创建新数组
            Object[] newElements = (Object[]) Array.newInstance(pliginElements.getClass().getComponentType(),
                    pliginElements.length + hostElements.length);
            System.arraycopy(hostElements, 0, newElements, 0, hostElements.length);
            System.arraycopy(pliginElements, 0, newElements, hostElements.length, pliginElements.length);

            // 将创建的 dexElements 放到宿主的 dexElements中
            // 宿主的dexElements = 新的dexElements
            dexElementsField.set(hostPathList, newElements);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
