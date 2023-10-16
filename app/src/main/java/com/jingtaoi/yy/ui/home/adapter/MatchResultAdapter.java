package com.jingtaoi.yy.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.HomeItemData;
import com.jingtaoi.yy.utils.AudioRecoderUtils;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchResultAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HomeItemData> data;
    private AudioRecoderUtils utils;

    public MatchResultAdapter(Context context, ArrayList<HomeItemData> data) {
        this.context = context;
        this.data = data;
        utils = new AudioRecoderUtils();
        utils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {

            }

            @Override
            public void onStop(String filePath) {

            }

            @Override
            public void onStartPlay() {

            }

            @Override
            public void onFinishPlay() {

            }
        });
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View convert = convertView;
        if (convert == null) {
            convert = LayoutInflater.from(context).inflate(R.layout.item_match_result, parent, false);
            convert.setTag(new ViewHolder(convert));
        }
        ViewHolder holder = (ViewHolder) convert.getTag();
        HomeItemData item = (HomeItemData)getItem(position);
        //todo 渲染
        holder.ivHead.setImageURI(item.getImg());
        holder.tvName.setText(item.getName());
        holder.tvName.setSelected(item.getSex() == 1);
        Double distance = item.getDistance();
        if (distance>=1000){
            holder.tvDistance.setText(String.format("%.2fkm",distance/1000));
        }else {
            holder.tvDistance.setText(String.format("%.2fm",distance));
        }
        holder.tvFansCount.setText(item.getNum()+"");
//        holder.tvLevel.setCompoundDrawablesWithIntrinsicBounds(ImageShowUtils.getGrade(item.getTreasureGrade()),0,0,0);
//        holder.tvStar.setCompoundDrawablesWithIntrinsicBounds(ImageShowUtils.getCharm(item.getCharmGrade()),0,0,0);

        TextView iv_grade_allmsg =    holder.tvLevel;//财富等级显示
        iv_grade_allmsg.setBackgroundResource(ImageShowUtils.getGrade(item.getTreasureGrade()));
        iv_grade_allmsg.setText(ImageShowUtils.getGradeText(item.getTreasureGrade()));

        TextView iv_charm_allmsg =   holder.tvStar;//魅力等级显示
        iv_charm_allmsg.setBackgroundResource(ImageShowUtils.getGrade(item.getCharmGrade()));
        iv_charm_allmsg.setText(ImageShowUtils.getCharmText(item.getCharmGrade()));

        holder.tv_sign.setText("个性签名："+item.getIndividuation());
        if (item.getVoice()!=null && !item.getVoice().isEmpty()){
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tvPlayVoice.setVisibility(View.VISIBLE);
            holder.tvPlayVoice.setText(item.getVoiceTime()+"''");
            holder.tvPlayVoice.setOnClickListener(v -> {
                utils.startplayMusic(context,item.getVoice());
            });
        }else {
            holder.tv_title.setVisibility(View.GONE);
            holder.tvPlayVoice.setVisibility(View.GONE);
        }
        return convert;
    }

    public void stopVoice(){
        utils.stopPlayMusic();
    }

    class ViewHolder {
        @BindView(R.id.iv_head)
        SimpleDraweeView ivHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_distance)
        TextView tvDistance;
        @BindView(R.id.tv_fans_count)
        TextView tvFansCount;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.tv_star)
        TextView tvStar;
        @BindView(R.id.tv_play_voice)
        TextView tvPlayVoice;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_sign)
        TextView tv_sign;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
