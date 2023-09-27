package com.siyecaoi.yy.ui.mine.adapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiftHisBean;

public class GiftHisListAdapter extends BaseQuickAdapter<GiftHisBean, BaseViewHolder> {


    public GiftHisListAdapter(int layoutResId) {
        super(layoutResId);
    }

    int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftHisBean item) {
//        helper.setText(R.id.tv_time_show, MyUtils.getInstans()
//                .showTime(MyUtils.getInstans().getLongTime(item.getCreateTime()), "yyyy.MM.dd"));
        RecyclerView mRecyclerView_show = helper.getView(R.id.mRecyclerView_show);
        GiftHisItemAdapter giftHisItemAdapter = new GiftHisItemAdapter(R.layout.item_gifthis);
        giftHisItemAdapter.setState(state);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView_show.setLayoutManager(layoutManager);
        mRecyclerView_show.setAdapter(giftHisItemAdapter);
    }
}
