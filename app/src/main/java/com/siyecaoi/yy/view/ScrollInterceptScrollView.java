package com.siyecaoi.yy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/** 处理recyclerview嵌套及下滑颜色渐变
 * Created by Administrator on 2018/2/11.
 */

public class ScrollInterceptScrollView extends ScrollView {
    private int downX, downY;
    private int mTouchSlop;
    //嵌套处理
    public ScrollInterceptScrollView(Context context) {
        this(context, null);
    }

    public ScrollInterceptScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollInterceptScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollInterceptScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                // 判断是否滑动，若滑动就拦截事件
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }


    /**
     * 回调接口监听事件   颜色渐变
     */
    private OnObservableScrollViewListener mOnObservableScrollViewListener;


    /**
     * 添加回调接口，便于把滑动事件的数据向外抛
     */
    public interface OnObservableScrollViewListener {
        void onObservableScrollViewListener(int l, int t, int oldl, int oldt);
    }

    /**
     * 注册回调接口监听事件
     *
     * @param onObservableScrollViewListener
     */
    public void setOnObservableScrollViewListener(OnObservableScrollViewListener onObservableScrollViewListener) {
        this.mOnObservableScrollViewListener = onObservableScrollViewListener;
    }

    /**
     * This is called in response to an internal scroll in this view (i.e., the
     * view scrolled its own contents). This is typically as a result of
     * {@link #scrollBy(int, int)} or {@link #scrollTo(int, int)} having been
     * called.
     *
     * @param l Current horizontal scroll origin. 当前滑动的x轴距离
     * @param t Current vertical scroll origin. 当前滑动的y轴距离
     * @param oldl Previous horizontal scroll origin. 上一次滑动的x轴距离
     * @param oldt Previous vertical scroll origin. 上一次滑动的y轴距离
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnObservableScrollViewListener != null) {
            //将监听到的数据向外抛
            mOnObservableScrollViewListener.onObservableScrollViewListener(l, t, oldl, oldt);
        }
    }

}
