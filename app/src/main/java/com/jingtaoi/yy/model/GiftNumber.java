package com.jingtaoi.yy.model;

public class GiftNumber {
    private int numberShow;//数量
    private String desShow;//描述

    public GiftNumber(int numberShow, String desShow) {
        this.numberShow = numberShow;
        this.desShow = desShow;
    }

    public int getNumberShow() {
        return numberShow;
    }

    public void setNumberShow(int numberShow) {
        this.numberShow = numberShow;
    }

    public String getDesShow() {
        return desShow;
    }

    public void setDesShow(String desShow) {
        this.desShow = desShow;
    }
}
