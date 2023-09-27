package com.siyecaoi.yy.ui.room.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiftShowBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.DanJackpotAdapter;
import com.siyecaoi.yy.ui.room.adapter.DanJiangAdapter;
import com.siyecaoi.yy.ui.room.adapter.ViewPagerAdapter;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 本期奖池
 */
public class DanJiangDialog extends Dialog {

    Context mContext;

    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.rcList)
    RecyclerView rcList;
    private int type;
    private DanJackpotAdapter danJackpotAdapter;
    private Map<Integer, Integer> fishBeanViewMap;

    public DanJiangDialog(Context context, int type, Map<Integer, Integer> fishBeanViewMap) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
        this.type = type;
        this.fishBeanViewMap = fishBeanViewMap;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_danjiang);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (ScreenUtil.getScreenWidth(mContext));
        lp.height = (int) (ScreenUtil.getScreenHeight(mContext) * 0.55);
        getWindow().setAttributes(lp);

        initView();
        getCall();
    }

    private void initView() {
        iv_close.setOnClickListener(view -> {
            dismiss();
        });
        rcList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        danJackpotAdapter = new DanJackpotAdapter(R.layout.dan_jackpot_item,fishBeanViewMap);
        rcList.setAdapter(danJackpotAdapter);

    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        String reqString = Api.JackpotInfoMark;
        map.put("type", type);
        HttpManager.getInstance().post(reqString, map, new MyObserver(mContext) {
            @Override
            public void success(String responseString) {
                GiftShowBean giftShowBean = JSON.parseObject(responseString, GiftShowBean.class);
                danJackpotAdapter.setNewData(giftShowBean.getData());
//                setFragment(giftShowBean.getData(), giftShowBean.getData().size());
            }
        });
    }

//    DanJiangAdapter danJiangAdapter;
//    GridLayoutManager layoutManager;
//    List<DanJiangAdapter> groudAdapterList;
//    private List<View> mPagerList;//页面集合
//    private ViewPagerAdapter viewPagerAdapter;
//    private int curIndex;

//    private void setFragment(List<GiftShowBean.DataEntity> data, int size) {
//        groudAdapterList = new ArrayList<>();
//        mPagerList = new ArrayList<>();
//        int fragNumber = size / 8;
//        int fragEndNum = size % 8;
////        总的页数=总数/每页数量，并取整
////        int fragNumber = (int) Math.ceil(size / Const.IntShow.EIGHT);
//        for (int i = 0; i <= fragNumber; i++) {
////            GiftFragment giftFragment = new GiftFragment();
//            List<GiftShowBean.DataEntity> giftData;
//            if (fragNumber == i) {
//                giftData = data.subList(i * 8, i * 8 + fragEndNum);
//            } else {
//                giftData = data.subList(i * 8, (i + 1) * 8);
//            }
//            RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(mContext)
//                    .inflate(R.layout.layout_recycler_gift, mViewPager_danjiang, false);
//            danJiangAdapter = new DanJiangAdapter(R.layout.item_chestsshow);
//            recyclerView.setAdapter(danJiangAdapter);
//
//            layoutManager = new GridLayoutManager(mContext, 4);
//            recyclerView.setLayoutManager(layoutManager);
//            danJiangAdapter.setNewData(giftData);
//            groudAdapterList.add(danJiangAdapter);
//            mPagerList.add(recyclerView);
//        }
//        viewPagerAdapter = new ViewPagerAdapter(mPagerList, mContext);
//        mViewPager_danjiang.setAdapter(viewPagerAdapter);
//        mViewPager_danjiang.setCurrentItem(0);
////        for (int i = 0; i < groudAdapterList.size(); i++) {
////            groudAdapterList.get(i).setGid(gid);
////            groudAdapterList.get(i).notifyDataSetChanged();
////        }
//        for (int i = 0; i <= fragNumber; i++) {
//            ImageView iv_indicator = (ImageView) LayoutInflater.from(mContext)
//                    .inflate(R.layout.iv_indicator_gift, ll_indicator_gift, false);
//            if (i == 0) {
//                iv_indicator.setSelected(true);
//            }
//            ll_indicator_gift.addView(iv_indicator);
//        }
//
//
//        mViewPager_danjiang.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                ll_indicator_gift.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(false);
//                curIndex = i;
//                ll_indicator_gift.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(true);
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//    }

}
