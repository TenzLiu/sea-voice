package com.siyecaoi.yy.ui.message.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.bean.MyattentionBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.message.OtherHomeActivity;
import com.siyecaoi.yy.ui.mine.adapter.MyAttentionListAdapter;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//关注
public class MyAttentionFragment extends MyBaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    MyAttentionListAdapter attentionListAdapter;

    Unbinder unbinder;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycler_top1, container, false);
    }

    @Override
    public void initView() {

        setRecycler();

    }

    private void setRecycler() {
        attentionListAdapter = new MyAttentionListAdapter(R.layout.item_attention);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(attentionListAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
        attentionListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                attentionListAdapter.setEnableLoadMore(false);
                page = 1;
                getCall();
            }
        });

        attentionListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);

        attentionListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyattentionBean.DataEntity dataEntity = (MyattentionBean.DataEntity) adapter.getItem(position);
                Bundle bundle = new Bundle();
                assert dataEntity != null;
                bundle.putInt(Const.ShowIntent.ID, dataEntity.getId());
                ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
            }
        });

        attentionListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_end_attention:
                        MyattentionBean.DataEntity dataEntity = (MyattentionBean.DataEntity) adapter.getItem(position);
                        assert dataEntity != null;
                        getFindCall(dataEntity.getId());
                        break;
                }
            }
        });
    }

    /**
     * 去找他
     */
    private void getFindCall(int userId) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", userId);
        HttpManager.getInstance().post(Api.UserRoom, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Const.ShowIntent.ROOMID, getOneBean.getData().getRid());
                    ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                } else {
                    showToast(getOneBean.getMsg());
                }
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", Const.IntShow.TWO);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.addfriendList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    attentionListAdapter.setEnableLoadMore(true);
                }
                MyattentionBean myattentionBean = JSON.parseObject(responseString, MyattentionBean.class);
                if (myattentionBean.getCode() == Api.SUCCESS) {
                    setData(myattentionBean.getData(), attentionListAdapter);
                } else {
                    showToast(myattentionBean.getMsg());
                }
            }
        });
    }

    private void setData(List<MyattentionBean.DataEntity> data, MyAttentionListAdapter adapter) {
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
            adapter.loadMoreEnd(true);
        } else {
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void setResume() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                page = 1;
                getCall();
            }
        });
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
