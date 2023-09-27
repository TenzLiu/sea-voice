package com.siyecaoi.yy.agora;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import java.io.File;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

/**
 * 初始化直播模块
 */
public class WorkerThread extends Thread {


    private final Context mContext;

    private static final int ACTION_WORKER_THREAD_QUIT = 0X1010; // quit this thread

    private static final int ACTION_WORKER_JOIN_CHANNEL = 0X2010;

    private static final int ACTION_WORKER_LEAVE_CHANNEL = 0X2011;

    private static final int ACTION_WORKER_CONFIG_ENGINE = 0X2012;

    private static final int ACTION_WORKER_MUSIC_PLAY = 0X2013;//播放音乐

    private static final int ACTION_WORKER_MUSIC_PAUSE = 0X2014;//暂停播放音乐

    private static final int ACTION_WORKER_MUSIC_REPLAY = 0X2015;//继续播放音乐

    private static final int ACTION_WORKER_MUSIC_STOP = 0X2016;//停止播放音乐

    private static final int ACTION_WORKER_MUSIC_NOW = 0X2017;//获取音乐播放进度

    private static final int ACTION_WORKER_MUSIC_VOLUME = 0X2018;//设置音量

    private static final int ACTION_WORKER_MUSIC_NOW_SET = 0X2019;//设置音乐播放进度
    private static final int ACTION_WORKER_NEW_TOKEN = 0X2020;//设置新的token

    private String TAG = "agora";

    private static final class WorkerThreadHandler extends Handler {

        private WorkerThread mWorkerThread;

        WorkerThreadHandler(WorkerThread thread) {
            this.mWorkerThread = thread;
        }

        public void release() {
            mWorkerThread = null;
        }

        @Override
        public void handleMessage(Message msg) {
            if (this.mWorkerThread == null) {
                LogUtils.d("agora", "handler is already released! " + msg.what);
                return;
            }

            switch (msg.what) {
                case ACTION_WORKER_THREAD_QUIT:
                    mWorkerThread.exit();
                    break;
                case ACTION_WORKER_JOIN_CHANNEL:
                    String[] data = (String[]) msg.obj;
                    mWorkerThread.joinChannel(data[0], msg.arg1, data[1]);
                    break;
                case ACTION_WORKER_LEAVE_CHANNEL:
                    String channel = (String) msg.obj;
                    mWorkerThread.leaveChannel(channel);
                    break;
                case ACTION_WORKER_NEW_TOKEN:
                    String rtcToken = (String) msg.obj;
                    mWorkerThread.renewToken(rtcToken);
                    break;
                case ACTION_WORKER_CONFIG_ENGINE:
                    Object[] configData = (Object[]) msg.obj;
                    mWorkerThread.configEngine((int) configData[0]);
                    break;
                case ACTION_WORKER_MUSIC_PLAY:
                    String filePath = (String) msg.obj;
                    mWorkerThread.musicPlay(filePath);
                    break;
                case ACTION_WORKER_MUSIC_PAUSE:
                    mWorkerThread.musicPause();
                    break;
                case ACTION_WORKER_MUSIC_REPLAY:
                    mWorkerThread.musicReplay();
                    break;
                case ACTION_WORKER_MUSIC_STOP:
                    mWorkerThread.musicStop();
                    break;
                case ACTION_WORKER_MUSIC_NOW:
                    mWorkerThread.getMusicNow();
                    break;
                case ACTION_WORKER_MUSIC_VOLUME:
                    int volume = (int) msg.obj;
                    mWorkerThread.musicVolumeSet(volume);
                    break;
                case ACTION_WORKER_MUSIC_NOW_SET:
                    int nowSet = (int) msg.obj;
                    mWorkerThread.musicSetTime(nowSet);
                    break;
            }
        }
    }

    private WorkerThreadHandler mWorkerHandler;

    private boolean mReady;

