package com.siyecaoi.yy.model;

import java.util.List;

public class EmojiList {

    List<DataBean> emoji_list;

    public List<DataBean> getEmoji_list() {
        return emoji_list;
    }

    public void setEmoji_list(List<DataBean> emoji_list) {
        this.emoji_list = emoji_list;
    }

    public static class DataBean {
        private String name;
        private int unicode;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUnicode() {
            return unicode;
        }

        public void setUnicode(int unicode) {
            this.unicode = unicode;
        }
    }
}
