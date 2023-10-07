package com.siyecaoi.yy.base;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.multidex.MultiDex;

import com.opensource.svgaplayer.SVGAParser;
import com.siyecaoi.yy.BuildConfig;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.control.ChatRoomMessage;
import com.siyecaoi.yy.dialog.GiftShowDialogFragment;
import com.siyecaoi.yy.ui.message.ChatActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.ImageLoaderConfig;
import com.siyecaoi.yy.utils.UniException;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.siyecaoi.yy.agora.YySignaling;
import com.siyecaoi.yy.agora.WorkerThread;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;

import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.sinata.xldutils.BaseApplication;
import cn.sinata.xldutils.utils.Toast;


/**
 * Created by Administrator on 2018/1/17.
 */

public class MyApplication extends BaseApplication implements Thread.UncaughtExceptionHandler {

    private static MyApplication appContext;

    public static double lat = 0.0;
    public static double lon = 0.0;

    //设置debug模式
    boolean isDebug = BuildConfig.DEBUG;
//    boolean isDebug = true;

    public static MyApplication getInstance() {
        return appContext;
    }

    @Override
    protected String setSharedPreferencesName() {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        LitePal.initialize(this);
        //开启log打印
        LogUtils.OpenLog(true);

        CrashReport.initCrashReport(getApplicationContext(), Const.BUGLY_ID, true);

        //如果是主进程
        if (getCurProcessName(this).equals(getApplicationInfo().packageName)) {

            setTuiKit();
            setFresco();
            initLocation();
            setAgora();
            setSVGA();
            setJpush();

//            setException();
        }
    }

    private void setJpush() {

        JPushInterface.setDebugMode(isDebug);
        JPushInterface.init(this);
    }

    private void setTuiKit() {

        // 配置 Config，请按需配置
        TUIKitConfigs configs = TUIKit.getConfigs();
        configs.setSdkConfig(new TIMSdkConfig(Const.TUIKIT_KEY));
        configs.setCustomFaceConfig(new CustomFaceConfig());
        GeneralConfig generalConfig = new GeneralConfig();
        generalConfig.enableLogPrint(true);
        configs.setGeneralConfig(generalConfig);

        TUIKit.init(this, Const.TUIKIT_KEY, configs);

//        TUIKit.init(this, Const.TUIKIT_KEY, BaseUIKitConfigs.getDefaultConfigs());
////        // 设置离线推送监听器
        TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
            @Override
            public void handleNotification(TIMOfflinePushNotification notification) {
                LogUtils.d("MyApplication", "recv offline push");

                // 这里的 doNotify 是 ImSDK 内置的通知栏提醒，应用也可以选择自己利用回调参数 notification 来构造自己的通知栏提醒
                notification.doNotify(appContext, R.mipmap.ic_launcher);
            }
        });

        //替换成IMEventListener，在替换成IMEventListener实现相关事件回调处理
        TUIKit.setIMEventListener(new IMEventListener() {
            /**
             * 被踢下线时回调
             */
            @Override
            public void onForceOffline() {
                super.onForceOffline();
                Toast.create(MyApplication.getInstance()).show("您已在其他设备登录");
                BroadcastManager.getInstance(appContext).sendBroadcast(Const.BroadCast.EXIT);
            }

            @Override
            public void onNewMessages(List<TIMMessage> msgs) {
                super.onNewMessages(msgs);
                LogUtils.i("收到信消息"+msgs.size());
                for (TIMMessage timMsg : msgs) {
                    if (timMsg.getElement(0) instanceof TIMCustomElem) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(((TIMCustomElem) timMsg.getElement(0)).getData(), "UTF-8"));
                            LogUtils.i("接收腾讯消息",jsonObject.toString());
                            int state = jsonObject.optInt("state");
                            if (state == 2) {
                                ChatActivity activity = ActivityCollector.getActivity(ChatActivity.class);
                                if (activity != null && !activity.isFinishing()) {
                                    String showUrl = jsonObject.getString("showUrl");
                                    String showMsg = jsonObject.getString("showMsg");
                                    GiftShowDialogFragment giftShowDialogFragment = new GiftShowDialogFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Const.ShowIntent.IMG, showUrl);
                                    bundle.putString(Const.ShowIntent.NUMBER, showMsg);
                                    giftShowDialogFragment.setArguments(bundle);
                                    giftShowDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                                    giftShowDialogFragment.show(activity.getSupportFragmentManager(), "giftShowDialogFragment");
                                }
                            } else if (state == 3 || state == 4) {
                                if (chatRoomMessageList != null) {
                                    for (ChatRoomMessage chatRoomMessage : chatRoomMessageList) {
                                        chatRoomMessage.onNewMessage(timMsg);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LogUtils.e("新消息转换出错");
                            e.printStackTrace();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(new String(((TIMCustomElem) timMsg.getElement(0)).getData(), "UTF-8"));
                            LogUtils.i("接收腾讯消息1",jsonObject.toString());
                            int code = jsonObject.optInt("code");
                            if (code == 119 || code == 121 || code == 122 || code == 1001) {
                                if (chatRoomMessage != null) {
                                    chatRoomMessage.onNewMessage(timMsg);
                                }
                            }
                        } catch (Exception e) {
                            LogUtils.e("新消息转换出错");
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    /**
     * 房间全频道消息回调
     */
    ChatRoomMessage chatRoomMessage;

    public ChatRoomMessage getChatRoomMessage() {
        return chatRoomMessage;
    }

    public void setChatRoomMessage(ChatRoomMessage chatRoomMessage) {
        this.chatRoomMessage = chatRoomMessage;
    }

    /**
     * 广播交友消息回调
     */
    List<ChatRoomMessage> chatRoomMessageList;

    public List<ChatRoomMessage> getChatRoomMessageList() {
        return chatRoomMessageList;
    }


    public void addChatRoomMessage(ChatRoomMessage chatRoomMessage) {
        if (chatRoomMessageList == null) {
            chatRoomMessageList = new ArrayList<>();
        }
        if (chatRoomMessage != null && !chatRoomMessageList.contains(chatRoomMessage)) {
            chatRoomMessageList.add(chatRoomMessage);
        }
    }

    public void delChatRoomMessage(ChatRoomMessage chatRoomMessage) {
        if (chatRoomMessageList == null) {
            return;
        }
        if (chatRoomMessage != null && chatRoomMessageList.contains(chatRoomMessage)) {
            chatRoomMessageList.remove(chatRoomMessage);
        }
    }

    private void setException() {
        UniException.getInstance().init();
    }

    private void setSVGA() {
        SVGAParser.Companion.shareParser().init(this);
        File cacheDir = new File(getApplicationContext().getCacheDir(), "http");
//        val cacheDir = File(context.applicationContext.cacheDir, "http")
        try {
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    private WorkerThread mWorkerThread;

    //配置声网相关信息
    private void setAgora() {
        //初始化声网直播
        initWorkerThread();
        //初始化声网信令
        YySignaling.getInstans().initSignaling(this);
    }

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    private void initLocation() {

    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        if (list != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
                if (runningAppProcessInfo.pid == pid) {
                    return runningAppProcessInfo.processName;
                }
            }
        }
        return "";
    }


    private void setFresco() {
//        Fresco.initialize(this);
        ImagePipelineConfig config = ImageLoaderConfig.getImagePipelineConfig(this);
        Fresco.initialize(this, config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }
}
