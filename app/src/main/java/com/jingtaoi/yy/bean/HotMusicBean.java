package com.jingtaoi.yy.bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class HotMusicBean {

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

    public static class DataBean extends LitePalSupport {
        /**
         * createDate : 2019-03-05 14:59:22
         * gsNane : 5月天
         * id : 2
         * isDelete : 1
         * musicName : 突然好想你
         * state : 2
         * uid : 101
         * url : cccc
         */

        /**
         * createDate	string	时间
         * gsNane	string	歌手名称
         * id	number	id
         * isDelete	number	是否删除
         * musicName	string	歌曲名称
         * state	number	是否是通过
         * uid	number	上传用户id
         * url	string	音乐路径
         */
        private String createDate;
        private String gsNane;
        private int id;
        private int isDelete;
        private String musicName;
        private int state;
        private int uid;
        private String url;
        private int dataLenth;//歌曲长度

        private int musicState;//歌曲状态  1 已下载或已添加  2播放   3 暂停

        private String objectKey;//下载地址

        private int times;//音乐时长（s）

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public String getObjectKey() {
            return objectKey;
        }

        public void setObjectKey(String objectKey) {
            this.objectKey = objectKey;
        }

        public int getMusicState() {
            return musicState;
        }

        public void setMusicState(int musicState) {
            this.musicState = musicState;
        }

        public int getDataLenth() {
            return dataLenth;
        }

        public void setDataLenth(int dataLenth) {
            this.dataLenth = dataLenth;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getGsNane() {
            return gsNane;
        }

        public void setGsNane(String gsNane) {
            this.gsNane = gsNane;
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

        public String getMusicName() {
            return musicName;
        }

        public void setMusicName(String musicName) {
            this.musicName = musicName;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
