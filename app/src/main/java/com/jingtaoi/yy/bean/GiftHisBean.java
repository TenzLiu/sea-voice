package com.jingtaoi.yy.bean;

import java.util.List;

public class GiftHisBean {

    private String msg;
    private int code;
    private List<GiftRecordEntity> data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(List<GiftRecordEntity> data) {
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

    public List<GiftRecordEntity> getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class GiftRecordEntity {
            /**
             * img : http://thirdqq.qlogo.cn/qqapp/1107759765/50AC222D6A68FD8301B811C885079F9C/100
             * createTime : 2019-04-12 15:16:15
             * num : 1
             * name : 你好久
             * <p>
             * createTime	string	时间
             * img	string	礼物图片
             * name	string	名字
             * num	number	数量
             */
            private String img;
            private String createDate;
            private int num;
            private String name;
            private int type;//1是普通，2 是宝箱

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public void setCreateDate(String createTime) {
                this.createDate = createTime;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public String getCreateDate() {
                return createDate;
            }

            public int getNum() {
                return num;
            }

            public String getName() {
                return name;
            }
        }
}
