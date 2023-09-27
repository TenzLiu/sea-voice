package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.PkSetBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * pk弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyPkDialog extends Dialog {

    int userId;
    Context context;
    @BindView(R.id.iv_tosmallpk_voice)
    ImageView ivTosmallpkVoice;
    @BindView(R.id.iv_oneimg_max_voice)
    SimpleDraweeView ivOneimgMaxVoice;
    @BindView(R.id.tv_onetype_max_voice)
    TextView tvOnetypeMaxVoice;
    @BindView(R.id.tv_pk_max_voice)
    TextView tvPkMaxVoice;
    @BindView(R.id.iv_twoimg_max_voice)
    SimpleDraweeView ivTwoimgMaxVoice;
    @BindView(R.id.tv_twotype_max_voice)
    TextView tvTwotypeMaxVoice;
    @BindView(R.id.tv_onename_maxpk_voice)
    TextView tvOnenameMaxpkVoice;
    @BindView(R.id.tv_twoname_maxpk_voice)
    TextView tvTwonameMaxpkVoice;
    @BindView(R.id.progress_maxpk_voice)
    ProgressBar progressMaxpkVoice;
    @BindView(R.id.tv_show_maxpk_voice)
    TextView tvShowMaxpkVoice;
    @BindView(R.id.ll_maxpk_voice)
    LinearLayout llMaxpkVoice;
    @BindView(R.id.rl_pk_voice)
    RelativeLayout rlPkVoice;
    @BindView(R.id.tv_max_end_voice)
    TextView tvMaxEndVoice;


    PkSetBean.DataBean dataBean;
    @BindView(R.id.tv_onenumber_pk)
    TextView tvOnenumberPk;
    @BindView(R.id.tv_twonumber_pk)
    TextView tvTwonumberPk;


    MyBottomShowDialog myBottomShowDialog;
    int buid;//选择送的那个人的id
    @BindView(R.id.tv_show_clicker_voice)
    TextView tvShowClickerVoice;

    public MyPkDialog(Context context, int userId, PkSetBean.DataBean data) {
        super(context, R.style.CustomDialogStyle);
        this.userId = userId;
        this.context = context;
        this.dataBean = data;
    }

    public void updateData(PkSetBean.DataBean data) {
        this.dataBean = data;
        initShow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.voice_pk);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        initShow();

    }

    @SuppressLint("SetTextI18n")
    private void initShow() {
        ImageUtils.loadUri(ivOneimgMaxVoice, dataBean.getUser().getImg());
        ImageUtils.loadUri(ivTwoimgMaxVoice, dataBean.getUser1().getImg());
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/impact.ttf");
        tvPkMaxVoice.setTypeface(typeface);
        tvOnenameMaxpkVoice.setText(dataBean.getUser().getName());
        tvTwonameMaxpkVoice.setText(dataBean.getUser1().getName());
        progressMaxpkVoice.setMax(dataBean.getUser().getNum() + dataBean.getUser1().getNum());
        progressMaxpkVoice.setProgress(dataBean.getUser().getNum());
        tvOnenumberPk.setText(dataBean.getUser().getNum() + "");
        tvTwonumberPk.setText(dataBean.getUser1().getNum() + "");
        if (dataBean.getState() == 1) {
            tvShowMaxpkVoice.setText("本轮按照人数投票");
            tvShowClickerVoice.setText("点击头像投票");
        } else if (dataBean.getState() == 2) {
            tvShowMaxpkVoice.setText("本轮按照礼物价值投票");
            tvShowClickerVoice.setText("点击头像打赏");
        }

        long timeLong = dataBean.getTime();
        long nowTime = System.currentTimeMillis();
        long endTimeShow = timeLong - nowTime;
        if (endTimeShow > 0) {
            tvMaxEndVoice.setText((endTimeShow / 1000) + "s");
            CountDownTimer countDownTimer = new CountDownTimer(endTimeShow, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvMaxEndVoice.setText(millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    setEndShow();
                }
            };
            countDownTimer.start();
        } else {
            setEndShow();
        }
    }

    private void setEndShow() {
        tvOnetypeMaxVoice.setVisibility(View.VISIBLE);
        tvTwotypeMaxVoice.setVisibility(View.VISIBLE);
        int oneNum = dataBean.getUser().getNum();
        int twoNum = dataBean.getUser1().getNum();
        if (oneNum > twoNum) {
            tvOnetypeMaxVoice.setText(context.getString(R.string.tv_win));
            tvTwotypeMaxVoice.setText(context.getString(R.string.tv_lose));
        } else if (oneNum == twoNum) {
            tvOnetypeMaxVoice.setText(context.getString(R.string.tv_flat));
            tvTwotypeMaxVoice.setText(context.getString(R.string.tv_flat));
        } else if (oneNum < twoNum) {
            tvOnetypeMaxVoice.setText(context.getString(R.string.tv_lose));
            tvTwotypeMaxVoice.setText(context.getString(R.string.tv_win));
        }

        handler.sendEmptyMessageDelayed(0, 3000);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                dismiss();
            }
        }
    };


    public void setOneClicker(View.OnClickListener oneClicker) {
        ivOneimgMaxVoice.setOnClickListener(oneClicker);
    }

    public void setTwoClicker(View.OnClickListener oneClicker) {
        ivTwoimgMaxVoice.setOnClickListener(oneClicker);
    }


    @OnClick({R.id.iv_tosmallpk_voice, R.id.iv_oneimg_max_voice, R.id.iv_twoimg_max_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_tosmallpk_voice:
                dismiss();
                break;
            case R.id.iv_oneimg_max_voice:
//                buid = dataBean.getUser().getId();
//                setShow();
                break;
            case R.id.iv_twoimg_max_voice:
//                buid = dataBean.getUser1().getId();
//                setShow();
                break;
        }
    }

//    private void setShow() {
//        BottomShowRecyclerAdapter bottomShowRecyclerAdapter = showMybottomDialog(initTypeShow(), context);
//        bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
//                    myBottomShowDialog.dismiss();
//                }
//                switch (position) {
//                    case 0:
//                        break;
//                    case 1:
//
//                        break;
//
//                }
//            }
//
//
//        });
//    }
//
//
//    private ArrayList<String> initTypeShow() {
//        ArrayList<String> bottomList = new ArrayList<>();
//        if (dataBean.getState() == 1) {
//            bottomList.add(context.getString(R.string.tv_tou_roompk));
//        } else if (dataBean.getState() == 2) {
//            bottomList.add(context.getString(R.string.tv_send_roompk));
//        }
//        bottomList.add(context.getString(R.string.tv_look_roompk));
//        bottomList.add(context.getString(R.string.tv_cancel));
//        return bottomList;
//    }
//
//    private BottomShowRecyclerAdapter showMybottomDialog(ArrayList<String> seatList, Context context) {
//        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
//            myBottomShowDialog.dismiss();
//        }
//        myBottomShowDialog = new MyBottomShowDialog(context, seatList);
//        myBottomShowDialog.show();
//        return myBottomShowDialog.getAdapter();
//    }
}