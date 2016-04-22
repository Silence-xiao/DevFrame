package com.homework.hui.utils;

import android.util.Log;


/**
 * 通用的Log组件,当前是否在调试模式由是否是Release版本和Debug开关共同控制<br />
 * Debug版本会自动打印所有日志，Release版本通过如下设置可以打开日志开关 <br />
 * {@code adb shell setprop log.tag.DEBUGSWITCH VERBOSE} <br />
 * <p>使用举例</p>
 * <code>
 * <pre>
 *     private static LogUtils log = LogUtils.getLog("ImageViewUtils");
 *     log.d("Current Count is %d",123);
 *     </pre>
 * </code>
 */
public class LogUtils {
    private String tag;

    /**
     * 生成一个Log对象
     *
     * @param tag 用于生成Log的tag
     * @return
     */
    public static LogUtils getLog(String tag) {
        return new LogUtils("hui-log" + tag);
    }

    private LogUtils(String tag) {
        if (tag.length() > 23) {
            tag = tag.substring(0, 23);
        }
        this.tag = tag;
    }

    public void d(String format, Object... args) {
        log(Log.DEBUG, format, args);
    }

    public void d(Throwable throwable, String format, Object... args) {
        log(Log.DEBUG, throwable, format, args);
    }

    public void d(String msg) {
        log(Log.DEBUG, msg);
    }

    public void d(Throwable throwable, String msg) {
        log(Log.DEBUG, throwable, msg);
    }


    public void e(String format, Object... args) {
        log(Log.ERROR, format, args);
    }

    public void e(Throwable throwable, String format, Object... args) {
        log(Log.ERROR, throwable, format, args);
    }

    public void e(String msg) {
        log(Log.ERROR, msg);
    }

    public void e(Throwable throwable, String msg) {
        log(Log.ERROR, throwable, msg);
    }


    public void w(String format, Object... args) {
        log(Log.WARN, format, args);
    }

    public void w(Throwable throwable, String format, Object... args) {
        log(Log.WARN, throwable, format, args);
    }

    public void w(String msg) {
        log(Log.WARN, msg);
    }

    public void w(Throwable throwable, String msg) {
        log(Log.WARN, throwable, msg);
    }


    public void v(String format, Object... args) {
        log(Log.VERBOSE, format, args);
    }

    public void v(Throwable throwable, String format, Object... args) {
        log(Log.VERBOSE, throwable, format, args);
    }

    public void v(String msg) {
        log(Log.VERBOSE, msg);
    }

    public void v(Throwable throwable, String msg) {
        log(Log.VERBOSE, throwable, msg);
    }


    public void i(String format, Object... args) {
        log(Log.INFO, format, args);
    }

    public void i(Throwable throwable, String format, Object... args) {
        log(Log.INFO, throwable, format, args);
    }

    public void i(String msg) {
        log(Log.INFO, msg);
    }

    public void i(Throwable throwable, String msg) {
        log(Log.INFO, throwable, msg);
    }

    public void log(int level, String format, Object... args) {
        switch (level) {
            case Log.DEBUG:
                Log.d(tag, String.format(format, args));
                break;
            case Log.ERROR:
                Log.e(tag, String.format(format, args));
                break;
            case Log.INFO:
                Log.i(tag, String.format(format, args));
                break;
            case Log.WARN:
                Log.w(tag, String.format(format, args));
                break;
            case Log.VERBOSE:
                Log.v(tag, String.format(format, args));
                break;
        }

    }

    public void log(int level, Throwable throwable, String format, Object... args) {
        switch (level) {
            case Log.DEBUG:
                Log.d(tag, String.format(format, args), throwable);
                break;
            case Log.ERROR:
                Log.e(tag, String.format(format, args), throwable);
                break;
            case Log.INFO:
                Log.i(tag, String.format(format, args), throwable);
                break;
            case Log.WARN:
                Log.w(tag, String.format(format, args), throwable);
                break;
            case Log.VERBOSE:
                Log.v(tag, String.format(format, args), throwable);
                break;
        }

    }

    public void log(int level, Throwable throwable, String msg) {
        switch (level) {
            case Log.DEBUG:
                Log.d(tag, msg, throwable);
                break;
            case Log.ERROR:
                Log.e(tag, msg, throwable);
                break;
            case Log.INFO:
                Log.i(tag, msg, throwable);
                break;
            case Log.WARN:
                Log.w(tag, msg, throwable);
                break;
            case Log.VERBOSE:
                Log.v(tag, msg, throwable);
                break;
        }

    }

    public void log(int level, String msg) {
        switch (level) {
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;

        }
    }
}
