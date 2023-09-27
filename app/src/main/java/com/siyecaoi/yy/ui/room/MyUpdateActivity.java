package com.siyecaoi.yy.ui.room;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.MyUpdateBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.MyUpdateListAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的上传页面
 */
public class MyUpdateActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    MyUpdateListAdapter updateListAdapter;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.layout_recycler_top);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {

        setTitleText(R.string.title_myupdate);

        setRecycler();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getCall();
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid",userToken);
        map.put("pageSize",PAGE_SIZE);
        map.put("pageNum",page);
        HttpManager.getInstance().post(Api.userMusice, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    updateListAdapter.setEnableLoadMore(true);
                }
                MyUpdateBean baseBean = JSON.parseObject(responseString, MyUpdateBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    setData(baseBean.getData());
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void setData(List<MyUpdateBean.DataBean> data) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            updateListAdapter.setNewData(data);
        } else {
            if (size > 0) {
                updateListAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            updateListAdapter.loadMoreEnd(false);
        } else {
            updateListAdapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
        updateListAdapter = new MyUpdateListAdapter(R.layout.item_mymusic);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(updateListAdapter);

        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        tvNodata.setText(getString(R.string.tv_nodata_musicupdate));
        updateListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCall();
            }
        });

        updateListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);


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
