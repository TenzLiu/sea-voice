package com.jingtaoi.yy.ui.find.adapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.InviteRankingBean;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.MyUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class InviteRankingRecyAdapter extends BaseQuickAdapter<InviteRankingBean.DataEntity.UserListEntity, BaseViewHolder> {

    public InviteRankingRecyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, InviteRankingBean.DataEntity.UserListEntity item) {
        SimpleDraweeView iv_show_ranking = helper.getView(R.id.iv_show_ranking);
        ImageView iv_gradeshow_ranking = helper.getView(R.id.iv_gradeshow_ranking);
        ImageView iv_sex_ranking = helper.getView(R.id.iv_sex_ranking);
        TextView tv_gradeshow_ranking = helper.getView(R.id.tv_gradeshow_ranking);
        TextView tv_name_ranking = helper.getView(R.id.tv_name_ranking);
        TextView tv_show_ranking = helper.getView(R.id.tv_show_ranking);

        ImageUtils.loadUri(iv_show_ranking, item.getImg());

        if (item.getSex() == 1) { //男
            iv_sex_ranking.setImageResource(R.drawable.sex_male);
        } else if (item.getSex() == 2) {
            iv_sex_ranking.setImageResource(R.drawable.sex_female);
        }

        tv_name_ranking.setText(item.getName());
        tv_show_ranking.setText(MyUtils.getInstans().doubleTwo(item.getMoney()) + "元");


        int rankShow = helper.getAdapterPosition();
        tv_gradeshow_ranking.setText(String.valueOf(rankShow + 1));
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/impact.ttf");
        tv_gradeshow_ranking.setTypeface(typeface);

        tv_gradeshow_ranking.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        iv_gradeshow_ranking.setVisibility(View.VISIBLE);
        tv_gradeshow_ranking.setVisibility(View.INVISIBLE);
        if (rankShow == 0) {
            iv_gradeshow_ranking.setImageResource(R.drawable.goldmedal);
        } else if (rankShow == 1) {
            iv_gradeshow_ranking.setImageResource(R.drawable.silvermedal);
        } else if (rankShow == 2) {
            iv_gradeshow_ranking.setImageResource(R.drawable.bronzemedal);
        } else {
            tv_gradeshow_ranking.setVisibility(View.VISIBLE);
            tv_gradeshow_ranking.setTextColor(ContextCompat.getColor(mContext, R.color.textColor7));
            iv_gradeshow_ranking.setVisibility(View.INVISIBLE);
        }
    }
}
