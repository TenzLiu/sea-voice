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
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.Md5;
import cn.sinata.xldutils.utils.StringUtils;

import static com.mobsandgeeks.saripaar.annotation.Password.Scheme.ALPHA;

/**
 * 绑定手机页面
 */
public class BindPhoneActivity extends MyBaseActivity {
    @BindView(R.id.edt_phone_bindphone)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_phone_login)
    @IsPhoneNumber(sequence = 2)
    @Order(1)
    EditText edtPhoneBindphone;
    @BindView(R.id.tv_getcode_bindphone)
    TextView tvGetcodeBindphone;
    @BindView(R.id.edt_psd_bindphone)
    @NotEmpty(message = "请输入验证码")
    @Order(2)
    EditText edtPsdBindphone;
    @BindView(R.id.edt_newpass_bindphone)
    @NotEmpty(message = "请输入密码")
    @Password(messageResId = R.string.hint_psdall_register, scheme = ALPHA)
    @Order(3)
    EditText edtNewpassBindphone;
    @BindView(R.id.btn_sure_bindphone)
    Button btnSureBindphone;

    Validator validator;

    String phone;
    VirifyCountDownTimer virifyCountDownTimer;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_bindphone);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_bindphone);
        validator = new Validator(this);
        validator.setValidationListener(validationListener);
        Validator.registerAnnotation(IsPhoneNumber.class);
        btnSureBindphone.setText("确认");
        edtPhoneBindphone.setHint(getString(R.string.hint_newphone_changephone));
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
                showToast(error.getFailedRules().get(0).getMessage(BindPhoneActivity.this));
            }
        }
    };

    private void getCall() {
        showDialog();
        phone = edtPhoneBindphone.getText().toString();
        String password = edtNewpassBindphone.getText().toString();
        String smsCode = edtPsdBindphone.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("phone", phone);
        map.put("password", Md5.getMd5Value(password));
        map.put("smsCode", smsCode);
        HttpManager.getInstance().post(Api.bindPhone, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("绑定手机成功");
                    SharedPreferenceUtils.put(BindPhoneActivity.this, Const.User.PHONE, phone);
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
