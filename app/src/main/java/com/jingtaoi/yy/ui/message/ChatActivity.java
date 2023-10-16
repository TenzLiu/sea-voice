package com.jingtaoi.yy.ui.message;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.dialog.MyBottomPersonDialog;
import com.jingtaoi.yy.dialog.MyBottomShowDialog;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.model.CustomOneModel;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.message.fragment.GiftDialogFragment;
import com.jingtaoi.yy.ui.mine.PersonHomeActivity;
import com.jingtaoi.yy.ui.other.SelectPhotoActivity;
import com.jingtaoi.yy.ui.room.TopupActivity;
import com.jingtaoi.yy.ui.room.VoiceActivity;
import com.jingtaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.NoticeLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.inputmore.InputMoreActionUnit;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageCustomHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.activitys.SelectPhotoDialog;
import io.reactivex.functions.Consumer;


/**
 * 聊天页面
 */
public class ChatActivity extends MyBaseActivity {

    String chatId;
    String userName;
    //    MyGiftDialog giftDialog;
    MyBottomPersonDialog myBottomPersonDialog;

    MyBottomShowDialog myBottomShowDialog;
    GiftDialogFragment giftDialogFragment;
    boolean isRoom;
    @BindView(R.id.chat_layout)
    ChatLayout chatLayout;
    private ChatInfo mChatInfo;
    private ArrayList<String> bottomList;//弹框显示内容

