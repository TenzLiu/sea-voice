package com.jingtaoi.yy.bean;

import java.io.Serializable;
import java.util.List;

public class GiftBean implements Serializable {

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

    public static class DataBean implements Serializable {
        /**
         * createTime : 2019-02-13 15:07:25
         * day : 0
         * gold : 200
         * id : 1
         * img : http://uucj.oss-cn-beijing.aliyuncs.com/e1677552-acff-4a0d-91ae-77363ade3d72.jpg
         * imgFm : http://uucj.oss-cn-beijing.aliyuncs.com/e1677552-acff-4a0d-91ae-77363ade3d72.jpg
         * isDelete : 1
         * isState : 1
         * name : 测试礼物
         * sequence : 1
         * state : 1
         * status : 1
         * volume : 0
         * yz : 100
         */

        /**
         * createTime	string	时间
         * day	number	天数
         * gold	number	需要浪花
         * id	number	无
         * img	string	礼物图片
         * imgFm	string	l礼物封面
         * isDelete	number	1 否,2 是
         * isState	number	1 销售 ,2 下架
         * name	string	礼物名字
         * sequence	string	排序
         * state	number	1包裹，2普通礼物，3神奇礼物，4 专属礼物
         * status	number	是否是特价 1否，2是
         * volume	number	销量
         * yz	number	可兑换钻石
         */
        private String createTime;
        private int day;
        private int gold;
        private int id;
        private String img;
        private String imgFm;
        private int isDelete;
        private int isState;
        private String name;
        private String sequence;
        private int state;
        private int status;//是否是特价 1否，2是
        private int volume;
        private int yz;
        private boolean isChecked;//选中的礼物

        //1是显示，2是不显示  是否限时
        private int restrict;

        //1是显示，2是不显示  是否新的
        private int xin;
        private int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getRestrict() {
            return restrict;
        }

        public void setRestrict(int restrict) {
            this.restrict = restrict;
        }

        public int getXin() {
            return xin;
        }

        public void setXin(int xin) {
            this.xin = xin;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImgFm() {
            return imgFm;
        }

        public void setImgFm(String imgFm) {
            this.imgFm = imgFm;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public int getIsState() {
            return isState;
        }

        public void setIsState(int isState) {
            this.isState = isState;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
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

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public int getYz() {
            return yz;
        }

        public void setYz(int yz) {
            this.yz = yz;
        }
    }
}
