package com.jingtaoi.yy.ui.mine.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.GetOneBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 魅力等级
 */
public class CharmFragment extends MyBaseFragment {
    @BindView(R.id.iv_rule_charm)
    ImageView ivRuleCharm;
    @BindView(R.id.iv_header_charm)
    SimpleDraweeView ivHeaderCharm;
    @BindView(R.id.tv_grade_charm)
    ImageView tvGradeCharm;
    @BindView(R.id.mProgress_charm)
    ProgressBar mProgressCharm;
    @BindView(R.id.tv_next_charm)
    TextView tvNextCharm;
    @BindView(R.id.iv_three_grade)
    ImageView ivThreeGrade;
    @BindView(R.id.tv_three_grade)
    TextView tvThreeGrade;
    @BindView(R.id.tv_name)
    TextView tv_name;
    Unbinder unbinder;
    int charm;
    @BindView(R.id.iv_grade_radioda)
    TextView ivGradeRadioda;
    @BindView(R.id.tv_cast)
    TextView tvCast;
    @BindView(R.id.iv_current)
    ImageView ivCurrent;
    @BindView(R.id.iv_next)
    ImageView ivNext;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charm, container, false);
    }

    @Override
    public void initView() {

        initShow();
        getCall();
    }

    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("state", Const.IntShow.TWO);
        HttpManager.getInstance().post(Api.UserGold, map, new MyObserver(this) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    if (charm != getOneBean.getData().getGrade()) {
                        SharedPreferenceUtils.put(getContext(), Const.User.GRADE_T, getOneBean.getData().getGrade());
                        initShow();
                    }
                    if (mProgressCharm != null) {
                        mProgressCharm.setMax(getOneBean.getData().getZd());
                        mProgressCharm.setProgress(getOneBean.getData().getXz());
                    }
                    if (tvCast!=null){
                        tvCast.setText(getOneBean.getData().getXz()+"");
                        tvNextCharm.setText("距离下一级还需要获得" + getOneBean.getData().getGradeNum() + "魅力");
                    }
                } else {
                    showToast(getOneBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initShow() {
        String img = (String) SharedPreferenceUtils.get(getContext(), Const.User.IMG, "");
        charm = (int) SharedPreferenceUtils.get(getContext(), Const.User.CharmGrade, 0);
        tv_name.setText((String) SharedPreferenceUtils.get(getContext(), Const.User.NICKNAME, ""));

        ImageUtils.loadUri(ivHeaderCharm, img);
//        tvGradeCharm.setText("" + charm);
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/impact.ttf");
//        tvGradeCharm.setTypeface(typeface);


        tvGradeCharm.setImageResource(ImageShowUtils.getCharm(charm));
        ivCurrent.setImageResource(ImageShowUtils.getCharm(charm));
        ivNext.setImageResource(ImageShowUtils.getCharm(charm+1));
//        tvGradeCharm.setText(ImageShowUtils.getCharmText(charm));
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

    @OnClick(R.id.iv_rule_charm)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.TYPE, Const.IntShow.TWO);
        ActivityCollector.getActivityCollector().toOtherActivity(GradeShowActivity.class, bundle);
    }
}