    @Override
    public void initData() {
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void initView() {
        chatId = getBundleString(Const.ShowIntent.ID);
        userName = getBundleString(Const.ShowIntent.NAME);
        isRoom = getBundleBoolean("isRoom", false);
        mChatInfo = (ChatInfo) getBundleSerializable(Const.ShowIntent.CHAT_INFO);

        setTitleText(userName);
        setLeftImg(getResources().getDrawable(R.drawable.icon_back1));
        setRightImg(getResources().getDrawable(R.drawable.icon_msg_more));

        setView();
//        getUserData();
        bottomList = new ArrayList<>();
        bottomList.add(getString(R.string.tv_ju_data));
        bottomList.add(getString(R.string.tv_hei_data));
        bottomList.add(getString(R.string.tv_cancel));



        tv_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMybottomDialog(bottomList);
            }
        });
    }


    private void showMybottomDialog(ArrayList<String> bottomList) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(ChatActivity.this, bottomList);
        myBottomShowDialog.show();
        BottomShowRecyclerAdapter adapter = myBottomShowDialog.getAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
                switch (position) {
                    case 0:
                        ArrayList<String> bottomListJu = new ArrayList<>();
                        bottomListJu.add(getString(R.string.tv_sensitivity_report));
                        bottomListJu.add(getString(R.string.tv_se_report));
                        bottomListJu.add(getString(R.string.tv_ad_report));
                        bottomListJu.add(getString(R.string.tv_at_report));
                        bottomListJu.add(getString(R.string.tv_cancel));
                        showMybottomDialogJu(bottomListJu);
                        break;
                    case 1:
                        showMyDialog(Const.IntShow.TWO, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                getHeiCall();
                            }
                        });

                        break;
                }
            }
        });
    }


    /**
     * 加入黑名单
     */
    private void getHeiCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", chatId);
        HttpManager.getInstance().post(Api.getAddUserblock, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("拉黑成功，您可以在设置中将Ta移除黑名单");
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void showMybottomDialogJu(ArrayList<String> bottomList) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(ChatActivity.this, bottomList);
        myBottomShowDialog.show();
        BottomShowRecyclerAdapter adapter = myBottomShowDialog.getAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
                switch (position) {
                    case 4:

                        break;
                    default:
                        String updateString = (String) adapter.getItem(position);
                        updateCall(updateString);
                        break;
                }
            }
        });
    }

    //举报人
    private void updateCall(String updateString) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", Const.IntShow.ONE);
        map.put("buid", chatId);
        map.put("content", updateString);
        HttpManager.getInstance().post(Api.ReportSave, map, new MyObserver(this) {
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


//    private void getUserData() {
//
//        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
//            @Override
//            public void onError(int i, String s) {
//                LogUtils.e(LogUtils.TAG, i + "  " + s);
//            }
//
//            @Override
//            public void onSuccess(TIMUserProfile timUserProfile) {
//                LogUtils.e(LogUtils.TAG, timUserProfile.getNickName() + " " + timUserProfile.getFaceUrl());
////                chatPanel.setSelfImg(timUserProfile.getFaceUrl());
//            }
//        });
//
//        //待获取用户资料的用户列表
//        List<String> users = new ArrayList<String>();
//        users.add(chatId);
//        //获取用户资料
//        TIMFriendshipManager.getInstance().getUsersProfile(users, false, new TIMValueCallBack<List<TIMUserProfile>>() {
//            @Override
//            public void onError(int code, String desc) {
//                //错误码 code 和错误描述 desc，可用于定位请求失败原因
//                //错误码 code 列表请参见错误码表
//                LogUtils.e(LogUtils.TAG, "getUsersProfile failed: " + code + " desc");
//            }
//
//            @Override
//            public void onSuccess(List<TIMUserProfile> result) {
//                LogUtils.e(LogUtils.TAG, "getUsersProfile succ");
//                for (TIMUserProfile res : result) {
//                    if (res.getIdentifier().equals(chatId)) {
//                        userName = res.getNickName();
//                        setTitleText(userName);
////                        chatPanel.setOtherImg(res.getFaceUrl());
//                    }
//                }
//            }
//        });
//    }

    private void setView() {

        // 单聊面板的默认 UI 和交互初始化
        chatLayout.initDefault();

        // 传入 ChatInfo 的实例，这个实例必须包含必要的聊天信息，一般从调用方传入
        chatLayout.setChatInfo(mChatInfo);

        //获取单聊面板的标题栏
        TitleBarLayout mTitleBar = chatLayout.getTitleBar();
        mTitleBar.setVisibility(View.GONE);

        // 从 ChatLayout 里获取 NoticeLayout
        NoticeLayout noticeLayout = chatLayout.getNoticeLayout();
        noticeLayout.setVisibility(View.GONE);


        // 从 ChatLayout 里获取 MessageLayout
        MessageLayout messageLayout = chatLayout.getMessageLayout();
        ////// 设置头像 //////
        messageLayout.setLeftChatContentFontColor(Color.parseColor("#000103"));
        messageLayout.setRightChatContentFontColor(Color.parseColor("#FFFFFF"));
        messageLayout.setChatContextFontSize(12);
        // 设置头像圆角，不设置则默认不做圆角处理
        messageLayout.setAvatarRadius(24);
        // 设置头像大小
        messageLayout.setAvatarSize(new int[]{48, 48});
        // 设置自己聊天气泡的背景
//        messageLayout.setRightBubble(getResources().getDrawable(R.drawable.frame2));
        // 设置朋友聊天气泡的背景
//        messageLayout.setLeftBubble(getResources().getDrawable(R.drawable.frame1));

        /**
         * 长按消息事件及头像点击事件
         */
        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                chatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                if (null == messageInfo || null == messageInfo.getTIMMessage()) {
                    return;
                }
                Bundle bundle = new Bundle();
                if (messageInfo.isSelf()) {
                    bundle.putInt(Const.ShowIntent.ID, userToken);
                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                } else {
                    bundle.putInt(Const.ShowIntent.ID, Integer.parseInt(messageInfo.getTIMMessage().getSender()));
                    ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
                    ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
                }
            }
        });

        messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());

        // 从 ChatLayout 里获取 InputLayout
        InputLayout inputLayout = chatLayout.getInputLayout();
        // 隐藏拍照并发送
        inputLayout.disableCaptureAction(true);
        // 隐藏发送文件
        inputLayout.disableSendFileAction(true);
        // 隐藏发送图片
        inputLayout.disableSendPhotoAction(true);
        // 隐藏摄像并发送
        inputLayout.disableVideoRecordAction(true);

        inputLayout.setBackgroundColor(getResources().getColor(R.color.white));

        // 定义一个动作单元
        InputMoreActionUnit unit = new InputMoreActionUnit();
        unit.setIconResId(R.drawable.image); // 设置单元的图标
        unit.setTitleId(R.string.action_nothing); // 设置单元的文字标题
        unit.setOnClickListener(new View.OnClickListener() { // 定义点击事件
            @Override
            public void onClick(View v) {
                openSelectPhoto();
            }
        });
        if (!isRoom) {
            // 把定义好的单元增加到更多面板
            inputLayout.addAction(unit);
        }else {
            inputLayout.disableMoreInput(true);
        }

        // 定义一个动作单元
        InputMoreActionUnit unit1 = new InputMoreActionUnit();
        unit1.setIconResId(R.drawable.gift); // 设置单元的图标
        unit1.setTitleId(R.string.action_nothing); // 设置单元的文字标题
        unit1.setOnClickListener(new View.OnClickListener() { // 定义点击事件
            @Override
            public void onClick(View v) {
                showGiftDialog();
            }
        });
        // 把定义好的单元增加到更多面板
