package com.jingtaoi.yy.model;

/**
 * 自定义分享消息，自定义礼物消息
 */
public class CustomOneModel {
    private String showMsg;//显示内容
    private String showImg;//展示图片
    private String showUrl;//链接或封面图片
    private String roomId;//房间id
    private int state;//1分享消息  2礼物消息

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getShowMsg() {
        return showMsg;
    }

    public void setShowMsg(String showMsg) {
        this.showMsg = showMsg;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg(String showImg) {
        this.showImg = showImg;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
