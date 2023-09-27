package cn.sinata.xldutils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.widget.autoscoll.AutoScrollViewPager;


/**
 * banner
 * Created by liaoxiang on 16/3/17.
 */
public class BannerLayout extends FrameLayout implements ViewPager.OnPageChangeListener {
    private AutoScrollViewPager autoScrollViewPager;
    private LinearLayout indicator;
    private ImageView[] indicatorViews;
    private String indicatorBgColor = "#7F000000";
    private int indicatorHeight = 30;//默认高度，单位dip
    private float aspect = -1f;
    private int interval = 5*1000;//5秒
    private boolean showIndicator = true;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mDataSetObserver;

    public BannerLayout(Context context) {
        super(context);
        init(context,null);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    void init(Context context,AttributeSet attrs){
        //如果pageAdapter调用notifyDataSetChanged();则重新绘制圆点
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showIndicator(showIndicator);
                if (autoScrollViewPager!=null){
                    autoScrollViewPager.notifyDataSetChanged();
                }
            }
        };

        if (attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerLayout);
            aspect= a.getFloat(R.styleable.BannerLayout_aspect, aspect);
            a.recycle();
        }

        autoScrollViewPager = new AutoScrollViewPager(getContext());
        autoScrollViewPager.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        autoScrollViewPager.setInterval(interval);
//        autoScrollViewPager.setBorderAnimation(false);
        autoScrollViewPager.setBoundaryCaching(false);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
        autoScrollViewPager.setOffscreenPageLimit(6);
        //noinspection deprecation
        autoScrollViewPager.setOnPageChangeListener(this);
        autoScrollViewPager.setBackgroundColor(Color.GRAY);
        addView(autoScrollViewPager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (aspect>0) {
            setMeasuredDimension(sizeWidth, (int) (sizeWidth/aspect));
            measureChildren(widthMeasureSpec,heightMeasureSpec);
        }
        else {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        System.err.println("--onLayout--");
        if (aspect>0){
            if (autoScrollViewPager.getLayoutParams()!=null && autoScrollViewPager.getLayoutParams().height<=0){
                int width = getMeasuredWidth();
                //重设高度
                autoScrollViewPager.getLayoutParams().height = (int) (width/aspect);
            }
        }
    }

    /**
     * 设置数据adapter
     * @param pagerAdapter pagerAdapter
     */
    public void setAdapter(PagerAdapter pagerAdapter){
        if (mPagerAdapter!=null){
            mPagerAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mPagerAdapter = pagerAdapter;
        if (pagerAdapter!=null) {

            mPagerAdapter.registerDataSetObserver(mDataSetObserver);
            autoScrollViewPager.setAdapter(mPagerAdapter);
            showIndicator(showIndicator);
        }

    }

    /**
     * 开始自动滚动
     */
    public void startAutoScroll(){
        autoScrollViewPager.startAutoScroll();
    }

    /**
     * 停止自动滚动
     */
    public void stopAutoScroll(){
        autoScrollViewPager.stopAutoScroll();
    }

    public void showIndicator(boolean show){
        showIndicator = show;
        if (show){
            if (indicator !=null){
                removeView(indicator);
                indicator = null;
            }
            createIndicatorLayout();
        }else {
            if (indicator!=null){
                removeView(indicator);
                indicator = null;
            }
        }

    }

    public void setCurrentItem(int position){
        autoScrollViewPager.setCurrentItem(position,false);
    }

//    public void setMode(int mode){
////        autoScrollViewPager.setSlideBorderMode(mode);
//    }
    public void setBoundaryLooping(boolean looping){
//        autoScrollViewPager.setSlideBorderMode(mode);
        autoScrollViewPager.setBoundaryLooping(looping);
    }

    public void setAspect(float aspect){
        this.aspect = aspect;
        invalidate();
    }

    private void createIndicatorLayout(){
        indicator = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,dip2px(indicatorHeight));
        params.gravity = Gravity.BOTTOM;
        indicator.setLayoutParams(params);
        indicator.setGravity(Gravity.CENTER);
        indicator.setOrientation(LinearLayout.HORIZONTAL);
        indicator.setBackgroundColor(Color.parseColor(indicatorBgColor));
        addIndicator();
        //添加小圆点
        addView(indicator);
    }

    private void addIndicator(){
        PagerAdapter pagerAdapter = autoScrollViewPager.getAdapter();
        if (pagerAdapter!=null){
            int count = pagerAdapter.getCount();
//            System.err.println("---->"+count);
            indicatorViews = new ImageView[count];
            for (int i = 0;i<count;i++){
                ImageView view = new ImageView(getContext());
                indicatorViews[i] = view;
                LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(dip2px(5),dip2px(5));
                params.rightMargin = dip2px(5);
                view.setLayoutParams(params);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setBackgroundResource(R.drawable.page_indicator);
                if (i == 0){
                    view.setSelected(true);
                }
                indicator.addView(view);
            }
        }
    }

    private int dip2px(int dip){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * scale + 0.5f);
    }

    /**
     * 设置选中的tip的背景
     * @param selectItems 选中项
     */
    private void setImageBackground(int selectItems){
        if (indicatorViews!=null&& indicatorViews.length>0) {
            for (int i = 0; i < indicatorViews.length; i++) {
                if (i == selectItems) {
                    indicatorViews[i].setSelected(true);
                } else {
                    indicatorViews[i].setSelected(false);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (indicator!=null){
            setImageBackground(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
