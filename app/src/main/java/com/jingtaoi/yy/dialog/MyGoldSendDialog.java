package com.jingtaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyApplication;
import com.jingtaoi.yy.bean.GetOneBean;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.BroadcastManager;
import com.jingtaoi.yy.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.utils.Toast;


/**
 * Created by Administrator on 2018/3/9.
 */

public class MyGoldSendDialog extends Dialog {


    @BindView(R.id.tv_hinttitle)
    TextView tvHinttitle;
    @BindView(R.id.tv_name_goldsend)
    TextView tvNameGoldsend;
    @BindView(R.id.tv_hintshow)
    TextView tvHintshow;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.tv_numbershow)
    TextView tvNumbershow;
    @BindView(R.id.rl_cen)
    LinearLayout rlCen;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.ll_show)
    LinearLayout llShow;
    @BindView(R.id.rl_center)
    RelativeLayout rlCenter;
    VoiceUserBean.DataBean dataBean;
    int userId;
    Context context;
    @BindView(R.id.tv_id_goldsend)
    TextView tvIdGoldsend;
    @BindView(R.id.tv_record)
    TextView tv_record;

    public MyGoldSendDialog(Context context, VoiceUserBean.DataBean dataBean, int userId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.userId = userId;
        this.dataBean = dataBean;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_goldsend);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        tvNameGoldsend.setText("昵称：" + dataBean.getName());
        tvIdGoldsend.setText("ID：" + dataBean.getUsercoding());

        setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getGoldCall();
    }

    private void getGoldCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        HttpManager.getInstance().post(Api.getUserMoney, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    if (getOneBean.getData().getState() == 2) {
                        Toast.create(MyApplication.getInstance()).show("您的账号已锁定，请联系客服");
                        BroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(Const.BroadCast.EXIT);
                        return;
                    }
                    tv_balance.setText("" + getOneBean.getData().getGold());

                    tvNumbershow.setText("目前拥有" + getOneBean.getData().getGold() + "个");
                } else {
                    showToast(getOneBean.getMsg());
                }

            }
        });
    }

    public void setRecordButton(View.OnClickListener clickListener) {
        tv_record.setOnClickListener(clickListener);
    }

    /**
     * 设置右键文字和点击事件
     *
     * @param rightStr      文字
     * @param clickListener 点击事件
     */
    public void setRightButton(String rightStr, View.OnClickListener clickListener) {
        tvSure.setOnClickListener(clickListener);
        tvSure.setText(rightStr);
    }

    public void setRightButton(View.OnClickListener clickListener) {
        tvSure.setOnClickListener(clickListener);
    }

    /**
     * 设置左键文字和点击事件
     *
     * @param leftStr       文字
     * @param clickListener 点击事件
     */
    public void setLeftButton(String leftStr, View.OnClickListener clickListener) {
        tvCancel.setOnClickListener(clickListener);
        tvCancel.setText(leftStr);
    }

    public void setLeftButton(View.OnClickListener clickListener) {
        tvCancel.setOnClickListener(clickListener);
    }

    /**
     * 设置title
     *
     * @param str 内容
     */
    public void setHintTitle(String str) {
        tvHinttitle.setText(str);
    }


    /**
     * 设置提示内容
     *
     * @param str 内容
     */
    public void setHintText(String str) {
        tvHintshow.setText(str);
    }

    /**
     * 给两个按钮 设置文字
     *
     * @param leftStr  左按钮文字
     * @param rightStr 右按钮文字
     */
    public void setBtnText(String leftStr, String rightStr) {
        tvCancel.setText(leftStr);
        tvSure.setText(rightStr);
    }

    public void setRightText(String rightStr) {
        tvSure.setText(rightStr);
    }

    public void setLeftText(String rightStr) {
        tvCancel.setText(rightStr);
    }

    public String getNumber() {
        return edtNumber.getText().toString();
    }

}