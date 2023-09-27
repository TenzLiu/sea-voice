package com.siyecaoi.yy.mvp.presenter;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.bean.GiftBean;
import com.siyecaoi.yy.bean.GiveGiftResultBean;
import com.siyecaoi.yy.bean.MicXgfjBean;
import com.siyecaoi.yy.bean.OnlineUserBean;
import com.siyecaoi.yy.dialog.MyGiftDialog;
import com.siyecaoi.yy.dialog.UserOperationDialog;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.ui.room.adapter.GiftGroudAdapter;
import com.siyecaoi.yy.ui.room.dialog.ManagerUserShowDialog;
import com.siyecaoi.yy.ui.room.dialog.OtherShowDialog;
import com.siyecaoi.yy.ui.room.dialog.UserShowDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.bean.VoiceHomeBean;
import com.siyecaoi.yy.bean.VoiceMicBean;
import com.siyecaoi.yy.bean.VoiceUserBean;
import com.siyecaoi.yy.mvp.model.VoiceModel;
import com.siyecaoi.yy.mvp.view.VoiceView;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.OnlinesActivity;
import com.siyecaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.RoomUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.HashMap;
import java.util.List;

import cn.sinata.xldutils.utils.StringUtils;

public class VoicePresenter extends MyBasePresenter<VoiceView, VoiceModel> {

    public VoicePresenter(@NonNull VoiceView view, @NonNull VoiceModel model) {
        super(view, model);
    }

