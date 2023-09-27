package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.DrawBean;
import com.siyecaoi.yy.bean.ExchangeBean;
import com.siyecaoi.yy.bean.LotteryMyincomeBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.BindAlipayActivity;
import com.siyecaoi.yy.ui.mine.PayOneActivity;
import com.siyecaoi.yy.ui.mine.adapter.DrawListAdapter;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;

public class WithdrawDialogFragment extends DialogFragment {
    Unbinder unbinder;

    @BindView(R.id.tv_bind_draw)
    TextView tvBindDraw;
    @BindView(R.id.tv_alipayaccount_draw)
    TextView tvAlipayaccountDraw;
    @BindView(R.id.tv_alipayname_draw)
    TextView tvAlipaynameDraw;
    @BindView(R.id.tv_change)
    TextView tv_change;
    @BindView(R.id.rl_alipay_draw)
    RelativeLayout rlAlipayDraw;
    @BindView(R.id.mRecyclerView_draw)
    RecyclerView mRecyclerViewDraw;
    @BindView(R.id.btn_draw)
    Button btnDraw;
    DrawListAdapter drawListAdapter;
    @BindView(R.id.tv_show_draw)
    TextView tvShowDraw;
    @BindView(R.id.tv_showmon_draw)
    TextView tvShowmonDraw;
    @BindView(R.id.edt_mon_draw)
    EditText edtMonDraw;

    int id;
    double yNum;
    boolean isHasAlipay;
    private String trueName;
    private String payAccount;
    private int zsNum;
    private int type;

    View rootView;


