package com.siyecaoi.yy.bean;

public class OnlineUserBean {

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
         * state | number| 1是未关注，2是已关注 |
         */
        private int state;
        private UserBean user;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * charmGrade : 1
             * constellation : 双鱼
             * id : 14
             * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
             * name : 小小米
             * num : 1
             * sex : 1
             * treasureGrade : 1
             * usercoding : 26969461
             */

            /**
             * charmGrade | number|//魅力 |
             * | constellation | string| //星座 |
             * | id | number| 用户id |
             * | img | string|//头像|
             * | name | string| //姓名 |
             * | num | number| //粉丝 |
             * | sex | number| //性别 |
             * | treasureGrade | number| //财富 |
             * | usercoding | string| 唯一标识id |
             */
            private int charmGrade;
            private String constellation;
            private int id;
            private String img;
            private String name;
            private int num;
            private int sex;
            private int treasureGrade;
            private String usercoding;
            private String liang;

            public String getLiang() {
                return liang;
            }

            public void setLiang(String liang) {
                this.liang = liang;
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
        }
    }
}
