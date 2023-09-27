package com.siyecaoi.yy.ui.mine.adapter;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.DrawHisBean;

public class DrawHisListAdapter extends BaseQuickAdapter<DrawHisBean.DataEntity, BaseViewHolder> {


    public DrawHisListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, DrawHisBean.DataEntity item) {

        RecyclerView mRecyclerView_show = helper.getView(R.id.mRecyclerView_show);
        DrawHisItemAdapter drawHisItemAdapter = new DrawHisItemAdapter(R.layout.item_drawhis);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView_show.setLayoutManager(layoutManager);
        mRecyclerView_show.setAdapter(drawHisItemAdapter);
        drawHisItemAdapter.setNewData(item.getWithdraw());
    }
}
