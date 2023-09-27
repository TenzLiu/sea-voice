package com.siyecaoi.yy.ui.room.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.RoomBlackBean;
import com.siyecaoi.yy.utils.ImageUtils;

public class BlackUserListAdapter extends BaseQuickAdapter<RoomBlackBean.DataBean, BaseViewHolder> {


    public BlackUserListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomBlackBean.DataBean item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_onlines);
        ImageUtils.loadUri(iv_show_onlines, item.getImg());
        helper.setText(R.id.tv_name_onlines, item.getName());
        ImageView iv_sex_onlines = helper.getView(R.id.iv_sex_onlines);
        if (item.getSex() == 1) { //男
            iv_sex_onlines.setImageResource(R.drawable.sex_male);
        } else if (item.getSex() == 2) {
            iv_sex_onlines.setImageResource(R.drawable.sex_female);
        }
        if (item.getState() == 1) {
            helper.setText(R.id.tv_setone_onlines, "房主：" + item.getGlName());
        } else if (item.getState() == 2) {
            helper.setText(R.id.tv_setone_onlines, "管理员：" + item.getGlName());
        }

        helper.setText(R.id.tv_time_onlines, item.getCreateTime());

    }
}
