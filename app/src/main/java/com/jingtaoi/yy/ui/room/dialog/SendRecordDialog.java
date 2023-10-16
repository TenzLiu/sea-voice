package com.jingtaoi.yy.ui.room.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.SendRecordBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.adapter.SendRecordAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 赠送历史
 * Created by Administrator on 2018/3/9.
 */

public class SendRecordDialog extends Dialog {


    @BindView(R.id.title_show1)
    TextView titleShow1;
    @BindView(R.id.mRecyclerView_dialog)
    RecyclerView mRecyclerViewDialog;
    private SendRecordAdapter sendRecordAdapter;

    private Activity activity;
    private int page = 1;
    private int userId;

    public SendRecordDialog(Activity activity, int userId) {
        super(activity, R.style.CustomDialogStyle);
        this.activity = activity;
        this.userId = userId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_sendrecode);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        setRecycler();
        getCall();
    }

    private void setRecycler() {
        sendRecordAdapter = new SendRecordAdapter(R.layout.item_sendrecord);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerViewDialog.setAdapter(sendRecordAdapter);
        mRecyclerViewDialog.setLayoutManager(layoutManager);

        sendRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerViewDialog);


    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        String reqString = Api.givingRecords;
        map.put("uid", userId);
        map.put("pageSize", 10);
        map.put("pageNum", page);
        HttpManager.getInstance().post(reqString, map, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                SendRecordBean sendRecordBean = JSON.parseObject(responseString, SendRecordBean.class);
                setData(sendRecordBean.getData(), sendRecordAdapter);
            }
        });
    }

    private void setData(List<SendRecordBean.DataBean> data, SendRecordAdapter adapter) {
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