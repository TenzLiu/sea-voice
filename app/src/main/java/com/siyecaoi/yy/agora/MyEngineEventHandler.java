package com.siyecaoi.yy.agora;

import android.content.Context;
import com.siyecaoi.yy.utils.LogUtils;


import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class MyEngineEventHandler {
    public MyEngineEventHandler(Context ctx, EngineConfig config) {
        this.mContext = ctx;
        this.mConfig = config;
    }

    private final EngineConfig mConfig;

    private final Context mContext;

    Iterator<AGEventHandler> it;

    private final ConcurrentHashMap<AGEventHandler, Integer> mEventHandlerList = new ConcurrentHashMap<>();

    public void addEventHandler(AGEventHandler handler) {
        this.mEventHandlerList.put(handler, 0);
    }

    public void removeEventHandler(AGEventHandler handler) {
        this.mEventHandlerList.remove(handler);
    }

    final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        /**
         * 远端用户/主播加入当前频道回调。直播模式下，该回调提示有主播加入了频道，并返回该主播的用户 ID。
         * 如果在加入之前，已经有主播在频道中了，新加入的用户也会收到已有主播加入频道的回调。
         * Agora 建议连麦主播不超过 17 人。
         * @param uid
         * @param elapsed
         */
        @Override
        public void onUserJoined(int uid, int elapsed) {
            LogUtils.d(LogUtils.TAG, "远端用户/主播加入当前频道" + uid);
            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_USER_JOIN, uid, elapsed);
            }
        }

        /**
         * 提示有远端用户/主播离开了频道（或掉线）。用户离开频道有两个原因，即正常离开和超时掉线：
         * @param uid
         * @param reason
         */
        @Override
        public void onUserOffline(int uid, int reason) {
            LogUtils.d("agora", "onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason);

            // FIXME this callback may return times
            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onUserOffline(uid, reason);
            }
        }


        /**
         * 本地音频状态发生改变回调。
         * @param state
         * @param error
         */
        @Override
        public void onLocalAudioStateChanged(int state, int error) {
            super.onLocalAudioStateChanged(state, error);
            LogUtils.d(LogUtils.TAG, "麦克风状态已改变" + state);
            it = mEventHandlerList.keySet().iterator();
            //LOCAL_AUDIO_STREAM_STATE_STOPPED(0)：本地音频默认初始状态。
            //LOCAL_AUDIO_STREAM_STATE_CAPTURING(1)：本地音频采集设备启动成功。
            //LOCAL_AUDIO_STREAM_STATE_ENCODING(2)：本地音频首帧编码成功。
            //LOCAL_AUDIO_STREAM_STATE_FAILED(3)：本地音频启动失败
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_MIC, state);
            }
        }

        /**
         * 提示频道内谁正在说话以及说话者音量的回调
         * @param speakers
         * @param totalVolume
         */
        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
            super.onAudioVolumeIndication(speakers, totalVolume);
            LogUtils.d(LogUtils.TAG, "频道内谁正在说话以及说话者音量的回调" + totalVolume);
            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_VOLUMEINDICATION, speakers, totalVolume);
            }
        }

        /**
         * 提示有其他用户将他的音频流静音/取消静音。
         * @param uid
         * @param muted true: 该用户已静音音频 false: 该用户已取消音频静音
         */
        @Override
        public void onUserMuteAudio(int uid, boolean muted) {
            super.onUserMuteAudio(uid, muted);
            LogUtils.d(LogUtils.TAG, " 该用户已静音音频" + uid + " " + muted);
            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_USERMUTEAUDIO, uid, muted);
            }
        }

        /**
         * 语音路由已变更回调。
         * @param routing
         */
        @Override
        public void onAudioRouteChanged(int routing) {
            super.onAudioRouteChanged(routing);

        }

        /**
         * 本地音乐文件播放已结束回调
         */
        @Override
        public void onAudioMixingFinished() {
            super.onAudioMixingFinished();
            LogUtils.e(LogUtils.TAG, "音乐播放结束");
            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_AUDIOFINISH);
            }
        }

        /**
         * 本地音效文件播放已结束回调。
         * @param soundId
         */
        @Override
        public void onAudioEffectFinished(int soundId) {
            super.onAudioEffectFinished(soundId);
        }

        /**
         * 离开频道回调。
         * @param stats
         */
        @Override
        public void onLeaveChannel(RtcStats stats) {
            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onLeaveChannel(stats);
            }
        }

        /**
         * 报告本地用户的网络质量。当你调用 enableLastmileTest 之后，该回调函数每 2 秒触发一次。
         * @param quality
         */
        @Override
        public void onLastmileQuality(int quality) {
            LogUtils.d("agora", "onLastmileQuality " + quality);
        }

        @Override
        public void onError(int error) {
            LogUtils.d("agora", "onError " + error);

            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR, error, RtcEngine.getErrorDescription(error));
            }
        }

//       //远端音频质量回调
//        @Override
//        public void onAudioQuality(int uid, int quality, short delay, short lost) {
//            it = mEventHandlerList.keySet().iterator();
//            while (it.hasNext()) {
//                AGEventHandler handler = it.next();
//                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_AUDIO_QUALITY, uid, quality, delay, lost);
//            }
//        }

        /**
         * 网络连接中断，且 SDK 未在 10 秒内连接服务器回调。
         */
        @Override
        public void onConnectionLost() {
            LogUtils.d("agora", "onConnectionLost");

            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_LOST_ERROR);
            }
        }

        /**
         * 网络连接状态已改变回调。
         * @param state
         * @param reason
         */
        @Override
        public void onConnectionStateChanged(int state, int reason) {
            super.onConnectionStateChanged(state, reason);
            LogUtils.d("agora", "onConnectionStateChanged---" + state);
            //CONNECTION_STATE_DISCONNECTED(1)：网络连接断开。
            //CONNECTION_STATE_CONNECTING(2)：建立网络连接中。
            //CONNECTION_STATE_CONNECTED(3)：网络已连接。
            //CONNECTION_STATE_RECONNECTING(4)：重新建立网络连接中。
            //CONNECTION_STATE_FAILED(5)：网络连接失败。
            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_APP_NET_CHANGED, state);
            }
        }

        /**
         * 表示客户端已经登入服务器
         * @param channel
         * @param uid
         * @param elapsed
         */
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            LogUtils.d("agora", "onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);

            mConfig.mUid = uid;

            it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }

        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
            LogUtils.d("agora", "onRejoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);
        }

        public void onWarning(int warn) {
            LogUtils.d("agora", "onWarning " + warn);
        }

        /**
         * Token过期
         */
        @Override
        public void onRequestToken() {
            super.onRequestToken();
            LogUtils.d("agora", "Token过期");
            it = mEventHandlerList.keySet().iterator();
            //todo 应该放onTokenPrivilegeWillExpire最好
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onTokenPrivilegeWillExpire("");
            }
        }
        /**
         * 声网即将过期token，提前30秒触发
         */
        @Override
        public void onTokenPrivilegeWillExpire(String token) {
            super.onTokenPrivilegeWillExpire(token);
            LogUtils.d("agora", "Token即将过期");

        }
    };

}
