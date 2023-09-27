package com.siyecaoi.yy.ui.mine.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.StoreListBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class StoreListAdapter extends BaseQuickAdapter<StoreListBean.DataEntity, BaseViewHolder> {

    public int checkId = -1;

    public StoreListAdapter(int layoutResId) {
        super(layoutResId);
    }

    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCheck(int id) {
        this.checkId = id;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, StoreListBean.DataEntity item) {
        SimpleDraweeView iv_show_store = helper.getView(R.id.iv_show_store);
        TextView tv_try_store = helper.getView(R.id.tv_try_store);
        TextView tv_name_store = helper.getView(R.id.tv_name_store);
        TextView tv_gold_store = helper.getView(R.id.tv_gold_store);
        TextView tv_time_store = helper.getView(R.id.tv_time_store);
        View bg_checked = helper.getView(R.id.rl_show_store);

        ImageUtils.loadUri(iv_show_store, item.getImgFm());
        if (item.getDay() == -1) {
            tv_time_store.setText("长期");
        } else {
            tv_time_store.setText(item.getDay() + "天");
        }

        if (type == 1) {
            tv_try_store.setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.tv_try_store);
        } else {
            tv_try_store.setVisibility(View.GONE);
        }

        tv_name_store.setText(item.getName());
        tv_gold_store.setText(item.getGold() + "");

        if (checkId == item.getId()){
            bg_checked.setBackgroundResource(R.drawable.bg_shop_check);
        }else {
            bg_checked.setBackgroundResource(R.drawable.bg_shop_uncheck);
        }

        helper.addOnClickListener(R.id.rl_show_store);
    }
}
