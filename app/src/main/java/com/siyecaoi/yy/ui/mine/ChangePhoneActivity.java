package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.control.VirifyCountDownTimer;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.utils.spripaar.IsPhoneNumber;
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
 * 修改绑定手机号
 */
public class ChangePhoneActivity extends MyBaseActivity {
    @BindView(R.id.edt_oldphone_changephone)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_phone_login)
    @IsPhoneNumber(sequence = 2)
    @Order(1)
    EditText edtOldphoneChangephone;
    @BindView(R.id.edt_newphone_changephone)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_newphone_login)
    @IsPhoneNumber(sequence = 2)
    @Order(2)
    EditText edtNewphoneChangephone;
    @BindView(R.id.edt_repass_changepass)
    @NotEmpty(message = "请输入验证码")
    @Order(3)
    EditText edtRepassChangepass;
    @BindView(R.id.tv_getcode_changephone)
    TextView tvGetcodeChangephone;
    @BindView(R.id.btn_sure_changephone)
    Button btnSureChangephone;

    Validator validator;

    String phone;
    VirifyCountDownTimer virifyCountDownTimer;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_changephone);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_changephone);
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
                showToast(error.getFailedRules().get(0).getMessage(ChangePhoneActivity.this));
            }
        }
    };

    private void getCall() {
        showDialog();
        phone = edtNewphoneChangephone.getText().toString();
        String oldphone = edtOldphoneChangephone.getText().toString();
        String smsCode = edtRepassChangepass.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("phone", phone);
        map.put("oldphone", oldphone);
        map.put("smsCode", smsCode);
        HttpManager.getInstance().post(Api.ModifyPhone, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("修改绑定手机成功");
                    SharedPreferenceUtils.put(ChangePhoneActivity.this, Const.User.PHONE, phone);
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
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

    @OnClick({R.id.tv_getcode_changephone, R.id.btn_sure_changephone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode_changephone:
                phone = edtNewphoneChangephone.getText().toString();
                String oldphone = edtOldphoneChangephone.getText().toString();
                if (!StringUtils.isPhoneNumberValid(phone)) {
                    showToast(getString(R.string.hint_phone_login));
                    return;
                }
                String getOldPhone = (String) SharedPreferenceUtils.get(ChangePhoneActivity.this, Const.User.PHONE, "");
                if (!StringUtils.isEmpty(getOldPhone)) {
                    if (!oldphone.equals(getOldPhone)) {
                        showToast(getString(R.string.hint_phone_changephone));
                        return;
                    }
                }
                virifyCountDownTimer =
                        new VirifyCountDownTimer(tvGetcodeChangephone, 60000, 1000);
                getCodeCall(phone);
                break;
            case R.id.btn_sure_changephone:
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
