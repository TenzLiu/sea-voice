package com.jingtaoi.yy.bean;

import java.util.List;

public class ChestOpenBean {

    private String msg;
    private int code;
    private DataBean data;
    private String sys;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public static class DataBean {

        private UserBean user;
        private List<LotteryBean> Lottery;
        private List<MicXgfjBean.DataBean.ListDataBean> msgList;


        public List<MicXgfjBean.DataBean.ListDataBean> getMsgList() {
            return msgList;
        }

        public void setMsgList(List<MicXgfjBean.DataBean.ListDataBean> msgList) {
            this.msgList = msgList;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<LotteryBean> getLottery() {
            return Lottery;
        }

        public void setLottery(List<LotteryBean> Lottery) {
            this.Lottery = Lottery;
        }

        public static class UserBean {

            private int age;
            private int attentionNum;
            private int charmGrade;
            private String constellation;
            private String createDate;
            private String dateOfBirth;
            private double divideAward;
            private int fansNum;
            private int gift;
            private int gold;
            private int goldNum;
            private double historyDeposit;
            private double historyRecharge;
            private int id;
            private String imgTx;
            private double inviteAward;
            private int inviteCount;
            private String jd;
            private String nickname;
            private String password;
            private String phone;
            private String qqSid;
            private int scene;
            private int sex;
            private int state;
            private int status;
            private int treasureGrade;
            private String userTh;
            private String usercoding;
            private String wd;
            private String wxSid;
            private int ynum;
            private int yuml;

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public int getAttentionNum() {
                return attentionNum;
            }

            public void setAttentionNum(int attentionNum) {
                this.attentionNum = attentionNum;
            }

            public int getCharmGrade() {
                return charmGrade;
            }

            public void setCharmGrade(int charmGrade) {
                this.charmGrade = charmGrade;
            }

            public String getConstellation() {
                return constellation;
            }

            public void setConstellation(String constellation) {
                this.constellation = constellation;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getDateOfBirth() {
                return dateOfBirth;
            }

            public void setDateOfBirth(String dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
            }

            public double getDivideAward() {
                return divideAward;
            }

            public void setDivideAward(double divideAward) {
                this.divideAward = divideAward;
            }

            public int getFansNum() {
                return fansNum;
            }

            public void setFansNum(int fansNum) {
                this.fansNum = fansNum;
            }

            public int getGift() {
                return gift;
            }

            public void setGift(int gift) {
                this.gift = gift;
            }

            public int getGold() {
                return gold;
            }

            public void setGold(int gold) {
                this.gold = gold;
            }

            public int getGoldNum() {
                return goldNum;
            }

            public void setGoldNum(int goldNum) {
                this.goldNum = goldNum;
            }

            public double getHistoryDeposit() {
                return historyDeposit;
            }

            public void setHistoryDeposit(double historyDeposit) {
                this.historyDeposit = historyDeposit;
            }

            public double getHistoryRecharge() {
                return historyRecharge;
            }

            public void setHistoryRecharge(double historyRecharge) {
                this.historyRecharge = historyRecharge;
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

            public double getInviteAward() {
                return inviteAward;
            }

            public void setInviteAward(double inviteAward) {
                this.inviteAward = inviteAward;
            }

            public int getInviteCount() {
                return inviteCount;
            }

            public void setInviteCount(int inviteCount) {
                this.inviteCount = inviteCount;
            }

            public String getJd() {
                return jd;
            }

            public void setJd(String jd) {
                this.jd = jd;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getQqSid() {
                return qqSid;
            }

            public void setQqSid(String qqSid) {
                this.qqSid = qqSid;
            }

            public int getScene() {
                return scene;
            }

            public void setScene(int scene) {
                this.scene = scene;
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getTreasureGrade() {
                return treasureGrade;
            }

            public void setTreasureGrade(int treasureGrade) {
                this.treasureGrade = treasureGrade;
            }

            public String getUserTh() {
                return userTh;
            }

            public void setUserTh(String userTh) {
                this.userTh = userTh;
            }

            public String getUsercoding() {
                return usercoding;
            }

            public void setUsercoding(String usercoding) {
                this.usercoding = usercoding;
            }

            public String getWd() {
                return wd;
            }

            public void setWd(String wd) {
                this.wd = wd;
            }

            public String getWxSid() {
                return wxSid;
            }

            public void setWxSid(String wxSid) {
                this.wxSid = wxSid;
            }

            public int getYnum() {
                return ynum;
            }

            public void setYnum(int ynum) {
                this.ynum = ynum;
            }

            public int getYuml() {
                return yuml;
            }

            public void setYuml(int yuml) {
                this.yuml = yuml;
            }
        }

        public static class LotteryBean {
            /**
             * day : 0
             * id : 2
             * img :
             * name : yy
             * type : 1
             */

            /**
             * day	number	天数，0 就是不限
             * id	number	礼物的id
             * img	string	图片
             * name	string	名称
             * type	number	类型 1道具座驾，2道具头环，3礼物 ，4 再接再厉
             */

            private int day;
            private int id;
            private String img;
            private String name;
            private int type;
            private int num;
            private long cost;
            private long gold;

            public long getGold() {
                return gold;
            }

            public void setGold(long gold) {
                this.gold = gold;
            }

            public long getCost() {
                return getGold();
            }

            public void setCost(long cost) {
                setGold(cost);
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
