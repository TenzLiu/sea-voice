package com.siyecaoi.yy.model;

/**
 * 100 频道聊天消息,104 红包消息，105 设置管理员，106 等级提升 ,109关注房主
 * 111 禁麦 ，112加入黑名单,114 抱他上麦，115
 */
public class ChatMessageBean {


    /**
     * code : 100 , 104 ，105，106，109,110,112,117
     * data : {"grade":0,"messageShow":"你好","name":"家有逗比小花喵！123456","uid":172}
     */

    private int code;
    private DataBean data;

    public ChatMessageBean(int code, DataBean data) {
        this.code = code;
        this.data = data;
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

    public static class DataBean {
        /**
         * grade : 0
         * messageShow : 你好
         * name : 家有逗比小花喵！123456
         * uid : 172
         */

        private int grade;//财富等级
        private String messageShow;//消息内容
        private String name;//名称
        private int uid;//用户id
        private int state;//消息状态（105  1 设置管理员，2 取消管理员）

        private String rid;//房间id(115 送礼物全屏通知消息)
        private String nickname;//房主名称(115 送礼物全屏通知消息)

        private Long cost;//礼物价值（122 探险全服通知）

        private int type;//类型

        public DataBean() {
        }

        public DataBean(int grade, String messageShow, String name, int uid) {
            this.grade = grade;
            this.messageShow = messageShow;
            this.name = name;
            this.uid = uid;
        }

        /**
         * 117 进房通知消息
         *
         * @param grade 等级
         * @param name  名称
         */
        public DataBean(int grade, String name, int uid) {
            this.grade = grade;
            this.name = name;
            this.uid = uid;
        }

        public Long getCost() {
            return cost;
        }

        public void setCost(Long cost) {
            this.cost = cost;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getMessageShow() {
            return messageShow;
        }

        public void setMessageShow(String messageShow) {
            this.messageShow = messageShow;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
