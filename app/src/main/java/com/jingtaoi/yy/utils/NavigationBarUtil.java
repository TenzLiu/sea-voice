//package com.cityrise.uuvoice.utils;
//
//import android.content.Context;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.os.Build;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.util.DisplayMetrics;
//import android.view.Display;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.view.Window;
//import android.view.WindowManager;
//
//import java.lang.reflect.Method;
//
//public class NavigationBarUtil {
//    /**
//     * 判断虚拟导航栏是否显示
//     *
//     * @param context 上下文对象
//     * @param window  当前窗口
//     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
//     */
//    public static boolean checkNavigationBarShow(@NonNull Context context, @NonNull Window window) {
//        boolean show;
//        if (Build.MANUFACTURER.equals("Xiaomi")) {
//            show = xiaomiNavigationGestureEnabled(context);
//        } else {
//            Display display = window.getWindowManager().getDefaultDisplay();
//            Point point = new Point();
//            display.getRealSize(point);
//            View decorView = window.getDecorView();
//            Configuration conf = context.getResources().getConfiguration();
//            if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
//                View contentView = decorView.findViewById(android.R.id.content);
//                show = (point.x != contentView.getWidth());
//            } else {
//                Rect rect = new Rect();
//                decorView.getWindowVisibleDisplayFrame(rect);
//                show = (rect.bottom != point.y);
//            }
//        }
//        return show;
//    }
//
//    /**
//     * 小米手机是否开启手势
//     *
//     * @param context
//     * @return
//     */
//    public static boolean xiaomiNavigationGestureEnabled(Context context) {
//        int val = Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0);
//        return val != 0;
//    }
//
//    public static int getHeightOfNavigationBar(Context context) {
//        //如果小米手机开启了全面屏手势隐藏了导航栏则返回 0
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            if (Settings.Global.getInt(context.getContentResolver(),
//                    "force_fsg_nav_bar", 0) != 0) {
//                return 0;
//            }
//        }
//        int realHeight = getScreenSize(context)[1];
//
//        Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
//                .getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        d.getMetrics(displayMetrics);
//
//        int displayHeight = displayMetrics.heightPixels;
//
//        return realHeight - displayHeight;
//    }
//
//    public static int[] getScreenSize(Context context) {
//        int[] size = new int[2];
//
//        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display d = w.getDefaultDisplay();
//        DisplayMetrics metrics = new DisplayMetrics();
//        d.getMetrics(metrics);
//        // since SDK_INT = 1;
//        int widthPixels = metrics.widthPixels;
//        int heightPixels = metrics.heightPixels;
//
//        // includes window decorations (statusbar bar/menu bar)
//        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
//            try {
//                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
//                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
//            } catch (Exception ignored) {
//            }
//        // includes window decorations (statusbar bar/menu bar)
//        if (Build.VERSION.SDK_INT >= 17)
//            try {
//                Point realSize = new Point();
//                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
//                widthPixels = realSize.x;
//                heightPixels = realSize.y;
//            } catch (Exception ignored) {
//            }
//        size[0] = widthPixels;
//        size[1] = heightPixels;
//        return size;
//    }
//
//    /**
//     * 获取设备是否存在NavigationBar
//     *
//     * @param context
//     * @return
//     */
//    public static boolean checkDeviceHasNavigationBar(Context context) {
//        boolean hasNavigationBar = false;
//        Resources rs = context.getResources();
//        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
//        if (id > 0) {
//            hasNavigationBar = rs.getBoolean(id);
//        }
//        try {
//            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
//            Method m = systemPropertiesClass.getMethod("get", String.class);
//            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
//            if ("1".equals(navBarOverride)) {
//                hasNavigationBar = false;
//            } else if ("0".equals(navBarOverride)) {
//                hasNavigationBar = true;
//            }
//        } catch (Exception e) {
//            //do something
//        }
//        return hasNavigationBar;
//    }
//
//    /**
//     * 获取NavigationBar的高度
//     *
//     * @return
//     */
//    public static int getNavigationBarHeight(Context context) {
//        Resources resources = context.getResources();
//        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//        int height = resources.getDimensionPixelSize(resourceId);
//        return height;
//    }
//
//    /**
//     * 获取手机屏幕高度
//     */
//    public static int getHeight(Context context) {
//        DisplayMetrics dm = new DisplayMetrics();
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        windowManager.getDefaultDisplay().getMetrics(dm);
//        return dm.heightPixels;
//    }
//
//    /**
//     * 获取屏幕真实高度（包括虚拟键盘）
//     */
//    public static int getRealHeight(Context context) {
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
//        DisplayMetrics dm = new DisplayMetrics();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            display.getRealMetrics(dm);
//        } else {
//            display.getMetrics(dm);
//        }
//        int realHeight = dm.heightPixels;
//        return realHeight;
//    }
//
//    //获取屏幕原始尺寸高度，包括虚拟功能键高度
//    private int getDpi(Context context) {
//        int dpi = 0;
//        WindowManager windowManager = (WindowManager)
//                context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        @SuppressWarnings("rawtypes")
//        Class c;
//        try {
//            c = Class.forName("android.view.Display");
//            @SuppressWarnings("unchecked")
//            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
//            method.invoke(display, displayMetrics);
//            dpi = displayMetrics.heightPixels;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dpi;
//    }
//
//
//    public interface NavigationListener {
//        void show();
//
//        void hide();
//    }
//
//    //虚拟导航栏显示/隐藏
//    public static void setNavigationListener(final View rootView, Context context, final NavigationListener navigationListener) {
//        if (rootView == null || navigationListener == null) {
//            return;
//        }
//        if (getRealHeight(context) != getHeight(context)) {
//            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                int rootViewHeight;
//
//                @Override
//                public void onGlobalLayout() {
//                    int viewHeight = rootView.getHeight();
//                    if (rootViewHeight != viewHeight) {
//                        rootViewHeight = viewHeight;
//                        if (viewHeight == getRealHeight(context)) {
//                            //隐藏虚拟按键
//                            if (navigationListener != null) {
//                                navigationListener.hide();
//                            }
//                        } else {
//                            //显示虚拟按键
//                            if (navigationListener != null) {
//                                navigationListener.show();
//                            }
//                        }
//                    }
//                }
//            });
//        }
//    }
//}