//        inputLayout.addAction(unit1);


        //单聊组件的默认UI和交互初始化
//        chatPanel.initDefault();
//        chatPanel.setRoom(isRoom);
        /*
         * 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）来加载聊天消息。在上一章节SessionClickListener中回调函数的参数SessionInfo对象中持有每一会话的会话ID，所以在会话列表点击时都可传入会话ID。
         * 特殊的如果用户应用不具备类似会话列表相关的组件，则需自行实现逻辑获取会话ID传入。
         */
//        chatPanel.setBaseChatId(chatId);
        //获取单聊面板的标题栏
//        PageTitleBar chatTitleBar = chatPanel.getTitleBar();
//        chatTitleBar.setVisibility(View.GONE);

//        /**
//         *送礼物点击事件
//         */
//        chatPanel.setOnMyViewClicker(new ChatPanel.OnMyViewClicker() {
//            @Override
//            public void giftOnclicker() {
////                showMyGiftDialog(userToken, Integer.parseInt(chatId), Const.IntShow.THREE, userName);
//                showGiftDialog();
//            }
//
//            /**
//             * 头像点击事件
//             * @param messageInfo
//             */
//            @Override
//            public void onUserIconClicker(MessageInfo messageInfo) {
//                Bundle bundle = new Bundle();
//                if (messageInfo.isSelf()) {
//                    bundle.putInt(Const.ShowIntent.ID, userToken);
//                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
//                } else {
//                    bundle.putInt(Const.ShowIntent.ID, Integer.parseInt(messageInfo.getPeer()));
//                    ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
//                    ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
//                }
//            }
//
//            @Override
//            public void onShareClicker(String roomId) {
//                if (Const.RoomId.equals(roomId)) {
//                    showToast("您已在当前房间");
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Const.ShowIntent.ROOMID, roomId);
//                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
//                    ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                }
//            }
//
//            @Override
//            public void onCopyStr(String copyStr) {
//                MyUtils.getInstans().copyStr(ChatActivity.this, copyStr);
//                showToast("复制成功");
//            }
//        });
    }

    public class CustomMessageDraw implements IOnCustomMessageDrawListener {

        @Override
        public void onDraw(MessageCustomHolder parent, MessageInfo info) {
            // 获取到自定义消息的json数据
            TIMCustomElem elem = (TIMCustomElem) info.getTIMMessage().getElement(0);
            // 自定义的json数据，需要解析成bean实例
            try {
                JSONObject jsonObject = new JSONObject(new String(elem.getData(), "UTF-8"));
                int state = jsonObject.optInt("state");
                String showMsg = jsonObject.optString("showMsg");
                String showUrl = jsonObject.optString("showUrl");
                if (state == 1) { //分享消息
                    // 把自定义消息view添加到TUIKit内部的父容器里
                    View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_adapter_share, null, false);
                    parent.addMessageContentView(view);

                    final String roomId = jsonObject.getString("roomId");
                    TextView tv_user_msg = view.findViewById(R.id.tv_user_msg);
                    TextView tv_room_msg = view.findViewById(R.id.tv_room_msg);
                    ImageView iv_meg_data_gropu = view.findViewById(R.id.iv_meg_data_gropu);
                    LinearLayout ll_share_data_group = view.findViewById(R.id.ll_share_data_group);

                    Glide.with(TUIKit.getAppContext()).load(showUrl).into(iv_meg_data_gropu);
                    FaceManager.handlerEmojiText(tv_user_msg, showMsg);
                    if (info.isSelf()) {
                        tv_user_msg.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.white));
                        tv_room_msg.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.white));
                    } else {
                        tv_user_msg.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.black));
                        tv_room_msg.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.text_ff0));
                    }
                    ll_share_data_group.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Const.RoomId.equals(roomId)) {
                                showToast("您已在当前房间");
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                                ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                            }
                        }
                    });

                } else if (state == 2) { //礼物消息
                    // 把自定义消息view添加到TUIKit内部的父容器里
                    View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_adapter_image, null, false);
                    parent.addMessageContentView(view);

                    TextView tv_gift_data_group = view.findViewById(R.id.tv_gift_data_group);
                    ImageView iv_gift_data_gropu = view.findViewById(R.id.iv_gift_data_gropu);

                    Glide.with(TUIKit.getAppContext()).load(showUrl).into(iv_gift_data_gropu);
                    tv_gift_data_group.setText(showMsg);
                    if (info.isSelf()) {
                        tv_gift_data_group.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.white));
                    } else {
                        tv_gift_data_group.setTextColor(ContextCompat.getColor(ChatActivity.this, R.color.black));
                    }

                } else if (state == 3) {

                    //送礼\浪花私聊提示
                    // 把自定义消息view添加到TUIKit内部的父容器里
                    View view = LayoutInflater.from(ChatActivity.this).inflate(com.tencent.qcloud.tim.uikit.R.layout.chat_adapter_send_gift_tips, null, false);
                    parent.addMessageContentView(view);
                    TextView tvContent = view.findViewById(R.id.tv_content);
                    tvContent.setText(showMsg);
                }else {

                }
            } catch (Exception e) {
                LogUtils.e("新消息转换出错");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Const.RequestCode.SELECTPHOTO_CODE:
                    if (data != null) {
                        String filePath = data.getStringExtra(SelectPhotoDialog.DATA);
                        MessageInfo info = MessageInfoUtil.buildImageMessage(Uri.fromFile(new File(filePath)), true);
                        chatLayout.sendMessage(info, false);
//                        if (chatLayout.getInputLayout().getmMessageHandler() != null) {
//                            chatLayout.getInputLayout().getmMessageHandler().sendMessage(info);
//                            chatLayout.getInputLayout().hideSoftInput();
//                        }
                    }
                    break;
            }
        }
    }

    @SuppressLint("CheckResult")
    private void openSelectPhoto() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ActivityCollector.getActivityCollector()
                                    .toOtherActivity(SelectPhotoActivity.class, Const.RequestCode.SELECTPHOTO_CODE);
                        } else {
                            showToast("请在应用权限页面开启相机权限");
                        }
                    }
                });
    }

    private void showGiftDialog() {
        giftDialogFragment = new GiftDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.User.USER_TOKEN, userToken);
        bundle.putInt(Const.ShowIntent.OTHRE_ID, Integer.parseInt(chatId));
        bundle.putString(Const.ShowIntent.NAME, userName);
        giftDialogFragment.setArguments(bundle);
        giftDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        giftDialogFragment.show(getSupportFragmentManager(), "giftDialogFragment");

        giftDialogFragment.setSendGift(new GiftDialogFragment.SendGift() {
            @Override
            public void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold) {
                int userGold = (int) SharedPreferenceUtils.get(ChatActivity.this, Const.User.GOLD, 0);
                if (userGold < goodGold * num * sum) {
                    showMyDialog();
                } else {
                    getGiftCall(ids, gid, img, showImg, num, sum);
                }
            }

            @Override
            public void onUserClicker() {
                if (giftDialogFragment != null) {
                    giftDialogFragment.dismiss();
                    int sendId = giftDialogFragment.getOtherId();
                    showMyPseronDialog(userToken, sendId);
                }
            }
        });
    }


    /**
     * 礼物弹窗
     *
     * @param userId
     * @param otherId  //被赠送的用户id
     * @param type     //1送礼物  2pk送礼物  3私聊送礼物
     * @param userName 展示的名字，可为空
     */
