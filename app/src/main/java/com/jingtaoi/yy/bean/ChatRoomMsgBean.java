package com.jingtaoi.yy.bean;

import com.jingtaoi.yy.model.ChatRoomMsgModel;

import java.util.List;

public class ChatRoomMsgBean {
    private String msg;
    private int code;
    private long sys;
    private List<ChatRoomMsgModel.DataBean> data;

    public List<ChatRoomMsgModel.DataBean> getData() {
        return data;
    }

    public void setData(List<ChatRoomMsgModel.DataBean> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getSys() {
        return sys;
    }

    public void setSys(long sys) {
        this.sys = sys;
    }
}
