package com.jingtaoi.yy.utils;

import android.util.Log;

// http://www.jianshu.com/p/325e8f025c98
public class LogUtils {

    public static final String TAG = "xld.";

    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    public static final int LEVEL = VERBOSE;

    private static boolean IsOpenLog;

    public static void OpenLog(boolean isOpen) {
        if (isOpen) {
            IsOpenLog = true;
        } else {
            IsOpenLog = false;
        }
    }

    public static void v(String target, String msg) {
        if (LEVEL <= VERBOSE && IsOpenLog) {
            Log.v(TAG + target, msg);
        }
    }

    public static void d(String target, String msg) {
        if (LEVEL <= DEBUG && IsOpenLog) {
            Log.d(TAG + target, msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= DEBUG && IsOpenLog) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String target, String msg) {
        if (LEVEL <= INFO && IsOpenLog) {
            Log.i(TAG + target, msg);
        }
    }

    public static void w(String target, String msg) {
        if (LEVEL <= WARN && IsOpenLog) {
            Log.w(TAG + target, msg);
        }
    }

    public static void e(String target, String msg) {

        if (target == null || target.length() == 0 || !IsOpenLog)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.e(TAG + target, msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.e(TAG + target, logContent);
            }
            Log.e(TAG + target, msg);// 打印剩余日志
        }
    }

    public static void e(String msg) {
        if (!IsOpenLog)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.e(TAG, msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.e(TAG, logContent);
            }
            Log.e(TAG, msg);// 打印剩余日志
        }
    }

}
