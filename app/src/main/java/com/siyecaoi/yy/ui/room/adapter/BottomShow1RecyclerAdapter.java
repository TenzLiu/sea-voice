package com.siyecaoi.yy.ui.room.adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiftShowBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.MyUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2019/1/2.
 */

public class BottomShow1RecyclerAdapter extends BaseQuickAdapter<GiftShowBean.DataEntity, BaseViewHolder> {

    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BottomShow1RecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, GiftShowBean.DataEntity item) {
        TextView tv_name_show1 = helper.getView(R.id.tv_name_show1);
        TextView tv_gold_show1 = helper.getView(R.id.tv_gold_show1);
        SimpleDraweeView iv_header_show1 = helper.getView(R.id.iv_header_show1);
        if (type == 1) {
            tv_name_show1.setText(item.getName());
            tv_gold_show1.setText(item.getGold() + "");
            ImageUtils.loadUri(iv_header_show1, item.getImg());
        } else if (type == 2) {
            tv_name_show1.setText(item.getName() + "×" + item.getNum());
            tv_gold_show1.setText(MyUtils.getInstans().forMartDays(item.getCreateTime()));
            ImageUtils.loadUri(iv_header_show1, item.getImg());
        }

    }
}
