package com.jingtaoi.yy.ui.mine.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.TopupHisBean;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.MyUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class TopupHisItemAdapter extends BaseQuickAdapter<TopupHisBean.DataEntity.RechargeEntity, BaseViewHolder> {


    public TopupHisItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopupHisBean.DataEntity.RechargeEntity item) {
        helper.setText(R.id.tv_name_gifthis, item.getMark());
        ((TextView)helper.getView(R.id.tv_name_gifthis)).setTextColor(Color.parseColor("#000103"));
        helper.setText(R.id.tv_time_gifthis, MyUtils.getInstans().showTimeYMDHM(MyUtils.getInstans().getLongTime(item.getCreateTime())));
        SimpleDraweeView iv_show_gifthis = helper.getView(R.id.iv_show_gifthis);
        ImageUtils.loadDrawable(iv_show_gifthis, R.drawable.yhc_small);
        helper.setText(R.id.tv_money_gifthis,  + item.getGold()+"");
    }
}
