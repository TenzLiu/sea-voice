package com.jingtaoi.yy.ui.room;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.HotMusicBean;
import com.jingtaoi.yy.model.MusicLibBean;
import com.jingtaoi.yy.ui.room.adapter.PlaceMusicListAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.MyUtils;
import com.jingtaoi.yy.utils.ScanMusicUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 本地音乐页面
 */
public class PlaceMusicActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    PlaceMusicListAdapter musicListAdapter;
    List<HotMusicBean.DataBean> list;
    List<HotMusicBean.DataBean> searchList;
    @BindView(R.id.iv_back_place)
    ImageView ivBackPlace;
    @BindView(R.id.edt_search_palce)
    EditText edtSearchPalce;
    @BindView(R.id.tv_cancel_place)
    TextView tvCancelPlace;
    @BindView(R.id.ll_search_place)
    LinearLayout llSearchPlace;

    @Override
    public void initData() {
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_place);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {

        setTitleText(R.string.title_mymusic);
        setRightImg(getDrawable(R.drawable.search));

        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitle(false);
                llSearchPlace.setVisibility(View.VISIBLE);
            }
        });

        setRecycler();
        setMusicShow();
        mSwipeRefreshLayout.setEnabled(false);

        edtSearchPalce.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String searchStr = edtSearchPalce.getText().toString();
            if (!StringUtils.isEmpty(searchStr)) {
                setSearch(searchStr);
            }
        }
    };

    private void setSearch(String searchStr) {
        if (searchList == null) {
            searchList = new ArrayList<>();
        } else {
            searchList.clear();
        }
        for (HotMusicBean.DataBean dataBean : list) {
            if (dataBean.getMusicName().contains(searchStr)) {
                searchList.add(dataBean);
                continue;
            }
            if (dataBean.getGsNane().contains(searchStr)) {
                searchList.add(dataBean);
                continue;
            }
        }
        musicListAdapter.setNewData(searchList);
    }

    private void setRecycler() {
        musicListAdapter = new PlaceMusicListAdapter(R.layout.item_mymusic);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(musicListAdapter);
        musicListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_show_mymusic:
                        HotMusicBean.DataBean dataBean = (HotMusicBean.DataBean) adapter.getItem(position);
                        assert dataBean != null;
                        if (dataBean.getMusicState() == 0) {
                            MusicLibBean musicLibBean = new MusicLibBean();
                            musicLibBean.setMusicName(dataBean.getMusicName());
                            musicLibBean.setGsNane(dataBean.getGsNane());
                            musicLibBean.setDataLenth(dataBean.getDataLenth());
                            musicLibBean.setUrl(dataBean.getUrl());
                            musicLibBean.setMusicState(Const.IntShow.ONE);
                            if (musicLibBean.save()) {
                                showToast("添加成功");
                                dataBean.setMusicState(1);
                                musicListAdapter.notifyItemChanged(position, dataBean);
                            } else {
                                showToast("添加失败，请稍后再试");
                            }

                        }
                        break;
                }
            }
        });


        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        tvNodata.setText(getString(R.string.tv_nodata_music));
        musicListAdapter.setEmptyView(view);

//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                setMusicShow();
//            }
//        });
    }

    private void setMusicShow() {
        showDialog();
        list = ScanMusicUtils.getMusicData(this);
        List<MusicLibBean> placeList = LitePal.findAll(MusicLibBean.class);
        for (HotMusicBean.DataBean data : list) {
            for (int i = 0; i < placeList.size(); i++) {
                if (data.getMusicName().equals(placeList.get(i).getMusicName())) {
                    data.setMusicState(1);
                }
            }
        }
        musicListAdapter.setNewData(list);
        dismissDialog();
    }

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

    @OnClick({R.id.iv_back_place, R.id.tv_cancel_place})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_place:
                ActivityCollector.getActivityCollector().finishActivity();
                break;
            case R.id.tv_cancel_place:
                showTitle(true);
                llSearchPlace.setVisibility(View.GONE);
                musicListAdapter.setNewData(list);
                MyUtils.getInstans().hideKeyboard(tvCancelPlace);
                break;
        }
    }
}
