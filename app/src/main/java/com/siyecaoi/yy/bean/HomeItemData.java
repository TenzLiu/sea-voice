package com.siyecaoi.yy.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class HomeItemData implements MultiItemEntity {
    public static final int TYPE_ITEM1 = 1;
    public static final int TYPE_ITEM2 = 2;
    /**
     * img : http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png
     * roomLabel : 聊天
     * roomName : 18382412222
     * uid : 155
     * userName : 18382412222
     * usercoding : 72634891
     */
    private int itemType=TYPE_ITEM2;
    private String img;
    private String roomLabel;
    private String roomName;
    private String name;
    private int uid;
    private int type; //1在是房间，2是不在房间
    private int num;
    private int voiceTime;
    private int manNum;
    private int treasureGrade;//财富
    private String liang;

    public String getLiang() {
        return liang;
    }

    public void setLiang(String liang) {
        this.liang = liang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getManNum() {
        return manNum;
    }

    public void setManNum(int manNum) {
        this.manNum = manNum;
    }

    public int getTreasureGrade() {
        return treasureGrade;
    }

    public void setTreasureGrade(int treasureGrade) {
        this.treasureGrade = treasureGrade;
    }

    public int getCharmGrade() {
        return charmGrade;
    }

    public void setCharmGrade(int charmGrade) {
        this.charmGrade = charmGrade;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(int voiceTime) {
        this.voiceTime = voiceTime;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getIndividuation() {
        return individuation;
    }

    public void setIndividuation(String individuation) {
        this.individuation = individuation;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    private int charmGrade; //魅力
    private int sex;
    private int id;
    private Double distance;
    private String userName;
    private String individuation;
    private String voice;
    private String usercoding;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRoomLabel() {
        return roomLabel;
    }

    public void setRoomLabel(String roomLabel) {
        this.roomLabel = roomLabel;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUsercoding() {
        return usercoding;
    }

    public void setUsercoding(String usercoding) {
        this.usercoding = usercoding;
    }


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }


    @Override
    public int getItemType() {
        return itemType;
    }
}