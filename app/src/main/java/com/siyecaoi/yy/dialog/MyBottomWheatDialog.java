package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.ui.room.adapter.WheatListAdapter;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.WheatListBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.Const;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 排麦人数显示
 * Created by Administrator on 2018/3/9.
 */

public class MyBottomWheatDialog extends Dialog {


    Context context;
    @BindView(R.id.tv_title_wheat)
    TextView tvTitleWheat;
    @BindView(R.id.mRecyclerView_wheat)
    RecyclerView mRecyclerViewWheat;
    @BindView(R.id.btn_wheat)
    Button btnWheat;

    private WheatListAdapter wheatListAdapter;
    private String roomId;
    private int userId;
    private int pageNumber;
    private int userNumber;//排麦人数;
    private int state;//是否已排麦

    public MyBottomWheatDialog(Context context, String roomId, int userId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.roomId = roomId;
        this.userId = userId;
        pageNumber = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_wheat);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        setRecycler();

        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", userId);
        map.put("pageSize", MyBaseActivity.PAGE_SIZE);
        map.put("pageNum", pageNumber);
        HttpManager.getInstance().post(Api.getRowWheat, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                WheatListBean wheatListBean = JSON.parseObject(responseString, WheatListBean.class);
                if (wheatListBean.getCode() == Api.SUCCESS) {
                    setData(wheatListBean.getData());
                } else {
                    showToast(wheatListBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(WheatListBean.DataBean dataBean) {
        userNumber = dataBean.getNum();
        state = dataBean.getState();
        setBtnShow();
        List<WheatListBean.DataBean.UserListBean> data = dataBean.getUserList();
        final int size = data == null ? 0 : data.size();
        if (pageNumber == 1) {
            wheatListAdapter.setNewData(data);
        } else {
            if (size > 0) {
                wheatListAdapter.addData(data);
            } else {
                pageNumber--;
            }
        }
        if (size < Const.IntShow.TEN) {
            //第一页如果不够一页就不显示没有更多数据布局
            wheatListAdapter.loadMoreEnd(true);
        } else {
            wheatListAdapter.loadMoreComplete();
        }
    }

    private void setBtnShow() {
        tvTitleWheat.setText(context.getString(R.string.tv_now_wheat) + " " + userNumber);
        if (state == 1) {
            btnWheat.setText(context.getString(R.string.tv_wheat_p));
        } else if (state == 2) {
            btnWheat.setText(context.getString(R.string.tv_cancel_wheat));
            btnWheat.setTextColor(ContextCompat.getColor(context,R.color.textColor3));
            btnWheat.setBackgroundResource(R.drawable.bg_round5_f2f);
        }
    }

    private void setRecycler() {
        wheatListAdapter = new WheatListAdapter(R.layout.item_bottomonlines);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerViewWheat.setLayoutManager(layoutManager);
        mRecyclerViewWheat.setAdapter(wheatListAdapter);

        wheatListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNumber++;
                getCall();
            }
        }, mRecyclerViewWheat);
    }


    @OnClick(R.id.btn_wheat)
    public void onViewClicked() {
        getPaiCall();
    }

    private void getPaiCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", userId);
        String actionReq = null;
        if (state == 1) {
            actionReq = Api.addRowWheat;
        } else if (state == 2) {
            actionReq = Api.delRowWheat;
        }
        HttpManager.getInstance().post(actionReq, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (state == 1) {
                        state = 2;
                        showToast("排麦成功");
                    } else if (state == 2) {
                        state = 1;
                        showToast("取消排麦成功");
                    }
                    dismiss();
                    setBtnShow();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }
}