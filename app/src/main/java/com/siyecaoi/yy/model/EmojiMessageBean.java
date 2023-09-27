package com.siyecaoi.yy.model;

/**
 * 表情消息  108
 */
public class EmojiMessageBean {

    private int code;
    private DataBean data;

    public EmojiMessageBean(int code, DataBean data) {
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
        private int emojiCode;//表情的code码
        private String name;//名称
        private int uid;//用户id
        private int numberShow;//(几个选项的最后结果)
        private String imageName;//表情图片文件名

        public DataBean(int grade, int emojiCode, String name, int uid, int numberShow,String imageName) {
            this.grade = grade;
            this.emojiCode = emojiCode;
            this.name = name;
            this.uid = uid;
            this.numberShow = numberShow;
            this.imageName = imageName;
        }

        public int getNumberShow() {
            return numberShow;
        }

        public void setNumberShow(int numberShow) {
            this.numberShow = numberShow;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getEmojiCode() {
            return emojiCode;
        }

        public void setEmojiCode(int emojiCode) {
            this.emojiCode = emojiCode;
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
    }
}
