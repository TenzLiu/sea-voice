package com.jingtaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.GiftBean;
import com.jingtaoi.yy.utils.ImageUtils;

public class GiftGroudAdapter extends BaseQuickAdapter<GiftBean.DataBean, BaseViewHolder> {


    int gid;//礼物id
    int sendType;

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public GiftGroudAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftBean.DataBean item) {

        SimpleDraweeView iv_gift_gift = helper.getView(R.id.iv_gift_gift);
        ImageUtils.loadUri(iv_gift_gift, item.getImgFm());
        ImageView iv_type1_gift = helper.getView(R.id.iv_type1_gift);
        ImageView iv_type2_gift = helper.getView(R.id.iv_type2_gift);
        ImageView iv_type3_gift = helper.getView(R.id.iv_type3_gift);
//        View view_end_show = helper.getView(R.id.view_end_show);
//        View view_bottom_show = helper.getView(R.id.view_bottom_show);
        View view_select_show = helper.getView(R.id.view_select_show);
        TextView tv_numbershow_gift = helper.getView(R.id.tv_numbershow_gift);
        RelativeLayout rl_back_gift = helper.getView(R.id.rl_back_gift);
        if (item.getStatus() == 1) { //是否是特价
            iv_type3_gift.setVisibility(View.GONE);
        } else if (item.getStatus() == 2) {
            iv_type3_gift.setVisibility(View.VISIBLE);
        }

        if (item.getXin() == 1) {
            iv_type1_gift.setVisibility(View.VISIBLE);
        } else if (item.getXin() == 2) {
            iv_type1_gift.setVisibility(View.GONE);
        }

//        view_bottom_show.setVisibility(View.VISIBLE);
        if (item.getRestrict() == 1) {
            iv_type2_gift.setVisibility(View.VISIBLE);
        } else if (item.getRestrict() == 2) {
            iv_type2_gift.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_price_gift, item.getGold()+"" );
        helper.setText(R.id.tv_name_gift, item.getName());

        if (sendType == 1) {
            tv_numbershow_gift.setVisibility(View.INVISIBLE);
        } else if (sendType == 2) {
            tv_numbershow_gift.setVisibility(View.VISIBLE);
            iv_type1_gift.setVisibility(View.GONE);
            iv_type2_gift.setVisibility(View.GONE);
            iv_type3_gift.setVisibility(View.GONE);
            tv_numbershow_gift.setText(item.getNum() + "");
        }

        if (gid == item.getId()) {
            view_select_show.setVisibility(View.VISIBLE);
        } else {
            view_select_show.setVisibility(View.INVISIBLE);
        }

//        if (helper.getAdapterPosition() % 4 == 3) {
//            view_end_show.setVisibility(View.INVISIBLE);
//        } else {
//            view_end_show.setVisibility(View.VISIBLE);
//        }
//        if (helper.getAdapterPosition() % 2 == 0) {
//            rl_back_gift.setBackgroundResource(R.drawable.bg1);
//        } else {
//            rl_back_gift.setBackgroundResource(R.drawable.bg2);
//        }
    }
}
