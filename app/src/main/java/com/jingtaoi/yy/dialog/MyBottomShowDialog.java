package com.jingtaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 底部选项弹框
 * Created by Administrator on 2018/3/9.
 */

public class MyBottomShowDialog extends Dialog {


    @BindView(R.id.mRecyclerView_dialog)
    RecyclerView mRecyclerViewDialog;

    private BottomShowRecyclerAdapter bottomShowRecyclerAdapter;

    private Context context;
    private ArrayList<String> bottomList;

    public MyBottomShowDialog(Context context, ArrayList<String> bottomList) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.bottomList = bottomList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_bottom_show);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        setRecycler();
    }

    private void setRecycler() {
        bottomShowRecyclerAdapter = new BottomShowRecyclerAdapter(R.layout.item_bottomshow);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerViewDialog.setAdapter(bottomShowRecyclerAdapter);
        mRecyclerViewDialog.setLayoutManager(layoutManager);
        bottomShowRecyclerAdapter.setNewData(bottomList);
    }

    public BottomShowRecyclerAdapter getAdapter() {
        return this.bottomShowRecyclerAdapter;
    }

}