package com.siyecaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.model.MusicLibBean;
import com.siyecaoi.yy.ui.room.MusicActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;


/**
 * 音乐弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyMusicDialog extends Dialog {

    @BindView(R.id.tv_name_music)
    TextView tvNameMusic;
    @BindView(R.id.iv_menu_music)
    ImageView ivMenuMusic;
    @BindView(R.id.iv_pre_music)
    ImageView ivPreMusic;
    @BindView(R.id.iv_play_music)
    ImageView ivPlayMusic;
    @BindView(R.id.iv_next_music)
    ImageView ivNextMusic;
    @BindView(R.id.ll_back_music)
    LinearLayout llBackMusic;
    @BindView(R.id.ll_center_music)
    LinearLayout llCenterMusic;
    @BindView(R.id.seekbar_play_voice)
    SeekBar seekbarPlayVoice;

    Context context;
    int volumeShow;

    private int playState;
    private List<MusicLibBean> list;
    MusicLibBean musicLibBean;
    private int chooseOne;

    public MyMusicDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_music);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        setShow();
        seekbarPlayVoice.setMax(100);
        int musicVolume = (int) SharedPreferenceUtils.get(context, Const.User.VOLUME, 50);
        seekbarPlayVoice.setProgress(musicVolume);

        updateShow();
    }

    private boolean isCanChange;

    private void setShow() {

        seekbarPlayVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeShow = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isCanChange = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isCanChange = true;
                setChange(volumeShow);
            }
        });
    }

    /**
     * 更新播放文件
     */
    public void updateShow() {
        if (Const.MusicShow.isHave) {
            if (StringUtils.isEmpty(Const.MusicShow.musicName)) {
                tvNameMusic.setText("暂无歌曲播放");
                Const.MusicShow.isHave = false;
            } else {
                tvNameMusic.setText(Const.MusicShow.musicName);
            }
            playState = Const.MusicShow.musicPlayState;
            if (playState == 2) {
                ivPlayMusic.setImageResource(R.drawable.pause);
            } else {
                ivPlayMusic.setImageResource(R.drawable.music_play);
            }
        } else {
            tvNameMusic.setText("暂无歌曲播放");
        }
    }

    /**
     * 播放下一首（自动播放）
     */
    public void nextPlay() {
        setNextPlay();
        BroadcastManager.getInstance(context).sendBroadcast(Const.BroadCast.MUSIC_TONEXT);
    }

    private void setChange(int progress) {
//        seekbarPlayVoice.setProgress(progress);
        SharedPreferenceUtils.put(context, Const.User.VOLUME, progress);
        MyApplication.getInstance().getWorkerThread().musicVolumeSet(progress);
    }

    @OnClick({R.id.iv_menu_music, R.id.iv_pre_music, R.id.iv_play_music,
            R.id.iv_next_music, R.id.ll_center_music})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_menu_music:
                ActivityCollector.getActivityCollector().toOtherActivity(MusicActivity.class);
                break;
            case R.id.iv_pre_music:
                list = LitePal.findAll(MusicLibBean.class);
                if (list.size() > 0) {
                    if (chooseOne > 0) {
                        chooseOne--;
                    } else {
                        chooseOne = list.size() - 1;
                    }
                    musicLibBean = list.get(chooseOne);
                    playState = 2;
                    Const.MusicShow.isHave = true;
                    Const.MusicShow.musicLength = musicLibBean.getDataLenth();
                    Const.MusicShow.musicName = musicLibBean.getMusicName();
                    Const.MusicShow.musicPath = musicLibBean.getUrl();
                    Const.MusicShow.id = musicLibBean.getId();
                    Const.MusicShow.musicId = musicLibBean.getMusicId();
                    MyApplication.getInstance().getWorkerThread().musicPlay(musicLibBean.getUrl());
                    ivPlayMusic.setImageResource(R.drawable.pause);
                    tvNameMusic.setText(musicLibBean.getMusicName());
                    Const.MusicShow.musicPlayState = playState;
                }
                break;
            case R.id.iv_play_music:
                if (playState == 2) {
                    playState = 3;
                    ivPlayMusic.setImageResource(R.drawable.music_play);
//                    MyApplication.getInstance().getWorkerThread().musicPause();
                    Const.MusicShow.musicPlayState = playState;
                    BroadcastManager.getInstance(context).sendBroadcast(Const.BroadCast.MUSIC_PAUSE);
                } else if (playState == 3) {
                    playState = 2;
                    ivPlayMusic.setImageResource(R.drawable.pause);
//                    MyApplication.getInstance().getWorkerThread().musicReplay();
                    Const.MusicShow.musicPlayState = playState;
                    BroadcastManager.getInstance(context).sendBroadcast(Const.BroadCast.MUSIC_REPLAY);
                } else {
                    list = LitePal.findAll(MusicLibBean.class);
                    if (list.size() > 0) {
                        musicLibBean = list.get(chooseOne);
                        playState = 2;
                        Const.MusicShow.isHave = true;
                        Const.MusicShow.musicLength = musicLibBean.getDataLenth();
                        Const.MusicShow.musicName = musicLibBean.getMusicName();
                        Const.MusicShow.musicPath = musicLibBean.getUrl();
                        Const.MusicShow.id = musicLibBean.getId();
                        Const.MusicShow.musicId = musicLibBean.getMusicId();
//                        MyApplication.getInstance().getWorkerThread().musicPlay(musicLibBean.getUrl());
                        tvNameMusic.setText(musicLibBean.getMusicName());
                        Const.MusicShow.musicPlayState = playState;
                        ivPlayMusic.setImageResource(R.drawable.pause);
                        BroadcastManager.getInstance(context).sendBroadcast(Const.BroadCast.MUSIC_PLAY, musicLibBean.getUrl());
                    }
                }
                break;
            case R.id.iv_next_music:
                setNextPlay();
                break;
            case R.id.ll_center_music:
                break;
        }
    }

    /**
     * 手动点击下一曲
     */
    private void setNextPlay() {
        list = LitePal.findAll(MusicLibBean.class);
        if (list.size() > 0) {
            if (chooseOne < list.size() - 1) {
                chooseOne++;
            } else {
                chooseOne = 0;
            }
            musicLibBean = list.get(chooseOne);
            playState = 2;
            Const.MusicShow.isHave = true;
            Const.MusicShow.musicLength = musicLibBean.getDataLenth();
            Const.MusicShow.musicName = musicLibBean.getMusicName();
            Const.MusicShow.musicPath = musicLibBean.getUrl();
            Const.MusicShow.id = musicLibBean.getId();
            Const.MusicShow.musicId = musicLibBean.getMusicId();
//            MyApplication.getInstance().getWorkerThread().musicPlay(musicLibBean.getUrl());
            ivPlayMusic.setImageResource(R.drawable.pause);
            tvNameMusic.setText(musicLibBean.getMusicName());
            Const.MusicShow.musicPlayState = playState;
            BroadcastManager.getInstance(context).sendBroadcast(Const.BroadCast.MUSIC_PLAY, musicLibBean.getUrl());
        }
    }

    @OnClick(R.id.ll_back_music)
    public void onViewClicked() {
        dismiss();
    }
}