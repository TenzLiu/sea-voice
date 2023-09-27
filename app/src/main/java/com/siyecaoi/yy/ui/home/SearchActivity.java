package com.siyecaoi.yy.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.ui.home.fragment.SearchFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends MyBaseActivity {
    @BindView(R.id.mTabLayout_home)
    SlidingTabLayout mTabLayoutHome;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.back_iv)
    ImageView back_iv;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search);
    }

    FragPagerAdapter fragPagerAdapter;
    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    @Override
    public void initView() {
        showTitle(false);
//        setTitleText("搜索");
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("找房间");
        titles.add("找朋友");
        fragments.add(SearchFragment.getInstance(1));
        fragments.add(SearchFragment.getInstance(2));
        fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        viewPager.setAdapter(fragPagerAdapter);
        mTabLayoutHome.setViewPager(viewPager);
        mTabLayoutHome.setCurrentTab(0);


        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
