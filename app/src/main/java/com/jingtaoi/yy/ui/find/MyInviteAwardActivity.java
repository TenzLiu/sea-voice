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
import com.jingtaoi.yy.bean.InviteAwardBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.find.adapter.InviteAwardRecyAdapter;
import com.jingtaoi.yy.utils.MyUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分成奖励
 */
public class MyInviteAwardActivity extends MyBaseActivity {
    @BindView(R.id.tv_number_invitenumber)
    TextView tvNumberInvitenumber;
    @BindView(R.id.tv_allnumber_invitenumber)
    TextView tvAllnumberInvitenumber;
    @BindView(R.id.ll_show_invitenumber)
    LinearLayout llShowInvitenumber;
    @BindView(R.id.tv_show_invitenumber)
    TextView tvShowInvitenumber;
    @BindView(R.id.mRecyclerView_invitenumber)
    RecyclerView mRecyclerViewInvitenumber;
    @BindView(R.id.tv_share_invitenumber)
    TextView tvShareInvitenumber;

    InviteAwardRecyAdapter inviteAwardRecyAdapter;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_inviteaward);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_inviteaward);
        setRecycler();
        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.getUserDivide, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                InviteAwardBean inviteAwardBean = JSON.parseObject(responseString, InviteAwardBean.class);
                if (page == 1) {
                    tvNumberInvitenumber.setText(MyUtils.getInstans().doubleTwo(inviteAwardBean.getData().getToday()));
                    tvAllnumberInvitenumber.setText(MyUtils.getInstans().doubleTwo(inviteAwardBean.getData().getSumMoney()));
                }
                if (inviteAwardBean.getCode()==Api.SUCCESS){
                    setData(inviteAwardBean.getData().getDivideList(), inviteAwardRecyAdapter);
                }else {
                    showToast(inviteAwardBean.getMsg());
                }
            }
        });
    }

    private void setData(List<InviteAwardBean.DataEntity.DivideListEntity> data, InviteAwardRecyAdapter adapter) {
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
        inviteAwardRecyAdapter = new InviteAwardRecyAdapter(R.layout.item_sharehis);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewInvitenumber.setLayoutManager(layoutManager);
        mRecyclerViewInvitenumber.setAdapter(inviteAwardRecyAdapter);

        inviteAwardRecyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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
