package com.jingtaoi.yy.ui.message.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.GiftBean;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class GiftChatAdapter extends BaseQuickAdapter<GiftBean.DataBean, BaseViewHolder> {


    int gid;//被选中礼物id

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public GiftChatAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftBean.DataBean item) {

        SimpleDraweeView iv_gift_gift = helper.getView(R.id.iv_gift_gift);
        ImageUtils.loadUri(iv_gift_gift, item.getImgFm());
        ImageView iv_type1_gift = helper.getView(R.id.iv_type1_gift);
        ImageView iv_type2_gift = helper.getView(R.id.iv_type2_gift);
        ImageView iv_type3_gift = helper.getView(R.id.iv_type3_gift);
        View view_select_show = helper.getView(R.id.view_select_show);
        RelativeLayout rl_back_gift = helper.getView(R.id.rl_back_gift);
        if (item.getStatus() == 1) { //是否是特价
            iv_type3_gift.setVisibility(View.GONE);
        } else if (item.getStatus() == 2) {
            iv_type3_gift.setVisibility(View.VISIBLE);
        }

        if (item.getXin() == 1) {  //是否新品
            iv_type1_gift.setVisibility(View.VISIBLE);
        } else if (item.getXin() == 2) {
            iv_type1_gift.setVisibility(View.GONE);
        }

        if (item.getRestrict() == 1) { //是否限品
            iv_type2_gift.setVisibility(View.VISIBLE);
        } else if (item.getRestrict() == 2) {
            iv_type2_gift.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_price_gift, item.getGold() + mContext.getString(R.string.tv_you));
        helper.setText(R.id.tv_name_gift, item.getName());

        if (gid == item.getId()) {
            view_select_show.setVisibility(View.VISIBLE);
        } else {
            view_select_show.setVisibility(View.GONE);
        }

        if (helper.getAdapterPosition() % 2 == 0) {
            rl_back_gift.setBackgroundResource(R.drawable.bg1);
        } else {
            rl_back_gift.setBackgroundResource(R.drawable.bg2);
        }

    }
}
