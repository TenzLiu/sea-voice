package com.jingtaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.GiveGiftBean;
import com.jingtaoi.yy.utils.ImageUtils;

/**
 * Created by Administrator on 2019/1/2.
 */

public class GiveGiftAdapter extends BaseQuickAdapter<GiveGiftBean.DataEntity, BaseViewHolder> {

    private int giveType;

    public GiveGiftAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setGiveType(int giveType) {
        this.giveType = giveType;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, GiveGiftBean.DataEntity item) {
        TextView name = helper.getView(R.id.txt_give_name);
        TextView type = helper.getView(R.id.txt_give_type);
        TextView time = helper.getView(R.id.txt_give_time);
        SimpleDraweeView image = helper.getView(R.id.iv_give_gift);

        name.setText(item.getNickname() + " (" + item.getUsercoding() + ")");
        if (giveType == 1){
            type.setText("  收到您");
        } else if (giveType == 2){
            type.setText("  赠送您");
        }
        time.setText(item.getCreatetime());
        ImageUtils.loadUri(image, item.getImgFm());
    }
}
