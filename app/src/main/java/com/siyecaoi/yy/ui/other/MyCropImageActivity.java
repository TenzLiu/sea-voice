package com.siyecaoi.yy.ui.other;

import android.annotation.SuppressLint;
import android.os.Bundle;


import com.siyecaoi.yy.utils.ActivityCollector;

import cn.sinata.xldutils.activitys.CropImageActivity;

/**
 * Created by Administrator on 2018/8/30.
 */

@SuppressLint("Registered")
public class MyCropImageActivity extends CropImageActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this, getClass());
        ActivityCollector.getActivityCollector().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        ActivityCollector.getActivityCollector().finishActivity(this);
    }
}
