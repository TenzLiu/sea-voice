package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.utils.ActivityCollector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账单页面
 */
public class BillActivity extends MyBaseActivity {
    @BindView(R.id.rl_gift_bill)
    RelativeLayout rlGiftBill;
    @BindView(R.id.tv_tupup_bill)
    TextView tvTupupBill;
    @BindView(R.id.tv_draw_bill)
    TextView tvDrawBill;
    @BindView(R.id.tv_share_bill)
    TextView tvShareBill;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_bill);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_bill_wallet);
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

    @OnClick({R.id.rl_gift_bill, R.id.tv_tupup_bill, R.id.tv_draw_bill, R.id.tv_share_bill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_gift_bill:
                ActivityCollector.getActivityCollector().toOtherActivity(GiftHisActivity.class);
                break;
            case R.id.tv_tupup_bill:
                ActivityCollector.getActivityCollector().toOtherActivity(TopupHistoryActivity.class);
                break;
            case R.id.tv_draw_bill:
                ActivityCollector.getActivityCollector().toOtherActivity(DrawHisActivity.class);
                break;
            case R.id.tv_share_bill:
                ActivityCollector.getActivityCollector().toOtherActivity(ShareHisActivity.class);
                break;
        }
    }
}
