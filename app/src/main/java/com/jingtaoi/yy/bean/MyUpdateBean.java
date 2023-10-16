package com.jingtaoi.yy.bean;

import java.util.List;

public class MyUpdateBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : [{"createDate":"2019-03-05 14:59:22","gsNane":"5月天","id":2,"isDelete":2,"musicName":"突然好想你","objectKey":"music/f0fe4ca11dff436babe97c9b04174b53.mp3","state":2,"status":2,"uid":101,"url":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/music/f0fe4ca11dff436babe97c9b04174b53.mp3"}]
     * sys : 1554713096055
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

    public static class DataBean {
        /**
         * createDate : 2019-03-05 14:59:22
         * gsNane : 5月天
         * id : 2
         * isDelete : 2
         * musicName : 突然好想你
         * objectKey : music/f0fe4ca11dff436babe97c9b04174b53.mp3
         * state : 2
         * status : 2
         * uid : 101
         * url : http://yoyovoice.oss-cn-shanghai.aliyuncs.com/music/f0fe4ca11dff436babe97c9b04174b53.mp3
         */

        private String createDate;
        private String gsNane;
        private int id;
        private int isDelete;
        private String musicName;
        private String objectKey;
        private int state;
        private int status;
        private int uid;
        private String url;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getGsNane() {
            return gsNane;
        }

        public void setGsNane(String gsNane) {
            this.gsNane = gsNane;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public String getMusicName() {
            return musicName;
        }

        public void setMusicName(String musicName) {
            this.musicName = musicName;
        }

        public String getObjectKey() {
            return objectKey;
        }

        public void setObjectKey(String objectKey) {
            this.objectKey = objectKey;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
