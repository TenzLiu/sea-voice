package com.siyecaoi.yy.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.agora.YySignaling;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.bean.VersionBean;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.dialog.MyHintDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.login.LoginActivity;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.MyUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Utils;
import io.reactivex.functions.Consumer;

public class SettingActivity extends MyBaseActivity {

    @BindView(R.id.rl_black_setting)
    RelativeLayout rlBlackSetting;
    @BindView(R.id.rl_scan_setting)
    RelativeLayout rlScanSetting;
    @BindView(R.id.rl_opinion_setting)
    RelativeLayout rlOpinionSetting;
    @BindView(R.id.rl_service_setting)
    RelativeLayout rlServiceSetting;
    @BindView(R.id.rl_help_setting)
    RelativeLayout rlHelpSetting;
    @BindView(R.id.rl_forus_setting)
    RelativeLayout rlForusSetting;
    @BindView(R.id.tv_version_setting)
    TextView tvVersionSetting;
    @BindView(R.id.rl_version_setting)
    RelativeLayout rlVersionSetting;

    @BindView(R.id.rl_bindphone_safety)
    RelativeLayout rlBindphoneSafety;
    @BindView(R.id.tv_phone_safety)
    TextView tvPhoneSafety;
    @BindView(R.id.rl_changepass_safety)
    RelativeLayout rlChangepassSafety;

    boolean isHavePhone;
    @BindView(R.id.rl_paypass_safety)
    RelativeLayout rlPaypassSafety;
    @BindView(R.id.iv_fangDingHao)
    ImageView ivFangDingHao;

    private int mIsFangDingHao = 1;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {

        setTitleText(R.string.tv_setting_mine);

        tvVersionSetting.setText("V" + Utils.getAppVersion(this));
        mIsFangDingHao = (int) SharedPreferenceUtils.get(this, Const.User.IS_FANG_DING_HAO, 1);
        switchFangDingHao();
    }

    private void switchFangDingHao() {
        if (mIsFangDingHao == 1) {
            ivFangDingHao.setSelected(false);
        } else if (mIsFangDingHao == 2) {        //开启防顶号
            ivFangDingHao.setSelected(true);
        }
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

    @OnClick({R.id.rl_black_setting, R.id.rl_scan_setting,
            R.id.rl_opinion_setting, R.id.rl_service_setting, R.id.rl_help_setting,
            R.id.rl_forus_setting, R.id.rl_version_setting, R.id.rl_exit_setting,
            R.id.rl_bindphone_safety,
            R.id.rl_changepass_safety, R.id.rl_paypass_safety, R.id.iv_fangDingHao})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
//        Intent intent = new Intent();
        switch (view.getId()) {
//            case R.id.rl_safety_setting:
//                ActivityCollector.getActivityCollector().toOtherActivity(SafetyActivity.class);
//                break;
            case R.id.rl_black_setting:
                ActivityCollector.getActivityCollector().toOtherActivity(BlackUserActivity.class);
                break;
            case R.id.rl_scan_setting:
                openErcode();
                break;
            case R.id.rl_opinion_setting:
                ActivityCollector.getActivityCollector().toOtherActivity(OpinionActivity.class);
                break;
            case R.id.rl_service_setting:
                bundle.putInt(Const.ShowIntent.TYPE, 16);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.tv_service_setting));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.rl_help_setting:
                bundle.putInt(Const.ShowIntent.TYPE, 14);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.tv_help_setting));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.rl_forus_setting:
                bundle.putInt(Const.ShowIntent.TYPE, 14);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.tv_forus_setting));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.rl_version_setting:
                getVersionCall();
                break;
            case R.id.rl_exit_setting:
                showMyDialog();
                break;

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
                Bundle bundle1 = new Bundle();
                if (StringUtils.isEmpty(payPass)) {
                    bundle1.putInt(Const.ShowIntent.TYPE, 1);
                } else {
                    bundle1.putInt(Const.ShowIntent.TYPE, 0);
                }
                ActivityCollector.getActivityCollector().toOtherActivity(PayOneActivity.class, bundle1);
                break;
            case R.id.iv_fangDingHao:

                if (mIsFangDingHao == 1) {
                    switchFangDinghao(2);
                } else {
                    switchFangDinghao(1);
                }
                break;
        }
    }

    private void getVersionCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", Const.IntShow.ONE);
        HttpManager.getInstance().post(Api.getVersions, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                VersionBean versionBean = JSON.parseObject(responseString, VersionBean.class);
                String appVersion = Utils.getAppVersion(SettingActivity.this);
                if (versionBean.getData().getVersions().equals(appVersion)) {
                    showNoVersionDialog();
                } else {
                    showVersionDialog(versionBean.getData());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
    }

    private void switchFangDinghao(int isFangDingHao) {
        int userToken = (int) SharedPreferenceUtils.get(this, Const.User.USER_TOKEN, 0);
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("isFangDingHao", isFangDingHao);
        HttpManager.getInstance().post(Api.setFangDingHao, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    mIsFangDingHao = isFangDingHao;
                    SharedPreferenceUtils.put(SettingActivity.this, Const.User.IS_FANG_DING_HAO, isFangDingHao);
                    switchFangDingHao();
                }

            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                showToast(msg);
            }
        });
    }

    private void showNoVersionDialog() {
        if (isFinishing()){
            return;
        }
        if (myHintDialog != null && myHintDialog.isShowing()) {
            myHintDialog.dismiss();
        }
        myHintDialog = new MyHintDialog(this);
        myHintDialog.show();
        myHintDialog.setHintText("当前已经是最新版本！");
    }

    private void showVersionDialog(VersionBean.DataEntity data) {
        if (isFinishing()) {
            return;
        }
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setCancelable(false);
        if (data.getState() == 1) {
            myDialog.setHintText("检测到有最新版本的版本是否更新");
        } else if (data.getState() == 2) {
            myDialog.setHintText("检测到有最新版本请前往更新");
            myDialog.setLeftText("退出应用");
        }
        myDialog.setRightButton("前往更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                MyUtils.getInstans().toWebView(SettingActivity.this, data.getUrl());
            }
        });
        myDialog.setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
            }
        });
    }


    @SuppressLint("CheckResult")
    private void openErcode() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ActivityCollector.getActivityCollector().toOtherActivity(ScanActivity.class);
                        } else {
                            showToast("请在应用权限页面开启相机权限");
                        }
                    }
                });
    }

    MyDialog myDialog;
    MyHintDialog myHintDialog;

    private void showMyDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(SettingActivity.this);
        myDialog.show();
        myDialog.setHintText("确定要退出登录吗？");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                logout();
            }
        });
    }

    private void logout() {
        showDialog();
        //登出
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {

                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                LogUtils.d(LogUtils.TAG, "logout failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess() {
                //登出成功
            }
        });
        setExit();
    }

    private void setExit() {
        MyApplication.getInstance().getWorkerThread().musicStop();
        MyApplication.getInstance().getWorkerThread().leaveChannel(Const.RoomId);
        Const.RoomId = "";
        YySignaling.getInstans().logout();//退出声网信令系统
//        MyApplication.getInstance().getWorkerThread().exit();//退出声网直播系统
        dismissDialog();
        showToast("退出登录成功");
        SharedPreferenceUtils.clear(this);
        SharedPreferenceUtils.put(this, Const.User.UUID, MyUtils.getInstans().getUuid(this));
        ActivityCollector.getActivityCollector().toOtherActivity(LoginActivity.class);
        ActivityCollector.getActivityCollector().finishAllBaseActivity();
    }
}
