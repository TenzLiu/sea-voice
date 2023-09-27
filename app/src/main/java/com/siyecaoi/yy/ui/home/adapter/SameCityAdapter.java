package com.siyecaoi.yy.ui.home.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.HomeItemData;
import com.siyecaoi.yy.utils.AudioRecoderUtils;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sinata.xldutils.adapter.HFRecyclerAdapter;
import cn.sinata.xldutils.adapter.util.ViewHolder;

/**
 * 同城适配器
 */
public class SameCityAdapter extends HFRecyclerAdapter<HomeItemData> {
    private AudioRecoderUtils utils;
    private AnimationDrawable animationDrawable;//在播放动画的View
    private int playingItem = -1;//在播放动画的position

    public SameCityAdapter(List<HomeItemData> mData) {
        super(mData, R.layout.item_same_city);
        utils = new AudioRecoderUtils();
    }

    public void stopPaly() {
        utils.stopPlayMusic();
        if (animationDrawable != null)
            stopAnim(animationDrawable);
    }

    @Override
    public void onBind(int position, HomeItemData hotData, ViewHolder holder) {
        View title = holder.bind(R.id.tv_title);
        TextView play = holder.bind(R.id.tv_play_voice);
        TextView sign = holder.bind(R.id.tv_sign);
        View afk = holder.bind(R.id.tv_akf);
        ((SimpleDraweeView) holder.bind(R.id.iv_head)).setImageURI(hotData.getImg());
        holder.setText(R.id.tv_count_1, "房间人数：" + hotData.getManNum());
        holder.setText(R.id.tv_name, hotData.getName());
        if (hotData.getSex() == 1)
            holder.bind(R.id.tv_name).setSelected(true);
        else
            holder.bind(R.id.tv_name).setSelected(false);
        Double distance = hotData.getDistance();
        if (distance >= 1000) {
            holder.setText(R.id.tv_distance, String.format("%.2fkm", distance / 1000));
        } else {
            holder.setText(R.id.tv_distance, String.format("%.2fm", distance));
        }
        holder.setText(R.id.tv_fans_count, "粉丝：" + hotData.getNum());
        if (playingItem == position) //记录动画情况
            animationDrawable.start();
        else {
            if (animationDrawable != null)
                stopAnim(animationDrawable);
        }
        if (hotData.getVoiceTime() == 0) {
            play.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        } else {
            play.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            play.setText(hotData.getVoiceTime() + "''");
            play.setOnClickListener(v -> {
                utils.startplayMusic(mContext, hotData.getVoice());
                if (animationDrawable != null) {
                    stopAnim(animationDrawable);
                }
                AnimationDrawable compoundDrawable = (AnimationDrawable) play.getCompoundDrawables()[0];
                animationDrawable = compoundDrawable;
                compoundDrawable.start();
                playingItem = position;
                new CountDownTimer(hotData.getVoiceTime() * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        if (playingItem == position) {
                            stopAnim(compoundDrawable);
                            playingItem = -1;
                        }
                    }
                }.start();
            });
        }
        ((TextView) holder.bind(R.id.tv_level)).setCompoundDrawablesWithIntrinsicBounds(ImageShowUtils.getGrade(hotData.getTreasureGrade()), 0, 0, 0);
        ((TextView) holder.bind(R.id.tv_star)).setCompoundDrawablesWithIntrinsicBounds(ImageShowUtils.getCharm(hotData.getCharmGrade()), 0, 0, 0);
        String individuation = hotData.getIndividuation();
        if (individuation == null || individuation.isEmpty()) {
            sign.setText("个性签名：这家伙很懒，还没 有个性签名。");
        } else
            sign.setText("个性签名：" + individuation);
        if (hotData.getType() == 1)
            afk.setVisibility(View.GONE);
        else
            afk.setVisibility(View.VISIBLE);
    }

    private void stopAnim(AnimationDrawable animationDrawable) {
        animationDrawable.stop();
        animationDrawable.selectDrawable(0);
    }
}
