package com.siyecaoi.yy.model;

public class ChatRoomMsgModel {

    /**
     * 113 广播消息
     */
    private int code;
    private DataBean data;
    private int state;//state 3 自定义广播消息  4全国消息

    public ChatRoomMsgModel(int code, DataBean data) {
        this.code = code;
        this.data = data;
    }

    public ChatRoomMsgModel(int code, DataBean data, int state) {
        this.code = code;
        this.data = data;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

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

        private int charm;//用户魅力等级
        private int grade;//财富等级
        private String messageShow;//消息内容
        private String name;//名称
        private int uid;//用户id
        private String header;//头像
        private int sex;//性别  1男2女

        public int getCharm() {
            return charm;
        }

        public void setCharm(int charm) {
            this.charm = charm;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getMessageShow() {
            return messageShow;
        }

        public void setMessageShow(String messageShow) {
            this.messageShow = messageShow;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
