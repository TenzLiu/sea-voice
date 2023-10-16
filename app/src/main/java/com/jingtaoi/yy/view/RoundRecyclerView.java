package com.jingtaoi.yy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.jingtaoi.yy.R;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 *
 * 上直角底部直角
 *
 */
public class RoundRecyclerView extends RecyclerView {

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
//    private float[] mRids = {200.0f,200.0f,
//                             200.0f,200.0f,
//                             0.0f,0.0f,
//                             0.0f,0.0f,};

    private Path mPath;
    private Paint mPaint;
    private RectF mRectF;
    private float mRadius;
    private boolean isClipBackground;

    public RoundRecyclerView(@NonNull Context context) {
        this(context, null);
    }
    public RoundRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RoundRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRelativeLayout);
        mRadius = ta.getDimension(R.styleable.RoundRelativeLayout_rlRadius, 0);
        isClipBackground = ta.getBoolean(R.styleable.RoundRelativeLayout_rlClipBackground, true);
        ta.recycle();

        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }
    public void setRadius(float radius) {
        mRadius = radius;
        postInvalidate();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(0, 0, w, h);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= 28) {
            draw28(canvas);
        } else {
            draw27(canvas);
        }
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= 28) {
            dispatchDraw28(canvas);
        } else {
            dispatchDraw27(canvas);
        }
    }
    private void draw27(Canvas canvas) {
        if (isClipBackground) {
            canvas.saveLayer(mRectF, null, Canvas.ALL_SAVE_FLAG);
            super.draw(canvas);
            canvas.drawPath(genPath(), mPaint);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }
    private void draw28(Canvas canvas) {
        if (isClipBackground) {
            canvas.save();
            canvas.clipPath(genPath());
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }
    private void dispatchDraw27(Canvas canvas) {
        canvas.saveLayer(mRectF, null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.drawPath(genPath(), mPaint);
        canvas.restore();
    }
    private void dispatchDraw28(Canvas canvas) {
        canvas.save();
        canvas.clipPath(genPath());
        super.dispatchDraw(canvas);
        canvas.restore();
    }
    private Path genPath() {
        mPath.reset();
        mPath.addRoundRect(mRectF, mRadius, mRadius, Path.Direction.CW);  // 四周圆角
//        mPath.addRoundRect(mRectF, takeRadiusData(mRadius), Path.Direction.CW);               // 底部直角
        return mPath;
    }

    private float[] takeRadiusData(float fValue){
        float[] mRids = new float[8];

        mRids[0] = fValue;
        mRids[1] = fValue;
        mRids[2] = fValue;
        mRids[3] = fValue;
        mRids[4] = 0f;
        mRids[5] = 0f;
        mRids[6] = 0f;
        mRids[7] = 0f;

        return mRids;
    }
}