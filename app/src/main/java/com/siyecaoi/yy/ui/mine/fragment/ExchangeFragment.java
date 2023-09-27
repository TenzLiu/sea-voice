package com.siyecaoi.yy.ui.mine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.ExchangeHisBean;
import com.siyecaoi.yy.bean.GiftHisBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.NewMyBillActivity;
import com.siyecaoi.yy.ui.mine.adapter.ExchangeHisItemAdapter;
import com.siyecaoi.yy.ui.mine.adapter.GiftHisItemAdapter;
import com.siyecaoi.yy.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;

public class ExchangeFragment extends MyBaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    Unbinder unbinder;

    ExchangeHisItemAdapter giftHisListAdapter;
    String checkTime;//选择日期
    private NewMyBillActivity activity;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycler_top2, container, false);
    }

    @Override
    public void initView() {
        activity = (NewMyBillActivity) getActivity();
        setRecycler();
        setCheckTime("");
    }




    public void setCheckTime(String checkTime) {
        giftHisListAdapter.setNewData(new ArrayList<>());
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
            tv_nodata.setText("您当日没有兑换记录");
            giftHisListAdapter.setEmptyView(view);
        }
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.exchangeLog, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    giftHisListAdapter.setEnableLoadMore(true);
                }
                ExchangeHisBean giftHisBean = JSON.parseObject(responseString, ExchangeHisBean.class);
                if (giftHisBean.getCode() == Api.SUCCESS) {
                    setData(giftHisBean.getData());
                } else {
                    showToast(giftHisBean.getMsg());
                }
            }
        });
    }

    private void setData(List<ExchangeHisBean.ExchangeEntity> data) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            giftHisListAdapter.setNewData(data);
        } else {
            if (size > 0) {
                giftHisListAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            giftHisListAdapter.loadMoreEnd(false);
        } else {
            giftHisListAdapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
//        mRecyclerView.setBackgroundResource(R.color.FBFBFB);
        giftHisListAdapter = new ExchangeHisItemAdapter(R.layout.item_gifthis);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(giftHisListAdapter);

        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
        giftHisListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCall();
            }
        });
        giftHisListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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
