package com.siyecaoi.yy.bean;

import java.util.ArrayList;

public class RealtimeListBean {
    public String msg;
    public String sys;
    public int code;
    public RealtimeDataBean data;
    public static class RealtimeDataBean{
        public double num;
        public ArrayList<GiftBean> list;
    }

    public static class GiftBean{
        public String giftImg;
        public String giftCost;
        public String giftName;
        public int giftNum;
        public static GiftBean newEmpty(){
            GiftBean giftBean = new GiftBean();
            giftBean.giftImg = "";
            giftBean.giftCost = "";
            return giftBean;
        }
    }
}
