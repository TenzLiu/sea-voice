package com.jingtaoi.yy.bean;

import java.util.List;

public class PkHistoryBean {

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

    public static class DataBean {

        /**
         * createTime	string	开始时间
         * state	number	1 按人数投票，2 按礼物价值投票
         * second	number	开启的秒数
         * - - wz1	object	位置一
         * id	number	id
         * img	string	头像
         * name	string	昵称
         * num	number	数量
         * sex	number	性别
         * state	number	1 是第一个位置获胜，2是第二个位置获胜，3 平手
         * - - wz2	object	位置二
         * id	number	id
         * img	string	头像
         * name	string	昵称
         * num	number	数量
         * sex	number	性别
         * state	number	1 是第一个位置获胜，2是第二个位置获胜，3 平手
         */
        private String createTime;
        private int state;
        private int second;
        private Wz1Bean wz1;
        private Wz2Bean wz2;

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public Wz1Bean getWz1() {
            return wz1;
        }

        public void setWz1(Wz1Bean wz1) {
            this.wz1 = wz1;
        }

        public Wz2Bean getWz2() {
            return wz2;
        }

        public void setWz2(Wz2Bean wz2) {
            this.wz2 = wz2;
        }

        public static class Wz1Bean {
            /**
             * id : 100
             * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
             * name : 18382414601
             * num : 0
             * sex : 1
             * state : 3
             */

            private int id;
            private String img;
            private String name;
            private int num;
            private int sex;
            private int state;

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

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
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
        }

        public static class Wz2Bean {
            /**
             * id : 101
             * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
             * name : 小小米
             * num : 0
             * sex : 1
             * state : 3
             */

            private int id;
            private String img;
            private String name;
            private int num;
            private int sex;
            private int state;

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

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
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
        }
    }
}
