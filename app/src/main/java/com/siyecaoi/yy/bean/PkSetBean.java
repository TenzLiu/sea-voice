package com.siyecaoi.yy.bean;

public class PkSetBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : {"user1":{"id":100,"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","name":"18382414601","num":0,"sex":1},"time":"1551770562000","id":11,"user":{"id":101,"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","name":"小小米","num":0,"sex":1}}
     * sys : 1551770553624
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
         * - user1	object	第二个位置的
         * id	number	用户id
         * img	string	头像
         * name	string	名称
         * num	number	数量
         * sex	number	性别 1男，2女
         * time	string	结算时间
         * id	number	这条数据id
         * - - user	object	第一个位置的人
         * id	number	用户id
         * img	string	头像
         * name	string	名称
         * num	number	数量
         * sex	number	性别 1男，2女
         */
        private User1Bean user1;
        private long time;
        private int id;//这条数据id
        private int state;//1	1 按人数投票，2 按礼物价值投票
        private UserBean user;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public User1Bean getUser1() {
            return user1;
        }

        public void setUser1(User1Bean user1) {
            this.user1 = user1;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class User1Bean {
            /**
             * id : 100
             * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
             * name : 18382414601
             * num : 0
             * sex : 1
             */

            private int id;
            private String img;
            private String name;
            private int num;
            private int sex;

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
        }

        public static class UserBean {
            /**
             * id : 101
             * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
             * name : 小小米
             * num : 0
             * sex : 1
             */

            private int id;
            private String img;
            private String name;
            private int num;
            private int sex;

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
        }
    }
}
