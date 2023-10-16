package com.jingtaoi.yy.bean;

import java.util.List;

/**
 * @author: xha
 * @date: 2020/10/25 13:41
 * @Description:
 */
public class MicXgfjBean {

    /**
     * msg : 获取成功
     * code : 0
     * sys : 1547631246851
     */

    private String msg;
    private int code;
    private String sys;
    private DataBean data;

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

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        //麦位信息
        private String attr_mics;
        //房间信息
        private String attr_xgfj;
        //全屏礼物通知消息
        private List<ListDataBean> msgList;
        //用户信息
        private UserBean.DataBean user;


        public static class ListDataBean {
            private String messageChannelSend;

            public String getMessageChannelSend() {
                return messageChannelSend;
            }

            public void setMessageChannelSend(String messageChannelSend) {
                this.messageChannelSend = messageChannelSend;
            }
        }

        public String getAttr_mics() {
            return attr_mics;
        }

        public void setAttr_mics(String attr_mics) {
            this.attr_mics = attr_mics;
        }

        public String getAttr_xgfj() {
            return attr_xgfj;
        }

        public void setAttr_xgfj(String attr_xgfj) {
            this.attr_xgfj = attr_xgfj;
        }

        public List<ListDataBean> getMsgList() {
            return msgList;
        }

        public void setMsgList(List<ListDataBean> msgList) {
            this.msgList = msgList;
        }

        public UserBean.DataBean getUser() {
            return user;
        }

        public void setUser(UserBean.DataBean user) {
            this.user = user;
        }
    }
}
