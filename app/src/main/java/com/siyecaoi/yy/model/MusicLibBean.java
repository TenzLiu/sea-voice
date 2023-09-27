package com.siyecaoi.yy.model;

import org.litepal.crud.LitePalSupport;

public class MusicLibBean extends LitePalSupport {


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
    private int musicId; //音乐id

    private int musicState;//歌曲状态  0未播放  1 已添加   2 播放  3暂停

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
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

    public int getDataLenth() {
        return dataLenth;
    }

    public void setDataLenth(int dataLenth) {
        this.dataLenth = dataLenth;
    }

    public int getMusicState() {
        return musicState;
    }

    public void setMusicState(int musicState) {
        this.musicState = musicState;
    }
}
