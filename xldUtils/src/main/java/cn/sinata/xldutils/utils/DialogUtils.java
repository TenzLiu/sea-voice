package cn.sinata.xldutils.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.widget.MyDatePickerDialog;

import java.util.Calendar;

/**
 *
 */
public class DialogUtils {


    /**
     * 创建简单dialog
     * @param context 上下文
     * @param message 提示文字
     */
    public static void createNoticeDialog(Context context,String message){
        createNoticeDialog(context,"请注意",message);
    }

    /**
     * 创建简单dialog
     * @param context 上下文
     * @param title 标题
     * @param message 提示文字
     */
    public static void createNoticeDialog(Context context,String title,String message){
        createNoticeDialog(context,title,message,true,"确定",null,null,null);
    }

    /**
     * 创建dialog
     * @param context 上下文
     * @param title 标题
     * @param message 提示信息
     * @param positiveButton 按钮文字，不显示按钮请传null
     */
    public static void createNoticeDialog(Context context,String title,String message,String positiveButton){
        createNoticeDialog(context,title,message,true,positiveButton,null,null,null);
    }

    /**
     * 创建dialog
     * @param context 上下文
     * @param title 标
     * @param message 提示信
     * @param positiveButton 按钮文字，不显示按钮请传null
     * @param negativeButton 按钮文字，不显示按钮请传null
     */
    public static void createNoticeDialog(Context context,String title,String message,String positiveButton,String negativeButton){
        createNoticeDialog(context,title,message,true,positiveButton,negativeButton,null,null);
    }
    /**
     * 创建dialog
     * @param context 上下文
     * @param title 标
     * @param message 提示信
     * @param positiveButton 按钮文字，不显示按钮请传null
     * @param positiveListener 按钮点击事件
     */
    public static void createNoticeDialog(Context context,String title,String message,String positiveButton,DialogInterface.OnClickListener positiveListener){
        createNoticeDialog(context,title,message,true,positiveButton,"取消",positiveListener,null);
    }
    /**
     * 创建dialog
     * @param context 上下文
     * @param title 标
     * @param message 提示信
     * @param positiveButton 按钮文字，不显示按钮请传null
     * @param negativeButton 按钮文字，不显示按钮请传null
     * @param positiveListener 确定按钮点击事件
     * @param negativeListener 取消按钮点击事件
     */
    public static void createNoticeDialog(Context context, String title, String message,boolean cancelOutSide, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelOutSide);
        if (!TextUtils.isEmpty(positiveButton)) {
            builder.setPositiveButton(positiveButton,positiveListener);
        }
        if (!TextUtils.isEmpty(negativeButton)) {
            builder.setNegativeButton(negativeButton, negativeListener);
        }
        builder.create().show();
    }

    /**
     *
     * @param context
     * @param onDateSetListener
     */
    public static void createTimeDialog(Context context, DatePickerDialog.OnDateSetListener onDateSetListener){
        createTimeDialog(context,-1,-1,onDateSetListener);
    }

    /**
     * 展示系统时间选择弹窗
     * @param context 上下文
     * @param onDateSetListener 选择监听
     * @param minDate 最小时间，不限制传－1
     *  @param maxDate 最大时间，不限制传－1
     */
    public static void createTimeDialog(Context context,long minDate,long maxDate, DatePickerDialog.OnDateSetListener onDateSetListener){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        MyDatePickerDialog datePickerDialog = new MyDatePickerDialog(context, R.style.DatePickDialog,onDateSetListener,mYear,mMonth,mDay);
        if (minDate>-1){
            datePickerDialog.setMinDate(minDate);
        }
        if (maxDate>-1){
            datePickerDialog.setMaxDate(maxDate);
        }
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.show();
    }
}
