package cn.sinata.xldutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.AspectRatioMeasure;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.MultiDraweeHolder;
import cn.sinata.xldutils.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *  网格展示多张图片的ImageView，图片通过Freso获取展示，调用setUrls(List<String> urls)设置数据源。
 * Created by liaoxiang on 16/7/27.
 */
@SuppressWarnings("unused")
public class MultiImageView extends View{
    private final AspectRatioMeasure.Spec mMeasureSpec = new AspectRatioMeasure.Spec();
    private float mAspectRatio = 1.0f;//默认正方形
    @SuppressWarnings("SpellCheckingInspection")
    private MultiDraweeHolder<GenericDraweeHierarchy> multiDraweeHolder;
    private boolean mInitialised = false;
    private Context mContext;
    private int padding = 0;
    private ArrayList<Drawable> drawables = new ArrayList<>();
    boolean mIsCapturingGesture;
    boolean mIsClickCandidate;
    long mActionDownTime;
    float mActionDownX;
    float mActionDownY;
    float mSingleTapSlopPx;

    public MultiImageView(Context context) {
        super(context);
        init(context);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /** This method is idempotent so it only has effect the first time it's called */
    private void init(Context context) {
        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mSingleTapSlopPx = viewConfiguration.getScaledTouchSlop();
        mContext = context;
        if (mInitialised) {
            return;
        }
        padding = DensityUtil.dip2px(context,2);
        mInitialised = true;

        multiDraweeHolder = new MultiDraweeHolder<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = multiDraweeHolder.size();
        int usedW = 0;
        int usedH = 0;
        drawables.clear();
        for (int i = 0; i < multiDraweeHolder.size(); ++i) {
            Drawable drawable = multiDraweeHolder.get(i).getTopLevelDrawable();
            if (drawable != null) {
                if (size == 1){
                    int w = getWidth();
                    int h = getHeight();
                    if (h<=0){
                        h = w;
                    }
                    drawable.setBounds(usedW,usedH,w+usedW,h);
                }else if (size < 4){
                    int w = (getWidth()- (padding*(size-1)))/size;
                    drawable.setBounds(usedW,usedH,w+usedW,w+usedH);
                    usedW += w+padding;
                }else if (size == 4){
                    //行首重置x坐标
                    if (i%2==0){
                        usedW = 0;
                    }
                    int w = (getWidth()-padding)/2;
                    usedH = (i/2)*(w+padding);
                    drawable.setBounds(usedW,usedH,w+usedW,w+usedH);
                    usedW += w+padding;
                }else {
                    //行首重置x坐标
                    if (i%3==0){
                        usedW = 0;
                    }
                    int w = (getWidth()- (padding*2))/3;
                    usedH = (i/3)*(w+padding);
                    drawable.setBounds(usedW,usedH,w+usedW,w+usedH);
                    usedW += (w+padding);
                }
                drawables.add(drawable);
            }
        }
        multiDraweeHolder.draw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onAttach();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDetach();
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        onDetach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        onAttach();
    }

    /** Called by the system to attach. Subclasses may override. */
    protected void onAttach() {
        doAttach();
    }

    /**  Called by the system to detach. Subclasses may override. */
    protected void onDetach() {
        doDetach();
    }

    /**
     * Does the actual work of attaching.
     *
     * Non-test subclasses should NOT override. Use onAttach for custom code.
     */
    protected void doAttach() {
        multiDraweeHolder.onAttach();
    }

    /**
     * Does the actual work of detaching.
     *
     * Non-test subclasses should NOT override. Use onDetach for custom code.
     */
    protected void doDetach() {
        drawables.clear();
        int size = multiDraweeHolder.size();
        for (int i = 0; i < size; i++) {
            multiDraweeHolder.get(i).getTopLevelDrawable().setCallback(null);
        }
        multiDraweeHolder.onDetach();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击重新加载图片，如果开启需要打开此段代码
//        if (multiDraweeHolder.onTouchEvent(event)) {
//            return true;
//        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsCapturingGesture = true;
                mIsClickCandidate = true;
                mActionDownTime = event.getEventTime();
                mActionDownX = event.getX();
                mActionDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - mActionDownX) > mSingleTapSlopPx ||
                        Math.abs(event.getY() - mActionDownY) > mSingleTapSlopPx) {
                    mIsClickCandidate = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsCapturingGesture = false;
                mIsClickCandidate = false;
                break;
            case MotionEvent.ACTION_UP:
                mIsCapturingGesture = false;
                if (Math.abs(event.getX() - mActionDownX) > mSingleTapSlopPx ||
                        Math.abs(event.getY() - mActionDownY) > mSingleTapSlopPx) {
                    mIsClickCandidate = false;
                }
                if (mIsClickCandidate) {
                    if (event.getEventTime() - mActionDownTime <= ViewConfiguration.getLongPressTimeout()) {
                        clickCheck(event,0);
                    } else {
                        //长按
                        clickCheck(event,1);
                    }
                }
                mIsClickCandidate = false;
                break;
        }
        return true;
    }

    private void clickCheck(MotionEvent event,int type){
        //检查点击区域
        if (drawables.size()>0){
            for (int i = 0; i < drawables.size(); i++) {
                if (drawables.get(i).getBounds().contains((int)event.getX(),(int)event.getY())){
                    if (type == 0){//单击
                        if (imageClickListener!=null){
                            imageClickListener.onClick(i);
                            break;
                        }
                    }else {//长按
                        if (imageLongClickListener!=null){
                            imageLongClickListener.onLongClick(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the desired aspect ratio (w/h).
     */
    public void setAspectRatio(float aspectRatio) {
        if (aspectRatio == mAspectRatio) {
            return;
        }
        mAspectRatio = aspectRatio;
        requestLayout();
    }

    /**
     * Gets the desired aspect ratio (w/h).
     */
    public float getAspectRatio() {
        return mAspectRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureSpec.width = widthMeasureSpec;
        mMeasureSpec.height = heightMeasureSpec;
        updateMeasureSpec(
                mMeasureSpec,
                mAspectRatio,
                getLayoutParams(),
                getPaddingLeft() + getPaddingRight(),
                getPaddingTop() + getPaddingBottom());
        super.onMeasure(mMeasureSpec.width, mMeasureSpec.height);
    }

    private void updateMeasureSpec(
            AspectRatioMeasure.Spec spec,
            float aspectRatio,
            @Nullable ViewGroup.LayoutParams layoutParams,
            int widthPadding,
            int heightPadding) {
        if (aspectRatio <= 0 || layoutParams == null) {
            return;
        }
        int size = multiDraweeHolder.size();
        if (shouldAdjust(layoutParams.height)) {
            int widthSpecSize = MeasureSpec.getSize(spec.width);
            int desiredHeight = 0;
            if (size == 1 || size == 4 || size > 6){
                desiredHeight = (int) ((widthSpecSize - widthPadding) / aspectRatio + heightPadding);
            }else if (size == 2 ){
                desiredHeight = (int) ((widthSpecSize - widthPadding - padding)/ 2 / aspectRatio + heightPadding);
            }else if (size == 3){
                desiredHeight = (int) ((widthSpecSize - widthPadding - padding)/ 3 / aspectRatio + heightPadding);
            }else if (size<7){
                desiredHeight = (int) ((widthSpecSize - widthPadding - padding)/ 3 / aspectRatio*2 + padding + heightPadding);
            }
            int resolvedHeight = View.resolveSize(desiredHeight, spec.height);
            spec.height = MeasureSpec.makeMeasureSpec(resolvedHeight, MeasureSpec.EXACTLY);
        } else if (shouldAdjust(layoutParams.width)) {
            int heightSpecSize = MeasureSpec.getSize(spec.height);
            int desiredWidth = (int) ((heightSpecSize - heightPadding) * aspectRatio + widthPadding);
            int resolvedWidth = View.resolveSize(desiredWidth, spec.width);
            spec.width = MeasureSpec.makeMeasureSpec(resolvedWidth, MeasureSpec.EXACTLY);
        }
    }

    private boolean shouldAdjust(int layoutDimension) {
        // Note: wrap_content is supported for backwards compatibility, but should not be used.
        return layoutDimension == 0 || layoutDimension == ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        super.verifyDrawable(who);
        return multiDraweeHolder.verifyDrawable(who);
    }

    public void setUrls(List<String> urls){
        if (urls==null){
            return;
        }
        multiDraweeHolder.clear();
        for (int i = 0; i < urls.size(); i++) {
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setPlaceholderImage(new ColorDrawable(Color.GRAY))
                    .setPlaceholderImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .build();
            //noinspection SpellCheckingInspection
            DraweeHolder<GenericDraweeHierarchy> mDraweeHolder = DraweeHolder.create(hierarchy, mContext);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(urls.get(i)))
                    .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                    .setOldController(mDraweeHolder.getController())
                    .build();
            mDraweeHolder.setController(controller);
            multiDraweeHolder.add(mDraweeHolder);
        }
        int size = multiDraweeHolder.size();
        for (int i = 0; i < size; i++) {
            multiDraweeHolder.get(i).getTopLevelDrawable().setCallback(this);
        }
    }



    private OnImageClickListener imageClickListener;

    public void setOnImageClickListener(OnImageClickListener imageClickListener){
        this.imageClickListener = imageClickListener;
    }
    private OnImageLongClickListener imageLongClickListener;

    public void setOnImageLongClickListener(OnImageLongClickListener imageLongClickListener){
        this.imageLongClickListener = imageLongClickListener;
    }

    public interface OnImageClickListener{
        void onClick(int position);
    }

    public interface OnImageLongClickListener{
        void onLongClick(int position);
    }
}
