package com.siyecaoi.yy.model;

public class GiftSendMessage {

    //101 礼物消息
    public GiftSendMessage(int code, DataBean dataBean) {
        this.code = code;
        this.data = dataBean;
    }

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
        /**
         * @param uid     赠送的用户id
         * @param name    赠送的用户的昵称
         * @param sendId  被赠送的用户的id
         * @param gid     礼物id
         * @param img     礼物图片
         * @param showImg 礼物效果图片
         * @param num     礼物数量
         */
        public DataBean(int uid, String name, int grade, String sendId, String names,
                        int gid, String img, String showImg, int num, int goodGold) {
            this.uid = uid;
            this.name = name;
            this.grade = grade;
            this.sendId = sendId;
            this.names = names;
            this.gid = gid;
            this.img = img;
            this.showImg = showImg;
            this.num = num;
            this.goodGold = goodGold;
        }

        public DataBean(String showImg, int num) {
            this.showImg = showImg;
            this.num = num;
        }

        private int uid;//赠送的用户id
        private String name;//赠送的用户的昵称
        private int grade;//用户等级

        private String sendId;//被赠送的用户的id 用逗号隔开
        private String names;//被赠送的用户名称
        private int gid;//礼物id
        private String img;//礼物图片
        private String showImg;//礼物效果图片
        private int num;//礼物数量
        private int goodGold;//礼物单价

        public int getGoodGold() {
            return goodGold;
        }

        public void setGoodGold(int goodGold) {
            this.goodGold = goodGold;
        }

        public String getNames() {
            return names;
        }

        public void setNames(String names) {
            this.names = names;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getUid() {
            return uid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSendId() {
            return sendId;
        }

        public void setSendId(String sendId) {
            this.sendId = sendId;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public String getShowImg() {
            return showImg;
        }

        public void setShowImg(String showImg) {
            this.showImg = showImg;
        }
    }
}
