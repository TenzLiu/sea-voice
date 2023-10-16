package com.jingtaoi.yy.ui.room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.RoomBlackBean;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.adapter.BlackUserListAdapter;
import com.jingtaoi.yy.utils.Const;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 房间黑名单页面
 */
public class BlackRoomActivity extends MyBaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    BlackUserListAdapter blackUserListAdapter;
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

        setTitleText(R.string.tv_black_roomset);
        setRecycler();
        setLeftImg(getResources().getDrawable(R.drawable.icon_back1));

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getCall();
            }
        });
    }

    private void setRecycler() {
        blackUserListAdapter = new BlackUserListAdapter(R.layout.item_black);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(blackUserListAdapter);

        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        tvNodata.setText(getString(R.string.tv_nodata_balckuser));
        ivNodata.setImageResource(R.drawable.blacklist_empty);
        blackUserListAdapter.setEmptyView(view);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                blackUserListAdapter.setUpFetchEnable(false);
                page = 1;
                getCall();
            }
        });

        blackUserListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);

        blackUserListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RoomBlackBean.DataBean dataBean = (RoomBlackBean.DataBean) adapter.getItem(position);
                showMyDialog(dataBean, position);
            }
        });
    }

    private void showMyDialog(final RoomBlackBean.DataBean dataBean, final int position) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("是否将" + dataBean.getName() + "移出黑名单列表？");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                setBlcak(dataBean.getId(), roomId, dataBean.getState(), Const.IntShow.TWO, position);
            }
        });
    }

    private void setBlcak(int userId, String roomId, int state, int type, final int position) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("rid", roomId);
        map.put("fgid", userToken);
        map.put("state", state);
        map.put("type", type);
        HttpManager.getInstance().post(Api.SaveRoomBlock, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("移除黑名单成功");
                    blackUserListAdapter.remove(position);
//                    blackUserListAdapter.notifyItemRemoved(position);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.getRoomBlock, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    blackUserListAdapter.setEnableLoadMore(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                RoomBlackBean roomBlackBean = JSON.parseObject(responseString, RoomBlackBean.class);
                if (roomBlackBean.getCode() == Api.SUCCESS) {
                    setData(roomBlackBean.getData());
                } else {
                    showToast(roomBlackBean.getMsg());
                }
            }
        });

    }

    private void setData(List<RoomBlackBean.DataBean> data) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            blackUserListAdapter.setNewData(data);
        } else {
            if (size > 0) {
                blackUserListAdapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            blackUserListAdapter.loadMoreEnd(false);
        } else {
            blackUserListAdapter.loadMoreComplete();
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
