package com.jingtaoi.yy.bean;

import java.util.List;

public class PersonalHomeBean {

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
         * 图片model
         * 用户model
         * 道具model
         */
        private List<ImgEntity> img;
        private UserEntity user;
        private List<SceneEntity> scene;
        private RoomEntity room;

        public RoomEntity getRoom() {
            return room;
        }

        public void setRoom(RoomEntity room) {
            this.room = room;
        }

        public void setImg(List<ImgEntity> img) {
            this.img = img;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public void setScene(List<SceneEntity> scene) {
            this.scene = scene;
        }

        public List<ImgEntity> getImg() {
            return img;
        }

        public UserEntity getUser() {
            return user;
        }

        public List<SceneEntity> getScene() {
            return scene;
        }

        public class RoomEntity{
            private String rid;
            private String rimg;
            private String rname;

            public String getRid() {
                return rid;
            }

            public void setRid(String rid) {
                this.rid = rid;
            }

            public String getRimg() {
                return rimg;
            }

            public void setRimg(String rimg) {
                this.rimg = rimg;
            }

            public String getRname() {
                return rname;
            }

            public void setRname(String rname) {
                this.rname = rname;
            }
        }

        public class UserEntity {
            /**
             * attentionNum : 1
             * voice : x
             * fansNum : 0
             * usercoding : 14326798
             * constellation : 双鱼
             * treasureGrade : 11
             * individuation : x
             * sex : 2
             * nickname : 小小鸨鸨
             * charmGrade : 6
             * imgTx : http://thirdqq.qlogo.cn/g?b=oidb&k=yVTv3iaRNxNUeMjWeUwVeLA&s=100
             * <p>
             * attentionNum	number	关注数
             * voice	string	声音签名
             * fansNum	number	粉丝数
             * usercoding	string	用户 深海号
             * constellation	string	星座
             * treasureGrade	number	财富等级
             * individuation	string	个性签名
             * sex	number	性别
             * nickname	string	昵称
             * charmGrade	number	魅力等级
             * imgTx	string	头像
             */
            private int attentionNum;
            private String voice;
            private int fansNum;
            private String usercoding;
            private String constellation;
            private int treasureGrade;
            private String individuation;
            private int sex;
            private String nickname;
            private int charmGrade;
            private String imgTx;
            private int isRoom;//是否开启房间 1未开启，2已开启
            private String liang;

            public String getLiang() {
                return liang;
            }

            public void setLiang(String liang) {
                this.liang = liang;
            }

            public int getIsRoom() {
                return isRoom;
            }

            public void setIsRoom(int isRoom) {
                this.isRoom = isRoom;
            }

            public void setAttentionNum(int attentionNum) {
                this.attentionNum = attentionNum;
            }

            public void setVoice(String voice) {
                this.voice = voice;
            }

            public void setFansNum(int fansNum) {
                this.fansNum = fansNum;
            }

            public void setUsercoding(String usercoding) {
                this.usercoding = usercoding;
            }

            public void setConstellation(String constellation) {
                this.constellation = constellation;
            }

            public void setTreasureGrade(int treasureGrade) {
                this.treasureGrade = treasureGrade;
            }

            public void setIndividuation(String individuation) {
                this.individuation = individuation;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setCharmGrade(int charmGrade) {
                this.charmGrade = charmGrade;
            }

            public void setImgTx(String imgTx) {
                this.imgTx = imgTx;
            }

            public int getAttentionNum() {
                return attentionNum;
            }

            public String getVoice() {
                return voice;
            }

            public int getFansNum() {
                return fansNum;
            }

            public String getUsercoding() {
                return usercoding;
            }

            public String getConstellation() {
                return constellation;
            }

            public int getTreasureGrade() {
                return treasureGrade;
            }

            public String getIndividuation() {
                return individuation;
            }

            public int getSex() {
                return sex;
            }

            public String getNickname() {
                return nickname;
            }

            public int getCharmGrade() {
                return charmGrade;
            }

            public String getImgTx() {
                return imgTx;
            }
        }

        public class SceneEntity {
            /**
             * img :
             * name : xx
             * state : 1
             * <p>
             * imgFm	string	道具封面
             * img	string	道具图片
             * name	string	名称
             * state	number	是否使用  1未使用  2是使用
             */
            private String imgFm;
            private String img;
            private String name;
            private int state;
            private int type;//	1座驾，2 是头环
            private int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getImgFm() {
                return imgFm;
            }

            public void setImgFm(String imgFm) {
                this.imgFm = imgFm;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getImg() {
                return img;
            }

            public String getName() {
                return name;
            }

            public int getState() {
                return state;
            }
        }
    }
}
