package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 赠送页面
 */
public class SendActivity extends MyBaseActivity {
    @BindView(R.id.mTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_tablayout);
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
