package com.siyecaoi.yy.ui.mine.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiftGetBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class PersonHomeGiftListAdapter extends BaseQuickAdapter<GiftGetBean.DataEntity, BaseViewHolder> {


    public PersonHomeGiftListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftGetBean.DataEntity item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_gift);
        TextView tv_name_gift = helper.getView(R.id.tv_name_gift);
        TextView tv_num_gift = helper.getView(R.id.tv_num_gift);
        ImageUtils.loadUri(iv_show_onlines,item.getImg());
        tv_name_gift.setText(item.getName());
        tv_num_gift.setText("x"+item.getNum());

    }
}
