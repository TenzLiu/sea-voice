package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 个性签名页面
 */
public class NicknameActivity extends MyBaseActivity {


    @BindView(R.id.edt_signer)
    EditText edtSigner;
    String nickName;

    @Override
    public void initData() {
        nickName = getBundleString(Const.ShowIntent.NAME);
    }

    @Override
    public void setContentView() {

        setContentView(R.layout.activity_singer);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_data);
        setRightText(R.string.tv_save);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUpdateCall();
            }
        });

        edtSigner.setHint(getString(R.string.hint_nickname_data));
        edtSigner.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        if (!StringUtils.isEmpty(nickName)) {
            edtSigner.setText(nickName);
//            edtSigner.setSelection(nickName.length());
        }
    }

    private void getUpdateCall() {
        String nickName = edtSigner.getText().toString();
        if (StringUtils.isEmpty(nickName)) {
            showToast(getString(R.string.hint_nickname_data));
            return;
        }
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", userToken);
        map.put("nickname", nickName);
        HttpManager.getInstance().post(Api.updateUser, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    showToast(getString(R.string.tv_success_data));
                    SharedPreferenceUtils.put(NicknameActivity.this, Const.User.NICKNAME, nickName);
                    setResult(RESULT_OK);
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
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
}
