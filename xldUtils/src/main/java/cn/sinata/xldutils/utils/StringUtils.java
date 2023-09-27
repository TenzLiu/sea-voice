package cn.sinata.xldutils.utils;

import android.text.TextUtils;

import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by LiaoXiang on 2015/11/18.
 */
public class StringUtils {
    /**
     * 是否为空字符或null
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || TextUtils.equals(str, "null");
    }

    public static boolean isNull(CharSequence str) {
        return str == null || TextUtils.equals(str, "null");
    }

    /**
     * 检查字符串是否为电话号码的方法,并返回true or false的判断值
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            return false;
        }
        String expression = "(^(1[3-9])[0-9]{9}$)";
//        String expression = "^((13[0-9])|(14[4,5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199)\\d{8}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断邮箱格式是否有效
     *
     * @param email 邮箱地址
     * @return true 是正确格式
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;
//        String expression = "^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[\n\t]"; //要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 隐藏手机号展示
     *
     * @param phone 手机号
     * @return
     */
    public static String hidePhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return phone;
        }
        if (phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) +
                "****" +
                phone.substring(phone.length() - 4, phone.length());
    }

    public static String hideCarNumber(String carNum) {
        if (TextUtils.isEmpty(carNum)) {
            return carNum;
        }
        if (carNum.length() < 5) {
            return carNum;
        }
        return carNum.substring(0, 2) +
                "***" +
                carNum.substring(carNum.length() - 3, carNum.length());
    }

    public static String hideIDCardNumber(String idCardNum) {
        if (TextUtils.isEmpty(idCardNum)) {
            return idCardNum;
        }
        if (idCardNum.length() < 10) {
            return idCardNum;
        }
        return idCardNum.substring(0, 6) +
                "********" +
                idCardNum.substring(idCardNum.length() - 4, idCardNum.length());
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static String formatMoneyString(String s, double money) {
        return String.format(Locale.CHINA, s, money);
    }
}
