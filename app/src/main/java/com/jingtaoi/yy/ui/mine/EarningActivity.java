package com.jingtaoi.yy.ui.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的收益页面
 *
 * 20200917,删除不用合并到钱包界面
 */
public class EarningActivity extends MyBaseActivity {
    @BindView(R.id.tv_you_earning)
    TextView tvYouEarning;
    @BindView(R.id.tv_draw_earning)
    TextView tvDrawEarning;
    @BindView(R.id.btn_change_earning)
    Button btnChangeEarning;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_earning);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_get_wallet);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setResume() {
        String Ynum = (String) SharedPreferenceUtils.get(this, Const.User.Ynum, "0");
        tvYouEarning.setText(Ynum + "");
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

    @OnClick({R.id.tv_draw_earning, R.id.btn_change_earning})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_draw_earning:
                ActivityCollector.getActivityCollector().toOtherActivity(DrawActivity.class);
                break;
            case R.id.btn_change_earning:
                ActivityCollector.getActivityCollector().toOtherActivity(ExchangeYouActivity.class);
//                Intent intent = new Intent(this, ExchangeYouActivity.class);
//                startActivity(intent);
                break;
        }
    }
}
