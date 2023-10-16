package com.jingtaoi.yy.bean;

import java.util.List;

public class RoomAuctionBean {


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
         * charmModels : {"grade":1,"id":56,"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","name":"小小米","num":1000,"sex":1}
         * grade : 1
         * id : 59
         * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
         * name : 18382419999
         * num : 1900
         * sex : 1
         */

        /**
         * charmModels |object | 无 |
         * | grade | number| 魅力等级 |
         * | id | number| 用户id |
         * | img | string| 头像 |
         * | name | string| 名称 |
         * | num | number| 魅力数 |
         * | sex | number| 性别 |
         */
        private CharmModelsBean charmModels;
        private int grade;
        private int id;
        private String img;
        private String name;
        private int num;
        private int sex;

        public CharmModelsBean getCharmModels() {
            return charmModels;
        }

        public void setCharmModels(CharmModelsBean charmModels) {
            this.charmModels = charmModels;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
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

        public static class CharmModelsBean {
            /**
             * grade : 1
             * id : 56
             * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
             * name : 小小米
             * num : 1000
             * sex : 1
             */

            /**
             * grade | number|魅力等级|
             * | id | number| 用户id|
             * | img | string| 头像|
             * | name | string| 名称 |
             * | num | number| 魅力数 |
             * | sex | number| 性别
             */
            private int grade;
            private int id;
            private String img;
            private String name;
            private int num;
            private int sex;

            public int getGrade() {
                return grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
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
        }
    }
}
