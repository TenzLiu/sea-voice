package com.siyecaoi.yy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtils {
    private static final String SHARED_NAME = "share_data";

    public static void put(Context context, String key, Object value) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_NAME, 0);
        Editor eidtor = preference.edit();
        if (value instanceof String) {
            eidtor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            eidtor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            eidtor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            eidtor.putBoolean(key, (Boolean) value);
        }
        eidtor.commit();
    }

    public static Object get(Context context, String key, Object defaultValue) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_NAME, 0);
        if (defaultValue instanceof String) {
            return preference.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return preference.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Float) {
            return preference.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return preference.getBoolean(key, (Boolean) defaultValue);
        } else {
//            return preference.get(key, (Object) defaultValue);
            throw new RuntimeException("错误类型");
        }
    }

    public static void clear(Context context) {
        SharedPreferences preference = context.getSharedPreferences(SHARED_NAME, 0);
        Editor eidtor = preference.edit();
        eidtor.clear().commit();
    }



}
