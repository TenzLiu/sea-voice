package com.siyecaoi.yy.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.bean.JpushModel;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.ui.MainActivity;
import com.siyecaoi.yy.ui.mine.PersonHomeActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;
import cn.sinata.xldutils.utils.Toast;

/**
 * Created by Administrator on 2018/12/8.
 */

public class MyTalkReceiver extends BroadcastReceiver {

    String TAG = "jpush";

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = "";
        Bundle bundle = intent.getExtras();
        LogUtils.e(TAG, "onReceive - " + intent.getAction());

        final String extraMessage = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!TextUtils.isEmpty(extraMessage)) { //如果收到有附加参数
            LogUtils.e("extraMessage " + extraMessage);
            JpushModel jpushModel = new Gson().fromJson(extraMessage, JpushModel.class);
            state = jpushModel.getState();
            if (state == null) {
                return;
            }
            if (state.equals("6")) { //锁定用户
                MainActivity mainActivity = ActivityCollector.getActivity(MainActivity.class);
                boolean inService = (ActivityCollector.isActivityExist(MainActivity.class) && mainActivity != null);//判断程序是在运行中
                int userId = (int) SharedPreferenceUtils.get(context, Const.User.USER_TOKEN, 0);
                boolean isLogin = (userId != 0);//判断用户是否登录
                if (inService) {
                    if (isLogin) {
                        Toast.create(MyApplication.getInstance()).show("您的账号已锁定，请联系客服");
                        BroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(Const.BroadCast.EXIT);
                    }
                }
            } else if (state.equals("3")) { //赠送头环或座驾
                MainActivity mainActivity = ActivityCollector.getActivity(MainActivity.class);
                boolean inService = (ActivityCollector.isActivityExist(MainActivity.class) && mainActivity != null);//判断程序是在运行中
                int userId = (int) SharedPreferenceUtils.get(context, Const.User.USER_TOKEN, 0);
                boolean isLogin = (userId != 0);//判断用户是否登录
                if (inService) {
                    if (isLogin) {
                        LogUtils.e("收到头环或座驾");
                        showMyGetDialog(jpushModel.getName(), jpushModel.getSceneName(), ActivityCollector.getActivityCollector().currentActivity());
                    }
                }
            }
        }
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtils.d(TAG, "[MyReceiver] 接收 Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
//            BroadcastManager.getInstance(context).sendBroadcast(Const.BroadCast.NEW_MESSAGE);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.d(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为

        } else {
            LogUtils.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    MyDialog myDialog;

    private void showMyGetDialog(String name, String sceneName, AppCompatActivity appCompatActivity) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(appCompatActivity);
        myDialog.show();
        myDialog.setHintText("你的好友" + name + "给你赠送了" + sceneName + ",快去看看吧，点击就可以使用");
        myDialog.setRightButton("立即查看", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                int userToken = (int) SharedPreferenceUtils.get(appCompatActivity, Const.User.USER_TOKEN, 0);
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.ID, userToken);
                ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
            }
        });
    }
}
