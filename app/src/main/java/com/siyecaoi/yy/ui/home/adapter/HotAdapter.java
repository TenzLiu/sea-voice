package com.siyecaoi.yy.ui.home.adapter;

import android.widget.TextView;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.HomeItemData;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Locale;

import cn.sinata.xldutils.adapter.HFRecyclerAdapter;
import cn.sinata.xldutils.adapter.util.ViewHolder;

/**
 * 弃用
 *
 * @author xha
 * @data 2019/8/20
 */
public class HotAdapter extends HFRecyclerAdapter<HomeItemData> {
    private boolean isShowAd;

    public HotAdapter(List<HomeItemData> mData, boolean isShowAd) { //isShowAd false:不显示广告
        super(mData, R.layout.item_hot1);
        this.isShowAd = isShowAd;
    }

    @Override
    public void onBind(int position, HomeItemData hotData, ViewHolder holder) {
//        View adView = holder.bind(R.id.iv_ad);
//        if (isShowAd && ((mData.size()<5&&position==mData.size()-1)||(mData.size()>=5&&position==4)))
//            adView.setVisibility(View.VISIBLE);
//        else
//            adView.setVisibility(View.GONE);
//        adView.setOnClickListener(v -> ActivityCollector.getActivityCollector().toOtherActivity(RadioDatingActivity.class));
        TextView tag = holder.bind(R.id.tv_tag);
        switch (position % 5) {
            case 0: {
                tag.setBackgroundResource(R.drawable.bg_tag_blue);
                break;
            }
            case 1: {
                tag.setBackgroundResource(R.drawable.bg_tag_green);
                break;
            }
            case 2: {
                tag.setBackgroundResource(R.drawable.bg_tag_red);
                break;
            }
            case 3: {
                tag.setBackgroundResource(R.drawable.bg_tag_orange);
                break;
            }
            case 4: {
                tag.setBackgroundResource(R.drawable.bg_tag_pink);
                break;
            }
        }
        tag.setText(hotData.getRoomLabel());
        holder.setText(R.id.tv_title, hotData.getRoomName());
        holder.setText(R.id.tv_count, String.format(Locale.CHINA, "%d人在线", hotData.getNum()));
        ((SimpleDraweeView) holder.bind(R.id.iv_head)).setImageURI(hotData.getImg());
//        holder.bind(R.id.iv_hot).setVisibility(View.GONE);
    }
}
