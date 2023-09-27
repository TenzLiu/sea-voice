package com.siyecaoi.yy.ui.room.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.utils.ObsUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.HotMusicBean;
import com.siyecaoi.yy.control.MusicSet;
import com.siyecaoi.yy.model.MusicLibBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.HotMusicListAdapter;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.MyUtils;
import com.siyecaoi.yy.utils.OSSUtil;

import org.litepal.LitePal;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HotMusicFragment extends MyBaseFragment {

    Unbinder unbinder;
    @BindView(R.id.edt_hotmusic)
    EditText edtHotmusic;
    @BindView(R.id.mRecyclerView_hotmusic)
    RecyclerView mRecyclerViewHotmusic;

    HotMusicListAdapter hotMusicListAdapter;
    @BindView(R.id.mSwipeRefreshLayout_hotmusic)
    SwipeRefreshLayout mSwipeRefreshLayoutHotmusic;

    List<MusicLibBean> list;//数据库list

    List<HotMusicBean.DataBean> dataList;//热门列表

    int playState;// 2 播放  3暂停
    int playPosition = -1;

    MusicSet musicSet;


    public MusicSet getMusicSet() {
        return musicSet;
    }

    public void setMusicSet(MusicSet musicSet) {
        this.musicSet = musicSet;
    }

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotmusic, container, false);
        return view;
    }

    @Override
    public void initView() {

        setBrodcast();
        setRecycler();

        initShow();
    }

    private void initShow() {
        mSwipeRefreshLayoutHotmusic.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayoutHotmusic.setRefreshing(true);
                getMusicCall();
            }
        });

        edtHotmusic.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // do some your things
                    String searchString = edtHotmusic.getText().toString();
                    if (StringUtils.isEmpty(searchString)) {
                        showToast("请输入歌名或歌手名");
//                        return false;
                    }
                    MyUtils.getInstans().hideKeyboard(edtHotmusic);
                    mSwipeRefreshLayoutHotmusic.setRefreshing(true);
                    getMusicCall();
                    return true;
                }
                return false;
            }
        });
    }

    private void setBrodcast() {
        BroadcastManager.getInstance(getContext()).addAction(Const.BroadCast.MUSIC_CHANGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dataList = hotMusicListAdapter.getData();
                if (dataList.size() <= 0) {
                    return;
                }
                //移除之前的歌曲的状态
                HotMusicBean.DataBean dataBean;
                if (playPosition != -1) {
                    if (playPosition < dataList.size()) {
                        dataBean = dataList.get(playPosition);
                        dataBean.setMusicState(Const.IntShow.ONE);
                        hotMusicListAdapter.setData(playPosition, dataBean);
                        hotMusicListAdapter.notifyDataSetChanged();
                    }
                }
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).getId() == Const.MusicShow.musicId) {
                        dataBean = dataList.get(i);
                        playPosition = i;
                        dataBean.setMusicState(Const.MusicShow.musicPlayState);
                        hotMusicListAdapter.setData(i, dataBean);
                        hotMusicListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        BroadcastManager.getInstance(getContext()).addAction(Const.BroadCast.MUSIC_PRE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dataList = hotMusicListAdapter.getData();
                if (dataList.size() <= 0) {
                    return;
                }
                //移除之前的歌曲的状态
                HotMusicBean.DataBean dataBean;
                if (playPosition != -1) {
                    if (playPosition < dataList.size()) {
                        dataBean = dataList.get(playPosition);
                        dataBean.setMusicState(Const.IntShow.ONE);
                        hotMusicListAdapter.setData(playPosition, dataBean);
                        hotMusicListAdapter.notifyDataSetChanged();
                    }
                }
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).getId() == Const.MusicShow.musicId) {
                        dataBean = dataList.get(i);
                        playPosition = i;
                        dataBean.setMusicState(Const.MusicShow.musicPlayState);
                        hotMusicListAdapter.setData(i, dataBean);
                        hotMusicListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
        BroadcastManager.getInstance(getContext()).addAction(Const.BroadCast.MUSIC_NEXT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dataList = hotMusicListAdapter.getData();
                if (dataList.size() <= 0) {
                    return;
                }
                //移除之前的歌曲的状态
                HotMusicBean.DataBean dataBean;
                if (playPosition != -1) {
                    if (playPosition < dataList.size()) {
                        dataBean = dataList.get(playPosition);
                        dataBean.setMusicState(Const.IntShow.ONE);
                        hotMusicListAdapter.setData(playPosition, dataBean);
                        hotMusicListAdapter.notifyDataSetChanged();
                    }
                }
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).getId() == Const.MusicShow.musicId) {
                        dataBean = dataList.get(i);
                        playPosition = i;
                        dataBean.setMusicState(Const.MusicShow.musicPlayState);
                        hotMusicListAdapter.setData(i, dataBean);
                        hotMusicListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

    }

    private void getMusicCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        String searchString = edtHotmusic.getText().toString();
        if (!StringUtils.isEmpty(searchString)) {
            page = 1;
            map.put("name", searchString);
        }
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);

        HttpManager.getInstance().post(Api.getMusic, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayoutHotmusic != null && mSwipeRefreshLayoutHotmusic.isRefreshing()) {
                    mSwipeRefreshLayoutHotmusic.setRefreshing(false);
                    hotMusicListAdapter.setEnableLoadMore(true);
                }
                HotMusicBean hotMusicBean = JSON.parseObject(responseString, HotMusicBean.class);

                if (hotMusicBean.getCode() == Api.SUCCESS) {
                    setData(hotMusicBean.getData());
                } else {
                    showToast(hotMusicBean.getMsg());
                }
            }
        });
    }

    private void setData(List<HotMusicBean.DataBean> data) {
        final int size = data == null ? 0 : data.size();
        list = LitePal.findAll(MusicLibBean.class);
        for (HotMusicBean.DataBean dataBean : data) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMusicId() == dataBean.getId()) {
                    dataBean.setMusicState(Const.IntShow.ONE);
                    if (Const.MusicShow.isHave) {
                        if (list.get(i).getId() == Const.MusicShow.id) {
                            dataBean.setMusicState(Const.MusicShow.musicPlayState);
                        }
                    }
                }
            }
        }
        if (page == 1) {
            hotMusicListAdapter.setNewData(data);
        } else {
            if (size > 0) {
                hotMusicListAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            if (page == 1) {
                //第一页如果不够一页就不显示没有更多数据布局
                hotMusicListAdapter.loadMoreEnd(true);
            } else {
                hotMusicListAdapter.loadMoreEnd(false);
            }
        } else {
            hotMusicListAdapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
        hotMusicListAdapter = new HotMusicListAdapter(R.layout.item_hotmusic);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewHotmusic.setLayoutManager(layoutManager);
        mRecyclerViewHotmusic.setAdapter(hotMusicListAdapter);

        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        tvNodata.setText(getString(R.string.tv_nodata_music));
        hotMusicListAdapter.setEmptyView(view);

        hotMusicListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_show_music:
                        HotMusicBean.DataBean dataBean = (HotMusicBean.DataBean) adapter.getItem(position);
                        assert dataBean != null;
                        switch (dataBean.getMusicState()) {
                            case 0://未下载
//                                DownLoadMusic(dataBean, position);
                                getDownLoadCall(dataBean, position);
                                break;
                            case 1://已下载
                                dataList = hotMusicListAdapter.getData();
                                if (playPosition != -1) {
                                    if (playPosition < dataList.size()) {
                                        dataBean = dataList.get(playPosition);
                                        dataBean.setMusicState(Const.IntShow.ONE);
                                        hotMusicListAdapter.setData(playPosition, dataBean);
                                        hotMusicListAdapter.notifyDataSetChanged();
                                    }
                                }
                                if (musicSet != null) {
                                    Const.MusicShow.isHave = true;
                                    Const.MusicShow.musicLength = dataBean.getTimes() * 1000;
                                    Const.MusicShow.musicName = dataBean.getMusicName();
                                    Const.MusicShow.musicPath = dataBean.getUrl();
                                    Const.MusicShow.id = dataBean.getId();
                                    Const.MusicShow.musicId = dataBean.getId();
                                    Const.MusicShow.musicPlayState = 2;
                                    musicSet.musicPlay(dataBean.getUrl(), dataBean.getMusicName(), dataBean.getTimes() * 1000);
                                    playState = 2;
                                    playPosition = position;
                                }
                                break;
                            case 2://播放
                                if (musicSet != null) {
                                    musicSet.musicPause();
                                    playState = 3;
                                }
                                break;
                            case 3://暂停
                                if (musicSet != null) {
                                    musicSet.musicRePlay();
                                    playState = 2;
                                }
                                break;
                        }
                        break;
                }
            }
        });

        mSwipeRefreshLayoutHotmusic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getMusicCall();
            }
        });

        hotMusicListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getMusicCall();
            }
        }, mRecyclerViewHotmusic);
    }

    private void getDownLoadCall(HotMusicBean.DataBean dataBean, int positon) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", dataBean.getId());
        HttpManager.getInstance().post(Api.userMusiceNum, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    downLoadMusicObs(dataBean, positon);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void downLoadMusicObs(HotMusicBean.DataBean dataBean, int positon) {
        showDialog("下载中...");
        ObsUtils obsUtils = new ObsUtils();
        File file = getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        String filePath = file.getPath() + "/" + dataBean.getMusicName() + System.currentTimeMillis() + ".mp3";
        LogUtils.e("msg", filePath);
        obsUtils.downloadFile(dataBean.getObjectKey(), filePath, new ObsUtils.ObsCallback() {
            @Override
            public void onUploadFileSuccess(String url) {
                dismissDialog();
                MusicLibBean musicLibBean = new MusicLibBean();
                musicLibBean.setUrl(url);
                musicLibBean.setMusicName(dataBean.getMusicName());
                musicLibBean.setMusicId(dataBean.getId());
                musicLibBean.setGsNane(dataBean.getGsNane());
                musicLibBean.setMusicState(0);
                musicLibBean.setDataLenth(dataBean.getTimes() * 1000);
                musicLibBean.save();
                Observable<String> observable = Observable.just("0");
                Disposable subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                dataBean.setMusicState(Const.IntShow.ONE);
                                hotMusicListAdapter.setData(positon, dataBean);
                                hotMusicListAdapter.notifyDataSetChanged();
                            }
                        });
            }

            @Override
            public void onUploadMoreFielSuccess() {

            }

            @Override
            public void onFail(String message) {
                showToast("资源下载失败，请重试");
                LogUtils.e("obs" + message);
                dismissDialog();
            }
        });
    }

    /**
     * 下载音乐
     *
     * @param dataBean
     */
    private void DownLoadMusic(HotMusicBean.DataBean dataBean, int positon) {
        showDialog("下载中...");
        OSSUtil ossUtil = new OSSUtil(getActivity());
        File file = getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        String filePath = file.getPath() + "/" + dataBean.getMusicName() + System.currentTimeMillis() + ".mp3";
        LogUtils.e("msg", filePath);
        ossUtil.downLoadSingle(dataBean.getObjectKey(), filePath, new OSSUtil.OSSdownLoadCallback() {
            @Override
            public void onFinish() {
                super.onFinish();
                dismissDialog();
                MusicLibBean musicLibBean = new MusicLibBean();
                musicLibBean.setUrl(filePath);
                musicLibBean.setMusicName(dataBean.getMusicName());
                musicLibBean.setMusicId(dataBean.getId());
                musicLibBean.setGsNane(dataBean.getGsNane());
                musicLibBean.setMusicState(0);
                musicLibBean.setDataLenth(dataBean.getTimes() * 1000);
                musicLibBean.save();
                Observable<String> observable = Observable.just("0");
                Disposable subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                dataBean.setMusicState(Const.IntShow.ONE);
                                hotMusicListAdapter.setData(positon, dataBean);
                                hotMusicListAdapter.notifyDataSetChanged();
                            }
                        });
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                showToast(message);
                dismissDialog();
            }

            @Override
            public void onSizeProgress(Long currentSize, Long totalSize) {
                super.onSizeProgress(currentSize, totalSize);
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
