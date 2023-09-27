package cn.sinata.xldutils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sinata.xldutils.R;

/**
 * 加减view，购物车等处使用，简单的组合按钮而已
 */
public class AddSubView extends LinearLayout implements View.OnClickListener{

    private ImageButton addView;//加
    private ImageButton subView;//减
    private TextView numView;//显示数字view
    private int step = 1;//步长，默认为1
    private int min = 0;//最小值，默认为1
    private int max = 99;//最大值，默认为99
    private int number = 0;
    private float textSize = 14;
    private int textColor = Color.parseColor("#333333");

    public AddSubView(Context context) {
        super(context);
        init(context,null);
    }

    public AddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddSubView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){

        addView = new ImageButton(context);
        subView = new ImageButton(context);
        numView = new TextView(context);
        int itemWidth = 30;
        int itemHeight = 30;
        int subBg = 0;
        int addBg = 0;
        int subSrc = 0;
        int addSrc = 0;
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AddSubView);
            itemWidth= a.getDimensionPixelSize(R.styleable.AddSubView_itemWidth, itemWidth);
            itemHeight= a.getDimensionPixelSize(R.styleable.AddSubView_itemHeight, itemHeight);
            max= a.getInt(R.styleable.AddSubView_max, max);
            min= a.getInt(R.styleable.AddSubView_min, min);
            subBg = a.getResourceId(R.styleable.AddSubView_subBackground, 0);
            subSrc = a.getResourceId(R.styleable.AddSubView_subSrc, 0);
            addBg = a.getResourceId(R.styleable.AddSubView_addBackground, 0);
            addSrc = a.getResourceId(R.styleable.AddSubView_addSrc, 0);
            a.recycle();
        }else {
            float density = context.getResources().getDisplayMetrics().density;
            itemWidth = (int) (itemWidth*density);
            itemHeight = (int) (itemHeight*density);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth,itemHeight);
        subView.setLayoutParams(params);
        if (subSrc >0){

            subView.setImageResource(subSrc);
        }
        if (subBg>0){
            subView.setBackgroundResource(subBg);
        }
        if (addBg>0){
            addView.setBackgroundResource(addBg);
        }
        if (addSrc >0){

            addView.setImageResource(addSrc);
        }

        addView.setLayoutParams(params);
        numView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,itemHeight));
        numView.setMinWidth(itemWidth);
        numView.setText(String.valueOf(min));
        if (min<0){
            min = 0;
        }
        number = min;
        numView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        numView.setTextColor(textColor);
        numView.setGravity(Gravity.CENTER);

        addView(subView);
        addView(numView);
        addView(addView);

        if (number <= 0){
            subView.setEnabled(false);
        }else {
            subView.setEnabled(true);
        }

        subView.setOnClickListener(this);
        addView.setOnClickListener(this);
    }

    public void setMaxNumber(int max){
        this.max = max;
        if (number>=max){
            number = max;
            addView.setEnabled(false);
        }else {
            addView.setEnabled(true);
        }
    }

    public void setMinNumber(int min){
        this.min = min;
        if (number<=min){
            number = min;
            subView.setEnabled(false);
        }else {
            subView.setEnabled(true);
        }
    }

    public void setNumber(int number){
        this.number = number;
        numView.setText(String.valueOf(number));
        if (number>=max){
            this.number = max;
            addView.setEnabled(false);
        }else {
            addView.setEnabled(true);
        }

        if (number<=min){
            this.number = min;
            subView.setEnabled(false);
        }else {
            subView.setEnabled(true);
        }
    }

    public int getNumber(){
        return number;
    }

    public void setOnNumberChangeListener(OnNumberChangeListener listener){
        this.listener = listener;
    }

    public interface OnNumberChangeListener{
        void onNumberChange(Type type, int num);
    }

    public enum Type{
        ADD,SUB
    }

    private OnNumberChangeListener listener;

    @Override
    public void onClick(View v) {
        if (v == subView){
            if (number>min) {
                number -= step;
                if (listener != null) {
                    listener.onNumberChange(Type.SUB,number);
                }
            }
        }else if (v == addView){
            if (number<max) {
                number += step;
                if (listener != null) {
                    listener.onNumberChange(Type.ADD,number);
                }
            }
        }

        if (number<=min){
            number = min;
            subView.setEnabled(false);
        }else {
            subView.setEnabled(true);
        }
        if (number>=max){
            number = max;
            addView.setEnabled(false);
        }else {
            addView.setEnabled(true);
        }
        numView.setText(String.valueOf(number));
    }
}
