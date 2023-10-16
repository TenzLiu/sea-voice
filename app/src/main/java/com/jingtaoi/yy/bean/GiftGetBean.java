package com.jingtaoi.yy.bean;

import java.util.List;

public class GiftGetBean {

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

    public class DataEntity {
        /**
         * img : http://uucj.oss-cn-beijing.aliyuncs.com/MP4/3e8f51a911914670a7e7d997bdbeb0d0.svga
         * num : 167
         * name : 8888
         * <p>
         * img	string	礼物图
         * name	string	名字
         * num	number	数量
         */
        private String img;
        private int num;
        private String name;

        public void setImg(String img) {
            this.img = img;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public int getNum() {
            return num;
        }

        public String getName() {
            return name;
        }
    }
}
