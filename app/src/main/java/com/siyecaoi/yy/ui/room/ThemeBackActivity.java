package com.siyecaoi.yy.ui.room;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.ThemeBackBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.RoomBackListAdapter;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主题背景页面
 */
public class ThemeBackActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    RoomBackListAdapter roomBackListAdapter;
    String roomImg;
    String roomId;
    String roomBackName;

    @Override
    public void initData() {
        roomImg = getBundleString(Const.ShowIntent.IMG);
        roomId = getBundleString(Const.ShowIntent.ROOMID);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.layout_recycler_top);
    }

    @Override
    public void initView() {
        setTitleText(R.string.title_theme);
        setRecycler();
        getCall();
        setRightText(R.string.tv_save);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUpdateCall();
            }
        });
    }

    private void getUpdateCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", userToken);
        map.put("bjImg", roomImg);
        map.put("bjName", roomBackName);
        HttpManager.getInstance().post(Api.getUpRoom, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    showToast(getString(R.string.hint_save_topic));
                    Intent intent = new Intent();
                    intent.putExtra(Const.ShowIntent.NAME, roomBackName);
                    setResult(RESULT_OK, intent);
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.SaveRoomBackdrop, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                ThemeBackBean themeBackBean = JSON.parseObject(responseString, ThemeBackBean.class);
                if (themeBackBean.getCode() == Api.SUCCESS) {
                    roomBackListAdapter.setNewData(themeBackBean.getData());
                } else {
                    showToast(themeBackBean.getMsg());
                }
            }
        });
    }

    private void setRecycler() {
        roomBackListAdapter = new RoomBackListAdapter(R.layout.item_roomback);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(roomBackListAdapter);
        roomBackListAdapter.setRoomImg(roomImg);

        roomBackListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ThemeBackBean.DataBean dataBean = (ThemeBackBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                roomImg = dataBean.getImg();
                roomBackName = dataBean.getName();
                roomBackListAdapter.setRoomImg(roomImg);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCall();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
