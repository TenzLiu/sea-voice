package com.jingtaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingtaoi.yy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 房间话题弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyTopicshowDialog extends Dialog {


    @BindView(R.id.tv_title_topicshow)
    TextView tvTitleTopicshow;
    @BindView(R.id.iv_close_topicshow)
    ImageView ivCloseTopicshow;
    @BindView(R.id.tv_show_topicshow)
    TextView tvShowTopicshow;
    @BindView(R.id.ll_back_onlines_user)
    LinearLayout llBackOnlinesUser;
    String title, content;

    public MyTopicshowDialog(Context context, String title, String content) {
        super(context, R.style.CustomDialogStyle);
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_topicshow);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        tvTitleTopicshow.setText(title);
        tvShowTopicshow.setText(content);

    }

    @OnClick(R.id.iv_close_topicshow)
    public void onViewClicked() {
        dismiss();
    }
}