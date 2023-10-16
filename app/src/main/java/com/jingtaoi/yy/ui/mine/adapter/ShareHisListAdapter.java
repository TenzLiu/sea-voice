package com.jingtaoi.yy.ui.mine.adapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.ShareHisBean;
import com.jingtaoi.yy.utils.MyUtils;

public class ShareHisListAdapter extends BaseQuickAdapter<ShareHisBean.DataEntity, BaseViewHolder> {


    public ShareHisListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareHisBean.DataEntity item) {
        helper.setText(R.id.tv_time_show, MyUtils.getInstans()
                .showTime(MyUtils.getInstans().getLongTime(item.getCreateTime()),"yyyy.MM.dd"));
        RecyclerView mRecyclerView_show = helper.getView(R.id.mRecyclerView_show);
        ShareHisItemAdapter shareHisItemAdapter = new ShareHisItemAdapter(R.layout.item_sharehis);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView_show.setLayoutManager(layoutManager);
        mRecyclerView_show.setAdapter(shareHisItemAdapter);
        shareHisItemAdapter.setNewData(item.getInvite());

    }
}
