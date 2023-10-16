package com.jingtaoi.yy.ui.mine.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.GradeShowBean;
import com.jingtaoi.yy.bean.WebBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.mine.adapter.GradeShowListAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.google.gson.Gson;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.Utils;

/**
 * 财富等级说明(魅力等级说明)
 */
public class GradeShowActivity extends MyBaseActivity {
    @BindView(R.id.mWebview_gradeshow)
    WebView mWebviewGradeshow;
    @BindView(R.id.mRecyclerView_gradeshow)
    RecyclerView mRecyclerViewGradeshow;

    GradeShowListAdapter gradeShowListAdapter;
    String url;
    int type;
    @BindView(R.id.tv_showone_gradeshow)
    TextView tvShowoneGradeshow;
    @BindView(R.id.tv_showtwo_gradeshow)
    TextView tvShowtwoGradeshow;

    @Override
    public void initData() {
        type = getBundleInt(Const.ShowIntent.TYPE, 0);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_gradeshow);
    }

    @Override
    public void initView() {

        if (type == 1) {
            setTitleText(R.string.tv_cai_gradeshow);
        } else if (type == 2) {
            setTitleText(R.string.tv_mei_gradeshow);
            tvShowoneGradeshow.setText("累计魅力值");
            tvShowtwoGradeshow.setText("要求魅力值");
        }
        setRecycler();
        getWebCall();
        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", type);
        HttpManager.getInstance().post(Api.UserList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GradeShowBean gradeShowBean = JSON.parseObject(responseString, GradeShowBean.class);
                if (gradeShowBean.getCode() == Api.SUCCESS) {
                    gradeShowListAdapter.setNewData(gradeShowBean.getData());
                } else {
                    showToast(gradeShowBean.getMsg());
                }
            }
        });
    }

    private void getWebCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        if (type == 1) {
            map.put("type", 12);
        } else if (type == 2) {
            map.put("type", 13);
        }
        HttpManager.getInstance().post(Api.Protocol, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                WebBean webBean = new Gson().fromJson(responseString, WebBean.class);
                if (webBean.getCode() == Api.SUCCESS) {
                    url = webBean.getData().getContent();
                    setWebview();
                } else {
                    showToast(webBean.getMsg());
                    ActivityCollector.getActivityCollector().finishActivity();
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebview() {
        WebSettings settings = mWebviewGradeshow.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        mWebviewGradeshow.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }

        });

        mWebviewGradeshow.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isDestroy) {
                    return;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Utils.systemErr(url);
                if (!TextUtils.isEmpty(url)) {
                    mWebviewGradeshow.loadUrl(url);
                }
                return true;
            }
        });
        mWebviewGradeshow.loadUrl(url);
    }

    private void setRecycler() {
        gradeShowListAdapter = new GradeShowListAdapter(R.layout.item_gradeshow);
        gradeShowListAdapter.setType(type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewGradeshow.setLayoutManager(layoutManager);
        mRecyclerViewGradeshow.setAdapter(gradeShowListAdapter);
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
}
