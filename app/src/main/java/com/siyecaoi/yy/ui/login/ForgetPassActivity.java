package com.siyecaoi.yy.ui.login;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.control.VirifyCountDownTimer;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.spripaar.IsPhoneNumber;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.Md5;
import cn.sinata.xldutils.utils.StringUtils;

import static com.mobsandgeeks.saripaar.annotation.Password.Scheme.ALPHA;

/**
 * 忘记密码
 */
public class ForgetPassActivity extends MyBaseActivity {
    @BindView(R.id.edt_phone_forgetpass)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_phone_login)
    @IsPhoneNumber(sequence = 2)
    @Order(1)
    EditText edtPhoneForgetpass;
    @BindView(R.id.tv_getcode_forgetpass)
    TextView tvGetcodeForgetpass;
    @BindView(R.id.edt_psd_forgetpass)
    @NotEmpty(message = "请输入验证码")
    @Order(2)
    EditText edtPsdForgetpass;
    @BindView(R.id.edt_newpass_forgetpass)
    @NotEmpty(message = "请输入新密码")
    @Password(messageResId = R.string.hint_psdall_register, scheme = ALPHA)
    @Order(3)
    EditText edtNewpassForgetpass;
    @BindView(R.id.iv_show_forgetpass)
    ImageView ivShowForgetpass;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @BindView(R.id.back_iv)
    ImageView back_iv;

    Validator validator;

    VirifyCountDownTimer virifyCountDownTimer;

    String phone;

    boolean isShowPass;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_forgetpass);
    }

    @Override
    public void initView() {

        validator = new Validator(this);
        validator.setValidationListener(validationListener);
        Validator.registerAnnotation(IsPhoneNumber.class);

        showTitle(false);
        showHeader(false);
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                showToast(error.getFailedRules().get(0).getMessage(ForgetPassActivity.this));
            }
        }
    };

    private void getCall() {
        showDialog();
        phone = edtPhoneForgetpass.getText().toString();
        String pass = edtNewpassForgetpass.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phone);
        map.put("smsCode", edtPsdForgetpass.getText().toString());
        map.put("password", Md5.getMd5Value(pass));
        HttpManager.getInstance().post(Api.ForgotPassword, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    showToast(getString(R.string.hint_passchange_forgetpass));
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

    @OnClick({R.id.tv_getcode_forgetpass, R.id.iv_show_forgetpass, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode_forgetpass:
                phone = edtPhoneForgetpass.getText().toString();
                if (!StringUtils.isPhoneNumberValid(phone)) {
                    showToast(getString(R.string.hint_phone_login));
                    return;
                }
//                String oldPhone = (String) SharedPreferenceUtils.get(ForgetPassActivity.this, Const.User.PHONE, "");
//                if (userToken != 0) {
//                    if (!StringUtils.isEmpty(oldPhone)) {
//                        if (!oldPhone.equals(phone)) {
//                            showToast("请输入绑定的手机号");
//                            return;
//                        }
//                    }
//                }
                virifyCountDownTimer =
                        new VirifyCountDownTimer(tvGetcodeForgetpass, 60000, 1000);
                getCodeCall(phone);

                break;
            case R.id.iv_show_forgetpass:
                if (isShowPass) {
                    isShowPass = false;
                    //否则隐藏密码
                    edtNewpassForgetpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivShowForgetpass.setSelected(false);
                    edtNewpassForgetpass.setSelection(edtNewpassForgetpass.getText().toString().length());
                } else {
                    isShowPass = true;
                    //如果选中，显示密码
                    edtNewpassForgetpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShowForgetpass.setSelected(true);
                    edtNewpassForgetpass.setSelection(edtNewpassForgetpass.getText().toString().length());
                }
                break;
            case R.id.btn_sure:
                validator.validate();
                break;
        }
    }

    private void getCodeCall(String phone) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phone);
        map.put("type", String.valueOf(Const.IntShow.TWO));
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
