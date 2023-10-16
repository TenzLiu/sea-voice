package com.jingtaoi.yy.ui.mine;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.adapter.FragPagerAdapter;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.dialog.MyBirthdayDialog;
import com.jingtaoi.yy.ui.mine.fragment.GiftHisFragment;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.MyUtils;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 礼物记录页面
 */
public class GiftHisActivity extends MyBaseActivity {
    @BindView(R.id.mTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    FragPagerAdapter fragPagerAdapter;
    GiftHisFragment giftHisFragment;
    GiftHisFragment giftHisFragment1;

    Calendar calendar;//日期
    String birthday;

    @Override
    public void initData() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add(getString(R.string.tv_get_gift));
        titles.add(getString(R.string.tv_sendone_gift));
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_tablayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {

        setTitleText(R.string.tv_gift_bill);
        setRightImg(getDrawable(R.drawable.date));
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyTimeDialog();
            }
        });
        setFrag();
    }

    private void showMyTimeDialog() {
        long timeBir;
        if (StringUtils.isEmpty(birthday)) {
            timeBir = System.currentTimeMillis();
        } else {
            timeBir = MyUtils.getInstans().getLongTime(birthday, "yyyy-MM-dd");
        }
        final MyBirthdayDialog myBirthdayDialog = new MyBirthdayDialog(this, timeBir);
        myBirthdayDialog.show();
        myBirthdayDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = myBirthdayDialog.getDate();
                showBirthday(calendar);
                myBirthdayDialog.dismiss();
            }
        });
    }

    private void showBirthday(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        birthday = year + "-" +
                MyUtils.getInstans().formatTime(monthOfYear + 1) +
                "-" +
                MyUtils.getInstans().formatTime(dayOfMonth);
        if (giftHisFragment != null) {
            giftHisFragment.setCheckTime(birthday);
        }
        if (giftHisFragment1 != null) {
            giftHisFragment1.setCheckTime(birthday);
        }
    }

    private void setFrag() {
        giftHisFragment = new GiftHisFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.TYPE, 0);
        giftHisFragment.setArguments(bundle);
        fragments.add(giftHisFragment);

        giftHisFragment1 = new GiftHisFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(Const.ShowIntent.TYPE, 1);
        giftHisFragment1.setArguments(bundle1);
        fragments.add(giftHisFragment1);

        fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPager.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();
        mTabLayout.setViewPager(mViewPager);
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
