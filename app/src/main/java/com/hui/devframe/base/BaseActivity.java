package com.hui.devframe.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.hui.devframe.util.DialogUtil;
import com.hui.devframe.util.ReturnCall;

import java.util.HashMap;

/**
 * Created by wanghui on 16/6/12.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final long clickSpanMillSeconds = 650;       // 重复点击检测延迟
    // 对话框工具
    public final DialogUtil dialogUtil = new DialogUtil();
    // 输入参数
    public Intent intent;
    protected FrameLayout mParent;
    protected LayoutInflater mInflater;
    protected Handler mHandler = new Handler();
    protected Activity mThisActivity = this;
    // 重复点击检测
    private long lastClickTime;
    private HashMap<Integer, ResultHandler> resultMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        if (intent != null) {
            getIntentData();
        }

        setContentView(getContentLayoutId());

        mInflater = LayoutInflater.from(this);
        mParent = (FrameLayout) findViewById(android.R.id.content);

        // 统一处理startActivityForResult的
        handleResult();
    }

    protected void getIntentData() {
    }

    protected abstract int getContentLayoutId();

    protected void handleResult() {
    }

    // 清理工作
    protected void onDestroy() {
        super.onDestroy();
        resultMap.clear();
    }

    // 延迟点击实现
    public void doDelayClick(ReturnCall callback) {
        long bootedTime = SystemClock.elapsedRealtime();
        if (bootedTime < clickSpanMillSeconds || bootedTime - lastClickTime > clickSpanMillSeconds) {
            if (callback != null) {
                callback.call();
                lastClickTime = bootedTime;
            }
        }
    }

    protected void addResultHandler(int requestCode, ResultHandler handler) {
        if (resultMap.containsKey(requestCode)) {
            throw new RuntimeException("duplicate requestCode");
        }
        resultMap.put(requestCode, handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultMap.containsKey(requestCode)) {
            ResultHandler handler = resultMap.get(requestCode);
            handler.operate(resultCode, data);
        }
    }

    public void toast(String string) {
        DialogUtil.toast(this, string);
    }

    // 通用的result处理
    public interface ResultHandler {
        void operate(int resultCode, Intent data);
    }
}
