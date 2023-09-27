package cn.sinata.xldutils.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 一些时间相关的方法
 * @author Administrator
 *
 */
public class TimeUtils {

	public static String getSimpleTime(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);

		SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd HH:mm",Locale.CHINA);
		Date date = null;
		try {
			date = f.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return time;
		}
	}

	public static String getChatSimpleTime(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);

		SimpleDateFormat formater = new SimpleDateFormat("yy-MM-dd",Locale.CHINA);
		Date date = null;
		try {
			date = f.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getTimeHM(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);

		SimpleDateFormat formater = new SimpleDateFormat("HH:mm",Locale.CHINA);
		Date date = null;
		try {
			date = f.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 获取时分
	 * @param time
	 * @return
	 */
	public static String getTimeHM(long time) throws ParseException {

		SimpleDateFormat formater = new SimpleDateFormat("HH:mm",Locale.CHINA);
		Date date = null;
		date = new Date(time);
		return formater.format(date);
	}
	/**
	 * 获取时分
	 * @param time
	 * @return
	 */
	public static String getTimeMS(long time){

		SimpleDateFormat formater = new SimpleDateFormat("mm:ss",Locale.CHINA);
		Date date = null;
		date = new Date(time);
		return formater.format(date);
	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 单位毫秒
	 * @return
	 */
	public static String getCurrentTimeMillisecond(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);

		return f.format(new Date(timestamp));

	}

	/**
	 * 转换时间为yyyy-MM-dd HH:mm 格式
	 * @param timestamp 时间戳
	 * @return yyyy-MM-dd HH:mm 格式
	 */
	public static String getTimeYYYYMMDDHHmm(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);

		return f.format(new Date(timestamp));

	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 单位毫秒
	 * @return
	 */
	public static String getCurrentTimeDDMM(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("ddMM月",Locale.CHINA);

		return f.format(new Date(timestamp));

	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 单位毫秒
	 * @return
	 */
	public static String getCurrentTimeMMDD(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("MM-dd",Locale.CHINA);

		return f.format(new Date(timestamp));

	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 单位秒
	 * @return
	 */
	public static String getCurrentTime(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);

		return f.format(new Date(timestamp*1000));

	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 单位秒
	 * @return
	 */
	public static String getTimeYMD(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);

		return f.format(new Date(timestamp));

	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 单位秒
	 * @return
	 */
	public static String getTimeYMDCN(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss",Locale.CHINA);

		return f.format(new Date(timestamp));

	}

	public static String getTimeMDCN(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("MM月dd日",Locale.CHINA);

		return f.format(new Date(timestamp));

	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 毫秒
	 * @return －年－月－日－时－分
	 */
	public static String getTimeYMDHMCN(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日HH时mm分",Locale.CHINA);

		return f.format(new Date(timestamp));

	}
	/**
	 * 时间戳转换日期格式
	 * @param timestamp 毫秒
	 * @return YYYY年
	 */
	public static int getTimeYear(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy",Locale.CHINA);
		int year;
		try {
			year = Integer.parseInt(f.format(new Date(timestamp)));
		}catch (Exception e){
			year=0;
		}

		return year;
	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 毫秒
	 * @return MM月
	 */
	public static int getTimeMonth(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("MM",Locale.CHINA);
		int month ;
		try {
			month =Integer.parseInt(f.format(new Date(timestamp)));
		}catch (Exception e){
			month = 0;
		}
		return month;
	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 毫秒
	 * @return dd日
	 */
	public static int getTimeDay(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("dd",Locale.CHINA);
		int day ;
		try {
			day =Integer.parseInt(f.format(new Date(timestamp)));
		}catch (Exception e){
			day = 0;
		}
		return day;
	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 毫秒
	 * @return HH时
	 */
	public static int getTimeHour(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("HH",Locale.CHINA);
		int hour ;
		try {
			hour =Integer.parseInt(f.format(new Date(timestamp)));
		}catch (Exception e){
			hour = 0;
		}
		return hour;
	}

	/**
	 * 时间戳转换日期格式
	 * @param timestamp 毫秒
	 * @return mm分
	 */
	public static int getTimeMinute(long timestamp) {

		SimpleDateFormat f = new SimpleDateFormat("mm",Locale.CHINA);
		int minute ;
		try {
			minute =Integer.parseInt(f.format(new Date(timestamp)));
		}catch (Exception e){
			minute = 0;
		}
		return minute;
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrentTime() {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);

		return f.format(new Date(System.currentTimeMillis()));

	}

	/**
	 * 获取时间的年份
	 * @return
	 */
	public static String getTimeYear(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		SimpleDateFormat formater = new SimpleDateFormat("yyyy",Locale.CHINA);
		Date date = null;
		try {
			date = f.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取时间的月份
	 * @return
	 */
	public static String getTimeMonth(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);

		SimpleDateFormat formater = new SimpleDateFormat("MM",Locale.CHINA);
		Date date = null;
		try {
			date = f.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 获取时间的日
	 * @return
	 */
	public static String getTimeDay(String time) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);

		SimpleDateFormat formater = new SimpleDateFormat("dd",Locale.CHINA);
		Date date = null;
		try {
			date = f.parse(time);
			return formater.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}

	}

	@SuppressWarnings("deprecation")
	public static int getYear(String dateString) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd",Locale.CHINA);

		try {
			Date d = formart.parse(dateString);

			return d.getYear();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@SuppressWarnings("deprecation")
	public static int getMonth(String dateString) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd",Locale.CHINA);

		try {
			Date d = formart.parse(dateString);

			return d.getMonth();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@SuppressWarnings("deprecation")
	public static int getDayOfMonth(String dateString) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd",Locale.CHINA);

		try {
			Date d = formart.parse(dateString);

			return d.getDate();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@SuppressWarnings("deprecation")
	public static int getHours(String timeString) {
		SimpleDateFormat formart = new SimpleDateFormat("HH:mm",Locale.CHINA);

		try {
			Date date = formart.parse(timeString);

			return date.getHours();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@SuppressWarnings("deprecation")
	public static int getMinutes(String timeString) {

		SimpleDateFormat formart = new SimpleDateFormat("HH:mm",Locale.CHINA);

		try {
			Date date = formart.parse(timeString);

			return date.getMinutes();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static Date parseTime(String time) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);

		try {
			Date d = formart.parse(time);

			return d;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static long parseTimeMillisecond(String time) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);

		try {
			Date d = formart.parse(time);

			return d.getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static Date parseTaskTime(String time) {

		SimpleDateFormat formart = new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.CHINA);

		try {
			Date d = formart.parse(time);

			return d;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getLastUpdateTimeDesc(String time) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss",Locale.CHINA);

		try {

			String desc = "";

			Date d = simpleDateFormat.parse(time);

			Date n = new Date();

			long delay = n.getTime() - d.getTime();

			long secondsOfHour = 60 * 60;

			long secondsOfDay = secondsOfHour * 24;

			long secondsOfTwoDay = secondsOfDay * 2;

			long secondsOfThreeDay = secondsOfDay * 3;

			// 相差的秒数
			long delaySeconds = delay / 1000;

			if (delaySeconds < 10) {
				desc = "刚刚";
			} else if (delaySeconds <= 60) {
				desc = delaySeconds + "秒前";
			} else if (delaySeconds < secondsOfHour) {
				desc = (delaySeconds / 60) + "分前";
			} else if (delaySeconds < secondsOfDay) {
				desc = (delaySeconds / 60 / 60) + "小时前";
			} else if (delaySeconds < secondsOfTwoDay) {
				desc = "一天前";
			} else if (delaySeconds < secondsOfThreeDay) {
				desc = "两天前";
			}

			return desc;

		} catch (ParseException e) {

			e.printStackTrace();
		}

		return time;

	}
	/**
	 * 获取时间和当前间隔。
	 * @param time
	 * @return
	 */
	public static String getTimeDesc(long time) {

		String desc = "";
		try {
			Date n = new Date();
			long delay = n.getTime() - time;
			long secondsOfHour = 60 * 60;
			long secondsOfDay = secondsOfHour * 24;
			// 相差的秒数
			long delaySeconds = delay / 1000;

			if (delaySeconds < 10) {
				desc = "刚刚";
			} else if (delaySeconds <= 60) {
				desc = delaySeconds + "秒前";
			} else if (delaySeconds < secondsOfHour) {
				desc = (delaySeconds / 60) + "分前";
			} else if (delaySeconds < secondsOfDay) {
				desc = getTimeHM(time);
			}else {
				desc =getCurrentTimeMMDD(time);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return desc;
	}

	/**
	 * 获取两个时间的间隔
	 * @param time
	 * @param nowTime
	 * @return
	 */
	public static String getTimeInterval(long time,long nowTime) {
		String desc = "";
		Date d = new Date(time);

		Date n = new Date(nowTime);

		long delay = n.getTime() - d.getTime();

		long secondsOfHour = 60 * 60;

		long secondsOfDay = secondsOfHour * 24;

		long secondsOfTwoDay = secondsOfDay * 2;

		long secondsOfThreeDay = secondsOfDay * 3;

		// 相差的秒数
		long delaySeconds = delay / 1000;

		if (delaySeconds < 10) {
			desc = "刚刚";
		} else if (delaySeconds <= 60) {
			desc = delaySeconds + "秒前";
		} else if (delaySeconds < secondsOfHour) {
			desc = (delaySeconds / 60) + "分前";
		} else if (delaySeconds < secondsOfDay) {
			desc = (delaySeconds / 60 / 60) + "小时前";
		} else if (delaySeconds < secondsOfTwoDay) {
			desc = "一天前";
		} else if (delaySeconds < secondsOfThreeDay) {
			desc = "两天前";
		}else {
			desc =getCurrentTimeMillisecond(time);
		}

		return desc;

	}

	/**
	 * 获取两个时间的间隔
	 * @param time
	 * @param nowTime
	 * @return
	 */
	public static String getTimeDDMM(long time,long nowTime) {
		String desc = "";
		Date d = new Date(time);

		Date n = new Date(nowTime);

		long delay = n.getTime() - d.getTime();

		long secondsOfHour = 60 * 60;

		long secondsOfDay = secondsOfHour * 24;

		long secondsOfTwoDay = secondsOfDay * 2;

		long secondsOfThreeDay = secondsOfDay * 3;

		// 相差的秒数
		long delaySeconds = delay / 1000;

		if (delaySeconds < secondsOfDay) {
			desc = "今天";
		} else if (delaySeconds < secondsOfTwoDay) {
			desc = "昨天";
		} else if (delaySeconds < secondsOfThreeDay) {
			desc = "前天";
		}else {
			desc =getCurrentTimeDDMM(time);
		}

		return desc;

	}

	/**
	 * 获取两个时间的间隔的分秒数
	 * @param time
	 * @param nowTime
	 * @return
	 */
	public static String getTimeIntervalMMSS(long time,long nowTime) {

		Date d = new Date(time);

		Date n = new Date(nowTime);

		long delay = n.getTime() - d.getTime();

		return getTimeIntervalMMSS(delay);

	}

	/**
	 * 间隔时间转换为分秒。
	 * @param interval 间隔时间毫秒级
	 * @return
	 */
	public static String getTimeIntervalMMSS(long interval) {
		String desc = "";
		long secondsOfHour = 60 * 60;

		long secondsOfDay = secondsOfHour * 24;

		long secondsOfTwoDay = secondsOfDay * 2;

		long secondsOfThreeDay = secondsOfDay * 3;

		// 相差的秒数
		long delaySeconds = interval / 1000;

		if (delaySeconds < 60) {
			desc = String.format(Locale.CHINA,"00:%02d",delaySeconds);
		} else if (delaySeconds < secondsOfHour) {
			desc = String.format(Locale.CHINA,"%02d:%02d",delaySeconds/60,delaySeconds%60);
		} else if (delaySeconds < secondsOfDay) {
			desc = (delaySeconds / 60 / 60) + "小时";
		} else if (delaySeconds < secondsOfTwoDay) {
			desc = "一天";
		} else if (delaySeconds < secondsOfThreeDay) {
			desc = "两天";
		}else {
			desc = "超过两天";
		}

		return desc;
	}

	public static String getTimeIntervalMMSSCN(long interval) {
		String desc = "";
		long secondsOfHour = 60 * 60;

		long secondsOfDay = secondsOfHour * 24;

		long secondsOfTwoDay = secondsOfDay * 2;

		long secondsOfThreeDay = secondsOfDay * 3;

		// 相差的秒数
		long delaySeconds = interval / 1000;

		if (delaySeconds < 60) {
			desc = String.format(Locale.CHINA,"00分%02d秒",delaySeconds);
		} else if (delaySeconds < secondsOfHour) {
			desc = String.format(Locale.CHINA,"%02d分%02d秒",delaySeconds/60,delaySeconds%60);
		} else if (delaySeconds < secondsOfDay) {
			desc = (delaySeconds / 60 / 60) + "小时";
		} else if (delaySeconds < secondsOfTwoDay) {
			desc = "一天";
		} else if (delaySeconds < secondsOfThreeDay) {
			desc = "两天";
		}else {
			desc = "超过两天";
		}

		return desc;
	}

	private static String getWeek(long timeStamp) {
		int myDate = 0;
		String week = null;
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date(timeStamp));
		myDate = cd.get(Calendar.DAY_OF_WEEK);
		// 获取指定日期转换成星期几
		if (myDate == 1) {
			week = "周日";
		} else if (myDate == 2) {
			week = "周一";
		} else if (myDate == 3) {
			week = "周二";
		} else if (myDate == 4) {
			week = "周三";
		} else if (myDate == 5) {
			week = "周四";
		} else if (myDate == 6) {
			week = "周五";
		} else if (myDate == 7) {
			week = "周六";
		}
		return week;
	}

	public static String getTimeAndWeek(long time){
		StringBuilder s=new StringBuilder();
		s.append(getTimeMDCN(time));
		s.append("\u3000");
		s.append(getWeek(time));
		return s.toString();
	}
}
