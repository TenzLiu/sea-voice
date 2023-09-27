package com.siyecaoi.yy.control;

import com.tencent.imsdk.TIMMessage;

public interface ChatRoomMessage {

    /**
     * 广播交友消息回调
     *
     * @param msgs
     */
    void onNewMessage(TIMMessage timMsg);
}
