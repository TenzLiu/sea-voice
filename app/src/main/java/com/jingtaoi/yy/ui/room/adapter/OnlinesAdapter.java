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

/**
 * Created by Administrator on 2019/1/2.
 */

public class OnlinesAdapter extends BaseQuickAdapter<VoiceUserBean.DataBean, BaseViewHolder> {

    public OnlinesAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, VoiceUserBean.DataBean item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_onlines);
        ImageUtils.loadUri(iv_show_onlines, item.getImg());
        helper.setText(R.id.tv_name_onlines, item.getName());
        ImageView iv_sex_onlines = helper.getView(R.id.iv_sex_onlines);
        TextView tv_leave_onlines = helper.getView(R.id.tv_leave_onlines);
        if (item.getSex() == 1) { //男
            iv_sex_onlines.setImageResource(R.drawable.sex_male);
        } else if (item.getSex() == 2) {
            iv_sex_onlines.setImageResource(R.drawable.sex_female);
        }

        if (item.getType() == 1) { //房主

        } else if (item.getType() == 2) {//管理员

        } else if (item.getType() == 3) {//用户

        }

        if (item.getIsAgreement() == 1) {
            tv_leave_onlines.setVisibility(View.GONE);
        } else if (item.getIsAgreement() == 2) {
            tv_leave_onlines.setVisibility(View.VISIBLE);
        }


        helper.getView(R.id.shangmai_tv).setVisibility(View.VISIBLE);
    }
}
