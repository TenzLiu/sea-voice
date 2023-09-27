package com.siyecaoi.yy.ui.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.dialog.MyPaypassDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.DrawBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.adapter.DrawListAdapter;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 提现页面
 */
public class DrawActivity extends MyBaseActivity {
    @BindView(R.id.tv_bind_draw)
    TextView tvBindDraw;
    @BindView(R.id.tv_alipayaccount_draw)
    TextView tvAlipayaccountDraw;
    @BindView(R.id.tv_alipayname_draw)
    TextView tvAlipaynameDraw;
    @BindView(R.id.rl_alipay_draw)
    RelativeLayout rlAlipayDraw;
    @BindView(R.id.mRecyclerView_draw)
    RecyclerView mRecyclerViewDraw;
    @BindView(R.id.btn_draw)
    Button btnDraw;
    DrawListAdapter drawListAdapter;
    @BindView(R.id.tv_show_draw)
    TextView tvShowDraw;
    @BindView(R.id.tv_showmon_draw)
    TextView tvShowmonDraw;
    @BindView(R.id.edt_mon_draw)
    EditText edtMonDraw;
    int id;
    double yNum;
    boolean isHasAlipay;

    private String trueName;
    private String payAccount;
    private int zsNum;
    private int type;

    @Override
    public void initData() {
        type = getBundleInt("type",-1);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_draw);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_draw_get);
        setRightText(R.string.tv_gui_draw);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.TYPE, 11);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.tv_gui_draw));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
            }
        });

        setRecycler();
    }

    private void setRecycler() {
        drawListAdapter = new DrawListAdapter(R.layout.item_draw);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerViewDraw.setLayoutManager(layoutManager);
        mRecyclerViewDraw.setAdapter(drawListAdapter);

        drawListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DrawBean.DataEntity.WithdrawEntity withdrawEntity = (DrawBean.DataEntity.WithdrawEntity) adapter.getItem(position);
                drawListAdapter.setChooseOne(position);
                assert withdrawEntity != null;
                id = withdrawEntity.getId();
                zsNum = withdrawEntity.getY();
                if (yNum == 0 || id == 0 || !isHasAlipay) {
                    btnDraw.setAlpha(0.5f);
                    btnDraw.setClickable(false);
                } else {
                    btnDraw.setAlpha(1.0f);
                    btnDraw.setClickable(true);
                }
            }
        });
    }

    private void getCall() {
//        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", type);
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
        drawListAdapter.setNewData(data.getWithdraw());
        if (data.getWithdraw().size() > 0) {
            id = data.getWithdraw().get(0).getId();
        }
        if (data.getState() == 1) { //没有支付宝
            btnDraw.setAlpha(0.5f);
            isHasAlipay = false;
            btnDraw.setClickable(false);
        } else if (data.getState() == 2) {
            isHasAlipay = true;
            btnDraw.setAlpha(1.0f);
            btnDraw.setClickable(true);
            tvBindDraw.setVisibility(View.INVISIBLE);
            trueName = data.getTrueName();
            payAccount = data.getPayAccount();
            tvAlipayaccountDraw.setText("支付宝账号：" + payAccount);
            tvAlipaynameDraw.setText("真实姓名：" + trueName);
        }
//        tvShowmonDraw.setText("最高可提现" + "元");
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(getString(R.string.tv_show_draw));
        int len = stringBuilder.length();
        stringBuilder.append("  钻石余额：");
        stringBuilder.append(data.getYnum());
        yNum = Double.parseDouble(data.getYnum());
        SharedPreferenceUtils.put(this, Const.User.Ynum, data.getYnum());

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FF003F));
        stringBuilder.setSpan(colorSpan, len,
                stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(24);
        stringBuilder.setSpan(absoluteSizeSpan, len, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvShowDraw.setText(stringBuilder);
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

                if (id == 0) {
                    showToast("请选择提现金额");
                    return;
                }

                if(yNum<=0f){
                    showToast("您的钻石余额不足");
                    return;
                }
                if(yNum<zsNum){
                    showToast("您的钻石余额不足"+zsNum);
                    return;
                }


                String realPay = (String) SharedPreferenceUtils.get(this, Const.User.PAY_PASS, "");
                if (StringUtils.isEmpty(realPay)) {
                    showSetPassDialog();
                } else {
                    showPassDialog();
                }
//                getDrawCall();
                break;
        }
    }


    MyPaypassDialog myPaypassDialog;

    private void showPassDialog() {
        if (myPaypassDialog != null && myPaypassDialog.isShowing()) {
            myPaypassDialog.dismiss();
        }
        myPaypassDialog = new MyPaypassDialog(this);
        myPaypassDialog.show();
        myPaypassDialog.setPaySuccess(new MyPaypassDialog.PaySuccess() {
            @Override
            public void onSuccessShow() {
                if (myPaypassDialog != null && myPaypassDialog.isShowing()) {
                    myPaypassDialog.dismiss();
                }
                getDrawCall();
            }
        });
    }


    MyDialog myDialog;

    private void showSetPassDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("您还没有设置支付密码，请前往设置");
        myDialog.setRightText("去设置");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.TYPE, 1);
                ActivityCollector.getActivityCollector().toOtherActivity(PayOneActivity.class, bundle);
            }
        });
    }

    private void getDrawCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("id", id);
        map.put("type", type);
        HttpManager.getInstance().post(Api.AddSetWithdraws, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean userBean = JSON.parseObject(responseString, BaseBean.class);
                if (userBean.getCode() == 0) {
                    showToast("提现申请成功,请耐心等待审核");
                    getCall();
                }else {
                    showToast(userBean.getMsg());
                }

            }
        });
    }
}
