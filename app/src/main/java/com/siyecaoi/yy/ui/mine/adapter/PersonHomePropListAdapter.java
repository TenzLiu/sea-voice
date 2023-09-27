package com.siyecaoi.yy.ui.mine.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.PersonalHomeBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class PersonHomePropListAdapter extends BaseQuickAdapter<PersonalHomeBean.DataEntity.SceneEntity, BaseViewHolder> {


    public PersonHomePropListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalHomeBean.DataEntity.SceneEntity item) {
        SimpleDraweeView iv_show_onlines = helper.getView(R.id.iv_show_prop);
        TextView tv_use_prop = helper.getView(R.id.tv_use_prop);
        TextView tv_name_prop = helper.getView(R.id.tv_name_prop);

        ImageUtils.loadUri(iv_show_onlines, item.getImgFm());
        if (item.getState() == 1) {
            tv_use_prop.setVisibility(View.INVISIBLE);
        } else if (item.getState() == 2) {
            tv_use_prop.setVisibility(View.VISIBLE);
        }
        tv_name_prop.setText(item.getName());
    }
}
