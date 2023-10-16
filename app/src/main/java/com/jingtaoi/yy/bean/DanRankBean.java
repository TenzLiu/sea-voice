package com.jingtaoi.yy.bean;

import java.util.List;

public class DanRankBean {

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
         * "createtime":"2020-10-08 15:26:07",
         *     			"gid":38,
         *     			"giftImg":"https://hw29115726.obs.cn-east-3.myhuaweicloud.com/admin/569303fc9d3b4b61afb09e6080dad171.svga",
         *     			"giftName":"梦生鲸",
         *     			"giftNum":1,
         *     			"id":17,
         *     			"imgTx":"https://hw29115726.obs.cn-east-3.myhuaweicloud.com:443/71855ccd-f200-4ee7-a074-735e9dbd3599",
         *     			"nickname":"mike008",
         *     			"num":100,
         *     			"treasureGrade":16,
         *     			"uid":43
         */

        private String createtime;
        private int gid;
        private String giftImg;
        private String giftName;
        private int giftNum;
        private int id;
        private String imgTx;
        private String nickname;
        private int num;
        private int treasureGrade;
        private int uid;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public String getGiftImg() {
            return giftImg;
        }

        public void setGiftImg(String giftImg) {
            this.giftImg = giftImg;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public int getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(int giftNum) {
            this.giftNum = giftNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgTx() {
            return imgTx;
        }

        public void setImgTx(String imgTx) {
            this.imgTx = imgTx;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getTreasureGrade() {
            return treasureGrade;
        }

        public void setTreasureGrade(int treasureGrade) {
            this.treasureGrade = treasureGrade;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
