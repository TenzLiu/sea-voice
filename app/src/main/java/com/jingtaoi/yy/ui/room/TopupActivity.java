package com.jingtaoi.yy.ui.room;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.alipay.PayResult;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.TopupListBean;
import com.jingtaoi.yy.bean.UserBean;
import com.jingtaoi.yy.dialog.MyBottomShowDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.mine.TopupHistoryActivity;
import com.jingtaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.jingtaoi.yy.ui.room.adapter.TopupListAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.BroadcastManager;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopupActivity extends MyBaseActivity {

    @BindView(R.id.iv_user_topup)
    SimpleDraweeView ivUserTopup;
    @BindView(R.id.tv_name_topup)
    TextView tvNameTopup;
    @BindView(R.id.tv_gold_topup)
    TextView tvGoldTopup;
    @BindView(R.id.mRecyclerView_topup)
    RecyclerView mRecyclerViewTopup;

    TopupListAdapter topupListAdapter;
    @BindView(R.id.tv_topupone_topup)
    TextView tvTopuponeTopup;

    MyBottomShowDialog myBottomShowDialog;
    private ArrayList<String> bottomList;//弹框显示内容

    @Override
    public void initData() {
        bottomList = new ArrayList<>();
        bottomList.add(getString(R.string.tv_alipay_topup));
        bottomList.add(getString(R.string.tv_weichat_topup));
        bottomList.add(getString(R.string.tv_cancel));
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_topup);
    }

    @Override
    public void initView() {
        setTitleText(R.string.tv_topup_gift);
        setRightText(R.string.tv_topuphis_gift);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.getActivityCollector().toOtherActivity(TopupHistoryActivity.class);
            }
        });


        setRecycler();

        getCall();
        getUserCall();

        setBroadCast();
    }

    private void setBroadCast() {
        BroadcastManager.getInstance(this).addAction(Const.BroadCast.WECHAT_PAYSUCCECSS, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LogUtils.e("调用更新显示");
                getUserCall();
            }
        });
    }

    private void getUserCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.getUserInfo, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    initShared(userBean.getData());
                } else {
                    showToast(userBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initShared(UserBean.DataBean dataBean) {
        SharedPreferenceUtils.put(this, Const.User.USER_TOKEN, dataBean.getId());
        SharedPreferenceUtils.put(this, Const.User.AGE, dataBean.getAge());
        SharedPreferenceUtils.put(this, Const.User.IMG, dataBean.getImgTx());
        SharedPreferenceUtils.put(this, Const.User.SEX, dataBean.getSex());
        SharedPreferenceUtils.put(this, Const.User.NICKNAME, dataBean.getNickname());
        SharedPreferenceUtils.put(this, Const.User.ROOMID, dataBean.getUsercoding());
        SharedPreferenceUtils.put(this, Const.User.CharmGrade, dataBean.getCharmGrade());
        SharedPreferenceUtils.put(this, Const.User.DATEOFBIRTH, dataBean.getDateOfBirth());
        SharedPreferenceUtils.put(this, Const.User.FansNum, dataBean.getFansNum());
        SharedPreferenceUtils.put(this, Const.User.AttentionNum, dataBean.getAttentionNum());
        SharedPreferenceUtils.put(this, Const.User.GOLD, dataBean.getGold());
        SharedPreferenceUtils.put(this, Const.User.GoldNum, dataBean.getGoldNum());
        SharedPreferenceUtils.put(this, Const.User.GRADE_T, dataBean.getTreasureGrade());
        SharedPreferenceUtils.put(this, Const.User.PHONE, dataBean.getPhone());
        SharedPreferenceUtils.put(this, Const.User.QQSID, dataBean.getQqSid());
        SharedPreferenceUtils.put(this, Const.User.WECHATSID, dataBean.getWxSid());
        SharedPreferenceUtils.put(this, Const.User.Ynum, dataBean.getYnum());
        SharedPreferenceUtils.put(this, Const.User.Yuml, dataBean.getYuml());
        SharedPreferenceUtils.put(this, Const.User.HEADWEAR_H, dataBean.getUserThfm());
        SharedPreferenceUtils.put(this, Const.User.CAR_H, dataBean.getUserZjfm());
        SharedPreferenceUtils.put(this, Const.User.HEADWEAR, dataBean.getUserTh());
        SharedPreferenceUtils.put(this, Const.User.CAR, dataBean.getUserZj());
        SharedPreferenceUtils.put(this, Const.User.IS_AGENT_GIVE, dataBean.getIsAgentGive());
//        SharedPreferenceUtils.put(this, Const.User.USER_SIG, dataBean.getToken());

        ImageUtils.loadUri(ivUserTopup, dataBean.getImgTx());
        tvNameTopup.setText(dataBean.getNickname());
        tvGoldTopup.setText(dataBean.getGold() + getString(R.string.tv_you));
    }


    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.SaveRecharge, map, new MyObserver(this) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                TopupListBean topupListBean = JSON.parseObject(responseString, TopupListBean.class);
                if (topupListBean.getCode() == Api.SUCCESS) {
//                    for (TopupListBean.DataBean.SetRechargeBean setRechargeBean :
//                            topupListBean.getData().getSetRecharge()) {
//                        if (setRechargeBean.getId() == 1) {
//                            topupListAdapter.setSendGold(setRechargeBean.getX());
//                        }
//                    }
                    List<TopupListBean.DataBean.SetRechargeBean> listTop = topupListBean.getData().getSetRecharge();
                    if (listTop.size() > 1) {
                        if (topupListBean.getData().getState() == 1) { //首冲
                            tvTopuponeTopup.setVisibility(View.VISIBLE);
                            tvTopuponeTopup.setText("首充送" + listTop.get(0).getX() + "浪花");
                        }
                        listTop.remove(0);
                        topupListAdapter.setNewData(listTop);
                    }
                } else {
                    showToast(topupListBean.getMsg());
                }
            }
        });
    }

    private void setRecycler() {
        topupListAdapter = new TopupListAdapter(R.layout.item_topup);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerViewTopup.setLayoutManager(gridLayoutManager);
        mRecyclerViewTopup.setAdapter(topupListAdapter);

        topupListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopupListBean.DataBean.SetRechargeBean setRechargeBean = (TopupListBean.DataBean.SetRechargeBean) adapter.getItem(position);
                topupListAdapter.setChoosePostion(position);
                assert setRechargeBean != null;
                showMybottomDialog(setRechargeBean.getId());
            }
        });
    }

    private void showMybottomDialog(int id) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(TopupActivity.this, bottomList);
        myBottomShowDialog.show();
        BottomShowRecyclerAdapter adapter = myBottomShowDialog.getAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
                switch (position) {
                    case 0://支付宝支付
                        break;
                    case 1://微信支付
                        break;
                }
            }
        });
    }



    private static final int SDK_PAY_ALIPAY = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_ALIPAY:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    LogUtils.e(TAG, "handleMessage: 支付结果" + payResult.getMemo());
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    Bundle bundle = new Bundle();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        LogUtils.e(TAG, "handleMessage: 跳转订单详情页");
                        showToast("支付成功");
                        getUserCall();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        showToast("取消支付");
                    } else {
                        showToast("支付失败");
                    }
                    break;
            }
        }
    };

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.WECHAT_PAYSUCCECSS);
        super.onDestroy();
    }
}
