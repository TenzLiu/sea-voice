package com.jingtaoi.yy.bean;

import java.util.List;

import cn.sinata.xldutils.utils.StringUtils;

public class VoiceUserBean {

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
     * id	number	用户id
     * img	string	用户头像
     * name	string	用户昵称
     * sequence	number	上麦顺序 默认是0
     * sex	number	性别 1男，2 女
     * state	number	是否上麦 1否，2 是
     * type	number	1是房主，2 是管理员，3用户
     */
    public static class DataBean {
        /**
         * id : 1
         * img : http://uucj.oss-cn-beijing.aliyuncs.com/80fbee3c-d18b-4911-8c49-5940c4d14553.jpg
         * name :
         * sequence : 1
         * sex : 1
         * state : 1
         * type : 1
         */

        private int id;
        private String img;
        private String name;
        private int sequence;
        private int sex;
        private int state;
        private int type;
        private String rtmToken;
        private String rtcToken;
        /**
         * 是否被选中为送礼用户(是否为打赏用户)
         */
        private boolean isChoose;

        /**
         * 判断麦上用户是否在说话
         */
        private boolean isSpeak;

        /**
         * 展示表情
         */
        private int showImg;

        private boolean isImgOver;//表情是否播放完毕


        private int numberShow;//动画效果的序号

        //魅力等级
        private int charmGrade;
        //财富等级
        private int treasureGrade;
        /**
         * 魅力
         */
        private int num;

        private int IsAgreement;//1用户，2 协议号

        private String userTh;//头环url

        private String usercoding;

        public String getUsercoding() {
            return usercoding;
        }

        public void setUsercoding(String usercoding) {
            this.usercoding = usercoding;
        }

        public boolean isImgOver() {
            return isImgOver;
        }

        public void setImgOver(boolean imgOver) {
            isImgOver = imgOver;
        }

        public int getNumberShow() {
            return numberShow;
        }

        public void setNumberShow(int numberShow) {
            this.numberShow = numberShow;
        }

        public String getUserTh() {
            return userTh;
        }

        public void setUserTh(String userTh) {
            this.userTh = userTh;
        }

        public int getIsAgreement() {
            return IsAgreement;
        }

        public void setIsAgreement(int isAgreement) {
            IsAgreement = isAgreement;
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

        public int getCharmGrade() {
            return charmGrade;
        }

        public void setCharmGrade(int charmGrade) {
            this.charmGrade = charmGrade;
        }

        public int getShowImg() {
            return showImg;
        }

        public void setShowImg(int showImg) {
            this.showImg = showImg;
        }

        public boolean isSpeak() {
            return isSpeak;
        }

        public void setSpeak(boolean speak) {
            isSpeak = speak;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            if (img == null || img.isEmpty()) {
                return "";
            }
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            if (StringUtils.isEmpty(name)) {
                return "--";
            }
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRtmToken() {
            return rtmToken;
        }

        public void setRtmToken(String rtmToken) {
            this.rtmToken = rtmToken;
        }

        public String getRtcToken() {
            return rtcToken;
        }

        public void setRtcToken(String rtcToken) {
            this.rtcToken = rtcToken;
        }
    }
}
