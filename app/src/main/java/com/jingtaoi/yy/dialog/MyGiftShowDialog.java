package com.jingtaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.utils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.jingtaoi.yy.utils.SvgaUtils;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 礼物显示页面
 * Created by Administrator on 2018/3/9.
 */

public class MyGiftShowDialog extends Dialog {


    @BindView(R.id.mSVGAImageView_gift)
    SVGAImageView mSVGAImageViewGift;

    String imgShow;
    Context context;
    int number;
    @BindView(R.id.tv_show_gift)
    TextView tvShowGift;
    @BindView(R.id.iv_show_gift)
    SimpleDraweeView ivShowGift;
    Timer timer;
    private SVGADrawable drawable;

    public MyGiftShowDialog(Context context, String imgShow, int number) {
        super(context, R.style.CustomDialogStyle);
        this.imgShow = imgShow;
        this.context = context;
        this.number = number;
    }

    public void updataShow(String imgShow, int number) {
        this.imgShow = imgShow;
        this.number = number;
        initShow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_giftshow);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().setGravity(Gravity.CENTER);

        initShow();
    }

    private void initShow() {
        if (number != 0) {
            tvShowGift.setText("X" + number);
        } else {
            tvShowGift.setText("");
        }
        if (imgShow.endsWith(".svga")) {
            mSVGAImageViewGift.setVisibility(View.VISIBLE);
            ivShowGift.setVisibility(View.INVISIBLE);
            mSVGAImageViewGift.setCallback(new SVGACallback() {
                @Override
                public void onPause() {
                    LogUtils.e("svga", "onPause");
                }

                @Override
                public void onFinished() {
                    LogUtils.e("msg", "svga完成");
                    handler.sendEmptyMessage(101);
                }

                @Override
                public void onRepeat() {

                }

                @Override
                public void onStep(int i, double v) {

                }
            });
            SvgaUtils.playSvgaFromUrl(imgShow, mSVGAImageViewGift, new SvgaUtils.SvgaDecodeListener() {
                @Override
                public void onError() {
                    LogUtils.e("svga", "图片加载出错");
                    handler.sendEmptyMessage(101);
                }
            });
        } else {
            mSVGAImageViewGift.setVisibility(View.INVISIBLE);
            ivShowGift.setVisibility(View.VISIBLE);
//            ImageUtils.loadUri(ivShowGift, imgShow);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(imgShow)
                    .setAutoPlayAnimations(true)
                    .setControllerListener(new BaseControllerListener<ImageInfo>(){
                        @Override
                        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);

                            if (timer != null) {
                                timer.cancel();
                            }
                            timer = new Timer();
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(101);
                                }
                            };
                            timer.schedule(timerTask, 5000);
                        }
                    })
                    .build();
            ivShowGift.setController(controller);
        }
//        else {
//            timer = new Timer();
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    handler.sendEmptyMessage(101);
//                }
//            };
//            timer.schedule(timerTask, 2000);
//        }

    }



    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
//                    dismiss();
                    if (onGiftFinish == null) {
                        dismiss();
                    } else {
                        drawable=null;
                        mSVGAImageViewGift.stopAnimation();
                        onGiftFinish.onFinishShow();
                    }
                    break;
            }
        }
    };
    OnGiftFinish onGiftFinish;

    public void setOnGiftFinish(OnGiftFinish onGiftFinish) {
        this.onGiftFinish = onGiftFinish;
    }

    public interface OnGiftFinish {
        void onFinishShow();
    }

}