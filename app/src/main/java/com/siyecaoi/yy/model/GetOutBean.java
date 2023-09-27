package com.siyecaoi.yy.model;

public class GetOutBean {
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

    public class DataBean {
        //用户id
        private Integer uid;
        //用户名称
        private String name;
        //用户财富等级
        private Integer grade;

        //用户在房间里等级 1房主，2房管， 3用户
        private  Integer state;

        //被踢用户id
        private Integer buid;

        //被踢用户名字
        private String bname;

        //被踢用户的财富等级
        private Integer bgrade;

        //被踢用户在房间里等级 1房主，2房管， 3用户
        private  Integer bstate;

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getGrade() {
            return grade;
        }

        public void setGrade(Integer grade) {
            this.grade = grade;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public Integer getBuid() {
            return buid;
        }

        public void setBuid(Integer buid) {
            this.buid = buid;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public Integer getBgrade() {
            return bgrade;
        }

        public void setBgrade(Integer bgrade) {
            this.bgrade = bgrade;
        }

        public Integer getBstate() {
            return bstate;
        }

        public void setBstate(Integer bstate) {
            this.bstate = bstate;
        }
    }
}
