package com.jingtaoi.yy.bean;

import java.util.List;

public class PacketBean {

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
         * gold	number	总浪花
         * lqGold	number	领取的钻石
         * img	string	头像
         * num	number	总个数
         * lqnum	number	领取个数
         * name	string	昵称
         */
        private int gold;
        private int lqGold;
        private String img;
        private int num;
        private int lqnum;
        private String name;
        private List<RedNumBean> RedNum;

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getLqGold() {
            return lqGold;
        }

        public void setLqGold(int lqGold) {
            this.lqGold = lqGold;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getLqnum() {
            return lqnum;
        }

        public void setLqnum(int lqnum) {
            this.lqnum = lqnum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<RedNumBean> getRedNum() {
            return RedNum;
        }

        public void setRedNum(List<RedNumBean> RedNum) {
            this.RedNum = RedNum;
        }

        public static class RedNumBean {
            /**
             * img : http://thirdqq.qlogo.cn/qqapp/1107759765/50AC222D6A68FD8301B811C885079F9C/100
             * name : 贝小柴
             * num : 91
             * time : 2019-03-04 17:35:57
             */

            /**
             * img	string	头像
             * name	string	昵称
             * num	number	领取钻石数
             * time	string	领取时间
             */

            private String img;
            private String name;
            private int num;
            private String time;

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

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
