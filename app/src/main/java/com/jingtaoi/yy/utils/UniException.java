package com.jingtaoi.yy.utils;

import android.os.Looper;
import android.widget.Toast;


import com.jingtaoi.yy.base.MyApplication;

import java.util.HashMap;
import java.util.Map;

public class UniException implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "UniException";

    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // UniException实例
    private static UniException INSTANCE = new UniException();

    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    /**
     * 保证只有一个实例
     */
    private UniException() {
    }

    /**
     * 获取UniException实例 ,单例模式
     */
    public static UniException getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UniException();
            INSTANCE.init();
        }
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init() {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该本类为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LogUtils.e("error for app: ", e.getMessage());
            }
            // 退出程序
            ActivityCollector.getActivityCollector().AppExit();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(MyApplication.getInstance(), "抱歉,程序出现异常", Toast.LENGTH_LONG).show();

                Looper.loop();
            }
        }.start();
//        // 收集设备参数信息
//        collectDeviceInfo(AppOS.appOS);
//        // 保存日志文件
//        try {
//            File file = new File(INI.INI_BUS_LOG_FLODER);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            FileOutputStream fout;
//            fout = new FileOutputStream(new File(INI.INI_BUS_LOG_FLODER + DateUtil.getYourTime("yyyyMMdd") + ".log"),
//                    true);
//            fout.write((">>>>时间：" + DateUtil.getYourTime(DateUtil.yyyyMMdd_HH_mm_ss) + "\r\n").getBytes("utf-8"));
//            fout.write(("信息：" + ex.getMessage() + "\r\n").getBytes("utf-8"));
//            for (int i = 0; i < ex.getStackTrace().length; i++) {
//                fout.write(("****StackTrace" + i + "\r\n").getBytes("utf-8"));
//                fout.write(("行数：" + ex.getStackTrace()[i].getLineNumber() + "\r\n").getBytes("utf-8"));
//                fout.write(("类名：" + ex.getStackTrace()[i].getClassName() + "\r\n").getBytes("utf-8"));
//                fout.write(("文件：" + ex.getStackTrace()[i].getFileName() + "\r\n").getBytes("utf-8"));
//                fout.write(("方法：" + ex.getStackTrace()[i].getMethodName() + "\r\n\r\n").getBytes("utf-8"));
//            }
//            fout.write(
//                    "--------------------------------\r\n--------------------------------\r\n--------------------------------\r\n"
//                            .getBytes("utf-8"));
//            fout.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return true;
    }
}

//    /**
//     * 收集设备参数信息
//     *
//     * @param ctx
//     */
//    public void collectDeviceInfo(Context ctx) {
//        try {
//            PackageManager pm = ctx.getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
//            if (pi != null) {
//                String versionName = pi.versionName == null ? "null" : pi.versionName;
//                String versionCode = pi.versionCode + "";
//                infos.put("versionName", versionName);
//                infos.put("versionCode", versionCode);
//            }
//        } catch (NameNotFoundException e) {
//            LogUtil.e("an error occured when collect package info" + e.getMessage());
//        }
//        Field[] fields = Build.class.getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true);
//                infos.put(field.getName(), field.get(null).toString());
//                LogUtil.e(field.getName() + " : " + field.get(null));
//            } catch (Exception e) {
//                LogUtil.e("an error occured when collect crash info" + e.getMessage());
//            }
//        }
//    }
