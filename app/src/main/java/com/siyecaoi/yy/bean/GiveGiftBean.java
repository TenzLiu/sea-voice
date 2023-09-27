package com.siyecaoi.yy.bean;

import java.util.List;

public class GiveGiftBean {

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
         * "buid":3,
         *             "createTime":"2020-12-03 00:30:24",
         *             "gid":59,
         *             "id":12814,
         *             "imgFm":"http://obs.siyecaoi.com/admin/21c09f496a7f47c49f9badd18e137ce6.png",
         *             "nickname":"程一电台",
         *             "num":1,
         *             "rid":"91054682",
         *             "uid":3166,
         *             "usercoding":"91054682"
         *         }
         */

        private int buid;
        private String createtime;
        private int gid;
        private int id;
        private String imgFm;
        private String nickname;
        private int num;
        private String rid;
        private int uid;
        private String usercoding;

        public int getBuid() {
            return buid;
        }

        public void setBuid(int buid) {
            this.buid = buid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgFm() {
            return imgFm;
        }

        public void setImgFm(String imgFm) {
            this.imgFm = imgFm;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUsercoding() {
            return usercoding;
        }

        public void setUsercoding(String usercoding) {
            this.usercoding = usercoding;
        }
    }
}
