package com.jingtaoi.yy.mvp.model;


import android.content.Context;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.VoiceHomeBean;
import com.jingtaoi.yy.bean.VoiceMicBean;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.dialog.MyBottomShowDialog;
import com.jingtaoi.yy.dialog.UserOperationDialog;
import com.jingtaoi.yy.mvp.basemvp.BaseModel;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.ui.room.dialog.ManagerUserShowDialog;
import com.jingtaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.jingtaoi.yy.ui.room.dialog.UserShowDialog;
import com.jingtaoi.yy.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

public class VoiceModel implements BaseModel {

    private ArrayList<String> bottomList;//弹框显示内容

    private MyBottomShowDialog myBottomShowDialog;

    private Context context;
    private UserOperationDialog operationDialog;
    private UserShowDialog userShowDialog;
    private ManagerUserShowDialog managerUserShowDialog;

    public VoiceModel(Context context) {
        bottomList = new ArrayList<>();
        this.context = context;
    }

    /**
     * 进入房间(退出房间)
     *
     * @param uid
     * @param roomId
     * @param observer
     */
    public void getInputRoom(int uid, String roomId, int type, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("pid", roomId);
        map.put("type", type);
        HttpManager.getInstance().post(Api.chatRoom, map, observer);
    }

    /**
     * 在线状态
     */
    public void getOnline(String pid, Integer uid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("pid", pid);
        HttpManager.getInstance().post(Api.addTime, map, observer);
    }

    /**
     * 获取全平台消息
     */
    public void getAlltopic(Integer uid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        HttpManager.getInstance().post(Api.getScreen, map, observer);
    }

