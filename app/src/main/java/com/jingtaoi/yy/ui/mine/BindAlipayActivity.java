package com.jingtaoi.yy.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.control.VirifyCountDownTimer;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;

public class BindAlipayActivity extends MyBaseActivity {
    @BindView(R.id.edt_alipay_bindalipay)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_alipay_bindalipay)
    @Order(1)
    EditText edtAlipayBindalipay;
    @BindView(R.id.edt_name_bindalipay)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_name_bindalipay)
    @Order(2)
    EditText edtNameBindalipay;
    @BindView(R.id.edt_code_bindalipay)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_code_bindalipay)
    @Order(3)
    EditText edtCodeBindalipay;
    @BindView(R.id.tv_getcode_bindalipay)
    TextView tvGetcodeBindalipay;

    Validator validator;

    String phone;
    VirifyCountDownTimer virifyCountDownTimer;
    @BindView(R.id.btn_sure_bindalipay)
    Button btnSureBindalipay;

    private String payAccount;
    private String trueName;

    @Override
    public void initData() {
        payAccount = getBundleString("payAccount");
        trueName = getBundleString("trueName");
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_bindalipay);
    }

    @Override
    public void initView() {

        setTitleText(TextUtils.isEmpty(payAccount)?R.string.title_bindalipay:R.string.title_changealipay);

        validator = new Validator(this);
        validator.setValidationListener(validationListener);
        phone = (String) SharedPreferenceUtils.get(this, Const.User.PHONE, "");

        if (!StringUtils.isEmpty(payAccount)) {
            edtAlipayBindalipay.setText(payAccount);
        }
        if (!StringUtils.isEmpty(trueName)) {
            edtNameBindalipay.setText(trueName);
        }
    }

    Validator.ValidationListener validationListener = new Validator.ValidationListener() {
        @Override //正确
        public void onValidationSucceeded() {
            getCall();
        }

        @Override //有误
        public void onValidationFailed(List<ValidationError> errors) {
            for (ValidationError error : errors) {
                View view = error.getView();
                showToast(error.getFailedRules().get(0).getMessage(BindAlipayActivity.this));
            }
        }
    };

    private void getCall() {
        String payAccount = edtAlipayBindalipay.getText().toString();
        String trueName = edtNameBindalipay.getText().toString();
        String smsCode = edtCodeBindalipay.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("payAccount", payAccount);
        map.put("trueName", trueName);
        map.put("smsCode", smsCode);
        map.put("phone", phone);
        HttpManager.getInstance().post(Api.UserPay, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean userBean = JSON.parseObject(responseString, BaseBean.class);
                if (userBean.getCode() == 0) {
                    showToast("支付宝绑定成功");
                    ActivityCollector.getActivityCollector().finishActivity();
                }else {
                    showToast(userBean.getMsg());
                }
            }
        });
    }

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

    @OnClick({R.id.tv_getcode_bindalipay, R.id.btn_sure_bindalipay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode_bindalipay:
                if (!StringUtils.isPhoneNumberValid(phone)) {
                    showToast("请先绑定手机号码");
                    return;
                }
                virifyCountDownTimer =
                        new VirifyCountDownTimer(tvGetcodeBindalipay, 60000, 1000);
                getCodeCall(phone);
                break;
            case R.id.btn_sure_bindalipay:
                validator.validate();
                break;
        }
    }

    private void getCodeCall(String phone) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phone);
        map.put("type", String.valueOf(Const.IntShow.THREE));
        HttpManager.getInstance().post(Api.getSmsCode, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = new Gson().fromJson(responseString, BaseBean.class);
                showToast(baseBean.getMsg());
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (virifyCountDownTimer != null) {
                        virifyCountDownTimer.start();
                    }
                }
            }
        });
    }
}
