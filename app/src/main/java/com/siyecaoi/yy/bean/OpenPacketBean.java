package com.siyecaoi.yy.bean;

import java.util.List;

public class OpenPacketBean {

    private String img;//头像
    private String name;//昵称
    private List<RedNumBean> RedNum;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RedNumBean> getRedNum() {
        return RedNum;
    }

    public void setRedNum(List<RedNumBean> RedNum) {
        this.RedNum = RedNum;
    }

    public static class RedNumBean {
        /**
         * createTime : 1554369033000
         * gold : 1
         * id : 367
         * redId : 164
         * rid : 14326798
         * state : 1
         * status : 1
         * uid : 170
         */

        /**
         * createTime	string	无
         * gold	number	浪花
         * id	number	这条数据id
         * redId	number	红包的id
         * rid	string	房间id
         * state	number	是否被定时器 1否，2是
         * status	number	是否被领取红包 1 未，2 是
         * uid	number	被送用户id
         */
        private long createTime;
        private int gold;
        private int id;
        private int redId;
        private String rid;
        private int state;
        private int status;
        private int uid;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
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

        public int getRedId() {
            return redId;
        }

        public void setRedId(int redId) {
            this.redId = redId;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
