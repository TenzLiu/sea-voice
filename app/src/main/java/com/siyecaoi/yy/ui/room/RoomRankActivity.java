package com.siyecaoi.yy.ui.room;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.ui.room.fragment.RoomRankFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoomRankActivity extends MyBaseActivity {
    @BindView(R.id.mTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @BindView(R.id.root_view)
    ConstraintLayout root_view;
    String roomId;
    int userRoomType;

    @Override
    public void initData() {
        roomId = getBundleString("roomId");
        userRoomType = getBundleInt("userRoomType", -1);
    }


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_rank);
        showTitle(false);
        setTitleBarColor(R.color.toolbar_color1);
    }

    @Override
    public void initView() {
        String[] title = {"豪气榜","魅力榜"};
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RoomRankFragment.newInstance(1, roomId, userRoomType));
        fragments.add(RoomRankFragment.newInstance(2, roomId, userRoomType));



        mTabLayout.setViewPager(mViewPager, title, this, fragments);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if (i == 0) {


                    mTabLayout.setIndicatorColor(Color.parseColor("#9871FF"));
//                    mTabLayout.setTextSelectColor(Color.parseColor("#A28036"));
//                    mTabLayout.notifyDataSetChanged();
                    setTitleBarColor(R.color.toolbar_color1);
                    root_view.setBackgroundResource(R.drawable.home_page_color3);
                } else if (i == 1) {
                    mTabLayout.setIndicatorColor(Color.parseColor("#FABC42"));
//                    mTabLayout.setTextSelectColor(Color.parseColor("#7E67A0"));
//                    mTabLayout.notifyDataSetChanged();
                    setTitleBarColor(R.color.toolbar_color);
                    root_view.setBackgroundResource(R.drawable.home_page_color2);

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

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

    @OnClick({R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
