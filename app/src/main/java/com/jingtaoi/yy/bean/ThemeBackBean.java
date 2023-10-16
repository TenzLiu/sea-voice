package com.jingtaoi.yy.bean;

import java.util.List;

public class ThemeBackBean {


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
         * createTime : 2019-02-13 14:40:49
         * id : 1
         * img : http://uucj.oss-cn-beijing.aliyuncs.com/de8af92b-e95b-464b-9f25-eff5e3d477c3.jpg
         * isDelete : 1
         * name : 哈哈
         * num : 1
         * sequence : 1
         * state : 1
         */

        /**
         * createTime	string	上传时间
         * id	number	无
         * img	string	背景图
         * isDelete	number	是否被删除（1=否 2=是
         * name	string	背景图名称
         * num	number	历史使用次数
         * sequence	number	顺序
         * state	number	是否锁定 1否，2是
         */
        private String createTime;
        private int id;
        private String img;
        private int isDelete;
        private String name;
        private int num;
        private int sequence;
        private int state;


        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
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

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
