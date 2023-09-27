package cn.sinata.xldutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cn.sinata.xldutils.utils.Utils;

/**
 * 裁剪视图
 * @author sinata
 */
public class ClipImageBorderView extends View{
	
	public enum Mode{
		Circle,Rect
	}
	
	private Mode mode=Mode.Rect;//默认矩形模式
	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding;
	/**
	 * 垂直方向与View的边距
	 */
	private int mVerticalPadding;
	/**
	 * 绘制的矩形的宽度
	 */
	private int mWidth;
	/**
	 * 边框的颜色，默认为白色
	 */
	private int mBorderColor = Color.parseColor("#FFFFFF");
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 1;

	private Paint mPaint;
	private float scale=1;

	public ClipImageBorderView(Context context)
	{
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	
		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
//		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		// 计算矩形区域的宽度
		mWidth = getWidth() - 2 * mHorizontalPadding;
		// 计算距离屏幕垂直边界 的边距
		mVerticalPadding = (getHeight() - (int)(mWidth/scale)) / 2;
		mPaint.setColor(Color.parseColor("#aa000000"));
		mPaint.setStyle(Style.FILL);
		
		if (mode==Mode.Circle) {
//			drawRect(canvas);
//			canvas.translate(mHorizontalPadding, mVerticalPadding);
			drawLiftUp(canvas);
			drawRightUp(canvas);
			drawLiftDown(canvas);
			drawRightDown(canvas);
			// 绘制外边框
			mPaint.setColor(mBorderColor);
			mPaint.setStrokeWidth(mBorderWidth);
			mPaint.setStyle(Style.STROKE);
			canvas.drawCircle(getWidth()/2, getHeight()/2, mWidth/2, mPaint);
		}else {
			// 绘制左边1
			canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
				// 绘制右边2
			canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
					getHeight(), mPaint);
			// 绘制上边3
			canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding,
					mVerticalPadding, mPaint);
			// 绘制下边4
			canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
					getWidth() - mHorizontalPadding, getHeight(), mPaint);
			// 绘制外边框
			mPaint.setColor(mBorderColor);
			mPaint.setStrokeWidth(mBorderWidth);
			mPaint.setStyle(Style.STROKE);
			canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
					- mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
		}
	}

	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
		
	}
	/**
	 * 设置宽高比
	 * @param scale
	 */
	public void setImageScale(float scale){
		this.scale=scale;
	}
	/**
	 * 设置裁剪显示区域模式圆或矩形。默认矩形。
	 * @param mode
	 */
	public void setMode(Mode mode){
		this.mode=mode;
	}
	
//	private void drawRect(Canvas canvas) {
//		Path path = new Path();
//		path.setFillType(FillType.INVERSE_WINDING);
//		path.addRect(mHorizontalPadding, mVerticalPadding, getWidth()-mHorizontalPadding, getHeight()-mVerticalPadding, Direction.CCW);
//		canvas.drawPath(path, mPaint);
//	}
	private void drawLiftUp(Canvas canvas) {
		
		Path path = new Path();
		path.moveTo(getWidth()/2, mVerticalPadding);
		path.lineTo(getWidth()/2, 0);
		path.lineTo(0, 0);
		path.lineTo(0, getHeight()/2);
		path.lineTo(mHorizontalPadding, getHeight()/2);
		path.arcTo(new RectF(
				mHorizontalPadding, 
				mVerticalPadding, 
				getWidth()-mHorizontalPadding, 
				getHeight()-mVerticalPadding), 
				180, 
				90);
		path.close();
		canvas.drawPath(path, mPaint);

	}
	private void drawLiftDown(Canvas canvas) {
		Path path = new Path();
		path.moveTo(mHorizontalPadding, getHeight()/2);
		path.lineTo(0, getHeight()/2);
		path.lineTo(0, getHeight());
		path.lineTo(getWidth()/2, getHeight());
		path.lineTo(getWidth()/2, getHeight()-mVerticalPadding);
		path.arcTo(new RectF(
				mHorizontalPadding, 
				mVerticalPadding, 
				getWidth()-mHorizontalPadding, 
				getHeight()-mVerticalPadding), 
				90, 
				90);
		path.close();
		canvas.drawPath(path, mPaint);
	}
	
	private void drawRightDown(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth()/2, getHeight()-mVerticalPadding);
		path.lineTo(getWidth()/2, getHeight());
		path.lineTo(getWidth(), getHeight());
		path.lineTo(getWidth(), getHeight()/2);
		path.lineTo(getWidth()-mHorizontalPadding, getHeight()/2);
		path.arcTo(new RectF(
				mHorizontalPadding, 
				mVerticalPadding, 
				getWidth()-mHorizontalPadding, 
				getHeight()-mVerticalPadding), 
				0, 
				90);
		path.close();
		canvas.drawPath(path, mPaint);
	}
	
	private void drawRightUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth()-mHorizontalPadding, getHeight()/2);
		path.lineTo(getWidth(), getHeight()/2);
		path.lineTo(getWidth(), 0);
		path.lineTo(getWidth()/2, 0);
		path.lineTo(getWidth()/2, mVerticalPadding);
		path.arcTo(new RectF(
				mHorizontalPadding, 
				mVerticalPadding, 
				getWidth()-mHorizontalPadding, 
				getHeight()-mVerticalPadding), 
				270, 
				90);
		path.close();
		canvas.drawPath(path, mPaint);
	}
}
