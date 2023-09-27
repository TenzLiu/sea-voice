package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.ui.mine.fragment.CharmFragment;
import com.siyecaoi.yy.ui.mine.fragment.GradeFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的等级页面
 */
public class GradeActivity extends MyBaseActivity {

    @BindView(R.id.mTabLayout_grade)
    SlidingTabLayout mTabLayoutGrade;
    @BindView(R.id.iv_share_grade)
    ImageView ivShareGrade;
    @BindView(R.id.mViewPager_grade)
    ViewPager mViewPagerGrade;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    FragPagerAdapter fragPagerAdapter;
    GradeFragment gradeFragment;
    CharmFragment charmFragment;

    @Override
    public void initData() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add(getString(R.string.tv_cai_grade));
        titles.add(getString(R.string.tv_mei_grade));
    }

    @Override
    public void setContentView() {

        setContentView(R.layout.activity_grade);
    }

    @Override
    public void initView() {
        setTitleText("我的等级");
        showTitle(false);
        showHeader(false);
        showToolbarLine(View.GONE);
        setFrag();
        setBlcakShow(false);
        iv_back.setOnClickListener(v->{
            finish();
        });
    }

    private void setFrag() {
        gradeFragment = new GradeFragment();
        charmFragment = new CharmFragment();
        fragments.add(gradeFragment);
        fragments.add(charmFragment);
        fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPagerGrade.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();
        mTabLayoutGrade.setViewPager(mViewPagerGrade);
        mViewPagerGrade.setCurrentItem(0);
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
