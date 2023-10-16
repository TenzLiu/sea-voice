package com.jingtaoi.yy.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.WebBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.SinglePicker;
import cn.qqtheme.framework.picker.WheelPicker;
import cn.sinata.xldutils.utils.TimeUtils;
import cn.sinata.xldutils.utils.Utils;

public class RecommendActivity extends MyBaseActivity {
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.ll_form)
    LinearLayout llForm;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.btn_action)
    Button btnAction;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.webView)
    WebView webView;

    private String date;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_recommend);
    }

    @Override
    public void initView() {
        setTitleText("推荐位");
        setRightText(R.string.record_apply);
        setRightButton(v ->
                startActivity(new Intent(this, ApplyRecordActivity.class))
        );
        tvNum.setText((String) SharedPreferenceUtils.get(this, Const.User.ROOMID, ""));
        getTuijianNum();
        getTuijianRule();
    }

    String url;

    private void getTuijianRule() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("type", Const.IntShow.ONE);
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
        WebSettings settings = webView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(false);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }

        });

        webView.setWebViewClient(new WebViewClient() {
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
                    webView.loadUrl(url);
                }
                return true;
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {
        tvTime.setOnClickListener(v -> {
                    DatePicker datePicker = new DatePicker(this, DateTimePicker.YEAR_MONTH_DAY);
                    initPicker(datePicker);
                    Calendar calendar = Calendar.getInstance();
                    datePicker.setRangeStart(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                    calendar.setTimeInMillis(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
                    datePicker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                    datePicker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> {
                                date = year + "-" + month + "-" + day;
                                tvTime.setText(date);
                            }
                    );
                    datePicker.show();
                }
        );
        ArrayList<String> strings = new ArrayList<>(); //时间段： 00：00—23：00
        for (int i = 0; i < 24; i++) {
            if (i < 10)
                strings.add("0" + i + ":00");
            else
                strings.add(i + ":00");
        }
        tvDuration.setOnClickListener(v -> {
                    if (date == null) {
                        showToast("请先选择日期");
                        return;
                    }
                    long l = System.currentTimeMillis();
                    SinglePicker timePicker;
                    if (date.equals(TimeUtils.getTimeYMD(l))) {//当天
                        int hour = TimeUtils.getTimeHour(l);
                        if (hour == 23) {
                            timePicker = new SinglePicker<>(this, new String[0]);
                        } else
                            timePicker = new SinglePicker<String>(this, strings.subList(hour + 1, strings.size()));
                    } else
                        timePicker = new SinglePicker<>(this, strings);
                    timePicker.setOnItemPickListener((index, item) -> {
                        tvDuration.setText((String) item);
                    });
                    initPicker(timePicker);
                    timePicker.show();

                }
        );
        btnAction.setOnClickListener(v -> {
            getTuijian();
        });
    }

    private void initPicker(WheelPicker picker) {
        picker.setLabelTextColor(Color.BLACK);
        picker.setCancelTextColor(Color.parseColor("#999999"));
        picker.setSubmitTextColor(Color.parseColor("#FF003F"));
        picker.setTopLineColor(Color.parseColor("#999999"));
        picker.setDividerColor(Color.WHITE);
        picker.setTextColor(Color.BLACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void getTuijian() {
        if (date== null || TextUtils.isEmpty(date)){
            showToast("请选择推荐时间");
            return;
        }
        if ("选择推荐时间".equals(tvDuration.getText().toString())){
            showToast("请选择推荐时间");
            return;
        }
        btnAction.setEnabled(false);
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", SharedPreferenceUtils.get(this, Const.User.USER_TOKEN, 0));
        map.put("time", date + " " + tvDuration.getText() + ":00");
        HttpManager.getInstance().post(Api.APPLY_TUIJIAN, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optInt("code") == 0) {
                        showToast("申请成功");
                        tvTime.setText("选择推荐日期");
                        tvDuration.setText("选择推荐时间");
                        date = null;
                        getTuijianNum();
                        btnAction.setEnabled(true);
                    } else {
                        btnAction.setEnabled(true);
                        showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("解析错误");
                    btnAction.setEnabled(true);
                }
            }
        });
    }

    /**
     * 推荐次数
     */
    private void getTuijianNum() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", SharedPreferenceUtils.get(this, Const.User.USER_TOKEN, 0));
        HttpManager.getInstance().post(Api.TUIJIAN_NUM, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optInt("code") == 0) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        int num = data.optInt("num");
                        btnAction.setText("您本周还有" + num + "次推荐");
                        if (num == 0) {
                            btnAction.setEnabled(false);
                            btnAction.setAlpha(0.5f);
                        } else {
                            btnAction.setEnabled(true);
                            btnAction.setAlpha(1f);
                        }
                    } else {
                        showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("解析错误");
                }
            }
        });
    }
}
