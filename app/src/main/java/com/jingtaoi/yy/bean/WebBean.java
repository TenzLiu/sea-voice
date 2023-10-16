package com.jingtaoi.yy.bean;

public class WebBean {

    private String msg;
    private int code;
    private DataEntity data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
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

    public DataEntity getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class DataEntity {
        /**
         * id : 1
         * type : 1
         * content : 123.html
         * 1推荐位，2 广播说明, 3 邀请大作战 ，4 奖励秘籍，5实名认证，6实名认证说明,7 5星好评，
         * 8 深海使用手册,9用户协议,10公众号送浪花,11提现规则,12财富等级说明,13魅力等级说明
         */
        private int id;
        private int type;
        private String content;

        public void setId(int id) {
            this.id = id;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public int getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }
}
