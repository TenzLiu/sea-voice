package com.jingtaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.AllmsgBean;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2019/1/2.
 */

public class AllMsgAdapter extends BaseQuickAdapter<AllmsgBean.DataEntity, BaseViewHolder> {


    public AllMsgAdapter() {
        super(R.layout.item_allmsg);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllmsgBean.DataEntity item) {
        SimpleDraweeView iv_header_allmsg = helper.getView(R.id.iv_header_allmsg);
        SimpleDraweeView iv_user_header_allmsg = helper.getView(R.id.iv_user_header_allmsg);
        RelativeLayout msg_ll = helper.getView(R.id.msg_ll);
        RelativeLayout user_msg_ll = helper.getView(R.id.user_msg_ll);
        int uuid = (int) SharedPreferenceUtils.get(helper.itemView.getContext(), Const.User.USER_TOKEN, 0);




        if ((uuid!=item.getUid())){

            ImageUtils.loadUri(iv_header_allmsg, item.getImgTx());
            helper.setText(R.id.tv_name_allmsg, item.getUserName());
            helper.setText(R.id.tv_show_allmsg, item.getContent());




            TextView iv_grade_allmsg = helper.getView(R.id.iv_grade_allmsg);//财富等级显示
            iv_grade_allmsg.setBackgroundResource(ImageShowUtils.getGrade(item.getTreasureGrade()));
            iv_grade_allmsg.setText(ImageShowUtils.getGradeText(item.getTreasureGrade()));

            TextView iv_charm_allmsg = helper.getView(R.id.iv_charm_allmsg);//魅力等级显示
            iv_charm_allmsg.setBackgroundResource(ImageShowUtils.getGrade(item.getCharmGrade()));
            iv_charm_allmsg.setText(ImageShowUtils.getCharmText(item.getCharmGrade()));

            msg_ll.setVisibility(View.VISIBLE);
            user_msg_ll.setVisibility(View.GONE);
        }else {
            ImageUtils.loadUri(iv_user_header_allmsg, item.getImgTx());
            helper.setText(R.id.tv_user_name_allmsg, item.getUserName());
            helper.setText(R.id.tv_user_show_allmsg, item.getContent());

            helper.addOnClickListener(R.id.iv_user_header_allmsg);
            helper.addOnClickListener(R.id.tv_user_show_allmsg);


            TextView iv_grade_allmsg = helper.getView(R.id.iv_user_grade_allmsg);//等级显示
            iv_grade_allmsg.setBackgroundResource(ImageShowUtils.getGrade(item.getTreasureGrade()));
            iv_grade_allmsg.setText(ImageShowUtils.getGradeText(item.getTreasureGrade()));

            TextView iv_user_charm_allmsg = helper.getView(R.id.iv_user_charm_allmsg);//魅力等级显示
            iv_user_charm_allmsg.setBackgroundResource(ImageShowUtils.getGrade(item.getCharmGrade()));
            iv_user_charm_allmsg.setText(ImageShowUtils.getCharmText(item.getCharmGrade()));

            msg_ll.setVisibility(View.GONE);
            user_msg_ll.setVisibility(View.VISIBLE);
        }
        helper.addOnClickListener(R.id.iv_header_allmsg);
        helper.addOnClickListener(R.id.rl_show_allmsg);
        helper.addOnClickListener(R.id.iv_user_header_allmsg);

//        ImageView iv_charm_allmsg = helper.getView(R.id.iv_charm_allmsg);//等级显示
//        iv_charm_allmsg.setImageResource(ImageShowUtils.getCharm(item.getCharmGrade()));


    }

}
