package com.siyecaoi.yy.ui.home.adapter;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.model.ApplyRecord;

import java.util.List;

import cn.sinata.xldutils.adapter.HFRecyclerAdapter;
import cn.sinata.xldutils.adapter.util.ViewHolder;

public class ApplyRecordAdapter extends HFRecyclerAdapter<ApplyRecord> {
    public ApplyRecordAdapter(List<ApplyRecord> mData) {
        super(mData, R.layout.item_apply_record);
    }

    @Override
    public void onBind(int position, ApplyRecord applyRecord, ViewHolder holder) {
        holder.setText(R.id.tv_id,applyRecord.getRid());
        holder.setText(R.id.tv_date,applyRecord.getYtime());
        holder.setText(R.id.tv_time,applyRecord.getMtime()+":00");
    }
}
