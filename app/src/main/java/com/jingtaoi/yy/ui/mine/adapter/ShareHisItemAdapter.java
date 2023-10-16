package com.jingtaoi.yy.ui.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.ShareHisBean;
import com.jingtaoi.yy.utils.MyUtils;

public class ShareHisItemAdapter extends BaseQuickAdapter<ShareHisBean.DataEntity.InviteEntity, BaseViewHolder> {


    public ShareHisItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareHisBean.DataEntity.InviteEntity item) {
        helper.setText(R.id.tv_name_sharehis, item.getName());
        helper.setText(R.id.tv_time_sharehis, MyUtils.getInstans().showTimeHHmmss(MyUtils.getInstans().getLongTime(item.getCreateTime())));
        helper.setText(R.id.tv_money_sharehis, "+" + MyUtils.getInstans().doubleTwo(item.getMoney()) + "元");

    }
}
