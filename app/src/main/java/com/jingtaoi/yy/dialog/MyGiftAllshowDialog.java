package com.jingtaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.model.GiftAllModel;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


/**
 * 全服礼物
 * Created by Administrator on 2018/3/9.
 */

public class MyGiftAllshowDialog extends Dialog {


    @BindView(R.id.iv_one_giftall)
    SimpleDraweeView ivOneGiftall;
    @BindView(R.id.tv_one_giftall)
    TextView tvOneGiftall;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.iv_two_giftall)
    SimpleDraweeView ivTwoGiftall;
    @BindView(R.id.tv_two_giftall)
    TextView tvTwoGiftall;
    @BindView(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @BindView(R.id.iv_gift_giftall)
    SimpleDraweeView ivGiftGiftall;
    @BindView(R.id.tv_number_giftall)
    TextView tvNumberGiftall;
    @BindView(R.id.view_close_giftall)
    View viewCloseGiftall;
    @BindView(R.id.tv_goto_giftall)
    TextView tvGotoGiftall;
    @BindView(R.id.rl_giftall)
    RelativeLayout rlGiftall;

    private ArrayList<GiftAllModel.DataBean> list;
    Activity context;

    Timer timer;
    TimerTask timerTask;
    Animation animation;
    String roomNow;

    public MyGiftAllshowDialog(Activity context, List<GiftAllModel.DataBean> giftAllModels) {
        super(context, R.style.CustomDialogStyle1);
        this.context = context;

        setGiftAllModel(giftAllModels);
    }

    public void setGiftAllModel(GiftAllModel.DataBean giftAllModel) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(giftAllModel);
    }

    public void setGiftAllModel(List<GiftAllModel.DataBean> giftAllModels) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(giftAllModels);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_giftallshow);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.TOP);
//        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);

//        getCall();
        setGiftShow();
        setGiftTimer();
    }

    private void setGiftTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @SuppressLint("CheckResult")
            @Override
            public void run() {
                Observable.just("0").observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                animation = AnimationUtils.loadAnimation(context, R.anim.popup_top_out);
                                rlGiftall.startAnimation(animation);
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    //动画结束的时候触发
                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        setGiftShow();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }
                        });

            }
        };
        timer.schedule(timerTask, 5000, 5000);
    }

    private void setGiftShow() {
        if (list != null && list.size() > 0) {
            GiftAllModel.DataBean giftAllModel = list.get(0);
            roomNow = giftAllModel.getRid();
            ImageUtils.loadUri(ivOneGiftall, giftAllModel.getSimg());
            tvOneGiftall.setText(giftAllModel.getSnickname());
            ImageUtils.loadUri(ivTwoGiftall, giftAllModel.getBimg());
            tvTwoGiftall.setText(giftAllModel.getBnickname());
            ImageUtils.loadUri(ivGiftGiftall, giftAllModel.getImg());
            tvNumberGiftall.setText("X" + giftAllModel.getNum());

            animation = AnimationUtils.loadAnimation(context, R.anim.popup_top_enter);
            rlGiftall.startAnimation(animation);
            list.remove(giftAllModel);
        } else {
            if (timer != null) {
                timer.cancel();
            }
            dismiss();
        }
    }

    public String getRoomId() {
        return roomNow;
    }

    public TextView getButton() {
        return tvGotoGiftall;
    }

    @OnClick({R.id.view_close_giftall, R.id.tv_goto_giftall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_close_giftall:
                if (timer != null) {
                    timer.cancel();
                }
                dismiss();
                break;
            case R.id.tv_goto_giftall:

                break;
        }
    }
}