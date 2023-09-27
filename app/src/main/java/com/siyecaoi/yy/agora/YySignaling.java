package com.siyecaoi.yy.agora;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.bean.CHannelAttrUpdateErro;
import com.siyecaoi.yy.bean.ChannelMessageErro;
import com.siyecaoi.yy.bean.GetAgoraTokenBean;
import com.siyecaoi.yy.control.ChatMessage;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.MessageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.agora.rtm.ChannelAttributeOptions;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.SendMessageOptions;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 信令配置
 */
public class YySignaling {

    Context context;


    RtmClient mRtmClient;

    RtmChannel mRtmChannel;

    private static YySignaling yySignaling;
    String TAG = "YySignaling";

    ArrayList<ChatMessage> fastConShows;

    public YySignaling() {

    }

    public static YySignaling getInstans() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (yySignaling == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (YySignaling.class) {
                //未初始化，则初始instance变量
                if (yySignaling == null) {
                    yySignaling = new YySignaling();
                }
            }
        }
        return yySignaling;
    }

    /**
     * 初始化信令
     *
     * @param context
     */
    public void initSignaling(Context context) {
        this.context = context;
//        m_agoraAPI = AgoraAPIOnlySignal.getInstance(context, Const.AGORA_APP_ID);
//        m_agoraAPI.callbackSet(iCallBack);
        try {
            mRtmClient = RtmClient.createInstance(context, Const.AGORA_APP_ID, new RtmClientListener() {
                @Override
                public void onConnectionStateChanged(int state, int reason) {

                }

                @Override
                public void onMessageReceived(RtmMessage rtmMessage, String s) {

                }

                @Override
                public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {

                }

                @Override
                public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

                }

                @Override
                public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

                }

                @Override
                public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

                }

                @Override
                public void onTokenExpired() {
                    LogUtils.d(TAG, "当前使用的 RTM Token 已超过 24 小时的签发有效期");
                    //刷新RtmToken
                    HashMap<String, Object> map = HttpManager.getInstance().getMap();
                    map.put("uid", Const.User.USER_TOKEN);
                    HttpManager.getInstance().post(Api.getAgoraRtmToken, map, new MyObserver(context) {
                        @Override
                        public void success(String responseString) {
                            GetAgoraTokenBean agoraTokenBean = JSON.parseObject(responseString, GetAgoraTokenBean.class);
                            GetAgoraTokenBean.AgoraToken data = agoraTokenBean.getData();
                            if (data != null) {
                                String token = data.getToken();
                                mRtmClient.renewToken(token, null);
                            }
                        }
                    });
                }

                @Override
                public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

                }
            });

        } catch (Exception e) {
            LogUtils.d(TAG, "创建云信令客户端失败" + e.getMessage());
        }

    }

    /**
     * 获取信令对象
     *
     * @return
     */
    public RtmClient getRtmClient() {
        return this.mRtmClient;
    }

    /**
     * 登录 Agora 信令系统
     *
     * @param account 用户id
     */
    public void loginRtmClient(String account, String agoraRtmToken, String roomId) {
        //强制先退出以前可能没退出的RTM
        mRtmClient.logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                LogUtils.d(TAG, "退出云信令成功");
                login(account, agoraRtmToken, roomId);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                LogUtils.d(TAG, "退出云信令失败"+errorInfo.getErrorDescription());
                login(account, agoraRtmToken, roomId);
            }
        });
//        m_agoraAPI.login2(Const.AGORA_APP_ID, account, "_no_need_token", 0, null, 30, 3);

//        m_agoraAPI.login2(context.getString(R.string.private_app_id), account, token, 0, null, 30, 3);
    }

    private void login(String account, String agoraRtmToken, String roomId) {
        LogUtils.d(TAG, "开始登录云信令");
        mRtmClient.login(agoraRtmToken, account, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                LogUtils.d(TAG, "登录云信令成功");
                //信令加入频道
                joinChannel(roomId);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                LogUtils.d(TAG, "登录云信令失败" + errorInfo.getErrorDescription());
            }
        });
    }

    /**
     * 退出信令系统
     */
    public void logout() {
        mRtmClient.logout(null);
        leaveChannel();
    }

    /**
     * 发送点对点消息
     *
     * @param account 接收方的用户id
     * @param msg     消息内容
     */
    public void sengMessage(String account, String msg) {
//        m_agoraAPI.messageInstantSend(account, 0, msg, "");
        final RtmMessage message = mRtmClient.createMessage();
        message.setText(msg);
        SendMessageOptions option = new SendMessageOptions();
        option.enableOfflineMessaging = true;
        mRtmClient.sendMessageToPeer(account, message, option, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                LogUtils.d(TAG, "发送点对点消息成功");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                LogUtils.d(TAG, "发送点对点消息失败" + errorInfo.getErrorDescription());
            }
        });
    }

