package com.jingtaoi.yy.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.ui.mine.PayOneActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.Md5;


/**
 * 分享弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyPaypassDialog extends Dialog {

    Context mContext;
    @BindView(R.id.iv_close_paypass)
    ImageView ivClosePaypass;
    @BindView(R.id.tv_hint_paypass)
    TextView tvHintPaypass;
    @BindView(R.id.tv_forgetpass_paypass)
    TextView tvForgetpassPaypass;
    @BindView(R.id.edt_pay_gone)
    EditText edtPayGone;
    @BindView(R.id.tv_pass1)
    TextView tvPass1;
    @BindView(R.id.tv_pass2)
    TextView tvPass2;
    @BindView(R.id.tv_pass3)
    TextView tvPass3;
    @BindView(R.id.tv_pass4)
    TextView tvPass4;
    @BindView(R.id.tv_pass5)
    TextView tvPass5;
    @BindView(R.id.tv_pass6)
    TextView tvPass6;
    @BindView(R.id.ll_payshow)
    LinearLayout llPayshow;
    @BindView(R.id.tv_wrong_paypass)
    TextView tvWrongPaypass;
    @BindView(R.id.layout_paypass)
    LinearLayout layoutPaypass;

    TextView[] textViews;
    String payPassShow;

    public MyPaypassDialog(Activity context) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.layout_paypass);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);

        setShow();
    }

    private void setShow() {
        textViews = new TextView[]{tvPass1, tvPass2, tvPass3, tvPass4, tvPass5, tvPass6};
        edtPayGone.addTextChangedListener(textWatcher);
        ivClosePaypass.setOnClickListener(v -> {
            dismiss();
        });
        tvForgetpassPaypass.setOnClickListener(v -> {
            dismiss();
            Bundle bundle = new Bundle();
            bundle.putInt(Const.ShowIntent.TYPE, 1);
            ActivityCollector.getActivityCollector().toOtherActivity(PayOneActivity.class, bundle);
        });
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            payPassShow = edtPayGone.getText().toString();
            setHintShow(payPassShow);
            if (payPassShow.length() == 6) {
                setPayShow();
            }
        }
    };

    private void setPayShow() {
        String payReal = (String) SharedPreferenceUtils.get(mContext, Const.User.PAY_PASS, "");
        if (payReal.equals(Md5.getMd5Value(payPassShow))) {
            if (paySuccess != null) {
                paySuccess.onSuccessShow();
            }
        } else {
            tvWrongPaypass.setVisibility(View.VISIBLE);
            payPassShow = "";
            edtPayGone.setText(payPassShow);
        }
    }

    private void setHintShow(String payPass) {
        char[] payArray = payPass.toCharArray();
        for (TextView textView : textViews) {
            textView.setText("");
        }
        for (int i = 0; i < payArray.length; i++) {
//            textViews[i].setText(payArray[i] + "");
            textViews[i].setText("●");
        }
    }

    PaySuccess paySuccess;

    public PaySuccess getPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(PaySuccess paySuccess) {
        this.paySuccess = paySuccess;
    }

    public interface PaySuccess {
        void onSuccessShow();
    }

}