package com.siyecaoi.yy.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/11.
 */

public class BaseBean implements Serializable {

    /**
     * msg : 验证码错误！
     * code : 1
     * data : {}
     * sys : 1518317997925
     */

    private String msg;
    private int code;
    private Object data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getSys() {
        return sys;
    }

    public void setSys(long sys) {
        this.sys = sys;
    }



}
