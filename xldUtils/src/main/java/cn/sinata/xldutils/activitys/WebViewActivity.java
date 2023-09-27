package cn.sinata.xldutils.activitys;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.utils.Utils;

/**
 * 网页加载
 */
public class WebViewActivity extends TitleActivity {

    private WebView webView;
    private View loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initView() {
        webView = bind(R.id.webView);
        loadingView = bind(R.id.ll_loading);

        String url = getIntent().getStringExtra("url");
        if (url == null) {
            url = "";
        }
        WebSettings settings = webView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (resetTitle()) {
                    setTitle(title);
                }
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isDestroy) {
                    return;
                }
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingView.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Utils.systemErr(url);
                if (!TextUtils.isEmpty(url)) {
                    webView.loadUrl(url);
                }
                return true;
            }
        });

        webView.loadUrl(url);
    }

    protected boolean resetTitle() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //webView回收,部分手机无效
        if (webView != null) {
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
        }
    }
}
