package com.siyecaoi.yy.ui.room;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.MyattentionBean;
import com.siyecaoi.yy.model.CustomOneModel;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.InviteFriendListAdapter;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分享 深海好友页面
 */
public class InviteFriendActivity extends MyBaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    InviteFriendListAdapter inviteFriendListAdapter;

    boolean isChoice;//是否多选
    @BindView(R.id.tv_all_invitefriend)
    TextView tvAllInvitefriend;
    @BindView(R.id.tv_invite_invitefriend)
    TextView tvInviteInvitefriend;
    @BindView(R.id.ll_bottom_invitefriend)
    LinearLayout llBottomInvitefriend;
    String roomId;
    String img, userName;
    boolean isAllChoice;//是否全选
    int choiceNumber;//选中的个数

    @Override
    public void initData() {
        roomId = getBundleString(Const.ShowIntent.ID);
        img = getBundleString(Const.ShowIntent.IMG);
        userName = getBundleString(Const.ShowIntent.NAME);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_invitefriend);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_invitee);
        setRightText(R.string.tv_more_friend);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChoiceShow();
            }
        });

        setRecycler();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                page = 1;
                getCall();
            }
        });
    }

    private void setChoiceShow() {
        if (isChoice) {
            isChoice = false;
            llBottomInvitefriend.setVisibility(View.GONE);
        } else {
            isChoice = true;
            llBottomInvitefriend.setVisibility(View.VISIBLE);
        }
        if (inviteFriendListAdapter != null) {
            inviteFriendListAdapter.setChoice(isChoice);
        }
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", Const.IntShow.ONE);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.addfriendList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    inviteFriendListAdapter.setEnableLoadMore(true);
                }
                MyattentionBean myattentionBean = JSON.parseObject(responseString, MyattentionBean.class);
                if (myattentionBean.getCode() == Api.SUCCESS) {
                    setData(myattentionBean.getData(), inviteFriendListAdapter);
                } else {
                    showToast(myattentionBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    inviteFriendListAdapter.setEnableLoadMore(true);
                }
            }
        });
    }

    private void setData(List<MyattentionBean.DataEntity> data, InviteFriendListAdapter adapter) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            adapter.setNewData(data);
        } else {
            if (size > 0) {
                adapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            adapter.loadMoreEnd(true);
        } else {
            adapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
        inviteFriendListAdapter = new InviteFriendListAdapter(R.layout.item_invitefriend);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(inviteFriendListAdapter);

        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        ivNodata.setImageResource(R.drawable.friend_empty);
        tvNodata.setText(getString(R.string.hint_nodata_friend));
        inviteFriendListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inviteFriendListAdapter.setEnableLoadMore(false);
                page = 1;
                getCall();
            }
        });

        inviteFriendListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCall();
            }
        }, mRecyclerView);

        inviteFriendListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyattentionBean.DataEntity dataEntity = (MyattentionBean.DataEntity) adapter.getItem(position);
                assert dataEntity != null;
                switch (view.getId()) {
                    case R.id.tv_invite_invitefriend:
                        dataEntity.setSend(true);
                        inviteFriendListAdapter.setData(position, dataEntity);
                        showToast("邀请成功");
                        setInviteOther(dataEntity);
                        break;
                    case R.id.iv_invite_invitefriend:
                        if (dataEntity.isSelect()) {
                            dataEntity.setSelect(false);
                            inviteFriendListAdapter.setData(position, dataEntity);
                            if (choiceNumber > 0) {
                                if (choiceNumber == adapter.getItemCount()) {
                                    isAllChoice = false;
                                    tvAllInvitefriend.setText("全选");
                                }
                                choiceNumber--;
                                if (choiceNumber == 0) {
                                    tvInviteInvitefriend.setAlpha(0.4f);
                                }
                            }
                        } else {
                            dataEntity.setSelect(true);
                            inviteFriendListAdapter.setData(position, dataEntity);
                            if (choiceNumber == 0) {
                                tvInviteInvitefriend.setAlpha(1.0f);
                            }
                            choiceNumber++;
                            if (choiceNumber == adapter.getItemCount()) {
                                isAllChoice = true;
                                tvAllInvitefriend.setText("取消全选");
                            }
                        }
                        break;
                }
            }
        });

    }

    //邀请 深海好友进入房间消息
    private void setInviteOther(MyattentionBean.DataEntity dataEntity) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                String.valueOf(dataEntity.getId()));

        CustomOneModel customOneModel = new CustomOneModel();
        customOneModel.setShowMsg("我邀请你参加【" + userName + "】的房间， 快来吧！");
        customOneModel.setShowUrl(img);
        customOneModel.setRoomId(roomId);
        customOneModel.setState(Const.IntShow.ONE);
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[房间邀请]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        String msgShow = JSON.toJSONString(customOneModel);
        //向 TIMMessage 中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgShow.getBytes());      //自定义 byte[]
        elem.setDesc("[房间邀请]"); //自定义描述信息
        //将 elem 添加到消息
        if (msg.addElement(elem) != 0) {
            LogUtils.d(LogUtils.TAG, "addElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() { //发送消息回调
            @Override
            public void onError(int code, String desc) { //发送消息失败
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
                LogUtils.e(LogUtils.TAG, "send message failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) { //发送消息成功
                LogUtils.e(LogUtils.TAG, "SendMsg ok");
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

    @OnClick({R.id.tv_all_invitefriend, R.id.tv_invite_invitefriend})
    public void onViewClicked(View view) {
        List<MyattentionBean.DataEntity> data = inviteFriendListAdapter.getData();
        switch (view.getId()) {
            case R.id.tv_all_invitefriend:
                for (MyattentionBean.DataEntity dataEntity : data) {
                    if (isAllChoice) {
                        dataEntity.setSelect(false);
                    } else {
                        dataEntity.setSelect(true);
                    }
                }
                if (isAllChoice) {
                    choiceNumber = 0;
                    tvAllInvitefriend.setText("全选");
                    tvInviteInvitefriend.setAlpha(0.4f);
                    isAllChoice = false;
                } else {
                    tvAllInvitefriend.setText("取消全选");
                    tvInviteInvitefriend.setAlpha(1.0f);
                    choiceNumber = data.size();
                    isAllChoice = true;
                }
                inviteFriendListAdapter.replaceData(data);
                break;
            case R.id.tv_invite_invitefriend:
                if (choiceNumber == 0) {
                    showToast("请选择邀请用户");
                    return;
                }
                for (MyattentionBean.DataEntity dataEntity : data) {
                    if (dataEntity.isSelect()) {
                        if (dataEntity.isSend()) {  //判断是否发送过

                        } else {
                            dataEntity.setSend(true);
                            setInviteOther(dataEntity);
                        }
                    }
                }
                inviteFriendListAdapter.replaceData(data);
                setChoiceShow();
                break;
        }
    }
}
