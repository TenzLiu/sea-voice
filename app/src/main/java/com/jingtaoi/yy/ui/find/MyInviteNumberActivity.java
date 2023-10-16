package com.jingtaoi.yy.ui.find;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.InviteNumberBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.find.adapter.InviteNumRecyAdapter;
import com.jingtaoi.yy.utils.MyUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的邀请人数
 */
public class MyInviteNumberActivity extends MyBaseActivity {
    @BindView(R.id.tv_number_invitenumber)
    TextView tvNumberInvitenumber;
    @BindView(R.id.tv_allnumber_invitenumber)
    TextView tvAllnumberInvitenumber;
    @BindView(R.id.tv_numbermon_invitenumber)
    TextView tvNumbermonInvitenumber;
    @BindView(R.id.ll_show_invitenumber)
    LinearLayout llShowInvitenumber;
    @BindView(R.id.tv_show_invitenumber)
    TextView tvShowInvitenumber;
    @BindView(R.id.mRecyclerView_invitenumber)
    RecyclerView mRecyclerViewInvitenumber;
    @BindView(R.id.tv_share_invitenumber)
    TextView tvShareInvitenumber;

    InviteNumRecyAdapter inviteNumRecyAdapter;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_invitenumber);
    }

    @Override
    public void initView() {
        setTitleText(R.string.title_myinviteshow);
        setRecycler();
        showDialog();
        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.getUserInvite, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                InviteNumberBean inviteNumberBean = JSON.parseObject(responseString, InviteNumberBean.class);
                if (inviteNumberBean.getCode() == Api.SUCCESS) {
                    if (page == 1) {
                        tvNumberInvitenumber.setText(String.valueOf(inviteNumberBean.getData().getToday()));
                        tvAllnumberInvitenumber.setText(String.valueOf(inviteNumberBean.getData().getSum()));
                        tvNumbermonInvitenumber.setText(MyUtils.getInstans().doubleTwo(inviteNumberBean.getData().getSumMoney()));
                    }
                    setData(inviteNumberBean.getData().getInviteList(), inviteNumRecyAdapter);
                } else {
                    showToast(inviteNumberBean.getMsg());
                }
            }
        });
    }

    private void setData(List<InviteNumberBean.DataEntity.InviteListEntity> data, InviteNumRecyAdapter adapter) {
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

    private void setRecycler() {
        inviteNumRecyAdapter = new InviteNumRecyAdapter(R.layout.item_sharehis);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewInvitenumber.setLayoutManager(layoutManager);
        mRecyclerViewInvitenumber.setAdapter(inviteNumRecyAdapter);

        inviteNumRecyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
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
