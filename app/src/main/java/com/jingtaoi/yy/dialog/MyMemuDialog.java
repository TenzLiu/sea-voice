package com.jingtaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.ui.room.MyUpdateActivity;
import com.jingtaoi.yy.ui.room.PlaceMusicActivity;
import com.jingtaoi.yy.utils.ActivityCollector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 音乐页面更多弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyMemuDialog extends Dialog {


    @BindView(R.id.tv_updata_music)
    TextView tvUpdataMusic;
    @BindView(R.id.tv_mine_music)
    TextView tvMineMusic;
    @BindView(R.id.tv_mineupdata_music)
    TextView tvMineupdataMusic;
    @BindView(R.id.ll_back_music)
    LinearLayout llBackMusic;

    public MyMemuDialog(Context context) {
        super(context, R.style.CustomDialogStyle);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_menu);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @OnClick({R.id.tv_updata_music, R.id.tv_mine_music, R.id.tv_mineupdata_music})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_updata_music:

                break;
            case R.id.tv_mine_music:
                dismiss();
                ActivityCollector.getActivityCollector().toOtherActivity(PlaceMusicActivity.class);
                break;
            case R.id.tv_mineupdata_music:
                dismiss();
                ActivityCollector.getActivityCollector().toOtherActivity(MyUpdateActivity.class);
                break;
        }
    }

    @OnClick(R.id.ll_back_music)
    public void onViewClicked() {
        dismiss();
    }

    public void setUpdate(View.OnClickListener onClickListener) {
        tvUpdataMusic.setOnClickListener(onClickListener);
    }
}