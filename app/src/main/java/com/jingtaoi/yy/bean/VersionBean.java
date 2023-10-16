package com.jingtaoi.yy.bean;

public class VersionBean {

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
         * versions : 1.0
         * id : 1
         * state : 1
         * mark : 测试
         * url : xxxx
         * createDate : 2019-05-17 15:33:58
         * <p>
         * createDate	string	上传时间
         * id	number	id
         * mark	string	版本描述
         * state	number	是否强制更新 1否，2 是
         * url	string	更新的链接
         * versions	string	版本号
         */
        private String versions;
        private int id;
        private int state;
        private String mark;
        private String url;
        private String createDate;

        public void setVersions(String versions) {
            this.versions = versions;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getVersions() {
            return versions;
        }

        public int getId() {
            return id;
        }

        public int getState() {
            return state;
        }

        public String getMark() {
            return mark;
        }

        public String getUrl() {
            return url;
        }

        public String getCreateDate() {
            return createDate;
        }
    }
}
