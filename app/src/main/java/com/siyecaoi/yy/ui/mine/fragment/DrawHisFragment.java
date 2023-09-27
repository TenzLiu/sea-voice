package com.siyecaoi.yy.ui.mine.fragment;

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
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.DrawHisBean;
import com.siyecaoi.yy.dialog.MyBirthdayDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.NewMyBillActivity;
import com.siyecaoi.yy.ui.mine.adapter.DrawHisListAdapter;
import com.siyecaoi.yy.utils.MyUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;

public class DrawHisFragment extends MyBaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    DrawHisListAdapter drawHisListAdapter;
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
        drawHisListAdapter.setNewData(new ArrayList<>());
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
        drawHisListAdapter.setNewData(new ArrayList<>());
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


    private void showBirthday(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        checkTime = year + "-" +
                MyUtils.getInstans().formatTime(monthOfYear + 1) +
                "-" +
                MyUtils.getInstans().formatTime(dayOfMonth);
        setRefresh();
    }

    private void setRecycler() {
//        mRecyclerView.setBackgroundResource(R.color.FBFBFB);
        drawHisListAdapter = new DrawHisListAdapter(R.layout.item_show1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(drawHisListAdapter);

        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
        drawHisListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                drawHisListAdapter.setEnableLoadMore(false);
                page = 1;
                getCall();
            }
        });

        drawHisListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        if (!StringUtils.isEmpty(checkTime)) {
            map.put("beginTime", checkTime);
            map.put("endTime", checkTime);
            View view = View.inflate(getContext(), R.layout.layout_nodata, null);
            TextView tv_nodata = view.findViewById(R.id.tv_nodata);
            tv_nodata.setText("您当日没有提现记录");
            drawHisListAdapter.setEmptyView(view);
        }
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.WithdrawList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    drawHisListAdapter.setEnableLoadMore(true);
                }
                DrawHisBean drawHisBean = JSON.parseObject(responseString, DrawHisBean.class);
                if (drawHisBean.getCode() == Api.SUCCESS) {
                    setData(drawHisBean.getData(), drawHisListAdapter);
                } else {
                    showToast(drawHisBean.getMsg());
                }
            }
        });
    }

    private void setData(List<DrawHisBean.DataEntity> data, DrawHisListAdapter adapter) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            adapter.setNewData(data);
        } else {
            if (size > 0) {
                adapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            adapter.loadMoreEnd(false);
        } else {
            adapter.loadMoreComplete();
        }
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