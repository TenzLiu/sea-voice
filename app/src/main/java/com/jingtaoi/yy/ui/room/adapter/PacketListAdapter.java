package com.jingtaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.PacketBean;
import com.jingtaoi.yy.utils.ImageUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class PacketListAdapter extends BaseQuickAdapter<PacketBean.DataBean.RedNumBean, BaseViewHolder> {

    public PacketListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, PacketBean.DataBean.RedNumBean item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_onlines);
        ImageView iv_sex_onlines = helper.getView(R.id.iv_sex_onlines);
        TextView tv_name_onlines = helper.getView(R.id.tv_name_onlines);
        TextView tv_type_onlines = helper.getView(R.id.tv_type_onlines);
        TextView tv_endshow_onlines = helper.getView(R.id.tv_endshow_onlines);

        ImageUtils.loadUri(iv_show_onlines, item.getImg());
        iv_sex_onlines.setVisibility(View.INVISIBLE);
        tv_name_onlines.setText(item.getName());
        tv_type_onlines.setText(item.getTime());
        tv_endshow_onlines.setText(item.getNum() + mContext.getString(R.string.tv_youz_packet));

    }
}
