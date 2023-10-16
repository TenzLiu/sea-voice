package com.jingtaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.ui.room.adapter.BottomOnlinesAdapter;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.Const;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.Toast;


/**
 * 在线用户弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyOnlineUserDialog extends Dialog {


    @BindView(R.id.iv_close_onlines_user)
    ImageView ivCloseOnlinesUser;
    @BindView(R.id.mRecyclerView_onlines_user)
    RecyclerView mRecyclerViewOnlinesUser;
    @BindView(R.id.ll_back_onlines_user)
    LinearLayout llBackOnlinesUser;
    @BindView(R.id.mSwipeRefreshLayout_onlines)
    SwipeRefreshLayout mSwipeRefreshLayoutOnlines;
    private Context context;

    private BottomOnlinesAdapter bottomOnlinesAdapter;

    private int pageNumber = 1;//页数

    private String roomId;
    private int userId;
    private int type;//1房间用户 2房主及麦上用户

    public MyOnlineUserDialog(Context context, String roomId, int userId, int type) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.roomId = roomId;
        this.userId = userId;
        this.type = type;
    }


    OnlineClicer onlineClicer;

    public OnlineClicer getOnlineClicer() {
        return onlineClicer;
    }

    public void setOnlineClicer(OnlineClicer onlineClicer) {
        this.onlineClicer = onlineClicer;
    }

    public interface OnlineClicer {
        void setOnlineClicer(VoiceUserBean.DataBean dataBean, int position);
    }

    public void setUserGetOut(int position) {
        if (bottomOnlinesAdapter != null) {
            if (bottomOnlinesAdapter.getData().size() > position) {
                bottomOnlinesAdapter.remove(position);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_onlines_user);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().setGravity(Gravity.CENTER);

        setRecycler();

        updateShow();
    }

    public void updateShow() {
        mSwipeRefreshLayoutOnlines.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayoutOnlines.setRefreshing(true);
                if (type == 1) {
                    pageNumber = 1;
                    getCall();
                } else if (type == 2) {
                    getMicCall();
                }
            }
        });
    }

    private void getMicCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.getUserChatrooms, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayoutOnlines != null && mSwipeRefreshLayoutOnlines.isRefreshing()) {
                    mSwipeRefreshLayoutOnlines.setRefreshing(false);
                }
                VoiceUserBean voiceMicBean = JSON.parseObject(responseString, VoiceUserBean.class);
                if (voiceMicBean.getCode() == 0) {
                    bottomOnlinesAdapter.setNewData(voiceMicBean.getData());
                    bottomOnlinesAdapter.loadMoreEnd(false);
                } else {
                    Toast.create(context).show(voiceMicBean.getMsg());
                }
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("pid", roomId);
        map.put("pageSize", Const.IntShow.TEN);
        map.put("pageNum", pageNumber);
        HttpManager.getInstance().post(Api.voiceUser, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayoutOnlines != null && mSwipeRefreshLayoutOnlines.isRefreshing()) {
                    mSwipeRefreshLayoutOnlines.setRefreshing(false);
                    bottomOnlinesAdapter.setEnableLoadMore(true);
                }
                VoiceUserBean voiceMicBean = JSON.parseObject(responseString, VoiceUserBean.class);
                if (voiceMicBean.getCode() == 0) {
                    setData(voiceMicBean.getData());
                } else {
                    Toast.create(context).show(voiceMicBean.getMsg());
                }
            }
        });
    }

    private void setData(List<VoiceUserBean.DataBean> data) {
        final int size = data == null ? 0 : data.size();
        if (pageNumber == 1) {
            bottomOnlinesAdapter.setNewData(data);
        } else {
            if (size > 0) {
                bottomOnlinesAdapter.addData(data);
            } else {
                pageNumber--;
            }
        }
        if (size < Const.IntShow.TEN) {
            bottomOnlinesAdapter.loadMoreEnd(false);
        } else {
            bottomOnlinesAdapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
        bottomOnlinesAdapter = new BottomOnlinesAdapter(R.layout.item_bottomonlines);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerViewOnlinesUser.setLayoutManager(layoutManager);
        mRecyclerViewOnlinesUser.setAdapter(bottomOnlinesAdapter);

        bottomOnlinesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VoiceUserBean.DataBean dataBean = (VoiceUserBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                if (dataBean.getIsAgreement() == 1) {
                    if (onlineClicer != null) {
                        onlineClicer.setOnlineClicer(dataBean, position);
                    }
                }

            }
        });

        bottomOnlinesAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNumber++;
                getCall();
            }
        }, mRecyclerViewOnlinesUser);

        mSwipeRefreshLayoutOnlines.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type == 1) {
                    pageNumber = 1;
                    getCall();
                } else if (type == 2) {
                    getMicCall();
                }
            }
        });

    }

    @OnClick({R.id.iv_close_onlines_user, R.id.ll_back_onlines_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_onlines_user:
                dismiss();
                break;
            case R.id.ll_back_onlines_user:
                dismiss();
                break;
        }
    }

    public BottomOnlinesAdapter getAdapter() {
        return this.bottomOnlinesAdapter;
    }
}