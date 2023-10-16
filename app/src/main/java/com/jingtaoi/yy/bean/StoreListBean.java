package com.jingtaoi.yy.bean;

import java.util.List;

public class StoreListBean {

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
         * gold : 2
         * imgFm :
         * img :
         * name : xx
         * day : 0
         * <p>
         * day	number	天数（-1不限制）
         * gold	number	需要浪花
         * img	string	道具图片(动态图)
         * imgFm	string	道具封面
         * name	string	道具名字
         */
        private int gold;
        private String imgFm;
        private String img;
        private String name;
        private int day;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public void setImgFm(String imgFm) {
            this.imgFm = imgFm;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getGold() {
            return gold;
        }

        public String getImgFm() {
            return imgFm;
        }

        public String getImg() {
            return img;
        }

        public String getName() {
            return name;
        }

        public int getDay() {
            return day;
        }
    }
}
