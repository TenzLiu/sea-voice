package com.jingtaoi.yy.bean;

import java.util.List;

public class VoiceMicBean {


    /**
     * msg : 获取成功
     * code : 0
     * sys : 1547631246851
     */

    private String msg;
    private int code;
    private String sys;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    /**
     * id	number	数据id
     * pid	string	房间id
     * sequence	number	麦位顺序
     * state	number	是否禁麦此座位 1否， 2 是
     * status	number	是否封锁此座位 1否， 2是
     * uid	number	房间的所有者的id
     */
    public static class DataBean {
        /**
         * id : 9
         * pid : 94765151
         * sequence : 1
         * state : 1
         * status : 1
         * uid : 7
         */

        private int id;
        private String pid;
        private int sequence;
        /**
         * 是否禁麦此座位 1否， 2 是
         */
        private int state;
        /**
         * 是否封锁此座位 1否， 2是
         */
        private int status;
        private int uid;
        private VoiceUserBean.DataBean userModel;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public VoiceUserBean.DataBean getUserModel() {
            return userModel;
        }

        public void setUserModel(VoiceUserBean.DataBean userModel) {
            this.userModel = userModel;
        }


    }
}
