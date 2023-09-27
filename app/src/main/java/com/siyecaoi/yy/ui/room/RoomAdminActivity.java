package com.siyecaoi.yy.ui.room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.RoomAdminBean;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.RoomAdminListAdapter;
import com.siyecaoi.yy.utils.Const;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 房间管理员页面
 */
public class RoomAdminActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    RoomAdminListAdapter roomAdminListAdapter;
    String roomId;
    MyDialog myDialog;

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

        setTitleText(R.string.tv_admin_roomset);
        setLeftImg(getResources().getDrawable(R.drawable.icon_back1));
        setRecycler();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getCall();
            }
        });
    }


    private void setRecycler() {
        roomAdminListAdapter = new RoomAdminListAdapter(R.layout.item_roomadmin);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(roomAdminListAdapter);

        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        tvNodata.setText(getString(R.string.tv_nodata_roomadmin));
        roomAdminListAdapter.setEmptyView(view);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCall();
            }
        });

        roomAdminListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_endcenter_roomadmin) {
                    RoomAdminBean.DataBean dataBean = (RoomAdminBean.DataBean) adapter.getItem(position);
                    assert dataBean != null;
                    showMyDialog(dataBean, position);

                }
            }
        });

    }

    private void showMyDialog(final RoomAdminBean.DataBean dataBean, final int position) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("是否将" + dataBean.getName() + "移除管理员列表？");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                setAdminState(dataBean.getId(), roomId, Const.IntShow.TWO, position);
            }
        });
    }

    public void setAdminState(int userId, String pid, int type, final int position) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("pid", pid);
        map.put("type", type);
        HttpManager.getInstance().post(Api.getChatroomsGLY, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("移除管理员成功");
                    roomAdminListAdapter.remove(position);
                    roomAdminListAdapter.notifyItemRemoved(position);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void getCall() {
        if (page == 1) {
            roomAdminListAdapter.setEnableLoadMore(false);
        }
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.getRoomManagement, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (page == 1) {
                    roomAdminListAdapter.setEnableLoadMore(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                RoomAdminBean roomAdminBean = JSON.parseObject(responseString, RoomAdminBean.class);
                if (roomAdminBean.getCode() == Api.SUCCESS) {
                    setData(roomAdminBean.getData());
                } else {
                    showToast(roomAdminBean.getMsg());
                }
            }
        });
    }

    private void setData(List<RoomAdminBean.DataBean> data) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            roomAdminListAdapter.setNewData(data);
        } else {
            if (size > 0) {
                roomAdminListAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            if (page == 1) {
                //第一页如果不够一页就不显示没有更多数据布局
                roomAdminListAdapter.loadMoreEnd(true);
            } else {
                roomAdminListAdapter.loadMoreEnd(false);
            }
        } else {
            roomAdminListAdapter.loadMoreComplete();
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
