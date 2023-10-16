package com.jingtaoi.yy.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AtsUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");

    public static String jiami(String key) {
        //对加密后的字符串进行处理：把 今天的日期 插入到加密后的文件中。
        String format = sdf.format(new Date());
        int j = 0;
        Set<Integer> numbers = new HashSet<>(Arrays.asList(41, 8, 80, 42, 3, 99, 66, 10, 77, 30));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            sb.append(key.charAt(i));
            if (numbers.contains(i)) {
                sb.append(format.charAt(j));
                j++;
            }
            ;
        }
        return sb.toString();
    }

    public static void test(View view) {
        AnimatorSet resizeAvenger = new AnimatorSet();
        ObjectAnimator objectAnimationX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0);
        ObjectAnimator objectAnimationY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0);
        objectAnimationX.setDuration(500);
        objectAnimationX.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimationY.setDuration(500);
        objectAnimationY.setRepeatMode(ValueAnimator.REVERSE);
        resizeAvenger.playTogether(objectAnimationX, objectAnimationY);
        resizeAvenger.start();
    }
}
