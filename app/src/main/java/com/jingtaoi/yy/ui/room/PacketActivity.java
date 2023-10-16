package com.jingtaoi.yy.ui.room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.PacketBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.adapter.PacketListAdapter;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 红包领取页面
 */
public class PacketActivity extends MyBaseActivity {
    @BindView(R.id.iv_header_packet)
    SimpleDraweeView ivHeaderPacket;
    @BindView(R.id.tv_name_packet)
    TextView tvNamePacket;
    @BindView(R.id.tv_show_packet)
    TextView tvShowPacket;
    @BindView(R.id.tv_number_packet)
    TextView tvNumberPacket;
    @BindView(R.id.tv_get_packet)
    TextView tvGetPacket;
    @BindView(R.id.mRecyclerView_packet)
    RecyclerView mRecyclerViewPacket;
    int reid;
    PacketListAdapter packetListAdapter;

    @Override
    public void initData() {
        reid = getBundleInt(Const.ShowIntent.DATA, 0);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_packet);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_packet);

        setRecycler();
        getCall();
    }

    private void setRecycler() {
        packetListAdapter = new PacketListAdapter(R.layout.item_bottomonlines);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewPacket.setLayoutManager(layoutManager);
        mRecyclerViewPacket.setAdapter(packetListAdapter);

    }

    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("reid", reid);
        HttpManager.getInstance().post(Api.GetRedList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                PacketBean packetBean = JSON.parseObject(responseString, PacketBean.class);
                if (packetBean.getCode() == Api.SUCCESS) {
                    setData(packetBean.getData());
                } else {
                    showToast(packetBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(PacketBean.DataBean data) {
        ImageUtils.loadUri(ivHeaderPacket, data.getImg());
        if (data.getNum() == 1) {
            tvNamePacket.setText(data.getName() + getString(R.string.tv_sendone_packet));
            tvGetPacket.setVisibility(View.INVISIBLE);
            tvNumberPacket.setVisibility(View.VISIBLE);
            tvNumberPacket.setText(data.getLqGold() + getString(R.string.tv_number_packet));
        } else {
            tvNamePacket.setText(data.getName() + getString(R.string.tv_send_packet));
            tvGetPacket.setText(getString(R.string.tv_getone_packet) + data.getLqnum() +
                    "/" + data.getNum() + getString(R.string.tv_number_packet)
                    + getString(R.string.tv_all_packet) + data.getLqGold() + "/" + data.getGold()
                    + getString(R.string.tv_youz_packet));
            packetListAdapter.setNewData(data.getRedNum());
        }
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
