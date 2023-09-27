package com.siyecaoi.yy.ui.room;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.PkHistoryBean;
import com.siyecaoi.yy.bean.PkSetBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.PkHistoryListAdapter;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PkHistoryActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    PkHistoryListAdapter pkHistoryListAdapter;
    String roomId;

    private boolean isCanClicker;

    @Override
    public void initData() {
        roomId = getBundleString(Const.ShowIntent.ROOMID);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.layout_recycler_top);
    }

    @Override
    public void initView() {
        isCanClicker = true;

        setTitleText(R.string.title_pkhistory);

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
        map.put("rid", roomId);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.ChatroomsPK, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    pkHistoryListAdapter.setEnableLoadMore(true);
                }
                PkHistoryBean pkHistoryBean = JSON.parseObject(responseString, PkHistoryBean.class);
                if (pkHistoryBean.getCode() == Api.SUCCESS) {
                    setData(pkHistoryBean.getData());
                } else {
                    showToast(pkHistoryBean.getMsg());
                }
            }
        });

    }

    private void setData(List<PkHistoryBean.DataBean> data) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            pkHistoryListAdapter.setNewData(data);
            if (size < PAGE_SIZE) {

            }
        } else {
            if (size > 0) {
                pkHistoryListAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            if (page == 1) {
                //第一页如果不够一页就不显示没有更多数据布局
                pkHistoryListAdapter.loadMoreEnd(true);
            } else {
                pkHistoryListAdapter.loadMoreEnd(false);
            }
        } else {
            pkHistoryListAdapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
        pkHistoryListAdapter = new PkHistoryListAdapter(R.layout.item_pkhistory);
        mRecyclerView.setAdapter(pkHistoryListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pkHistoryListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.btn_pk_pkhistory) {
                    PkHistoryBean.DataBean dataBean = (PkHistoryBean.DataBean) adapter.getItem(position);
                    assert dataBean != null;
                    if (isCanClicker) {
                        isCanClicker = false;
                        getPkCall(dataBean);
                    }

                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCall();
            }
        });

        pkHistoryListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);
    }


    private void getPkCall(PkHistoryBean.DataBean dataBean) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", dataBean.getWz1().getId());
        map.put("buid", dataBean.getWz2().getId());
        map.put("state", dataBean.getState());
        map.put("num", dataBean.getSecond());
        HttpManager.getInstance().post(Api.ChatroomsPk, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                isCanClicker = true;
                PkSetBean pkSetBean = JSON.parseObject(responseString, PkSetBean.class);
                if (pkSetBean.getCode() == Api.SUCCESS) {
                    showToast("开启Pk成功");
                    setResult(RESULT_OK);
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(pkSetBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                isCanClicker = true;
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
