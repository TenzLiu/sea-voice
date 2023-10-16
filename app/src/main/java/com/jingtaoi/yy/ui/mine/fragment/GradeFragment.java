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
import androidx.core.content.ContextCompat;

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
 * 财富等级
 */
public class GradeFragment extends MyBaseFragment {
    @BindView(R.id.iv_rule_grade)
    ImageView ivRuleGrade;
    @BindView(R.id.iv_header_grade)
    SimpleDraweeView ivHeaderGrade;
    @BindView(R.id.tv_grade_grade)
    ImageView tvGradeGrade;
    @BindView(R.id.mProgress_grade)
    ProgressBar mProgressGrade;
    @BindView(R.id.tv_next_grade)
    TextView tvNextGrade;
    @BindView(R.id.iv_one_grade)
    ImageView ivOneGrade;
    @BindView(R.id.tv_one_grade)
    TextView tvOneGrade;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_two_grade)
    ImageView ivTwoGrade;
    @BindView(R.id.tv_two_grade)
    TextView tvTwoGrade;
    @BindView(R.id.iv_three_grade)
    ImageView ivThreeGrade;
    @BindView(R.id.tv_three_grade)
    TextView tvThreeGrade;
    Unbinder unbinder;

    int grade;
    @BindView(R.id.iv_grade_radioda)
    TextView ivGradeRadioda;
    @BindView(R.id.tv_cast)
    TextView tvCast;
    @BindView(R.id.iv_current)
    ImageView ivCurrent;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.center)
    TextView center;
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grade, container, false);
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
        map.put("state", Const.IntShow.ONE);
        HttpManager.getInstance().post(Api.UserGold, map, new MyObserver(this) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    if (grade != getOneBean.getData().getGrade()) {
                        SharedPreferenceUtils.put(getContext(), Const.User.GRADE_T, getOneBean.getData().getGrade());
                        initShow();
                    }
                    if (tvCast!=null){
                        tvCast.setText(getOneBean.getData().getXz() + "");
                        mProgressGrade.setMax(getOneBean.getData().getZd());
                        mProgressGrade.setProgress(getOneBean.getData().getXz());
                        tvNextGrade.setText("距离下一级还需要消耗" + getOneBean.getData().getGradeNum() + "浪花");
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
        grade = (int) SharedPreferenceUtils.get(getContext(), Const.User.GRADE_T, 0);

        ImageUtils.loadUri(ivHeaderGrade, img);
        tv_name.setText((String) SharedPreferenceUtils.get(getContext(), Const.User.NICKNAME, ""));
        tvGradeGrade.setImageResource(ImageShowUtils.getGrade(grade));
        ivCurrent.setImageResource(ImageShowUtils.getGrade(grade));
        ivNext.setImageResource(ImageShowUtils.getGrade(grade + 1));
//        tvGradeGrade.setText(ImageShowUtils.getGradeText(grade));

//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/impact.ttf");
//        tvGradeGrade.setTypeface(typeface);


        if (grade > 0) {
            ivOneGrade.setImageResource(R.drawable.privilege_rank1);
            tvOneGrade.setText("Lv.1开启");
            tvOne.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
//            tvOneGrade.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
            tvOneGrade.setBackgroundResource(R.drawable.bg_round5_ffo1);
        }
        if (grade > 9) {
            ivTwoGrade.setImageResource(R.drawable.privilege_effect1);
            tvTwo.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
//            tvTwoGrade.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
            tvTwoGrade.setBackgroundResource(R.drawable.bg_round5_ffo1);
        }
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

    @OnClick(R.id.iv_rule_grade)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.TYPE, Const.IntShow.ONE);
        ActivityCollector.getActivityCollector().toOtherActivity(GradeShowActivity.class, bundle);
    }
}
