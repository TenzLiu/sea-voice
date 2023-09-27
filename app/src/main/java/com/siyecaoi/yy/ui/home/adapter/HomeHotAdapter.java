package com.siyecaoi.yy.ui.home.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.HomeItemData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.utils.ImageUtils;

import java.util.Locale;

public class HomeHotAdapter extends BaseQuickAdapter<HomeItemData, BaseViewHolder> {

    public HomeHotAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItemData item) {
        TextView tag = helper.getView(R.id.tv_tag);
        switch (helper.getAdapterPosition() % 5) {
            case 0: {
                tag.setBackgroundResource(R.drawable.bg_tag_blue1);
                break;
            }
            case 1: {
                tag.setBackgroundResource(R.drawable.bg_tag_green1);
                break;
            }
            case 2: {
                tag.setBackgroundResource(R.drawable.bg_tag_red1);
                break;
            }
            case 3: {
                tag.setBackgroundResource(R.drawable.bg_tag_orange1);
                break;
            }
            case 4: {
                tag.setBackgroundResource(R.drawable.bg_tag_green1);
                break;
            }
        }
        tag.setText(item.getRoomLabel());

        helper.setText(R.id.tv_title,item.getRoomName());
        helper.setText(R.id.tv_count,item.getNum()+"");
        ImageView ivHead = helper.getView(R.id.iv_head);
        ImageUtils.loadImage(ivHead, item.getImg(), 6, -1);
    }
}
