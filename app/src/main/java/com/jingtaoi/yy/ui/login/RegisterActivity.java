package com.jingtaoi.yy.ui.login;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.ui.other.WebActivity;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
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

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;

import static com.mobsandgeeks.saripaar.annotation.Password.Scheme.ALPHA;

/**
 * 注册页面
 */
public class RegisterActivity extends MyBaseActivity {
    @BindView(R.id.edt_phone_register)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_phone_login)
    @IsPhoneNumber(sequence = 2)
    @Order(1)
    EditText edtPhoneRegister;
    @BindView(R.id.tv_getcode_register)
    TextView tvGetcodeRegister;
    @BindView(R.id.edt_psd_register)
    @NotEmpty(message = "请输入验证码")
    @Order(2)
    EditText edtPsdRegister;
    @BindView(R.id.edt_newpass_register)
    @NotEmpty(message = "请输入密码")
    @Password(messageResId = R.string.hint_psdall_register, scheme = ALPHA)
    @Order(3)
    EditText edtNewpassRegister;
    @BindView(R.id.iv_show_register)
    ImageView ivShowRegister;
    @BindView(R.id.iv_check_register)
    ImageView ivCheckRegister;
    @BindView(R.id.tv_agreement_register)
    TextView tvAgreementRegister;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @BindView(R.id.back_iv)
    ImageView back_iv;

    Validator validator;

    String phone;
    VirifyCountDownTimer virifyCountDownTimer;
    boolean isSureRegister;
    boolean isShowPass;
    String cityName;

    @Override
    public void initData() {
        cityName = getBundleString(Const.ShowIntent.NAME);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initView() {

        validator = new Validator(this);
        validator.setValidationListener(validationListener);
        Validator.registerAnnotation(IsPhoneNumber.class);
        isSureRegister = true;
        ivCheckRegister.setSelected(true);

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
            if (isSureRegister) {
                getCall();
            } else {
                showToast("请先同意" + getString(R.string.hint_checktwo_register));
            }
        }

        @Override //有误
        public void onValidationFailed(List<ValidationError> errors) {
            for (ValidationError error : errors) {
                View view = error.getView();
                showToast(error.getFailedRules().get(0).getMessage(RegisterActivity.this));
            }
        }
    };

    private void getCall() {
        phone = edtPhoneRegister.getText().toString();
        String code = edtPsdRegister.getText().toString();
        String password = edtNewpassRegister.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.PHONE, phone);
        bundle.putString(Const.ShowIntent.CODE, code);
        bundle.putString(Const.ShowIntent.PASS, password);
        bundle.putString(Const.ShowIntent.NAME,cityName);

        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phone);
        map.put("smsCode", code);
        HttpManager.getInstance().post(Api.isCode, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Const.ShowIntent.PHONE, phone);
                    bundle.putString(Const.ShowIntent.CODE, code);
                    bundle.putString(Const.ShowIntent.PASS, password);
                    bundle.putString(Const.ShowIntent.NAME,cityName);
                    ActivityCollector.getActivityCollector().toOtherActivity(DataInputActivity.class, bundle);
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

    @OnClick({R.id.tv_getcode_register, R.id.iv_show_register, R.id.iv_check_register,
            R.id.tv_agreement_register, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getcode_register:
                phone = edtPhoneRegister.getText().toString();
                if (!StringUtils.isPhoneNumberValid(phone)) {
                    showToast(getString(R.string.hint_phone_login));
                    return;
                }
                virifyCountDownTimer =
                        new VirifyCountDownTimer(tvGetcodeRegister, 60000, 1000);
                getCodeCall(phone);
                break;
            case R.id.iv_show_register:
                if (isShowPass) {
                    isShowPass = false;
                    //否则隐藏密码
                    edtNewpassRegister.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivShowRegister.setSelected(false);
                    edtNewpassRegister.setSelection(edtNewpassRegister.getText().toString().length());
                } else {
                    isShowPass = true;
                    //如果选中，显示密码
                    edtNewpassRegister.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShowRegister.setSelected(true);
                    edtNewpassRegister.setSelection(edtNewpassRegister.getText().toString().length());
                }
                break;
            case R.id.iv_check_register:
                if (isSureRegister) {
                    isSureRegister = false;
                    ivCheckRegister.setSelected(false);
                } else {
                    isSureRegister = true;
                    ivCheckRegister.setSelected(true);
                }
                break;
            case R.id.tv_agreement_register:
//                getHtmlCall(Const.IntShow.ONE);
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.TYPE, 9);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.hint_checktwo_register));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.btn_sure:
//                ActivityCollector.getActivityCollector().toOtherActivity(DataInputActivity.class);
                validator.validate();
                break;
        }
    }

//    private void getHtmlCall(final int type) {
//        showDialog();
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        map.put("type", String.valueOf(type));
//        HttpManager.getInstance().post(Api.getHtml, map, new MyObserver(this) {
//            @Override
//            public void success(String responseString) {
//                HtmlBean htmlBean = new Gson().fromJson(responseString, HtmlBean.class);
//                if (htmlBean.getCode() == Const.IntShow.SUCCESS) {
//                    Intent intent = new Intent();
//                    if (type == 1) {
//                        intent.putExtra(Const.ShowIntent.TYPE, 4);
//                        intent.putExtra(Const.ShowIntent.URL, htmlBean.getData().getContent());
//                        ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, intent);
//                    }
//                } else {
//                    showToast(htmlBean.getMsg());
//                }
//            }
//        });
//    }

    private void getCodeCall(String phone) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phone);
        map.put("type", String.valueOf(Const.IntShow.ONE));
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
