package com.jingtaoi.yy.ui.mine.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.DrawBean;
import com.jingtaoi.yy.utils.MyUtils;

public class DrawListAdapter extends BaseQuickAdapter<DrawBean.DataEntity.WithdrawEntity, BaseViewHolder> {


    public DrawListAdapter(int layoutResId) {
        super(layoutResId);
    }

    int chooseOne;

    public int getChooseOne() {
        return chooseOne;
    }

    public void setChooseOne(int chooseOne) {
        this.chooseOne = chooseOne;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, DrawBean.DataEntity.WithdrawEntity item) {
        TextView tv_show_topup = helper.getView(R.id.tv_show_topup);
        TextView tv_price_topup = helper.getView(R.id.tv_price_topup);
        RelativeLayout rl_back_topup = helper.getView(R.id.rl_back_topup);
        String s = "¥" + MyUtils.getInstans().doubleDelete(item.getX());
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new AbsoluteSizeSpan(12,true),0,1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv_show_topup.setText(spannableString);
        tv_price_topup.setText("消耗" + item.getY());
        if (chooseOne == helper.getAdapterPosition()) {
            rl_back_topup.setSelected(true);
            tv_show_topup.setTextColor(Color.parseColor("#4BA6DC"));
        } else {
            rl_back_topup.setSelected(false);
            tv_show_topup.setTextColor(Color.parseColor("#2B2B2B"));
        }
    }
}
