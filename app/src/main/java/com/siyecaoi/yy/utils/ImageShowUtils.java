package com.siyecaoi.yy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.siyecaoi.yy.R;

public class ImageShowUtils {

    private static volatile ImageShowUtils imageShowUtils;

    public ImageShowUtils() {

    }

    public static ImageShowUtils getInstans() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (imageShowUtils == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (MyUtils.class) {
                //未初始化，则初始instance变量
                if (imageShowUtils == null) {
                    imageShowUtils = new ImageShowUtils();
                }
            }
        }
        return imageShowUtils;
    }


    public static Bitmap getGradeBitmap(Context mContext,int grade){

        TextView textView=new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setText(" "+getGradeText(grade));
        textView.setCompoundDrawablePadding(5);
        textView.setTextSize(11);
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        textView.setBackgroundResource(getGrade(grade));
        textView.setDrawingCacheEnabled(true);
        textView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(textView.getDrawingCache());
        //千万别忘最后一步
        textView.destroyDrawingCache();
        return bitmap;
    }

    public static int getGrade(int grade) {
        switch (grade) {
            case 0:
                return R.drawable.touming;
            case 1:
                return R.drawable.caifu1;
            case 2:
                return R.drawable.caifu2;
            case 3:
                return R.drawable.caifu3;
            case 4:
                return R.drawable.caifu4;
            case 5:
                return R.drawable.caifu5;
            case 6:
                return R.drawable.caifu6;
            case 7:
                return R.drawable.caifu7;
            case 8:
                return R.drawable.caifu8;
            case 9:
                return R.drawable.caifu9;
            case 10:
                return R.drawable.caifu10;
            case 11:
                return R.drawable.caifu11;
            case 12:
                return R.drawable.caifu12;
            case 13:
                return R.drawable.caifu13;
            case 14:
                return R.drawable.caifu14;
            case 15:
                return R.drawable.caifu15;
            case 16:
                return R.drawable.caifu16;
            case 17:
                return R.drawable.caifu17;
            case 18:
                return R.drawable.caifu18;
            case 19:
                return R.drawable.caifu19;
            case 20:
                return R.drawable.caifu20;
            case 21:
                return R.drawable.caifu21;
            case 22:
                return R.drawable.caifu22;
            case 23:
                return R.drawable.caifu23;
            case 24:
                return R.drawable.caifu24;
            case 25:
                return R.drawable.caifu25;
            case 26:
                return R.drawable.caifu26;
            case 27:
                return R.drawable.caifu27;
            case 28:
                return R.drawable.caifu28;
            case 29:
                return R.drawable.caifu29;
            case 30:
                return R.drawable.caifu30;
            case 31:
                return R.drawable.caifu31;
            case 32:
                return R.drawable.caifu32;
            case 33:
                return R.drawable.caifu33;
            case 34:
                return R.drawable.caifu34;
            case 35:
                return R.drawable.caifu35;
            case 36:
                return R.drawable.caifu36;
            case 37:
                return R.drawable.caifu37;
            case 38:
                return R.drawable.caifu38;
            case 39:
                return R.drawable.caifu39;
            case 40:
                return R.drawable.caifu40;
            case 41:
                return R.drawable.caifu41;
            case 42:
                return R.drawable.caifu42;
            case 43:
                return R.drawable.caifu43;
            case 44:
                return R.drawable.caifu44;
            case 45:
                return R.drawable.caifu45;
            case 46:
                return R.drawable.caifu46;
            case 47:
                return R.drawable.caifu47;
            case 48:
                return R.drawable.caifu48;
            case 49:
                return R.drawable.caifu49;
            case 50:
                return R.drawable.caifu50;
            default:
                return R.drawable.caifu50;
        }
    }


    public static String getGradeText(int grade) {
//        switch (grade) {
//            case 0:
//                return "";
//            case 1:
//                return "平民1";
//            case 2:
//                return "平民2";
//            case 3:
//                return "平民3";
//            case 4:
//                return "平民4";
//            case 5:
//                return "平民5";
//            case 6:
//                return "地主1";
//            case 7:
//                return "地主2";
//            case 8:
//                return "地主3";
//            case 9:
//                return "地主4";
//            case 10:
//                return "地主5";
//            case 11:
//                return "剑士1";
//            case 12:
//                return "剑士2";
//            case 13:
//                return "剑士3";
//            case 14:
//                return "剑士4";
//            case 15:
//                return "剑士5";
//            case 16:
//                return "骑士1";
//            case 17:
//                return "骑士2";
//            case 18:
//                return "骑士3";
//            case 19:
//                return "骑士4";
//            case 20:
//                return "骑士5";
//            case 21:
//                return "子爵1";
//            case 22:
//                return "子爵2";
//            case 23:
//                return "子爵3";
//            case 24:
//                return "子爵4";
//            case 25:
//                return "子爵5";
//            case 26:
//                return "伯爵1";
//            case 27:
//                return "伯爵2";
//            case 28:
//                return "伯爵3";
//            case 29:
//                return "伯爵4";
//            case 30:
//                return "伯爵5";
//            case 31:
//                return "侯爵1";
//            case 32:
//                return "侯爵2";
//            case 33:
//                return "侯爵3";
//            case 34:
//                return "侯爵4";
//            case 35:
//                return "侯爵5";
//            case 36:
//                return "勋爵1";
//            case 37:
//                return "勋爵2";
//            case 38:
//                return "勋爵3";
//            case 39:
//                return "勋爵4";
//            case 40:
//                return "勋爵5";
//            case 41:
//                return "公爵1";
//            case 42:
//                return "公爵2";
//            case 43:
//                return "公爵3";
//            case 44:
//                return "公爵4";
//            case 45:
//                return "公爵5";
//            case 46:
//                return "帝王1";
//            case 47:
//                return "帝王2";
//            case 48:
//                return "帝王3";
//            case 49:
//                return "帝王4";
//            case 50:
//                return "帝王5";
//            default:
//                return "帝王5";
//        }
        return "     ";
    }


    public static String getCharmText(int grade) {
        switch (grade) {
            case 0:
                return "";
            case 1:
                return "新秀1";
            case 2:
                return "新秀2";
            case 3:
                return "新秀3";
            case 4:
                return "新秀4";
            case 5:
                return "新秀5";
            case 6:
                return "出道1";
            case 7:
                return "出道2";
            case 8:
                return "出道3";
            case 9:
                return "出道4";
            case 10:
                return "出道5";
            case 11:
                return "新星1";
            case 12:
                return "新星2";
            case 13:
                return "新星3";
            case 14:
                return "新星4";
            case 15:
                return "新星5";
            case 16:
                return "知名1";
            case 17:
                return "知名2";
            case 18:
                return "知名3";
            case 19:
                return "知名4";
            case 20:
                return "知名5";
            case 21:
                return "金马1";
            case 22:
                return "金马2";
            case 23:
                return "金马3";
            case 24:
                return "金马4";
            case 25:
                return "金马5";
            case 26:
                return "传奇1";
            case 27:
                return "传奇2";
            case 28:
                return "传奇3";
            case 29:
                return "传奇4";
            case 30:
                return "传奇5";
            case 31:
                return "殿堂1";
            case 32:
                return "殿堂2";
            case 33:
                return "殿堂3";
            case 34:
                return "殿堂4";
            case 35:
                return "殿堂5";
            case 36:
                return "巨星1";
            case 37:
                return "巨星2";
            case 38:
                return "巨星3";
            case 39:
                return "巨星4";
            case 40:
                return "巨星5";
            case 41:
                return "天王1";
            case 42:
                return "天王2";
            case 43:
                return "天王3";
            case 44:
                return "天王4";
            case 45:
                return "天王5";
            case 46:
                return "帝星1";
            case 47:
                return "帝星2";
            case 48:
                return "帝星3";
            case 49:
                return "帝星4";
            case 50:
                return "帝星5";
            default:
                return "帝星5";
        }
    }

    public static int getCharm(int charm) {
        switch (charm) {
            case 0:
                return R.drawable.touming;
            case 1:
                return R.drawable.meili1;
            case 2:
                return R.drawable.meili2;
            case 3:
                return R.drawable.meili3;
            case 4:
                return R.drawable.meili4;
            case 5:
                return R.drawable.meili5;
            case 6:
                return R.drawable.meili6;
            case 7:
                return R.drawable.meili7;
            case 8:
                return R.drawable.meili8;
            case 9:
                return R.drawable.meili9;
            case 10:
                return R.drawable.meili10;
            case 11:
                return R.drawable.meili11;
            case 12:
                return R.drawable.meili12;
            case 13:
                return R.drawable.meili13;
            case 14:
                return R.drawable.meili14;
            case 15:
                return R.drawable.meili15;
            case 16:
                return R.drawable.meili16;
            case 17:
                return R.drawable.meili17;
            case 18:
                return R.drawable.meili18;
            case 19:
                return R.drawable.meili19;
            case 20:
                return R.drawable.meili20;
            case 21:
                return R.drawable.meili21;
            case 22:
                return R.drawable.meili22;
            case 23:
                return R.drawable.meili23;
            case 24:
                return R.drawable.meili24;
            case 25:
                return R.drawable.meili25;
            case 26:
                return R.drawable.meili26;
            case 27:
                return R.drawable.meili27;
            case 28:
                return R.drawable.meili28;
            case 29:
                return R.drawable.meili29;
            case 30:
                return R.drawable.meili30;
            case 31:
                return R.drawable.meili31;
            case 32:
                return R.drawable.meili32;
            case 33:
                return R.drawable.meili33;
            case 34:
                return R.drawable.meili34;
            case 35:
                return R.drawable.meili35;
            case 36:
                return R.drawable.meili36;
            case 37:
                return R.drawable.meili37;
            case 38:
                return R.drawable.meili38;
            case 39:
                return R.drawable.meili39;
            case 40:
                return R.drawable.meili40;
            case 41:
                return R.drawable.meili41;
            case 42:
                return R.drawable.meili42;
            case 43:
                return R.drawable.meili43;
            case 44:
                return R.drawable.meili44;
            case 45:
                return R.drawable.meili45;
            case 46:
                return R.drawable.meili46;
            case 47:
                return R.drawable.meili47;
            case 48:
                return R.drawable.meili48;
            case 49:
                return R.drawable.meili49;
            case 50:
                return R.drawable.meili50;
            default:
                return R.drawable.meili50;
        }
    }

    /**
     * 获取svga动画名称
     *
     * @param uniCode
     * @return
     */
    public String getAssetsName(int uniCode) {
        String resStr = "";
        switch (uniCode) {
//            case 128604:
//                resStr = "";
//                break;
//            case 128605:
//                resStr = "";
//                break;
//            case 128606:
//                resStr = "sv128606.svga";
//                break;
//            case 128607:
//                resStr = "sv128607.svga";
//                break;
//            case 128608:
//                resStr = "sv128608.svga";
//                break;
//            case 128609:
//                resStr = "sv128609.svga";
//                break;
//            case 128610:
//                resStr = "sv128610.svga";
//                break;
//            case 128611:
//                resStr = "sv128611.svga";
//                break;
//            case 128612:
//                resStr = "sv128612.svga";
//                break;
//            case 128613:
//                resStr = "sv128613.svga";
//                break;
//            case 128614:
//                resStr = "sv128614.svga";
//                break;
//            case 128615:
//                resStr = "sv128615.svga";
//                break;
//            case 128616:
//                resStr = "sv128616.svga";
//                break;
//            case 128617:
//                resStr = "sv128617.svga";
//                break;
//            case 128618:
//                resStr = "sv128618.svga";
//                break;
//            case 128619:
//                resStr = "sv128619.svga";
//                break;
//            case 128620:
//                resStr = "sv128620.svga";
//                break;
//            case 128621:
//                resStr = "sv128621.svga";
//                break;
//            case 128622:
//                resStr = "sv128622.svga";
//                break;
//            case 128623:
//                resStr = "sv128623.svga";
//                break;
//            case 128624:
//                resStr = "sv128624.svga";
//                break;
//            case 128625:
//                resStr = "sv128625.svga";
//                break;
//            case 128626:
//                resStr = "sv128626.svga";
//                break;
        }

        return resStr;
    }

    /**
     * 获取资源文件
     *
     * @param uniCode
     * @param number
     * @return
     */
    public int getResId(int uniCode, int number) {
        int resId = 0;
        switch (uniCode) {
            case 128552://排麦序
                resId = getMaiDrawable(number);
                break;
            case 128553://爆灯
                resId = R.drawable.deng_1;
                break;
            case 128554://举手
                resId = R.drawable.hand_4;
                break;
            case 128555://猜拳
                resId = getCaiDrawable(number);
                break;
            case 128556:
                resId = R.drawable.xiaoku;
                break;
            case 128557:
                resId = R.drawable.sese;
                break;
            case 128558:
                resId = R.drawable.songhua;
                break;
            case 128559:
                resId = R.drawable.taoqi;
                break;
            case 128560:
                resId = R.drawable.yaofan;
                break;
            case 128561:
                resId = R.drawable.qinqin;
                break;
            case 128562://骰子
                resId = getZhiDrawable(number);
                break;
            case 128563://猜硬币
                resId = getYingBiDrawable(number);
                break;

            case 128604:
                resId = R.drawable.sv128604;
                break;
            case 128605:
                resId = R.drawable.sv128605;
                break;
            case 128606:
                resId = R.drawable.sv128606;
                break;
            case 128607:
                resId = R.drawable.sv128607;
                break;
            case 128608:
                resId = R.drawable.sv128608;
                break;
            case 128609:
                resId = R.drawable.sv128609;
                break;
            case 128610:
                resId = R.drawable.sv128610;
                break;
            case 128611:
                resId = R.drawable.sv128611;
                break;
            case 128612:
                resId = R.drawable.sv128612;
                break;
            case 128613:
                resId = R.drawable.sv128613;
                break;
            case 128614:
                resId = R.drawable.sv128614;
                break;
            case 128615:
                resId = R.drawable.sv128615;
                break;
        }
        return resId;
    }

    /**
     * 排麦序
     *
     * @param nuberShow
     * @return
     */
    private int getMaiDrawable(int nuberShow) {
        switch (nuberShow) {
            case 1:
                return R.drawable.mai_1;
            case 2:
                return R.drawable.mai_2;
            case 3:
                return R.drawable.mai_3;
            case 4:
                return R.drawable.mai_4;
            case 5:
                return R.drawable.mai_5;
            case 6:
                return R.drawable.mai_6;
            case 7:
                return R.drawable.mai_7;
            case 8:
                return R.drawable.mai_8;
        }
        return 0;
    }

