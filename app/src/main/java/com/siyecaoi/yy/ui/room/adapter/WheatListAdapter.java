package com.siyecaoi.yy.ui.room.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.WheatListBean;
import com.siyecaoi.yy.utils.ImageUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class WheatListAdapter extends BaseQuickAdapter<WheatListBean.DataBean.UserListBean, BaseViewHolder> {

    public WheatListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, WheatListBean.DataBean.UserListBean item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_onlines);
        ImageUtils.loadUri(iv_show_onlines, item.getImg());
        helper.setText(R.id.tv_name_onlines, item.getName());
        helper.setText(R.id.tv_type_onlines, "ID：" + item.getUsercoding());
        ImageView iv_sex_onlines = helper.getView(R.id.iv_sex_onlines);

        if (item.getSex() == 1) {
            iv_sex_onlines.setSelected(true);
        } else if (item.getSex() == 2) {
            iv_sex_onlines.setSelected(false);
        }
    }

}
