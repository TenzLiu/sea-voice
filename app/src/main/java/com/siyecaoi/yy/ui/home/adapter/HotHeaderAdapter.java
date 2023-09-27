package com.siyecaoi.yy.ui.home.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.HomeItemData;
import com.siyecaoi.yy.utils.ImageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Locale;

public class HotHeaderAdapter extends BaseMultiItemQuickAdapter<HomeItemData, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public HotHeaderAdapter(List<HomeItemData> data) {
        super(data);
        addItemType(HomeItemData.TYPE_ITEM1, R.layout.item_xh_item1);//小布局
        addItemType(HomeItemData.TYPE_ITEM2, R.layout.item_xh_item2);//大布局
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItemData item) {
        TextView tag = helper.getView(R.id.tv_tag);
        tag.setText(item.getRoomLabel());
        helper.setText(R.id.tv_title, item.getRoomName());
        helper.setText(R.id.tv_count, item.getNum() + "");
        ImageView ivHead = helper.getView(R.id.iv_head);
        switch (helper.getItemViewType()) {
            case HomeItemData.TYPE_ITEM1:
                ImageUtils.loadImage(ivHead, item.getImg(), 0, -1);
                break;
            case HomeItemData.TYPE_ITEM2:
                ImageUtils.loadImage(ivHead, item.getImg(), 16, -1);
                break;
        }


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
                tag.setBackgroundResource(R.drawable.bg_tag_green);
                break;
            }
        }

    }
}