//    /**
//     * 排麦序
//     *
//     * @param nuberShow
//     * @return
//     */
//    private int getMaiDrawable(int nuberShow) {
//        switch (nuberShow) {
//            case 1:
//                return R.drawable.s_number_1;
//            case 2:
//                return R.drawable.s_number_2;
//            case 3:
//                return R.drawable.s_number_3;
//            case 4:
//                return R.drawable.s_number_4;
//            case 5:
//                return R.drawable.s_number_5;
//            case 6:
//                return R.drawable.s_number_6;
//            case 7:
//                return R.drawable.s_number_7;
//            case 8:
//                return R.drawable.s_number_8;
//        }
//        return 0;
//    }

    /**
     * 骰子
     *
     * @param nuberShow
     * @return
     */
    private int getZhiDrawable(int nuberShow) {
        switch (nuberShow) {
            case 1:
                return R.drawable.zhi_01;
            case 2:
                return R.drawable.zhi_02;
            case 3:
                return R.drawable.zhi_03;
            case 4:
                return R.drawable.zhi_04;
            case 5:
                return R.drawable.zhi_05;
            case 6:
                return R.drawable.zhi_06;
        }
        return 0;
    }

    /**
     * 石头剪刀布
     *
     * @param nuberShow
     * @return
     */
    private int getCaiDrawable(int nuberShow) {
        switch (nuberShow) {
            case 1:
                return R.drawable.cai_1;
            case 2:
                return R.drawable.cai_2;
            case 3:
                return R.drawable.cai_3;
        }
        return 0;
    }

//    /**
//     * 骰子
//     *
//     * @param nuberShow
//     * @return
//     */
//    private int getZhiDrawable(int nuberShow) {
//        switch (nuberShow) {
//            case 1:
//                return R.drawable.zhi1;
//            case 2:
//                return R.drawable.zhi2;
//            case 3:
//                return R.drawable.zhi3;
//            case 4:
//                return R.drawable.zhi4;
//            case 5:
//                return R.drawable.zhi5;
//            case 6:
//                return R.drawable.zhi6;
//        }
//        return 0;
//    }

    private int getYingBiDrawable(int nuberShow) {
        switch (nuberShow) {
            case 1:
                return R.drawable.yingbi1;
            case 2:
                return R.drawable.yingbi2;
        }
        return 0;
    }

//    private int getCaiDrawable(int nuberShow) {
//        switch (nuberShow) {
//            case 1:
//                return R.drawable.cai1;
//            case 2:
//                return R.drawable.cai2;
//            case 3:
//                return R.drawable.cai3;
//        }
//        return 0;
//    }
}
