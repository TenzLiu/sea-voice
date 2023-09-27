package com.siyecaoi.yy.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.dialog.SignSuccessDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.SpanBuilder;

public class SignUpActivity extends MyBaseActivity {
    @BindView(R.id.btn_sign)
    TextView btnSign;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.view_2)
    View view2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.view_3)
    View view3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.view_4)
    View view4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.view_5)
    View view5;
    @BindView(R.id.tv_6)
    TextView tv6;
    @BindView(R.id.view_6)
    View view6;
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.tv_content)
    TextView tv_title;

    private ArrayList<TextView> textViews;
    private ArrayList<View> views;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    public void initView() {
        setTitleText("签到");
        getSignData();
        textViews = new ArrayList<TextView>();
        textViews.add(tv1);
        textViews.add(tv2);
        textViews.add(tv3);
        textViews.add(tv4);
        textViews.add(tv5);
        textViews.add(tv6);
        textViews.add(tv7);
        views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
        views.add(view6);
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {
        btnSign.setOnClickListener(v ->
                sign());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 签到详情
     */
    private void getSignData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", SharedPreferenceUtils.get(this, Const.User.USER_TOKEN, 0));
        HttpManager.getInstance().post(Api.SIGN_DATA, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optInt("code") == 0) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        int is_sign = data.optInt("is_sign");
                        if (is_sign == 0) {
                            btnSign.setEnabled(true);
                        } else
                            btnSign.setEnabled(false);
                        int sign = data.optInt("sign");
                        tv_title.setText(SpanBuilder.content("已连续签到 "+sign+" 天")
                                .boldSpan(SignUpActivity.this,6,7)
                                .sizeSpan(6,7,18)
                                .colorSpan(SignUpActivity.this,6,7,R.color.red2).build());
                        for (int i=0;i<sign;i++){
                            textViews.get(i).setSelected(true);
                            if (i==0)
                                continue;
                            views.get(i-1).setSelected(true);
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

    /**
     * 签到
     */
    private void sign() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", SharedPreferenceUtils.get(this, Const.User.USER_TOKEN, 0));
        HttpManager.getInstance().post(Api.SIGN, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optInt("code") == 0) {
                        SignSuccessDialog signSuccessDialog = new SignSuccessDialog();
                        Bundle args = new Bundle();
                        args.putInt("num",jsonObject.optJSONObject("data").optInt("money"));
                        signSuccessDialog.setArguments(args);
                        signSuccessDialog.show(getSupportFragmentManager(),"success");
                        getSignData();
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
