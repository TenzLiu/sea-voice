package com.jingtaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.control.VirifyCountDownTimer;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.spripaar.IsPhoneNumber;
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

/**
 * 支付密码验证手机号页面
 *
 * @author xha
 * @data 2019/9/27
 */
public class PayOneActivity extends MyBaseActivity {
    @NotEmpty(sequence = 1, messageResId = R.string.hint_phone_login)
    @IsPhoneNumber(sequence = 2)
    @Order(1)
    @BindView(R.id.edt_phone_bindphone)
    EditText edtPhoneBindphone;
    @BindView(R.id.tv_getcode_bindphone)
    TextView tvGetcodeBindphone;
    @BindView(R.id.edt_psd_bindphone)
    @NotEmpty(message = "请输入验证码")
    @Order(2)
    EditText edtPsdBindphone;
    @BindView(R.id.btn_sure_bindphone)
    Button btnSureBindphone;

    Validator validator;

    String phone;
    VirifyCountDownTimer virifyCountDownTimer;
    int type;

    @Override
    public void initData() {
        type = getBundleInt(Const.ShowIntent.TYPE,0);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_payone);
    }

    @Override
    public void initView() {
        if (type == 0) {
            setTitleText(R.string.tv_changepass_setting);
        } else {
            setTitleText(R.string.tv_changepass1_setting);
        }
        validator = new Validator(this);
        validator.setValidationListener(validationListener);
        Validator.registerAnnotation(IsPhoneNumber.class);
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
                showToast(error.getFailedRules().get(0).getMessage(PayOneActivity.this));
            }
        }
    };

    private void getCall() {
        String smsCode = edtPsdBindphone.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("smsCode", smsCode);
        ActivityCollector.getActivityCollector().toOtherActivity(PayPassActivity.class, bundle);
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


    @OnClick({R.id.tv_getcode_bindphone, R.id.btn_sure_bindphone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode_bindphone:
                phone = edtPhoneBindphone.getText().toString();
                if (!StringUtils.isPhoneNumberValid(phone)) {
                    showToast(getString(R.string.hint_phone_login));
                    return;
                }
                virifyCountDownTimer =
                        new VirifyCountDownTimer(tvGetcodeBindphone, 60000, 1000);
                getCodeCall(phone);
                break;
            case R.id.btn_sure_bindphone:
                validator.validate();
                break;
        }
    }

    private void getCodeCall(String phone) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phone);
        map.put("type", String.valueOf(Const.IntShow.FOUR));
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
