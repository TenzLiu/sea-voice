package com.jingtaoi.yy.ui.message.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.ui.room.VoiceActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.MyattentionBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.message.ChatActivity;
import com.jingtaoi.yy.ui.message.OtherHomeActivity;
import com.jingtaoi.yy.ui.message.adapter.FriendListAdapter;
import com.jingtaoi.yy.ui.mine.MyAttentionActivity;
import com.jingtaoi.yy.ui.mine.MyFansActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.view.ScrollInterceptScrollView;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 好友页面
 */
public class FriendFragment extends MyBaseFragment {
    @BindView(R.id.rl_attention_friend)
    RelativeLayout rlAttentionFriend;
    @BindView(R.id.rl_fans_friend)
    RelativeLayout rlFansFriend;
    @BindView(R.id.rl_invite_friend)
    RelativeLayout rlInviteFriend;
    @BindView(R.id.mRecyclerView_friend)
    RecyclerView mRecyclerViewFriend;
    Unbinder unbinder;

    FriendListAdapter friendListAdapter;
    @BindView(R.id.mSwipeRefreshLayout_friend)
    SwipeRefreshLayout mSwipeRefreshLayoutFriend;

    @BindView(R.id.mscrollView_friend)
    ScrollInterceptScrollView mscrollViewFriend;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void initView() {

        setRecycler();
        mSwipeRefreshLayoutFriend.post(new Runnable() {
            @Override
            public void run() {
                if (mSwipeRefreshLayoutFriend!=null){
                    mSwipeRefreshLayoutFriend.setRefreshing(true);
                }
                page = 1;
                getCall();
            }
        });
    }

    @Override
    protected void onVisibleToUser() {
        super.onVisibleToUser();

    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", Const.IntShow.ONE);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.addfriendList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayoutFriend != null && mSwipeRefreshLayoutFriend.isRefreshing()) {
                    mSwipeRefreshLayoutFriend.setRefreshing(false);
                    friendListAdapter.setEnableLoadMore(true);
                }
                MyattentionBean myattentionBean = JSON.parseObject(responseString, MyattentionBean.class);
                if (myattentionBean.getCode() == Api.SUCCESS) {
                    setData(myattentionBean.getData(), friendListAdapter);
//                    mscrollViewFriend.fullScroll(ScrollView.FOCUS_UP);
                } else {
                    showToast(myattentionBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                if (mSwipeRefreshLayoutFriend != null && mSwipeRefreshLayoutFriend.isRefreshing()) {
                    mSwipeRefreshLayoutFriend.setRefreshing(false);
                    friendListAdapter.setEnableLoadMore(true);
                }
            }
        });
    }

    private void setData(List<MyattentionBean.DataEntity> data, FriendListAdapter adapter) {
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
        friendListAdapter = new FriendListAdapter(R.layout.item_friend);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewFriend.setLayoutManager(layoutManager);
        mRecyclerViewFriend.setAdapter(friendListAdapter);

        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        ivNodata.setImageResource(R.drawable.friend_empty);
        tvNodata.setText(getString(R.string.hint_nodata_friend));
        friendListAdapter.setEmptyView(view);

        mSwipeRefreshLayoutFriend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friendListAdapter.setEnableLoadMore(false);
                page = 1;
                getCall();
            }
        });

        friendListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerViewFriend);

        friendListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyattentionBean.DataEntity dataEntity = (MyattentionBean.DataEntity) adapter.getItem(position);
                Bundle bundle = new Bundle();
                assert dataEntity != null;
//                bundle.putInt(Const.ShowIntent.ID, dataEntity.getId());
//                ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);

                bundle.putString(Const.ShowIntent.ID, String.valueOf(dataEntity.getId()));
                bundle.putString(Const.ShowIntent.NAME, dataEntity.getName());
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(TIMConversationType.C2C);
                chatInfo.setId(String.valueOf(dataEntity.getId()));
                chatInfo.setChatName(dataEntity.getName());
                bundle.putSerializable(Const.ShowIntent.CHAT_INFO, chatInfo);
                VoiceActivity activity = ActivityCollector.getActivity(VoiceActivity.class);
                if (activity == null) {
                    bundle.putBoolean("isRoom", false);
                } else {
                    bundle.putBoolean("isRoom", true);
                }
                ActivityCollector.getActivityCollector().toOtherActivity(ChatActivity.class, bundle);
            }
        });

        friendListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_show_friend:
                        MyattentionBean.DataEntity dataEntity = (MyattentionBean.DataEntity) adapter.getItem(position);
                        Bundle bundle = new Bundle();
                        assert dataEntity != null;
                        bundle.putInt(Const.ShowIntent.ID, dataEntity.getId());
                        ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
                        break;
                }
            }
        });

    }

    @Override
    public void setResume() {
        page = 1;
        getCall();
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

    @OnClick({R.id.rl_attention_friend, R.id.rl_fans_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_attention_friend:
                ActivityCollector.getActivityCollector().toOtherActivity(MyAttentionActivity.class);
                break;
            case R.id.rl_fans_friend:
                ActivityCollector.getActivityCollector().toOtherActivity(MyFansActivity.class);
                break;
        }
    }

}
