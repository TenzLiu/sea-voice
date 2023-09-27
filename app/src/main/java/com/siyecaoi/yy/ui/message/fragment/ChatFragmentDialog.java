package com.siyecaoi.yy.ui.message.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.dialog.MyBottomPersonDialog;
import com.siyecaoi.yy.dialog.MyBottomShowDialog;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.model.CustomOneModel;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.message.ChatActivity;
import com.siyecaoi.yy.ui.room.TopupActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.qcloud.tim.uikit.TUIKit;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class ChatFragmentDialog extends DialogFragment {
    Unbinder unbinder;

    String chatId;
    String userName;
    //    MyGiftDialog giftDialog;
    MyBottomPersonDialog myBottomPersonDialog;

    MyBottomShowDialog myBottomShowDialog;
    GiftDialogFragment giftDialogFragment;
    boolean isRoom = true;
    private ChatInfo mChatInfo;
    private ArrayList<String> bottomList;//弹框显示内容
    protected int userToken;

    View rootView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
//    @BindView(R.id.chat_layout)
//    ChatLayout chatLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chat, container);
        unbinder = ButterKnife.bind(this, view);
        rootView = view.getRootView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        chatId = bundle.getString(Const.ShowIntent.ID);
        userName = bundle.getString(Const.ShowIntent.NAME);
        mChatInfo = (ChatInfo) bundle.getSerializable(Const.ShowIntent.CHAT_INFO);
        userToken = (int) SharedPreferenceUtils.get(getActivity(), Const.User.USER_TOKEN, 0);
        tvTitle.setText(userName);
//        setView();
    }

