package com.siyecaoi.yy.bean;

import java.util.List;

public class SearchHotKeyBean {
    /**
     * msg : 获取成功
     * code : 0
     * data : [{"createTime":"2019-03-12 20:02:31","id":1,"isDelete":1,"name":"你好1","num":0,"state":1,"status":2}]
     * sys : 1556005142131
     */

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
         * createTime : 2019-03-12 20:02:31
         * id : 1
         * isDelete : 1
         * name : 你好1
         * num : 0
         * state : 1
         * status : 2
         */

        private String createTime;
        private int id;
        private int isDelete;
        private String name;
        private int num;
        private int state;
        private int status;

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

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
