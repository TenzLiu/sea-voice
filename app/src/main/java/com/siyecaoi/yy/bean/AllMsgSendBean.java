package com.siyecaoi.yy.bean;

public class AllMsgSendBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : [{"uid":405,"createTime":"2019-09-18 09:46:49","id":1,"content":"哈哈"}]
     * sys : 1568771382343
     */
    private String msg;
    private int code;
    private AllmsgBean.DataEntity data;
    private String sys;

    public AllmsgBean.DataEntity getData() {
        return data;
    }

    public void setData(AllmsgBean.DataEntity data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public String getSys() {
        return sys;
    }


}
