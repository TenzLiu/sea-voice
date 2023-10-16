package com.jingtaoi.yy.bean;

public class GetOneBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : {"m":"20浪花一次，头等奖花湖"}
     * sys : 1551929859675
     */

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
         * m : 20浪花一次，头等奖花湖
         */

        private String m;

        public String getM() {
            return m;
        }

        public void setM(String m) {
            this.m = m;
        }


        /**
         * 获取用户最后登录的时间
         *
         * "time": "2019-04-08 11:56:06"
         */
        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }


        /**
         *  用户财富，用户魅力
         *
         * num	number	等级需要多少
         * Grade	number	等级
         * gradeNum	number	这个等级要好多经验到达下一个等级
         */
        private int num;
        private int Grade;
        private int gradeNum;
        private int zd;//到下一个等奖总的经验
        private int xz;//现在有的经验

        public int getZd() {
            return zd;
        }

        public void setZd(int zd) {
            this.zd = zd;
        }

        public int getXz() {
            return xz;
        }

        public void setXz(int xz) {
            this.xz = xz;
        }

        public int getGradeNum() {
            return gradeNum;
        }

        public void setGradeNum(int gradeNum) {
            this.gradeNum = gradeNum;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getGrade() {
            return Grade;
        }

        public void setGrade(int grade) {
            Grade = grade;
        }


        /**
         * rid	string	房间id
         */
        private String rid;

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        private int state;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }


        /**
         * 获取宝箱的信息的说明信息
         * m1	string	第一句话
         * m2	string	第二句话
         */
        private String m1;
        private String m2;

        public String getM1() {
            return m1;
        }

        public void setM1(String m1) {
            this.m1 = m1;
        }

        public String getM2() {
            return m2;
        }

        public void setM2(String m2) {
            this.m2 = m2;
        }



        private String str;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }


        private String token;//声网token

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        private String gold;

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }
    }
}
