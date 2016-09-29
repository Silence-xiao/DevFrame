package hui.devframe.base;

import android.app.Application;

import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;

import hui.devframe.veex.ImageAdapter;

/**
 * BaseApplication执行一些初始化工作
 * Created by wanghui on 16/6/12.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // veex init
        WXSDKEngine.addCustomOptions("appName", "WXSample");
        WXSDKEngine.addCustomOptions("appGroup", "WXApp");
        WXSDKEngine.initialize(this,
                new InitConfig.Builder()
                        .setImgAdapter(new ImageAdapter())
                        .build()
        );
    }
}
