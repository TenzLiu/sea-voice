package com.siyecaoi.yy.bean;

import java.util.List;

public class VoiceHomeBean {

    private String msg;
    private int code;
    private DataBean data;
    private String sys;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public static class DataBean {

        private RoomBean room;
        private int minGift;//礼物显示的最小值
        private List<VoiceUserBean.DataBean> UserModel;

        public RoomBean getRoom() {
            return room;
        }

        public void setRoom(RoomBean room) {
            this.room = room;
        }

        public List<VoiceUserBean.DataBean> getUserModel() {
            return UserModel;
        }

        public void setUserModel(List<VoiceUserBean.DataBean> UserModel) {
            this.UserModel = UserModel;
        }

        public int getMinGift() {
            return minGift;
        }

        public void setMinGift(int minGift) {
            this.minGift = minGift;
        }

        public static class RoomBean {
            /**
             * bjImg : 123456
             * createTime : 2019-02-12 15:03:26
             * id : 1
             * isDelete : 1
             * isState : 1
             * lineNum : 1
             * name : 小小米
             * password : 123456
             * protocolNum : 25
             * rid : 59727172
             * roomCount : 萨达啊飒飒的
             * roomHint : 撒旦啊实打实的
             * roomLabel : 聊天
             * roomName : 小小米
             * roomTopic : 123
             * sequence : 0
             * state : 1
             * status : 1
             * type : 1
             * uid : 2
             */

            /**
             * bjImg	string	背景图片
             * createTime	string	时间
             * id	number	id
             * isDelete	number	是否被删除（1=否 2=是）
             * isState	number	是否屏蔽低礼物特效 1否， 2是
             * lineNum	number	在线人数
             * name	string	房主昵称
             * password	string	房间密码
             * protocolNum	number	协议号数
             * rid	string	房间id
             * roomCount	string	房间话题内容
             * roomHint	string	房间提示
             * roomLabel	string	房间标签
             * roomName	string	房间名称
             * roomTopic	string	房间话题
             * sequence	number	顺序
             * state	number	是否牌子房间 1否，2是
             * status	number	是否有效 1未锁定，2已锁定 (房间是否被锁定)
             * type	number	1是热门，2是女神，3是男神，4娱乐，5听歌，6相亲，7电台
             * uid	number	用户id
             */

            private String bjImg;
            private String bjName;//bjName	房间背景名称
            private String createTime;
            private int id;
            private int isDelete;
            private int isState;
            private int lineNum;
            private String name;
            private String password;
            private int protocolNum;
            private String rid;
            private String roomCount;
            private String roomHint;
            private String roomLabel;
            private String roomName;
            private String roomTopic;
            private int sequence;
            private int state;//是否牌子房间 1否，2是
            private int status;
            private int type;
            private int uid;
            private int isPk;//是否开启pk 1是未，2 是开启
            private int isJp;//	是否开启竞拍 1是未，2 是开启
            private int isGp;//公屏 1 开启， 2 关闭
            private int isPkState;//1正常关闭 2是主动关闭
            private int isfz;//房主是否在房间 1 在， 2不在
            private String mark;//房间官方语句。
            private String liang;//靓号
            private int RoomNum;

            public int getRoomNum() {
                return RoomNum;
            }

            public void setRoomNum(int roomNum) {
                RoomNum = roomNum;
            }

            public String getLiang() {
                return liang;
            }

            public void setLiang(String liang) {
                this.liang = liang;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getBjName() {
                return bjName;
            }

            public void setBjName(String bjName) {
                this.bjName = bjName;
            }

            public int getIsfz() {
                return isfz;
            }

            public void setIsfz(int isfz) {
                this.isfz = isfz;
            }

            public int getIsPkState() {
                return isPkState;
            }

            public void setIsPkState(int isPkState) {
                this.isPkState = isPkState;
            }

            public int getIsPk() {
                return isPk;
            }

            public void setIsPk(int isPk) {
                this.isPk = isPk;
            }

            public int getIsJp() {
                return isJp;
            }

            public void setIsJp(int isJp) {
                this.isJp = isJp;
            }

            public int getIsGp() {
                return isGp;
            }

            public void setIsGp(int isGp) {
                this.isGp = isGp;
            }

            public String getBjImg() {
                return bjImg;
            }

            public void setBjImg(String bjImg) {
                this.bjImg = bjImg;
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

            public int getLineNum() {
                return lineNum;
            }

            public void setLineNum(int lineNum) {
                this.lineNum = lineNum;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getProtocolNum() {
                return protocolNum;
            }

            public void setProtocolNum(int protocolNum) {
                this.protocolNum = protocolNum;
            }

            public String getRid() {
                return rid;
            }

            public void setRid(String rid) {
                this.rid = rid;
            }

            public String getRoomCount() {
                return roomCount;
            }

            public void setRoomCount(String roomCount) {
                this.roomCount = roomCount;
            }

            public String getRoomHint() {
                return roomHint;
            }

            public void setRoomHint(String roomHint) {
                this.roomHint = roomHint;
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

            public String getRoomTopic() {
                return roomTopic;
            }

            public void setRoomTopic(String roomTopic) {
                this.roomTopic = roomTopic;
            }

            public int getSequence() {
                return sequence;
            }

            public void setSequence(int sequence) {
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }

    }
}
