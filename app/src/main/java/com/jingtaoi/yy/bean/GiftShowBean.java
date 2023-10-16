package com.jingtaoi.yy.bean;

import java.util.List;

public class GiftShowBean {

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
         * gold : 99
         * img : http://yoyovoice.oss-cn-shanghai.aliyuncs.com/img/a831b41456d24a14a46f0c004548b518.png
         * name : 测试1
         */
        private int gold;
        private String img;
        private String name;

        /**
         * createTime	string	时间
         * img	string	图片
         * name	string	名称
         * num	number	数量
         */
        private long createTime;
        private int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGold() {
            return gold;
        }

        public String getImg() {
            return img;
        }

        public String getName() {
            return name;
        }
    }
}
