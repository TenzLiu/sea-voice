package com.jingtaoi.yy.bean;

import java.util.List;

public class MyattentionBean {

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
         * img : http://thirdqq.qlogo.cn/g?b=oidb&k=1tb7tEdrLaPniasRgbz4yyw&s=100
         * name : 家有逗比小花喵！123456
         * id : 172
         * <p>
         * id	number	用户id
         * img	string	用户头像
         * name	string	用户昵称
         */
        private String img;
        private String name;
        private int id;
        private int state;//在关注列表判断是否在房间 1 未在房间，2 在房间   在粉丝列表 是否关注 1未关注，2已关注

        private boolean isSelect;//判断当前用户是否选中
        private boolean isSend;//判断是否已邀请

        private boolean isSendSuccescc;//判断赠送头饰或座驾成功

        public boolean isSendSuccescc() {
            return isSendSuccescc;
        }

        public void setSendSuccescc(boolean sendSuccescc) {
            isSendSuccescc = sendSuccescc;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean isSend() {
            return isSend;
        }

        public void setSend(boolean send) {
            isSend = send;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}
