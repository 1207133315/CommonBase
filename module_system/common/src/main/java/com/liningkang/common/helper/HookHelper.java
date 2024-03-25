package com.liningkang.common.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.liningkang.common.listener.GlobalClickListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookHelper {
    /**
     * hook的核心代码
     * 这个方法的唯一目的：用自己的点击事件，替换掉 View原来的点击事件
     *
     * @param v hook的范围仅限于这个view
     */
    public static void Hook(Context context, View v){
        try {
            //首先进行反射执行View类的getListenerInfo()方法，拿到v的mListenerInfo对象，这个对象就是点击事件的持有者
            Method method = View.class.getDeclaredMethod("getListenerInfo");
            //由于getListenerInfo()方法并不是public的，所以要加这个代码来保证访问权限
            method.setAccessible(true);
            //这里拿到的就是mListenerInfo对象，也就是点击事件的持有者
            Object mListenerInfo = method.invoke(v);
            //获取到当前的点击事件
            Class<?> clz = Class.forName("android.view.View$ListenerInfo");
            //获取内部类的表示方法
            Field field = clz.getDeclaredField("mOnClickListener");
            //保证访问权限
            field.setAccessible(true);
            //获取真实的mOnClickListener对象
            View.OnClickListener onClickListenerInstance = (View.OnClickListener) field.get(mListenerInfo);
            // 用自定义的 OnClickListener 替换原始的 OnClickListener
            View.OnClickListener hookedOnClickListener = new GlobalClickListener(onClickListenerInstance);
            //设置到"持有者"中
            field.set(mListenerInfo, hookedOnClickListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
