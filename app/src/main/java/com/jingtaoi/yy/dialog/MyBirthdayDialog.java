package com.jingtaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;


import com.jingtaoi.yy.R;

import java.util.Calendar;


/**
 * Created by Administrator on 2018/3/9.
 */

public class MyBirthdayDialog extends Dialog implements DatePicker.OnDateChangedListener {


    TextView tv_cancel, tv_sure;
    DatePicker datePicker;
    Calendar calendar;
    Context context;
    private long timeBirthday;


    public MyBirthdayDialog(Context context, long timeBirthday) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.timeBirthday = timeBirthday;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.layout_mybirthday);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        tv_cancel = findViewById(R.id.tv_cancle);
        tv_sure = findViewById(R.id.tv_sure);
        datePicker = findViewById(R.id.datepicker_dialog);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeBirthday);
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, monthOfYear, dayOfMonth, this);
        datePicker.setMaxDate(System.currentTimeMillis());
        setLeftButton(new View.OnClickListener() {
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
        tv_sure.setOnClickListener(clickListener);
        tv_sure.setText(rightStr);
    }

    public void setRightButton(View.OnClickListener clickListener) {
        tv_sure.setOnClickListener(clickListener);
    }

    public Calendar getDate() {
        return calendar;
    }

    /**
     * 设置左键文字和点击事件
     *
     * @param leftStr       文字
     * @param clickListener 点击事件
     */
    public void setLeftButton(String leftStr, View.OnClickListener clickListener) {
        setLeftButton(clickListener);
        tv_cancel.setText(leftStr);
    }

    public void setLeftButton(View.OnClickListener clickListener) {
        tv_cancel.setOnClickListener(clickListener);

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }
}