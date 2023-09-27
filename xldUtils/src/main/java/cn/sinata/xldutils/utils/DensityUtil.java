package cn.sinata.xldutils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import androidx.core.view.ViewConfigurationCompat;

/**
 * 设备显示相关参数
 * @author Administrator
 *
 */
public class DensityUtil {
	
	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getDeviceWidth(Context context){ 
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels; 
	}
	

	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getDeviceHeight(Context context){ 
		
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
	}
	
	/**
	 * dip转换px
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 

	/**
	 * px转换dip
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
	} 
	
	/**
     * 将sp值转换为px值，保证文字大小不变
     * @param context 上下文
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    }

	public static float getDensity(Context context){
		final float density = context.getResources().getDisplayMetrics().density;
		return density;
	}

	// 判断设备是否有返回键、菜单键来确定是否有 NavigationBar
	public static boolean hasNavigationBar(Context context) {
		boolean hasMenuKey = ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(context));
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		if (!hasMenuKey && !hasBackKey) {
			return true;
		}
		return false;
	}

	// 获取 NavigationBar 的高度
	public static int getNavigationBarHeight(Activity activity) {
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		return resources.getDimensionPixelSize(resourceId);
	}
}