//    // 设置对端收到消息回调
//    m_agoraAPI.onMessageInstantReceive(account, uid, msg){
//        // Your code
//    }


    /**
     * 加入频道
     *
     * @param channelName 频道名称
     */
    public void joinChannel(String channelName) {
//        m_agoraAPI.channelJoin(channelName);
        if (mRtmChannel != null) {
            leaveChannel();
        }
        try {
            // 创建 RTM 频道
            mRtmChannel = mRtmClient.createChannel(channelName, new RtmChannelListener() {
                @Override
                public void onMemberCountUpdated(int i) {

                }

                @Override
                public void onAttributesUpdated(List<RtmChannelAttribute> list) {
                    LogUtils.e(LogUtils.TAG, "频道属性发生变化回调");
                    fastConShows = MessageUtils.getInstans().getChat();
                    if (fastConShows != null) {
                        for (RtmChannelAttribute rtmChannelAttribute : list) {
                            String attributeName = rtmChannelAttribute.getKey();
                            String attributeValue = rtmChannelAttribute.getValue();
                            for (ChatMessage chatMessage : fastConShows) {
                                if (chatMessage != null) {
                                    chatMessage.setChannelAttrUpdated(channelName, attributeName, attributeValue, rtmChannelAttribute.getLastUpdateUserId());
                                }
                            }
                        }
                    }


                }

                @Override
                public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember rtmChannelMember) {
                    LogUtils.e(TAG, "对端接收到频道消息" + rtmMessage.getText());
                    fastConShows = MessageUtils.getInstans().getChat();
                    if (fastConShows != null) {
                        for (ChatMessage chatMessage : fastConShows) {
                            if (chatMessage != null) {
                                chatMessage.setMessageShow(rtmChannelMember.getChannelId(), rtmChannelMember.getUserId(), 0, rtmMessage.getText());
                            }
                        }
                    }

                }

                @Override
                public void onImageMessageReceived(RtmImageMessage rtmImageMessage, RtmChannelMember rtmChannelMember) {
                    //该方法已废弃
                }

                @Override
                public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {
                    //该方法已废弃
                }

                @Override
                public void onMemberJoined(RtmChannelMember rtmChannelMember) {
                    //远端用户加入频道回调。
                }

                @Override
                public void onMemberLeft(RtmChannelMember rtmChannelMember) {
                    //

                }
            });
            // 加入 RTM 频道
            mRtmChannel.join(new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void responseInfo) {
                    LogUtils.d(TAG, "加入RTM频道成功");
                    checkErroCHannelAttr();
                    checkChannelMessageErro();
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    LogUtils.d(TAG, "加入RTM频道失败" + errorInfo.getErrorDescription());
                }
            });
        } catch (RuntimeException e) {
            LogUtils.d(TAG, "创建频道失败" + e.getMessage());
        }


    }


    /**
     * 退出频道
     */
    public void leaveChannel() {
//        m_agoraAPI.channelLeave(channelName);
        if (mRtmChannel != null) {
            mRtmChannel.leave(null);
            mRtmChannel.release();
        }
    }

    /**
     * 发送频道消息
     *
     * @param msg 消息内容
     */
    private LinkedList<ChannelMessageErro> mChannelMessageErros = new LinkedList<>();

    public void sendChannelMessage(String msg, String channelId, String userId) {
        if (mRtmChannel == null) {
            LogUtils.d(TAG, "消息发送失败RtmChannel未创建");
            mChannelMessageErros.offer(new ChannelMessageErro(msg, channelId, userId));
            return;
        }

        fastConShows = MessageUtils.getInstans().getChat();
        if (fastConShows != null) {
            for (ChatMessage chatMessage : fastConShows) {
                if (chatMessage != null) {
                    chatMessage.setMessageShow(channelId, userId, (int) System.currentTimeMillis(), msg);
                }
            }
        }
        // 创建消息实例
        RtmMessage message = mRtmClient.createMessage();
        message.setText(msg);

        // 发送频道消息
        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtils.d(TAG, "消息发送成功");

            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                if (errorInfo.getErrorCode() == 1) {
                    mChannelMessageErros.offer(new ChannelMessageErro(msg, channelId, userId));
                }
                LogUtils.d(TAG, "消息发送失败" + mChannelMessageErros.size() + "----" + errorInfo.getErrorCode() + "---" + errorInfo.getErrorDescription());
//                io.reactivex.Observable.just(errorInfo.getErrorDescription())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                android.widget.Toast.makeText(MyApplication.getInstance(), "消息发送失败" + s, Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
        });

    }

    /**
     * 频道属性发生变化
     *
     * @param channelName    频道名
     * @param attributeName  属性名
     * @param attributeValue 属性值
     */
    private LinkedList<CHannelAttrUpdateErro> onFailureCHannelAttrUpdate = new LinkedList<>();

    public void sendCHannelAttrUpdate(String channelName, String attributeName, String attributeValue) {
//        m_agoraAPI.channelSetAttr(s, s1, s2);
        List<RtmChannelAttribute> rtmChannelAttributes = new ArrayList<>();
        rtmChannelAttributes.add(new RtmChannelAttribute(attributeName, attributeValue));
        mRtmClient.addOrUpdateChannelAttributes(channelName, rtmChannelAttributes, new ChannelAttributeOptions(true), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void unused) {
                LogUtils.d(TAG, "频道属性更新成功");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                //未登陆就发消息
                if (errorInfo.getErrorCode() == 102) {
                    onFailureCHannelAttrUpdate.offer(new CHannelAttrUpdateErro(channelName, attributeName, attributeValue));
                }
                LogUtils.d(TAG, "频道属性更新失败" + errorInfo.getErrorCode() + "---" + errorInfo.getErrorDescription());
            }
        });

    }

    /*
    检查是否有未登陆就发送更新频道消息（失败的数据）
     */
    private void checkErroCHannelAttr() {
        CHannelAttrUpdateErro erroAttr = onFailureCHannelAttrUpdate.poll();
        if (erroAttr != null) {
            sendCHannelAttrUpdate(erroAttr.channelName, erroAttr.attributeName, erroAttr.attributeValue);
            checkErroCHannelAttr();
            LogUtils.d(TAG, "checkErroCHannelAttr");
        }
    }

    /*
    检查是否有未登陆就发送频道消息（失败的数据）
     */
    private void checkChannelMessageErro() {
        LogUtils.d(TAG, "checkChannelMessageErro" + mChannelMessageErros.size());
        ChannelMessageErro channelMessageErro = mChannelMessageErros.poll();
        if (channelMessageErro != null) {
            sendChannelMessage(channelMessageErro.msg, channelMessageErro.channelId, channelMessageErro.userId);
            checkChannelMessageErro();
            LogUtils.d(TAG, "checkChannelMessageErro");
        }
    }

