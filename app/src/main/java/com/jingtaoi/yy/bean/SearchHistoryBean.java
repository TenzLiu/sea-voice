package com.jingtaoi.yy.bean;

import java.util.List;

public class SearchHistoryBean {
    /**
     * count : 0
     * data : [{"name":"","time":10000000000}]
     */

    private int count;
    private List<DataBean> data;

    public SearchHistoryBean(int count, List<DataBean> data) {
        this.count = count;
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        public DataBean(String name) {
            this.name = name;
        }

        private String name;
        private long time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
