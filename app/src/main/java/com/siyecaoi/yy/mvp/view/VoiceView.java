package com.siyecaoi.yy.mvp.view;

import com.siyecaoi.yy.bean.VoiceMicBean;
import com.siyecaoi.yy.bean.VoiceUserBean;
import com.siyecaoi.yy.model.GiftAllModel;
import com.siyecaoi.yy.mvp.basemvp.BaseView;

import java.util.List;

public interface VoiceView extends BaseView {

    //获取麦上用户列表(更新麦位信息)
    void onGetChatUserCallSuccess(List<VoiceMicBean.DataBean> chatUserList);

    //获取所以用户列表
    void onUserCallSuccess(List<VoiceUserBean.DataBean> userList);

    //用户上麦下麦
    void onMicStateChange(int uid, int type, int sequence);

    //点击设置
    void onUserSetClicker(int positon);

    /**
     * 普通用户点击自己的麦位
     *
     * @param position 点击的下标
     * @param sequence 麦位
     */
    void onUserDownClicker(int position, int sequence, String name);

    /**
     * 进入或退出房间
     *
     * @param type 类型
     */
    void onChatRoom(int userId, int type);

    /**
     * 显示用户资料
     *
     * @param userId  用户id
     * @param otherId 显示的用户id
     */
    void onDataShow(int userId, int otherId);

//    /**
//     * 麦位禁麦
//     *
//     * @param type
//     * @param sequence
//     */
//    void onMicSeatStateChange(int type, int sequence);

    /**
     * pk时点击赠送礼物
     *
     * @param pid
     * @param buid
     * @param userName
     */
    void onPkGiftSend(int pid, int buid, String userName);

    /**
     * pk时点击用户资料
     *
     * @param buid
     */
    void onPersonShow(int buid);

    /**
     * 返回接口请求数据
     *
     * @param res  数据
     * @param type 类型   1 pk记录数据    2 pk关闭  3判断用户是否关注房主
     *             5 踢出用户成功  6赠送浪花
     */
    void onCallbackShow(String res, int type);

    /**
     * 获取接口返回数据
     *
     * @param type           1 获取全国消息数据  2发送全平台消息 3,探险的开启和关闭和等级设置
     *                       4  赠送浪花
     * @param responseString
     */
    void onCallBack(int type, String responseString);

    /**
     * 麦位赠送礼物
     *
     * @param otherId  被送礼的用户id
     * @param userName 用户昵称
     */
    void onMicSendGift(int otherId, String userName);

    /**
     * 送礼物给在线用户
     */
    void onPersonSendGift(int otherId, String userName);

    /**
     * 打开红包成功
     *
     * @param packetId
     */
    void onPacketOpen(int packetId);

    /**
     * 礼物送出成功
     *
     * @param uid
     * @param ids
     * @param gid
     * @param img
     * @param showImg
     * @param num
     * @param goldNum 礼物单价
     */
    void onGiftSendSuccess(int uid, String ids, String names, int gid, String img, String showImg, int num, int goldNum);

    /**
     * 拉黑或取消拉黑
     *
     * @param userId   被拉黑的用户id
     * @param roomId   房间id
     * @param fgid     操作者id
     * @param state    操作者房间内的状态
     * @param type     1拉黑，2揭开
     * @param userName 被拉黑用户昵称
     */
    void onBlackListAdd(int userId, String roomId, int fgid, int state, final int type, String userName);

    /**
     * Toast消息展示
     *
     * @param msgShow
     */
    void onMsgShow(String msgShow);

    /**
     * 进入房间失败回调
     *
     * @param msgShow
     */
    void onJoinRoomShow(String msgShow);

    /**
     * 红包赠送成功
     */
    void onPacketSendSuccecss();

    void onGetOutSuccecss(int position);

    void onGetToken(String token);

    /**
     * 赠送浪花(代充值)
     *
     * @param dataBean
     */
    void onGoldSend(VoiceUserBean.DataBean dataBean);

    /**
     * 私聊
     */
    void onChatTo(int userId, String userName);


    /**
     * 发送房间属性变化 消息
     * @param micData
     */
    void onMicDataSend(String micData);

    /**
     * 发送房间信息更改 消息
     * @param roomData
     */
    void onRoomDataSend(String roomData);

    /**
     * 发送房间全服礼物消息通知
     * @param giftData
     */
    void onGiftAllSend(List<GiftAllModel.DataBean> giftData);


    /**
     * 声网RtmToken
     * @param agoraRtmToken
     */
//    void getAgoraRtmToken(String agoraRtmToken);

    /**
     * 声网RtcToken
     * @param agoraRtcToken
     */
    void getAgoraRtcToken(String agoraRtcToken);

    /**
     * 更新公屏状态
     * @param type
     */
    void updateGpState(int type);
}