//    private void showMyGiftDialog(int userId, int otherId, int type, String userName) {
//        if (giftDialog != null && giftDialog.isShowing()) {
//            giftDialog.dismiss();
//        }
//
//        giftDialog = new MyGiftDialog(ChatActivity.this, userId, otherId, type, userName, Const.IntShow.THREE);
//        giftDialog.show();
//        giftDialog.setDataShow(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int sendId = giftDialog.getOtherId();
//                if (giftDialog != null && giftDialog.isShowing()) {
//                    giftDialog.dismiss();
//                }
//                showMyPseronDialog(userId, sendId);
//            }
//        });
//        giftDialog.setSendGift(new MyGiftDialog.SendGift() {
//            @Override
//            public void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold) {
//                int userGold = (int) SharedPreferenceUtils.get(ChatActivity.this, Const.User.GOLD, 0);
//                if (userGold < goodGold * num * sum) {
//                    showMyDialog();
//                } else {
//                    getGiftCall(ids, gid, img, showImg, num, sum);
//                }
//            }
//        });
//    }

    MyDialog myDialog;


    private void showMyDialog(int showType, View.OnClickListener onClickListener) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(ChatActivity.this);
        myDialog.show();
        if (showType == 2) {
            myDialog.setHintText("加入黑名单后， 您将不再收到对方的消息。");
        }
        myDialog.setRightButton(onClickListener);
    }



    private void showMyDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(ChatActivity.this);
        myDialog.show();
        myDialog.setHintText(getString(R.string.hint_nogold_gift));
        myDialog.setRightText(getString(R.string.tv_topup_packet));
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
            }
        });
    }

    private void getGiftCall(String ids, int gid, String img, String showImg, int num, int sum) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("ids", ids);
        map.put("gid", gid);
        map.put("num", num);
        map.put("sum", sum);
        HttpManager.getInstance().post(Api.userSaveGift, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    sendMessageShow(num, img, showImg);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void sendMessageShow(int num, String img, String showImg) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                chatId);

        CustomOneModel customOneModel = new CustomOneModel();
        customOneModel.setShowMsg("×" + num);
        customOneModel.setShowImg(showImg);
        customOneModel.setShowUrl(img);
        customOneModel.setState(Const.IntShow.TWO);
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[礼物]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        String msgShow = JSON.toJSONString(customOneModel);
        //向 TIMMessage 中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgShow.getBytes());      //自定义 byte[]
        elem.setDesc("[礼物]"); //自定义描述信息
        //将 elem 添加到消息
        if (msg.addElement(elem) != 0) {
            LogUtils.d(LogUtils.TAG, "addElement failed");
            return;
        }

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTIMMessage(msg);
        messageInfo.setSelf(true);
        messageInfo.setMsgTime(System.currentTimeMillis());
        messageInfo.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        messageInfo.setFromUser(TIMManager.getInstance().getLoginUser());
        chatLayout.sendMessage(messageInfo, false);

    }

    /**
     * 个人资料
     *
     * @param userId  用户id
     * @param otherId 查询对象id
     */
    private void showMyPseronDialog(int userId, int otherId) {
        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
            myBottomPersonDialog.dismiss();
        }
        myBottomPersonDialog = new MyBottomPersonDialog(ChatActivity.this, userId, otherId);
        myBottomPersonDialog.show();

    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stopPlayRecord();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        //退出Activity时释放单聊相关资源
//        C2CChatManager.getInstance().destroyC2CChat();
        chatLayout.exitChat();
        super.onDestroy();
    }
}