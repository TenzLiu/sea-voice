package com.jingtaoi.yy.ui.room.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.MyRankingBean;
import com.jingtaoi.yy.utils.ImageUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class RankingListAdapter extends BaseQuickAdapter<MyRankingBean.DataBean, BaseViewHolder> {

    private int chooseTop;//1是财富，2是魅力

    public int getChooseTop() {
        return chooseTop;
    }

    public void setChooseTop(int chooseTop) {
        this.chooseTop = chooseTop;
    }

    public RankingListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyRankingBean.DataBean item) {
        SimpleDraweeView iv_show_myranking = helper.getView(R.id.iv_show_myranking);
        ImageUtils.loadUri(iv_show_myranking, item.getImg());
        helper.setText(R.id.tv_name_myranking, item.getName());
        ImageView iv_sex_onlines = helper.getView(R.id.iv_sex_myranking);
        if (item.getSex() == 1) { //男
            iv_sex_onlines.setImageResource(R.drawable.sex_male);
        } else if (item.getSex() == 2) {
            iv_sex_onlines.setImageResource(R.drawable.sex_female);
        }
        TextView tv_endcenter_myranking = helper.getView(R.id.tv_endcenter_myranking);
        ImageView iv_grade_myranking = helper.getView(R.id.iv_grade_myranking);
        int resShowid;
        if (chooseTop == 1) {
            tv_endcenter_myranking.setText(item.getNum() + mContext.getString(R.string.tv_up_ranking));
            resShowid = ImageShowUtils.getGrade(item.getGrade());
            iv_grade_myranking.setImageResource(resShowid);
        } else if (chooseTop == 2) {
            tv_endcenter_myranking.setText(item.getNum() + mContext.getString(R.string.tv_down_ranking));
            resShowid = ImageShowUtils.getCharm(item.getGrade());
            iv_grade_myranking.setImageResource(resShowid);
        }

        TextView tv_gradeshow_myranking = helper.getView(R.id.tv_gradeshow_myranking);
        ImageView iv_gradeshow_myranking = helper.getView(R.id.iv_gradeshow_myranking);


        int rankShow = helper.getAdapterPosition();
        tv_gradeshow_myranking.setText(String.valueOf(rankShow + 1));
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/impact.ttf");
        tv_gradeshow_myranking.setTypeface(typeface);

        tv_gradeshow_myranking.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        iv_gradeshow_myranking.setVisibility(View.VISIBLE);
        tv_gradeshow_myranking.setVisibility(View.INVISIBLE);
        if (rankShow == 0) {
            iv_gradeshow_myranking.setImageResource(R.drawable.goldmedal);
        } else if (rankShow == 1) {
            iv_gradeshow_myranking.setImageResource(R.drawable.silvermedal);
        } else if (rankShow == 2) {
            iv_gradeshow_myranking.setImageResource(R.drawable.bronzemedal);
        } else {
            tv_gradeshow_myranking.setVisibility(View.VISIBLE);
            tv_gradeshow_myranking.setTextColor(ContextCompat.getColor(mContext, R.color.textColor7));
            iv_gradeshow_myranking.setVisibility(View.INVISIBLE);
        }
    }

}
