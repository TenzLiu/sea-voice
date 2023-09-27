package com.siyecaoi.yy.ui.room.adapter;

import android.graphics.Typeface;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;

import java.util.List;

public class RoomsetListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    String chooseMark;

    public String getChooseMark() {
        return chooseMark;
    }

    public void setChooseMark(String chooseMark) {
        this.chooseMark = chooseMark;
        notifyDataSetChanged();
    }

    public RoomsetListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
//        helper.setText(R.id.tv_show_mark, item);
        TextView tv_show_mark = helper.getView(R.id.tv_show_mark);
        tv_show_mark.setText(item);
        if (chooseMark != null && chooseMark.equals(item)) {
            tv_show_mark.setTextColor(ContextCompat.getColor(mContext, R.color.text_ff0));
            tv_show_mark.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        } else {
            tv_show_mark.setTextColor(ContextCompat.getColor(mContext, R.color.black6));
            tv_show_mark.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }
}
