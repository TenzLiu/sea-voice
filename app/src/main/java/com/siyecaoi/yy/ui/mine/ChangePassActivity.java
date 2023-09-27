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
import com.siyecaoi.yy.ui.login.ForgetPassActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
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

import static com.mobsandgeeks.saripaar.annotation.Password.Scheme.ALPHA;

/**
 * 修改密码
 */
public class ChangePassActivity extends MyBaseActivity {
    @BindView(R.id.edt_oldpass_changepass)
    @NotEmpty(message = "请输入原密码")
    @Password(messageResId = R.string.hint_psdall_register, scheme = ALPHA)
    @Order(1)
    EditText edtOldpassChangepass;
    @BindView(R.id.edt_newpass_changepass)
    @NotEmpty(message = "请输入新密码")
    @Password(messageResId = R.string.hint_psdall_register, scheme = ALPHA)
    @Order(2)
    EditText edtNewpassChangepass;
    @BindView(R.id.edt_repass_changepass)
    @NotEmpty(message = "请确认新密码")
    @Password(messageResId = R.string.hint_psdall_register, scheme = ALPHA)
    @Order(3)
    EditText edtRepassChangepass;
    @BindView(R.id.tv_forgetpass_changepass)
    TextView tvForgetpassChangepass;
    @BindView(R.id.btn_sure_changepass)
    Button btnSureChangepass;


    Validator validator;

    VirifyCountDownTimer virifyCountDownTimer;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_changepass);
    }

    @Override
    public void initView() {
        setTitleText(R.string.tv_changepass_setting);
        validator = new Validator(this);
        validator.setValidationListener(validationListener);

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
                showToast(error.getFailedRules().get(0).getMessage(ChangePassActivity.this));
            }
        }
    };

    private void getCall() {
        String oldPass = edtOldpassChangepass.getText().toString();
        String newPass = edtNewpassChangepass.getText().toString();
        String rePass = edtRepassChangepass.getText().toString();
        if (oldPass.equals(newPass)) {
            showToast("新密码不得与原密码相同");
            return;
        }
        if (!newPass.equals(rePass)) {
            showToast("新密码两次输入不一致");
            return;
        }
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("oldpassword", Md5.getMd5Value(oldPass));
        map.put("password", Md5.getMd5Value(newPass));
        HttpManager.getInstance().post(Api.ModPassword, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("密码修改成功");
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

    @OnClick({R.id.tv_forgetpass_changepass, R.id.btn_sure_changepass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetpass_changepass:
                ActivityCollector.getActivityCollector().toOtherActivity(ForgetPassActivity.class);
                break;
            case R.id.btn_sure_changepass:
                validator.validate();
                break;
        }
    }
}
