package com.siyecaoi.yy.bean;

public class UpdateOneBean {

    private String msg;
    private int code;
    private DataEntity data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
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

    public DataEntity getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class DataEntity {
        /**
         * userTh : x
         * thid : 0
         * id : 294
         * userThfm : x
         */
        private String userTh;
        private int thid;
        private int id;
        private String userThfm;
        private String userZj;
        private String userZjfm;

        public String getUserZj() {
            return userZj;
        }

        public void setUserZj(String userZj) {
            this.userZj = userZj;
        }

        public String getUserZjfm() {
            return userZjfm;
        }

        public void setUserZjfm(String userZjfm) {
            this.userZjfm = userZjfm;
        }

        public void setUserTh(String userTh) {
            this.userTh = userTh;
        }

        public void setThid(int thid) {
            this.thid = thid;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setUserThfm(String userThfm) {
            this.userThfm = userThfm;
        }

        public String getUserTh() {
            return userTh;
        }

        public int getThid() {
            return thid;
        }

        public int getId() {
            return id;
        }

        public String getUserThfm() {
            return userThfm;
        }
    }
}
