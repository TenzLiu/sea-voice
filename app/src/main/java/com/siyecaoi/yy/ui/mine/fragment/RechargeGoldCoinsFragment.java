package com.siyecaoi.yy.ui.mine.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.alipay.PayResult;
import com.siyecaoi.yy.bean.AlipayBean;
import com.siyecaoi.yy.bean.TopupListBean;
import com.siyecaoi.yy.bean.TopupOrderBean;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.bean.WechatBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.WalletActivity;
import com.siyecaoi.yy.ui.room.adapter.TopupListAdapter;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RechargeGoldCoinsFragment extends DialogFragment {
    Unbinder unbinder;

    View rootView;
    protected String TAG = "msg";
    TopupListAdapter topupListAdapter;

    @BindView(R.id.mRecyclerView_topup)
    RecyclerView mRecyclerViewTopup;
    private int userToken;
    private WalletActivity activity;
    @BindView(R.id.tv_gold_topup)
    TextView tvGoldTopup;


    @BindView(R.id.btn_Submit)
    TextView btn_Submit;


    @BindView(R.id.zfb_iv)
    ImageView zfb_iv;


    @BindView(R.id.wx_iv)
    ImageView wx_iv;

    @BindView(R.id.tv_topupone_topup)
    TextView tvTopuponeTopup;

    @BindView(R.id.iv_close_message_d)
    ImageView iv_close_message_d;


    private TopupListBean.DataBean.SetRechargeBean setRechargeBean;
    private int payType;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_recharge_gold_coins, container);
        unbinder = ButterKnife.bind(this, view);
        rootView = view.getRootView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        activity = (WalletActivity) getActivity();
        userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);

        setRecycler();

        getCall();
        getUserCall();

        setBroadCast();

        payType = 0;
        zfb_iv.setImageResource(R.drawable.check);
        wx_iv.setImageResource(R.drawable.checknull);

        iv_close_message_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        WindowManager manager = getActivity().getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        int height = outMetrics.heightPixels;
//        params.height = height / 3 * 2;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
//        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        super.onResume();
    }


    private void setBroadCast() {
        BroadcastManager.getInstance(getContext()).addAction(Const.BroadCast.WECHAT_PAYSUCCECSS, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LogUtils.e("调用更新显示");
                getUserCall();
            }
        });
    }

    private void getUserCall() {
        activity.showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.getUserInfo, map, new MyObserver(getActivity()) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    initShared(userBean.getData());
                } else {
                    activity.showToast(userBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initShared(UserBean.DataBean dataBean) {
        SharedPreferenceUtils.put(getContext(), Const.User.USER_TOKEN, dataBean.getId());
        SharedPreferenceUtils.put(getContext(), Const.User.AGE, dataBean.getAge());
        SharedPreferenceUtils.put(getContext(), Const.User.IMG, dataBean.getImgTx());
        SharedPreferenceUtils.put(getContext(), Const.User.SEX, dataBean.getSex());
        SharedPreferenceUtils.put(getContext(), Const.User.NICKNAME, dataBean.getNickname());
        SharedPreferenceUtils.put(getContext(), Const.User.ROOMID, dataBean.getUsercoding());
        SharedPreferenceUtils.put(getContext(), Const.User.CharmGrade, dataBean.getCharmGrade());
        SharedPreferenceUtils.put(getContext(), Const.User.DATEOFBIRTH, dataBean.getDateOfBirth());
        SharedPreferenceUtils.put(getContext(), Const.User.FansNum, dataBean.getFansNum());
        SharedPreferenceUtils.put(getContext(), Const.User.AttentionNum, dataBean.getAttentionNum());
        SharedPreferenceUtils.put(getContext(), Const.User.GOLD, dataBean.getGold());
        SharedPreferenceUtils.put(getContext(), Const.User.GoldNum, dataBean.getGoldNum());
        SharedPreferenceUtils.put(getContext(), Const.User.GRADE_T, dataBean.getTreasureGrade());
        SharedPreferenceUtils.put(getContext(), Const.User.PHONE, dataBean.getPhone());
        SharedPreferenceUtils.put(getContext(), Const.User.QQSID, dataBean.getQqSid());
        SharedPreferenceUtils.put(getContext(), Const.User.WECHATSID, dataBean.getWxSid());
        SharedPreferenceUtils.put(getContext(), Const.User.Ynum, dataBean.getYnum());
        SharedPreferenceUtils.put(getContext(), Const.User.Yuml, dataBean.getYuml());
        SharedPreferenceUtils.put(getContext(), Const.User.HEADWEAR_H, dataBean.getUserThfm());
        SharedPreferenceUtils.put(getContext(), Const.User.CAR_H, dataBean.getUserZjfm());
        SharedPreferenceUtils.put(getContext(), Const.User.HEADWEAR, dataBean.getUserTh());
        SharedPreferenceUtils.put(getContext(), Const.User.CAR, dataBean.getUserZj());
//        SharedPreferenceUtils.put(this, Const.User.USER_SIG, dataBean.getToken());


        tvGoldTopup.setText(dataBean.getGold()+"");
    }


    private void getCall() {
        activity.showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.SaveRecharge, map, new MyObserver(getActivity()) {
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
        topupListAdapter = new TopupListAdapter(R.layout.item_topup1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerViewTopup.setLayoutManager(gridLayoutManager);
        mRecyclerViewTopup.setAdapter(topupListAdapter);

        topupListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setRechargeBean = (TopupListBean.DataBean.SetRechargeBean) adapter.getItem(position);
                topupListAdapter.setChoosePostion(position);
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
                        activity.showToast("支付成功");
                        getUserCall();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        activity.showToast("取消支付");
                    } else {
                        activity.showToast("支付失败");
                    }
                    break;
            }
        }
    };




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BroadcastManager.getInstance(getContext()).destroy(Const.BroadCast.WECHAT_PAYSUCCECSS);

        activity.dismissDialog();
        unbinder.unbind();
    }

    //    @OnClick({R.id.view_message_d, R.id.iv_close_message_d})
    @OnClick({R.id.view_message_d,R.id.zfb_ll,R.id.wx_ll,R.id.btn_Submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_message_d:
                dismiss();
                break;
            case R.id.iv_close_message_d:
                dismiss();
                break;
            case R.id.zfb_ll:
                //支付宝支付
                payType = 0;
                zfb_iv.setImageResource(R.drawable.check);
                wx_iv.setImageResource(R.drawable.checknull);

                break;

            case R.id.wx_ll:
                //微信支付
                payType=1;
                zfb_iv.setImageResource(R.drawable.checknull);
                wx_iv.setImageResource(R.drawable.check);
                break;
            case R.id.btn_Submit:

                activity.showToast("期待开放");

//                if (setRechargeBean==null) {
//                    setRechargeBean = topupListAdapter.getItem(0);
//                }
//                switch (payType) {
//                    case 0://支付宝支付
//                        getOrderCall(Const.IntShow.ONE, setRechargeBean.getId());
//                        break;
//                    case 1://微信支付
//                        getOrderCall(Const.IntShow.TWO, setRechargeBean.getId());
//                        break;
//                }
                break;
        }
    }


}
