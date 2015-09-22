package com.iamyours.android.android_ioc;

import android.app.Activity;


import com.iamyours.android.android_ioc.annotation.ContentView;
import com.iamyours.android.android_ioc.annotation.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Roger on 2015/9/22.
 */
public class ViewUtils {
    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    /**
     * setContentView
     * @param activity
     */
    private static void injectContentView(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView==null)return;
        int contentViewId = contentView.value();
        try {
            Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW,int.class);
            method.setAccessible(true);
            method.invoke(activity,contentViewId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * findViewById
     * @param activity
     */
    private static void injectViews(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        Field[]fields = clazz.getDeclaredFields();
        Method method = null;
        try {
            method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID,int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if(method==null)return;;
        for(Field field:fields){
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if(viewInject==null)continue;
            int viewId = viewInject.value();
            if(viewId==-1)continue;
            try {
                Object resView = method.invoke(activity,viewId);
                field.setAccessible(true);
                field.set(activity,resView);
            } catch (Exception e) {
                continue;
            }
        }
    }

    public static void inject(Activity activity){
        injectContentView(activity);
        injectViews(activity);
    }
}