//    private void setView() {
//
//        // 单聊面板的默认 UI 和交互初始化
//        chatLayout.initDefault();
//
//        // 传入 ChatInfo 的实例，这个实例必须包含必要的聊天信息，一般从调用方传入
//        chatLayout.setChatInfo(mChatInfo);
//
//        //获取单聊面板的标题栏
//        TitleBarLayout mTitleBar = chatLayout.getTitleBar();
//        mTitleBar.setVisibility(View.GONE);
//
//        // 从 ChatLayout 里获取 NoticeLayout
//        NoticeLayout noticeLayout = chatLayout.getNoticeLayout();
//        noticeLayout.setVisibility(View.GONE);
//
//
//        // 从 ChatLayout 里获取 MessageLayout
//        MessageLayout messageLayout = chatLayout.getMessageLayout();
//        ////// 设置头像 //////
//        messageLayout.setLeftChatContentFontColor(Color.parseColor("#000103"));
//        messageLayout.setRightChatContentFontColor(Color.parseColor("#FFFFFF"));
//        messageLayout.setChatContextFontSize(12);
//        // 设置头像圆角，不设置则默认不做圆角处理
//        messageLayout.setAvatarRadius(24);
//        // 设置头像大小
//        messageLayout.setAvatarSize(new int[]{48, 48});
//        // 设置自己聊天气泡的背景
////        messageLayout.setRightBubble(getResources().getDrawable(R.drawable.frame2));
//        // 设置朋友聊天气泡的背景
////        messageLayout.setLeftBubble(getResources().getDrawable(R.drawable.frame1));
//
//        /**
//         * 长按消息事件及头像点击事件
//         */
//        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
//            @Override
//            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
//                //因为adapter中第一条为加载条目，位置需减1
//                chatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
//            }
//
//            @Override
//            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
//                if (null == messageInfo || null == messageInfo.getTIMMessage()) {
//                    return;
//                }
//                Bundle bundle = new Bundle();
//                if (messageInfo.isSelf()) {
//                    bundle.putInt(Const.ShowIntent.ID, userToken);
//                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
//                } else {
//                    bundle.putInt(Const.ShowIntent.ID, Integer.parseInt(messageInfo.getTIMMessage().getSender()));
//                    ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
//                    ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
//                }
//            }
//        });
//
//        messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());
//
//        // 从 ChatLayout 里获取 InputLayout
//        InputLayout inputLayout = chatLayout.getInputLayout();
//        // 隐藏拍照并发送
//        inputLayout.disableCaptureAction(true);
//        // 隐藏发送文件
//        inputLayout.disableSendFileAction(true);
//        // 隐藏发送图片
//        inputLayout.disableSendPhotoAction(true);
//        // 隐藏摄像并发送
//        inputLayout.disableVideoRecordAction(true);
//
//        inputLayout.setBackgroundColor(getResources().getColor(R.color.white));
//
//        // 定义一个动作单元
//        InputMoreActionUnit unit = new InputMoreActionUnit();
//        unit.setIconResId(R.drawable.image); // 设置单元的图标
//        unit.setTitleId(R.string.action_nothing); // 设置单元的文字标题
//        unit.setOnClickListener(new View.OnClickListener() { // 定义点击事件
//            @Override
//            public void onClick(View v) {
////                openSelectPhoto();
//            }
//        });
//        if (!isRoom) {
//            // 把定义好的单元增加到更多面板
//            inputLayout.addAction(unit);
//        }
//
//        // 定义一个动作单元
//        InputMoreActionUnit unit1 = new InputMoreActionUnit();
//        unit1.setIconResId(R.drawable.gift); // 设置单元的图标
//        unit1.setTitleId(R.string.action_nothing); // 设置单元的文字标题
//        unit1.setOnClickListener(new View.OnClickListener() { // 定义点击事件
//            @Override
//            public void onClick(View v) {
//                showGiftDialog();
//            }
//        });
//        // 把定义好的单元增加到更多面板
//        inputLayout.addAction(unit1);
//
//    }

    private void showGiftDialog() {
        giftDialogFragment = new GiftDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.User.USER_TOKEN, userToken);
        bundle.putInt(Const.ShowIntent.OTHRE_ID, Integer.parseInt(chatId));
        bundle.putString(Const.ShowIntent.NAME, userName);
        giftDialogFragment.setArguments(bundle);
        giftDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        giftDialogFragment.show(getChildFragmentManager(), "giftDialogFragment");

        giftDialogFragment.setSendGift(new GiftDialogFragment.SendGift() {
            @Override
            public void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold) {
                int userGold = (int) SharedPreferenceUtils.get(getActivity(), Const.User.GOLD, 0);
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
     * 个人资料
     *
     * @param userId  用户id
     * @param otherId 查询对象id
     */
    private void showMyPseronDialog(int userId, int otherId) {
        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
            myBottomPersonDialog.dismiss();
        }
        myBottomPersonDialog = new MyBottomPersonDialog(getActivity(), userId, otherId);
        myBottomPersonDialog.show();

    }

    MyDialog myDialog;

    private void showMyDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(getActivity());
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
        HttpManager.getInstance().post(Api.userSaveGift, map, new MyObserver(getActivity()) {
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
//        chatLayout.sendMessage(messageInfo, false);

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
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.chat_adapter_share, null, false);
                    parent.addMessageContentView(view);

                    final String roomId = jsonObject.getString("roomId");
                    TextView tv_user_msg = view.findViewById(R.id.tv_user_msg);
                    TextView tv_room_msg = view.findViewById(R.id.tv_room_msg);
                    ImageView iv_meg_data_gropu = view.findViewById(R.id.iv_meg_data_gropu);
                    LinearLayout ll_share_data_group = view.findViewById(R.id.ll_share_data_group);

                    Glide.with(TUIKit.getAppContext()).load(showUrl).into(iv_meg_data_gropu);
                    FaceManager.handlerEmojiText(tv_user_msg, showMsg);
                    if (info.isSelf()) {
                        tv_user_msg.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        tv_room_msg.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    } else {
                        tv_user_msg.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        tv_room_msg.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_ff0));
                    }
                    ll_share_data_group.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Const.RoomId.equals(roomId)) {
//                                showToast("您已在当前房间");
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
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.chat_adapter_image, null, false);
                    parent.addMessageContentView(view);

                    TextView tv_gift_data_group = view.findViewById(R.id.tv_gift_data_group);
                    ImageView iv_gift_data_gropu = view.findViewById(R.id.iv_gift_data_gropu);

                    Glide.with(TUIKit.getAppContext()).load(showUrl).into(iv_gift_data_gropu);
                    tv_gift_data_group.setText(showMsg);
                    if (info.isSelf()) {
                        tv_gift_data_group.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    } else {
                        tv_gift_data_group.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    }

                }else if (state == 3) {
                    //送礼\浪花私聊提示
                    // 把自定义消息view添加到TUIKit内部的父容器里
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.chat_adapter_send_gift_tips, null, false);
                    parent.addMessageContentView(view);
                    TextView tvContent = view.findViewById(R.id.tv_content);
                    tvContent.setText(showMsg);
                    if (info.isSelf()) {
                        tvContent.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    } else {
                        tvContent.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    }
                }else {

                }
            } catch (Exception e) {
                LogUtils.e("新消息转换出错");
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.view_message_d, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_message_d:
            case R.id.iv_back:
                dismissAllowingStateLoss();
                break;
        }
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        super.onResume();
    }
}
