package com.jingtaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 账户安全页面
 */
public class SafetyActivity extends MyBaseActivity {
    @BindView(R.id.rl_bindphone_safety)
    RelativeLayout rlBindphoneSafety;
    @BindView(R.id.tv_phone_safety)
    TextView tvPhoneSafety;
    @BindView(R.id.rl_changepass_safety)
    RelativeLayout rlChangepassSafety;

    boolean isHavePhone;
    @BindView(R.id.rl_paypass_safety)
    RelativeLayout rlPaypassSafety;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_safety);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_safety_setting);
    }

    @Override
    public void setResume() {
        String phone = (String) SharedPreferenceUtils.get(this, Const.User.PHONE, "");
        if (StringUtils.isEmpty(phone)) {
            rlChangepassSafety.setVisibility(View.GONE);
            isHavePhone = false;
        } else {
            isHavePhone = true;
            rlChangepassSafety.setVisibility(View.VISIBLE);
            tvPhoneSafety.setText(StringUtils.hidePhoneNumber(phone));
        }
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


    @OnClick({R.id.rl_bindphone_safety, R.id.rl_changepass_safety, R.id.rl_paypass_safety})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_bindphone_safety:
                if (isHavePhone) {
                    ActivityCollector.getActivityCollector().toOtherActivity(ChangePhoneActivity.class);
                } else {
                    ActivityCollector.getActivityCollector().toOtherActivity(BindPhoneActivity.class);
                }
                break;
            case R.id.rl_changepass_safety:
                ActivityCollector.getActivityCollector().toOtherActivity(ChangePassActivity.class);
                break;
            case R.id.rl_paypass_safety:
                String payPass = (String) SharedPreferenceUtils.get(this, Const.User.PAY_PASS, "");
                Bundle bundle = new Bundle();
                if (StringUtils.isEmpty(payPass)) {
                    bundle.putInt(Const.ShowIntent.TYPE, 1);
                } else {
                    bundle.putInt(Const.ShowIntent.TYPE, 0);
                }
                ActivityCollector.getActivityCollector().toOtherActivity(PayOneActivity.class, bundle);
                break;
        }
    }
}
