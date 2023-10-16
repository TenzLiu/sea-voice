package com.jingtaoi.yy.ui.room.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.HotMusicBean;
import com.jingtaoi.yy.utils.ScanMusicUtils;

public class PlaceMusicListAdapter extends BaseQuickAdapter<HotMusicBean.DataBean, BaseViewHolder> {


    public PlaceMusicListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotMusicBean.DataBean item) {
        TextView tv_name_music = helper.getView(R.id.tv_name_mymusic);
        TextView tv_type_music = helper.getView(R.id.tv_type_mymusic);
        ImageView iv_show_music = helper.getView(R.id.iv_show_mymusic);

        tv_name_music.setText(item.getMusicName()+"-"+item.getGsNane());
        tv_type_music.setText(ScanMusicUtils.formatTime(item.getDataLenth()));

        if (item.getMusicState() == 0) {
            iv_show_music.setImageResource(R.drawable.add);
            helper.addOnClickListener(R.id.iv_show_mymusic);
        } else if (item.getMusicState() == 1) {
            iv_show_music.setImageResource(R.drawable.select_theme);
        }

    }

}
