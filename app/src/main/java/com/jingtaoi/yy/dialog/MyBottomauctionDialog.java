package com.jingtaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.ui.room.adapter.AuctionListAdapter;
import com.jingtaoi.yy.bean.RoomAuctionBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 竞拍零时榜单页面
 * Created by Administrator on 2018/3/9.
 */

public class MyBottomauctionDialog extends Dialog {


    Context context;
    @BindView(R.id.iv_close_auction)
    ImageView ivCloseAuction;
    @BindView(R.id.mRecyclerView_auction)
    RecyclerView mRecyclerViewAuction;
    @BindView(R.id.ll_back_auction)
    LinearLayout llBackAuction;
    String roomId;

    AuctionListAdapter auctionListAdapter;

    public MyBottomauctionDialog(Context context, String roomId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.roomId = roomId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_myauction);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        Objects.requireNonNull(getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        setRecycler();
        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.UserAuctionCharm, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                RoomAuctionBean roomAuctionBean = JSON.parseObject(responseString, RoomAuctionBean.class);
                if (roomAuctionBean.getCode() == 0) {
                    auctionListAdapter.setNewData(roomAuctionBean.getData());
                } else {
                    showToast(roomAuctionBean.getMsg());
                }
            }
        });
    }

    private void setRecycler() {
        auctionListAdapter = new AuctionListAdapter(R.layout.item_myauction);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerViewAuction.setLayoutManager(layoutManager);
        mRecyclerViewAuction.setAdapter(auctionListAdapter);

    }


    @OnClick({R.id.iv_close_auction, R.id.ll_back_auction})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_auction:
                dismiss();
                break;
            case R.id.ll_back_auction:
                dismiss();
                break;
        }
    }
}