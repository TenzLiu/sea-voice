package com.jingtaoi.yy.model;

public class CarShowMessageBean {

    /**
     * 116座驾消息
     */
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

    public CarShowMessageBean() {

    }

    public static class DataBean {
        private String userName;//用户名称
        private String carCover;//座驾封面图片
        private String carUrl;//座驾动图效果
        private int grade;//财富等级
        private int uid;//用户id

        public DataBean(String userName, String carCover, String carUrl, int grade, int uid) {
            this.userName = userName;
            this.carCover = carCover;
            this.carUrl = carUrl;
            this.grade = grade;
            this.uid = uid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCarCover() {
            return carCover;
        }

        public void setCarCover(String carCover) {
            this.carCover = carCover;
        }

        public String getCarUrl() {
            return carUrl;
        }

        public void setCarUrl(String carUrl) {
            this.carUrl = carUrl;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }
}
