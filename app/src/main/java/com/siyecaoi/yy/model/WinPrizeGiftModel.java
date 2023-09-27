package com.siyecaoi.yy.model;

import java.util.List;

public class WinPrizeGiftModel {

    private int code;
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

        private String       messageShow;// 消息内容
        private String       rid;// 房间id
        private String       nickname;//用户昵称
        private String       userImg;//用户头像
        private Integer      cost;//价值
        private Integer      giftNum;//数量
        private String       giftName;//名称
        private String       giftImg;//图片
        private List<String> rids;


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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public Integer getCost() {
            return cost;
        }

        public void setCost(Integer cost) {
            this.cost = cost;
        }

        public Integer getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(Integer giftNum) {
            this.giftNum = giftNum;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getGiftImg() {
            return giftImg;
        }

        public void setGiftImg(String giftImg) {
            this.giftImg = giftImg;
        }

        public List<String> getRids() {
            return rids;
        }

        public void setRids(List<String> rids) {
            this.rids = rids;
        }
    }
}
