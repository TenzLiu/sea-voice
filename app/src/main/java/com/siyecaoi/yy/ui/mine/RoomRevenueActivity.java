package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.ui.mine.fragment.HeadPortraitStroeFragment;
import com.siyecaoi.yy.ui.mine.fragment.RoomRevenueFragment;
import com.siyecaoi.yy.ui.mine.fragment.StroeFragment;
import com.siyecaoi.yy.utils.Const;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomRevenueActivity extends MyBaseActivity {

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.iv_back)
    ImageView iv_back;


    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    FragPagerAdapter fragPagerAdapter;
    StroeFragment stroeFragment;
    HeadPortraitStroeFragment portraitStroeFragment;

    @BindView(R.id.mTabLayout)
    SlidingTabLayout mTabLayout;
    @Override
    public void initData() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("个人收益");
        titles.add("房间返点");
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_roomrevenue);
    }

    @Override
    public void initView() {
        showTitle(false);
        showHeader(false);
        setBlcakShow(false);
        setTitleText("我的收益");
        iv_back.setOnClickListener(v->{
            finish();
        });
        setTitleBarColor(R.color.fjsy_color);
        setToobarColor(R.color.fjsy_color);
        showToolbarLine(View.GONE);
        setFrag();
    }

    private void setFrag() {

        RoomRevenueFragment fragment1=new RoomRevenueFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(Const.ShowIntent.TYPE, 1);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);


        RoomRevenueFragment fragment2=new RoomRevenueFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(Const.ShowIntent.TYPE, 2);
        fragment2.setArguments(bundle2);
        fragments.add(fragment2);

        fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPager.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();

        String[] strs = titles.toArray(new String[titles.size()]);
//        mTabLayout.setTabData(strs);
        mTabLayout.setViewPager(mViewPager,strs);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(0);
        mTabLayout.setCurrentTab(0);
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
