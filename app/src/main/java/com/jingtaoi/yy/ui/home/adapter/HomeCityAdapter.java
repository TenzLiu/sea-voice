package com.jingtaoi.yy.ui.home.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.HomeItemData;
import com.jingtaoi.yy.utils.AudioRecoderUtils;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.utils.ImageUtils;

public class HomeCityAdapter extends BaseQuickAdapter<HomeItemData, BaseViewHolder> {
    private AudioRecoderUtils utils;
    private AnimationDrawable animationDrawable;//在播放动画的View
    private int playingItem = -1;//在播放动画的position

    public HomeCityAdapter(int layoutResId) {
        super(layoutResId);
        utils = new AudioRecoderUtils();
    }

    public void stopPaly() {
        utils.stopPlayMusic();
        if (animationDrawable != null)
            stopAnim(animationDrawable);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItemData hotData) {
        View title = helper.getView(R.id.tv_title);
        TextView play = helper.getView(R.id.tv_play_voice);
        TextView sign = helper.getView(R.id.tv_sign);
        View afk = helper.getView(R.id.tv_akf);
        ImageView ivHead = helper.getView(R.id.iv_head);
        ImageUtils.loadImage(ivHead,hotData.getImg(), 6, -1);
        helper.setText(R.id.tv_count_1, "房间人数：" + hotData.getManNum());
        helper.setText(R.id.tv_name, hotData.getName());
        if (hotData.getSex() == 1)
            helper.getView(R.id.tv_name).setSelected(true);
        else
            helper.getView(R.id.tv_name).setSelected(false);
        Double distance = hotData.getDistance();
        if (distance >= 1000) {
            helper.setText(R.id.tv_distance, String.format("%.2fkm", distance / 1000));
        } else {
            helper.setText(R.id.tv_distance, String.format("%.2fm", distance));
        }
        helper.setText(R.id.tv_fans_count, "粉丝：" + hotData.getNum());
        int position = helper.getAdapterPosition();
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
        ((TextView) helper.getView(R.id.tv_level)).setCompoundDrawablesWithIntrinsicBounds(ImageShowUtils.getGrade(hotData.getTreasureGrade()), 0, 0, 0);
        ((TextView) helper.getView(R.id.tv_star)).setCompoundDrawablesWithIntrinsicBounds(ImageShowUtils.getCharm(hotData.getCharmGrade()), 0, 0, 0);
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
