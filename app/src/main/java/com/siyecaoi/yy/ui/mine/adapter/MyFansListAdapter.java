package com.siyecaoi.yy.ui.mine.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.MyattentionBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class MyFansListAdapter extends BaseQuickAdapter<MyattentionBean.DataEntity, BaseViewHolder> {


    public MyFansListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyattentionBean.DataEntity item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_friend);
        ImageUtils.loadUri(iv_show_onlines, item.getImg());
        helper.setText(R.id.tv_name_friend, item.getName());
        TextView iv_end_attention = helper.getView(R.id.iv_end_attention);
        TextView caozuot_tv = helper.getView(R.id.caozuot_tv);
        helper.addOnClickListener(R.id.caozuot_tv);

        iv_end_attention.setVisibility(View.GONE);
        caozuot_tv.setVisibility(View.VISIBLE);

        if (item.getState() == 1) {
            caozuot_tv.setText("立即回关");
        } else if (item.getState() == 2) {
            caozuot_tv.setText("取消关注");
            caozuot_tv.setVisibility(View.GONE);
        }
    }
}
