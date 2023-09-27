package com.siyecaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.MyUpdateBean;

public class MyUpdateListAdapter extends BaseQuickAdapter<MyUpdateBean.DataBean, BaseViewHolder> {


    public MyUpdateListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyUpdateBean.DataBean item) {
        TextView tv_name_music = helper.getView(R.id.tv_name_mymusic);
        TextView tv_type_music = helper.getView(R.id.tv_type_mymusic);
        ImageView iv_show_music = helper.getView(R.id.iv_show_mymusic);

        iv_show_music.setVisibility(View.INVISIBLE);
        tv_name_music.setText(item.getMusicName());
        tv_type_music.setText(item.getGsNane());

    }

}
