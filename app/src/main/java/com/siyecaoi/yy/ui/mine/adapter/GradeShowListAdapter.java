package com.siyecaoi.yy.ui.mine.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GradeShowBean;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.siyecaoi.yy.utils.MyUtils;

public class GradeShowListAdapter extends BaseQuickAdapter<GradeShowBean.DataEntity, BaseViewHolder> {


    public GradeShowListAdapter(int layoutResId) {
        super(layoutResId);
    }

    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, GradeShowBean.DataEntity item) {
        TextView iv_start_gradeshow = helper.getView(R.id.iv_start_gradeshow);
        View view_center = helper.getView(R.id.view_center);
        TextView iv_end_gradeshow = helper.getView(R.id.iv_end_gradeshow);
        TextView tv_lei_gradeshow = helper.getView(R.id.tv_lei_gradeshow);
        TextView tv_show_gradeshow = helper.getView(R.id.tv_show_gradeshow);

        if (type == 1) {
            iv_start_gradeshow.setBackgroundResource(ImageShowUtils.getGrade(item.getX()));
//            iv_start_gradeshow.setText(ImageShowUtils.getGradeText(item.getX()));

            if (item.getX() == item.getY()) {
                view_center.setVisibility(View.INVISIBLE);
            } else {
                view_center.setVisibility(View.VISIBLE);
                iv_end_gradeshow.setBackgroundResource(ImageShowUtils.getGrade(item.getY()));
//                iv_end_gradeshow.setText(ImageShowUtils.getGradeText(item.getY()));
            }
            if (item.getB() >= 10000) {
                String showB = MyUtils.getInstans().doubleDelete(item.getB() / 10000);
                tv_show_gradeshow.setText("每级需" + showB + "万财富");
            } else {
                tv_show_gradeshow.setText("每级需" + item.getB() + "财富");
            }

        } else if (type == 2) {

            iv_start_gradeshow.setBackgroundResource(ImageShowUtils.getCharm(item.getX()));
//            iv_start_gradeshow.setText(ImageShowUtils.getCharmText(item.getX()));
            if (item.getX() == item.getY()) {
                view_center.setVisibility(View.INVISIBLE);
            } else {
                view_center.setVisibility(View.VISIBLE);
                iv_end_gradeshow.setBackgroundResource(ImageShowUtils.getGrade(item.getY()));
//                iv_end_gradeshow.setText(ImageShowUtils.getCharmText(item.getY()));
//                iv_end_gradeshow.setImageResource(ImageShowUtils.getCharm(item.getY()));
            }
//            iv_end_gradeshow.setImageResource(ImageShowUtils.getCharm(item.getY()));
//            tv_show_gradeshow.setText("每级需" + item.getB() + "魅力");
            if (item.getB() >= 10000) {
                String showB = MyUtils.getInstans().doubleDelete(item.getB() / 10000);
                tv_show_gradeshow.setText("每级需" + showB + "万魅力");
            } else {
                tv_show_gradeshow.setText("每级需" + item.getB() + "魅力");
            }
        }

        String startZ, endP;
        if (item.getZ() >= 10000) {
            startZ = String.valueOf(item.getZ() / 10000) + "万";
        } else {
            startZ = String.valueOf(item.getZ());
        }
        if (item.getP() >= 10000) {
            endP = String.valueOf(item.getP() / 10000) + "万";
        } else {
            endP = String.valueOf(item.getP());
        }
        tv_lei_gradeshow.setText(startZ + "-" + endP);
    }
}
