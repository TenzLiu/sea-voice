package com.siyecaoi.yy.ui.find.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.InviteNumberBean;
import com.siyecaoi.yy.utils.MyUtils;

public class InviteNumRecyAdapter extends BaseQuickAdapter<InviteNumberBean.DataEntity.InviteListEntity, BaseViewHolder> {

    public InviteNumRecyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteNumberBean.DataEntity.InviteListEntity item) {
        helper.setText(R.id.tv_name_sharehis, item.getName());
        helper.setText(R.id.tv_time_sharehis, item.getCreateTime());
        helper.setText(R.id.tv_money_sharehis, "+" + MyUtils.getInstans().doubleTwo(item.getMoney()) + "元");
    }
}
