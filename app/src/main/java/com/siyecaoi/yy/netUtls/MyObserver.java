package com.siyecaoi.yy.netUtls;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.siyecaoi.yy.utils.LogUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.sinata.xldutils.activitys.BaseActivity;
import cn.sinata.xldutils.fragment.BaseFragment;
import cn.sinata.xldutils.net.utils.Error;
import cn.sinata.xldutils.net.utils.ResultException;
import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Toast;
import cn.sinata.xldutils.utils.Utils;
import cn.sinata.xldutils.xldUtils;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 自定义Observer类处理网络数据
 * Created by Administrator on 2018/1/25.
 */

public abstract class MyObserver implements Observer<ResponseBody>, Disposable {

    WeakReference<BaseActivity> activities;
    WeakReference<BaseFragment> fragments;
    Context context;


    public MyObserver(BaseActivity activity) {
        activities = new WeakReference<>(activity);
        if (activity != null) {
            this.context = activity;
            activity.compositeSubscription.add(this);
        }
    }

    public MyObserver(BaseFragment fragment) {
        fragments = new WeakReference<BaseFragment>(fragment);
        if (fragment != null && !fragment.isDetached()) {
            this.context = fragment.getContext();
            fragment.addDisposable(this);
        }
    }

    public MyObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull ResponseBody responseBody) {
        dismissDialog();
        try {
            String responseString = responseBody.string();
            LogUtils.i("responseString", "**" + responseString);
            if (StringUtils.isEmpty(responseString)) { //判空
                int code = -1;
                String msg = Error.PARSER_ERROR;
                onError(code, msg);
                return;
            }
//            success(responseString);
            BaseBean baseBean = new Gson().fromJson(responseString, BaseBean.class);
            if (baseBean.getCode() == Api.SUCCESS) {
                success(responseString);
            } else if (baseBean.getCode() == 3) {//为3验证token失败，重新登录
                onError(baseBean.getCode(), baseBean.getMsg());
                BroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(Const.BroadCast.EXIT);
            } else {
                success(responseString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        dismissDialog();
        if (xldUtils.DEBUG) {
            e.printStackTrace();
        }
        Utils.systemErr(e);
        LogUtils.i("responseString", "responseString get  " + e.toString());
        int code = -1;
        String msg = Error.REQUEST_ERROR;
        if (e instanceof JsonSyntaxException ||
                e instanceof NumberFormatException ||
                e instanceof IllegalStateException) {
            msg = Error.PARSER_ERROR;
            onError(code, msg);
        } else if (e instanceof HttpException) {
            msg = Error.SERVER_ERROR;
            onError(code, msg);
        } else if (e instanceof ConnectException) {
            msg = Error.NET_ERROR;
            onError(code, msg);
        } else if (e instanceof SocketTimeoutException) {
            msg = Error.NET_TIMEOUT;
            onError(code, msg);
        } else if (e instanceof ResultException) {
            code = ((ResultException) e).getCode();
            msg = e.getMessage();
            onError(code, msg);
        }else if (e instanceof UnknownHostException){
            msg = Error.NET_ERROR;
            onError(code, msg);
        }

    }

    protected void onError(int code, String msg) {
        if (shouldShowErrorToast()) {
            showToast(msg);
        }
    }

    protected boolean shouldShowErrorToast() {
        return true;
    }

    @Override
    public void onComplete() {

    }

    /**
     * 显示toast
     *
     * @param s 提示文字
     */
    protected void showToast(String s) {
        Toast.create(context).show(s);
    }

    /**
     * 访问成功回调抽象方法
     *
     * @param responseString 返回的数据
     */
    public abstract void success(String responseString);

    /**
     * 访问成功回调方法
     *
     * @param bitmap 获取的Bitmap
     */
    public void success(Bitmap bitmap) {
    }

    private void dismissDialog() {
        if (activities != null) {
            BaseActivity activity = activities.get();
            if (activity != null) {
                activity.dismissDialog();

            }
        }
        if (fragments != null) {
            BaseFragment fragment = fragments.get();
            if (fragment != null) {
                fragment.dismissDialog();

            }
        }
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

    @Override
    public void dispose() {

    }
}
