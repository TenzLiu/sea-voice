package com.jingtaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.UserBlackBean;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.mine.adapter.BlackListAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户黑名单页面
 */
public class BlackUserActivity extends MyBaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    BlackListAdapter blackListAdapter;
    MyDialog myDialog;

    @Override
    public void initData() {
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.layout_recycler_top);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_black_roomset);
        setRecycler();
        showLine(false);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getCall();
            }
        });
    }

    private void setRecycler() {
        blackListAdapter = new BlackListAdapter(R.layout.item_onlines);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(blackListAdapter);

        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        tvNodata.setText(getString(R.string.tv_nodata_balckuser));
        blackListAdapter.setEmptyView(view);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                blackListAdapter.setEnableLoadMore(false);
                page = 1;
                getCall();
            }
        });

        blackListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);

        blackListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_clear_onlines) {
                    UserBlackBean.DataEntity dataBean = (UserBlackBean.DataEntity) adapter.getItem(position);
                    showMyDialog(dataBean, position);
                }
            }
        });
    }

    private void showMyDialog(final UserBlackBean.DataEntity dataBean, final int position) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("是否将" + dataBean.getName() + "移出黑名单列表？");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                setBlcak(dataBean.getId(), position);
            }
        });
    }

    private void setBlcak(int id, int position) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", id);
        HttpManager.getInstance().post(Api.delUserblock, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("移除黑名单成功");
                    blackListAdapter.remove(position);
//                    blackUserListAdapter.notifyItemRemoved(position);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.getUserblock, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    blackListAdapter.setEnableLoadMore(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                UserBlackBean roomBlackBean = JSON.parseObject(responseString, UserBlackBean.class);
                if (roomBlackBean.getCode() == Api.SUCCESS) {
                    setData(roomBlackBean.getData());
                } else {
                    showToast(roomBlackBean.getMsg());
                }
            }
        });

    }

    private void setData(List<UserBlackBean.DataEntity> data) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            blackListAdapter.setNewData(data);
        } else {
            if (size > 0) {
                blackListAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            blackListAdapter.loadMoreEnd(false);
        } else {
            blackListAdapter.loadMoreComplete();
        }
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
