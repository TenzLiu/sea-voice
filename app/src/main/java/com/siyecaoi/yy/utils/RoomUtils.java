package com.siyecaoi.yy.utils;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.model.CustomOneModel;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;

public class RoomUtils {
    public static void sendPrivateGiftTips(String userId,String content){
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                userId);

        CustomOneModel customOneModel = new CustomOneModel();
        customOneModel.setShowMsg(content);
        customOneModel.setState(Const.IntShow.THREE);
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("礼物");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        String msgShow = JSON.toJSONString(customOneModel);
        //向 TIMMessage 中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgShow.getBytes());      //自定义 byte[]
        elem.setDesc("[礼物]"); //自定义描述信息
        //将 elem 添加到消息
        if (msg.addElement(elem) != 0) {
            LogUtils.d(LogUtils.TAG, "addElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() { //发送消息回调
            @Override
            public void onError(int code, String desc) { //发送消息失败
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
                LogUtils.e(LogUtils.TAG, "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) { //发送消息成功
                LogUtils.e(LogUtils.TAG, "SendMsg ok");
            }
        });

    }

    public static void sendPrivateCoinTips(String userId, String gold){
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                userId);

        CustomOneModel customOneModel = new CustomOneModel();
        customOneModel.setShowMsg("\uD83E\uDDE7送给你"+gold+"个浪花");
        customOneModel.setState(Const.IntShow.THREE);
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("浪花");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        String msgShow = JSON.toJSONString(customOneModel);
        //向 TIMMessage 中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgShow.getBytes());      //自定义 byte[]
        elem.setDesc("[浪花]"); //自定义描述信息
        //将 elem 添加到消息
        if (msg.addElement(elem) != 0) {
            LogUtils.d(LogUtils.TAG, "addElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() { //发送消息回调
            @Override
            public void onError(int code, String desc) { //发送消息失败
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
                LogUtils.e(LogUtils.TAG, "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) { //发送消息成功
                LogUtils.e(LogUtils.TAG, "SendMsg ok");
            }
        });

    }
}