    /**
     * 探险的开启和关闭和等级设置
     */
    public void getRoomCahce(Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.SZ, map, observer);
    }

    /**
     * 发送全平台消息
     */
    public void getAllChat(Integer uid, String roomId, String content, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("content", content);
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.AddScreen, map, observer);
    }

    /**
     * 获取用户声网token
     */
    public void getToken(Integer uid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        HttpManager.getInstance().post(Api.SWToken, map, observer);
    }

    /**
     * 获取麦上用户
     *
     * @param uid
     * @param roomId
     * @param observer
     */
    public void getChatUserCall(int uid, String roomId, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("pid", roomId);
        HttpManager.getInstance().post(Api.voiceChatUser, map, observer);
    }

    /**
     * 获取房间信息
     *
     * @param uid
     * @param roomId
     * @param observer
     */
    public void getCall(int uid, String roomId, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("pid", roomId);
        HttpManager.getInstance().post(Api.getVoiceHome, map, observer);
    }

    /**
     * 获取房间所有用户信息
     *
     * @param uid
     * @param roomId
     * @param page
     * @param pageSize
     */
    public void getUserCall(int uid, String roomId, int page, int pageSize, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("pid", roomId);
        map.put("pageNum", page);
        map.put("pageSize", pageSize);
        HttpManager.getInstance().post(Api.voiceUser, map, observer);
    }

    /**
     * 获取用户上麦和下麦
     *
     * @param uid
     * @param pid      房间id
     * @param sequence 麦序
     * @param type     1 是上麦， 2是下麦
     * @param observer
     */
    public void getMicUpdateCall(int uid, String pid, int sequence, int type, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("pid", pid);
        map.put("sequence", sequence);
        map.put("type", type);
        if (type == 1) {
            map.put("state", Const.IntShow.ONE);
        }
        HttpManager.getInstance().post(Api.micUpdate, map, observer);
    }

    /**
     * 显示dialog并返回adapter(点击麦位时)
     *
     * @param dataBean     麦位数据
     * @param position     下标
     * @param userRoomType 当前用户角色  1房主  2管理员  3普通用户
     * @param userId       用户id
     * @return
     */
    public Object PlaceClicker(VoiceMicBean.DataBean dataBean, int position,
                               int userRoomType, int userId, int state) {
        if (dataBean.getUserModel() == null || dataBean.getUserModel().getId() == 0) {//麦位没有用户
            if (userRoomType == 1) {
                return showMybottomDialog(initBottomNopersonClicker(dataBean), context);
            } else if (userRoomType == 2) {
                return showMybottomDialog(initAdminNopersonClicker(dataBean), context);
            }
        } else {//麦位有用户
            switch (userRoomType) {
                case 1:
                    return showUserOperationDialog(initSetHomeMicClicker(dataBean, state), context, dataBean, userId);
                case 2:
                    if (dataBean.getUserModel().getId() == userId) { //管理员点击自己
                        return showManagerUserDialog(initSetAdminMineClicker(dataBean), context, dataBean, userId, true);
                    } else if (dataBean.getUserModel().getType() == 2) { //管理员点击其他管理员
                        return showManagerUserDialog(initSetAdminMicClicker(dataBean), context, dataBean, userId, false);
                    } else if (dataBean.getUserModel().getType() == 3) {
                        return showManagerUserDialog(initSetAdminMicClicker(dataBean), context, dataBean, userId, false);
                    }
                case 3:
                    if (dataBean.getUserModel().getId() == userId) {
                        return showOperationDialog(initDownUserClicker(), context, dataBean, userId, true);
                    } else { //点击别人送礼
                        return showOperationDialog(initDownUserOtherClicker(), context, dataBean, userId, false);
                    }
            }
        }
        return null;
    }

    /**
     * 管理员和用户点击房主事件
     *
     * @param dataBean
     * @param userRoomType
     * @param userId
     * @return
     */
    public Object HomePicker(VoiceMicBean.DataBean dataBean, int userRoomType, int userId) {
        switch (userRoomType) {
            case 1:
                return showUserOperationDialog(initHomeUserClicker(), context, dataBean, userId, true);
            case 2:
                return showManagerUserDialog(initDownManagerHomeClicker(), context, dataBean, userId, false);
            case 3:
                return showOperationDialog(initDownUserOtherClicker(), context, dataBean, userId, false);
        }
        return null;
    }

    public BottomShowRecyclerAdapter onLineClicker(VoiceUserBean.DataBean dataBean,
                                                   int userRoomType, int userId, int state) {
        if (userId == dataBean.getId()) { //点击自己弹出资料弹窗
            return null;
        } else {
            switch (userRoomType) {
                case 1:
                    return showMybottomDialog(initSetHomeOnlineClicker(userRoomType, dataBean, state), context);
                case 2:
                    if (dataBean.getType() == 1) {//	1是房主，2 是管理员，3用户
                        return null;
                    } else {
                        return showMybottomDialog(initSetHomeOnlineClicker(userRoomType, dataBean, state), context);
                    }

                case 3:
                    return null;
            }
        }
        return null;
    }

    /**
     * 点击设置时显示弹窗并返回adapter
     *
     * @param userRoomType 用户类型
     * @param roomBean
     * @return
     */
    public BottomShowRecyclerAdapter SetClicker(int userRoomType, VoiceHomeBean.DataBean.RoomBean roomBean) {
        switch (userRoomType) {
            case 1:
                return showMybottomDialog(initSetAdminClicker(roomBean), context);
            case 2:
                return showMybottomDialog(initSetAdminClicker(roomBean), context);
            case 3:
                return showMybottomDialog(initSetUserClicker(), context);
        }
        return null;
    }

    public BottomShowRecyclerAdapter setReport() {
        return showMybottomDialog(initReportRoom(), context);
    }

    /**
     * 普通用户点击自己麦位（查看资料，下麦旁听）
     *
     * @return
     */
    private ArrayList<String> initDownUserClicker() {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_message_bottomshow));
        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_down_bottomshow));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 普通用户点击其他麦位（查看资料，下麦旁听）
     *
     * @return
     */
    private ArrayList<String> initDownUserOtherClicker() {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_message_bottomshow));
        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_sixin));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 管理员点击房主麦位（查看资料，下麦旁听）
     *
     * @return
     */
    private ArrayList<String> initDownManagerHomeClicker() {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_message_bottomshow));
        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_sixin));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }


    /**
     * 普通用户点击设置
     *
     * @return
     */
    private ArrayList<String> initSetUserClicker() {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_informroom_bottomshow));
        bottomList.add(context.getString(R.string.tv_mix_bottomshow));
        bottomList.add(context.getString(R.string.tv_exitroom_bottomshow));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 房主点击在线自己麦位（查看资料）
     *
     * @return
     */
    private ArrayList<String> initHomeUserClicker() {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_message_bottomshow));
        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 房主点击在线用户（管理员）
     *
     * @return
     */
    private ArrayList<String> initSetHomeOnlineClicker(int userRoomType, VoiceUserBean.DataBean dataBean, int state) {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_message_bottomshow));
        bottomList.add(context.getString(R.string.tv_outhome_bottomshow));
        if (userRoomType == 1) {
            if (dataBean.getType() == 2) { //移除管理员
                bottomList.add(context.getString(R.string.tv_deladmin_bottomshow));
            } else if (dataBean.getType() == 3) { //设置管理员
                bottomList.add(context.getString(R.string.tv_setadmin_bottomshow));
            }
//            bottomList.add(context.getString(R.string.tv_setadmin_bottomshow));
        }
        if (userRoomType == 1) {
            bottomList.add(context.getString(R.string.tv_addblacklist_bottomshow));
            if (state == 2) {
                bottomList.add(context.getString(R.string.tv_send_bottomshow));
            }
        } else if (userRoomType == 2 && dataBean.getType() == 3) {
            bottomList.add(context.getString(R.string.tv_addblacklist_bottomshow));
        }
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }


    private ArrayList<String> initReportRoom() {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_sensitivity_report));
        bottomList.add(context.getString(R.string.tv_se_report));
        bottomList.add(context.getString(R.string.tv_ad_report));
        bottomList.add(context.getString(R.string.tv_at_report));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 管理员点击设置（房主）
     *
     * @return
     */
    private ArrayList<String> initSetAdminClicker(VoiceHomeBean.DataBean.RoomBean roomBean) {
        bottomList.clear();
        if (roomBean.getIsGp() == 1) { //公屏 1 开启， 2 关闭
            bottomList.add(context.getString(R.string.tv_closechat_bottomshow));
        } else if (roomBean.getIsGp() == 2) {
            bottomList.add(context.getString(R.string.tv_openchat_bottomshow));
        }
        bottomList.add(context.getString(R.string.tv_set_bottomshow));
//        if (roomBean.getIsJp() == 1) { //竞拍 1是未，2 是开启
//            bottomList.add(context.getString(R.string.tv_openauction_bottomshow));
//        } else if (roomBean.getIsJp() == 2) {
//            bottomList.add(context.getString(R.string.tv_disauction_bottomshow));
//        }
//        if (roomBean.getIsPk() == 1) { //是否开启pk 1是未，2 是开启
//            bottomList.add(context.getString(R.string.tv_openpk_bottomshow));
//        } else if (roomBean.getIsPk() == 2) {
//            bottomList.add(context.getString(R.string.tv_dispk_bottomshow));
//        }
        bottomList.add(context.getString(R.string.tv_mix_bottomshow));
        bottomList.add(context.getString(R.string.tv_exitroom_bottomshow));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 房主点击麦上用户
     *
     * @return
     */
    private ArrayList<String> initSetHomeMicClicker(VoiceMicBean.DataBean dataBean, int state) {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_sixin));
        bottomList.add(context.getString(R.string.tv_downwheat_bottomshow));
        if (dataBean.getState() == 1) {
            bottomList.add(context.getString(R.string.tv_banmai_bottomshow));
        } else if (dataBean.getState() == 2) {
            bottomList.add(context.getString(R.string.tv_mai_bottomshow));
        }
        bottomList.add(context.getString(R.string.tv_outhome_bottomshow));
        if (dataBean.getUserModel().getType() == 2) {
            bottomList.add(context.getString(R.string.tv_deladmin_bottomshow));
        } else if (dataBean.getUserModel().getType() == 3) {
            bottomList.add(context.getString(R.string.tv_setadmin_bottomshow));
        }
        bottomList.add(context.getString(R.string.tv_addblacklist_bottomshow));
        if (state == 2) {
            bottomList.add(context.getString(R.string.tv_send_bottomshow));
        }
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 管理员点击麦上其他用户
     *
     * @return
     */
    private ArrayList<String> initSetAdminMicClicker(VoiceMicBean.DataBean dataBean) {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_message_bottomshow));
        if (dataBean.getUserModel().getType() == 3) {
            bottomList.add(context.getString(R.string.tv_downwheat_bottomshow));
            if (dataBean.getState() == 1) {
                bottomList.add(context.getString(R.string.tv_banmai_bottomshow));
            } else if (dataBean.getState() == 2) {
                bottomList.add(context.getString(R.string.tv_mai_bottomshow));
            }
            bottomList.add(context.getString(R.string.tv_outhome_bottomshow));
            bottomList.add(context.getString(R.string.tv_addblacklist_bottomshow));
        }
        bottomList.add(context.getString(R.string.tv_sixin));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 管理员点击自己
     *
     * @param dataBean
     * @return
     */
    private ArrayList<String> initSetAdminMineClicker(VoiceMicBean.DataBean dataBean) {
        bottomList.clear();

        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_message_bottomshow));
        bottomList.add(context.getString(R.string.tv_down_bottomshow));
        if (dataBean.getState() == 1) {
            bottomList.add(context.getString(R.string.tv_banmai_bottomshow));
        } else if (dataBean.getState() == 2) {
            bottomList.add(context.getString(R.string.tv_mai_bottomshow));
        }

        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }


    /**
     * 其他位置用户点击
     * @return
     */
    public ArrayList<String> initOtherClicker() {
        bottomList.clear();

        bottomList.add(context.getString(R.string.tv_sendgift_bottomshow));
        bottomList.add(context.getString(R.string.tv_message_bottomshow));
        bottomList.add(context.getString(R.string.tv_sixin));
        return bottomList;
    }


    /**
     * 麦位为空时房主点击时选项
     */
    private ArrayList<String> initBottomNopersonClicker(VoiceMicBean.DataBean dataBean) {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_onwheat_bottomshow));
        if (dataBean.getState() == 1) {
            bottomList.add(context.getString(R.string.tv_banmai_bottomshow));
        } else if (dataBean.getState() == 2) {
            bottomList.add(context.getString(R.string.tv_mai_bottomshow));
        }

        if (dataBean.getStatus() == 2) {
            bottomList.add(context.getString(R.string.tv_noblock_bottomshow));
        } else if (dataBean.getStatus() == 1) {
            bottomList.add(context.getString(R.string.tv_block_bottomshow));
        }
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 麦位为空时管理员点击时选项
     */
    private ArrayList<String> initAdminNopersonClicker(VoiceMicBean.DataBean dataBean) {
        bottomList.clear();
        bottomList.add(context.getString(R.string.tv_onwheat_bottomshow));
        if (dataBean.getState() == 1) {
            bottomList.add(context.getString(R.string.tv_banmai_bottomshow));
        } else if (dataBean.getState() == 2) {
            bottomList.add(context.getString(R.string.tv_mai_bottomshow));
        }

        if (dataBean.getStatus() == 2) {
            bottomList.add(context.getString(R.string.tv_noblock_bottomshow));
        } else if (dataBean.getStatus() == 1) {
            bottomList.add(context.getString(R.string.tv_block_bottomshow));
        }
        bottomList.add(context.getString(R.string.tv_move_bottomshow));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * 处理底部弹框显示并返回adapter
     *
     * @param seatList 显示的数据源
     * @return adapter
     */
    private BottomShowRecyclerAdapter showMybottomDialog(ArrayList<String> seatList, Context context) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(context, seatList);
        myBottomShowDialog.show();
        return myBottomShowDialog.getAdapter();
    }


    /**
     * 处理底部弹框显示并返回adapter
     *
     * @param seatList 显示的数据源
     * @param dataBean
     * @return adapter
     */
    private UserOperationDialog showUserOperationDialog(ArrayList<String> seatList,
                                                        Context context,
                                                        VoiceMicBean.DataBean dataBean,
                                                        int userID) {
        if (operationDialog != null && operationDialog.isShowing()) {
            operationDialog.dismiss();
        }
        operationDialog = new UserOperationDialog(context, seatList, dataBean, userID);
        operationDialog.show();
        return operationDialog;
    }

    /**
     * 管理员点击
     *
     * @param seatList 显示的数据源
     * @param dataBean
     * @return adapter
     */
    private ManagerUserShowDialog showManagerUserDialog(ArrayList<String> seatList,
                                                        Context context,
                                                        VoiceMicBean.DataBean dataBean,
                                                        int userID,
                                                        boolean isShow) {
        if (managerUserShowDialog != null && managerUserShowDialog.isShowing()) {
            managerUserShowDialog.dismiss();
        }
        managerUserShowDialog = new ManagerUserShowDialog(context, seatList, dataBean, userID, isShow);
        managerUserShowDialog.show();
        return managerUserShowDialog;
    }


    /**
     * 房主点击
     *
     * @param seatList 显示的数据源
     * @param dataBean
     * @return adapter
     */
    private UserOperationDialog showUserOperationDialog(ArrayList<String> seatList, Context context,
                                                        VoiceMicBean.DataBean dataBean,
                                                        int userID,
                                                        boolean isUserOpration) {
        if (operationDialog != null && operationDialog.isShowing()) {
            operationDialog.dismiss();
        }
        operationDialog = new UserOperationDialog(context, seatList, dataBean, userID, isUserOpration);
        operationDialog.show();
        return operationDialog;
    }

    /**
     * 普通用户
     *
     * @param seatList 显示的数据源
     * @param dataBean
     * @return adapter
     */
    private UserShowDialog showOperationDialog(ArrayList<String> seatList, Context context,
                                               VoiceMicBean.DataBean dataBean,
                                               int userID,
                                               boolean isUserOpration) {
        if (userShowDialog != null && userShowDialog.isShowing()) {
            userShowDialog.dismiss();
        }
        userShowDialog = new UserShowDialog(context, seatList, dataBean, userID, isUserOpration);
        userShowDialog.show();
        return userShowDialog;
    }

    public void dissmissUserOperationDialog() {
        if (operationDialog != null && operationDialog.isShowing()) {
            operationDialog.dismiss();
        }
    }

    public void dissmissMybottomDialog() {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
    }

    /**
     * 禁麦或解禁此座位
     *
     * @param roomId   房间id
     * @param sequence 麦位
     * @param type     是否禁麦  1否 2是
     */
    public void getSeatState(String roomId, int userId, int sequence, int type, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("pid", roomId);
        map.put("uid", userId);
        map.put("sequence", sequence);
        map.put("type", type);
        HttpManager.getInstance().post(Api.getSeatState, map, observer);
    }

    /**
     * 封锁此座位
     *
     * @param roomId   房间id
     * @param sequence 麦位
     * @param type     是否禁麦  1否 2是
     */
    public void getSeatStates(String roomId, int userId, int sequence, int type, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("pid", roomId);
        map.put("uid", userId);
        map.put("sequence", sequence);
        map.put("type", type);
        HttpManager.getInstance().post(Api.getSeatStatus, map, observer);
    }

    /**
     * 设置或删除管理员
     *
     * @param userId   用户id
     * @param pid      房间id
     * @param type     1设置为管理员，2取消管理员
     * @param observer
     */
    public void setAdminState(int userId, String pid, int type, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("pid", pid);
        map.put("type", type);
        HttpManager.getInstance().post(Api.getChatroomsGLY, map, observer);
    }

    /**
     * 举报房间
     *
     * @param userId 用户id
     * @param roomId 房间id
     */
    public void getUpdateReport(int userId, int type, String roomId, int buid, String context, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("type", type);
//        map.put("buid", buid);
//        map.put("rid", roomId);
        if (type == 1 || type == 3) {
            map.put("buid", buid);
        } else if (type == 2) {
            map.put("rid", roomId);
        }
        map.put("content", context);
        HttpManager.getInstance().post(Api.ReportSave, map, observer);
    }

    /**
     * 拉黑或取消拉黑
     *
     * @param userId   被拉黑的用户id
     * @param roomId   房间id
     * @param fgid     操作者id
     * @param state    操作者房间内的状态
     * @param type     1拉黑，2揭开
     * @param observer
     */
    public void getAddRoomBlack(int userId, String roomId, int fgid, int state, int type, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("rid", roomId);
        map.put("fgid", fgid);
        map.put("state", state);
        map.put("type", type);
        HttpManager.getInstance().post(Api.SaveRoomBlock, map, observer);
    }


    /**
     * 开启或关闭公屏
     *
     * @param roomId
     * @param type
     * @param observer
     */
    public void getOpenGp(String roomId, int type, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("type", type);
        HttpManager.getInstance().post(Api.ChatroomsGP, map, observer);
    }

    /**
     * 开启或关闭竞拍
     *
     * @param roomId
     * @param type
     * @param observer
     */
    public void getOpenJp(String roomId, int type, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("type", type);
        HttpManager.getInstance().post(Api.UserAuction, map, observer);
    }

    public void getSendRedPacket(int uid, String rid, int gold, String ids, int num, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("rid", rid);
        map.put("gold", gold);
        map.put("ids", ids);
        map.put("num", num);
        HttpManager.getInstance().post(Api.SvaeRed, map, observer);
    }

    public void getSendGift(int uid, String rid, String ids, int gid, int num, int sum, int sendType, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("rid", rid);
        map.put("ids", ids);
        map.put("gid", gid);
        map.put("num", num);
        map.put("sum", sum);
        map.put("type", sendType);
        HttpManager.getInstance().post(Api.SaveGift, map, observer);
    }

    /**
     * @param state
     * @return
     */
    public BottomShowRecyclerAdapter getPkclicker(int state) {
        return showMybottomDialog(initPkClicker(state), context);
    }

    /**
     * 初始化pk用户点击事件
     *
     * @param state
     * @return
     */
    private ArrayList<String> initPkClicker(int state) {
        bottomList.clear();
        if (state == 1) {
            bottomList.add(context.getString(R.string.tv_tou_roompk));
        } else if (state == 2) {
            bottomList.add(context.getString(R.string.tv_send_roompk));
        }
        bottomList.add(context.getString(R.string.tv_look_roompk));
        bottomList.add(context.getString(R.string.tv_cancel));
        return bottomList;
    }

    /**
     * pk投票
     *
     * @param uid
     * @param pid
     * @param buid
     * @param num
     * @param gid
     * @param observer
     */
    public void getPkTou(int uid, int pid, int buid, int state, int num, int gid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("pid", pid);
        map.put("buid", buid);
        map.put("state", state);
        map.put("num", num);
        map.put("gid", gid);
        HttpManager.getInstance().post(Api.addPk, map, observer);
    }

    /**
     * 关闭pk
     *
     * @param pid
     * @param observer
     */
    public void getPkCloseCall(int pid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("pid", pid);
        HttpManager.getInstance().post(Api.delChatroomsPK, map, observer);
    }


    /**
     * pk数据
     *
     * @param pid
     * @param observer
     */
    public void getPkData(int pid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("pid", pid);
        HttpManager.getInstance().post(Api.ChatroomsPKOne, map, observer);
    }


    /**
     * 踢出房间
     *
     * @param rid
     * @param uid
     * @param buid
     * @param observer
     */
    public void getOut(String rid, int uid, int buid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", rid);
        map.put("uid", uid);
        map.put("buid", buid);
        HttpManager.getInstance().post(Api.deltrooms, map, observer);
    }

    /**
     * 打开红包
     *
     * @param id
     * @param observer
     */
    public void getOpenPacket(int id, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", id);
        HttpManager.getInstance().post(Api.GetRed, map, observer);
    }

    /**
     * 获取我是否关注和一些个人信息
     *
     * @param userId
     * @param otherId
     * @param observer
     */
    public void getUserAttention(int userId, int otherId, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("buid", otherId);
        HttpManager.getInstance().post(Api.UserAttention, map, observer);
    }

    /**
     * 代冲浪花
     *
     * @param uid
     * @param buid
     * @param gold
     * @param rid
     * @param observer
     */
    public void getSendGold(int uid, int buid, String gold, String rid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("buid", buid);
        map.put("gold", gold);
        map.put("rid", rid);
        HttpManager.getInstance().post(Api.getGeneration, map, observer);
    }

    /**
     * 一键全刷
     *
     * @param rid
     * @param uid
     * @param toUid
     * @param observer
     */
    public void getSendAllPackageGift(String rid, int uid, int toUid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", rid);
        map.put("uid", uid);
        map.put("toUid", toUid);
        HttpManager.getInstance().post(Api.aKeyAllBrush, map, observer);
    }

    /**
     * 转赠好友礼物
     *
     * @param rid
     * @param uid
     * @param toUid
     * @param observer
     */
    public void getSendPartyGift(String rid, int uid, int toUid, int giftid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", rid);
        map.put("uid", uid);
        map.put("buid", toUid);
        map.put("gid", giftid);
        HttpManager.getInstance().post(Api.givePartyGift, map, observer);
    }

    /**
     * 获取声网RtmToken
     *
     * @param uid
     * @param observer
     */
    public void getAgoraRtmToken(int uid, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        HttpManager.getInstance().post(Api.getAgoraRtmToken, map, observer);
    }

    /**
     * 获取声网RtcToken
     *
     * @param uid
     * @param observer
     */
    public void getAgoraRtcToken(int uid,String channelName, Observer<ResponseBody> observer) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("channelName", channelName);
        HttpManager.getInstance().post(Api.getAgoraRtcToken, map, observer);
    }

}
