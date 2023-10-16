package com.jingtaoi.yy.ui.room;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.adapter.OnlinesAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 在线听众页面
 */
public class OnlinesActivity extends MyBaseActivity {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    OnlinesAdapter onlinesAdapter;
    String roomId;
    int sequence;


    @Override
    public void initData() {
        roomId = getBundleString(Const.ShowIntent.ROOMID);
        sequence = getBundleInt(Const.ShowIntent.TYPE, 1);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.layout_recycler_top);
    }

    @Override
    public void initView() {
        setTitleText(R.string.title_onlines);
        setRecycler();
        setLeftImg(getResources().getDrawable(R.drawable.icon_back1));
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getCall();
            }
        });
    }

    private void setRecycler() {
        onlinesAdapter = new OnlinesAdapter(R.layout.item_onlines);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(onlinesAdapter);

        onlinesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VoiceUserBean.DataBean dataBean = (VoiceUserBean.DataBean) adapter.getItem(position);
                int uid = dataBean.getId();
                if (dataBean.getIsAgreement() == 1) {//用户
                    getUpdateCall(uid);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCall();
            }
        });

        onlinesAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);
    }

    private void getUpdateCall(int uid) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("pid", roomId);
        map.put("sequence", sequence);
        map.put("type", 1);
        map.put("state", Const.IntShow.TWO);
        map.put("buid", userToken);
        HttpManager.getInstance().post(Api.micUpdate, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                    ActivityCollector.getActivityCollector().finishActivity();
                }
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("pid", roomId);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.UserNoWheat, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (page == 1) {
                    mSwipeRefreshLayout.setEnabled(false);
                }
                VoiceUserBean voiceUserBean = JSON.parseObject(responseString, VoiceUserBean.class);
                if (voiceUserBean.getCode() == Api.SUCCESS) {
                    setData(voiceUserBean.getData());
                } else {
                    showToast(voiceUserBean.getMsg());
                }
            }
        });
    }

    private void setData(List<VoiceUserBean.DataBean> data) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            onlinesAdapter.setNewData(data);
        } else {
            if (size > 0) {
                onlinesAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            onlinesAdapter.loadMoreEnd(false);
        } else {
            onlinesAdapter.loadMoreComplete();
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
