package com.jingtaoi.yy.dialog;

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

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/3/9.
 */

public class MyHintDialog extends Dialog {


    @BindView(R.id.tv_hinttitle)
    TextView tvHinttitle;
    @BindView(R.id.tv_hintshow)
    TextView tvHintshow;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_show)
    LinearLayout llShow;
    @BindView(R.id.rl_center)
    RelativeLayout rlCenter;

    public MyHintDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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