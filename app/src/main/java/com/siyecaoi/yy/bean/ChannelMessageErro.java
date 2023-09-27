package com.siyecaoi.yy.bean;

/**
 * Date: 2022-07-16
 * Time: 15:27
 */
public class ChannelMessageErro {
    public String msg;
    public String channelId;
    public String userId;

    public ChannelMessageErro(String msg, String channelId, String userId) {
        this.msg = msg;
        this.channelId = channelId;
        this.userId = userId;
    }
}
