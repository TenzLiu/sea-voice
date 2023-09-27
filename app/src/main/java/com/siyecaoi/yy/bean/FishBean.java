package com.siyecaoi.yy.bean;

import android.widget.ImageView;

public class FishBean {
    //控件
    public ImageView imageView;
    //价格索引
    public int gold;

    public FishBean(ImageView imageView, int gold) {
        this.imageView = imageView;
        this.gold = gold;
    }
}
