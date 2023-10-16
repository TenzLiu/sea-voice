package com.jingtaoi.yy.ui.room.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.ThemeBackBean;
import com.jingtaoi.yy.utils.ImageUtils;

public class RoomBgListAdapter extends BaseQuickAdapter<ThemeBackBean.DataBean, BaseViewHolder> {

    String roomImg;

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
        notifyDataSetChanged();
    }

    public RoomBgListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeBackBean.DataBean item) {
        View cl_bg = helper.getView(R.id.cl_bg);
        SimpleDraweeView iv_show_roomback = helper.getView(R.id.iv_bg);
        ImageUtils.loadUri(iv_show_roomback, item.getImg());
        helper.setText(R.id.tv_name,item.getName());
        if (roomImg.equals(item.getImg())) {
            cl_bg.setBackgroundResource(R.drawable.bg_green_line_6dp);
        } else {
            cl_bg.setBackgroundResource(R.drawable.bg_grey_line_6dp);
        }
    }
}
