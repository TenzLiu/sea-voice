package com.jingtaoi.yy.ui.mine.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.UserBlackBean;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class BlackListAdapter extends BaseQuickAdapter<UserBlackBean.DataEntity, BaseViewHolder> {


    public BlackListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBlackBean.DataEntity item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_onlines);
        ImageUtils.loadUri(iv_show_onlines, item.getImg());
        helper.setText(R.id.tv_name_onlines, item.getName());
        ImageView iv_sex_onlines = helper.getView(R.id.iv_sex_onlines);
        if (item.getSex() == 1) { //男
            iv_sex_onlines.setImageResource(R.drawable.sex_male);
        } else if (item.getSex() == 2) {
            iv_sex_onlines.setImageResource(R.drawable.sex_female);
        }
        TextView tv_clear_onlines = helper.getView(R.id.tv_clear_onlines);
        tv_clear_onlines.setVisibility(View.VISIBLE);
        helper.addOnClickListener(R.id.tv_clear_onlines);
//        helper.setText(R.id.tv_id,)

    }
}