    /**
     * 获取房间信息
     *
     * @param uid
     * @param roomId
     */
    public void getCall(int uid, String roomId) {
        getModel().getCall(uid, roomId, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().onCallSuccess(responseString);
            }
        });
    }

    /**
     * 进出房间
     *
     * @param uid    用户id
     * @param roomId 房间id
     * @param type   1进房间，2是出房间
     */
    public void getChatRoom(int uid, String roomId, final int type) {
        getModel().getInputRoom(uid, roomId, type, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                MicXgfjBean baseBean = JSON.parseObject(responseString, MicXgfjBean.class);
                if (baseBean.getCode() == 0) {
                    getView().onMicDataSend(baseBean.getData().getAttr_mics());
                    if (!StringUtils.isEmpty(baseBean.getData().getAttr_xgfj())) {
                        getView().onRoomDataSend(baseBean.getData().getAttr_xgfj());
                    }
                    getView().onChatRoom(uid, type);
                } else {
                    getView().onJoinRoomShow(baseBean.getMsg());
//                    showToast(baseBean.getMsg());
//                    ActivityCollector.getActivityCollector().finishActivity();
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                showToast(msg);
                ActivityCollector.getActivityCollector().finishActivity();
            }
        });
    }

    /**
     * 退出房间 （从一个房间进入另一个房间，退出之前的房间）
     *
     * @param uid    用户id
     * @param roomId 房间id
     *               type   1进房间，2是出房间
     */
    public void getChatRoom(int uid, String roomId) {
        getModel().getInputRoom(uid, roomId, 2, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
//                MicXgfjBean baseBean = JSON.parseObject(responseString, MicXgfjBean.class);
//                if (baseBean.getCode() == 0) {
//                    getView().onMicDataSend(baseBean.getData().getAttr_mics());
//                    if (!StringUtils.isEmpty(baseBean.getData().getAttr_xgfj())){
//                        getView().onRoomDataSend(baseBean.getData().getAttr_xgfj());
//                    }
//                    getView().onChatRoom(uid, type);
//                } else {
//                    getView().onJoinRoomShow(baseBean.getMsg());
////                    showToast(baseBean.getMsg());
////                    ActivityCollector.getActivityCollector().finishActivity();
//                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
    }

    /**
     * 获取全平台消息
     *
     * @param uid
     */
    public void getAllTopic(int uid) {
        getModel().getAlltopic(uid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    getView().onCallBack(Const.IntShow.ONE, responseString);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    public void getRoomCahce() {
        getModel().getRoomCahce(new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().onCallBack(3, responseString);
            }
        });
    }

    /**
     * 发送全平台消息
     *
     * @param uid
     * @param content
     */
    public void getAllChat(int uid, String roomId, String content) {
        getModel().getAllChat(uid, roomId, content, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().onCallBack(Const.IntShow.TWO, responseString);
            }
        });
    }

    /**
     * 用户在线状态
     *
     * @param uid
     * @param pid
     */
    public void getOnline(int uid, String pid) {
        getModel().getOnline(pid, uid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {

            }
        });
    }

    public void getToken(int uid) {
        getModel().getToken(uid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                BaseBean getOneBean = JSON.parseObject(responseString, BaseBean.class);
                if (getOneBean.getCode() == 0) {
                    getView().onGetToken((String) getOneBean.getData());
                }
            }
        });
    }

    /**
     * 获取麦上用户列表
     *
     * @param uid
     * @param roomId
     */
    public void getChatShow(int uid, String roomId) {
        getModel().getChatUserCall(uid, roomId, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                VoiceMicBean voiceMicBean = JSON.parseObject(responseString, VoiceMicBean.class);
                if (voiceMicBean.getCode() == 0) {
                    getView().onGetChatUserCallSuccess(voiceMicBean.getData());
                } else {
                    showToast(voiceMicBean.getMsg());
                }
            }
        });
    }

    //获取所有用户列表
    public void getUserCall(int uid, String roomId, int page, int pageSize) {
        getModel().getUserCall(uid, roomId, page, pageSize, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                VoiceUserBean voiceMicBean = JSON.parseObject(responseString, VoiceUserBean.class);
                if (voiceMicBean.getCode() == 0) {
                    getView().onUserCallSuccess(voiceMicBean.getData());
                } else {
                    showToast(voiceMicBean.getMsg());
                }
            }
        });
    }

    /**
     * 获取用户上麦或者下麦
     *
     * @param uid      用户id
     * @param roomId   房间id
     * @param sequence 麦序 1-8
     * @param type     1 是上麦， 2是下麦
     */
    public void getMicUpdateCall(final int uid, String roomId, final int sequence, final int type) {
        getModel().getMicUpdateCall(uid, roomId, sequence, type, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                LogUtils.i("上下麦" + responseString);
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    getView().onMicDataSend((String) baseBean.getData());
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * 麦位点击事件
     *
     * @param dataBean     当前位置的数据
     * @param sequence     点击的下标
     * @param userId       当前用户id
     * @param userRoomType 当前用户角色  1房主  2管理员  3普通用户
     * @param roomId       房间id
     * @param state        是否牌子房间 1否，2是
     */
    public void PlaceClicker(final VoiceMicBean.DataBean dataBean, final int sequence, final int userId,
                             final int userRoomType, final String roomId, int state) {
        BottomShowRecyclerAdapter bottomShowRecyclerAdapter = null;
        if (dataBean.getUserModel() == null || dataBean.getUserModel().getId() == 0) { //没有用户
            switch (userRoomType) {
                case 1: //房主
                    bottomShowRecyclerAdapter =
                            (BottomShowRecyclerAdapter) getModel().PlaceClicker(dataBean, sequence, userRoomType, userId, state);
                    if (bottomShowRecyclerAdapter != null) {
                        bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                getModel().dissmissMybottomDialog();
                                switch (position) {
                                    case 0://抱他上麦
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Const.ShowIntent.ROOMID, roomId);
                                        bundle.putInt(Const.ShowIntent.TYPE, sequence + 1);
                                        ActivityCollector.getActivityCollector().toOtherActivity(OnlinesActivity.class, bundle, Const.RequestCode.MICUPDATE_CODE);
                                        break;
                                    case 1://禁麦或解禁
                                        if (dataBean.getState() == 1) { //禁麦
                                            setSeatState(roomId, userId, sequence + 1, Const.IntShow.TWO);
                                        } else if (dataBean.getState() == 2) { //解禁
                                            setSeatState(roomId, userId, sequence + 1, Const.IntShow.ONE);
                                        }
                                        break;
                                    case 2://封锁或解封
                                        if (dataBean.getStatus() == 1) { //封锁
                                            setSeatStates(roomId, userId, sequence + 1, Const.IntShow.TWO);
                                        } else if (dataBean.getStatus() == 2) {//解封
                                            setSeatStates(roomId, userId, sequence + 1, Const.IntShow.ONE);
                                        }
                                        break;
                                }
                            }
                        });
                    }
                    break;

                case 2: //管理员
                    bottomShowRecyclerAdapter =
                            (BottomShowRecyclerAdapter) getModel().PlaceClicker(dataBean, sequence, userRoomType, userId, state);
                    if (bottomShowRecyclerAdapter != null) {
                        bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                getModel().dissmissMybottomDialog();
                                switch (position) {
                                    case 0://抱他上麦
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Const.ShowIntent.ROOMID, roomId);
                                        bundle.putInt(Const.ShowIntent.TYPE, sequence + 1);
                                        ActivityCollector.getActivityCollector().toOtherActivity(OnlinesActivity.class, bundle, Const.RequestCode.MICUPDATE_CODE);
                                        break;
                                    case 1://禁麦或解禁
                                        if (dataBean.getState() == 1) { //禁麦
                                            setSeatState(roomId, userId, sequence + 1, Const.IntShow.TWO);
                                        } else if (dataBean.getState() == 2) {//解禁
                                            setSeatState(roomId, userId, sequence + 1, Const.IntShow.ONE);
                                        }
                                        break;
                                    case 2://封锁或解封
                                        if (dataBean.getStatus() == 1) { //封锁
                                            setSeatStates(roomId, userId, sequence + 1, Const.IntShow.TWO);
                                        } else if (dataBean.getStatus() == 2) { //解封
                                            setSeatStates(roomId, userId, sequence + 1, Const.IntShow.ONE);
                                        }
                                        break;
                                    case 3://移动到此麦位
                                        getMicUpdateCall(userId, roomId, sequence + 1, Const.IntShow.ONE);
                                        break;
                                }
                            }
                        });
                    }
                    break;
                case 3: //普通用户上麦
                    getMicUpdateCall(userId, roomId, sequence + 1, Const.IntShow.ONE);
                    break;
            }
        } else {  //麦位有用户
            switch (userRoomType) {
                case 1:
                    UserOperationDialog operationDialog =
                            (UserOperationDialog) getModel().PlaceClicker(dataBean, sequence, userRoomType, userId, state);
                    operationDialog.setOnItemClickListener(position -> {
                        getModel().dissmissUserOperationDialog();
                        switch (position) {
                            case 0://送礼
                                getView().onMicSendGift(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                                break;
                            case 1://查看资料
                                getView().onDataShow(userId, dataBean.getUserModel().getId());
                                break;
                            case 2://抱他下麦
                                getMicUpdateCall(dataBean.getUserModel().getId(), roomId, sequence, Const.IntShow.TWO);
                                break;
                            case 3://禁麦或解禁
                                if (dataBean.getState() == 1) { //禁麦
                                    setSeatState(roomId, userId, sequence + 1, Const.IntShow.TWO);
                                } else if (dataBean.getState() == 2) { //解禁
                                    setSeatState(roomId, userId, sequence + 1, Const.IntShow.ONE);
                                }
                                break;
                            case 4://踢出房间
                                setGetOut(roomId, userId, dataBean.getUserModel().getId());
                                break;
                            case 5://设置管理员
                                if (dataBean.getUserModel().getType() == 2) { //删除管理员
                                    setAdminState(roomId, dataBean.getUserModel().getId(), Const.IntShow.TWO);
                                } else if (dataBean.getUserModel().getType() == 3) { //设置管理员
                                    setAdminState(roomId, dataBean.getUserModel().getId(), Const.IntShow.ONE);
                                }
                                break;
                            case 6://加入黑名单
                                getView().onBlackListAdd(dataBean.getUserModel().getId(),
                                        roomId, userId, userRoomType, Const.IntShow.ONE, dataBean.getUserModel().getName());
                                break;
                            case 7://赠送浪花
                                getView().onGoldSend(dataBean.getUserModel());
                                break;
                            case 8://私信
                                getView().onChatTo(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                                break;
                            case 9://清魅力
                                getClearCall(userId, dataBean.getUserModel().getId(), roomId, dataBean.getSequence(), dataBean.getUserModel().getType());
                                break;

                        }
                    });

                    break;
                case 2:
                    if (dataBean.getUserModel().getId() == userId) { //管理员点击自己
                        ManagerUserShowDialog managerOperationDialog =
                                (ManagerUserShowDialog) getModel().PlaceClicker(dataBean, sequence, userRoomType, userId, state);
                        managerOperationDialog.setOnItemClickListener(position -> {
                            switch (position) {
                                case 0://查看资料
                                    getView().onDataShow(userId, dataBean.getUserModel().getId());
                                    break;
                                case 1://下麦旁听
                                    getMicUpdateCall(userId, roomId, sequence + 1, Const.IntShow.TWO);
                                    break;
                                case 2://禁麦或解禁
                                    if (dataBean.getState() == 1) { //禁麦
                                        setSeatState(roomId, userId, sequence + 1, Const.IntShow.TWO);
                                    } else if (dataBean.getState() == 2) { //解禁
                                        setSeatState(roomId, userId, sequence + 1, Const.IntShow.ONE);
                                    }
                                    break;
                                case 6://送礼物
                                    getView().onMicSendGift(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                                    break;
                                case 9://清魅力
                                    getClearCall(userId, dataBean.getUserModel().getId(), roomId, dataBean.getSequence(), dataBean.getUserModel().getType());
                                    break;
                            }
                        });

                    } else { //管理员点击别人
                        ManagerUserShowDialog operationDialog1 =
                                (ManagerUserShowDialog) getModel().PlaceClicker(dataBean, sequence, userRoomType, userId, state);
                        operationDialog1.setOnItemClickListener(position -> {
                            switch (position) {
                                case 0://查看资料
                                    getView().onDataShow(userId, dataBean.getUserModel().getId());
                                    break;
                                case 1://抱他下麦 (针对普通用户)
                                    if (dataBean.getUserModel().getType() == 3) {
                                        getMicUpdateCall(dataBean.getUserModel().getId(), roomId, sequence + 1, Const.IntShow.TWO);
                                    }
                                    break;
                                case 2://禁麦或解禁(针对普通用户)
                                    if (dataBean.getUserModel().getType() == 3) {
                                        if (dataBean.getState() == 1) { //禁麦
                                            setSeatState(roomId, userId, sequence + 1, Const.IntShow.TWO);
                                        } else if (dataBean.getState() == 2) { //解禁
                                            setSeatState(roomId, userId, sequence + 1, Const.IntShow.ONE);
                                        }
                                    }
                                    break;
                                case 4://踢出房间(针对普通用户)
                                    if (dataBean.getUserModel().getType() == 3) {
                                        setGetOut(roomId, userId, dataBean.getUserModel().getId());
                                    }
                                    break;
                                case 5://加入黑名单(针对普通用户)
                                    if (dataBean.getUserModel().getType() == 3) {
                                        getView().onBlackListAdd(dataBean.getUserModel().getId(),
                                                roomId, userId, userRoomType, Const.IntShow.ONE, dataBean.getUserModel().getName());
                                    }
                                    break;
                                case 6://送礼
                                    getView().onMicSendGift(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                                    break;
                                case 7://代充值
                                    getView().onGoldSend(dataBean.getUserModel());
                                    break;
                                case 8://私信
                                    getView().onChatTo(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                                    break;
                                case 9://清魅力
                                    getClearCall(userId, dataBean.getUserModel().getId(), roomId, dataBean.getSequence(), dataBean.getUserModel().getType());
                                    break;
                            }
                        });
                    }
                    break;
                case 3:
                    if (dataBean.getUserModel().getId() == userId) { //判断是否为自己
                        UserShowDialog operationDialog1 =
                                (UserShowDialog) getModel().PlaceClicker(dataBean, sequence, userRoomType, userId, state);
                        operationDialog1.setOnItemClickListener(position -> {
                            getView().onUserDownClicker(position, sequence + 1, dataBean.getUserModel().getName());
                        });

//                        bottomShowRecyclerAdapter =
//                                (BottomShowRecyclerAdapter) getModel().PlaceClicker(dataBean, sequence, userRoomType, userId, state);
//                        if (bottomShowRecyclerAdapter != null) {
//                            bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                                    getModel().dissmissMybottomDialog();
//                                    getView().onUserDownClicker(position, sequence + 1);
//                                }
//                            });
//                        }
                    } else {  // 点击别人
                        UserShowDialog userShowDialog =
                                (UserShowDialog) getModel().PlaceClicker(dataBean, sequence, userRoomType, userId, state);
                        userShowDialog.setOnItemClickListener(position -> {
                            switch (position) {
                                case 0://查看资料
                                    getView().onDataShow(userId, dataBean.getUserModel().getId());
                                    break;
                                case 6://送礼
                                    getView().onMicSendGift(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                                    break;
                                case 7://代充值
                                    getView().onGoldSend(dataBean.getUserModel());
                                    break;
                                case 8://私信
                                    getView().onChatTo(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                                    break;
                            }
                        });
                    }
                    break;
            }
        }

    }

    /**
     * 在线用户点击事件
     *
     * @param dataBean
     * @param userRoomType
     * @param userId
     * @param roomId
     * @param state        //是否牌子房间 1否，2是
     */
    public void onLineClicker(VoiceUserBean.DataBean dataBean, int userRoomType, int userId, String roomId, int position, int state) {
        BottomShowRecyclerAdapter bottomShowRecyclerAdapter =
                getModel().onLineClicker(dataBean, userRoomType, userId, state);
        if (userId == dataBean.getId()) { //点击自己查看资料
            getModel().dissmissMybottomDialog();
            getView().onDataShow(userId, userId);
        } else {
            switch (userRoomType) {
                case 1:
                    if (bottomShowRecyclerAdapter != null) {
                        bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                getModel().dissmissMybottomDialog();
                                switch (position) {
                                    case 0://送礼物
                                        getView().onPersonSendGift(dataBean.getId(), dataBean.getName());
                                        break;
                                    case 1://查看资料
                                        getView().onDataShow(userId, dataBean.getId());
                                        break;
                                    case 2://踢出房间
                                        setGetOut(roomId, userId, dataBean.getId(), position);
                                        break;
                                    case 3://设置管理员
                                        if (dataBean.getType() == 2) { //移除管理员
                                            setAdminState(roomId, dataBean.getId(), Const.IntShow.TWO);
                                        } else if (dataBean.getType() == 3) { //设置管理员
                                            setAdminState(roomId, dataBean.getId(), Const.IntShow.ONE);
                                        }
                                        break;
                                    case 4://加入黑名单
                                        getView().onBlackListAdd(dataBean.getId(),
                                                roomId, userId, userRoomType, Const.IntShow.ONE, dataBean.getName());
                                        break;
                                    case 5://赠送浪花
                                        if (state == 2) {
                                            getView().onGoldSend(dataBean);
                                        }
                                        break;
                                }
                            }
                        });
                    }
                    break;
                case 2:
                    if (dataBean.getType() == 1) { //管理员点击房主为送礼物
                        getView().onPersonSendGift(dataBean.getId(), dataBean.getName());
                    } else {
                        if (bottomShowRecyclerAdapter != null) {
                            bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    getModel().dissmissMybottomDialog();
                                    switch (position) {
                                        case 0://送礼物
                                            getView().onPersonSendGift(dataBean.getId(), dataBean.getName());
                                            break;
                                        case 1://查看资料
                                            getView().onDataShow(userId, dataBean.getId());
                                            break;
                                        case 2://踢出房间
                                            setGetOut(roomId, userId, dataBean.getId());
                                            break;
                                        case 3://加入黑名单
                                            getView().onBlackListAdd(dataBean.getId(),
                                                    roomId, userId, userRoomType, Const.IntShow.ONE, dataBean.getName());

                                            break;
                                    }
                                }
                            });
                        }
                    }
                    break;
                case 3://用户点击其他人为送礼物
                    getView().onPersonSendGift(dataBean.getId(), dataBean.getName());
                    break;
            }
        }
    }

    /**
     * 点击房主事件（房主、管理员和普通用户）
     *
     * @param dataBean
     * @param userId
     * @param userRoomType
     * @param roomId
     */
    public void onUserClicker(final VoiceMicBean.DataBean dataBean, final int userId,
                              final int userRoomType, final String roomId) {
        if (userRoomType == 1) {
            UserOperationDialog userOperationDialog =
                    (UserOperationDialog) getModel().HomePicker(dataBean, userRoomType, userId);
            userOperationDialog.setOnItemClickListener(position -> {
                getModel().dissmissUserOperationDialog();
                switch (position) {
                    case 1://查看资料
                        getView().onDataShow(userId, dataBean.getUserModel().getId());
                        break;
                    case 0://送礼
                        getView().onMicSendGift(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                        break;
                    case 9://清魅力
                        getClearCall(userId, dataBean.getUserModel().getId(), roomId, dataBean.getSequence(), dataBean.getUserModel().getType());
                        break;
                }
            });
        } else if (userRoomType == 2) {
            ManagerUserShowDialog operationDialog1 =
                    (ManagerUserShowDialog) getModel().HomePicker(dataBean, userRoomType, userId);
            operationDialog1.setOnItemClickListener(position -> {
                switch (position) {
                    case 0://查看资料
                        getView().onDataShow(userId, dataBean.getUserModel().getId());
                        break;
                    case 6://送礼
                        getView().onMicSendGift(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                        break;
                    case 7://代充值
                        getView().onGoldSend(dataBean.getUserModel());
                        break;
                    case 8://私信
                        getView().onChatTo(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                        break;
                    case 9://清魅力
                        getClearCall(userId, dataBean.getUserModel().getId(), roomId, dataBean.getSequence(), dataBean.getUserModel().getType());
                        break;
                }
            });
        } else if (userRoomType == 3) {
            UserShowDialog userShowDialog =
                    (UserShowDialog) getModel().HomePicker(dataBean, userRoomType, userId);
            userShowDialog.setOnItemClickListener(position -> {
                switch (position) {
                    case 0://查看资料
                        getView().onDataShow(userId, dataBean.getUserModel().getId());
                        break;
                    case 6://送礼
                        getView().onMicSendGift(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                        break;
                    case 7://代充值
                        getView().onGoldSend(dataBean.getUserModel());
                        break;
                    case 8://私信
                        getView().onChatTo(dataBean.getUserModel().getId(), dataBean.getUserModel().getName());
                        break;
                }
            });
        }
    }

    private void setGetOut(String rid, int uid, int buid) {
        getModel().getOut(rid, uid, buid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                MicXgfjBean baseBean = JSON.parseObject(responseString, MicXgfjBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    getView().onCallbackShow(responseString, Const.IntShow.FIVE);
                    getView().onMicDataSend(baseBean.getData().getAttr_mics());
                    if (!StringUtils.isEmpty(baseBean.getData().getAttr_xgfj())) {
                        getView().onRoomDataSend(baseBean.getData().getAttr_xgfj());
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    //其他位置点击弹窗
    OtherShowDialog otherShowDialog;

    public void showOtherDialog(int position, int userId, OnlineUserBean.DataBean.UserBean user) {
        switch (position) {
            case 0://查看资料
                getView().onDataShow(userId, user.getId());
                break;
            case 6://送礼
                getView().onMicSendGift(user.getId(), user.getName());
                break;
            case 7://代充值
                VoiceUserBean.DataBean dataBean = new VoiceUserBean.DataBean();
                dataBean.setName(user.getName());
                dataBean.setUsercoding(user.getUsercoding());
                dataBean.setId(user.getId());
                getView().onGoldSend(dataBean);
                break;
            case 8://私信
                getView().onChatTo(user.getId(), user.getName());
                break;
        }
    }

    /**
     * @param rid
     * @param uid
     * @param buid
     */
    private void setGetOut(String rid, int uid, int buid, int position) {
        getModel().getOut(rid, uid, buid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                MicXgfjBean baseBean = JSON.parseObject(responseString, MicXgfjBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    getView().onGetOutSuccecss(position);
                    getView().onMicDataSend(baseBean.getData().getAttr_mics());
                    if (!StringUtils.isEmpty(baseBean.getData().getAttr_xgfj())) {
                        getView().onRoomDataSend(baseBean.getData().getAttr_xgfj());
                    }
                } else {
                    showToast(baseBean.getMsg());
                }

            }
        });
    }

    /**
     * 禁言或解禁此座位
     *
     * @param roomId
     * @param sequence
     * @param type
     */
    private void setSeatState(String roomId, int userId, int sequence, int type) {
        getModel().getSeatState(roomId, userId, sequence, type, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                LogUtils.i("禁麦或解禁" + responseString);
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    getView().onMicDataSend((String) baseBean.getData());
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * 封锁或解封此座位
     *
     * @param roomId
     * @param sequence
     * @param type
     */
    private void setSeatStates(String roomId, int userId, int sequence, int type) {
        getModel().getSeatStates(roomId, userId, sequence, type, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    getView().onMicDataSend((String) baseBean.getData());
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * 点击设置
     *
     * @param userRoomType
     * @param roomBean
     */
    public void SetClicker(int userRoomType, VoiceHomeBean.DataBean.RoomBean roomBean) {
        BottomShowRecyclerAdapter bottomShowRecyclerAdapter = getModel().SetClicker(userRoomType, roomBean);
        if (bottomShowRecyclerAdapter != null) {
            bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    getModel().dissmissMybottomDialog();
                    getView().onUserSetClicker(position);
                }
            });
        }
    }

    /**
     * 举报房间
     *
     * @param uid    用户id
     * @param roomId 房间id
     */
    public void updateReport(final int uid, final String roomId) {
        BottomShowRecyclerAdapter bottomShowRecyclerAdapter = getModel().setReport();
        if (bottomShowRecyclerAdapter != null) {
            bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    getModel().dissmissMybottomDialog();
                    if (position == 4) {
                        return;
                    }
                    String updateShow = (String) adapter.getItem(position);
                    getModel().getUpdateReport(uid, Const.IntShow.TWO, roomId, 0,
                            updateShow, new MyObserver(getView().getSelfActivity()) {
                                @Override
                                public void success(String responseString) {
                                    BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                                    if (baseBean.getCode() == Api.SUCCESS) {
                                        showToast("举报成功，我们会尽快为您处理");
                                    } else {
                                        showToast(baseBean.getMsg());
                                    }
                                }
                            });
                }
            });
        }
    }

    /**
     * 设置或删除管理员
     *
     * @param roomId 用户id
     * @param roomId 房间id
     * @param type   1设置为管理员，2取消管理员
     */
    private void setAdminState(String roomId, int userId, int type) {
        getModel().setAdminState(userId, roomId, type, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().onCallbackShow(responseString, Const.IntShow.FOUR);
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
//                    showToast(baseBean.getMsg());
                    getView().onMicDataSend((String) baseBean.getData());
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * 拉黑或取消拉黑
     *
     * @param userId 被拉黑的用户id
     * @param roomId 房间id
     * @param fgid   操作者id
     * @param state  操作者房间内的状态
     * @param type   1拉黑，2揭开
     */
    public void addRoomBlack(int userId, String roomId, int fgid, int state, final int type) {
        getModel().getAddRoomBlack(userId, roomId, fgid, state, type, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() != 0) {
                    showToast(baseBean.getMsg());
                } else {
                    if (type == 1) {
                        showToast(getView().getSelfActivity().getString(R.string.tv_addblack_hint));
                    } else if (type == 2) {
                        showToast(getView().getSelfActivity().getString(R.string.tv_disblack_hint));
                    }
                }
            }
        });
    }

    /**
     * 打开或关闭公屏
     *
     * @param rid
     * @param type
     */
    public void addOpenGp(String rid, final int type) {
        getModel().getOpenGp(rid, type, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() != 0) {
                    showToast(baseBean.getMsg());
                } else {
                    getView().updateGpState(type);
                }
            }
        });
    }

    /**
     * 开启或关闭竞拍
     *
     * @param rid  房间id
     * @param type 1开启竞拍， 2关闭竞拍
     */
    public void IsOpenJp(String rid, final int type) {
        getModel().getOpenJp(rid, type, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() != 0) {
                    showToast(baseBean.getMsg());
                } else {
                    if (type == 1) {
                        showToast(getView().getSelfActivity().getString(R.string.tv_openjp_hint));
                    } else if (type == 2) {
                        showToast(getView().getSelfActivity().getString(R.string.tv_disjp_hint));
                    }
                }
            }
        });
    }

    /**
     * 房间送红包
     *
     * @param uid
     * @param rid
     * @param gold
     * @param ids
     * @param num
     */
    public void sendRedPacket(int uid, String rid, int gold, String ids, int num) {
        getModel().getSendRedPacket(uid, rid, gold, ids, num, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                UserBean baseBean = JSON.parseObject(responseString, UserBean.class);
                if (baseBean.getCode() == 0) {
                    SharedPreferenceUtils.put(getView().getSelfActivity(), Const.User.GOLD, baseBean.getData().getGold());
                    getView().onPacketSendSuccecss();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * 送礼物
     *
     * @param uid      用户id
     * @param rid      房间id
     * @param ids      被送用户id 用逗号隔开
     * @param gid      礼物id
     * @param num      送礼物数
     * @param sum      送礼物的人数
     * @param sendType 1 是普通， 2是背包
     */
    public void sendGift(int uid, String rid, String ids, String names, int gid, String img,
                         String showImg, int num, int sum, int goldNum, int sendType) {
        getModel().getSendGift(uid, rid, ids, gid, num, sum, sendType, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                MicXgfjBean baseBean = JSON.parseObject(responseString, MicXgfjBean.class);
                getView().onCallBack(6, responseString);
                if (baseBean.getCode() == 0) {
                    showToast(getView().getSelfActivity().getString(R.string.hint_success_gift));
                    SharedPreferenceUtils.put(getView().getSelfActivity(), Const.User.GOLD, baseBean.getData().getUser().getGold());
                    getView().onGiftSendSuccess(uid, ids, names, gid, img, showImg, num, goldNum);
                    getView().onMicDataSend(baseBean.getData().getAttr_mics());
                    if (!StringUtils.isEmpty(baseBean.getData().getAttr_xgfj())) {
                        getView().onRoomDataSend(baseBean.getData().getAttr_xgfj());
                    }
//                    if (baseBean.getData().getAttr_xgfj().getState() == 2) {
//                        if (baseBean.getData().getAttr_xgfj().getState() == 2) {
//                            if (baseBean.getData().getMsgList() != null && baseBean.getData().getMsgList().size() > 0) {
//                                getView().onGiftAllSend(baseBean.getData().getMsgList());
//                            }
//                        }
//                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * pk用户点击事件
     *
     * @param state
     */
    public void setPkClicker(int state, int uid, int pid, int buid, String userName) {
        BottomShowRecyclerAdapter bottomShowRecyclerAdapter = getModel().getPkclicker(state);
        bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getModel().dissmissMybottomDialog();

                switch (position) {
                    case 0:
                        if (state == 1) { //1 按人数投票，2 按礼物价值投票
                            getTou(uid, pid, buid, Const.IntShow.ONE, 0, 0, null, null, null, 0);
                        } else if (state == 2) {
                            getView().onPkGiftSend(pid, buid, userName);
                        }
                        break;
                    case 1:
                        getView().onPersonShow(buid);
                        break;
                }
            }
        });
    }

    /**
     * 房间pk投票
     *
     * @param uid
     * @param pid
     * @param buid
     * @param showImg
     * @param img
     * @param state   1 人数投票  2礼物价值投票
     * @param num
     * @param gid
     */
    public void getTou(int uid, int pid, int buid, int state, int num, int gid, String img, String showImg, String names, int goldNum) {
        getModel().getPkTou(uid, pid, buid, state, num, gid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                UserBean baseBean = JSON.parseObject(responseString, UserBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("投票成功");
                    if (state == Const.IntShow.TWO) {
                        SharedPreferenceUtils.put(getView().getSelfActivity(), Const.User.GOLD, baseBean.getData().getGold());
                        getView().onGiftSendSuccess(uid, String.valueOf(buid), names, gid, img, showImg, num, goldNum);
                    }
                } else {
                    showToast(baseBean.getMsg());
                }

            }
        });
    }

    /**
     * 关闭pk
     *
     * @param pid pk记录数据id
     */
    public void pkClose(int pid) {
        getModel().getPkCloseCall(pid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("关闭pk成功");
//                    getView().onCallbackShow(responseString, Const.IntShow.TWO);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * 弃用
     *
     * @param pid
     */
    public void pkData(int pid) {
        getModel().getPkData(pid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {

            }
        });
    }

    /**
     * 打开红包
     *
     * @param id
     */
    public void openPacket(int id, int packetId) {
        getModel().getOpenPacket(id, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    getView().onPacketOpen(packetId);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * 判断用户是否关注房主
     *
     * @param userId
     * @param otherId
     */
    public void getUserAttention(int userId, int otherId) {
        getModel().getUserAttention(userId, otherId, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().onCallbackShow(responseString, Const.IntShow.THREE);
            }
        });
    }

    public void getSendGoldCall(int uid, int buid, String gold, String rid) {
        getModel().getSendGold(uid, buid, gold, rid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                RoomUtils.sendPrivateCoinTips(String.valueOf(buid),gold);
                getView().onCallBack(4, responseString);
            }
        });
    }

    public void getSendAllGift(String rid, int uid, int buid, MyGiftDialog giftDialog) {
        getModel().getSendAllPackageGift(rid, uid, buid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().onCallBack(5, responseString);
                MicXgfjBean baseBean = JSON.parseObject(responseString, MicXgfjBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("一键全刷成功");
                    getView().onMicDataSend(baseBean.getData().getAttr_mics());
                    if (!StringUtils.isEmpty(baseBean.getData().getAttr_xgfj())) {
                        getView().onRoomDataSend(baseBean.getData().getAttr_xgfj());
                    }
                    //发个私聊消息
                    // 礼物1 * 19
                    //礼物2 * 19
                    //礼物3 * 19
                    //礼物4 * 19
                    //总价值:（数值）*0.8=1111
                    if (giftDialog != null && giftDialog.getGroudAdapterList() != null) {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append("\uD83C\uDF81送给你\n");
                        int totalValue = 0;
                        for (GiftGroudAdapter giftGroudAdapter : giftDialog.getGroudAdapterList()) {
                            List<GiftBean.DataBean> giftBeans = giftGroudAdapter.getData();
                            if (giftBeans != null && giftBeans.size() > 0) {
                                for (GiftBean.DataBean bean : giftBeans) {
                                    totalValue = totalValue + bean.getNum() * bean.getGold();
                                    stringBuffer.append(bean.getName()).append("*").append(bean.getNum()).append("\n");
                                }
                            }

                        }
                        stringBuffer.append("总价值：").append(totalValue).append("*0.8\n=").append(String.format("%.2f", totalValue*0.8));
                        RoomUtils.sendPrivateGiftTips(String.valueOf(buid),stringBuffer.toString());
//                        TIMMessage msg = new TIMMessage();
//                        TIMTextElem timTextElem = new TIMTextElem();
//                        timTextElem.setText(stringBuffer.toString());
//                        msg.addElement(timTextElem);
//                        TIMManager.getInstance().getConversation(TIMConversationType.C2C, String.valueOf(buid)).sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
//                            @Override
//                            public void onError(int i, String s) {
//
//                            }
//
//                            @Override
//                            public void onSuccess(TIMMessage timMessage) {
//
//                            }
//                        });
                    }

//                    if (baseBean.getData().getAttr_xgfj().getState() == 2) {
//                        if (baseBean.getData().getMsgList() != null && baseBean.getData().getMsgList().size() > 0) {
//                            getView().onGiftAllSend(baseBean.getData().getMsgList());
//                        }
//                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    public void getSendPartyGift(String rid, int uid, int buid, int giftid) {
        getModel().getSendPartyGift(rid, uid, buid, giftid, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().onCallBack(8, responseString);
            }
        });
    }

