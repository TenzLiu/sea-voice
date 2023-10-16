package com.jingtaoi.yy.ui.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.dialog.ExchangeDialogFragment;
import com.jingtaoi.yy.ui.mine.fragment.RechargeGoldCoinsFragment;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包
 */
public class WalletActivity extends MyBaseActivity {
    @BindView(R.id.tv_topup_wallet)
    TextView tvTopupWallet;
    @BindView(R.id.tv_you_wallet)
    TextView tvYouWallet;
    @BindView(R.id.tv_you_earning)
    TextView tvYouEarning;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_wallet);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_wallet_mine);
        setRightText(R.string.tv_bill_wallet);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.getActivityCollector().toOtherActivity(NewMyBillActivity.class);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setResume() {
        int gold = (int) SharedPreferenceUtils.get(this, Const.User.GOLD, 0);
        tvYouWallet.setText(gold + "");

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

    @OnClick({R.id.tv_topup_wallet, R.id.tv_draw_earning, R.id.btn_change_earning,R.id.fjsr_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_topup_wallet:

                RechargeGoldCoinsFragment coinsFragment = new RechargeGoldCoinsFragment();
                coinsFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                coinsFragment.show(getSupportFragmentManager(), "coinsFragment");

//                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
                break;
            case R.id.tv_draw_earning:
                ActivityCollector.getActivityCollector().toOtherActivity(DrawActivity.class);
                break;
            case R.id.btn_change_earning:

                ExchangeDialogFragment exchangeDialog = new ExchangeDialogFragment();
                exchangeDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                exchangeDialog.show(getSupportFragmentManager(), "exchangeDialog");

//                ActivityCollector.getActivityCollector().toOtherActivity(ExchangeYouActivity.class);
                break;
            case R.id.fjsr_rl:
                ActivityCollector.getActivityCollector().toOtherActivity(RoomRevenueActivity.class);
                break;


        }
    }
}
