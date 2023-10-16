package com.jingtaoi.yy.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import cn.sinata.xldutils.utils.StringUtils;

import static android.content.Context.TELEPHONY_SERVICE;


/**
 * Created by Administrator on 2017/7/10.
 */

public class MyUtils {

    private static volatile MyUtils myUtils;
    public static Gson mGson = new Gson();

    public MyUtils() {

    }

    public static MyUtils getInstans() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (myUtils == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (MyUtils.class) {
                //未初始化，则初始instance变量
                if (myUtils == null) {
                    myUtils = new MyUtils();
                }
            }
        }
        return myUtils;
    }

    //根据时间戳显示时间
    public String showTime(long timestamp, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }

    //根据string转换为时间戳
    public long getLongTime(String timestamp, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(timestamp);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    //根句string转时间戳
    public long getLongTime(String timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(timestamp);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }


    //根据时间戳显示时间
    public String showTimeYMD(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }

    //根据时间戳显示时间
    public String showTimeYMDHM(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }

    //根据时间戳显示时间
    public String showTimeMMdd(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }

    //根据时间戳显示时间
    public String showTimeHHmm(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }

    //根据时间戳显示时间
    public String showTimeHHmmss(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }

    /**
     * 显示，今天、明后、后天、或者时间 yyyy年MM月dd日 HH:mm
     *
     * @param timestamp
     * @return
     */
    public String showTimeDays(long timestamp) {
        Date date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(sdf.format(new Date()));
            if (timestamp > date.getTime() && timestamp < (date.getTime() + 24 * 60 * 60 * 1000)) {//今天
                return String.format("今天 %s", showTimeMMdd(timestamp));
            } else if (timestamp > (date.getTime() + 24 * 60 * 60 * 1000) && timestamp < (date.getTime() + 48 * 60 * 60 * 1000)) {//明天
                return String.format("明天 %s", showTimeMMdd(timestamp));
            } else if (timestamp > (date.getTime() + 48 * 60 * 60 * 1000) && timestamp < (date.getTime() + 72 * 60 * 60 * 1000)) {
                return String.format("后天 %s", showTimeMMdd(timestamp));
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
                return simpleDateFormat.format(new Date(timestamp));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("DefaultLocale")
    //列表展示发布时间
    public String forMartDays(long timestamp) {
        long nowTime = System.currentTimeMillis();
        long endTime = nowTime - timestamp;
        if (endTime > 0) {
            if (endTime < 24 * 3600 * 1000) {
                return showTimeHHmm(timestamp);
            } else if (endTime < 24 * 3600 * 1000 * 2) {
                return "昨天";
            } else if (endTime < 24 * 3600 * 1000 * 3) {
                return "前天";
            } else if (endTime < 24 * 3600 * 1000 * 24) {
                return "3天前";
            } else {
                return "一月前";
            }
        } else {
            return "刚刚";
        }
    }

    @SuppressLint("DefaultLocale")
    //列表展示发布时间
    public String forMartTimeShow(long timestamp) {
        long nowTime = System.currentTimeMillis();
        long endTime = nowTime - timestamp;
        if (endTime > 0) {
            if (endTime < 24 * 3600 * 1000) {
                return showTimeHHmm(timestamp);
            } else {
                return showTimeMMdd(timestamp);
            }
        } else {
            return "刚刚";
        }
    }

    //设置double精确到小数点后两位
    public String doubleTwo(double price) {
        String shows = String.format("%.2f", price);
//        if (shows.indexOf(".")>0){
//            shows = shows.replaceAll("0+?$", "");//去掉多余的0
//            shows = shows.replaceAll("[.]$", "");//如最后一位是.则去掉
//        }
        return shows;
    }

    //设置double精确到小数点后两位(删除末尾的0)
    public String doubleDelete(double price) {
        String shows = String.format("%.2f", price);
        if (shows.indexOf(".") > 0) {
            shows = shows.replaceAll("0+?$", "");//去掉多余的0
            shows = shows.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return shows;
    }

    //设置double精确到小数点后1位
    public String doubleOne(double price) {
        String shows = String.format("%.1f", price);

        return shows;
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    public String formatTime(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }


    /**
     * 跳转到应用详情界面
     */
    public void gotoAppDetailIntent(Activity activity, int resquestCode) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, resquestCode);
    }

    /**
     * 转跳外部链接
     *
     * @param context
     * @param urlHtml
     */
    public void toWebView(Context context, String urlHtml) {
        if (StringUtils.isEmpty(urlHtml)) {
            return;
        } else if (urlHtml.startsWith("http://") || urlHtml.startsWith("https://")) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(urlHtml));//Url 就是你要打开的网址
            intent.setAction(Intent.ACTION_VIEW);
            context.startActivity(intent); //启动浏览器
        }
    }

    /**
     * 复制到剪切板
     *
     * @param copyStr
     * @return
     */
    public void copyStr(Context context, String copyStr) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", copyStr);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }


    /**
     * 调起拨号页面
     *
     * @param context
     * @param phone
     */
    public void CallPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 过去uuid
     */

    //获得全球唯一性的id
    public String getUUid() {
        String id = UUID.randomUUID().toString();
        //生成的id942cd30b-16c8-449e-8dc5-028f38495bb5中间含有横杠，
        // <span style="color: rgb(75, 75, 75); font-family: Verdana, Arial, Helvetica,
        // sans-serif; line-height: 20.7999992370605px;">用来生成数据库的主键id是很实用的。</span>
//        id = id.replace("-", "");//替换掉中间的那个斜杠
        return id;
    }

    //获取手机唯一id (IMEI)
    public String getUuid(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {// android 10以上
            String androidId = Settings.System.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return androidId;
        }

        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        @SuppressLint({"MissingPermission", "HardwareIds"}) String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }


    /**
     * 获取指定文件大小(单位：字节)
     *
     * @param file
     * @return
     * @throws Exception
     */
    public long getFileSize(File file) throws Exception {
        if (file == null) {
            return 0;
        }
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }

    /**
     * 判断压缩级别
     *
     * @param filePath
     * @return
     */
    public int getSampleSize(String filePath) {
        File file = new File(filePath);
        long size;
        try {
            size = getFileSize(file);
            int fileSize = (int) size / 1048576;
            LogUtils.e("文件大小", size + "");
            LogUtils.e("msg", "文件压缩级别" + fileSize);
            if (fileSize < 1) {
                return 0;
            } else if (fileSize <= 5) {
                return 2;
            } else if (fileSize <= 20) {
                return 4;
            } else if (fileSize <= 80) {
                return 6;
            } else if (fileSize <= 160) {
                return 8;
            } else {
                return 10;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2;
    }


    public BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }


    public void compressBitmapToFile(Bitmap bmp, File file) {
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 1;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将json对象转换成map集合
     *
     * @param gsonStr
     * @return
     */
    public Map<String, Object> gsonToMap(String gsonStr) {
        Log.e("tag", gsonStr);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map = gson.fromJson(gsonStr, type);
        return map;
    }

    /**
     * 隐藏展示身份证
     *
     * @param idNumber 身份证号码
     * @return
     */
    public String hideIdNumber(String idNumber) {
        if (StringUtils.isEmpty(idNumber)) {
            return idNumber;
        }
        if (idNumber.length() == 15 || idNumber.length() == 18) {
            StringBuffer substring = new StringBuffer();
            substring.append(idNumber.substring(0, idNumber.length() - 12));
            substring.append("********");
            substring.append(idNumber.substring(idNumber.length() - 4));
            return substring.toString();
        }
        return idNumber;
    }


    /**
     * 隐藏展示身份证
     *
     * @param idNumber 身份证号码
     * @return
     */
    public String hideIdNumber3(String idNumber) {
        if (StringUtils.isEmpty(idNumber)) {
            return idNumber;
        }
        if (idNumber.length() == 18) {
            StringBuffer substring = new StringBuffer();
            substring.append(idNumber.substring(0, idNumber.length() - 15));
            substring.append("************");
            substring.append(idNumber.substring(idNumber.length() - 3));
            return substring.toString();
        }
        return idNumber;
    }

    //判断是否包含字母和数字(长度6-16)
    public static boolean isLetterDigit(String str) {
        if (str.length() < 6 || str.length() > 16) {
            return false;
        }
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    //隐藏软键盘
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示或隐藏软键盘
     *
     * @param context 上下文对象
     */
    public void showOrHahe(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public void getMemoryInfo(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        LogUtils.e("Memory", "系统总内存:" + (info.totalMem / (1024 * 1024)) + "M");
        LogUtils.e("Memory", "系统剩余内存:" + (info.availMem / (1024 * 1024)) + "M");
        LogUtils.e("Memory", "系统是否处于低内存运行：" + info.lowMemory);
        LogUtils.e("Memory", "系统剩余内存低于" + (info.threshold / (1024 * 1024)) + "M时为低内存运行");
    }


    /**
     * 是否使屏幕常亮
     *
     * @param activity
     * @Param isOpenLight
     */
    public void keepScreenLongLight(Activity activity, boolean isOpenLight) {
        Window window = activity.getWindow();
        if (isOpenLight) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    public String readAssetsFile(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int fileLength = is.available();
            byte[] buffer = new byte[fileLength];
            int readLength = is.read(buffer);
            is.close();
            return new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "读取错误，请检查文件是否存在";
    }

    /**
     * 生成随机用户名
     * @param src
     * @return
     */
    public String randomUserName(String src) {
        String outName = "";
        Random random = new Random();
        for (int i = 0; i < src.length(); i++) {
            outName = outName + src.charAt(random.nextInt(src.length()));
        }

        return outName;
    }

}
