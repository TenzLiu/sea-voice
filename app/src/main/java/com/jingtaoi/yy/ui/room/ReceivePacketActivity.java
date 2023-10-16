package com.jingtaoi.yy.ui.room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.ReceivePacketBean;
import com.jingtaoi.yy.dialog.MyPacketDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.adapter.ReceivePacketListAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.BroadcastManager;
import com.jingtaoi.yy.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 未领取红包页面
 */
public class ReceivePacketActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    ReceivePacketListAdapter receivePacketListAdapter;

    String roomId;

    MyPacketDialog packetDialog;

    @Override
    public void initData() {
        roomId = getBundleString(Const.ShowIntent.ROOMID);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.layout_recycler_top);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_packet);

        setRecycler();
        getCall();
    }

    private void setRecycler() {
        receivePacketListAdapter = new ReceivePacketListAdapter(R.layout.item_receivepacket);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(receivePacketListAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        receivePacketListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ReceivePacketBean.DataBean dataBean = (ReceivePacketBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                showPacketDialog(dataBean.getId(), dataBean.getRedId(), dataBean.getImg(), dataBean.getName(), position);
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCall();
            }
        });
    }

    /**
     * 收红包弹窗
     *
     * @param id       数据id
     * @param packetId 红包id
     */
    @SuppressLint("CheckResult")
    private void showPacketDialog(int id, int packetId, String img, String nickName, int position) {
        if (packetDialog != null && packetDialog.isShowing()) {
            packetDialog.dismiss();
        }
        packetDialog = new MyPacketDialog(ReceivePacketActivity.this, id, packetId, userToken, img, nickName, 1);
        if (!isFinishing()) {
            packetDialog.show();
        }
        packetDialog.setOpenOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPacket(id, packetId, position);
            }

        });

    }

    /**
     * 开红包
     *
     * @param id
     * @param packetId
     */
    private void openPacket(int id, int packetId, int position) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", id);
        HttpManager.getInstance().post(Api.GetRed, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (packetDialog != null && packetDialog.isShowing()) {
                        packetDialog.dismiss();
                    }
                    receivePacketListAdapter.remove(position);
                    int dataSize = receivePacketListAdapter.getData().size();
                    BroadcastManager.getInstance(ReceivePacketActivity.this).sendBroadcast(Const.BroadCast.PACKET_OVER, dataSize);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Const.ShowIntent.DATA, packetId);
                    ActivityCollector.getActivityCollector().toOtherActivity(PacketActivity.class, bundle);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.UserRedList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                ReceivePacketBean baseBean = JSON.parseObject(responseString, ReceivePacketBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    int dataSize = baseBean.getData().size();
                    BroadcastManager.getInstance(ReceivePacketActivity.this).sendBroadcast(Const.BroadCast.PACKET_OVER, dataSize);
                    receivePacketListAdapter.setNewData(baseBean.getData());
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
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
