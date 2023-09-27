package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.DanRankBean;
import com.siyecaoi.yy.bean.GiftShowBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.MyUtils;

import cn.sinata.xldutils.utils.TimeUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class DanRankAdapter extends BaseQuickAdapter<DanRankBean.DataEntity, BaseViewHolder> {


    public DanRankAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, DanRankBean.DataEntity item) {
        SimpleDraweeView iv_header_danrank = helper.getView(R.id.iv_header_danrank);
        TextView tv_name_danrank = helper.getView(R.id.tv_name_danrank);
        TextView tv_grade_danrank = helper.getView(R.id.tv_grade_danrank);
        TextView tv_time_danrank = helper.getView(R.id.tv_time_danrank);
        TextView tv_wan_danrank = helper.getView(R.id.tv_wan_danrank);
        TextView tv_liwu_danrank = helper.getView(R.id.tv_liwu_danrank);
        SimpleDraweeView iv_show_danrank = helper.getView(R.id.iv_show_danrank);

        ImageUtils.loadUri(iv_header_danrank, item.getImgTx());
        tv_name_danrank.setText(item.getNickname());
        tv_grade_danrank.setText(item.getTreasureGrade() + "");
        tv_time_danrank.setText(TimeUtils.getLastUpdateTimeDesc(item.getCreatetime()));
        tv_wan_danrank.setText("探险*" + item.getNum());
        tv_liwu_danrank.setText(item.getGiftName() + "X" + item.getGiftNum());
        ImageUtils.loadUri(iv_show_danrank, item.getGiftImg());
    }
}
