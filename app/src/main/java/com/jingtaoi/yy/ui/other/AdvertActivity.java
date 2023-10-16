package com.jingtaoi.yy.ui.other;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.AdvertBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.MainActivity;
import com.jingtaoi.yy.ui.login.LoginActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageUtils;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/12/27.
 * 广告页面
 */

public class AdvertActivity extends MyBaseActivity {
    @BindView(R.id.iv_show)
    ImageView ivShow;
    @BindView(R.id.tv_jump_advert)
    TextView tvJumpAdvert;
    //    int timeShow;
    Timer timer;

    int urlType;
    String urlHtml;

    boolean isToMain;

    @Override
    public void initData() {
//        timeShow = 3;
        isToMain = getBundleBoolean(Const.ShowIntent.STATE, false);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_advert);
    }

    @Override
    public void initView() {

        showTitle(false);
        showHeader(false);

        getCall();
        timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
//                timeShow--;
                Message message = new Message();
//                message.obj = timeShow;
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 3000);
    }


    private void getCall() {
        HttpManager.getInstance().post(Api.Advertising, new HashMap<>(), new MyObserver(this) {
            @Override
            public void success(String responseString) {
                AdvertBean advertBean = new Gson().fromJson(responseString, AdvertBean.class);
                if (advertBean.getCode() == 0) {
                    String imgUrl = advertBean.getData().getImgUrl();
                    ImageUtils.loadImage(ivShow, imgUrl,0,-1);
                    urlType = advertBean.getData().getUrlType();
                    urlHtml = advertBean.getData().getUrlHtml();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    toNext();
                    break;
            }
        }
    };

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private void toNext() {
        if (isToMain) {
            ActivityCollector.getActivityCollector().toOtherActivity(MainActivity.class);
        } else {
            ActivityCollector.getActivityCollector().toOtherActivity(LoginActivity.class);
        }
        ActivityCollector.getActivityCollector().finishActivity(AdvertActivity.class);
    }

    @OnClick({R.id.iv_show, R.id.tv_jump_advert})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_show:
                if (isToMain && urlType != 1) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt(Const.ShowIntent.URLTYPE, urlType);
                    bundle.putString(Const.ShowIntent.URL, urlHtml);
                    ActivityCollector.getActivityCollector().toOtherActivity(MainActivity.class, bundle);
                    ActivityCollector.getActivityCollector().finishActivity(AdvertActivity.class);
                }
                break;
            case R.id.tv_jump_advert:
                if (timer != null) {
                    timer.cancel();
                }
                toNext();
                break;
        }
    }
}
