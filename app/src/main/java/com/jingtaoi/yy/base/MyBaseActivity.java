package com.jingtaoi.yy.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import butterknife.ButterKnife;
import cn.sinata.xldutils.activitys.BaseActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2018/1/16.
 */

public abstract class MyBaseActivity extends BaseActivity {
    public static final boolean D = true;

    //用户token
    protected int userToken;
    protected String TAG = "msg";

    private LinearLayout title_layout;
    RelativeLayout rl_title, rl_header;
    public TextView tv_Left, tv_Right;
    TextView tv_title;
    View view_line_title;
    private boolean hasTitle;
    public static final int PAGE_SIZE = 10;//每页条数
    public int page = 1;//当前第几页
    private boolean isBlackshow = true;//判断是否显示深色导航栏字体
    Bundle bundle;
    Intent intent;
    private View toolbar_line;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this, getClass());
        ActivityCollector.getActivityCollector().addActivity(this);
        intent = getIntent();
        bundle = intent.getExtras();
        userToken = (int) SharedPreferenceUtils.get(getApplicationContext(), Const.User.USER_TOKEN, 0);
        LogUtils.e("用户userToken", String.valueOf(userToken));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setTouMing();
        initData();
        setBlcakShow(isBlackshow);
        setContentView();
        ButterKnife.bind(this);
        initView();
        setOnclick();
    }


    public void setTouMing() {
//        UltimateBar.newImmersionBuilder()
////                .applyNav(true)         // 是否应用到导航栏
//                .build(this)
//                .apply();

        UltimateBar.Companion.with(this)
                .statusDrawable2(getResources().getDrawable(R.drawable.bg_lucency))         // Android 6.0 以下状态栏灰色模式时状态栏颜色
                .applyNavigation(false)              // 应用到导航栏，默认 flase
                .navigationDark(false)              // 导航栏灰色模式(Android 8.0+)，默认 false
//                .navigationDrawable2(getResources().getDrawable(R.drawable.bg_gray))     // Android 8.0 以下导航栏灰色模式时导航栏颜色
                .create()
                .immersionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public abstract void initData();

    /**
     * 设置导航栏字体颜色  黑色 true / 白色 false
     *
     * @param isBlack
     */
    public void setBlcakShow(boolean isBlack) {
        if (isBlack) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //设置状态栏文字颜色及图标为深色
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //设置状态栏文字颜色及图标为浅色
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    public abstract void setContentView();

    public abstract void initView();

    public abstract void setResume();

    public abstract void setOnclick();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        ActivityCollector.getActivityCollector().finishActivity(this);
    }

//    @Override
//    protected boolean shouldPortrait() {
//        return isShouldPortrait();
//    }

    protected boolean isShouldPortrait() {
        return true;
    }

    /**
     * 重新获取userToken
     */
    public void updateUserToken() {
        userToken = (int) SharedPreferenceUtils.get(getApplicationContext(), Const.User.USER_TOKEN, 0);
    }


    /**
     * 获取string数据
     *
     * @param key
     * @return
     */
    public String getBundleString(String key) {
        if (bundle != null) {
            return bundle.getString(key);
        }
        return "";
    }

    /**
     * 获取int数据
     *
     * @param key
     * @return
     */
    public int getBundleInt(String key, int defaultValue) {
        if (bundle != null) {
            return bundle.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public Object getBundleSerializable(String key) {
        if (bundle != null) {
            return bundle.getSerializable(key);
        }
        return null;
    }

    public boolean getBundleBoolean(String key, boolean defaultValue) {
        if (bundle != null) {
            return bundle.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }


    //设置字体不随系统改变
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) { //非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    /**
     * 结束页面的时候将页面中相关控件移除
     * http://blog.csdn.net/newlai913/article/details/78095873
     */
    @Override
    public void finish() {
//        ViewGroup view = (ViewGroup) getWindow().getDecorView();
//        view.removeAllViews();
        super.finish();
    }

    @SuppressLint("ResourceType")
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.app_title);
        init();
        if (layoutResID > 0) {
            LayoutInflater.from(this).inflate(layoutResID, title_layout, true);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        init();
        if (view != null) {
            title_layout.addView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        init();
        if (view != null) {
            title_layout.addView(view, params);
        }
    }

    private void init() {
        title_layout = (LinearLayout) findViewById(R.id.title_layout);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        tv_Left = (TextView) findViewById(R.id.tv_left_title);
        tv_Right = (TextView) findViewById(R.id.tv_right_title);
        rl_header = (RelativeLayout) findViewById(R.id.rl_header_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        toolbar_line = (View) findViewById(R.id.toolbar_line);
        view_line_title=toolbar_line;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            rl_header.setBackgroundResource(R.color.gray);
        }

        setBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * 是否显示title
     *
     * @param hasTitle
     */
    public void showTitle(boolean hasTitle) {
        rl_title.setVisibility(hasTitle ? VISIBLE : GONE);
    }

  public void showLine(boolean hasLine) {
        view_line_title.setVisibility(hasLine ? VISIBLE : GONE);
    }
    public void setBack(View.OnClickListener onClickListener) {
        tv_Left.setOnClickListener(onClickListener);
    }

    /**
     * 是否显示返回键
     *
     * @param v
     */
    public void showBack(int v) {
        tv_Left.setVisibility(v);
    }


    public void showToolbarLine(int v) {
        toolbar_line.setVisibility(v);
    }


    public void setTitleBarColor(int color) {
        if (rl_header !=null)
        rl_header.setBackgroundResource(color);
    }

    public void setToobarColor(int color) {
        if (rl_title !=null)
            rl_title.setBackgroundResource(color);
    }

 public void setTitleBackGroundColor(int color) {
        if (rl_title != null) {
            rl_title.setBackgroundResource(color);
        }
    }

    /**
     * 是否显示白色状态栏
     *
     * @param hasHeader 是否显示头部
     */
    public void showHeader(boolean hasHeader) {
        rl_header.setVisibility(hasHeader ? VISIBLE : GONE);
    }

    /**
     * 设置title文字
     *
     * @param titleId
     */
    public void setTitleText(int titleId) {
        tv_title.setText(titleId);
    }

    /**
     * 设置title颜色
     *
     * @param colorRes
     */
    public void setTitleColorRes(int colorRes) {
        tv_title.setTextColor(getResources().getColor(colorRes));
    }

    /**
     * 设置title文字
     *
     * @param title
     */
    public void setTitleText(String title) {
        tv_title.setText(title);
    }

    //设置title点击事件
    public void setTitleOnClicker(View.OnClickListener onClicker) {
        tv_title.setOnClickListener(onClicker);
    }

    //设置title图片资源id
    public void setTitleShow(int ResId) {
        Drawable nav_up = getResources().getDrawable(ResId);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tv_title.setCompoundDrawables(null, null, nav_up, null);
    }

    /**
     * 设置左边按钮文字
     *
     * @param titleId
     */
    public void setLeftText(int titleId) {
        tv_Left.setText(titleId);
    }

    public void setLeftImg(Drawable drawable) {
        tv_Left.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    public void setRightImg(Drawable drawable) {
        tv_Right.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    /**
     * 设置右边按钮文字
     *
     * @param titleId
     */
    public void setRightText(int titleId) {
        tv_Right.setText(titleId);
    }

    public void setRightClickerable(boolean clickerable) {
        tv_Right.setEnabled(clickerable);
    }

    public void setRightButton(View.OnClickListener onClickListener) {
        tv_Right.setOnClickListener(onClickListener);
    }


}
