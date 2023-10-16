package com.jingtaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.MyattentionBean;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class InviteFriendListAdapter extends BaseQuickAdapter<MyattentionBean.DataEntity, BaseViewHolder> {


    public InviteFriendListAdapter(int layoutResId) {
        super(layoutResId);
    }

    boolean isChoice;//是否多选

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, MyattentionBean.DataEntity item) {
        SimpleDraweeView iv_show_invitefriend = helper.getView(R.id.iv_show_invitefriend);
        TextView tv_name_invitefriend = helper.getView(R.id.tv_name_invitefriend);
        TextView tv_invite_invitefriend = helper.getView(R.id.tv_invite_invitefriend);
        ImageView iv_invite_invitefriend = helper.getView(R.id.iv_invite_invitefriend);
        ImageUtils.loadUri(iv_show_invitefriend, item.getImg());
        tv_name_invitefriend.setText(item.getName());
        helper.addOnClickListener(R.id.tv_invite_invitefriend);
        helper.addOnClickListener(R.id.iv_invite_invitefriend);

        if (isChoice) {
            if (item.isSend()) {
                tv_invite_invitefriend.setVisibility(View.VISIBLE);
                iv_invite_invitefriend.setVisibility(View.GONE);
            } else {
                tv_invite_invitefriend.setVisibility(View.GONE);
                iv_invite_invitefriend.setVisibility(View.VISIBLE);
            }
        } else {
            tv_invite_invitefriend.setVisibility(View.VISIBLE);
            iv_invite_invitefriend.setVisibility(View.GONE);
        }

        if (item.isSend()) {
            tv_invite_invitefriend.setText("已邀请");
            tv_invite_invitefriend.setBackgroundResource(R.color.white);
            tv_invite_invitefriend.setTextColor(ContextCompat.getColor(mContext, R.color.black6));
        } else {
            tv_invite_invitefriend.setText("邀请");
            tv_invite_invitefriend.setBackgroundResource(R.drawable.bg_round5_ff0);
            tv_invite_invitefriend.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        }

        if (item.isSelect()) {
            iv_invite_invitefriend.setSelected(true);
        } else {
            iv_invite_invitefriend.setSelected(false);
        }
    }
}
