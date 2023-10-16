package com.jingtaoi.yy.mvp.basemvp;

import cn.sinata.xldutils.activitys.BaseActivity;

public interface BaseView {

    /**
     * 网络请求Call返回
     * @param responseString
     */
    void onCallSuccess(String responseString);

    /**
     * 错误处理
     * @param code
     * @param msg
     */
    void onError(int code, String msg);

    /**
     * 获取 Activity 对象
     *
     * @return activity
     */
    BaseActivity getSelfActivity();
}
