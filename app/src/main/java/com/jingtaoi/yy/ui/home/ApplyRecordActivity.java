package com.jingtaoi.yy.ui.home;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.TuijianRecordeBean;
import com.jingtaoi.yy.model.ApplyRecord;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.home.adapter.ApplyRecordAdapter;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;

public class ApplyRecordActivity extends MyBaseActivity {
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshRecyclerLayout mSwipeRefreshLayout;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_apply_record);
    }


    private ApplyRecordAdapter adapter;
    private ArrayList<ApplyRecord> records = new ArrayList<>();

    @Override
    public void initView() {
        setTitleText("申请记录");
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mSwipeRefreshLayout.setMode(SwipeRefreshRecyclerLayout.Mode.Both);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshRecyclerLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                page++;
                getData();
            }
        });
        adapter = new ApplyRecordAdapter(records);
        mSwipeRefreshLayout.setAdapter(adapter);
        getData();
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    private void getData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", SharedPreferenceUtils.get(this, Const.User.USER_TOKEN, 0));
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.TUIJIAN_RECORD, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                mSwipeRefreshLayout.setRefreshing(false);
                TuijianRecordeBean baseBean = JSON.parseObject(responseString, TuijianRecordeBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    List<ApplyRecord> data = baseBean.getData();
                    if (page == 1)
                        records.clear();
                    records.addAll(data);
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
