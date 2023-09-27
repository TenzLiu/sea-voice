package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.ChestOpenBean;
import com.siyecaoi.yy.bean.GiftShowBean;
import com.siyecaoi.yy.ui.room.adapter.ChestsShowAdapter;
import com.siyecaoi.yy.ui.room.adapter.DanJiangAdapter;
import com.siyecaoi.yy.ui.room.adapter.ViewPagerAdapter;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MyChestsShowDialog extends Dialog {

    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.iv_bottom)
    ImageView iv_bottom;
    @BindView(R.id.iv_border)
    ImageView iv_border;
    @BindView(R.id.iv_border_mid)
    ImageView iv_border_mid;
    @BindView(R.id.iv_border_normal)
    ImageView iv_border_normal;
    @BindView(R.id.iv_showone_chestsshow)
    SimpleDraweeView ivShowoneChestsshow;
    @BindView(R.id.tv_showone_chestsshow)
    TextView tvShowoneChestsshow;
    @BindView(R.id.tv_gold)
    TextView tvGold;
    @BindView(R.id.mViewPager_danjiang)
    ViewPager mViewPager_danjiang;
    @BindView(R.id.ll_indicator_gift)
    LinearLayout ll_indicator_gift;
    @BindView(R.id.tv_giftTotalValue)
    TextView tvGiftTotalValue;

    ChestOpenBean.DataBean data;
    ChestsShowAdapter adapter;
    Context mContext;
    @BindView(R.id.ll_one_chestsshow)
    RelativeLayout llOneChestsshow;
    int chooseOne;//为4则为自动探险

    public MyChestsShowDialog(Context context, ChestOpenBean.DataBean data, int chooseOne) {
        super(context, R.style.CustomDialogStyle);
        this.data = data;
        this.mContext = context;
        this.chooseOne = chooseOne;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_chestsshow);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.CenterDialogAnimation2);

        if (data.getLottery().size() == 0) {
            dismiss();
        } else if (data.getLottery().size() == 1) {
            llOneChestsshow.setVisibility(View.VISIBLE);
            mViewPager_danjiang.setVisibility(View.GONE);
            ll_indicator_gift.setVisibility(View.GONE);
            ImageUtils.loadUri(ivShowoneChestsshow, data.getLottery().get(0).getImg());
            tvShowoneChestsshow.setText(data.getLottery().get(0).getName() + "×" + data.getLottery().get(0).getNum());
            tvGold.setText(data.getLottery().get(0).getGold()+"");
            // 礼物价值1-520（绿色）1000-10000（紫色-带闪亮）20000-52000（橙色带闪亮）

            if (data.getLottery().get(0).getCost()<=520){
                iv_border_normal.setVisibility(View.VISIBLE);
            }else if (data.getLottery().get(0).getCost()<10000){
                iv_border_mid.setVisibility(View.VISIBLE);
//                ImageUtils.loadFrameAnimation(iv_border, R.array.bg_frame_mid,true, Const.giftBorderFrame,null);
            }else {
                iv_border.setVisibility(View.VISIBLE);
//                ImageUtils.loadFrameAnimation(iv_border, R.array.bg_frame_red,true,Const.giftBorderFrame,null);
            }


        } else {
            llOneChestsshow.setVisibility(View.GONE);
            mViewPager_danjiang.setVisibility(View.VISIBLE);
            ll_indicator_gift.setVisibility(View.VISIBLE);
            setFragment(data.getLottery(),data.getLottery().size());
        }
        long mGiftTotalValue = 0L;
        for (ChestOpenBean.DataBean.LotteryBean lotteryBean : data.getLottery()) {
            mGiftTotalValue = mGiftTotalValue+lotteryBean.getGold()*lotteryBean.getNum();
        }
        tvGiftTotalValue.setText("礼物总值："+mGiftTotalValue);
    }


    public void setButton(View.OnClickListener onClickListener) {
        iv_bottom.setOnClickListener(onClickListener);
    }

    public void setCancel(View.OnClickListener onClickListener) {
        iv_close.setOnClickListener(onClickListener);
    }


    ChestsShowAdapter danJiangAdapter;
    GridLayoutManager layoutManager;
    List<ChestsShowAdapter> groudAdapterList;
    private List<View> mPagerList;//页面集合
    private ViewPagerAdapter viewPagerAdapter;
    private int curIndex;

    private void setFragment(List<ChestOpenBean.DataBean.LotteryBean> data, int size) {
        groudAdapterList = new ArrayList<>();
        mPagerList = new ArrayList<>();
        int fragNumber = size / 6;
        int fragEndNum = size % 6;
        for (int i = 0; i <= fragNumber; i++) {
            List<ChestOpenBean.DataBean.LotteryBean> giftData;
            if (fragNumber == i) {
                giftData = data.subList(i * 6, i * 6 + fragEndNum);
            } else {
                giftData = data.subList(i * 6, (i + 1) * 6);
            }
            if (giftData.size() != 0){
                RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_recycler_gift, mViewPager_danjiang, false);
                danJiangAdapter = new ChestsShowAdapter(giftData);
                recyclerView.setAdapter(danJiangAdapter);

                layoutManager = new GridLayoutManager(mContext, 3);
                recyclerView.setLayoutManager(layoutManager);
                danJiangAdapter.setNewData(giftData);
                groudAdapterList.add(danJiangAdapter);
                mPagerList.add(recyclerView);
            }
        }
        viewPagerAdapter = new ViewPagerAdapter(mPagerList, mContext);
        mViewPager_danjiang.setAdapter(viewPagerAdapter);
        mViewPager_danjiang.setCurrentItem(0);

        for (int i = 0; i <= fragNumber; i++) {
            ImageView iv_indicator = (ImageView) LayoutInflater.from(mContext)
                    .inflate(R.layout.iv_indicator_gift, ll_indicator_gift, false);
            if (i == 0) {
                iv_indicator.setSelected(true);
            }
            if (i == fragNumber&&fragEndNum == 0)
                break;
            ll_indicator_gift.addView(iv_indicator);
        }


        mViewPager_danjiang.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ll_indicator_gift.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(false);
                curIndex = i;
                ll_indicator_gift.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}
