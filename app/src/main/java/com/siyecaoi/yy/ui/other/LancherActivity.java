package com.siyecaoi.yy.ui.other;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;

import com.siyecaoi.yy.bean.VersionBean;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.LogUtils;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.MyUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;


import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Utils;
import io.reactivex.functions.Consumer;

/**
 * 启动页面
 * Created by Administrator on 2018/10/10.
 */

public class LancherActivity extends MyBaseActivity {
    MyHandler myHandler;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_lancher);
    }

    @Override
    public void initView() {
        showHeader(false);
        showTitle(false);
        setRxpermiss();
    }

    @SuppressLint("CheckResult")
    private void setRxpermiss() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            myHandler = new MyHandler(LancherActivity.this);
                            myHandler.sendEmptyMessageDelayed(0, 2000);
                        } else {
                            showMyDialog();
                        }
                    }
                });
    }

    private void showMyDialog() {
        MyDialog myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("请在应用权限页面开启相关权限");
        myDialog.setLeftButton("退出应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
            }
        });
        myDialog.setRightButton("前往设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                MyUtils.getInstans().gotoAppDetailIntent(LancherActivity.this, 101);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                toNextActivity();
                getCall();
            }
        }
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", Const.IntShow.ONE);
        HttpManager.getInstance().post(Api.getVersions, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                VersionBean versionBean = new Gson().fromJson(responseString, VersionBean.class);
                String appVersion = Utils.getAppVersion(LancherActivity.this);
                if (versionBean.getCode() == 0) {
                    if (versionBean.getData().getVersions().equals(appVersion)) {
                        toNextActivity();
                    } else {
                        showVersionDialog(versionBean.getData());
                    }
                } else {
                    toNextActivity();
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                showVersionDialog("连接失败，请重试");
            }
        });
    }

    MyDialog myDialog;

    private void showVersionDialog(VersionBean.DataEntity data) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setCancelable(false);
        if (data.getState() == 1) {
            myDialog.setHintText("检测到有最新版本是否更新");
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
                MyUtils.getInstans().toWebView(LancherActivity.this, data.getUrl());
            }
        });
        myDialog.setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                if (data.getState() == 1) {
                    toNextActivity();
                } else if (data.getState() == 2) { //强制更新
                    ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
                }
            }
        });
    }

    private void showVersionDialog(String msgShow) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setHintText(msgShow);
        myDialog.setLeftButton("退出应用", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
            }
        });

        myDialog.setRightButton("重新获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                getCall();
            }
        });
    }

    private void toNextActivity() {
        String uuid = (String) SharedPreferenceUtils.get(this, Const.User.UUID, "");
        if (StringUtils.isEmpty(uuid)) {
            SharedPreferenceUtils.put(this, Const.User.UUID, MyUtils.getInstans().getUuid(this));
        }
        if (userToken <= 0) {
            toLogin();
        } else {
            String userSig = (String) SharedPreferenceUtils.get(this, Const.User.USER_SIG, "");
            onRecvUserSig(String.valueOf(userToken), userSig);
        }
    }

    private void toMain() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Const.ShowIntent.STATE, true);
        ActivityCollector.getActivityCollector().toOtherActivity(AdvertActivity.class, bundle);
        ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
    }

    private void toLogin() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Const.ShowIntent.STATE, false);
        ActivityCollector.getActivityCollector().toOtherActivity(AdvertActivity.class, bundle);
        ActivityCollector.getActivityCollector().finishActivity(LancherActivity.class);
    }

    private void onRecvUserSig(String userId, String userSig) {
//        showDialog();
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM 登录成功后的回调操作，一般为跳转到应用的主页（这里的主页内容为下面章节的会话列表）
                 */
                LogUtils.e(LogUtils.TAG, "登录腾讯云成功");
                dismissDialog();
                toMain();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e(LogUtils.TAG, errCode + "登录腾讯云失败" + errMsg);
                dismissDialog();
                if (errCode == 6208) { //被踢
                    showToast("您已在其他设备登录，请重新登录");
                } else if (errCode == 70001) { //用户票据过期
                    showToast("登录授权过时，请重新登录");
                }
                toLogin();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            setRxpermiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
