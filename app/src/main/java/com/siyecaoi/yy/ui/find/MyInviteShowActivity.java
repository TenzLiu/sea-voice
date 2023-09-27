package com.siyecaoi.yy.ui.find;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.InviteShowBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.MyUtils;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的邀请奖励
 */
public class MyInviteShowActivity extends MyBaseActivity {
    @BindView(R.id.tv_showmon_myinvite)
    TextView tvShowmonMyinvite;
    @BindView(R.id.tv_showtype_myinvite)
    TextView tvShowtypeMyinvite;
    @BindView(R.id.tv_draw_myinvite)
    TextView tvDrawMyinvite;
    @BindView(R.id.tv_number_myinvite)
    TextView tvNumberMyinvite;
    @BindView(R.id.tv_mon_myinvite)
    TextView tvMonMyinvite;
    @BindView(R.id.ll_inviteshow_myinvite)
    LinearLayout llInviteshowMyinvite;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.rl_number_myinvite)
    RelativeLayout rlNumberMyinvite;
    @BindView(R.id.rl_mon_myinvite)
    RelativeLayout rlMonMyinvite;
    @BindView(R.id.con_invite_myinvite)
    RelativeLayout conInviteMyinvite;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.con_show_myinvite)
    RelativeLayout conShowMyinvite;

    String moneyShow;

    @Override
    public void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_myinviteshow);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
        setTitleText(R.string.title_myinviteshow);
        setRightImg(getDrawable(R.drawable.share));

        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        getCall();
    }

    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.invite, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                InviteShowBean inviteShowBean = JSON.parseObject(responseString, InviteShowBean.class);
                if (inviteShowBean.getCode() == Api.SUCCESS) {
                    initShow(inviteShowBean.getData());
                } else {
                    showToast(inviteShowBean.getMsg());
                }
            }
        });
    }

    private void initShow(InviteShowBean.DataEntity data) {
        moneyShow = MyUtils.getInstans().doubleTwo(data.getMoney());
        tvShowmonMyinvite.setText(moneyShow);
        tvShowtypeMyinvite.setText(String.format(getString(R.string.tv_type_myinviteshow), (int) data.getTxMoney()));
        tvNumberMyinvite.setText(String.valueOf(data.getManNum()));
        tvMonMyinvite.setText(MyUtils.getInstans().doubleTwo(data.getFcMoney()));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101) {
            getCall();
        }
    }

    @OnClick({R.id.tv_draw_myinvite, R.id.ll_inviteshow_myinvite, R.id.con_invite_myinvite,
            R.id.con_show_myinvite, R.id.rl_number_myinvite, R.id.rl_mon_myinvite})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_draw_myinvite:
                bundle.putString(Const.ShowIntent.MONEY, moneyShow);
                ActivityCollector.getActivityCollector().toOtherActivity(FindDrawActivity.class, bundle, 101);
                break;
            case R.id.rl_number_myinvite:
                ActivityCollector.getActivityCollector().toOtherActivity(MyInviteNumberActivity.class);
                break;
            case R.id.rl_mon_myinvite:
                ActivityCollector.getActivityCollector().toOtherActivity(MyInviteAwardActivity.class);
                break;
            case R.id.ll_inviteshow_myinvite:
                ActivityCollector.getActivityCollector().toOtherActivity(MyinviteRankingActivity.class);
                break;
            case R.id.con_invite_myinvite:
                bundle.putInt(Const.ShowIntent.TYPE, 3);
                bundle.putString(Const.ShowIntent.TITLE, "邀请大作战");
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.con_show_myinvite:
                bundle.putInt(Const.ShowIntent.TYPE, 4);
                bundle.putString(Const.ShowIntent.TITLE, "奖励秘籍");
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
        }
    }
}
