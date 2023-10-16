package com.jingtaoi.yy.control;

public interface ChatMessage {
    /**
     * 频道消息回调
     * @param channelID 频道名
     * @param account   客户端定义的用户账号
     * @param uid       固定填 0
     * @param msg       消息正文
     */
    void setMessageShow(String channelID, String account, int uid, String msg);


    /**
     * 频道属性发生变化回调
     *
     * @param s  频道名
     * @param s1 属性名
     * @param s2 属性值
     * @param s3 变化类型:
     */
    void setChannelAttrUpdated(String s, String s1, String s2, String s3);

}


