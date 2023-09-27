package com.siyecaoi.yy.ui.find;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 广播交友页面
 */
public class PalActivity extends MyBaseActivity {
    @BindView(R.id.frame_pal)
    FrameLayout framePal;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_pal);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
