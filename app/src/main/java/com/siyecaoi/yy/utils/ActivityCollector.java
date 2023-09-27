package com.siyecaoi.yy.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Stack;

/**
 * Created by Chron on 2017/8/17.
 * http://www.jianshu.com/p/e46b843b95f4
 */

public class ActivityCollector {

    public static HashMap<Class<?>, Activity> activities = new LinkedHashMap<>();
    private Stack<AppCompatActivity> BaseActivityStack = null;
    private static Class<?> mTopClz;
    public static ActivityCollector activityCollector = null;

    public static void addActivity(Activity activity, Class<?> clz) {
        activities.put(clz, activity);

        mTopClz = clz;
    }

    public static void removeActivity(Activity activity) {
        if (activities.containsValue(activity)) {
            activities.remove(activity.getClass());
        }
    }


    public static <T extends Activity> boolean isActivityExist(Class<T> clz) {
        boolean res;
        Activity activity = getActivity(clz);
        if (activity == null) {
            res = false;
        } else {
            if (activity.isFinishing() || activity.isDestroyed()) {
                res = false;
            } else {
                res = true;
            }
        }

        return res;
    }

    public static <T extends Activity> T getActivity(Class<T> clazz) {
        return (T) activities.get(clazz);
    }

    /**
     * 获取AppManager实例
     *
     * @return 返回appManager实例
     */
    public static ActivityCollector getActivityCollector() {
        if (activityCollector == null) {
            activityCollector = new ActivityCollector();
        }
        return activityCollector;
    }

    /**
     * 传入需要添加的BaseActivity
     *
     * @param baseActivity
     */
    public void addActivity(AppCompatActivity baseActivity) {
        if (BaseActivityStack == null) {
            BaseActivityStack = new Stack<AppCompatActivity>();
        }
        BaseActivityStack.add(baseActivity);
    }

    /**
     * 移除当前的BaseActivity
     */
    public void finishActivity() {
        if (BaseActivityStack == null || BaseActivityStack.size() == 0) {
            return;
        }
        AppCompatActivity baseActivity = BaseActivityStack.lastElement();
        BaseActivityStack.remove(baseActivity);
        baseActivity.finish();
    }


    /**
     * 移除指定的BaseActivity'
     *
     * @param baseActivity
     */
    public void finishActivity(AppCompatActivity baseActivity) {
        BaseActivityStack.remove(baseActivity);
        baseActivity.finish();
    }

    /**
     * 通过名字去移除BaseActivity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        for (AppCompatActivity baseActivity : BaseActivityStack) {
            if (baseActivity.getClass().getName().equals(cls.getName())) {
                finishActivity(baseActivity);
                return;
            }
        }
    }


    /**
     * 获取当前BaseActivity
     */
    public AppCompatActivity currentActivity() {
        if (BaseActivityStack == null || BaseActivityStack.size() == 0) {
            return null;
        }
        AppCompatActivity baseActivity = BaseActivityStack.lastElement();
        return baseActivity;
    }


    public void finishOtherActivity() {
        Class<?> cls = currentActivity().getClass();
        for (AppCompatActivity baseActivity : BaseActivityStack) {
            if (!(baseActivity.getClass().getName().equals(cls.getName()))) {
                finishActivity(baseActivity);
            }
        }
    }

    /**
     * 关闭所有BaseActivity
     */
    public void finishAllBaseActivity() {
//        for (BaseActivity baseActivity : BaseActivityStack)
//        {
//            finishActivity(baseActivity);
//        }

        Iterator iterator = BaseActivityStack.iterator();

        while (iterator.hasNext()) {
            AppCompatActivity baseActivity = (AppCompatActivity) iterator.next();
            baseActivity.finish();
            iterator.remove();
        }
    }


    /**
     * 当前BaseActivity跳转到另一个BaseActivity携带bundle
     *
     * @param otherBaseActivity
     * @param bundle
     */
    public void toOtherActivity(Class<?> otherBaseActivity, Bundle bundle) {
        AppCompatActivity currentActivity = currentActivity();
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(currentActivity, otherBaseActivity);
        currentActivity.startActivity(intent);
    }


    /**
     * 当前BaseActivity跳转到另一个BaseActivity携带bundle和回调值
     *
     * @param otherBaseActivity
     * @param bundle
     */
    public void toOtherActivity(Class<?> otherBaseActivity, Bundle bundle, int key) {
        AppCompatActivity currentActivity = currentActivity();
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(currentActivity, otherBaseActivity);
        currentActivity.startActivityForResult(intent, key);
    }

    /**
     * 当前BaseActivity跳转到另一个BaseActivity携带intent和回调值
     *
     * @param otherBaseActivity
     * @param intent
     * @param key
     */
    public void toOtherActivity(Class<?> otherBaseActivity, Intent intent, int key) {
        AppCompatActivity currentActivity = currentActivity();
        intent.setClass(currentActivity, otherBaseActivity);
        currentActivity.startActivityForResult(intent, key);
    }

//    /**
//     * 当前BaseActivity跳转到另一个BaseActivity
//     *
//     * @param otherBaseActivity
//     * @param intentFlag
//     */
//    public void toOtherActivity(Class<?> otherBaseActivity, int intentFlag) {
//        AppCompatActivity currentActivity = currentActivity();
//        Intent intent = new Intent();
//        intent.setFlags(intentFlag);
//        intent.setClass(currentActivity, otherBaseActivity);
//        currentActivity.startActivity(intent);
//    }

    /**
     * 当前BaseActivity跳转到另一个BaseActivity携带回调值
     *
     * @param otherBaseActivity
     * @param requestCode
     */
    public void toOtherActivity(Class<?> otherBaseActivity, int requestCode) {
        AppCompatActivity currentActivity = currentActivity();
        Intent intent = new Intent();
        intent.setClass(currentActivity, otherBaseActivity);
        currentActivity.startActivityForResult(intent, requestCode);
    }

    /**
     * 当前BaseActivity跳转另一个BaseActivity不带值
     *
     * @param otherBaseActivity
     */
    public void toOtherActivity(Class<?> otherBaseActivity) {
        AppCompatActivity currentActivity = currentActivity();
        if (currentActivity == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(currentActivity, otherBaseActivity);
        currentActivity.startActivity(intent);
    }

    /**
     * 当前BaseActivity跳转另一个BaseActivity传入String
     *
     * @param otherBaseActivity
     * @param key
     * @param value
     */
    public void toOtherActivity(Class<?> otherBaseActivity, String key, String value) {
        AppCompatActivity currentActivity = currentActivity();
        Intent intent = new Intent();
        intent.putExtra(key, value);
        intent.setClass(currentActivity, otherBaseActivity);
        currentActivity.startActivity(intent);
    }

    /**
     * 当前BaseActivity跳转到另一个BaseActivity携带intent
     *
     * @param otherBaseActivity
     * @param intent
     */
    public void toOtherActivity(Class<?> otherBaseActivity, Intent intent) {
        AppCompatActivity currentActivity = currentActivity();
        intent.setClass(currentActivity, otherBaseActivity);
        currentActivity.startActivity(intent);
    }


    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllBaseActivity();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
