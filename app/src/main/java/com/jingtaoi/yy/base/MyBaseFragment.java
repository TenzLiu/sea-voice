package com.jingtaoi.yy.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;

import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import cn.sinata.xldutils.fragment.BaseFragment;


/**
 * MyBaseFragment
 * Created by Administrator on 2018/5/16.
 */

public abstract class MyBaseFragment extends BaseFragment {
    protected final int PAGE_SIZE = 10;//每页条数
    protected int page = 1;//当前第几页
    protected int userToken;

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected void onFirstVisibleToUser() {

    }

    @Override
    protected void onVisibleToUser() {

    }

    @Override
    protected void onInvisibleToUser() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setResume();
        setOnclick();
    }

    public abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public abstract void initView();

    public abstract void setResume();

    public abstract void setOnclick();


    //更新userToken
    public void updateUserToken() {
        userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);
    }
}
