package cn.sinata.xldutils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * Created by liaoxiang on 16/8/8.
 */
public class ActivityUtil {
    private Context fromActivity;
    private Fragment fragment;
    private Class goActivity;
    private Bundle bundle;

    private ActivityUtil(Context context){
        fromActivity = context;
//        this.goActivity = goActivity;
        bundle = new Bundle();
    }
    private ActivityUtil(Fragment fragment){
        this.fragment = fragment;
//        this.goActivity = goActivity;
        bundle = new Bundle();
    }

    public static ActivityUtil create(Context context){
        return new ActivityUtil(context);
    }
    public static ActivityUtil create(Fragment fragment){
        return new ActivityUtil(fragment);
    }

    public ActivityUtil go(Class goActivity){
        this.goActivity = goActivity;
        return this;
    }
    /**
     * 传递int参数
     * @param key 键
     * @param intValue 值
     * @return this
     */
    public ActivityUtil put(String key, int intValue){
        bundle.putInt(key,intValue);
        return this;
    }
    /**
     * 传递Parcelable参数
     * @param key 键
     * @param p 值
     * @return this
     */
    public ActivityUtil put(String key, Parcelable p){
        bundle.putParcelable(key,p);
        return this;
    }

    /**
     * 传递String参数
     * @param key 键
     * @param s 值
     * @return this
     */
    public ActivityUtil put(String key, String s){
        bundle.putString(key,s);
        return this;
    }
    /**
     * 传递float参数
     * @param key 键
     * @param f 值
     * @return this
     */
    public ActivityUtil put(String key, float f){
        bundle.putFloat(key,f);
        return this;
    }
    /**
     * 传递double参数
     * @param key 键
     * @param d 值
     * @return this
     */
    public ActivityUtil put(String key, double d){
        bundle.putDouble(key,d);
        return this;
    }
    /**
     * 传递Serializable参数
     * @param key 键
     * @param s 值
     * @return this
     */
    public ActivityUtil put(String key, Serializable s){
        bundle.putSerializable(key,s);
        return this;
    }
    /**
     * 传递StringList参数
     * @param key 键
     * @param a 值
     * @return this
     */
    public ActivityUtil putStringList(String key, ArrayList<String> a){
        bundle.putStringArrayList(key,a);
        return this;
    }
    /**
     * 传递ParcelableList参数
     * @param key 键
     * @param a 值
     * @return this
     */
    public ActivityUtil putParcelableList(String key, ArrayList<? extends Parcelable> a){
        bundle.putParcelableArrayList(key,a);
        return this;
    }

    /**
     * 跳转activity
     */
    public void start(){
        if (goActivity==null){
            return;
        }
        if (fromActivity!=null) {
            Intent intent = new Intent(fromActivity, goActivity);
            if (bundle != null && !bundle.isEmpty()) {
                intent.putExtras(bundle);
            }
            fromActivity.startActivity(intent);
        }else if (fragment!=null){
            Intent intent = new Intent(fragment.getContext(), goActivity);
            if (bundle != null && !bundle.isEmpty()) {
                intent.putExtras(bundle);
            }
            fragment.startActivity(intent);
        }
    }

    /**
     * 跳转activity 带requestCode
     * @param requestCode 请求code
     */
    public void startForResult(int requestCode){
        if (goActivity==null){
            return;
        }
        if (fromActivity!=null){
            Intent intent = new Intent(fromActivity,goActivity);
            if (bundle!=null && !bundle.isEmpty()){
                intent.putExtras(bundle);
            }
            if (fromActivity instanceof Activity) {
                ((Activity) fromActivity).startActivityForResult(intent, requestCode);
            }
        }else if (fragment!=null && !fragment.isDetached()){
            Intent intent = new Intent(fragment.getContext(),goActivity);
            if (bundle!=null && !bundle.isEmpty()){
                intent.putExtras(bundle);
            }
            fragment.startActivityForResult(intent, requestCode);
        }
    }

}
