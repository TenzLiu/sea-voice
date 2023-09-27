package com.siyecaoi.yy.ui.room.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.model.GiftNumber;

public class GiftNumberListAdapter extends BaseQuickAdapter<GiftNumber, BaseViewHolder> {


    public GiftNumberListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftNumber item) {
        helper.setText(R.id.tv_number_giftnumber, item.getNumberShow() + "");
        helper.setText(R.id.tv_desshow_giftnumber, item.getDesShow());

    }
}
