package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.PkHistoryBean;
import com.siyecaoi.yy.utils.ImageUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class PkHistoryListAdapter extends BaseQuickAdapter<PkHistoryBean.DataBean, BaseViewHolder> {

    public PkHistoryListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, PkHistoryBean.DataBean item) {
        helper.setText(R.id.tv_time_pkhistory, item.getCreateTime());
        TextView tv_type_pkhistory = helper.getView(R.id.tv_type_pkhistory);
        TextView tv_pk_pkhistory = helper.getView(R.id.tv_pk_pkhistory);
        TextView tv_onetype_pkhistory = helper.getView(R.id.tv_onetype_pkhistory);
        TextView tv_twotype_pkhistory = helper.getView(R.id.tv_twotype_pkhistory);
        TextView tv_onename_pkhistory = helper.getView(R.id.tv_onename_pkhistory);
        TextView tv_twoname_pkhistory = helper.getView(R.id.tv_twoname_pkhistory);
        TextView tv_endtime_pkhistory = helper.getView(R.id.tv_endtime_pkhistory);

        SimpleDraweeView iv_oneimg_pkhistory = helper.getView(R.id.iv_oneimg_pkhistory);
        SimpleDraweeView iv_twoimg_pkhistory = helper.getView(R.id.iv_twoimg_pkhistory);
        TextView tv_onenumber_pkhistory = helper.getView(R.id.tv_onenumber_pkhistory);
        TextView tv_twonumber_pkhistory = helper.getView(R.id.tv_twonumber_pkhistory);
        ProgressBar progress_pkhistory = helper.getView(R.id.progress_pkhistory);

        ImageUtils.loadUri(iv_oneimg_pkhistory, item.getWz1().getImg());
        ImageUtils.loadUri(iv_twoimg_pkhistory, item.getWz2().getImg());
        tv_onename_pkhistory.setText(item.getWz1().getName());
        tv_twoname_pkhistory.setText(item.getWz2().getName());
        int state = item.getWz1().getState();
        if (state == 1) {
            tv_onetype_pkhistory.setText(mContext.getString(R.string.tv_win));
            tv_twotype_pkhistory.setText(mContext.getString(R.string.tv_lose));
        } else if (state == 2) {
            tv_onetype_pkhistory.setText(mContext.getString(R.string.tv_lose));
            tv_twotype_pkhistory.setText(mContext.getString(R.string.tv_win));
        } else if (state == 3) {
            tv_onetype_pkhistory.setText(mContext.getString(R.string.tv_flat));
            tv_twotype_pkhistory.setText(mContext.getString(R.string.tv_flat));
        }
        int oneNumber = item.getWz1().getNum();
        int twoNumber = item.getWz2().getNum();
        progress_pkhistory.setMax(oneNumber + twoNumber);
        progress_pkhistory.setProgress(oneNumber);
        tv_onenumber_pkhistory.setText(oneNumber + "");
        tv_twonumber_pkhistory.setText(twoNumber + "");
        tv_endtime_pkhistory.setText(item.getSecond() + "s");

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/impact.ttf");
        tv_pk_pkhistory.setTypeface(typeface);

        if (item.getState() == 1) {
            tv_type_pkhistory.setText(mContext.getString(R.string.tv_person_pkhistory));
        } else if (item.getState() == 2) {
            tv_type_pkhistory.setText(mContext.getString(R.string.tv_price_pkhistory));
        }

        helper.addOnClickListener(R.id.btn_pk_pkhistory);
    }
}
