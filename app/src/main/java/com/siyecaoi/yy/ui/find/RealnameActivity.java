package com.siyecaoi.yy.ui.find;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.WebBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Utils;

/**
 * 实名认证页面
 */
public class RealnameActivity extends MyBaseActivity {

    @BindView(R.id.edt_name_realname)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_name_realname)
    @Order(1)
    EditText edtNameRealname;
    @BindView(R.id.edt_idcard_realname)
    @NotEmpty(sequence = 1, messageResId = R.string.hint_idcard_realname)
    @Order(2)
    EditText edtIdcardRealname;
    @BindView(R.id.iv_choose_realname)
    ImageView ivChooseRealname;
    @BindView(R.id.tv_web_realname)
    TextView tvWebRealname;
    @BindView(R.id.btn_sure_realname)
    Button btnSureRealname;
    @BindView(R.id.mWebview_realname)
    WebView mWebviewRealname;
    String url;
    boolean isCheck;

    Validator validator;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_realname);
    }

    @Override
    public void initView() {
        setTitleText(R.string.title_realname);

        String digits = "0123456789x";
        edtIdcardRealname.setKeyListener(DigitsKeyListener.getInstance(digits));
        getUrlCall();
        validator = new Validator(this);
        validator.setValidationListener(validationListener);

        edtIdcardRealname.addTextChangedListener(textWatcher);
        edtNameRealname.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setButtonShow();
        }
    };

    Validator.ValidationListener validationListener = new Validator.ValidationListener() {
        @Override //正确
        public void onValidationSucceeded() {
            getCall();
        }

        @Override //有误
        public void onValidationFailed(List<ValidationError> errors) {
            for (ValidationError error : errors) {
                View view = error.getView();
                showToast(error.getFailedRules().get(0).getMessage(RealnameActivity.this));
            }
        }
    };

    private void getCall() {
        String realName = edtNameRealname.getText().toString();
        String idcard = edtIdcardRealname.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("name", realName);
        map.put("card", idcard);
        HttpManager.getInstance().post(Api.Addaudit, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("提交审核成功，请耐心等待审核");
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void getUrlCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("type", Const.IntShow.SIX);
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
        WebSettings settings = mWebviewRealname.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        mWebviewRealname.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }

        });

        mWebviewRealname.setWebViewClient(new WebViewClient() {
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
                    mWebviewRealname.loadUrl(url);
                }
                return true;
            }
        });
        mWebviewRealname.loadUrl(url);
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //webView回收,部分手机无效
        if (mWebviewRealname != null) {
            mWebviewRealname.stopLoading();
            mWebviewRealname.removeAllViews();
            mWebviewRealname.destroy();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_web_realname, R.id.btn_sure_realname, R.id.iv_choose_realname})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_web_realname:
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.TYPE, Const.IntShow.FIVE);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.title_realname));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.iv_choose_realname:
                if (isCheck) {
                    isCheck = false;
                    ivChooseRealname.setSelected(false);
                } else {
                    isCheck = true;
                    ivChooseRealname.setSelected(true);
                }
                setButtonShow();
                break;
            case R.id.btn_sure_realname:
                if (isCheck) {
                    validator.validate();
                } else {
                    showToast(getString(R.string.hint_realname));
                }
                break;
        }
    }

    private void setButtonShow() {
        boolean isShow = true;
        if (!isCheck) {
            isShow = false;
        }
        String nameShow = edtNameRealname.getText().toString();
        String idcard = edtIdcardRealname.getText().toString();
        if (StringUtils.isEmpty(nameShow)) {
            isShow = false;
        }
        if (StringUtils.isEmpty(idcard) || idcard.length() != 18) {
            isShow = false;
        }
        if (isShow) {
            btnSureRealname.setAlpha(1.0f);
            btnSureRealname.setClickable(true);
        } else {
            btnSureRealname.setAlpha(0.5f);
            btnSureRealname.setClickable(false);
        }
    }
}
