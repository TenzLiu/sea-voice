package com.jingtaoi.yy.ui.room.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.RealtimeListBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.adapter.DanJiangRealtimeAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实时奖池
 */
public class DanJiangRealtimeDialog extends Dialog {

    Context mContext;

    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rv_gift)
    RecyclerView rvGift;
    @BindView(R.id.iv_fortune)
    TextView iv_fortune;
    @BindView(R.id.cl_bg)
    ConstraintLayout cl_bg;
    private int type;
    DanJiangRealtimeAdapter danJiangAdapter;


    public DanJiangRealtimeDialog(Context context,int type) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
        this.type = type;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_danjiang_realtime);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.CenterDialogAnimation);
        danJiangAdapter = new DanJiangRealtimeAdapter(R.layout.item_realtime_gift);
        rvGift.setLayoutManager(new GridLayoutManager(mContext,3));
        rvGift.setAdapter(danJiangAdapter);
        //占位控制高度
        ArrayList<RealtimeListBean.GiftBean> list = new ArrayList<>();
        while (list.size()<4){
            list.add(RealtimeListBean.GiftBean.newEmpty());
        }
        danJiangAdapter.setNewData(list);
        if (type == 1){ //普通奖池
            cl_bg.setBackgroundResource(R.drawable.bg_realtime_pool_normal);
            iv_fortune.setText(R.string.fortune_normal);
            iv_fortune.setBackgroundResource(R.drawable.tv_fortune_normal);
        }else {//钻石蛋奖池
            cl_bg.setBackgroundResource(R.drawable.bg_realtime_pool);
            iv_fortune.setText(R.string.fortune);
            iv_fortune.setBackgroundResource(R.drawable.tv_fortune);
        }
        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("type",type);
        String reqString = Api.jackpotPage;
        HttpManager.getInstance().post(reqString, map, new MyObserver(mContext) {
            @Override
            public void success(String responseString) {
                RealtimeListBean giftShowBean = JSON.parseObject(responseString, RealtimeListBean.class);
                if (giftShowBean.code == 0){
                    tvCount.setText(((int)giftShowBean.data.num)+"");
                    ArrayList<RealtimeListBean.GiftBean> list = new ArrayList<>();
                    if (giftShowBean.data.list.size()>6){
                        list.addAll(giftShowBean.data.list.subList(0,6));
                    }else
                        list.addAll(giftShowBean.data.list);
                    while (list.size()<6){
                        list.add(RealtimeListBean.GiftBean.newEmpty());
                    }
//                    if (list.size() == 5)//添加占位数据
//                        list.add(2,RealtimeListBean.GiftBean.newEmpty());
                    danJiangAdapter.setNewData(list);
                }else {
                    showToast(giftShowBean.msg);
                }
            }
        });
    }


    @OnClick({R.id.iv_fortune, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fortune:
                DanRankRealtimeDialog danRankDialog = new DanRankRealtimeDialog(mContext,type);
                danRankDialog.show();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
