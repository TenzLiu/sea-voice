package com.jingtaoi.yy.ui.find.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.model.ChatRoomMsgModel;

public class RadioDRecyAdapter extends BaseQuickAdapter<ChatRoomMsgModel.DataBean, BaseViewHolder> {

    public RadioDRecyAdapter(int layoutResId) {
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
//        SimpleDraweeView iv_header_radioda = helper.getView(R.id.iv_header_radioda);
//        ImageUtils.loadUri(iv_header_radioda, item.getHeader());
//        ImageView iv_sex_show = helper.getView(R.id.iv_sex_show);
//        ImageView iv_grade_radioda = helper.getView(R.id.iv_grade_radioda);
//        ImageView iv_charm_radioda = helper.getView(R.id.iv_charm_radioda);
//        if (item.getSex() == 1) {
//            iv_sex_show.setSelected(true);
//        } else if (item.getSex() == 2) {
//            iv_sex_show.setSelected(false);
//        }
//        iv_grade_radioda.setImageResource(ImageShowUtils.getGrade(item.getGrade()));
//        iv_charm_radioda.setImageResource(ImageShowUtils.getCharm(item.getCharm()));
//        TextView tv_nickname_radioda = helper.getView(R.id.tv_nickname_radioda);
//        TextView tv_show_radioda = helper.getView(R.id.tv_show_radioda);
//        tv_nickname_radioda.setText(item.getName());
//        tv_show_radioda.setText(item.getMessageShow());
//        ConstraintLayout con_radioda = helper.getView(R.id.con_radioda);
////        if (item.getUid()==userId){
////            con_radioda.setBackgroundColor(ContextCompat.getColor(mContext,R.color.bg_line));
////        }else {
////            con_radioda.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
////        }
//        helper.addOnClickListener(R.id.iv_header_radioda);
    }
}
