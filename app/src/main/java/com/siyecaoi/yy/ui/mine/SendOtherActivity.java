package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.ui.mine.fragment.SendOtherFragment;
import com.siyecaoi.yy.utils.Const;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 赠送他人道具或头环
 */
public class SendOtherActivity extends MyBaseActivity {
    @BindView(R.id.mTabLayout)
    SegmentTabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    FragPagerAdapter fragPagerAdapter;
    SendOtherFragment sendOtherFragment;

    int id;
    String nameShow;
    private int gold;

    @Override
    public void initData() {
        id = getBundleInt(Const.ShowIntent.ID, 0);
        gold = getBundleInt(Const.ShowIntent.GOLD, 0);
        nameShow = getBundleString(Const.ShowIntent.DATA);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add(getString(R.string.tv_friend_send));
//        titles.add(getString(R.string.tv_attention_send));
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_send_other);
    }

    @Override
    public void initView() {
        setTitleBarColor(R.color.white_color);
        setToobarColor(R.color.white_color);
        setTitleText("选择好友");
        setFrag();
    }

    private void setFrag() {
        for (int i = 0; i < titles.size(); i++) {
            sendOtherFragment = new SendOtherFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Const.ShowIntent.TYPE, i);
            bundle.putInt(Const.ShowIntent.ID, id);
            bundle.putInt(Const.ShowIntent.GOLD, gold);
            bundle.putString(Const.ShowIntent.DATA,nameShow);
            sendOtherFragment.setArguments(bundle);
            fragments.add(sendOtherFragment);
        }
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
