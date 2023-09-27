package com.siyecaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.ThemeBackBean;
import com.siyecaoi.yy.utils.ImageUtils;

public class RoomBackListAdapter extends BaseQuickAdapter<ThemeBackBean.DataBean, BaseViewHolder> {

    String roomImg;

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
        notifyDataSetChanged();
    }

    public RoomBackListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeBackBean.DataBean item) {
        ImageView iv_choose_roomback = helper.getView(R.id.iv_choose_roomback);
        SimpleDraweeView iv_show_roomback = helper.getView(R.id.iv_show_roomback);
        ImageUtils.loadUri(iv_show_roomback, item.getImg());
        if (roomImg.equals(item.getImg())) {
            iv_choose_roomback.setVisibility(View.VISIBLE);
        } else {
            iv_choose_roomback.setVisibility(View.INVISIBLE);
        }
    }
}
