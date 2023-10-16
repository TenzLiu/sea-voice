package com.jingtaoi.yy.ui.room.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.RoomAuctionBean;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2019/1/2.
 */

public class AuctionListAdapter extends BaseQuickAdapter<RoomAuctionBean.DataBean, BaseViewHolder> {


    public AuctionListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomAuctionBean.DataBean item) {
        SimpleDraweeView iv_show_auction = helper.getView(R.id.iv_show_auction);
        ImageUtils.loadUri(iv_show_auction, item.getImg());
        helper.setText(R.id.tv_name_auction, item.getName());
        ImageView iv_sex_auction = helper.getView(R.id.iv_sex_auction);
        if (item.getSex() == 1) { //男
            iv_sex_auction.setImageResource(R.drawable.sex_male);
        } else if (item.getSex() == 2) {
            iv_sex_auction.setImageResource(R.drawable.sex_female);
        }
        TextView tv_endcenter_auction = helper.getView(R.id.tv_endcenter_auction);
        tv_endcenter_auction.setText(item.getNum() + mContext.getString(R.string.tv_down_ranking));

        TextView tv_gradeshow_auction = helper.getView(R.id.tv_gradeshow_auction);
        ImageView iv_gradeshow_auction = helper.getView(R.id.iv_gradeshow_auction);
        int rankShow = helper.getAdapterPosition();

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/impact.ttf");
        tv_gradeshow_auction.setTypeface(typeface);

        tv_gradeshow_auction.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        iv_gradeshow_auction.setVisibility(View.VISIBLE);
        tv_gradeshow_auction.setVisibility(View.INVISIBLE);
        if (rankShow == 0) {
            iv_gradeshow_auction.setImageResource(R.drawable.goldmedal);
        } else if (rankShow == 1) {
            iv_gradeshow_auction.setImageResource(R.drawable.silvermedal);
        } else if (rankShow == 2) {
            iv_gradeshow_auction.setImageResource(R.drawable.bronzemedal);
        } else {
            tv_gradeshow_auction.setVisibility(View.VISIBLE);
            tv_gradeshow_auction.setText(String.valueOf(rankShow + 1));
            tv_gradeshow_auction.setTextColor(ContextCompat.getColor(mContext, R.color.textColor7));
            iv_gradeshow_auction.setVisibility(View.INVISIBLE);
        }

        ImageView iv_grade_auction = helper.getView(R.id.iv_grade_auction);//等级显示
        iv_grade_auction.setImageResource(ImageShowUtils.getGrade(item.getGrade()));

        ImageView iv_sex_send_auction = helper.getView(R.id.iv_sex_send_auction);//性别显示
        SimpleDraweeView iv_send_auction = helper.getView(R.id.iv_send_auction);
        TextView tv_send_auction = helper.getView(R.id.tv_send_auction);
//        TextView tv_sendshow_auction = helper.getView(R.id.tv_sendshow_auction);

        ImageUtils.loadUri(iv_send_auction, item.getCharmModels().getImg());
        if (item.getCharmModels().getSex() == 1) { //男
            iv_sex_send_auction.setImageResource(R.drawable.sex_male);
        } else if (item.getCharmModels().getSex() == 2) {
            iv_sex_send_auction.setImageResource(R.drawable.sex_female);
        }
        tv_send_auction.setText(item.getCharmModels().getName());

//        tv_sendshow_auction.setText(item.getCharmModels().getNum() + mContext.getString(R.string.tv_down_ranking));

    }

}
