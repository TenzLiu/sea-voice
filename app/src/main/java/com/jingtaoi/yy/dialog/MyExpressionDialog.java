package com.jingtaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.ui.room.adapter.EmojiListAdapter;
import com.jingtaoi.yy.ui.room.adapter.ViewPagerAdapter;
import com.jingtaoi.yy.model.EmojiList;
import com.jingtaoi.yy.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 表情弹窗页面
 * Created by Administrator on 2018/3/9.
 */

public class MyExpressionDialog extends Dialog {


    Context context;
    @BindView(R.id.mViewPager_expression)
    ViewPager mViewPagerExpression;
    @BindView(R.id.ll_bottom_expression)
    LinearLayout llBottomExpression;

    List<EmojiList.DataBean> emojiListBean;

    private List<View> mPagerList;//页面集合
    EmojiListAdapter emojiListAdapter;
    GridLayoutManager layoutManager;
    int curIndex;

    public MyExpressionDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        emojiListBean = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_expression);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);

        initEmoji();
//        getCall();
    }

    private void initEmoji() {
        mPagerList = new ArrayList<>();
        EmojiList emojiList = JSON.parseObject(MyUtils.getInstans()
                .readAssetsFile(context, "EmojiList.json"), EmojiList.class);
        emojiListBean = emojiList.getEmoji_list();
        setFragment(emojiListBean, emojiListBean.size());
    }

    EmojiChoose emojiChoose;

    public EmojiChoose getEmojiChoose() {
        return emojiChoose;
    }

    public void setEmojiChoose(EmojiChoose emojiChoose) {
        this.emojiChoose = emojiChoose;
    }

    public interface EmojiChoose {
        void chooseOne(EmojiList.DataBean dataBean);
    }


    private int PAGE_EXPRESSION_SIZE = 15;
    private int ROW_EXPRESSION_SIZE = 5;
    private void setFragment(List<EmojiList.DataBean> data, int size) {
        int fragNumber = size / PAGE_EXPRESSION_SIZE;
        int fragEndNum = size % PAGE_EXPRESSION_SIZE;
        for (int i = 0; i <= fragNumber; i++) {
//            GiftFragment giftFragment = new GiftFragment();
            List<EmojiList.DataBean> giftData;
            if (fragNumber == i) {
                giftData = data.subList(i * PAGE_EXPRESSION_SIZE, i * PAGE_EXPRESSION_SIZE + fragEndNum);
            } else {
                giftData = data.subList(i * PAGE_EXPRESSION_SIZE, (i + 1) * PAGE_EXPRESSION_SIZE);
            }
            RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(context)
                    .inflate(R.layout.layout_recycler_gift, mViewPagerExpression, false);
            emojiListAdapter = new EmojiListAdapter(R.layout.item_expression);

            recyclerView.setAdapter(emojiListAdapter);
            layoutManager = new GridLayoutManager(context, ROW_EXPRESSION_SIZE);
            recyclerView.setLayoutManager(layoutManager);
            emojiListAdapter.setNewData(giftData);
            mPagerList.add(recyclerView);
            emojiListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    EmojiList.DataBean dataBean = (EmojiList.DataBean) adapter.getItem(position);
                    if (emojiChoose != null) {
                        emojiChoose.chooseOne(dataBean);
                    }
                    dismiss();
                }
            });
        }

        mViewPagerExpression.setAdapter(new ViewPagerAdapter(mPagerList, context));
        mViewPagerExpression.setCurrentItem(0);
        for (int i = 0; i <= fragNumber; i++) {
            ImageView iv_indicator = (ImageView) LayoutInflater.from(context)
                    .inflate(R.layout.iv_indicator_gift, llBottomExpression, false);
            if (i==0){
                iv_indicator.setSelected(true);
            }
            llBottomExpression.addView(iv_indicator);
        }


        mViewPagerExpression.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                llBottomExpression.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(false);
                curIndex = i;
                llBottomExpression.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


}