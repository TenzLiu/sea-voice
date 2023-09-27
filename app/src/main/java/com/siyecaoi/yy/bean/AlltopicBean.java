package com.siyecaoi.yy.bean;

import java.util.List;

public class AlltopicBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : [{"uid":405,"createTime":"2019-09-18 09:46:49","id":1,"content":"哈哈"}]
     * sys : 1568771382343
     */
    private String msg;
    private int code;
    private List<DataEntity> data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(List<DataEntity> data) {
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

    public List<DataEntity> getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public static class DataEntity {
        /**
         * uid : 405
         * createTime : 2019-09-18 09:46:49
         * id : 1
         * content : 哈哈
         */
        private int uid;
        private String createTime;
        private int id;
        private String content;
        private String userName;

        private String messageShow ;// "messageShow": "iggit",
        private String   rid     ;//"rid": "07159486",
        private String  userImg  ;// "userImg": "https

        public String getMessageShow() {
            return messageShow;
        }

        public void setMessageShow(String messageShow) {
            this.messageShow = messageShow;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getUid() {
            return uid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }
}
