package com.siyecaoi.yy.ui.room;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.control.MusicSet;
import com.siyecaoi.yy.dialog.MyBottomShowDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ScanMusicUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.dialog.MyHintDialog;
import com.siyecaoi.yy.dialog.MyMemuDialog;
import com.siyecaoi.yy.ui.room.fragment.HotMusicFragment;
import com.siyecaoi.yy.ui.room.fragment.MyMusicFragment;
import com.siyecaoi.yy.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 音乐菜单页面
 */
public class MusicActivity extends MyBaseActivity {
    @BindView(R.id.tv_left_music)
    TextView tvLeftMusic;
    @BindView(R.id.tab_music)
    SlidingTabLayout tabMusic;
    @BindView(R.id.iv_more_music)
    ImageView ivMoreMusic;
    @BindView(R.id.mViewPager_music)
    ViewPager mViewPagerMusic;

    @BindView(R.id.tv_name_music)
    TextView tvNameMusic;
    @BindView(R.id.tv_update_music)
    TextView tvUpdateMusic;
    @BindView(R.id.tv_nowtime_music)
    TextView tvNowtimeMusic;
    @BindView(R.id.seekbar_music)
    SeekBar seekbarMusic;
    @BindView(R.id.tv_alltime_music)
    TextView tvAlltimeMusic;
    @BindView(R.id.iv_pre_music)
    ImageView ivPreMusic;
    @BindView(R.id.iv_play_music)
    ImageView ivPlayMusic;
    @BindView(R.id.iv_next_music)
    ImageView ivNextMusic;
    @BindView(R.id.ll_bottom_music)
    LinearLayout llBottomMusic;


    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    FragPagerAdapter fragPagerAdapter;
    MyMemuDialog myMemuDialog;
    MyHintDialog hintDialog;
    int nowLength;//当前进度
    int allLength;//总长度

    int playState;// 2 播放  3暂停

    boolean isCanChange = true;//判断seekbar是否可以更新

    boolean isMicDown;//是否下麦

