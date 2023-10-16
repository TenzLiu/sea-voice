package com.jingtaoi.yy.bean;

public class UserBean {

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
        /**
         * age : 0
         * attentionNum : 1
         * charmGrade : 1
         * createDate : 2019-02-20 10:13:51
         * dateOfBirth :
         * fansNum : 0
         * gift : 0
         * gold : 9900
         * goldNum : 0
         * historyDeposit : 0
         * historyRecharge : 0
         * id : 100
         * imgTx : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
         * jd : 104.07728530701289
         * nickname : 18382414601
         * password :
         * phone :
         * qqSid :
         * sex : 1
         * state : 1
         * status : 1
         * treasureGrade : 1
         * usercoding : 19372056
         * wd : 30.559209990493553
         * wxSid : 18382414601
         * ynum : 0
         * yuml : 0
         */

        /**
         * age	number	年龄
         * attentionNum	number	关注数
         * charmGrade	number	魅力等级
         * createDate	string	注册时间
         * dateOfBirth	string	出生日期
         * fansNum	number	粉丝数
         * gift	number	礼物数
         * gold	number	浪花数
         * goldNum	number	财富的累加值
         * historyDeposit	number	历史充值
         * historyRecharge	number	历史提现金额
         * id	number	用户id
         * imgTx	string	用户头像
         * jd	string	经度
         * nickname	string	昵称
         * password	string	密码
         * phone	string	手机号
         * qqSid	string	qq唯一标识
         * sex	number	性别(1 男, 2 女)
         * state	number	无
         * status	number	是否开通房间 1否 ， 2是
         * treasureGrade	number	财富等级
         * usercoding	string	用户 深海号
         * wd	string	维度
         * wxSid	string	微信标识
         * ynum	number	钻石数
         * yuml	number	钻石数累计数
         */
        private int age;
        public int isOpenDm; //钻石蛋是否要隐藏: 0隐藏  1显示
        public int isOpenCjd; //钻石蛋是否要隐藏: 0隐藏  1显示
        private int attentionNum;
        private int charmGrade;
        private String createDate;
        private String dateOfBirth;
        private int fansNum;
        private int gift;
        private int gold;
        private int goldNum;
        private int historyDeposit;
        private int historyRecharge;
        private int id;
        private String imgTx;
        private String jd;
        private String nickname;
        private String password;
        private String phone;
        private String qqSid;
        private int sex;
        private int state;
        private int status;
        private int treasureGrade;
        private String usercoding;
        private String wd;
        private String wxSid;
        private String ynum;
        private int yuml;
        private String token;//腾讯云usersig
        private String userThfm;//头环的url
        private String userZjfm;//座驾的url
        private String userTh;//头环的url
        private String userZj;//座驾的url
        private int autonym;//是否实名认证 1否，2是
        private String individuation;//用户签名
        private String liang;//靓号
        private String payPassword;//支付密码
        private String apptokenid;//用户token
        private int isAgentGive;//是否代充  1否 2是
        private int isFangDingHao;//是否防顶号；1=否；2=是


        public int getIsFangDingHao() {
            return isFangDingHao;
        }

        public void setIsFangDingHao(int isFangDingHao) {
            this.isFangDingHao = isFangDingHao;
        }

        public int getIsAgentGive() {
            return isAgentGive;
        }

        public void setIsAgentGive(int isAgentGive) {
            this.isAgentGive = isAgentGive;
        }

        public String getApptokenid() {
            return apptokenid;
        }

        public void setApptokenid(String apptokenid) {
            this.apptokenid = apptokenid;
        }

        public String getPayPassword() {
            return payPassword;
        }

        public void setPayPassword(String payPassword) {
            this.payPassword = payPassword;
        }

        public String getLiang() {
            return liang;
        }

        public void setLiang(String liang) {
            this.liang = liang;
        }

        public String getIndividuation() {
            return individuation;
        }

        public void setIndividuation(String individuation) {
            this.individuation = individuation;
        }

        public String getUserThfm() {
            return userThfm;
        }

        public void setUserThfm(String userThfm) {
            this.userThfm = userThfm;
        }

        public String getUserZjfm() {
            return userZjfm;
        }

        public void setUserZjfm(String userZjfm) {
            this.userZjfm = userZjfm;
        }

        public String getUserTh() {
            return userTh;
        }

        public void setUserTh(String userTh) {
            this.userTh = userTh;
        }

        public String getUserZj() {
            return userZj;
        }

        public void setUserZj(String userZj) {
            this.userZj = userZj;
        }

        public int getAutonym() {
            return autonym;
        }

        public void setAutonym(int autonym) {
            this.autonym = autonym;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

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

        public int getHistoryDeposit() {
            return historyDeposit;
        }

        public void setHistoryDeposit(int historyDeposit) {
            this.historyDeposit = historyDeposit;
        }

        public int getHistoryRecharge() {
            return historyRecharge;
        }

        public void setHistoryRecharge(int historyRecharge) {
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

        public String getYnum() {
            return ynum;
        }

        public void setYnum(String ynum) {
            this.ynum = ynum;
        }

        public int getYuml() {
            return yuml;
        }

        public void setYuml(int yuml) {
            this.yuml = yuml;
        }
    }
}
