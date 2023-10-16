package com.jingtaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.utils.ImageUtils;

public class GiftUserListAdapter extends BaseQuickAdapter<VoiceUserBean.DataBean, BaseViewHolder> {


    public GiftUserListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, VoiceUserBean.DataBean item) {
        SimpleDraweeView iv_header_giftuser = helper.getView(R.id.iv_header_giftuser);
        TextView tv_name_giftuser = helper.getView(R.id.tv_name_giftuser);
        ImageView iv_select_giftuser = helper.getView(R.id.iv_select_giftuser);
        ImageUtils.loadUri(iv_header_giftuser, item.getImg());
        tv_name_giftuser.setText(item.getName());
        if (item.isChoose()) {
            iv_select_giftuser.setVisibility(View.VISIBLE);
//            tv_name_giftuser.setTextColor(ContextCompat.getColor(mContext, R.color.FF003F));
            tv_name_giftuser.setBackgroundResource(R.drawable.bg_gift_mic_check);
        } else {
            iv_select_giftuser.setVisibility(View.INVISIBLE);
//            tv_name_giftuser.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            tv_name_giftuser.setBackgroundResource(R.drawable.bg_gift_mic);
        }
    }
}
