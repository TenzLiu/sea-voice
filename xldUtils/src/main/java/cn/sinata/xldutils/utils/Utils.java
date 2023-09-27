package cn.sinata.xldutils.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.sinata.xldutils.BuildConfig;
import cn.sinata.xldutils.xldUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 和一些系统相关的方法
 * @author Administrator
 *
 */
public class Utils {

	/**
	 * 获取总内存
	 */
	public void getTotalMemory() {
		String str1 = "/proc/meminfo";
		String str2 = "";
		try {
			FileReader fr = new FileReader(str1);
			@SuppressWarnings("resource")
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				Log.i("DEVICE", "---" + str2);
			}
		} catch (IOException e) {

		}
	}

	/**
	 * 获取可用内存
	 * @return
	 */
	public long getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		return mi.availMem;
	}
	
	/**
	 * 获取rom内存
	 * @return
	 */
	public long[] getRomMemroy() {
		long[] romInfo = new long[2];
		romInfo[0] = getTotalInternalMemorySize();
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		@SuppressWarnings("deprecation")
		long blockSize = stat.getBlockSize();
		@SuppressWarnings("deprecation")
		long availableBlocks = stat.getAvailableBlocks();
		romInfo[1] = blockSize * availableBlocks;
		getVersion();
		return romInfo;
	}

	public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		@SuppressWarnings("deprecation")
		long blockSize = stat.getBlockSize();
		@SuppressWarnings("deprecation")
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 获取内存卡空间大小
	 * @return
	 */
	public long[] getSDCardMemory() {
		long[] sdCardInfo = new long[2];
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			@SuppressWarnings("deprecation")
			long bSize = sf.getBlockSize();
			@SuppressWarnings("deprecation")
			long bCount = sf.getBlockCount();
			@SuppressWarnings("deprecation")
			long availBlocks = sf.getAvailableBlocks();

			sdCardInfo[0] = bSize * bCount;// 总大小
			sdCardInfo[1] = bSize * availBlocks;// 可用大小
		}
		return sdCardInfo;
	}

	/**
	 * 电池信息广播
	 */
	@SuppressWarnings("unused")
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			// level加%就是当前电量了
		}
	};

	/**
	 * 获取cpu信息
	 * @return
	 */
	public String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return cpuInfo;
	}

	/**
	 * 检测当的网络（WLAN、3G/2G）状态
	 *
	 * @param context Context
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (context == null) return true;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}

	public String[] getVersion() {
		String[] version = { "null", "null", "null", "null" };
		String str1 = "/proc/version";
		String str2;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			version[0] = arrayOfString[2];// KernelVersion
			localBufferedReader.close();
		} catch (IOException e) {
		}
		version[1] = Build.VERSION.RELEASE;// firmware version
		version[2] = Build.MODEL;// model
		version[3] = Build.DISPLAY;// system version
		return version;
	}

	public String getVersionString() {
		String[] array = getVersion();
		String RESULT = new String();
		for (String str : array) {
			RESULT += str + ";";
		}
		return RESULT;
	}
  	
	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 * @param context
	 * @param imageUri
	 */
	@SuppressLint("NewApi")
	public static String getUrlPath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
  	
  	 /**
  	   * 获取版本号
  	   * @return 当前应用的版本号
  	   */
	public static String getAppVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "未获取到版本号";
		}
	}
	
	/**
	   * 获取版本
	   * @return 当前应用的版本
	   */
	public static int getAppVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
			int code = info.versionCode;
			return code;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 获取ip地址
	 * @param context
	 * @return
	 */
	public static String getIpAddress(Context context){
		//获取wifi服务  
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        	return "未获取到ip";
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();   
        String ip = intToIp(ipAddress); 
        return ip;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
	/**
	 * 获取mac地址
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {  
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      //判断wifi是否开启  
        if (!wifi.isWifiEnabled()){
        	return "未获取到mac";
        } 
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress();
    } 
	/**
	 * get the network type 1：Wifi，2：2G，3：3G，4：4G
	 * @param context
	 * @return 1：Wifi，2：2G，3：3G，4：4G
	 */
	public static int getNetworkType(Context context){
		ConnectivityManager connectMgr = (ConnectivityManager) context
		        .getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (info==null) {
			return -1;
		}
		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return 1;//wifi
		}
		if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			if (info.getSubtype()==TelephonyManager.NETWORK_TYPE_CDMA 
					|| info.getSubtype()==TelephonyManager.NETWORK_TYPE_EDGE
					|| info.getSubtype()==TelephonyManager.NETWORK_TYPE_GPRS) {
				return 2;//2G
			}
			if (info.getSubtype()==TelephonyManager.NETWORK_TYPE_HSDPA
					|| info.getSubtype()==TelephonyManager.NETWORK_TYPE_UMTS
					|| info.getSubtype()==TelephonyManager.NETWORK_TYPE_EVDO_0
					|| info.getSubtype()==TelephonyManager.NETWORK_TYPE_EVDO_A
					|| info.getSubtype()==TelephonyManager.NETWORK_TYPE_EVDO_B) {
				return 3;//3G
			}
			if (info.getSubtype()==TelephonyManager.NETWORK_TYPE_LTE) {
				return 4;//4G
			}
		}
		return -1;
	}
	
	/**
	 * 是否有sd卡
	 * @return
	 */
	public static boolean ExistSDCard() {  
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;  
		} else  
			return false;  
	}
	/**
	 * sd卡剩余空间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getSDFreeSize(){  
		//取得SD卡文件路径  
		File path = Environment.getExternalStorageDirectory();   
		StatFs sf = new StatFs(path.getPath());  
		//获取单个数据块的大小(Byte)  
		long blockSize ; 
		//空闲的数据块的数量  
		long freeBlocks ;  
		if (Build.VERSION.SDK_INT>=18) {
			blockSize = sf.getBlockSizeLong();
			freeBlocks = sf.getAvailableBlocksLong();
		}else {
			blockSize = sf.getBlockSize();
			freeBlocks = sf.getAvailableBlocks();
		}
		//返回SD卡空闲大小  
		//return freeBlocks * blockSize;  //单位Byte  
		//return (freeBlocks * blockSize)/1024;   //单位KB  
		return (freeBlocks * blockSize)/1024 /1024; //单位MB  
	}

	/**
	 * 删除号码中的所有非数字
	 *
	 * @param str
	 * @return
	 */
	public static String filterUnNumber(String str) {
		if (str == null) {
			return null;
		}
		if (str.startsWith("+86")) {
			str = str.substring(3, str.length());
		}

//		if (str.contains("#")) {
//
//			return str.replaceAll("#", "@");
//		}

		// 只允数字
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		// 替换与模式匹配的所有字符（即非数字的字符将被""替换）
		// 对voip造成的负数号码，做处理
		if (str.startsWith("-")) {
			return "-" + m.replaceAll("").trim();
		} else {
			return m.replaceAll("").trim();
		}

	}
	public static final String PHONE_PREFIX = "+86";
	/**
	 * 去除+86
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static String formatPhone(String phoneNumber) {
		if (phoneNumber == null) {
			return "";
		}
		if (phoneNumber.startsWith(PHONE_PREFIX)) {
			return phoneNumber.substring(PHONE_PREFIX.length()).trim();
		}
		return phoneNumber.trim();
	}

	/**
	 *
	 * @param stream
	 * @param dip
	 * @return
	 */
	public static Bitmap decodeStream(InputStream stream, float dip) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		if (dip != 0.0F) {
			options.inDensity = (int) (160.0F * dip);
		}
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		setInNativeAlloc(options);
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
			return bitmap;
		} catch (OutOfMemoryError e) {
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			setInNativeAlloc(options);
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(stream, null,
						options);
				return bitmap;
			} catch (OutOfMemoryError e2) {
			}
		}
		return null;
	}
	public static boolean inNativeAllocAccessError = false;
	public static void setInNativeAlloc(BitmapFactory.Options options) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
				&& !inNativeAllocAccessError) {
			try {
				BitmapFactory.Options.class.getField("inNativeAlloc")
						.setBoolean(options, true);
				return;
			} catch (Exception e) {
				inNativeAllocAccessError = true;
			}
		}
	}

	/**
	 * 调用拨号页面
	 * @param context 上下文
	 * @param phone 电话号码
	 */
	public static void callPhone(final Context context, final String phone){
		RxPermissions rxPermissions = new RxPermissions((Activity) context);
		rxPermissions.request(Manifest.permission.CALL_PHONE)
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(@NonNull Boolean aBoolean) throws Exception {
						if (!aBoolean) {
							Toast.create(context).show("没有拨号权限！");
						} else {
							Intent intent = new Intent(Intent.ACTION_DIAL);
							intent.setData(Uri.parse("tel:"+phone));
							context.startActivity(intent);
						}
					}
				});
	}

	/**
	 * 调用发送短信页面
	 */
	public static void sendSMS(Context context,String phone,String sms){
		Uri uri = Uri.parse("smsto:" + phone);
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
		sendIntent.putExtra("sms_body", sms);
		context.startActivity(sendIntent);
	}


	/**
	 * 输出err信息
	 * @param msg 信息
	 */
	public static void systemErr(Object msg){

		if (xldUtils.DEBUG){
			try {
				Log.e("cn.sinata.log",String.valueOf(msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 输出err信息
	 * @param msg 信息
     */
	public static void systemErr(String msg){

		if (xldUtils.DEBUG){
			try {
				Log.e("cn.sinata.log",msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * @param context 上下文
	 * @return 状态
	 */
	public static final boolean isOpenLocationProvider(final Context context) {
		LocationManager locationManager
				= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}

	/**
	 * 输入金钱类型判断
	 * @param s 输入框文字
	 * @param length 小数点后位数
     */
	public static void inputMoneyString(Context context,Editable s,int length,int maxNum){
		//只能出现一个小数点，并且小数点后最多n位
		String editable = s.toString();
		int index = editable.indexOf('.');
		if (editable.contains(".") && index!=editable.length()-1){
			if (s.length() > 0) {
				int pos = s.length() - 1;
				char c = s.charAt(pos);

				if (c == '.' || pos>index+length) {
					//这里限制在字串最后追加.
					s.delete(pos,pos+1);
					Toast.create(context).show(String.format("当前仅支持小数点后%s位",length));
				}
				if (pos == index+length){
					double min = Double.parseDouble(s.toString());
					if (min<1/Math.pow(10,length)){
						s.delete(pos-1,pos);
						s.append("1");
						Toast.create(context).show(String.format("单次金额不能小于%s",s.toString()));
					}
				}

			}

		}
		if (maxNum>0){
			double m ;
			try {
				m =Double.parseDouble(s.toString());
			}catch (NumberFormatException e){
				m=0;
			}
			if (m > maxNum){
				s.clear();
				s.append(String.valueOf(maxNum));
				Toast.create(context).show(String.format("单次金额不能大于%s",maxNum));
			}
		}
	}

	/**
	 * 获取文件后缀
	 * @param file 文件
	 * @return 文件后缀
     */
	public static String getFileSuffix(File file){
		String fileName=file.getName();
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}
	/**
	 * 获取文件后缀
	 * @param path 文件路径
	 * @return 文件后缀
	 */
	public static String getFileSuffix(String path){
		File f =new File(path);
		String fileName=f.getName();
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}

	/**
	 * 获取唯一标识码
	 * @param context 上下文
	 * @return 返回唯一标识码
     */
	@SuppressLint("MissingPermission")
	public static String getUUID(Context context){
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}

	public static boolean isSdk21(){
		return Build.VERSION.SDK_INT>=21;
	}

	// 判断服务是否正在运行
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}
}
