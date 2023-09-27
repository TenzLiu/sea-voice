package com.siyecaoi.yy.ui.room.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.VoiceUserBean;
import com.siyecaoi.yy.utils.ImageUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class BottomOnlinesAdapter extends BaseQuickAdapter<VoiceUserBean.DataBean, BaseViewHolder> {

    public BottomOnlinesAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, VoiceUserBean.DataBean item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_onlines);
        ImageUtils.loadUri(iv_show_onlines, item.getImg());
        helper.setText(R.id.tv_name_onlines, item.getName());
        ImageView iv_sex_onlines = helper.getView(R.id.iv_sex_onlines);
        if (item.getSex() == 1) { //男
            iv_sex_onlines.setImageResource(R.drawable.sex_male);
        } else if (item.getSex() == 2) {
            iv_sex_onlines.setImageResource(R.drawable.sex_female);
        }
        TextView tv_upmic_onlines = helper.getView(R.id.tv_upmic_onlines);
        TextView tv_state_onlines = helper.getView(R.id.tv_state_onlines);
        TextView tv_leave_onlines = helper.getView(R.id.tv_leave_onlines);
        Drawable drawableLeft = mContext.getResources().getDrawable(
                R.drawable.icon_mic);

        tv_upmic_onlines.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        tv_upmic_onlines.setText(mContext.getString(R.string.tv_up_bottomshow));
        if (item.getState() == 1) {//未上麦
            tv_upmic_onlines.setVisibility(View.GONE);
        } else if (item.getState() == 2) {//上麦
            tv_upmic_onlines.setVisibility(View.VISIBLE);
        }

        if (item.getType() == 1) {  //房主
            tv_state_onlines.setVisibility(View.GONE);
            drawableLeft = mContext.getResources().getDrawable(
                    R.drawable.owner);
            tv_upmic_onlines.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            tv_upmic_onlines.setText(mContext.getString(R.string.tv_home_bottomshow));
        } else if (item.getType() == 2) {  //管理员
            tv_state_onlines.setVisibility(View.VISIBLE);
        } else if (item.getType() == 3) {  //用户
            tv_state_onlines.setVisibility(View.GONE);
        }
        if (item.getIsAgreement() == 1) {
            tv_leave_onlines.setVisibility(View.GONE);
        } else if (item.getIsAgreement() == 2) {
            tv_leave_onlines.setVisibility(View.VISIBLE);
        }
    }

}
