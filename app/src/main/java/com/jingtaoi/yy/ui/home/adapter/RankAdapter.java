package com.jingtaoi.yy.ui.home.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.RankBean;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.jingtaoi.yy.utils.ImageUtils;

import java.util.List;

import cn.sinata.xldutils.adapter.HFRecyclerAdapter;
import cn.sinata.xldutils.adapter.util.ViewHolder;

public class RankAdapter extends HFRecyclerAdapter<RankBean.DataBean> {
    private int type;
    private int textColor;
    private boolean isModeLiang;
    private boolean isZHYM=true;
    /**
     * 当前用户user_id
     */
    private int currentCUserID;
    /**
     * 当前用户角色  1房主  2管理员  3普通用户
     */
    private int userRoomType = -1;

    public RankAdapter(int textColor,List<RankBean.DataBean> mData,int type, int currentCUserID) {
        super(mData, R.layout.item_rank_user);
        this.type = type;
        this.textColor = textColor;
        this.currentCUserID = currentCUserID;
    }

    public RankAdapter(List<RankBean.DataBean> mData,int type, int currentCUserID, int userRoomType) {
        super(mData, R.layout.item_rank_user);
        this.type = type;
        this.currentCUserID = currentCUserID;
        this.userRoomType = userRoomType;
    }

    @Override
    public void onBind(int position, RankBean.DataBean hotData, ViewHolder holder) {
        ImageView ivSex = holder.bind(R.id.iv_sex);
        ImageView liang_iv = holder.bind(R.id.liang_iv);
        TextView tv_title_1 = holder.bind(R.id.tv_title_1);
        TextView xuzan_tv= holder.bind(R.id.xuzan_tv);
        ConstraintLayout cl_bg= holder.bind(R.id.cl_bg);
        if (hotData.getSex() == 1){
            ivSex.setSelected(true);
        }else
            ivSex.setSelected(false);
        ImageView ivHead = holder.bind(R.id.iv_head);
        ImageUtils.loadImage(ivHead,hotData.getImg(),-1,R.drawable.default_head);
        holder.setText(R.id.tv_name,hotData.getName());
        holder.setText(R.id.tv_rank,""+(position+4));
//        ((TextView)holder.bind(R.id.tv_name)).setCompoundDrawablesWithIntrinsicBounds(type==2?ImageShowUtils.getCharm(hotData.getGrade()):ImageShowUtils.getGrade(hotData.getGrade()),0,0,0);
        holder.setText(R.id.tv_account,"ID:"+hotData.getUsercoding());
        String s1 = "距上名\n" + hotData.getNum();
        SpannableString string1 = new SpannableString(s1);
        string1.setSpan(new ForegroundColorSpan(textColor),3,s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView tvOffset = holder.bind(R.id.tv_offset);
        tvOffset.setText(string1);
        holder.setText(R.id.xuzan_tv,hotData.getNum()+"");
//        holder.setText(R.id.xuzan_tv,hotData.getNum()+"");

        ImageView  zan_icon_iv= holder.bind(R.id.zan_icon_iv);
        if (type==1){
          //土豪榜
            zan_icon_iv.setImageResource(R.drawable.icon_xz_d);
        }else {
            //魅力榜
            zan_icon_iv.setImageResource(R.drawable.icon_ml_d);
        }


        TextView iv_gradetwo_person = holder.bind(R.id.iv_gradetwo_person);
        TextView ivGradePerson = holder.bind(R.id.iv_grade_person);
        if (type==2){

            //魅力等级显示
            iv_gradetwo_person.setBackgroundResource(ImageShowUtils.getGrade(hotData.getGrade()));
            iv_gradetwo_person.setText(ImageShowUtils.getCharmText(hotData.getGrade()));
            iv_gradetwo_person.setVisibility(View.VISIBLE);
            ivGradePerson.setVisibility(View.GONE);
        }else {

            //财富等级显示
            ivGradePerson.setBackgroundResource(ImageShowUtils.getGrade(hotData.getGrade()));
            ivGradePerson.setText(ImageShowUtils.getGradeText(hotData.getGrade()));

            iv_gradetwo_person.setVisibility(View.GONE);
            ivGradePerson.setVisibility(View.VISIBLE);
        }

        if (isModeLiang)
        {
            if (hotData.getIsliang()==1){
                liang_iv.setVisibility(View.VISIBLE);
            }else {
                liang_iv.setVisibility(View.GONE);
            }
        }else {
            if (TextUtils.isEmpty(hotData.getLiang())){
                liang_iv.setVisibility(View.GONE);
            }else {
                liang_iv.setVisibility(View.VISIBLE);
            }
        }

//        if (isZHYM){
//            tv_title_1.setVisibility(View.VISIBLE);
//        }else {
//            tv_title_1.setVisibility(View.GONE);
//        }

        if (userRoomType == 1 || userRoomType == 2){
            // 房主或管理员查看全部距离上一名
        } else {
            // 排行榜只能看到自己
            if (currentCUserID == hotData.getId()){
                tvOffset.setVisibility(View.VISIBLE);
//                xuzan_tv.setVisibility(View.VISIBLE);
//                zan_icon_iv.setVisibility(View.VISIBLE);
//                tv_title_1.setVisibility(View.VISIBLE);
            } else {
//                xuzan_tv.setVisibility(View.INVISIBLE);
//                zan_icon_iv.setVisibility(View.INVISIBLE);
                tvOffset.setVisibility(View.INVISIBLE);
            }
        }

        if (position == mData.size()-1){
            cl_bg.setBackgroundResource(R.drawable.bg_white_bottom_20dp);
        }else
            cl_bg.setBackgroundColor(Color.WHITE);
    }


    /**
     * 设置是否显示最后一名
     */
    public void setZHYM(boolean b){
        isZHYM = b;
    }

    /**
     * true 的话用 Isliang 判断 false 就是用liang 来判断
     * @param b
     */
    public void setModeLiang(boolean b){
        isModeLiang = b;
    }
}
