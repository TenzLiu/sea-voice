package com.jingtaoi.yy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.GiftShowBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.adapter.BottomShow1RecyclerAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 底部选项弹框(奖池 历史数据)
 * Created by Administrator on 2018/3/9.
 */

public class MyBottomShow1Dialog extends Dialog {


    @BindView(R.id.title_show1)
    ImageView titleShow1;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.mRecyclerView_dialog)
    RecyclerView mRecyclerViewDialog;
    private BottomShow1RecyclerAdapter bottomShowRecyclerAdapter;

    private Activity activity;
    private int type;
    private int eggType;
    private int page = 1;
    private int userId;

    public MyBottomShow1Dialog(Activity activity, int type,int userId,int eggType) {
        super(activity, R.style.CustomDialogStyle);
        this.activity = activity;
        this.type = type;
        this.userId = userId;
        this.eggType = eggType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_bottom_show1);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (ScreenUtil.getScreenWidth(activity));
        lp.height = (int) (ScreenUtil.getScreenHeight(activity) * 0.55);
        getWindow().setAttributes(lp);
        setRecycler();
//        if (type == 1) {
//            titleShow1.setText("本期奖池");
//        } else if (type == 2) {
//            titleShow1.setText("我的记录");
//        }
        getCall();
        iv_close.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void setRecycler() {
        bottomShowRecyclerAdapter = new BottomShow1RecyclerAdapter(R.layout.item_bottomshow1);
        bottomShowRecyclerAdapter.setType(type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerViewDialog.setAdapter(bottomShowRecyclerAdapter);
        mRecyclerViewDialog.setLayoutManager(layoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type == 2){
                    page = 1;
                    getCall();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        if (type == 2) {
            bottomShowRecyclerAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    page++;
                    getCall();
                }
            }, mRecyclerViewDialog);
        }

    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        String reqString = null;
        if (type == 1) {
            reqString = Api.JackpotInfoMark;
        } else if (type == 2) {
            map.put("uid", userId);
            map.put("pageSize", 10);
            map.put("type", eggType);
            map.put("pageNum", page);
            reqString = Api.UserGifit;
        }
        HttpManager.getInstance().post(reqString, map, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);
                GiftShowBean giftShowBean = JSON.parseObject(responseString, GiftShowBean.class);
                if (type == 1) {
                    bottomShowRecyclerAdapter.setNewData(giftShowBean.getData());
                } else if (type == 2) {
                    setData(giftShowBean.getData(), bottomShowRecyclerAdapter);
                }
            }
        });
    }

    private void setData(List<GiftShowBean.DataEntity> data, BottomShow1RecyclerAdapter adapter) {
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
        if (size < 10) {
            adapter.loadMoreEnd(false);
        } else {
            adapter.loadMoreComplete();
        }
    }


}