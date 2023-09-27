package com.siyecaoi.yy.ui.message;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.ui.message.fragment.MyAttentionFragment;
import com.siyecaoi.yy.ui.message.fragment.MyFansFragment;
import com.siyecaoi.yy.ui.message.fragment.NewFriendFragment;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

public class MailListActivity extends MyBaseActivity {

    @BindView(R.id.mViewPager_message)
    ViewPager mViewPagerMessageD;

    @BindView(R.id.mTabLayout_message_d)
    SegmentTabLayout mTabLayoutMessageD;
    private ArrayList<String> titles;
    private ArrayList<Fragment>  fragments;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_maillist);
    }




    @Override
    public void initView() {
        setTitleText("通讯录");
        setToobarColor(R.color.msg_color);
        setTitleBarColor(R.color.msg_color);
        setLeftImg(getResources().getDrawable(R.drawable.icon_back1));
        setFrag();
    }


    private void setFrag() {
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        titles.add("好友");
        titles.add("关注");
        titles.add("粉丝");
        NewFriendFragment friendFragment1 = new NewFriendFragment();

        MyAttentionFragment friendFragment2 = new MyAttentionFragment();
        MyFansFragment friendFragment3 = new MyFansFragment();
        fragments.add(friendFragment1);
        fragments.add(friendFragment2);
        fragments.add(friendFragment3);
        FragPagerAdapter  fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPagerMessageD.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();

        String[] strs = titles.toArray(new String[titles.size()]);
        mTabLayoutMessageD.setTabData(strs);
        mTabLayoutMessageD.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPagerMessageD.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPagerMessageD.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayoutMessageD.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPagerMessageD.setCurrentItem(0);
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }


}
