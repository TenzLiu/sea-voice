package com.jingtaoi.yy.ui.mine.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.MyattentionBean;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.mine.adapter.SendOtherListAdapter;
import com.jingtaoi.yy.utils.Const;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SendOtherFragment extends MyBaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.group_buy)
    Group group_buy;
    Unbinder unbinder;

    SendOtherListAdapter sendOtherListAdapter;
    int id;
    String nameShow;
    int type;
    MyDialog myDialog;
    private int gold;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycler_stroe, container, false);
    }

    @Override
    public void initView() {
        group_buy.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        type = bundle.getInt(Const.ShowIntent.TYPE);
        id = bundle.getInt(Const.ShowIntent.ID);
        gold = bundle.getInt(Const.ShowIntent.GOLD);
        nameShow = bundle.getString(Const.ShowIntent.DATA);

        setRecycler();

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                page = 1;
                getCall();
            }
        });
    }

    private void setRecycler() {
        sendOtherListAdapter = new SendOtherListAdapter(R.layout.item_sendother);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(sendOtherListAdapter);

        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
        sendOtherListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendOtherListAdapter.setEnableLoadMore(false);
                page = 1;
                getCall();
            }
        });

        sendOtherListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);

        sendOtherListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        sendOtherListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyattentionBean.DataEntity dataEntity = (MyattentionBean.DataEntity) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_send_sendother:
                        assert dataEntity != null;
                        showMyDialog(dataEntity, position);
                        break;
                }
            }
        });
    }

    private void showMyDialog(MyattentionBean.DataEntity dataEntity, int position) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(getContext());
        myDialog.show();
        SpannableStringBuilder spannableString = new SpannableStringBuilder("确认购买“" + nameShow + "”并赠送给" + dataEntity.getName() + "吗？");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4BA6DC"));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#4BA6DC"));
        spannableString.setSpan(colorSpan, 4, 4+nameShow.length()+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, 10+nameShow.length(), 10+dataEntity.getName().length()+nameShow.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.append("\n");
        spannableString.append("img");

        ImageSpan imageSpan = new ImageSpan( getContext(), R.drawable.gold);
        spannableString.setSpan(imageSpan, spannableString.length() - 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.append(" ");

        spannableString.append(String.valueOf(gold));
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#4BA6DC"));
        spannableString.setSpan(colorSpan1, spannableString.length()-String.valueOf(gold).length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        myDialog.setHintText(spannableString);
        myDialog.setHintTitle("购买提示");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                getBuyCall(dataEntity, position);
            }
        });
    }

    private void getBuyCall(MyattentionBean.DataEntity dataEntity, int position) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", dataEntity.getId());
        map.put("gid", id);
        HttpManager.getInstance().post(Api.addSceneList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("赠送成功");
                    dataEntity.setSendSuccescc(true);
                    sendOtherListAdapter.setData(position, dataEntity);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", type + 1);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.addfriendList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    sendOtherListAdapter.setEnableLoadMore(true);
                }
                MyattentionBean myattentionBean = JSON.parseObject(responseString, MyattentionBean.class);
                if (myattentionBean.getCode() == Api.SUCCESS) {
                    setData(myattentionBean.getData(), sendOtherListAdapter);
                } else {
                    showToast(myattentionBean.getMsg());
                }
            }
        });
    }

    private void setData(List<MyattentionBean.DataEntity> data, SendOtherListAdapter adapter) {
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
