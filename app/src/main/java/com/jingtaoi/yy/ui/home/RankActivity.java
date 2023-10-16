package com.jingtaoi.yy.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.ui.home.fragment.RankFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RankActivity extends MyBaseActivity {
    @BindView(R.id.mTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @BindView(R.id.root_view)
    ConstraintLayout root_view;

    @Override
    public void initData() {
    }


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_rank);
        showTitle(false);
    }

    @Override
    public void initView() {
        showHeader(false);
        String[] title = {"豪气榜","魅力榜"};
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RankFragment.newInstance(1));
        fragments.add(RankFragment.newInstance(2));

        setBlcakShow(false);

        mTabLayout.setViewPager(mViewPager, title, this, fragments);
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

    @OnClick({R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
