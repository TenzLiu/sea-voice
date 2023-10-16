package com.jingtaoi.yy.ui.other;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.WebBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.google.gson.Gson;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * webview页面
 * Created by Administrator on 2018/1/18.
 */

public class WebActivity extends MyBaseActivity {

    int type;
    String url;
    boolean isUrl;//是否可直接访问网址
    String title;
    int state;
//    @BindView(R.id.webView)
//    WebView webView;
//    @BindView(R.id.ll_loading)
//    LinearLayout llLoading;
//    @BindView(R.id.tv_show_forus)
//    TextView tvShowForus;
    @BindView(R.id.lay_webView)
    LinearLayout lay_webView;

    private AgentWeb mAgentWeb;

    @Nullable
    @Override
    public void initData() {
        isUrl = getBundleBoolean(Const.ShowIntent.STATE, true);
        type = getBundleInt(Const.ShowIntent.TYPE, 0);
        url = getBundleString(Const.ShowIntent.URL);
        title = getBundleString(Const.ShowIntent.TITLE);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_webview);
    }

    @Override
    public void initView() {
        setTitleText(title);


//        if (type == 15) {
//            tvShowForus.setVisibility(View.VISIBLE);
//        }

        if (isUrl) {
            if (StringUtils.isEmpty(url)) {
                getUrlCall();
//                showToast("无效的网络链接");
//                ActivityCollector.getActivityCollector().finishActivity();
            } else {
                setWebview();
            }
        } else {
            if (type == 0) {
                setWebview();
            } else if (type == 1) {
//                getUrlCall();
            }

        }


    }

    private void getUrlCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("type", type);
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

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(lay_webView, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(-1, 3)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
                .interceptUnkownUrl()
                .createAgentWeb()
                .ready()
                .go(url);
//        mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
//        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小


//        WebSettings settings = webView.getSettings();
//        settings.setSavePassword(false);
//        settings.setJavaScriptEnabled(false);
//        settings.setDomStorageEnabled(true);
//        settings.setDefaultTextEncodingName("utf-8");
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                setTitle(title);
//            }
//
//        });
//
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                if (isDestroy) {
//                    return;
//                }
//                llLoading.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                llLoading.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Utils.systemErr(url);
//                if (!TextUtils.isEmpty(url)) {
//                    webView.loadUrl(url);
//                }
//                return true;
//            }
//        });
//        if (isUrl) {
//            webView.loadUrl(url);
//        } else {
//            webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
//
//        }
    }


    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    protected boolean resetTitle() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAgentWeb.clearWebCache();
        mAgentWeb.destroy();

        //webView回收,部分手机无效
//        if (webView != null) {
//            webView.stopLoading();
//            webView.removeAllViews();
//            webView.destroy();
//        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
