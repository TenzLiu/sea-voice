package com.siyecaoi.yy.ui.find;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.DrawBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.BindAlipayActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;

public class FindDrawActivity extends MyBaseActivity {
    @BindView(R.id.tv_bind_draw)
    TextView tvBindDraw;
    @BindView(R.id.tv_alipayaccount_draw)
    TextView tvAlipayaccountDraw;
    @BindView(R.id.tv_alipayname_draw)
    TextView tvAlipaynameDraw;
    @BindView(R.id.rl_alipay_draw)
    RelativeLayout rlAlipayDraw;
    @BindView(R.id.tv_show_draw)
    TextView tvShowDraw;
    @BindView(R.id.tv_showmon_draw)
    TextView tvShowmonDraw;
    @BindView(R.id.edt_mon_draw)
    EditText edtMonDraw;
    @BindView(R.id.btn_draw)
    Button btnDraw;

    String moneyShow;

    private String trueName;
    private String payAccount;

    @Override
    public void initData() {
        moneyShow = getBundleString(Const.ShowIntent.MONEY);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_finddraw);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_draw_get);

    }

    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.SetWithdrawServiceList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                DrawBean drawBean = JSON.parseObject(responseString, DrawBean.class);
                if (drawBean.getCode() == Api.SUCCESS) {
                    setData(drawBean.getData());
                } else {
                    showToast(drawBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(DrawBean.DataEntity data) {
        if (data.getState() == 1) {//没有支付宝
            btnDraw.setAlpha(0.5f);
            btnDraw.setClickable(false);
        } else if (data.getState() == 2) {
            btnDraw.setAlpha(1.0f);
            btnDraw.setClickable(true);
            tvBindDraw.setVisibility(View.INVISIBLE);
            trueName = data.getTrueName();
            payAccount = data.getPayAccount();
            tvAlipayaccountDraw.setText("支付宝账号：" + payAccount);
            tvAlipaynameDraw.setText("真实姓名：" + trueName);
//            tvAlipayaccountDraw.setText("支付宝账号：" + data.getPayAccount());
//            tvAlipaynameDraw.setText("真实姓名：" + data.getTrueName());
        }
        tvShowmonDraw.setText("最高可提现" + moneyShow + "元");
//        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
//        stringBuilder.append(getString(R.string.tv_show_draw));
//        int len = stringBuilder.length();
//        stringBuilder.append("  钻石余额：");
//        stringBuilder.append(data.getYnum() + "");
//        if (data.getYnum() == 0) {
//            btnDraw.setAlpha(0.5f);
//        }
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FF003F));
//        stringBuilder.setSpan(colorSpan, len,
//                stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(24);
//        stringBuilder.setSpan(absoluteSizeSpan, len, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvShowDraw.setText(stringBuilder);
        SharedPreferenceUtils.put(this, Const.User.GOLD, data.getGold());
        SharedPreferenceUtils.put(this, Const.User.Ynum, data.getYnum());
    }

    @Override
    public void setResume() {
        getCall();
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

    @OnClick({R.id.rl_alipay_draw, R.id.btn_draw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_alipay_draw:
                Bundle bundle = new Bundle();
                bundle.putString("trueName", trueName);
                bundle.putString("payAccount", payAccount);
                ActivityCollector.getActivityCollector().toOtherActivity(BindAlipayActivity.class, bundle);
                break;
            case R.id.btn_draw:
                String updateMon = edtMonDraw.getText().toString();
                if (StringUtils.isEmpty(updateMon)) {
                    showToast("请输入提现金额");
                    return;
                } else {
                    getUpdateCall(updateMon);
                }
                break;
        }
    }

    private void getUpdateCall(String updateMon) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("money", updateMon);
        HttpManager.getInstance().post(Api.AddSetWithdraw, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("提现申请成功,请耐心等待审核");
                    setResult(RESULT_OK);
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }
}
