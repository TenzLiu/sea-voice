package cn.sinata.xldutils.utils;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 *
 * Created by liaoxiang on 16/3/17.
 */
public class Toast {
    /** 系统Toast对象 */
    private android.widget.Toast mToast = null ;
    /** 第一次时间 */
    private static long oneTime = 0 ;

    private static class Builder{
        private static final Toast toast = new Toast();

        private static Toast build(Context context){
            toast.mToast = android.widget.Toast.makeText(context.getApplicationContext(),"", android.widget.Toast.LENGTH_SHORT);
            return toast;
        }
    }

    private Toast(){}

    public static Toast create(Context context){
        return Builder.build(context);
    }

    public static Toast create(Fragment fragment){
        return Builder.build(fragment.getContext());
    }
    /**
     * 显示Toast
     * @param msg 显示文字
     */
    public void show(String msg){
        if (oneTime == 0) {
            mToast.setText(msg);
            mToast.show();
            oneTime = System.currentTimeMillis();
        } else {
            /* 第二次时间 */
            long twoTime = System.currentTimeMillis();
            mToast.setText(msg);
            if (twoTime - oneTime > 1000) {
                mToast.show();
                oneTime = twoTime;
            }
        }
    }
}