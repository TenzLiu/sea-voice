package com.siyecaoi.yy.model;

/**
 * 设置管理员消息
 */
public class SetAdminBean {

    private int code;
    private DataBean data;

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

    public static class DataBean {


        private int buid;//被设置的管理员的id
        private String name;//管理员名称
        private int grade;//管理员财富等级

        public int getBuid() {
            return buid;
        }

        public void setBuid(int buid) {
            this.buid = buid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }
}
