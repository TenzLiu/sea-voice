package com.jingtaoi.yy.ui.mine.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.MyattentionBean;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class MyAttentionListAdapter extends BaseQuickAdapter<MyattentionBean.DataEntity, BaseViewHolder> {


    public MyAttentionListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyattentionBean.DataEntity item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_friend);
        ImageUtils.loadUri(iv_show_onlines, item.getImg());
        helper.setText(R.id.tv_name_friend, item.getName());
        TextView iv_end_attention = helper.getView(R.id.iv_end_attention);
        helper.addOnClickListener(R.id.iv_end_attention);

        if (item.getState() == 1) {
            iv_end_attention.setVisibility(View.GONE);
        } else if (item.getState() == 2) {
            iv_end_attention.setVisibility(View.VISIBLE);

        }
    }
}
