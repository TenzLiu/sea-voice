package com.jingtaoi.yy.ui.mine.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.TopupHisBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.mine.NewMyBillActivity;
import com.jingtaoi.yy.ui.mine.adapter.TopupHisListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;

public class TopupHistoryFragment extends MyBaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    TopupHisListAdapter topupHisListAdapter;
    String checkTime;
    Calendar calendar;//日期
    Unbinder unbinder;
    private NewMyBillActivity activity;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycler_top2, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {

        setRecycler();

        setRefresh();

        activity = (NewMyBillActivity) getActivity();
    }

    public void setRefresh() {
        topupHisListAdapter.setNewData(new ArrayList<>());
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                page = 1;
                getCall();
            }
        });
    }


    public void setCheckTime(String checkTime) {
        topupHisListAdapter.setNewData(new ArrayList<>());
        this.checkTime = checkTime;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                page = 1;
                getCall();
            }
        });
    }


    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        if (!StringUtils.isEmpty(checkTime)) {
            map.put("beginTime", checkTime);
            map.put("endTime", checkTime);
            View view = View.inflate(getContext(), R.layout.layout_nodata, null);
            TextView tv_nodata = view.findViewById(R.id.tv_nodata);
            tv_nodata.setText("您当日没有充值记录");
            topupHisListAdapter.setEmptyView(view);
        }
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.UserRecharge, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    topupHisListAdapter.setEnableLoadMore(true);
                }
                TopupHisBean topupHisBean = JSON.parseObject(responseString, TopupHisBean.class);
                if (topupHisBean.getCode() == Api.SUCCESS) {
                    setData(topupHisBean.getData());
                } else {
                    showToast(topupHisBean.getMsg());
                }
            }
        });
    }

    private void setData(List<TopupHisBean.DataEntity> data) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            topupHisListAdapter.setNewData(data);
        } else {
            if (size > 0) {
                topupHisListAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            topupHisListAdapter.loadMoreEnd(false);
        } else {
            topupHisListAdapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
//        mRecyclerView.setBackgroundResource(R.color.FBFBFB);
        topupHisListAdapter = new TopupHisListAdapter(R.layout.item_show1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(topupHisListAdapter);

        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
        topupHisListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCall();
            }
        });

        topupHisListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);
    }

    @Override
    public void setResume() {
        checkTime=activity.getCheckTime();
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
    }
}