package cn.sinata.xldutils.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import cn.sinata.xldutils.R;

import cn.sinata.xldutils.view.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 标题栏
 * Created by liaoxiang on 16/3/22.
 */
public class TitleBar extends FrameLayout{

    private boolean hasLeft = false;
    private TextView leftView;
    private EditText titleView;
    private List<View> rightViews = new ArrayList<>();

    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_title_bar,this,true);
        ViewHolder viewHolder = new ViewHolder(this);
        leftView = viewHolder.bind(R.id.leftButton);
        titleView = viewHolder.bind(R.id.titleView);
        //默认不可输入
        titleView.setInputType(InputType.TYPE_NULL);
        titleView.setEnabled(false);
        //默认白色
        titleView.setBackgroundDrawable(null);
//        ViewUtils.setBoundsViewOutlineProvider(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int lw = 0;
        if (hasLeft) {
            lw = leftView.getMeasuredWidth();
        }
        int rw = 0;
        if (isHasRight()) {
            for (View view:rightViews){
                if (view.getVisibility() != GONE) {
                    int rightMargin = ((LayoutParams) view.getLayoutParams()).rightMargin;
                    int temp = rw ;
                    if (rightMargin!=temp){
                        ((LayoutParams) view.getLayoutParams()).rightMargin = temp;
                        view.requestLayout();
                    }
                    rw += view.getMeasuredWidth()+5;
                }
            }
        }
        int leftMargin = ((LayoutParams) titleView.getLayoutParams()).leftMargin;
        int rightMargin = ((LayoutParams) titleView.getLayoutParams()).rightMargin;
        int newMargin = Math.max(lw, rw)+5;
//        Utils.systemErr("onlayout-------"+leftMargin+"--->"+rightMargin);
        if (leftMargin != newMargin || rightMargin != newMargin){
            ((LayoutParams) titleView.getLayoutParams()).leftMargin = newMargin;
            ((LayoutParams) titleView.getLayoutParams()).rightMargin = newMargin;
            titleView.requestLayout();
        }

    }

    public void showLeft(boolean show){
        hasLeft = show;
        leftView.setVisibility(show?VISIBLE:GONE);
    }

    public void setLeftButtonText(String text){
        if (leftView!=null){
            leftView.setText(text);
        }
    }

    public void setLeftButton(String title){
        setLeftButton(title, 0, new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).onBackPressed();
            }
        });
    }
    public void setLeftButton(OnClickListener onClickListener){
        setLeftButton(null,0,onClickListener);
    }
    public void setLeftButton(String title,OnClickListener onClickListener){
        setLeftButton(title,0,onClickListener);
    }
    public void setLeftButton( int left, OnClickListener onClickListener){
        setLeftButton(null,left,0,onClickListener);
    }
    public void setLeftButton(String title, int left, OnClickListener onClickListener){
        setLeftButton(title,left,0,onClickListener);
    }

    public void setLeftButton(String title, int leftId,int rightId, OnClickListener onClickListener){
        if (title!=null){
            leftView.setText(title);
        }
        Drawable left =null;
        if (leftId>0){
            left = getResources().getDrawable(leftId);
        }
        Drawable right =null;
        if (rightId>0){
            right = getResources().getDrawable(rightId);
        }
        if (left!=null | right!=null) {
            leftView.setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);
        }
        if (onClickListener!=null){
            leftView.setOnClickListener(onClickListener);
        }
        showLeft(true);
    }

    public void setCanEditable(boolean canEditable){
        if (canEditable){
            titleView.setInputType(InputType.TYPE_CLASS_TEXT);
            titleView.setEnabled(true);
        }
    }

    public void setTitle(CharSequence s){
        titleView.setText(s);
    }
    public void setTitleColor(int color){
        titleView.setTextColor(ContextCompat.getColor(getContext(),color));
    }

    public EditText getTitleView(){
        return titleView;
    }
    public void setTitle(CharSequence s,float size){
        titleView.setText(s);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
    }

    public void setTitleBackground(int resId){
        titleView.setBackgroundResource(resId);
    }

    public void setTitleTextColor(int color){
        titleView.setTextColor(getResources().getColor(color));
    }

    public void setTitleOnClick(OnClickListener onClickListener){
        titleView.setOnClickListener(onClickListener);
    }

    public void addRightButton(String title,OnClickListener onClickListener){
        addRightButton(title,0,onClickListener);
    }
    public void addRightButton(int right,OnClickListener onClickListener){
        addRightButton(null,right,onClickListener);
    }

    public void addRightButton(String title,int rightId,OnClickListener onClickListener){
//        hasRight = true;
        int padding = dip2px(16);
        int w= 0;
        for (View view :rightViews){
            if (view.getVisibility() != GONE) {
                w += view.getMeasuredWidth() + 5;
            }
        }

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        params.rightMargin = w;
        params.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
        TextView rightView = new TextView(getContext());
        if (title!=null) {
            rightView.setText(title);
        }
        rightView.setTextColor(Color.parseColor("#333333"));
        rightView.setPadding(padding/2,0,padding,0);
        rightView.setGravity(Gravity.CENTER_VERTICAL);
        rightView.setLayoutParams(params);
        Drawable right = null;
        if (rightId>0){
            right = ContextCompat.getDrawable(getContext(),rightId);
        }
        rightView.setCompoundDrawablesWithIntrinsicBounds(null,null,right,null);
        rightView.setOnClickListener(onClickListener);
        addView(rightView);
        rightViews.add(rightView);
    }

    public void setRightButton(int position,String title,int resId){
        if (isHasRight() && rightViews.size()>position && position>0){

            ((TextView)(rightViews.get(position))).setText(title);
            if (resId>0) {
                Drawable drawable = ContextCompat.getDrawable(getContext(),resId);
                ((TextView)(rightViews.get(position))).setCompoundDrawablesWithIntrinsicBounds(null, drawable,null,null);
            }

        }
    }


    public void setRightButtonText(int position,String title){
        if (isHasRight() && rightViews.size()>position && position>0){
            ((TextView)(rightViews.get(position))).setText(title);
        }
    }

    public TextView getRightButton(int position){
        View view = rightViews.get(position);
        if (view instanceof TextView){
            return (TextView)view;
        }
        return new TextView(getContext());
    }

    public void hideRightButton(int position,boolean hide){
        if (isHasRight() && rightViews.size()>position && position>=0){
            rightViews.get(position).setVisibility(hide?GONE:VISIBLE);
        }
    }

    public void hideAllRightButton(){
        if (isHasRight()){
            for (View view:rightViews){
                view.setVisibility(GONE);
            }
        }
    }

    private int dip2px(int dip){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * scale + 0.5f);
    }

    private boolean isHasRight(){
        return rightViews.size()>0;
    }
}
