package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiftShowBean;
import com.siyecaoi.yy.bean.RealtimeListBean;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class DanJiangRealtimeAdapter extends BaseQuickAdapter<RealtimeListBean.GiftBean, BaseViewHolder> {

    public DanJiangRealtimeAdapter(int layoutResId) {
        super(layoutResId);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, RealtimeListBean.GiftBean item) {
        if (item.giftImg.isEmpty()&&item.giftCost.isEmpty()){
            helper.itemView.setVisibility(View.INVISIBLE);
        }else {
            helper.itemView.setVisibility(View.VISIBLE);
        }
        SimpleDraweeView iv_gift = helper.getView(R.id.iv_gift);
        TextView tv_count = helper.getView(R.id.tv_count);
        iv_gift.setImageURI(item.giftImg);
        helper.setText(R.id.tv_price,item.giftCost);
        tv_count.setText(item.giftNum+"");
        if (item.giftNum == 0){
            tv_count.setTextColor(Color.WHITE);
            tv_count.setBackgroundResource(R.drawable.bg_realtime_0);
        }else {
            tv_count.setTextColor(Color.parseColor("#FCEDD3"));
            tv_count.setBackgroundResource(R.drawable.bg_realtime_red);
        }
    }

}
