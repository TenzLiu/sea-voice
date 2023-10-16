package com.jingtaoi.yy.ui.mine;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.TopupHisBean;
import com.jingtaoi.yy.dialog.MyBirthdayDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.mine.adapter.TopupHisListAdapter;
import com.jingtaoi.yy.utils.MyUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 充值记录页面
 */
public class TopupHistoryActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    TopupHisListAdapter topupHisListAdapter;
    String checkTime;
    Calendar calendar;//日期

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
        setTitleText(R.string.tv_topuphis_gift);
        setRightImg(getDrawable(R.drawable.date));
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyTimeDialog();
            }
        });

        setRecycler();

        setRefresh();
    }

    public void setRefresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                page = 1;
                getCall();
            }
        });
    }

    private void showMyTimeDialog() {
        long timeBir;
        if (StringUtils.isEmpty(checkTime)) {
            timeBir = System.currentTimeMillis();
        } else {
            timeBir = MyUtils.getInstans().getLongTime(checkTime, "yyyy-MM-dd");
        }
        final MyBirthdayDialog myBirthdayDialog = new MyBirthdayDialog(this, timeBir);
        myBirthdayDialog.show();
        myBirthdayDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = myBirthdayDialog.getDate();
                showBirthday(calendar);
                myBirthdayDialog.dismiss();
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

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        if (!StringUtils.isEmpty(checkTime)) {
            map.put("beginTime", checkTime);
            map.put("endTime", checkTime);
            View view = View.inflate(this, R.layout.layout_nodata, null);
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
        mRecyclerView.setBackgroundResource(R.color.FBFBFB);
        topupHisListAdapter = new TopupHisListAdapter(R.layout.item_show);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(topupHisListAdapter);

        View view = View.inflate(this, R.layout.layout_nodata, null);
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
