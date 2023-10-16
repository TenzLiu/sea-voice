package com.jingtaoi.yy.ui.message.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.MyattentionBean;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.message.OtherHomeActivity;
import com.jingtaoi.yy.ui.mine.adapter.MyFansListAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFansFragment extends MyBaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    MyFansListAdapter fansListAdapter;
    Unbinder unbinder;
    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycler_top1, container, false);
    }



    @Override
    public void initView() {
        setRecycler();

    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", Const.IntShow.THREE);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.addfriendList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    fansListAdapter.setEnableLoadMore(true);
                }
                MyattentionBean myattentionBean = JSON.parseObject(responseString, MyattentionBean.class);
                if (myattentionBean.getCode() == Api.SUCCESS) {
                    setData(myattentionBean.getData(), fansListAdapter);
                } else {
                    showToast(myattentionBean.getMsg());
                }
            }
        });
    }

    private void setData(List<MyattentionBean.DataEntity> data, MyFansListAdapter adapter) {
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

    private void setRecycler() {
        fansListAdapter = new MyFansListAdapter(R.layout.item_attention);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(fansListAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
        fansListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fansListAdapter.setEnableLoadMore(false);
                page = 1;
                getCall();
            }
        });

        fansListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);

        fansListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyattentionBean.DataEntity dataEntity = (MyattentionBean.DataEntity) adapter.getItem(position);
                Bundle bundle = new Bundle();
                assert dataEntity != null;
                bundle.putInt(Const.ShowIntent.ID, dataEntity.getId());
                ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
            }
        });

        fansListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_end_attention:
                        MyattentionBean.DataEntity dataEntity = (MyattentionBean.DataEntity) adapter.getItem(position);
                        showMyDialog(dataEntity, position);
                        break;
                    case R.id.caozuot_tv:
                        MyattentionBean.DataEntity dataEntity1 = (MyattentionBean.DataEntity) adapter.getItem(position);
                        showMyDialog(dataEntity1, position);
                        break;

                }
            }
        });
    }

    MyDialog myDialog;

    private void showMyDialog(MyattentionBean.DataEntity dataEntity, int position) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(getContext());
        myDialog.show();
        myDialog.setHintText("确定要关注" + "<font color='#2980B9'> "+dataEntity.getName()+" </font>"  + "吗？");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                getAttentionCall(dataEntity, position);
            }
        });
    }

    private void getAttentionCall(MyattentionBean.DataEntity dataEntity, int position) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", dataEntity.getId());
        map.put("type", Const.IntShow.ONE);
        HttpManager.getInstance().post(Api.addAttention, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("关注成功，相互关注可以成为好友");
                    dataEntity.setState(Const.IntShow.TWO);
                    fansListAdapter.setData(position, dataEntity);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
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
