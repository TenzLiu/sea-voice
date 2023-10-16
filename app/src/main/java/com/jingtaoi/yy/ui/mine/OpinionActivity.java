package com.jingtaoi.yy.ui.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ActivityCollector;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 意见反馈页面
 */
public class OpinionActivity extends MyBaseActivity {
    @BindView(R.id.edt_input_opinion)
    EditText edtInputOpinion;
    @BindView(R.id.tv_end_opinon)
    TextView tvEndOpinon;
    @BindView(R.id.edt_phone_opinion)
    EditText edtPhoneOpinion;

    @BindView(R.id.btn_Submit)
    Button btn_Submit;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_opinion);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_opinion_setting);


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateShow = edtInputOpinion.getText().toString();
                if (StringUtils.isEmpty(updateShow)) {
                    showToast("请输入反馈内容");
                } else {
                    getUpdateCall(updateShow);
                }
            }
        });

        edtInputOpinion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                String intputShow = edtInputOpinion.getText().toString();
                int endNumber = 200 - intputShow.length();
                tvEndOpinon.setText(String.valueOf(endNumber));
            }
        });
    }

    private void getUpdateCall(String updateShow) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("content", updateShow);
        String phoneShow = edtPhoneOpinion.getText().toString();
        if (!StringUtils.isEmpty(phoneShow)) {
            boolean phoneNumberValid = StringUtils.isPhoneNumberValid(phoneShow);
            if (phoneNumberValid) {
                map.put("phone", phoneShow);
            } else {
                showToast("请输入正确的电话号码");
                return;
            }
        }
        HttpManager.getInstance().post(Api.FeedbackSave, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("反馈成功");
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
}
