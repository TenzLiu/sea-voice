package com.siyecaoi.yy.ui.room.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.control.MusicSet;
import com.siyecaoi.yy.model.MusicLibBean;
import com.siyecaoi.yy.ui.room.adapter.MyMusicListAdapter;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.MyUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;

public class MyMusicFragment extends MyBaseFragment {

    @BindView(R.id.tv_all_mymusic)
    TextView tvAllMymusic;
    @BindView(R.id.mRecyclerView_mymusic)
    RecyclerView mRecyclerViewMymusic;
    Unbinder unbinder;

    MyMusicListAdapter myMusicListAdapter;
    List<MusicLibBean> list;

    String musicName;
    int id;//数据库id
    int playState;// 2 播放  3暂停
    int playPosition = -1;

    MusicSet musicSet;
    @BindView(R.id.edt_mymusic)
    EditText edtMymusic;
    @BindView(R.id.mSwipeRefreshLayout_mymusic)
    SwipeRefreshLayout mSwipeRefreshLayoutMymusic;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mymusic, container, false);
        return view;
    }

    @Override
    protected void onVisibleToUser() {
        super.onVisibleToUser();
        getMyMusic();
    }

    @Override
    public void initView() {

        setRecycler();
        getMyMusic();
        setBrodcast();

        edtMymusic.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // do some your things
                    getMusicShow();
                    return true;
                }
                return false;
            }
        });

        edtMymusic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchString = edtMymusic.getText().toString();
                if (StringUtils.isEmpty(searchString)) {
                    myMusicListAdapter.setNewData(list);
                }
            }
        });
    }

    private void getMusicShow() {
        String searchString = edtMymusic.getText().toString();
        if (StringUtils.isEmpty(searchString)) {
            showToast("请输入歌名或歌手名");
            return;
        }
        MyUtils.getInstans().hideKeyboard(edtMymusic);
        List<MusicLibBean> searchList = new ArrayList<>();
        for (MusicLibBean musicLib : list) {
            if (musicLib.getMusicName().contains(searchString)) {
                searchList.add(musicLib);
            } else if (musicLib.getGsNane().contains(searchString)) {
                searchList.add(musicLib);
            }
        }
        myMusicListAdapter.setNewData(searchList);
    }

    private void setBrodcast() {
        BroadcastManager.getInstance(getContext()).addAction(Const.BroadCast.MUSIC_CHANGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                list = myMusicListAdapter.getData();
                if (list.size() <= 0) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId() == Const.MusicShow.id) {
                        MusicLibBean musicLibBean = list.get(i);
                        musicLibBean.setMusicState(Const.MusicShow.musicPlayState);
                        myMusicListAdapter.setData(i, musicLibBean);
                        break;
                    }
                }
            }
        });

        BroadcastManager.getInstance(getContext()).addAction(Const.BroadCast.MUSIC_PRE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (list.size() <= 0) {
                    return;
                }
                //移除之前的歌曲的状态
                if (playPosition != -1) {
                    MusicLibBean oldMusic = (MusicLibBean) myMusicListAdapter.getItem(playPosition);
                    assert oldMusic != null;
                    oldMusic.setMusicState(0);
                    myMusicListAdapter.setData(playPosition, oldMusic);
                }

                if (playPosition > 0) {
                    playPosition--;
                } else {
                    playPosition = list.size() - 1;
                }
                MusicLibBean musicLibBean = list.get(playPosition);
                if (musicSet != null) {
                    Const.MusicShow.isHave = true;
                    Const.MusicShow.musicLength = musicLibBean.getDataLenth();
                    Const.MusicShow.musicName = musicLibBean.getMusicName();
                    Const.MusicShow.musicPath = musicLibBean.getUrl();
                    Const.MusicShow.id = musicLibBean.getId();
                    Const.MusicShow.musicId = musicLibBean.getMusicId();
                    musicSet.musicPlay(musicLibBean.getUrl(), musicLibBean.getMusicName(), musicLibBean.getDataLenth());
                    playState = 2;
                    Const.MusicShow.musicPlayState = playState;
                    musicLibBean.setMusicState(playState);
                    myMusicListAdapter.setData(playPosition, musicLibBean);
                }
            }
        });
        BroadcastManager.getInstance(getContext()).addAction(Const.BroadCast.MUSIC_NEXT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (list.size() <= 0) {
                    return;
                }
                //移除之前的歌曲的状态
                if (playPosition != -1) {
                    MusicLibBean oldMusic = (MusicLibBean) myMusicListAdapter.getItem(playPosition);
                    assert oldMusic != null;
                    oldMusic.setMusicState(0);
                    myMusicListAdapter.setData(playPosition, oldMusic);
                }
                //更新选中项
                if (playPosition < list.size() - 1) {
                    playPosition++;
                } else {
                    playPosition = 0;
                }
                MusicLibBean musicLibBean = list.get(playPosition);
                if (musicSet != null) {
                    Const.MusicShow.isHave = true;
                    Const.MusicShow.musicLength = musicLibBean.getDataLenth();
                    Const.MusicShow.musicName = musicLibBean.getMusicName();
                    Const.MusicShow.musicPath = musicLibBean.getUrl();
                    Const.MusicShow.id = musicLibBean.getId();
                    Const.MusicShow.musicId = musicLibBean.getMusicId();
                    musicSet.musicPlay(musicLibBean.getUrl(), musicLibBean.getMusicName(), musicLibBean.getDataLenth());
                    playState = 2;
                    Const.MusicShow.musicPlayState = playState;
                    musicLibBean.setMusicState(playState);
                    myMusicListAdapter.setData(playPosition, musicLibBean);
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void getMyMusic() {
        list = LitePal.findAll(MusicLibBean.class);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == Const.MusicShow.id) {
                list.get(i).setMusicState(Const.MusicShow.musicPlayState);
            }
        }
        tvAllMymusic.setText("全部：" + list.size() + "首");
        if (mSwipeRefreshLayoutMymusic != null && mSwipeRefreshLayoutMymusic.isRefreshing()) {
            mSwipeRefreshLayoutMymusic.setRefreshing(false);
        }
        myMusicListAdapter.setNewData(list);
    }

    public MusicSet getMusicSet() {
        return musicSet;
    }

    public void setMusicSet(MusicSet musicSet) {
        this.musicSet = musicSet;
    }

    private void setRecycler() {
        myMusicListAdapter = new MyMusicListAdapter(R.layout.item_hotmusic);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewMymusic.setAdapter(myMusicListAdapter);
        mRecyclerViewMymusic.setLayoutManager(layoutManager);

        mSwipeRefreshLayoutMymusic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyMusic();
            }
        });

        myMusicListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_show_music:
                        MusicLibBean musicLibBean = (MusicLibBean) adapter.getItem(position);
                        assert musicLibBean != null;
                        playState = musicLibBean.getMusicState();
                        if (playState == 2) { //暂停
                            if (musicSet != null) {
                                musicSet.musicPause();
                                playState = 3;
                            }
                        } else if (playState == 3) { //恢复播放
                            if (musicSet != null) {
                                musicSet.musicRePlay();
                                playState = 2;
                            }
                        } else { //播放
                            if (musicSet != null) {
                                Const.MusicShow.isHave = true;
                                Const.MusicShow.musicLength = musicLibBean.getDataLenth();
                                Const.MusicShow.musicName = musicLibBean.getMusicName();
                                Const.MusicShow.musicPath = musicLibBean.getUrl();
                                Const.MusicShow.id = musicLibBean.getId();
                                Const.MusicShow.musicId = musicLibBean.getMusicId();
                                musicSet.musicPlay(musicLibBean.getUrl(), musicLibBean.getMusicName(), musicLibBean.getDataLenth());
                                playState = 2;

                                if (playPosition != -1) {
                                    MusicLibBean oldMusic = (MusicLibBean) adapter.getItem(playPosition);
                                    assert oldMusic != null;
                                    oldMusic.setMusicState(0);
                                    myMusicListAdapter.setData(playPosition, oldMusic);
                                }
                            }
                        }

                        playPosition = position;
                        Const.MusicShow.musicPlayState = playState;
//                        musicLibBean.setMusicState(playState);
//                        myMusicListAdapter.notifyItemChanged(position, musicLibBean);
                        break;
                }
            }
        });

    }


    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        BroadcastManager.getInstance(getContext()).destroy(Const.BroadCast.MUSIC_CHANGE);
        BroadcastManager.getInstance(getContext()).destroy(Const.BroadCast.MUSIC_PRE);
        BroadcastManager.getInstance(getContext()).destroy(Const.BroadCast.MUSIC_NEXT);
    }
}
