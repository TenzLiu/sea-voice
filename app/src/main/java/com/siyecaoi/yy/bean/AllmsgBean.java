package com.siyecaoi.yy.bean;

import java.util.List;

public class AllmsgBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : [{"uid":405,"createTime":"2019-09-18 09:46:49","id":1,"content":"哈哈"}]
     * sys : 1568771382343
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
         * |参数|类型|描述|
         * |:———-|:———-|:———-|
         * | charmGrade | number| 魅力等级 |
         * | content | string| 内容 |
         * | createTime | string| 时间 |
         * | id | number| 无 |
         * | treasureGrade | number| 财富等级 |
         * | uid | number| 用户id |
         * | userName | string| 用户 |
         * | imgTx | string| 用户头像 |
         * | rid | string| 房间号 |
         *
         */
        private int charmGrade;
        private String content;
        private String createTime;
        private int id;
        private int treasureGrade;
        private int uid;
        private String userName;
        private String imgTx;
        private String rid;

        public int getCharmGrade() {
            return charmGrade;
        }

        public void setCharmGrade(int charmGrade) {
            this.charmGrade = charmGrade;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

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

        public int getTreasureGrade() {
            return treasureGrade;
        }

        public void setTreasureGrade(int treasureGrade) {
            this.treasureGrade = treasureGrade;
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

        public String getImgTx() {
            return imgTx;
        }

        public void setImgTx(String imgTx) {
            this.imgTx = imgTx;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }
    }
}
