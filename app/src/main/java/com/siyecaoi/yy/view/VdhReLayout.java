package com.siyecaoi.yy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.siyecaoi.yy.view.swipeView.ViewDragHelper;

import java.util.ArrayList;
import java.util.List;


public class VdhReLayout extends RelativeLayout {
    ViewDragHelper dragHelper;

    List<View> listView;

    public VdhReLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        listView = new ArrayList<>();
        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                for (View v : listView) {
                    if (v == child) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

//            // 当前被捕获的View释放之后回调
//            @Override
//            public void onViewReleased(View releasedChild, float xvel, float yvel) {
////                if (releasedChild == autoBackView) {
////                    dragHelper.settleCapturedViewAt(autoBackViewOriginLeft, autoBackViewOriginTop);
////                    invalidate();
////                }
//            }

            //这个地方实际上left就代表 你将要移动到的位置的坐标。返回值就是最终确定的移动的位置。
            // 我们要让view滑动的范围在我们的layout之内
            //实际上就是判断如果这个坐标在layout之内 那我们就返回这个坐标值。
            //如果这个坐标在layout的边界处 那我们就只能返回边界的坐标给他。不能让他超出这个范围
            //除此之外就是如果你的layout设置了padding的话，也可以让子view的活动范围在padding之内的.

//            @Override
//            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                //取得左边界的坐标
//                final int leftBound = getPaddingLeft();
//                //取得右边界的坐标
//                final int rightBound = getWidth() - child.getWidth() - leftBound;
//                //这个地方的含义就是 如果left的值 在leftbound和rightBound之间 那么就返回left
//                //如果left的值 比 leftbound还要小 那么就说明 超过了左边界 那我们只能返回给他左边界的值
//                //如果left的值 比rightbound还要大 那么就说明 超过了右边界，那我们只能返回给他右边界的值
//                return Math.min(Math.max(left, leftBound), rightBound);
//            }
//
//            //纵向的注释就不写了 自己体会
//            @Override
//            public int clampViewPositionVertical(View child, int top, int dy) {
//                final int topBound = getPaddingTop();
//                final int bottomBound = getHeight() - child.getHeight() - topBound;
//                return Math.min(Math.max(top, topBound), bottomBound);
//            }


//            @Override
//            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
//                dragHelper.captureChildView(edgeDragView, pointerId);
//            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }
        });
//        // 设置左边缘可以被Drag
//        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //决定是否拦截当前事件
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //处理事件
        dragHelper.processTouchEvent(event);
        return true;
    }

//    @Override
//    public void computeScroll() {
//        if (dragHelper.continueSettling(true)) {
//            invalidate();
//        }
//    }

//    View dragView;
//    View edgeDragView;
//    View autoBackView;

    /**
     * 添加View
     *
     * @param view
     */
    public void addView(View view) {
        if (listView == null) {
            listView = new ArrayList<>();
        }
        if (!listView.contains(view)) {
            listView.add(view);
        }
    }

    /**
     * 删除View
     *
     * @param view
     */
    public void delView(View view) {
        if (listView == null)
            return;
        listView.remove(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        dragView = findViewById(R.id.dragView);
//        edgeDragView = findViewById(R.id.edgeDragView);
//        autoBackView = findViewById(R.id.autoBackView);
    }

//    int autoBackViewOriginLeft;
//    int autoBackViewOriginTop;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        autoBackViewOriginLeft = autoBackView.getLeft();
//        autoBackViewOriginTop = autoBackView.getTop();
    }

}
