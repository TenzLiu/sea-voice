package com.xld.javalib;

public class MyClass {


    public static void main(String args[]) {

//        System.out.println("123456");
//        System.out.println(isPalindrome(121) + "");
        System.out.println(romanToInt("LVIII") + "");
    }

    public static int romanToInt(String s) {
        int res = 0;

        return res;
    }

    public static boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        if (x == 0) {
            return true;
        }

        int res = 0;
        int fanilx = x;
        while (fanilx != 0) {
            int pop = fanilx % 10;
            fanilx /= 10;
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && pop > 7))
                return false;
            if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && pop < -8))
                return false;
            res = res * 10 + pop;
        }
        return x == res;

    }
}
