package com.jingtaoi.yy.agora;

import io.agora.rtc.IRtcEngineEventHandler;

public interface AGEventHandler {
    /**
     * 表示客户端已经登入服务器，且分配了频道 ID 和用户 ID。
     *
     * @param channel
     * @param uid
     * @param elapsed
     */
    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    /**
     * 声网rtcToken即将过期
     * @param rtcToken
     */
    void onTokenPrivilegeWillExpire(String rtcToken);

    /**
     * 离开频道回调。
     *
     * @param stats
     */
    void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats);

    /**
     * 提示有远端用户/主播离开了频道（或掉线）。用户离开频道有两个原因，即正常离开和超时掉线：
     *
     * @param uid    主播 ID
     * @param reason 离线原因
     */
    void onUserOffline(int uid, int reason);

    /**
     * 回调Callback
     *
     * @param type
     * @param data
     */
    void onExtraCallback(int type, Object... data);

    int EVENT_TYPE_ON_USER_AUDIO_MUTED = 7;

    int EVENT_TYPE_ON_SPEAKER_STATS = 8;//提示频道内谁正在说话以及说话者音量的回调

    int EVENT_TYPE_ON_AGORA_MEDIA_ERROR = 9;//发生错误回调。

    int EVENT_TYPE_ON_AUDIO_QUALITY = 10;//远端音频质量回调。在通话中，该回调方法每两秒触发一次，报告当前通话的音频质量。默认启用。

    int EVENT_TYPE_ON_LOST_ERROR = 12;//网络连接中断，且 SDK 未在 10 秒内连接服务器回调。(需要 leaveChannel后可再次进入)

    //该回调在网络连接状态发生改变的时候触发，并告知用户当前的网络连接状态，和引起网络状态改变的原因。
    int EVENT_TYPE_ON_APP_NET_CHANGED = 13;

    int EVENT_TYPE_ON_MIC = 14;//麦克风状态已改变回调

    int EVENT_TYPE_ON_USER_JOIN = 15;//远端用户/主播加入当前频道回调。

    int EVENT_TYPE_VOLUMEINDICATION = 16;//提示频道内谁正在说话以及说话者音量的回调

    int EVENT_TYPE_USERMUTEAUDIO = 17;//远端用户静音回调。

    int EVENT_TYPE_AUDIOFINISH = 18;//音乐文件播放完毕

}
