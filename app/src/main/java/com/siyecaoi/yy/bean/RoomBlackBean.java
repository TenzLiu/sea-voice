package com.siyecaoi.yy.bean;

import java.util.List;

public class RoomBlackBean {


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
         * createTime : 2019-02-12 17:47:12
         * glName : Abitofcomedy
         * id : 32
         * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
         * name : 小小米
         * sex : 1
         */

        /**
         * createTime	string	拉黑时间
         * glName	string	管理人名称
         * id	number	被拉黑用户id
         * img	string	头像
         * name	string	名称
         * sex	number	性别
         */
        private String createTime;
        private String glName;
        private int id;
        private String img;
        private String name;
        private int sex;
        private int state;//用户状态 1是房主，2是管理员

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getGlName() {
            return glName;
        }

        public void setGlName(String glName) {
            this.glName = glName;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
