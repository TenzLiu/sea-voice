package com.siyecaoi.yy.bean;

import java.util.List;

public class GradeShowBean {

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
         * p : 1000
         * b : 1000
         * x : 1
         * y : 2
         * z : 2
         * id : 1
         * state : 1
         *
         * b	number	需要等级的经验
         * id	number	无
         * p	number	到达的财富和魅力
         * state	number	1是财富，2是魅力
         * x	number	开头级数
         * y	number	到达级数
         * z	number	开始的财富和魅力
         */
        private int p;
        private int b;
        private int x;
        private int y;
        private int z;
        private int id;
        private int state;

        public void setP(int p) {
            this.p = p;
        }

        public void setB(int b) {
            this.b = b;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setZ(int z) {
            this.z = z;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getP() {
            return p;
        }

        public int getB() {
            return b;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public int getId() {
            return id;
        }

        public int getState() {
            return state;
        }
    }
}
