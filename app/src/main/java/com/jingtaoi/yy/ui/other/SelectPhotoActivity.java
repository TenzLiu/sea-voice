package com.jingtaoi.yy.ui.other;

import android.os.Bundle;


import com.jingtaoi.yy.utils.ActivityCollector;

import cn.sinata.xldutils.activitys.SelectPhotoDialog;

/**
 * 选择图片页面
 * Created by Administrator on 2018/1/18.
 */

public class SelectPhotoActivity extends SelectPhotoDialog {

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
