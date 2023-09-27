package com.siyecaoi.yy.bean;

import java.util.List;

public class InviteRankingBean {

    private String msg;
    private int code;
    private DataEntity data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
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

    public DataEntity getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class DataEntity {

        private List<UserListEntity> userList;
        private UserEntity user;

        public void setUserList(List<UserListEntity> userList) {
            this.userList = userList;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public List<UserListEntity> getUserList() {
            return userList;
        }

        public UserEntity getUser() {
            return user;
        }

        public class UserListEntity {
            /**
             * img : http://yoyovoice.oss-cn-shanghai.aliyuncs.com/aa92cc75-54c5-4c33-9e57-7063014a54ef
             * distance : 0
             * money : 0
             * sex : 1
             * name : 哈哈
             * id : 203
             * <p>
             * distance	number	距离
             * id	number	用户id
             * img	string	头像
             * money	number	邀请金额
             * name	string	名称
             * num	number	排名 这个排名是假的，你们按照数据递增
             * sex	number	性别
             */
            private String img;
            private int distance;
            private double money;
            private int sex;
            private String name;
            private int id;

            public void setImg(String img) {
                this.img = img;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public int getDistance() {
                return distance;
            }


            public int getSex() {
                return sex;
            }

            public String getName() {
                return name;
            }

            public int getId() {
                return id;
            }
        }

        public class UserEntity {
            /**
             * img : http://yoyovoice.oss-cn-shanghai.aliyuncs.com/7f6e5bc5-8869-4e45-a7e3-8f6335ab196d
             * distance : 0
             * money : 0
             * num : 17
             * sex : 2
             * name : 玲珑大
             * id : 170
             * <p>
             * distance	number	距离
             * id	number	用户id
             * img	string	头像
             * money	number	邀请金额
             * name	string	名称
             * num	number	排名
             * sex	number	性别
             */
            private String img;
            private int distance;
            private double money;
            private int num;
            private int sex;
            private String name;
            private int id;

            public void setImg(String img) {
                this.img = img;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImg() {
                return img;
            }

            public int getDistance() {
                return distance;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public int getNum() {
                return num;
            }

            public int getSex() {
                return sex;
            }

            public String getName() {
                return name;
            }

            public int getId() {
                return id;
            }
        }
    }
}
