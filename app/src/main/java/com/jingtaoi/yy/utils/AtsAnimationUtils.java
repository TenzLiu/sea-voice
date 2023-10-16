package com.jingtaoi.yy.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jingtaoi.yy.R;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.List;

/**
 * 动画工具类
 */
public class AtsAnimationUtils {

    /**
     * 撒网操作
     *
     * @param fishViews
     * @param type      类型
     * @param fishViews 鱼
     * @param parent    父布局
     */
    public static void fishingAnima(Context context, List<ImageView> fishViews, int type, RelativeLayout parent, FishingAnimaListener fishingAnimaListener) {
        int path = R.mipmap.ic_deep_sea_fishing_net;
        if (type == 1) {
            path = R.mipmap.ic_deep_sea_fishing_net_effects;
        } else if (type == 2) {
            path = R.mipmap.ic_dinghai_fishing_net;
        } else if (type == 3) {
            path = R.mipmap.ic_donghai_fishing_net;
        }
        int pos = 0;
        boolean isLast = false;
        for (ImageView fishView : fishViews) {
            ImageView fishNet = new ImageView(context);
            RelativeLayout.LayoutParams fishNetLayoutParams = new RelativeLayout.LayoutParams(ScreenUtil.getPxByDp(70), ScreenUtil.getPxByDp(70));
//            ImageUtils.loadImage(context,fishNet, R.mipmap.ic_deep_sea_fishing_net,0,false);
            fishNet.setImageResource(path);
            fishNetLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            fishNetLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.rl_content);
            fishNet.setLayoutParams(fishNetLayoutParams);
            parent.addView(fishNet);
            scaleXY(fishNet);
            if (pos == fishViews.size() - 1) {
                //最后一个
                isLast = true;
            }
            moveToWord(fishNet, fishView, parent, type, isLast, fishingAnimaListener);
            pos++;
        }

    }

    /**
     * 缩放动画
     *
     * @param view
     */
    public static void scaleXY(View view) {
        AnimatorSet resizeAvenger = new AnimatorSet();
        ObjectAnimator objectAnimationX = ObjectAnimator.ofFloat(view, "scaleX", 0.2f, 1f);
        ObjectAnimator objectAnimationY = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1f);
        objectAnimationX.setDuration(1000);
        objectAnimationX.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimationY.setDuration(1000);
        objectAnimationY.setRepeatMode(ValueAnimator.REVERSE);
        resizeAvenger.playTogether(objectAnimationX, objectAnimationY);
        resizeAvenger.start();
    }

    /**
     * 一个view移动到指定view位置
     */
    public static void moveToWord(
            ImageView view,
            View targetView,
            RelativeLayout parent, int type, boolean isLast, FishingAnimaListener fishingAnimaListener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                float x = view.getX() + ScreenUtil.getPxByDp(5);
                float y = view.getY() - ScreenUtil.getPxByDp(10);
                float targetX = targetView.getX() + (targetView.getWidth() / 3) - ScreenUtil.getPxByDp(12);
                float targetY = targetView.getY() + (targetView.getHeight() / 3) - ScreenUtil.getPxByDp(25);
                ValueAnimator valueAnimator = new ValueAnimator();
                valueAnimator.setDuration(1000);
                valueAnimator.setObjectValues(new PointF(x, y));
                valueAnimator.setInterpolator(new DecelerateInterpolator());


                //首先判断 目标点在上还是在下
                boolean flagX = x - targetX > 0;
                boolean flagY = y - targetY > 0;
                valueAnimator.setEvaluator(new TypeEvaluator() {
                    @Override
                    public Object evaluate(float fraction, Object startValue, Object endValue) {
                        PointF point = new PointF();
                        //这里是需要倒着来  最后要到达200 200 这个点
                        float fractionNeed = 1 - fraction;
                        if (flagX) {
                            float vX = x - targetX;
                            point.x = vX * fractionNeed + targetX;
                        } else {
                            float vX = targetX - x;
                            point.x = x + vX * fraction;
                        }
                        if (flagY) {
                            float vY = y - targetY;
                            point.y = vY * fractionNeed + targetY;
                        } else {
                            float vY = targetY - y;
                            point.y = y + vY * fraction;
                        }
                        return point;
                    }
                });
                valueAnimator.start();
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        PointF point = (PointF) valueAnimator.getAnimatedValue();
                        float vX = point.x;
                        float vY = point.y;
                        //说明vx 最大值就是view原坐标
                        if (flagX) {
                            if (vX <= x && vX >= targetX) {
                                view.setX(vX);
                            }
                        } else { //说明vx 最小值就是view原坐标
                            if (vX >= x && vX <= targetX) {
                                view.setX(vX);
                            }
                        }

                        //说明vY 最大值就是view原坐标
                        if (flagY) {
                            if (vY <= y && vY >= targetY) {
                                view.setY(vY);
                            }
                        } else { //说明vx 最小值就是view原坐标
                            if (vY >= y && vY <= targetY) {
                                view.setY(vY);
                            }
                        }
                    }
                });
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        int path = R.mipmap.ic_deep_sea_fishing_net_effects;
                        if (type == 1) {
                            path = R.mipmap.ic_deep_sea_fishing_net_effects;
                        } else if (type == 2) {
                            path = R.mipmap.ic_dinghai_fishing_net_effects;
                        } else if (type == 3) {
                            path = R.mipmap.ic_donghai_fishing_net_effects;
                        }
                        ImageUtils.loadEffect(parent.getContext(), view, path, 2, new ImageUtils.LoadEffectCallback() {
                            @Override
                            public void onAnimationEnd() {
                                view.setVisibility(View.GONE);
                                parent.removeView(view);
                                if (isLast){
                                    fishingAnimaListener.fishingAnimaComplete();
                                }
                            }
                        });

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
        });

    }

    public interface FishingAnimaListener {
        void fishingAnimaComplete();
    }
}
