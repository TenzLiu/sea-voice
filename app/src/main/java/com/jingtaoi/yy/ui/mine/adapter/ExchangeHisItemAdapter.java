package com.jingtaoi.yy.ui.mine.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.ExchangeHisBean;
import com.jingtaoi.yy.utils.MyUtils;

public class ExchangeHisItemAdapter extends BaseQuickAdapter<ExchangeHisBean.ExchangeEntity, BaseViewHolder> {


    public ExchangeHisItemAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, ExchangeHisBean.ExchangeEntity item) {
        helper.setText(R.id.tv_name_gifthis, "-"+item.dhDiamond);
        ((TextView)helper.getView(R.id.tv_name_gifthis)).setTextColor(Color.parseColor("#000103"));
        helper.setText(R.id.tv_time_gifthis, MyUtils.getInstans().showTimeYMDHM(MyUtils.getInstans().getLongTime(item.createTime)));
//        SimpleDraweeView iv_show_gifthis = helper.getView(R.id.iv_show_gifthis);
//        ImageUtils.loadDrawable(iv_show_gifthis, R.drawable.gold);
        helper.setText(R.id.tv_money_gifthis,  "");

    }
}
