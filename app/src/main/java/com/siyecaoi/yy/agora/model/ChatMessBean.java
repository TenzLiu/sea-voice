package com.siyecaoi.yy.agora.model;

public class ChatMessBean {
    /**
     * 消息码（0是普通消息）
     */
    private int code;
    /**
     * 消息内容
     */
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * 用户id
         */
        private int uid;
        /**
         * 用户昵称
         */
        private String userName;
        /**
         * 消息内容
         */
        private String msgShow;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMsgShow() {
            return msgShow;
        }

        public void setMsgShow(String msgShow) {
            this.msgShow = msgShow;
        }
    }

}