    public final void waitForReady() {
        while (!mReady) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogUtils.e(TAG, "wait for " + WorkerThread.class.getSimpleName());
        }
    }

    @Override
    public void run() {
        LogUtils.e(TAG, "start to run");
        Looper.prepare();

        mWorkerHandler = new WorkerThreadHandler(this);

        ensureRtcEngineReadyLock();

        mReady = true;

        // enter thread looper
        Looper.loop();
    }

    private RtcEngine mRtcEngine;

    /**
     * 加入频道
     *
     * @param channel
     * @param uid
     * @param agoraToken
     */
    public final void joinChannel(final String channel, int uid, String agoraToken) {
        if (Thread.currentThread() != this) {
            LogUtils.e(TAG, "joinChannel() - worker thread asynchronously " + channel + " " + uid);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_JOIN_CHANNEL;
            envelop.obj = new String[]{channel, agoraToken};
            envelop.arg1 = uid;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        mRtcEngine.joinChannel(agoraToken, channel, "OpenVCall", uid);

        mEngineConfig.mChannel = channel;

        LogUtils.e(TAG, "joinChannel " + channel + " " + uid);
    }

    /**
     * 离开频道
     *
     * @param channel
     */
    public final void leaveChannel(String channel) {
        LogUtils.e(TAG, "leaveChannel " + channel);
        if (Thread.currentThread() != this) {
            LogUtils.e(TAG, "leaveChannel() - worker thread asynchronously " + channel);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_LEAVE_CHANNEL;
            envelop.obj = channel;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }
        mEngineConfig.reset();
    }

    /**
     * 设置新的token
     *
     * @param rtcToken
     */
    public final void renewToken(String rtcToken) {
        LogUtils.e(TAG, "renewToken " + rtcToken);
        if (Thread.currentThread() != this) {
            LogUtils.e(TAG, "renewToken() - worker thread asynchronously " + rtcToken);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_NEW_TOKEN;
            envelop.obj = rtcToken;
            mWorkerHandler.sendMessage(envelop);
            return;
        }
        ensureRtcEngineReadyLock();
        if (mRtcEngine != null) {
            mRtcEngine.renewToken(rtcToken);
        }
    }

    /**
     * 开始播放音频文件
     *
     * @param filePath
     */
    public final void musicPlay(String filePath) {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_MUSIC_PLAY;
            envelop.obj = filePath;
            mWorkerHandler.sendMessage(envelop);
            return;
        }
        ensureRtcEngineReadyLock();
        mRtcEngine.startAudioMixing(filePath, false, false, 1);
        LogUtils.e("msg", "开始播放音乐");
    }

    public final void musicPause() {
        if (Thread.currentThread() != this) {
            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_MUSIC_PAUSE);
            return;
        }
        ensureRtcEngineReadyLock();
        mRtcEngine.pauseAudioMixing();
        LogUtils.e("msg", "暂停播放音乐");
    }

    public final void musicReplay() {
        if (Thread.currentThread() != this) {
            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_MUSIC_REPLAY);
            return;
        }
        ensureRtcEngineReadyLock();
        mRtcEngine.resumeAudioMixing();
        LogUtils.e("msg", "继续播放音乐");
    }

    public final void musicStop() {
        if (Thread.currentThread() != this) {
            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_MUSIC_STOP);
            return;
        }
        ensureRtcEngineReadyLock();
        mRtcEngine.stopAudioMixing();
        LogUtils.e("msg", "停止播放音乐");
    }

    /**
     * 获取播放进度
     *
     * @return
     */
    public final int getMusicNow() {
        int positionNow = 0;
//        if (Thread.currentThread() != this) {
//            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_MUSIC_NOW);
//        } else {
        ensureRtcEngineReadyLock();
        positionNow = mRtcEngine.getAudioMixingCurrentPosition();
//        }
        LogUtils.e("msg", "播放进度" + positionNow);
        return positionNow;
    }


    public final void musicVolumeSet(int volume) {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_MUSIC_VOLUME;
            envelop.obj = volume;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        mRtcEngine.adjustAudioMixingVolume(volume);
        LogUtils.e("msg", "设置音量" + volume);
    }

    public final void musicSetTime(int time) {
        if (Thread.currentThread() != this) {
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_MUSIC_NOW_SET;
            envelop.obj = time;
            mWorkerHandler.sendMessage(envelop);
            return;
        }
        ensureRtcEngineReadyLock();
        mRtcEngine.setAudioMixingPosition(time);
        LogUtils.e("msg", "设置播放位置" + time);
    }


    private EngineConfig mEngineConfig;


    public final EngineConfig getEngineConfig() {
        return mEngineConfig;
    }

    private final MyEngineEventHandler mEngineEventHandler;

    //设置用户角色属性  1主播  2观众
    public final void configEngine(int cRole) {
        if (Thread.currentThread() != this) {
//            LogUtils.e(TAG, "configEngine() - worker thread asynchronously " + cRole);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_CONFIG_ENGINE;
            envelop.obj = new Object[]{cRole};
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        mEngineConfig.mClientRole = cRole;

        mRtcEngine.setClientRole(cRole);

        LogUtils.d(TAG, "configEngine " + cRole);
    }

    public static String getDeviceID(Context context) {
        // XXX according to the API docs, this value may change after factory reset
        // use Android id as device id
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private RtcEngine ensureRtcEngineReadyLock() {
        if (mRtcEngine == null) {
            String appId = Const.AGORA_APP_ID;
            if (TextUtils.isEmpty(appId)) {
                throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
            }
            try {
                mRtcEngine = RtcEngine.create(mContext, appId, mEngineEventHandler.mRtcEventHandler);
            } catch (Exception e) {
                LogUtils.d(TAG, Log.getStackTraceString(e));
                throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
            }
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableAudioVolumeIndication(200, 3, true); // 200 ms
        }
        return mRtcEngine;
    }

    public MyEngineEventHandler eventHandler() {
        return mEngineEventHandler;
    }

    public RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    /**
     * call this method to exit
     * should ONLY call this method when this thread is running
     */
    public final void exit() {
        if (Thread.currentThread() != this) {
            LogUtils.d(TAG, "exit() - exit app thread asynchronously");
            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_THREAD_QUIT);
            return;
        }

        mReady = false;

        // TODO should remove all pending(read) messages

        LogUtils.d(TAG, "exit() > start");

        // exit thread looper
        Looper.myLooper().quit();

        mWorkerHandler.release();

        LogUtils.d(TAG, "exit() > end");
    }

    public WorkerThread(Context context) {
        this.mContext = context;

        this.mEngineConfig = new EngineConfig();
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int userId = (int) SharedPreferenceUtils.get(context, Const.User.USER_TOKEN, 0);
        this.mEngineConfig.mUid = userId;

        this.mEngineEventHandler = new MyEngineEventHandler(mContext, this.mEngineConfig);
    }
}