    @Override
    public void initData() {

        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add(getString(R.string.title_music));
        titles.add(getString(R.string.tv_hot_music));
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_music);
    }

    @Override
    public void initView() {

        showTitle(false);
        setFrag();
        setShow();
        setBroadCast();

    }

    private void setBroadCast() {
        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MIC_DOWN, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isMicDown = intent.getBooleanExtra(Const.ShowIntent.DATA, false);
                if (isMicDown) {
                    showToast("你已被房主或管理员抱下麦位");
                    ActivityCollector.getActivityCollector().finishActivity(MusicActivity.class);
                }
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MUSIC_TONEXT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setShow();
                BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_NEXT);
            }
        });
    }

    int choosePosition;

    private void setShow() {
        if (Const.MusicShow.isHave) {
            if (timer != null) {
                timer.cancel();
            }
            llBottomMusic.setVisibility(View.VISIBLE);
            allLength = Const.MusicShow.musicLength;
            nowLength = MyApplication.getInstance().getWorkerThread().getMusicNow();
            playState = Const.MusicShow.musicPlayState;
            if (playState == 2) {
                tvNameMusic.setText(Const.MusicShow.musicName);
                tvAlltimeMusic.setText(ScanMusicUtils.formatTime(allLength));
                seekbarMusic.setMax(allLength);
                seekbarMusic.setProgress(nowLength);
                ivPlayMusic.setImageResource(R.drawable.pause);
                tvNowtimeMusic.setText(ScanMusicUtils.formatTime(nowLength));
                initTimer();
                BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_REPLAY);
            } else if (playState == 3) {
                ivPlayMusic.setImageResource(R.drawable.music_play);
                BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_PAUSE);
                tvNameMusic.setText(Const.MusicShow.musicName);
                tvAlltimeMusic.setText(ScanMusicUtils.formatTime(allLength));
                seekbarMusic.setMax(allLength);
                seekbarMusic.setProgress(nowLength);
                tvNowtimeMusic.setText(ScanMusicUtils.formatTime(nowLength));
            }
        }

        seekbarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtils.e("msg","选中"+progress);
                choosePosition = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isCanChange = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isCanChange = true;
                setNowChange(choosePosition);
            }
        });
    }

    private void setNowChange(int progress) {
        nowLength = progress;
        MyApplication.getInstance().getWorkerThread().musicSetTime(progress);
    }

    private void setFrag() {
        MyMusicFragment myMusicFragment = new MyMusicFragment();
        HotMusicFragment hotMusicFragment = new HotMusicFragment();
        myMusicFragment.setMusicSet(musicSet);
        hotMusicFragment.setMusicSet(musicSet);
        fragments.add(myMusicFragment);
        fragments.add(hotMusicFragment);
        fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPagerMusic.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();
        tabMusic.setViewPager(mViewPagerMusic);
        mViewPagerMusic.setCurrentItem(0);
    }

    MusicSet musicSet = new MusicSet() {
        @Override
        public void musicPlay(String filePath, String name, int length) {
            if (isMicDown) {
                showToast("你已被房主或管理员抱下麦位");
                return;
            }
            llBottomMusic.setVisibility(View.VISIBLE);
            if (timer != null) {
                timer.cancel();
            }
            playState = 2;
            tvNameMusic.setText(name);
            allLength = length;
            tvAlltimeMusic.setText(ScanMusicUtils.formatTime(allLength));
            seekbarMusic.setMax(allLength);
            nowLength = 0;
            seekbarMusic.setProgress(nowLength);
            ivPlayMusic.setImageResource(R.drawable.pause);
            initTimer();
            Const.MusicShow.musicPlayState = playState;
            Disposable subscribe = Observable.just("0").observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_PLAY, filePath);
                            BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_CHANGE);
                        }
                    });
        }

        @Override
        public void musicPause() {
            if (isMicDown) {
                showToast("你已被房主或管理员抱下麦位");
                return;
            }
            if (timer != null) {
                timer.cancel();
            }
            playState = 3;
            ivPlayMusic.setImageResource(R.drawable.music_play);
            Const.MusicShow.musicPlayState = playState;
            Disposable subscribe = Observable.just("0").observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_PAUSE);
                            BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_CHANGE);
                        }
                    });
        }

        @Override
        public void musicRePlay() {
            if (isMicDown) {
                showToast("你已被房主或管理员抱下麦位");
                return;
            }
            if (timer != null) {
                timer.cancel();
            }
            initTimer();
            playState = 2;
            ivPlayMusic.setImageResource(R.drawable.pause);
            Const.MusicShow.musicPlayState = playState;
            Disposable subscribe = Observable.just("0").observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_REPLAY);
                            BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_CHANGE);
                        }
                    });
        }
    };

    Timer timer;

    private void initTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 101;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
                    if (nowLength < allLength) {
                        nowLength += 1000;
                        if (isCanChange) {
                            tvNowtimeMusic.setText(ScanMusicUtils.formatTime(nowLength));
                            seekbarMusic.setProgress(nowLength);
                        }
                    } else {
                        if (timer != null) {
                            timer.cancel();
                        }
                        playState = 0;
                        ivPlayMusic.setImageResource(R.drawable.music_play);
                        Const.MusicShow.musicPlayState = playState;
                        BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_CHANGE);
                    }
                    break;
            }
        }
    };

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MIC_DOWN);
    }

    @OnClick({R.id.tv_left_music, R.id.iv_more_music, R.id.tv_update_music,
            R.id.iv_pre_music, R.id.iv_play_music, R.id.iv_next_music})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_left_music:
                ActivityCollector.getActivityCollector().finishActivity();
                break;
            case R.id.iv_more_music:
                showMore();
                break;
            case R.id.tv_update_music:
                if (Const.MusicShow.musicId == 0) {
                    showToast("只能举报下载歌曲");
                } else {
                    showUpdateDialog();
                }
                break;
            case R.id.iv_pre_music:
                if (timer != null) {
                    timer.cancel();
                }
                BroadcastManager.getInstance(this).sendBroadcast(Const.BroadCast.MUSIC_PRE);
                break;
            case R.id.iv_play_music:
                setPlayChange();
                break;
            case R.id.iv_next_music:
                if (timer != null) {
                    timer.cancel();
                }
                BroadcastManager.getInstance(this).sendBroadcast(Const.BroadCast.MUSIC_NEXT);
                break;
        }
    }

    MyBottomShowDialog myBottomShowDialog;
    private ArrayList<String> bottomList = new ArrayList<>();//弹框显示内容

    private void showUpdateDialog() {
        bottomList.clear();
        bottomList.add(getString(R.string.tv_update1_music));
        bottomList.add(getString(R.string.tv_update2_music));
        bottomList.add(getString(R.string.tv_update3_music));
        bottomList.add(getString(R.string.tv_update4_music));
        bottomList.add(getString(R.string.tv_cancel));
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(this, bottomList);
        myBottomShowDialog.show();

        BottomShowRecyclerAdapter adapter = myBottomShowDialog.getAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 4:
                        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                            myBottomShowDialog.dismiss();
                        }
                        break;
                    default:
                        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                            myBottomShowDialog.dismiss();
                        }
                        String updateString = (String) adapter.getItem(position);
                        updateCall(updateString);
                        break;
                }
            }
        });
    }

    //举报歌曲
    private void updateCall(String updateString) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", Const.IntShow.THREE);
        map.put("buid", Const.MusicShow.musicId);
        map.put("content", updateString);
        HttpManager.getInstance().post(Api.ReportSave, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("举报成功，我们会尽快为您处理");
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    //改变状态
    private void setPlayChange() {
        if (isMicDown) {
            showToast("你已被房主或管理员抱下麦位");
            return;
        }
        if (playState == 2) {
            if (timer != null) {
                timer.cancel();
            }
            playState = 3;
            ivPlayMusic.setImageResource(R.drawable.music_play);
            BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_PAUSE);
            Const.MusicShow.musicPlayState = playState;
            BroadcastManager.getInstance(this).sendBroadcast(Const.BroadCast.MUSIC_CHANGE);
        } else if (playState == 3) {
            initTimer();
            playState = 2;
            ivPlayMusic.setImageResource(R.drawable.pause);
            BroadcastManager.getInstance(MusicActivity.this).sendBroadcast(Const.BroadCast.MUSIC_REPLAY);
            Const.MusicShow.musicPlayState = playState;
            BroadcastManager.getInstance(this).sendBroadcast(Const.BroadCast.MUSIC_CHANGE);
        } else {
            if (timer != null) {
                timer.cancel();
            }
            BroadcastManager.getInstance(this).sendBroadcast(Const.BroadCast.MUSIC_NEXT);
        }
    }

    private void showMore() {
        if (myMemuDialog != null && myMemuDialog.isShowing()) {
            myMemuDialog.dismiss();
        }
        myMemuDialog = new MyMemuDialog(this);
        myMemuDialog.show();
        myMemuDialog.setUpdate(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMemuDialog.dismiss();
                showHint();
            }
        });
    }

    private void showHint() {
        if (hintDialog != null && hintDialog.isShowing()) {
            hintDialog.dismiss();
        }
        hintDialog = new MyHintDialog(this);
        hintDialog.show();
//        hintDialog.setHintText("请到 惊涛语音-电脑端进行音乐上传！ 官网www.youyou.com");
        hintDialog.setHintText("请联系客服上传音乐");
    }

}
