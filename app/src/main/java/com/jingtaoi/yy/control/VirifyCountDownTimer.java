package com.jingtaoi.yy.control;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.jingtaoi.yy.R;

import cn.sinata.xldutils.utils.StringUtils;


/**
 * 获取验证码倒计时
 * Created by Administrator on 2018/2/11.
 */

public class VirifyCountDownTimer extends CountDownTimer {
    private TextView tvCountDown;
    String textShow;

    public VirifyCountDownTimer(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        tvCountDown = textView;
    }

    public VirifyCountDownTimer(TextView textView, String textShow, long millisInFuture,
                                long countDownInterval, TimerFinish timerFinish) {
        super(millisInFuture, countDownInterval);
        tvCountDown = textView;
        this.textShow = textShow;
        this.timerFinish = timerFinish;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        tvCountDown.setClickable(false);
//            @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String reacquire =
//                    String.format(getString(R.string.tv_login_s), millisUntilFinished / 1000);
////            String reacquire = millisUntilFinished / 1000 + "s";

        tvCountDown.setText(millisUntilFinished / 1000 + " 秒");  //设置倒计时时间
    }

    @Override
    public void onFinish() {
        tvCountDown.setClickable(true);
        if (StringUtils.isEmpty(textShow)) {
            tvCountDown.setText(R.string.tv_getagain_code);
        } else {
            tvCountDown.setText(textShow);
        }
        if (timerFinish != null)
            timerFinish.onFinish();
    }

    private TimerFinish timerFinish;

    public interface TimerFinish {
        void onFinish();
    }
}