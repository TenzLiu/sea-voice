package com.jingtaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;

/**
 * Created by Administrator on 2019/1/2.
 */

public class BottomShowRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public BottomShowRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv_show_bottomshow = helper.getView(R.id.tv_show_bottomshow);
        View line_view = helper.getView(R.id.line_view);
        tv_show_bottomshow.setText(item);
//        if (helper.getAdapterPosition() == 0) {
//            helper.setBackgroundRes(R.id.tv_show_bottomshow, R.drawable.bg_clicker_top20);
//        } else {
//            helper.setBackgroundRes(R.id.tv_show_bottomshow, R.drawable.bg_clicker);
//        }

        helper.setBackgroundRes(R.id.tv_show_bottomshow, R.drawable.bg_clicker);
        if (helper.getAdapterPosition()==getData().size()-1)
        {
            line_view.setVisibility(View.GONE);
        }else {
            line_view.setVisibility(View.VISIBLE);
        }
//        if (helper.getAdapterPosition() == getData().size() - 1) {
//            tv_show_bottomshow.setAlpha((float) 0.6);
//        }

    }
}
