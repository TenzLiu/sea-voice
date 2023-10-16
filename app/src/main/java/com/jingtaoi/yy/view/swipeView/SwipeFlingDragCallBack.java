package com.jingtaoi.yy.view.swipeView;

import android.util.Log;
import android.view.View;

/**
 * 拖拽事件回调处理
 *
 * @zc
 */
class SwipeFlingDragCallBack extends ViewDragHelper.Callback {

    private final static String TAG = SwipeFlingDragCallBack.class.getSimpleName();
    private SwipeFlingView mDragView;

    SwipeFlingDragCallBack(SwipeFlingView view) {
        this.mDragView = view;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top,
                                      int dx, int dy) {
        mDragView.onViewPositionChanged(changedView, left, top, dx, dy);
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        Log.d(TAG, "tryCaptureView");
        return mDragView.tryCaptureView(child, pointerId);
    }

    @Override
    public void onViewCaptured(View capturedChild, int activePointerId) {
        mDragView.onViewCaptured(capturedChild, activePointerId);
    }

    @Override
    public void onViewDragStateChanged(int state) {
        super.onViewDragStateChanged(state);
    }


    @Override
    public int getViewHorizontalDragRange(View child) {
        // 这个用来控制拖拽过程中松手后，自动滑行的速度
        return 0;
    }

    @Override
    public int getViewVerticalDragRange(View child) {
        return super.getViewVerticalDragRange(child);
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
        mDragView.onViewReleased(releasedChild, xvel, yvel);
    }

    @Override
    public int clampViewPositionHorizontal(View child, int left, int dx) {
        return left;
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        return top;
    }
}
