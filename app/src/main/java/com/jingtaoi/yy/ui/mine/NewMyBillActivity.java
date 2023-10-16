package com.jingtaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.adapter.FragPagerAdapter;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.dialog.MyBirthdayDialog;
import com.jingtaoi.yy.ui.mine.fragment.DrawHisFragment;
import com.jingtaoi.yy.ui.mine.fragment.ExchangeFragment;
import com.jingtaoi.yy.ui.mine.fragment.GiftHisFragment;
import com.jingtaoi.yy.ui.mine.fragment.TopupHistoryFragment;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.MyUtils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.StringUtils;

public class NewMyBillActivity  extends MyBaseActivity {
    @BindView(R.id.mTabLayout_home)
    SlidingTabLayout mTabLayoutHome;
    @BindView(R.id.mViewPager_home)
    ViewPager mViewPagerHome;
    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    String checkTime;
    Calendar calendar;//日期

    int currentItem=0;
    private TopupHistoryFragment historyFragment;
    private DrawHisFragment drawHisFragment;
    private ExchangeFragment exchangeFragment;
    private GiftHisFragment giftHisFragment;
    private GiftHisFragment giftHisFragment1;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_newmybill);
    }

    @Override
    public void initView() {

        setRightImg(getDrawable(R.drawable.date));
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyTimeDialog();
            }
        });

        setTitleText("我的账单");
        initFrag();
    }


    private void showMyTimeDialog() {
        long timeBir;
        if (StringUtils.isEmpty(checkTime)) {
            timeBir = System.currentTimeMillis();
        } else {
            timeBir = MyUtils.getInstans().getLongTime(checkTime, "yyyy-MM-dd");
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
        checkTime = year + "-" +
                MyUtils.getInstans().formatTime(monthOfYear + 1) +
                "-" +
                MyUtils.getInstans().formatTime(dayOfMonth);


        if (currentItem==0){
            historyFragment.setCheckTime(checkTime);
        }else if (currentItem==1){
            drawHisFragment.setCheckTime(checkTime);
        }else if (currentItem==2){
            exchangeFragment.setCheckTime(checkTime);
        }else if (currentItem==3){
            giftHisFragment.setCheckTime(checkTime);
        } else if (currentItem==4){
            giftHisFragment1.setCheckTime(checkTime);
        }
    }


    public String getCheckTime(){
        return checkTime;
    }




    private void initFrag() {
        //1是热门，2是陪玩，4娱乐，5听歌，6相亲，7电台
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("充值");
        titles.add("提现");
        titles.add("兑换");
        titles.add("收到礼物");
        titles.add("送出礼物");
//        titles.add("邀请记录");
        historyFragment = new TopupHistoryFragment();
        fragments.add(historyFragment);

        drawHisFragment = new DrawHisFragment();
        fragments.add(drawHisFragment);


        exchangeFragment = new ExchangeFragment();
        fragments.add(exchangeFragment);


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

//        ShareHisFragment shareHisFragment=new ShareHisFragment();
//        fragments.add(shareHisFragment);

        setFrag();
    }

    private void setFrag() {
        FragPagerAdapter   fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());

        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPagerHome.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();
        mTabLayoutHome.setViewPager(mViewPagerHome);
        mViewPagerHome.setCurrentItem(currentItem);
        mViewPagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentItem=i;
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

//    @OnClick({R.id.rl_gift_bill, R.id.tv_tupup_bill, R.id.tv_draw_bill, R.id.tv_share_bill})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.rl_gift_bill:
//                ActivityCollector.getActivityCollector().toOtherActivity(GiftHisActivity.class);
//                break;
//            case R.id.tv_tupup_bill:
//                ActivityCollector.getActivityCollector().toOtherActivity(TopupHistoryActivity.class);
//                break;
//            case R.id.tv_draw_bill:
//                ActivityCollector.getActivityCollector().toOtherActivity(DrawHisActivity.class);
//                break;
//            case R.id.tv_share_bill:
//                ActivityCollector.getActivityCollector().toOtherActivity(ShareHisActivity.class);
//                break;
//        }
//    }
}
