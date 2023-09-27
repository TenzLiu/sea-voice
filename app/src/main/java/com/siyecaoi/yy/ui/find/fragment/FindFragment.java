package com.siyecaoi.yy.ui.find.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.find.MyInviteShowActivity;
import com.siyecaoi.yy.ui.find.RadioDatingActivity;
import com.siyecaoi.yy.ui.find.RealnameActivity;
import com.siyecaoi.yy.ui.home.RecommendActivity;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/12/19.
 */

public class FindFragment extends MyBaseFragment {
    @BindView(R.id.iv_broadcast_find)
    ImageView ivBroadcastFind;
    @BindView(R.id.iv_dis_find)
    ImageView ivDisFind;
    @BindView(R.id.tv_name_find)
    TextView tvNameFind;
    @BindView(R.id.tv_five_find)
    TextView tvFiveFind;
    @BindView(R.id.tv_att_find)
    TextView tvAttFind;
    @BindView(R.id.tv_send_find)
    TextView tvSendFind;
    Unbinder unbinder;
    @BindView(R.id.iv_com_find)
    ImageView ivComFind;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_broadcast_find, R.id.iv_dis_find, R.id.tv_name_find,
            R.id.tv_five_find, R.id.tv_att_find, R.id.tv_send_find, R.id.iv_com_find})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_broadcast_find:
                ActivityCollector.getActivityCollector().toOtherActivity(RadioDatingActivity.class);
                break;
            case R.id.iv_dis_find:
                ActivityCollector.getActivityCollector().toOtherActivity(MyInviteShowActivity.class);
                break;
            case R.id.tv_name_find:
                getRealNameCall();
                break;
            case R.id.iv_com_find:
                startActivity(new Intent(getActivity(), RecommendActivity.class));
                break;
            case R.id.tv_five_find:
                bundle.putInt(Const.ShowIntent.TYPE, Const.IntShow.SEVEN);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.tv_five_find));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.tv_att_find:
                bundle.putInt(Const.ShowIntent.TYPE, Const.IntShow.EIGHT);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.tv_att_find));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.tv_send_find:
                bundle.putInt(Const.ShowIntent.TYPE, Const.IntShow.TEN);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.tv_send_find));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
        }
    }

    private void getRealNameCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.audit, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    //0未申请，1 待审核，2 审核通过， 3 已拒绝
                    int state = getOneBean.getData().getState();
                    if (state == 0) {
                        ActivityCollector.getActivityCollector().toOtherActivity(RealnameActivity.class);
                    } else if (state == 1) {
                        showToast("实名认证审核中，请耐心等待");
                    } else if (state == 2) {
                        showToast("实名认证通过");
                    } else if (state == 3) {
                        showToast("实名认证失败，请重新填写相关信息");
                        ActivityCollector.getActivityCollector().toOtherActivity(RealnameActivity.class);
                    }
                } else {
                    showToast(getOneBean.getMsg());
                }
            }
        });
    }
}
