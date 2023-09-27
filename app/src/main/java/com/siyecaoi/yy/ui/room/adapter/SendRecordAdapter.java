package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.SendRecordBean;
import com.siyecaoi.yy.utils.MyUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2019/1/2.
 */

public class SendRecordAdapter extends BaseQuickAdapter<SendRecordBean.DataBean, BaseViewHolder> {

    public SendRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, SendRecordBean.DataBean item) {
        TextView tv_name_sendrecord = helper.getView(R.id.tv_name_sendrecord);
        TextView tv_gold_sendrecord = helper.getView(R.id.tv_gold_sendrecord);
        ImageView iv_liang_sendrecord = helper.getView(R.id.iv_liang_sendrecord);
        TextView tv_id_sendrecord = helper.getView(R.id.tv_id_sendrecord);
        TextView tv_time_sendrecord = helper.getView(R.id.tv_time_sendrecord);

        tv_name_sendrecord.setText(item.getNickname());
        tv_gold_sendrecord.setText(MyUtils.getInstans().doubleDelete(item.getSumMoney()) + "");
        if (item.getIsLiang() == 1) {
            iv_liang_sendrecord.setVisibility(View.INVISIBLE);
        } else if (item.getIsLiang() == 2) {
            iv_liang_sendrecord.setVisibility(View.VISIBLE);
        }
        tv_id_sendrecord.setText("ID:" + item.getUsercoding());
        tv_time_sendrecord.setText(MyUtils.getInstans().showTimeYMDHM(MyUtils.getInstans().getLongTime(item.getCreateTime())));
    }
}
