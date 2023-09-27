package com.siyecaoi.yy.bean;

import java.util.List;

public class RoomAdminBean {


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
         * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
         * name : 小小米
         * id : 15
         * sex : 1
         */

        /**
         * img	string	头像
         * name	string	名称
         * sex	number	性别
         * id	number	用户id
         */
        private String img;
        private String name;
        private int id;
        private int sex;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
