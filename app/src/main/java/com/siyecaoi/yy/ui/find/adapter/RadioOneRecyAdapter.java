package com.siyecaoi.yy.ui.find.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.model.ChatRoomMsgModel;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class RadioOneRecyAdapter extends BaseQuickAdapter<ChatRoomMsgModel.DataBean, BaseViewHolder> {

    public RadioOneRecyAdapter(int layoutResId) {
        super(layoutResId);
    }

    private int userId;//当前用户id

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatRoomMsgModel.DataBean item) {
        ImageView iv_header_radioda = helper.getView(R.id.iv_header_radioda);
        ImageView iv_user_header_radioda = helper.getView(R.id.iv_user_header_radioda);
        RelativeLayout rl_item = helper.getView(R.id.rl_item);
        RelativeLayout rl_item_user = helper.getView(R.id.rl_item_user);
        int uuid = (int) SharedPreferenceUtils.get(helper.itemView.getContext(), Const.User.USER_TOKEN, 0);


        if ((item != null && uuid != item.getUid())) {
            ImageUtils.loadImage(iv_header_radioda, item.getHeader(), -1, R.drawable.default_head);
            ImageView iv_sex_show = helper.getView(R.id.iv_sex_show);
            if (item.getSex() == 1) {
                iv_sex_show.setSelected(true);
            } else if (item.getSex() == 2) {
                iv_sex_show.setSelected(false);
            }


            ImageView iv_grade_allmsg = helper.getView(R.id.iv_grade_radioda);//财富等级显示
            iv_grade_allmsg.setImageResource(ImageShowUtils.getGrade(item.getGrade()));
//            iv_grade_allmsg.setText(ImageShowUtils.getGradeText(item.getGrade()));


            ImageView iv_charm_allmsg = helper.getView(R.id.iv_charm_allmsg);//魅力等级显示
            iv_charm_allmsg.setImageResource(ImageShowUtils.getCharm(item.getCharm()));
//            iv_charm_allmsg.setText(ImageShowUtils.getCharmText(item.getCharm()));


            TextView tv_nickname_radioda = helper.getView(R.id.tv_nickname_radioda);
            TextView tv_show_radioda = helper.getView(R.id.tv_show_radioda);
            tv_nickname_radioda.setText(item.getName());
            tv_show_radioda.setText(item.getMessageShow());

            helper.addOnClickListener(R.id.iv_header_radioda);

            rl_item.setVisibility(View.VISIBLE);
            rl_item_user.setVisibility(View.GONE);
        } else {
            ImageUtils.loadImage(iv_user_header_radioda, item.getHeader(), -1, R.drawable.default_head);
            ImageView iv_sex_show = helper.getView(R.id.iv_user_sex_show);
            if (item.getSex() == 1) {
                iv_sex_show.setSelected(true);
            } else if (item.getSex() == 2) {
                iv_sex_show.setSelected(false);
            }

            ImageView iv_grade_allmsg = helper.getView(R.id.user_grade_radioda);//财富等级显示
            iv_grade_allmsg.setImageResource(ImageShowUtils.getGrade(item.getCharm()));

//            iv_grade_allmsg.setBackgroundResource(ImageShowUtils.getGrade(item.getGrade()));
//            iv_grade_allmsg.setText(ImageShowUtils.getGradeText(item.getGrade()));

            ImageView iv_charm_allmsg = helper.getView(R.id.iv_user_charm);//魅力等级显示
            iv_charm_allmsg.setImageResource(ImageShowUtils.getCharm(item.getCharm()));
//            iv_charm_allmsg.setBackgroundResource(ImageShowUtils.getGrade(item.getCharm()));
//            iv_charm_allmsg.setText(ImageShowUtils.getCharmText(item.getCharm()));

            TextView tv_nickname_radioda = helper.getView(R.id.tv_user_nickname_radioda);
            TextView tv_show_radioda = helper.getView(R.id.tv_user_show_radioda);
            tv_nickname_radioda.setText(item.getName());
            tv_show_radioda.setText(item.getMessageShow());

            helper.addOnClickListener(R.id.iv_header_radioda);
            helper.addOnClickListener(R.id.iv_user_header_radioda);

            rl_item.setVisibility(View.GONE);
            rl_item_user.setVisibility(View.VISIBLE);
        }

    }
}
