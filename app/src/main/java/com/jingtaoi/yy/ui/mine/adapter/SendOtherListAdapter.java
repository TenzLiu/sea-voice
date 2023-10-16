package com.jingtaoi.yy.ui.mine.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.MyattentionBean;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class SendOtherListAdapter extends BaseQuickAdapter<MyattentionBean.DataEntity, BaseViewHolder> {

    public SendOtherListAdapter(int layoutResId) {
        super(layoutResId);
    }

    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyattentionBean.DataEntity item) {
        SimpleDraweeView iv_show_sendother = helper.getView(R.id.iv_show_sendother);
        ImageUtils.loadUri(iv_show_sendother, item.getImg());
        helper.setText(R.id.tv_name_sendother, item.getName());
        TextView tv_send_sendother = helper.getView(R.id.tv_send_sendother);
        if (item.isSendSuccescc()) {
            tv_send_sendother.setText(R.string.tv_sendotherto);
        } else {
            tv_send_sendother.setText(R.string.tv_sendother);
        }
        helper.addOnClickListener(R.id.tv_send_sendother);
    }
}
