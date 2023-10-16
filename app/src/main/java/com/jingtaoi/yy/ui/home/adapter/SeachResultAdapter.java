package com.jingtaoi.yy.ui.home.adapter;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.HomeItemData;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.sinata.xldutils.adapter.HFRecyclerAdapter;
import cn.sinata.xldutils.adapter.util.ViewHolder;
import cn.sinata.xldutils.utils.StringUtils;

public class SeachResultAdapter extends HFRecyclerAdapter<HomeItemData> {
    public SeachResultAdapter(List mData) {
        super(mData, R.layout.item_search_result);
    }


    @Override
    public void onBind(int position, HomeItemData homeItemData, ViewHolder holder) {
        ((SimpleDraweeView)holder.bind(R.id.iv_head)).setImageURI(homeItemData.getImg());
        holder.setText(R.id.tv_name,homeItemData.getUserName());
        if (StringUtils.isEmpty(homeItemData.getLiang())){
            holder.setText(R.id.tv_num,"ID:"+homeItemData.getUsercoding());
        }else {
            holder.setText(R.id.tv_num,"ID:"+homeItemData.getLiang());
        }

        holder.setText(R.id.tv_room_name,homeItemData.getRoomName());
    }
}
