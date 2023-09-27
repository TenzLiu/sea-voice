package com.siyecaoi.yy.dialog;

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

import com.siyecaoi.yy.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 房间密码弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyRoomPassDialog extends Dialog {


    @BindView(R.id.tv_hinttitle)
    TextView tvHinttitle;
    @BindView(R.id.edt_pass_roompass)
    EditText edtPassRoompass;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_show)
    LinearLayout llShow;
    @BindView(R.id.rl_center)
    RelativeLayout rlCenter;

    public MyRoomPassDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_roompassdialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void setRightButton(View.OnClickListener clickListener) {
        tvSure.setOnClickListener(clickListener);

    }

    public void setLeftButton(View.OnClickListener clickListener) {
        tvCancel.setOnClickListener(clickListener);
    }

    public String getPassword() {
        return edtPassRoompass.getText().toString();
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
}