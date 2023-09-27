package com.siyecaoi.yy.ui.room.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.HotMusicBean;

public class HotMusicListAdapter extends BaseQuickAdapter<HotMusicBean.DataBean, BaseViewHolder> {


    public HotMusicListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotMusicBean.DataBean item) {
        TextView tv_name_music = helper.getView(R.id.tv_name_music);
        TextView tv_type_music = helper.getView(R.id.tv_type_music);
        ImageView iv_show_music = helper.getView(R.id.iv_show_music);

        tv_name_music.setText(item.getMusicName());
        tv_type_music.setText(item.getGsNane());

        if (item.getMusicState() == 0) {
            iv_show_music.setImageResource(R.drawable.songbook_download);
        } else if (item.getMusicState() == 1) {
            iv_show_music.setImageResource(R.drawable.songbook_play);
        } else if (item.getMusicState() == 2) {
            iv_show_music.setImageResource(R.drawable.songbook_spause);
        } else if (item.getMusicState() == 3) {
            iv_show_music.setImageResource(R.drawable.songbook_play);
        }

        helper.addOnClickListener(R.id.iv_show_music);
    }

}
