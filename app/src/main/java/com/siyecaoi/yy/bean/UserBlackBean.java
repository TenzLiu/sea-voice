package com.siyecaoi.yy.bean;

import java.util.List;

public class UserBlackBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : [{"img":"http://lvyouyou.oss-cn-beijing.aliyuncs.com/9a376757-0322-466d-9f21-a648d6cd3307","sex":1,"name":"大大"}]
     * sys : 1554880995227
     */
    private String msg;
    private int code;
    private List<DataEntity> data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(List<DataEntity> data) {
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

    public List<DataEntity> getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class DataEntity {
        /**
         * img : http://lvyouyou.oss-cn-beijing.aliyuncs.com/9a376757-0322-466d-9f21-a648d6cd3307
         * sex : 1
         * name : 大大
         *
         * img	string	头像
         * name	string	名称
         * sex	number	性别 1男，2女
         */
        private String img;
        private int sex;
        private String name;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public int getSex() {
            return sex;
        }

        public String getName() {
            return name;
        }
    }
}
