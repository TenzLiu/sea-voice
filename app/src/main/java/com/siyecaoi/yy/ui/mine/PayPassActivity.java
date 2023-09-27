package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.Md5;

/**
 * 设置密码页面
 *
 * @author xha
 * @data 2019/9/27
 */
public class PayPassActivity extends MyBaseActivity {
    @BindView(R.id.edt_newpass_changepass)
    EditText edtNewpassChangepass;
    @BindView(R.id.edt_repass_changepass)
    EditText edtRepassChangepass;
    @BindView(R.id.btn_sure_changepass)
    Button btnSureChangepass;
    String smsCode;

    @Override
    public void initData() {
        smsCode = getBundleString("smsCode");
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_paypass);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_changepass_setting);
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

    @OnClick(R.id.btn_sure_changepass)
    public void onViewClicked() {
        checkInput();
    }

    private void checkInput() {
        String pass = edtNewpassChangepass.getText().toString();
        String rePass = edtRepassChangepass.getText().toString();
        if (pass.length() != 6) {
            showToast(getString(R.string.hint_pass_paypass));
            return;
        }
        if (rePass.length() != 6) {
            showToast(getString(R.string.hint_pass_paypass));
            return;
        }
        if (!pass.equals(rePass)) {
            showToast("两次输入不一致，请重新输入");
            return;
        }
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("smsCode", smsCode);
        map.put("password", Md5.getMd5Value(pass));
        HttpManager.getInstance().post(Api.PlayPassword, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = new Gson().fromJson(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("支付密码设置成功");
                    SharedPreferenceUtils.put(PayPassActivity.this, Const.User.PAY_PASS, Md5.getMd5Value(pass));
                    ActivityCollector.getActivityCollector().finishActivity(PayOneActivity.class);
                    finish();
                } else {
                    showToast(baseBean.getMsg());
                }

            }
        });
    }
}
