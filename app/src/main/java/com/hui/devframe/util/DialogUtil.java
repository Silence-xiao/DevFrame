package com.hui.devframe.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * dialog工具
 * Created by wanghui on 15/9/1.
 */
public class DialogUtil {

    private static ProgressDialog dialog;

    /**
     * 显示toast，length short
     *
     * @param context 上下文
     * @param text    文本
     */
    public static void toast(final Context context, final String text) {
        if (Looper.myLooper() != Looper.getMainLooper()) {//不在主线程
            new Handler(context.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void toastLong(final Context context, final String text) {
        if (Looper.myLooper() != Looper.getMainLooper()) {//不在主线程
            new Handler(context.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    public static void showWaiting(Context context, String text) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(text);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void dismissWaiting() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
