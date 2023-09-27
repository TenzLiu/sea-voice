package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.ui.mine.fragment.HeadPortraitStroeFragment;
import com.siyecaoi.yy.ui.mine.fragment.StroeFragment;
import com.siyecaoi.yy.utils.Const;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 道具商城
 */
public class StoreActivity extends MyBaseActivity {

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;


    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    FragPagerAdapter fragPagerAdapter;
    StroeFragment stroeFragment;
    HeadPortraitStroeFragment portraitStroeFragment;

    @BindView(R.id.mTabLayout)
     SegmentTabLayout mTabLayout;
    @Override
    public void initData() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add(getString(R.string.tv_tou_store));
        titles.add(getString(R.string.tv_zuo_store));
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_store_tablayout);
    }

    @Override
    public void initView() {
        showTitle(false);
        showHeader(false);
        showToolbarLine(View.GONE);
        setFrag();
        setBlcakShow(false);
        findViewById(R.id.tv_left).setOnClickListener(v->{
            finish();
        });
    }

    private void setFrag() {
//        for (int i = 0; i < titles.size(); i++) {
//            stroeFragment = new StroeFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt(Const.ShowIntent.TYPE, i);
//            stroeFragment.setArguments(bundle);
//            fragments.add(stroeFragment);
//        }
        portraitStroeFragment = new HeadPortraitStroeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.TYPE, 0);
        portraitStroeFragment.setArguments(bundle);
        fragments.add(portraitStroeFragment);


        stroeFragment = new StroeFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(Const.ShowIntent.TYPE, 1);
        stroeFragment.setArguments(bundle1);
        fragments.add(stroeFragment);


        fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPager.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();

        String[] strs = titles.toArray(new String[titles.size()]);
        mTabLayout.setTabData(strs);
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
