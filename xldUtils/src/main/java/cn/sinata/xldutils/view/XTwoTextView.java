package cn.sinata.xldutils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sinata.xldutils.R;
import cn.sinata.xldutils.utils.DensityUtil;

/**
 *
 * Created by liaoxiang on 16/3/23.
 */
public class XTwoTextView extends LinearLayout{

    private TextView topView;
    private TextView bottomView;
    private ColorStateList topColor;
    private ColorStateList bottomColor;

    public XTwoTextView(Context context) {
        super(context);
        init(context,null);
    }

    public XTwoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public XTwoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    void init(Context context,AttributeSet attrs){
        if (isInEditMode()) {
            return;
        }
        int padding = DensityUtil.dip2px(getContext(),16);
        setPadding(0,padding,0,padding);
        setOrientation(VERTICAL);
        topView = new TextView(getContext());
        topView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        topView.setSingleLine(true);
        topView.setGravity(Gravity.CENTER);
        bottomView = new TextView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtil.dip2px(getContext(),8);
        bottomView.setLayoutParams(params);
        bottomView.setSingleLine(true);
        bottomView.setGravity(Gravity.CENTER);
        CharSequence topText=null;
        CharSequence bottomText=null;
        int topTextSize = DensityUtil.sp2px(context,14);
        int bottomTextSize = DensityUtil.sp2px(context,14);
        if (attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XTwoTextView);
            topColor = a.getColorStateList(R.styleable.XTwoTextView_topTextColor);
            bottomColor = a.getColorStateList(R.styleable.XTwoTextView_bottomTextColor);
            topText = a.getText(R.styleable.XTwoTextView_topText);
            bottomText = a.getText(R.styleable.XTwoTextView_bottomText);
            topTextSize = a.getDimensionPixelSize(R.styleable.XTwoTextView_topTextSize,topTextSize);
            bottomTextSize = a.getDimensionPixelSize(R.styleable.XTwoTextView_bottomTextSize,bottomTextSize);
            a.recycle();
        }

        topView.setTextColor(topColor != null ? topColor : ColorStateList.valueOf(0xFF333333));
        bottomView.setTextColor(bottomColor != null ? bottomColor : ColorStateList.valueOf(0xFF333333));

        if (topText!=null) {
            topView.setText(topText);
        }
//        System.err.println("bottomText-->"+bottomText);
        if (bottomText != null) {
            bottomView.setText(bottomText);
        }
        setRawTextSize(topTextSize,bottomTextSize);

        addView(topView);
        addView(bottomView);

    }

    private void setRawTextSize(int topTextSize , int bottomTextSize){
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        topView.setTextSize( TypedValue.COMPLEX_UNIT_PX,TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, topTextSize, r.getDisplayMetrics()));
        bottomView.setTextSize( TypedValue.COMPLEX_UNIT_PX,TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, bottomTextSize, r.getDisplayMetrics()));
    }

    /**
     * 设置上边文字大小
     * @param size 默认单位sp
     */
    public void setTopTextSize(int size){
        topView.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
    }
    /**
     * 设置下边文字大小
     * @param size 默认单位sp
     */
    public void setBottomTextSize(int size){
        bottomView.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
    }

    /**
     * 设置上边文字
     * @param s 文字
     */
    public void setTopText(CharSequence s){
        topView.setText(s);
    }
    /**
     * 设置下边文字
     * @param s 文字
     */
    public void setBottomText(CharSequence s){
        bottomView.setText(s);
    }

    /**
     * 设置上边文字颜色
     * @param color 颜色
     */
    public void setTopTextColor(int color){
        topView.setTextColor(color);
    }

    /**
     * 设置下边文字颜色
     * @param color 颜色
     */
    public void setBottomTextColor(int color){
        bottomView.setTextColor(color);
    }
}
