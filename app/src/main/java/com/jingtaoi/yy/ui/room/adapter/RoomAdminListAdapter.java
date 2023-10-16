package com.jingtaoi.yy.ui.room.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.RoomAdminBean;
import com.jingtaoi.yy.utils.ImageUtils;

public class RoomAdminListAdapter extends BaseQuickAdapter<RoomAdminBean.DataBean, BaseViewHolder> {


    public RoomAdminListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomAdminBean.DataBean item) {
        SimpleDraweeView iv_show_roomadmin = helper.getView(R.id.iv_show_roomadmin);
        ImageUtils.loadUri(iv_show_roomadmin, item.getImg());
        helper.setText(R.id.tv_name_roomadmin, item.getName());
        ImageView iv_sex_onlines = helper.getView(R.id.iv_sex_roomadmin);
        if (item.getSex() == 1) { //男
            iv_sex_onlines.setImageResource(R.drawable.sex_male);
        } else if (item.getSex() == 2) {
            iv_sex_onlines.setImageResource(R.drawable.sex_female);
        }
        helper.addOnClickListener(R.id.tv_endcenter_roomadmin);
//        helper.setText(R.id.user_id,"ID:"+item.getId());

    }
}
