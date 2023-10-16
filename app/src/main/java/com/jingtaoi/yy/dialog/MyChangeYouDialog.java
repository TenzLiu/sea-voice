package com.jingtaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.ExchangeBean;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/3/9.
 */

public class MyChangeYouDialog extends Dialog {


    @BindView(R.id.tv_hinttitle)
    TextView tvHinttitle;
    @BindView(R.id.tv_hintshow)
    TextView tvHintshow;
    @BindView(R.id.tv_you_hint)
    TextView tvYouHint;
    @BindView(R.id.rl_you)
    LinearLayout rlYou;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_show)
    LinearLayout llShow;
    @BindView(R.id.rl_center)
    RelativeLayout rlCenter;
    ExchangeBean baseBean;
    @BindView(R.id.iv_show_hint)
    SimpleDraweeView ivShowHint;

    public MyChangeYouDialog(Context context, ExchangeBean baseBean) {
        super(context, R.style.CustomDialogStyle);
        this.baseBean = baseBean;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_changeyou);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (baseBean.getData().getState() == 0) {
            tvHintshow.setText("兑换成功");
        } else if (baseBean.getData().getState() == 1) {
            tvHinttitle.setText("兑换成功");
            tvHintshow.setText("平台赠送您");
            ImageUtils.loadDrawable(ivShowHint, R.drawable.gold);
            tvYouHint.setText(" ×" + baseBean.getData().getNum());
        } else if (baseBean.getData().getState() == 2) {
            tvHinttitle.setText("兑换成功");
            tvHintshow.setText("平台赠送您");
            ImageUtils.loadUri(ivShowHint, baseBean.getData().getNum());
        }
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


    public void setRightText(String rightStr) {
        tvSure.setText(rightStr);
    }


}