package com.siyecaoi.yy.ui.home.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.ui.MainActivity;
import com.siyecaoi.yy.ui.home.SearchActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.view.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/12/19.
 */

public class HomeFragment extends MyBaseFragment {


    FragPagerAdapter fragPagerAdapter;
    @BindView(R.id.mTabLayout_home)
    SlidingTabLayout mTabLayoutHome;
    @BindView(R.id.mViewPager_home)
    ViewPager mViewPagerHome;


    @BindView(R.id.root_view)
    LinearLayout rootView;


    Unbinder unbinder;
    ArrayList<String> titles;
    ArrayList<Fragment> fragments;
    HomeHotFragment hotHomeFragment;//热门
//    MatchHomeFragment matchHomeFragment;//速配
    private int selectTab;
    private MainActivity activity;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void initView() {
        activity = (MainActivity) getActivity();
        initFrag();
    }

    private void initFrag() {
        //1是热门，2是陪玩，4娱乐，5听歌，6相亲，7电台
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add(getString(R.string.tv_hot_home));

        titles.add(getString(R.string.tv_pei_home));
//        titles.add(getString(R.string.tv_match_home));

        titles.add(getString(R.string.tv_radio_home));
//        titles.add(getString(R.string.tv_rec_home));
        titles.add("个人");

//        titles.add(getString(R.string.tv_rec_home));3
//        titles.add(getString(R.string.tv_music_home));4
//        titles.add(getString(R.string.tv_miai_home));5
//        titles.add(getString(R.string.tv_city_home));6
//        titles.add(getString(R.string.tv_radio_home));7
        //热门
        hotHomeFragment = new HomeHotFragment();
        fragments.add(hotHomeFragment);

        //陪玩
        Fragment otherFragment1 = new OtherFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(Const.ShowIntent.TYPE, 2);
        otherFragment1.setArguments(bundle1);
        fragments.add(otherFragment1);

//        速配
//        matchHomeFragment = new MatchHomeFragment();
//        fragments.add(matchHomeFragment);

        //电台
        Fragment otherFragment2 = new OtherFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(Const.ShowIntent.TYPE, 7);
        otherFragment2.setArguments(bundle2);
        fragments.add(otherFragment2);

        //游戏
//        Fragment otherFragment = new OtherFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(Const.ShowIntent.TYPE, 3);
//        otherFragment.setArguments(bundle);
//        fragments.add(otherFragment);


        //个人
        Fragment personalFragment = new HomePersonalFragment();
        fragments.add(personalFragment);

//        for (int i = 3; i < titles.size(); i++) {
//            Fragment otherFragment = new OtherFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt(Const.ShowIntent.TYPE, i);
//            otherFragment.setArguments(bundle);
//            fragments.add(otherFragment);
//        }

        setFrag();
    }

    private void setFrag() {
        fragPagerAdapter = new FragPagerAdapter(getChildFragmentManager());

        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPagerHome.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();
        mTabLayoutHome.setViewPager(mViewPagerHome);
        mViewPagerHome.setOffscreenPageLimit(5);
        mViewPagerHome.setCurrentItem(0);
        mViewPagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mTabLayoutHome.getTitleView(selectTab).setTextSize(16);
                mTabLayoutHome.getTitleView(selectTab).setTypeface(Typeface.DEFAULT);

                mTabLayoutHome.getTitleView(i).setTextSize(18);
                mTabLayoutHome.getTitleView(i).setTypeface(Typeface.DEFAULT_BOLD);

                selectTab=i;

//                if (i != 1) {
//                    matchHomeFragment.stopPlay();
//                }

//                if (i==0){
//                    rootView.setBackgroundResource(R.drawable.home_page_color1);
//                }else if (i==1){
//                    rootView.setBackgroundResource(R.drawable.home_page_color2);
//                    activity.getHomeMainTab().setBackgroundResource(R.color.bottm_color2);
//                }else {
//                    rootView.setBackgroundResource(R.drawable.home_page_color1);
//                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

//        mTabLayoutHome.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//            }
//        });

        selectTab = 0;
        mTabLayoutHome.getTitleView(0).setTextSize(18);
        mTabLayoutHome.getTitleView(0).setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }


    public void setMainTab(){
//        if (selectTab==0){
//            if (activity!=null)
//            activity.getHomeMainTab().setBackgroundResource(R.color.bottm_color1);
//        }else if (selectTab==1){
//            if (activity!=null)
//            activity.getHomeMainTab().setBackgroundResource(R.color.bottm_color2);
//        }else {
//            if (activity!=null)
//            activity.getHomeMainTab().setBackgroundResource(R.color.bottm_color1);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.search_ll)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }


    @OnClick(R.id.my_home_ll)
    public void onmyHomeViewClicked() {
        String roomId = (String) SharedPreferenceUtils.get(getContext(), Const.User.ROOMID, "");
        gotoRoom(roomId);
    }

    private void gotoRoom(String roomId) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ROOMID, roomId);
        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
    }

}
