package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.ChestOpenBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2019/1/2.
 */

public class ChestsShowAdapter extends BaseQuickAdapter<ChestOpenBean.DataBean.LotteryBean, BaseViewHolder> {

    public ChestsShowAdapter(int layoutResId) {
        super(layoutResId);
    }

    public ChestsShowAdapter(@Nullable List<ChestOpenBean.DataBean.LotteryBean> data) {
        super(R.layout.item_chestsshow_2,data);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, ChestOpenBean.DataBean.LotteryBean item) {
        ImageView iv_border = helper.getView(R.id.iv_border);
        ImageView iv_border_normal = helper.getView(R.id.iv_border_normal);
        ImageView iv_border_mid = helper.getView(R.id.iv_border_mid);
        SimpleDraweeView iv_showone_chestsshow = helper.getView(R.id.iv_showone_chestsshow);
        ImageUtils.loadUri(iv_showone_chestsshow, item.getImg());
//        礼物价值1-520（绿色）1000-10000（紫色-带闪亮）20000-52000（橙色带闪亮）
        if(item.getGold()<=520){
            iv_border.setVisibility(View.GONE);
            iv_border_normal.setVisibility(View.VISIBLE);
            iv_border_mid.setVisibility(View.GONE);

        }else if (item.getGold()<10000){
            iv_border.setVisibility(View.GONE);
            iv_border_normal.setVisibility(View.GONE);
            iv_border_mid.setVisibility(View.VISIBLE);
//            ImageUtils.loadFrameAnimation(iv_border_mid, R.array.bg_frame_mid,true, Const.giftBorderFrame,null);
//            iv_showone_chestsshow.setBackgroundResource(R.drawable.dan_back2);
        }else {
            iv_border.setVisibility(View.VISIBLE);
            iv_border_normal.setVisibility(View.GONE);
            iv_border_mid.setVisibility(View.GONE);
//            ImageUtils.loadFrameAnimation(iv_border, R.array.bg_frame_red,true,Const.giftBorderFrame,null);
//            iv_showone_chestsshow.setBackgroundResource(R.drawable.dan_back3);
        }

        TextView tv_showone_chestsshow = helper.getView(R.id.tv_showone_chestsshow);
        TextView tv_gold = helper.getView(R.id.tv_gold);
        tv_showone_chestsshow.setText(item.getName()+"x"+item.getNum());
        tv_gold.setText(item.getGold()+"");
    }

}
