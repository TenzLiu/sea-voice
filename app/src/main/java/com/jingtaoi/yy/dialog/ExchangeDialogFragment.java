package com.jingtaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.ExchangeBean;
import com.jingtaoi.yy.bean.GetOneBean;
import com.jingtaoi.yy.bean.LotteryMyincomeBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExchangeDialogFragment extends DialogFragment {
    Unbinder unbinder;

    @BindView(R.id.edt_number_exchangeyou)
    EditText edtNumberExchangeyou;
    @BindView(R.id.tv_diamond_exchangeyou)
    TextView tvDiamondExchangeyou;
    @BindView(R.id.tv_you_exchangeyou)
    TextView tvYouExchangeyou;
    @BindView(R.id.btn_sure_exchangeyou)
    Button btnSureExchangeyou;
    @BindView(R.id.tv_show_exchangeyou)
    TextView tvShowExchangeyou;

    double Ynum;
    @BindView(R.id.tv_show1_exchangeyou)
    TextView tvShow1Exchangeyou;

    View rootView;
    protected String TAG = "msg";


    @BindView(R.id.iv_close_message_d)
    ImageView iv_close_message_d;

    private MyBaseActivity activity;
    private int userToken;
    private int type;
    private String profitBalance;
    private DiassDialogLiser dialogLiser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_exchange, container);
        unbinder = ButterKnife.bind(this, view);
        rootView = view.getRootView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MyBaseActivity) getActivity();
        userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);

        setShow();

        edtNumberExchangeyou.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = edtNumberExchangeyou.getText().toString();
                if (str.length() >= 1) {
                    if (str.equals("0")) {
                        str = "";
                        edtNumberExchangeyou.setText(str);
                        //设置新的光标所在位置
                        edtNumberExchangeyou.setSelection(str.length());
                        btnSureExchangeyou.setAlpha(0.5f);
                        btnSureExchangeyou.setEnabled(false);
                    } else if (str.endsWith("0")) {
                        tvShowExchangeyou.setText(str);
                        btnSureExchangeyou.setAlpha(1.0f);
                        btnSureExchangeyou.setEnabled(true);
                    } else {
                        tvShowExchangeyou.setText("");
                        btnSureExchangeyou.setAlpha(0.5f);
                        btnSureExchangeyou.setEnabled(false);
                    }
                    int endNumber = 0;
                    if (!TextUtils.isEmpty(str)){
                        endNumber = Integer.parseInt(str);
                    }
                    if (endNumber > Ynum) {
                        activity.showToast("钻石数量少于兑换数量");
                    }
                }
            }
        });

        getSHowCall();


    }

    public void setType(int type){
        this.type=type;
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


    private void getSHowCall() {
//        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.UserConvertlist, map, new MyObserver(getActivity()) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean!=null && getOneBean.getData()!=null && getOneBean.getData().getStr()!=null){
                    tvShow1Exchangeyou.setText(getOneBean.getData().getStr());
                }
            }
        });
    }




    @SuppressLint("SetTextI18n")
    private void setShow() {
        int gold = (int) SharedPreferenceUtils.get(getContext(), Const.User.GOLD, 0);
        String strYnum = profitBalance;
        Ynum = Double.parseDouble(strYnum);
        tvDiamondExchangeyou.setText(Ynum + "");
        tvYouExchangeyou.setText(gold + "");
    }


    @OnClick(R.id.btn_sure_exchangeyou)
    public void onViewClicked() {
        String changeShow = edtNumberExchangeyou.getText().toString();
        if (changeShow.endsWith("0")) {
            updateCall(changeShow);
        } else {
            activity.showToast("钻石数量必须是10的整数倍");
        }
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

    //    @OnClick({R.id.view_message_d, R.id.iv_close_message_d})
    @OnClick({R.id.view_message_d,R.id.iv_close_message_d})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_message_d:
                dismiss();
                break;
            case R.id.iv_close_message_d:
                dismiss();
                break;

        }
    }


    public void setYE(String profitBalance) {
        this.profitBalance=profitBalance;
    }
}