//    // 设置对端接收到频道消息回调
//    m_agoraAPI.onMessageChannelReceive(channelID, account, uid, msg) {
//        // Your code
//    }


    //信令回调
//    IAgoraAPI.ICallBack iCallBack = new IAgoraAPI.ICallBack() {
//        @Override
//        public void onReconnecting(int i) {
//
//        }
//
//        @Override
//        public void onReconnected(int i) {
//
//        }
//
//        // 设置登录成功回调
//        @Override
//        public void onLoginSuccess(int i, int i1) {
//            LogUtils.d(TAG, "登录成功回调" + i + " " + i1);
//        }
//
//        @Override
//        public void onLogout(int i) {
//
//        }
//
//        // 设置登录失败回调
//        @Override
//        public void onLoginFailed(int i) {
//            LogUtils.d(TAG, "登录失败回调" + i + " ");
//        }
//
//        // 设置加入频道成功回调
//        @Override
//        public void onChannelJoined(String s) {
//            LogUtils.d(TAG, "加入频道成功" + s);
//        }
//
//        // 设置加入频道失败回调
//        @Override
//        public void onChannelJoinFailed(String s, int i) {
//            LogUtils.d(TAG, "加入频道失败" + s + i);
//        }
//
//        // 设置退出频道回调
//        @Override
//        public void onChannelLeaved(String s, int i) {
//            LogUtils.d(TAG, "退出频道" + s + i);
//        }
//
//        @Override
//        public void onChannelUserJoined(String s, int i) {
//
//        }
//
//        @Override
//        public void onChannelUserLeaved(String s, int i) {
//
//        }
//
//        @Override
//        public void onChannelUserList(String[] strings, int[] ints) {
//
//        }
//
//        @Override
//        public void onChannelQueryUserNumResult(String s, int i, int i1) {
//
//        }
//
//        @Override
//        public void onChannelQueryUserIsIn(String s, String s1, int i) {
//
//        }
//
//        @Override
//        public void onChannelAttrUpdated(String s, String s1, String s2, String s3) {
//            LogUtils.e(LogUtils.TAG, "频道属性发生变化回调" + s + s1 + s2);
//            LogUtils.e(LogUtils.TAG, "频道属性状态" + s3);
//            fastConShows = MessageUtils.getInstans().getChat();
//            if (s3.equals("update")) {
//                if (fastConShows != null) {
//                    if (!StringUtils.isEmpty(s2)) {
//                        for (ChatMessage chatMessage : fastConShows) {
//                            if (chatMessage != null) {
//                                chatMessage.setChannelAttrUpdated(s, s1, s2, s3);
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
//
//        @Override
//        public void onInviteReceived(String s, String s1, int i, String s2) {
//
//        }
//
//        @Override
//        public void onInviteReceivedByPeer(String s, String s1, int i) {
//
//        }
//
//        @Override
//        public void onInviteAcceptedByPeer(String s, String s1, int i, String s2) {
//
//        }
//
//        @Override
//        public void onInviteRefusedByPeer(String s, String s1, int i, String s2) {
//
//        }
//
//        @Override
//        public void onInviteFailed(String s, String s1, int i, int i1, String s2) {
//
//        }
//
//        @Override
//        public void onInviteEndByPeer(String s, String s1, int i, String s2) {
//
//        }
//
//        @Override
//        public void onInviteEndByMyself(String s, String s1, int i) {
//
//        }
//
//        @Override
//        public void onInviteMsg(String s, String s1, int i, String s2, String s3, String s4) {
//
//        }
//
//        // 设置消息发送失败回调
//        @SuppressLint("CheckResult")
//        @Override
//        public void onMessageSendError(String s, int i) {
//            LogUtils.d(TAG, "消息发送失败" + s + i);
//            io.reactivex.Observable.just(s)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<String>() {
//                        @Override
//                        public void accept(String s) throws Exception {
//                            android.widget.Toast.makeText(MyApplication.getInstance(), "消息发送失败" + s, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//        }
//
//        @Override
//        public void onMessageSendProgress(String s, String s1, String s2, String s3) {
//
//        }
//
//        // 设置消息发送成功回调
//        @Override
//        public void onMessageSendSuccess(String s) {
//            LogUtils.d(TAG, "消息发送成功" + s);
//        }
//
//        @Override
//        public void onMessageAppReceived(String s) {
//
//        }
//
//        @Override
//        public void onMessageInstantReceive(String s, int i, String s1) {
//
//        }
//
//        // 设置对端接收到频道消息回调
//        @Override
//        public void onMessageChannelReceive(String s, String s1, int i, String s2) {
//            LogUtils.e(TAG, "对端接收到频道消息" + s + "  " + s1 + " " + i + " " + s2);
//            fastConShows = MessageUtils.getInstans().getChat();
//            if (fastConShows != null) {
//                for (ChatMessage chatMessage : fastConShows) {
//                    if (chatMessage != null) {
//                        chatMessage.setMessageShow(s, s1, i, s2);
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onLog(String s) {
//
//        }
//
//        @Override
//        public void onInvokeRet(String s, String s1, String s2) {
//
//        }
//
//        @Override
//        public void onMsg(String s, String s1, String s2) {
//
//        }
//
//        @Override
//        public void onUserAttrResult(String s, String s1, String s2) {
//
//        }
//
//        @Override
//        public void onUserAttrAllResult(String s, String s1) {
//
//        }
//
//        @Override
//        public void onError(String s, int i, String s1) {
//
//        }
//
//        @Override
//        public void onQueryUserStatusResult(String s, String s1) {
//
//        }
//
//        @Override
//        public void onDbg(String s, byte[] bytes) {
//
//        }
//
//        @Override
//        public void onBCCall_result(String s, String s1, String s2) {
//
//        }
//    };

}