//    public void getAgoraRtmToken(int uid) {
//        getModel().getAgoraRtmToken( uid, new MyObserver(getView().getSelfActivity()) {
//            @Override
//            public void success(String responseString) {
//                getView().getAgoraRtmToken(responseString);
//            }
//        });
//    }

    public void getAgoraRtcToken(int uid, String channelName) {
        getModel().getAgoraRtcToken(uid, channelName, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().getAgoraRtcToken(responseString);
            }
        });
    }

    /**
     * @param userToken
     * @param roomId    房间id
     */
    private void getClearCall(int userToken, int userId, String roomId, int sequence, int type) {

        //如果是房主，清除魅力就调用这个
        if (sequence == 0 && type == 0) {
            clearFZMl(userId, roomId);
        } else {
            //其他8个坑位,清除魅力调用这个
            clearQt(userToken, userId, roomId, sequence);
        }

    }

    private void clearQt(int userToken, int otherId, String roomId, int sequence) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", otherId);
        map.put("pid", roomId);
        map.put("sequence", sequence);
        HttpManager.getInstance().post(Api.QTROOMDEL, map, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {

                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    showToast("清除成功");
                    getView().onMicDataSend((String) baseBean.getData());
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }


    public void clearFZMl(int userToken, String roomId) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.RoomDel, map, new MyObserver(getView().getSelfActivity()) {
            @Override
            public void success(String responseString) {
                getView().onCallBack(7, responseString);
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    showToast("清除成功");
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }
}
