package cn.sinata.xldutils.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.netstatus.NetChangeObserver;
import cn.sinata.xldutils.netstatus.NetStateReceiver;
import cn.sinata.xldutils.netstatus.NetUtils;
import cn.sinata.xldutils.utils.DensityUtil;
import cn.sinata.xldutils.utils.Toast;
import cn.sinata.xldutils.view.utils.ViewHolder;
import cn.sinata.xldutils.widget.ProgressDialog;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    public boolean isDestroy = false;
    protected ViewHolder mHolder;
    protected Context mContext;//上下文
    protected int mScreenWidth;//设备宽
    protected int mScreenHeight;//设备高
    //关闭应用广播action
    private String ACTION_CLOSE_ALL;
    public CompositeDisposable compositeSubscription;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && TextUtils.equals(intent.getAction(), ACTION_CLOSE_ALL)) {
                finish();
            }
        }
    };

    /**
     * network status
     */
    protected NetChangeObserver mNetChangeObserver = null;

    boolean isCanSop = true;//是否竖屏显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeDisposable();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            //设置竖屏显示
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            if (isCanSop) {
                //设置竖屏显示
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        ACTION_CLOSE_ALL = String.format("cn.sinata.base.%s.all.close", getPackageName());
        //注册“关闭页面”广播监听器
        if (isRegisterCloseBroadReceiver()) {
            registerReceiver(broadcastReceiver, new IntentFilter(ACTION_CLOSE_ALL));
        }

        mScreenHeight = DensityUtil.getDeviceHeight(this);
        mScreenWidth = DensityUtil.getDeviceWidth(this);
        mContext = this;
        isDestroy = false;
        mHolder = new ViewHolder(this);

        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                onNetworkDisConnected();
            }
        };

        //按需注册网络变化监听器
        if (shouldRegisterNetworkChangeReceiver()) {
            NetStateReceiver.registerNetworkStateReceiver(this);
            NetStateReceiver.registerObserver(mNetChangeObserver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shouldRegisterNetworkChangeReceiver()) {
            NetStateReceiver.unRegisterNetworkStateReceiver(this);
            NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        }
        if (isRegisterCloseBroadReceiver()) {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (mHolder != null) {
            mHolder.unBind();
        }
        isDestroy = true;
        dismissDialog();
        if (!compositeSubscription.isDisposed()) {
            compositeSubscription.dispose();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    /**
     * 设置是否可以竖屏显示
     *
     * @param showSop
     */
    public void setShowSOP(boolean showSop) {
        isCanSop = showSop;
    }

    /**
     * 是否需要注册“关闭全部页面”广播
     *
     * @return 默认false
     */
    protected boolean isRegisterCloseBroadReceiver() {
        return true;
    }

    /**
     * 是否注册网络变化监听器
     *
     * @return 默认false
     */
    protected boolean shouldRegisterNetworkChangeReceiver() {
        return false;
    }

    /**
     * 显示toast
     *
     * @param s 提示文字
     */
    public void showToast(String s) {
        Toast.create(this).show(s);
    }

    /**
     * 关闭所有页面
     */
    protected void closeAll() {
        Intent intent = new Intent(ACTION_CLOSE_ALL);
        sendBroadcast(intent);
    }

    /**
     * 必须先调用注册网络状态监听广播。否则没有任何反应
     * must call NetStateReceiver.registerNetworkStateReceiver(context) first ,if not,nothing change
     *
     * @param type 网络类型
     */
    protected void onNetworkConnected(NetUtils.NetType type) {
    }

    /**
     * 必须先调用注册网络状态监听广播。否则没有任何反应
     * must call NetStateReceiver.registerNetworkStateReceiver(context) first ,if not,nothing change
     */
    protected void onNetworkDisConnected() {
    }

    /**
     * 绑定视图，简化系统findViewById写法
     *
     * @param resId 视图id
     * @param <T>   视图类型继承自系统的View类
     * @return 返回组件实例
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T bind(int resId) {
        return (T) mHolder.bind(resId);
    }

    /**
     * 显示加载提示窗
     */
    public void showDialog() {
        showDialog("加载中...");
    }

    /**
     * 显示加载提示窗
     *
     * @param msg 提示文字
     */
    protected void showDialog(CharSequence msg) {
        showDialog(msg, false);
    }

    /**
     * 显示加载提示窗
     *
     * @param msg       提示文字
     * @param canCancel 是否可手动取消
     */
    protected void showDialog(CharSequence msg, boolean canCancel) {
        if (isDestroy) {
            return;
        }
        if (dialog == null) {
            dialog = new ProgressDialog(this, R.style.Theme_ProgressDialog);
        }
        dialog.setCanceledOnTouchOutside(canCancel);
        dialog.setMessage(msg);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 关闭加载窗
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
