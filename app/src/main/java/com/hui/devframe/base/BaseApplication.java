package com.hui.devframe.base;

import android.app.Application;

/**
 * BaseApplication执行一些初始化工作
 * Created by wanghui on 16/6/12.
 */
public class BaseApplication extends Application {
    private static BaseApplication CONTEXT;

    public static Application getApplication() {
        return CONTEXT;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT = this;
    }
}
