package com.jingtaoi.yy.ui.room.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.model.MusicLibBean;

public class MyMusicListAdapter extends BaseQuickAdapter<MusicLibBean, BaseViewHolder> {


    public MyMusicListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicLibBean item) {
        TextView tv_name_music = helper.getView(R.id.tv_name_music);
        TextView tv_type_music = helper.getView(R.id.tv_type_music);
        ImageView iv_show_music = helper.getView(R.id.iv_show_music);

        tv_name_music.setText(item.getMusicName());
        tv_type_music.setText(item.getGsNane());

        if (item.getMusicState() == 2) {
            iv_show_music.setImageResource(R.drawable.songbook_spause);
        } else if (item.getMusicState() == 3) {
            iv_show_music.setImageResource(R.drawable.songbook_play);
        } else {
            iv_show_music.setImageResource(R.drawable.songbook_play);
        }
        helper.addOnClickListener(R.id.iv_show_music);
    }

}
