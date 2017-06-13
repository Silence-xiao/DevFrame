package com.hui.devframe.common.base;

import android.app.Application;

import com.hui.devframe.veex.ImageAdapter;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;

/**
 * BaseApplication执行一些初始化工作
 * Created by wanghui on 16/6/12.
 */
public class BaseApplication extends Application {
    private static BaseApplication CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT = this;
        // veex init
        WXSDKEngine.addCustomOptions("appName", "WXSample");
        WXSDKEngine.addCustomOptions("appGroup", "WXApp");
        WXSDKEngine.initialize(this,
                new InitConfig.Builder()
                        .setImgAdapter(new ImageAdapter())
                        .build()
        );
    }

    public static Application getApplication() {
        return CONTEXT;
    }
}