    private MyBaseActivity activity;
    private int userToken;
    private String profitBalance;
    private DiassDialogLiser dialogLiser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_withdraw, container);
        unbinder = ButterKnife.bind(this, view);
        rootView = view.getRootView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MyBaseActivity) getActivity();
        userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);
        type = getArguments().getInt("type");
        setShow();

        getCall();
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
        getCall();
    }



    @SuppressLint("SetTextI18n")
    private void setShow() {
        int gold = (int) SharedPreferenceUtils.get(getContext(), Const.User.GOLD, 0);
        String strYnum = profitBalance;
        setRecycler();

    }

    private void setRecycler() {
        drawListAdapter = new DrawListAdapter(R.layout.item_draw);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        mRecyclerViewDraw.setLayoutManager(layoutManager);
        mRecyclerViewDraw.setAdapter(drawListAdapter);

        drawListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DrawBean.DataEntity.WithdrawEntity withdrawEntity = (DrawBean.DataEntity.WithdrawEntity) adapter.getItem(position);
                drawListAdapter.setChooseOne(position);
                assert withdrawEntity != null;
                id = withdrawEntity.getId();
                zsNum = withdrawEntity.getY();
                if (yNum == 0 || id == 0 || !isHasAlipay) {
                    btnDraw.setAlpha(0.5f);
                    btnDraw.setClickable(false);
                } else {
                    btnDraw.setAlpha(1.0f);
                    btnDraw.setClickable(true);
                }
            }
        });
    }


    private void getCall() {
//        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", type);
        HttpManager.getInstance().post(Api.SetWithdrawServiceList, map, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                DrawBean drawBean = JSON.parseObject(responseString, DrawBean.class);
                if (drawBean.getCode() == Api.SUCCESS) {
                    setData(drawBean.getData());
                } else {
                    showToast(drawBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(DrawBean.DataEntity data) {
        drawListAdapter.setNewData(data.getWithdraw());
        if (data.getWithdraw().size() > 0) {
            id = data.getWithdraw().get(0).getId();
        }
        if (data.getState() == 1) { //没有支付宝
            btnDraw.setAlpha(0.5f);
            isHasAlipay = false;
            btnDraw.setClickable(false);
        } else if (data.getState() == 2) {
            isHasAlipay = true;
            btnDraw.setAlpha(1.0f);
            btnDraw.setClickable(true);
            tvBindDraw.setVisibility(View.INVISIBLE);
            tv_change.setVisibility(View.VISIBLE);
            trueName = data.getTrueName();
            payAccount = data.getPayAccount();
//            tvAlipayaccountDraw.setText(payAccount.charAt(0)+"**");
            tvAlipayaccountDraw.setText(payAccount);
            tvAlipaynameDraw.setText(trueName.charAt(0)+"**");
        }
//        tvShowmonDraw.setText("最高可提现" + "元");
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(getString(R.string.tv_show_draw));
        int len = stringBuilder.length();
        stringBuilder.append("  钻石余额：");
        stringBuilder.append(data.getYnum());
        yNum = Double.parseDouble(data.getYnum());
        SharedPreferenceUtils.put(activity, Const.User.Ynum, data.getYnum());

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.FF003F));
        stringBuilder.setSpan(colorSpan, len,
                stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(24);
        stringBuilder.setSpan(absoluteSizeSpan, len, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvShowDraw.setText(data.getYnum());
        SharedPreferenceUtils.put(activity, Const.User.GOLD, data.getGold());
        SharedPreferenceUtils.put(activity, Const.User.Ynum, data.getYnum());
    }

    private void updateCall(String changeShow) {
//        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("gold", changeShow);
        map.put("type",type);
        HttpManager.getInstance().post(Api.UserConvert, map, new MyObserver(getActivity()) {
            @Override
            public void success(String responseString) {
                ExchangeBean baseBean = JSON.parseObject(responseString, ExchangeBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showMyShowDialog(baseBean);
                    getUserCall();
                    dialogLiser.diass();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    MyChangeYouDialog myChangeYouDialog;

    private void showMyShowDialog(ExchangeBean baseBean) {
        if (myChangeYouDialog != null && myChangeYouDialog.isShowing()) {
            myChangeYouDialog.dismiss();
        }
        myChangeYouDialog = new MyChangeYouDialog(getContext(), baseBean);
        myChangeYouDialog.show();
    }

    private void getUserCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type",type);
        HttpManager.getInstance().post(Api.LOTTERY_MYINCOME, map, new MyObserver(getActivity()) {



            @Override
            public void success(String responseString) {

                LotteryMyincomeBean storeListBean = JSON.parseObject(responseString, LotteryMyincomeBean.class);
                if (storeListBean.getCode() == Api.SUCCESS) {

                    if (storeListBean.getData()!=null){
                        profitBalance = storeListBean.getData().getProfitBalance();
                        setShow();
                    }
                } else {
                    showToast(storeListBean.getMsg());
                }
            }
        });
    }

    public void setDiassDialogLiser(DiassDialogLiser  dialogLiser){
        this.dialogLiser=dialogLiser;

    }

    public  interface  DiassDialogLiser{
        void diass();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @OnClick({R.id.rl_alipay_draw, R.id.btn_draw,R.id.view_message_d,R.id.iv_close_message_d})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_message_d:
                dismiss();
                break;
            case R.id.iv_close_message_d:
                dismiss();
                break;
            case R.id.rl_alipay_draw:
                Bundle bundle = new Bundle();
                bundle.putString("trueName", trueName);
                bundle.putString("payAccount", payAccount);
                ActivityCollector.getActivityCollector().toOtherActivity(BindAlipayActivity.class, bundle);
                break;
            case R.id.btn_draw:

                if (id == 0) {
                    activity.showToast("请选择提现金额");
                    return;
                }

                if(yNum<=0f){
                    activity.showToast("您的钻石余额不足");
                    return;
                }
                if(yNum<zsNum){
                    activity.showToast("您的钻石余额不足"+zsNum);
                    return;
                }


                String realPay = (String) SharedPreferenceUtils.get(activity, Const.User.PAY_PASS, "");
                if (StringUtils.isEmpty(realPay)) {
                    showSetPassDialog();
                } else {
                    showPassDialog();
                }
//                getDrawCall();
                break;
        }
    }

    MyDialog myDialog;

    private void showSetPassDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(activity);
        myDialog.show();
        myDialog.setHintText("您还没有设置支付密码，请前往设置");
        myDialog.setRightText("去设置");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.TYPE, 1);
                ActivityCollector.getActivityCollector().toOtherActivity(PayOneActivity.class, bundle);
            }
        });
    }

    MyPaypassDialog myPaypassDialog;

    private void showPassDialog() {
        if (myPaypassDialog != null && myPaypassDialog.isShowing()) {
            myPaypassDialog.dismiss();
        }
        myPaypassDialog = new MyPaypassDialog(activity);
        myPaypassDialog.show();
        myPaypassDialog.setPaySuccess(new MyPaypassDialog.PaySuccess() {
            @Override
            public void onSuccessShow() {
                if (myPaypassDialog != null && myPaypassDialog.isShowing()) {
                    myPaypassDialog.dismiss();
                }
                getDrawCall();
            }
        });
    }

    private void getDrawCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("id", id);
        map.put("type", type);
        HttpManager.getInstance().post(Api.AddSetWithdraws, map, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                BaseBean userBean = JSON.parseObject(responseString, BaseBean.class);
                if (userBean.getCode() == 0) {
                    showToast("提现申请成功,请耐心等待审核");
                    getCall();
                }else {
                    showToast(userBean.getMsg());
                }

            }
        });
    }

    public void setYE(String profitBalance) {
        this.profitBalance=profitBalance;
    }
}
