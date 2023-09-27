package com.siyecaoi.yy.ui.room.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiveGiftBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.GiveGiftAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 手气榜
 * Created by Administrator on 2018/3/9.
 */

public class GiveGiftDialog extends Dialog {

    @BindView(R.id.title_show1)
    TextView titleShow1;
    @BindView(R.id.txt_tab_one)
    TextView tabOne;
    @BindView(R.id.txt_tab_two)
    TextView tabTwo;
    @BindView(R.id.mRecyclerView_dialog)
    RecyclerView mRecyclerViewDialog;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private GiveGiftAdapter giveGiftAdapter;

    private Context context;
    private int page = 1;
    private int userId;

    private int currentType = 1;// 送出1 收到2

    public GiveGiftDialog(Context context, int userId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_bottom_show2);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);

        setRecycler();
        titleShow1.setText("转赠记录");
        tabOne.setText("送出礼物");
        tabOne.setTextColor(ContextCompat.getColor(context, R.color.white));
        tabOne.setTextSize(14);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line2);
        tabTwo.setTextColor(ContextCompat.getColor(context, R.color.white));
        tabTwo.setText("收到礼物");
        tabTwo.setTextSize(12);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        getCall();
    }

    @OnClick({R.id.txt_tab_one, R.id.txt_tab_two})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_tab_one:
                if (currentType != 1){
                    setData(null, giveGiftAdapter);

                    tabOne.setTextColor(ContextCompat.getColor(context, R.color.white));
                    tabOne.setTextSize(16);
                    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line2);
                    tabTwo.setTextColor(ContextCompat.getColor(context, R.color.white));
                    tabTwo.setTextSize(14);
                    tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                    page = 1;
                    currentType = 1;

                    getCall();
                }
                break;
            case R.id.txt_tab_two:
                if (currentType != 2){
                    tabOne.setTextColor(ContextCompat.getColor(context, R.color.white));
                    tabOne.setTextSize(14);
                    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tabTwo.setTextColor(ContextCompat.getColor(context, R.color.white));
                    tabTwo.setTextSize(16);
                    tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line2);

                    page = 1;
                    currentType = 2;

                    getCall();
                }
                break;
        }

    }

    private void setRecycler() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCall();
            }
        });

        giveGiftAdapter = new GiveGiftAdapter(R.layout.item_givegift);
        giveGiftAdapter.setGiveType(currentType);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerViewDialog.setAdapter(giveGiftAdapter);
        mRecyclerViewDialog.setLayoutManager(layoutManager);

        giveGiftAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerViewDialog);

    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        String reqString = Api.givePartyGiftLog;
        map.put("uid", userId);
        map.put("pageSize", 10);
        map.put("pageNum", page);
        map.put("type", currentType);
        HttpManager.getInstance().post(reqString, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);

                GiveGiftBean giveGiftBean = JSON.parseObject(responseString, GiveGiftBean.class);
                setData(giveGiftBean.getData(), giveGiftAdapter);
            }
        });
    }

    private void setData(List<GiveGiftBean.DataEntity> data, GiveGiftAdapter adapter) {
        giveGiftAdapter.setGiveType(currentType);

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