package com.siyecaoi.yy.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/11.
 */

public class GetAgoraTokenBean implements Serializable {

    /**
     * msg : 验证码错误！
     * code : 1
     * data : {}
     * sys : 1518317997925
     */

    private String msg;
    private int code;
    private AgoraToken data;
    private long sys;

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

    public AgoraToken getData() {
        return data;
    }

    public void setData(AgoraToken data) {
        this.data = data;
    }

    public long getSys() {
        return sys;
    }

    public void setSys(long sys) {
        this.sys = sys;
    }


   public class AgoraToken {
        public String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
