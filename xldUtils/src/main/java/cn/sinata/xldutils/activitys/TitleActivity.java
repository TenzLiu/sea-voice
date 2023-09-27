package cn.sinata.xldutils.activitys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.R;
import cn.sinata.xldutils.view.TitleBar;

/**
 * 带标题的基础页面
 */
public abstract class TitleActivity extends BaseActivity {

    private LinearLayout root_layout;
    protected TitleBar titleBar;
    private Unbinder unbinder ;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base_title);
        init();
        if (layoutResID > 0) {
            LayoutInflater.from(this).inflate(layoutResID,root_layout,true);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        init();
        if (view != null) {
            root_layout.addView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        init();
        if (view != null) {
            root_layout.addView(view,params);
        }
    }
    //初始化部分视图，以及默认设置
    private void init() {
        unbinder = ButterKnife.bind(this);
        root_layout = (LinearLayout) findViewById(R.id.root_layout);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.showLeft(showLeftButton());
        //如果默认显示左边按钮，默认点击事件为关闭页面
        if (showLeftButton()){
            titleBar.setLeftButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        //设置根试图点击事件来实现点击输入框以外关闭键盘，缺点是如果子view设置了点击事件，将会无效。
        //暂时方式如此。
        if (shouldCloseSoftWindowWhenTouchOut()){
            root_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSoftWindow(v);
                }
            });

        }
    }

    protected void addView(int position,View view){
        root_layout.addView(view,position);
    }

    /**
     * 控制显示标题栏左边按钮
     * @return 默认显示
     */
    protected boolean showLeftButton(){
        return true;
    }

    /**
     * 设置左按钮文字
     * @param text 文字
     */
    protected void setLeftButtonText(String text){
        titleBar.setLeftButton(text);
    }

    /**
     * 设置左按钮点击事件
     * @param onClickListener 点击事件
     */
    public void setLeftButtonOnClick(View.OnClickListener onClickListener){
        titleBar.setLeftButton(onClickListener);
    }

    /**
     * 设置左按钮文字
     * @param text 文字
     * @param left 右边图片
     * @param onClickListener 单击事件
     */
    public void setLeftButtonTextLeft(String text, int left, View.OnClickListener onClickListener){
        titleBar.setLeftButton(text,left,0,onClickListener);
    }

    /**
     * 设置左按钮文字
     * @param text 文字
     * @param right 左边图片
     * @param onClickListener 单击事件
     */
    public void setLeftButtonTextRight(String text, int right, View.OnClickListener onClickListener){
        titleBar.setLeftButton(text,0,right,onClickListener);
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(String title){
        titleBar.setTitle(title);
    }

    public void setTitleColor(int color){
        titleBar.setTitleColor(color);
    }

    public EditText getTitleView(){
        return titleBar.getTitleView();
    }

    /**
     * 设置右边按钮，
     * @param position 按钮位置
     * @param text 按钮文字
     */
    protected void setRightButtonText(int position,String text){
        titleBar.setRightButtonText(position,text);
    }

    protected TextView getRightButton(int position){
         return titleBar.getRightButton(position);
    }

    /**
     * 设置标题是否为可输入状态
     * @param canEditable 是否可输入
     */
    public void setCanEditable(boolean canEditable){
        titleBar.setCanEditable(canEditable);
    }

    public void setRightButton(int position,String text,int resId){
        titleBar.setRightButton(position,text,resId);
    }

    protected void addRightButton(String title,View.OnClickListener onClickListener){
        addRightButton(title,0,onClickListener);
    }
    public void addRightButton(int drawableId,View.OnClickListener onClickListener){
        addRightButton(null,drawableId,onClickListener);
    }
    public void addRightButton(String title,int right,View.OnClickListener onClickListener){
        titleBar.addRightButton(title,right,onClickListener);
    }

    public void hideRightButton(int position,boolean hide){
        titleBar.hideRightButton(position,hide);
    }

    public void hideLeftButton(boolean hide){
        titleBar.showLeft(!hide);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    protected boolean shouldCloseSoftWindowWhenTouchOut(){
        return false;
    }
    /**
     * 关闭键盘
     * @param v
     */
    private void hideSoftWindow(View v){
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
