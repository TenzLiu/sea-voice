package com.jingtaoi.yy.ui.room;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.opensource.svgaplayer.SVGACallback;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.adapter.FragPagerAdapter;
import com.jingtaoi.yy.agora.AGEventHandler;
import com.jingtaoi.yy.agora.YySignaling;
import com.jingtaoi.yy.base.MyApplication;
import com.jingtaoi.yy.base.MyBaseMVPActivity;
import com.jingtaoi.yy.bean.AllmsgBean;
import com.jingtaoi.yy.bean.AlltopicBean;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.GetAgoraTokenBean;
import com.jingtaoi.yy.bean.GiveGiftResultBean;
import com.jingtaoi.yy.bean.MicXgfjBean;
import com.jingtaoi.yy.bean.OnlineUserBean;
import com.jingtaoi.yy.bean.OpenPacketBean;
import com.jingtaoi.yy.bean.PkSetBean;
import com.jingtaoi.yy.bean.RoomCacheBean;
import com.jingtaoi.yy.bean.VoiceHomeBean;
import com.jingtaoi.yy.bean.VoiceMicBean;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.control.ChatMessage;
import com.jingtaoi.yy.control.ChatRoomMessage;
import com.jingtaoi.yy.dialog.DeepSeaFishingDialog;
import com.jingtaoi.yy.dialog.DingHaiFishingDialog;
import com.jingtaoi.yy.dialog.DonghaiPalaceFishingDialog;
import com.jingtaoi.yy.dialog.MessageDialogFragment;
import com.jingtaoi.yy.dialog.MyBottomPersonDialog;
import com.jingtaoi.yy.dialog.MyBottomWheatDialog;
import com.jingtaoi.yy.dialog.MyBottomauctionDialog;
import com.jingtaoi.yy.dialog.MyChestsOneDialog;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.dialog.MyExpressionDialog;
import com.jingtaoi.yy.dialog.MyGiftAllshowDialog;
import com.jingtaoi.yy.dialog.MyGiftDialog;
import com.jingtaoi.yy.dialog.MyGoldSendDialog;
import com.jingtaoi.yy.dialog.MyHintDialog;
import com.jingtaoi.yy.dialog.MyMusicDialog;
import com.jingtaoi.yy.dialog.MyOnlineUserDialog;
import com.jingtaoi.yy.dialog.MyPacketDialog;
import com.jingtaoi.yy.dialog.MyPkDialog;
import com.jingtaoi.yy.dialog.MyRankingDialog;
import com.jingtaoi.yy.dialog.MyRewardDialog;
import com.jingtaoi.yy.dialog.MyRoomPassDialog;
import com.jingtaoi.yy.dialog.MyTopicshowDialog;
import com.jingtaoi.yy.dialog.RoomRankDialogFragment;
import com.jingtaoi.yy.dialog.WinPrizeGiftAllDialog;
import com.jingtaoi.yy.model.AllMsgModel;
import com.jingtaoi.yy.model.CarShowMessageBean;
import com.jingtaoi.yy.model.ChatMessageBean;
import com.jingtaoi.yy.model.EmojiList;
import com.jingtaoi.yy.model.EmojiMessageBean;
import com.jingtaoi.yy.model.GetOutBean;
import com.jingtaoi.yy.model.GiftAllModel;
import com.jingtaoi.yy.model.GiftSendMessage;
import com.jingtaoi.yy.model.MessageBean;
import com.jingtaoi.yy.model.VoiceTypeModel;
import com.jingtaoi.yy.model.WinPrizeGiftModel;
import com.jingtaoi.yy.mvp.model.VoiceModel;
import com.jingtaoi.yy.mvp.presenter.VoicePresenter;
import com.jingtaoi.yy.mvp.view.VoiceView;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.message.ChatActivity;
import com.jingtaoi.yy.ui.message.OtherHomeActivity;
import com.jingtaoi.yy.ui.message.fragment.ChatFragmentDialog;
import com.jingtaoi.yy.ui.mine.PersonHomeActivity;
import com.jingtaoi.yy.ui.room.adapter.ChatRecyclerAdapter;
import com.jingtaoi.yy.ui.room.adapter.PlaceRecyclerAdapter;
import com.jingtaoi.yy.ui.room.dialog.OtherShowDialog;
import com.jingtaoi.yy.ui.room.dialog.SendRecordDialog;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.BroadcastManager;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.MessageUtils;
import com.jingtaoi.yy.utils.MyUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.jingtaoi.yy.utils.SvgaUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.activitys.BaseActivity;
import cn.sinata.xldutils.utils.StringUtils;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.jingtaoi.yy.utils.Const.isOpenMicrophone;
import static com.jingtaoi.yy.utils.Const.isOpenReceiver;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 语音房间页面
 * Created by Administrator on 2018/12/20.
 */

public class VoiceActivity extends MyBaseMVPActivity implements VoiceView,
        ConversationManagerKit.MessageUnreadWatcher {
    private boolean isVD = false;
    @BindView(R.id.iv_topname_voice)
    ImageView ivTopnameVoice;
    @BindView(R.id.iv_topname_headwear)
    SVGAImageView ivTopnameHW;
    @BindView(R.id.tv_leave_voice)
    TextView tvLeaveVoice;
    @BindView(R.id.rl_topimg_voice)
    RelativeLayout rlTopimgVoice;
    @BindView(R.id.tv_homename_voice)
    TextView tvHomenameVoice;
    @BindView(R.id.tv_homeid_voice)
    TextView tvHomeidVoice;
    @BindView(R.id.tv_homenum_voice)
    TextView tvHomenumVoice;
    @BindView(R.id.iv_homeset_voice)
    ImageView ivHomesetVoice;
    @BindView(R.id.tv_homecon_voice)
    TextView tvHomeconVoice;
    @BindView(R.id.iv_auction_voice)
    ImageView ivAuctionVoice;
    @BindView(R.id.tv_music_voice)
    TextView tvMusicVoice;//音乐按钮
    @BindView(R.id.tv_hometype_voice)
    TextView tvHometypeVoice;
    @BindView(R.id.tv_homedetails_voice)
    TextView tvHomedetailsVoice;
    @BindView(R.id.mRecyclerView_place_voice)
    RecyclerView mRecyclerViewPlaceVoice;
    @BindView(R.id.mRecyclerView_chat_voice)
    RecyclerView mRecyclerViewChatVoice;
    @BindView(R.id.iv_message_voice)
    ImageView ivMessageVoice;
    @BindView(R.id.iv_mic_voice)
    ImageView ivMicVoice;
    @BindView(R.id.iv_receiver_voice)
    ImageView ivReceiverVoice;
    @BindView(R.id.iv_express_voice)
    ImageView ivExpressVoice;
    @BindView(R.id.iv_envelope_voice)
    ImageView ivEnvelopeVoice;
    @BindView(R.id.iv_packet_voice)
    ImageView ivPacketVoice;
    @BindView(R.id.iv_gift_voice)
    ImageView ivGiftVoice;
    @BindView(R.id.edt_input_mychat)
    EditText edtInputMychat;
    @BindView(R.id.btn_send_mychat)
    Button btnSendMychat;
    @BindView(R.id.rl_chat_back)
    RelativeLayout rlChatBack;
    @BindView(R.id.tv_has_message_voice)
    TextView tvHasMessageVoice;
    @BindView(R.id.iv_topnameback_voice)
    ImageView ivTopnamebackVoice;
    @BindView(R.id.iv_input_back_voice)
    ImageView ivInputBackVoice;
    @BindView(R.id.iv_deepSeaFishing)
    ImageView ivDeepSeaFishing;
    @BindView(R.id.iv_dinghaiFishing)
    ImageView ivDinghaiFishing;
    @BindView(R.id.iv_donghaiFishing)
    ImageView ivDonghaiFishing;
    @BindView(R.id.tv_smallpk_show_voice)
    TextView tvSmallpkShowVoice;
    @BindView(R.id.tv_onename_smallpk_voice)
    TextView tvOnenameSmallpkVoice;
    @BindView(R.id.tv_twoname_smallpk_voice)
    TextView tvTwonameSmallpkVoice;
    @BindView(R.id.progress_smallpk_voice)
    ProgressBar progressSmallpkVoice;

    @BindView(R.id.ll_chat_voice)
    LinearLayout llChatVoice;
    boolean isCanOpen;//判断是否可以显示输入框
    /**
     * 判断是否设置输入框位置改变
     */
//    boolean isCanSetMargin;
    boolean isOpenChat;//判断输入框是否打开
    @BindView(R.id.tv_endtime_show_voice)
    TextView tvEndtimeShowVoice;
    @BindView(R.id.tv_onenumber_voice)
    TextView tvOnenumberVoice;
    @BindView(R.id.tv_twonumber_voice)
    TextView tvTwonumberVoice;
    @BindView(R.id.ll_bottom_voice)
    LinearLayout llBottomVoice;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.rl_smallpk_voice)
    RelativeLayout rlSmallpkVoice;
    @BindView(R.id.tv_showimg_voice)
    TextView tvShowimgVoice;
    @BindView(R.id.ll_smallpk_voice)
    LinearLayout llSmallpkVoice;
    @BindView(R.id.iv_voiceback_voice)
    ImageView ivVoicebackVoice;
    @BindView(R.id.iv_getpacket_voice)
    ImageView ivGetpacketVoice;
    @BindView(R.id.iv_music_voice)
    ImageView ivMusicVoice;
    @BindView(R.id.rl_music_voice)
    RelativeLayout rlMusicVoice;
    @BindView(R.id.rl_back_voice)
    RelativeLayout rlBackVoice;
    @BindView(R.id.iv_showimg_voice)
    ImageView ivShowimgVoice;
    @BindView(R.id.tv_access_voice)
    TextView tvAccessVoice;

    String chatInputShow;//用户输入的内容
    @BindView(R.id.iv_showsvga_voice)
    SVGAImageView ivShowsvgaVoice;
    @BindView(R.id.tv_giftpoint_voice)
    TextView tvGiftpointVoice;
    @BindView(R.id.tv_alltopic_voice)
    TextView tvAlltopicVoice;

    @BindView(R.id.alltopic_voice_ll)
    LinearLayout alltopic_voice_ll;

    @BindView(R.id.tongzhi_img)
    ImageView tongzhi_img;

    @BindView(R.id.tv_chattype_voice)
    TextView tvChattypeVoice;
    @BindView(R.id.tv_chat2_voice)
    TextView tvChat2Voice;
    @BindView(R.id.tv_chat1_voice)
    TextView tvChat1Voice;
    @BindView(R.id.back_iv)
    ImageView back_iv;
    @BindView(R.id.ll_chattype_voice)
    LinearLayout llChattypeVoice;

    @BindView(R.id.liang_iv)
    ImageView liang_iv;

    @BindView(R.id.ll_giftall_voice)
    LinearLayout ll_giftall_voice;

    /**
     * 麦上用户适配器102
     */
    PlaceRecyclerAdapter placeRecyclerAdapter;
    /**
     * 聊天列表适配器
     */
    ChatRecyclerAdapter chatRecyclerAdapter;


    private String roomId;//房间id
    private String roomImg;//房间房主头像
    /**
     * 房主id
     */
    private int roomUserId;
    /**
     * 房主名称
     */
    private String roomUserName;
    /**
     * 当前用户角色  1房主  2管理员  3普通用户
     */
    private int userRoomType;
    //    /**
//     * 是否开启听筒(扬声器) 已使用静态变量替换
//     */
////    boolean isOpenReceiver;
    Observable<Boolean> openReceiverObservable;
    Consumer<Boolean> receiverConsumer;
    /**
     * 是否开启麦克风
     */
//    boolean isOpenMicrophone;
    /**
     * 判断用户是否在麦上
     */
    boolean isMicShow;
    /**
     * 用户如果在麦位上，麦序
     */
    int userMicOne;
    /**
     * 是否开启录音权限
     */
    boolean isOpenRecord;

    /**
     * 是否开启公屏
     */
    boolean isOpenGp;

    /**
     * 判断该用户所在麦位是否禁麦
     */
    boolean isMicCan;

//    MyBottomShowDialog myBottomShowDialog;
//    private ArrayList<String> bottomList;//弹框显示内容

    private ArrayList<String> chatShowChat;//聊天内容

    private List<VoiceMicBean.DataBean> chatUserList;//麦上用户

    VoicePresenter voicePresenter;
    //消息回调
    private Consumer<String> consumer;
    Observable<String> observable;
    Disposable subscribe;

    VoiceHomeBean.DataBean.RoomBean roomBean;//房间信息

    MyOnlineUserDialog myOnlineUserDialog;//在线用户弹窗

    String roomTopic, topicCount;//房间话题及内容
    String roomPass;//房间密码
    MyTopicshowDialog myTopicshowDialog;
    MyRankingDialog myRankingDialog;
    MyBottomPersonDialog myBottomPersonDialog;

    MyBottomWheatDialog bottomWheatDialog;

    MyBottomauctionDialog bottomauctionDialog;

    MyRoomPassDialog roomPassDialog;

    MyDialog myDialog;

    MyGiftDialog giftDialog;
    FragPagerAdapter fragPagerAdapter;

    MyExpressionDialog myExpressionDialog;

    MyRewardDialog rewardDialog;

    MyPacketDialog packetDialog;

    MyMusicDialog musicDialog;

    CountDownTimer countDownTimer;

    MyPkDialog pkDialog;
    PkSetBean.DataBean pkData;
    int roomPid;//房间pk记录id

    MyChestsOneDialog chestsDialog;
    String userName;//用户昵称
    int goldNum;//财富等级

//    MyGiftShowDialog giftShowDialog;

    private boolean isHavePacket;//是否有红包

    private int packetNumber;//红包个数
    private int giftMinShow;//礼物显示的最小值

    MessageDialogFragment messageDialogFragment;
    ArrayList<ChatMessageBean.DataBean> accessList;//进房用户列表
    boolean accessRoomShow;//判断是否在显示用户进房通知

    ArrayList<GiftSendMessage.DataBean> giftList;//礼物展示列表
    int giftNumberShow;//判断当前显示的礼物

    List<String> noShowString;//敏感词汇

    List<VoiceTypeModel> typeList;
    List<VoiceTypeModel> typeOneList;
    Timer timerType;
    TimerTask taskType;

    private boolean isAllChat;//是否是全平台消息
    private boolean roll_bottom = false;

    ArrayList<GiftAllModel.DataBean> giftAllList = new ArrayList<>();
    private DeepSeaFishingDialog deepSeaFishingDialog;
    private DingHaiFishingDialog dingHaiFishingDialog;
    private DonghaiPalaceFishingDialog donghaiPalaceFishingDialog;


    @Override
    public void initData() {
        roomId = getBundleString(Const.ShowIntent.ROOMID);
        roll_bottom = getBundleBoolean(Const.ShowIntent.ROLL_BOTTOM, false);
//        uid = getBundleInt(Const.ShowIntent.ID, 0);
//        bottomList = new ArrayList<>();
        userName = (String) SharedPreferenceUtils.get(this, Const.User.NICKNAME, "");
        goldNum = (int) SharedPreferenceUtils.get(this, Const.User.GRADE_T, 0);

        typeList = new ArrayList<>();
        typeOneList = new ArrayList<>();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_voice);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        }
        reqVoice();

        initShow();

        initBroadCast();

        initCall();

        initTimer();

        edtInputMychat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
//        voicePresenter.getAgoraRtmToken(userToken);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mRecyclerViewChatVoice != null && chatRecyclerAdapter != null && chatRecyclerAdapter.getItemCount() > 0) {
            tvHasMessageVoice.setVisibility(View.INVISIBLE);
            mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
        }


    }

    private void initTimer() {
        timerType = new Timer();
        taskType = new TimerTask() {
            @SuppressLint("CheckResult")
            @Override
            public void run() {
                typeOneList.clear();
                for (VoiceTypeModel typeModel : typeList) {
                    if (typeModel.getTime() > 0) {
                        LogUtils.e("type" + typeModel.toString());
                        typeModel.setTime(typeModel.getTime() - 1);
                    } else if (typeModel.getTime() <= 0) {
                        typeModel.setTime(-1);
                        typeOneList.add(typeModel);
                        LogUtils.e("type" + typeModel.toString());
                        Observable
                                .just(typeModel)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<VoiceTypeModel>() {
                                    @Override
                                    public void accept(VoiceTypeModel voiceTypeModel) throws Exception {
                                        switch (voiceTypeModel.getType()) { //待处理消息类型
                                            case 1://1关注消息(显示关注消息)
                                                //判断用户是否关注房主
                                                voicePresenter.getUserAttention(userToken, roomBean.getUid());
                                                break;
                                            case 2://2 表情消息（关闭表情消息）
                                                if (voiceTypeModel.getPosition() == 8) {
                                                    if (tvShowimgVoice != null) {
                                                        tvShowimgVoice.setVisibility(View.GONE);
                                                    }
                                                    if (ivShowimgVoice != null) {
                                                        ivShowimgVoice.setVisibility(View.GONE);
                                                    }
                                                    if (ivShowsvgaVoice != null) {
                                                        ivShowsvgaVoice.setVisibility(View.GONE);
                                                    }
                                                } else {
                                                    VoiceMicBean.DataBean item = placeRecyclerAdapter.getItem(voiceTypeModel.getPosition());
                                                    if (item.getUserModel() != null) {
                                                        item.getUserModel().setShowImg(0);
                                                        item.getUserModel().setImgOver(false);
                                                    }
                                                    if (placeRecyclerAdapter.getItem(voiceTypeModel.getPosition()) != null) {
                                                        placeRecyclerAdapter.setData(voiceTypeModel.getPosition(), item);
                                                    }
                                                }
                                                break;
                                            case 3://3是否说话中（关闭说话中状态）
                                                if (voiceTypeModel.getPosition() == 8) {
                                                    setHomeAnimation(false);
                                                } else {
                                                    VoiceMicBean.DataBean item = placeRecyclerAdapter.getItem(voiceTypeModel.getPosition());
                                                    if (item.getUserModel() != null) {
                                                        item.getUserModel().setSpeak(false);
                                                    }
                                                    if (placeRecyclerAdapter.getItem(voiceTypeModel.getPosition()) != null) {
                                                        placeRecyclerAdapter.setData(voiceTypeModel.getPosition(), item);
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                });
                    }
                }
                typeList.removeAll(typeOneList);
            }
        };
        timerType.schedule(taskType, 1000, 1000);
    }

    /**
     * 添加数据类型并保证一种数据只出现一次
     *
     * @param typeModelOne
     */
    private void addTypeList(VoiceTypeModel typeModelOne) {
        for (VoiceTypeModel typeModel : typeList) {
            if (typeModel.equals(typeModelOne)) {
                typeModel.setTime(typeModelOne.getTime());
                return;
            }
        }
        typeList.add(typeModelOne);
    }

    private void initCall() {
//        voicePresenter.getCall(userToken, roomId);
        voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.ONE);
//        voicePresenter.getAllTopic(userToken);
        voicePresenter.getRoomCahce();
    }

    private long nextGoRoomTime;

    @Override
    protected void onResume() {
        super.onResume();
        nextGoRoomTime = System.currentTimeMillis();
        if (voicePresenter != null)
            voicePresenter.getChatShow(userToken, roomId);
    }

    //处理房主及麦上用户显示
    private void updateChatShow() {
        placeRecyclerAdapter.setNewData(chatUserList);
        updateUserData(chatUserList);
    }

    private void updateUserData(List<VoiceMicBean.DataBean> chatUserList) {
        //先将用户默认设置为不在麦上
        if (userRoomType != 1) {
            isMicShow = false;
        }
        userMicOne = -1;
        for (VoiceMicBean.DataBean dataBean : chatUserList) {
            userMicOne++;
            if (dataBean.getUserModel() != null) {
                if (dataBean.getUserModel().getId() == userToken) {
                    if (dataBean.getState() == 1) { //未禁麦
                        isMicCan = true;
                        setMicisOpen();
                    } else if (dataBean.getState() == 2) { //禁麦
                        isMicCan = false;
                        setMicisOpen();
                    }
                    userRoomType = dataBean.getUserModel().getType();
                    //如果在麦位则更新状态
                    isMicShow = true;
                    break;
                }
            }
        }
        //如果当前用户不在麦位，则主动下麦
        if (!isMicShow) {
            isOpenMicrophone = false;
            setUpdateMic(Const.IntShow.TWO);
            BroadcastManager.getInstance(this).sendBroadcast(Const.BroadCast.MIC_DOWN, true);

            if (musicDialog != null && musicDialog.isShowing()) { //隐藏音乐弹窗
                musicDialog.dismiss();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void reqVoice() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            isOpenRecord = true;
                        } else {
                            showToast("请在应用权限页面开启语音录制权限");
                            ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                        }
                    }
                });
    }

    private void initBroadCast() {
        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MUSIC_PLAY, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String filePath = intent.getStringExtra(Const.ShowIntent.DATA);
                MyApplication.getInstance().getWorkerThread().musicPlay(filePath);
                if (musicDialog != null) {
                    musicDialog.updateShow();
                }
                showMusicAnimation(true);
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MUSIC_PAUSE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MyApplication.getInstance().getWorkerThread().musicPause();
                if (musicDialog != null) {
                    musicDialog.updateShow();
                }
                showMusicAnimation(false);
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MUSIC_REPLAY, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MyApplication.getInstance().getWorkerThread().musicReplay();
                if (musicDialog != null) {
                    musicDialog.updateShow();
                }
                showMusicAnimation(true);
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.PACKET_OVER, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                packetNumber = intent.getIntExtra(Const.ShowIntent.DATA, 0);
                setPacketShow();
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.MSG_COUN, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String topicBean = intent.getStringExtra(Const.ShowIntent.DATA);
                AllmsgBean.DataEntity dataEntity = MyUtils.mGson.fromJson(topicBean, AllmsgBean.DataEntity.class);
                setCountMsg(dataEntity);
            }
        });
    }

    /**
     * 音乐动画
     */
    ObjectAnimator musicAnimator;
    long mCurrentPlayTime;//暂停位置

    private void showMusicAnimation(boolean isShow) {
        if (musicAnimator == null) {
            musicAnimator = ObjectAnimator.ofFloat(ivMusicVoice, "rotation", 0, 360);
            musicAnimator.setDuration(3000);
            musicAnimator.setRepeatCount(-1);
            //线性均匀改变
            musicAnimator.setInterpolator(new LinearInterpolator());
        }
        if (isShow) {
            musicAnimator.start();
            if (0 != mCurrentPlayTime) {
                musicAnimator.setCurrentPlayTime(mCurrentPlayTime);
            }
        } else {
            //记录暂停位置
            mCurrentPlayTime = musicAnimator.getCurrentPlayTime();
            musicAnimator.cancel();
        }
    }

    /**
     * 设置开启PK后显示
     *
     * @param pkShow
     */
    float xdown, ydown;
    int Xmove, Ymove;
    int kleft, ktop;

    @SuppressLint("SetTextI18n")
    private void setPkShow() {
        LogUtils.e("msg", "重新布局");
        if (rlSmallpkVoice.getVisibility() == View.GONE) {
            rlSmallpkVoice.setVisibility(View.VISIBLE);
        }

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/impact.ttf");
        tvSmallpkShowVoice.setTypeface(typeface);
//        String endTime = data.getTime();
        tvOnenameSmallpkVoice.setText(pkData.getUser().getName());
        tvTwonameSmallpkVoice.setText(pkData.getUser1().getName());
        progressSmallpkVoice.setMax(pkData.getUser().getNum() + pkData.getUser1().getNum());
        progressSmallpkVoice.setProgress(pkData.getUser().getNum());
        tvOnenumberVoice.setText(pkData.getUser().getNum() + "");
        tvTwonumberVoice.setText(pkData.getUser1().getNum() + "");
        long endTimeLong = pkData.getTime();
        long nowTime = System.currentTimeMillis();
        long endTimeShow = endTimeLong - nowTime;
        if (pkDialog != null && pkDialog.isShowing()) {
            pkDialog.updateData(pkData);
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (endTimeShow > 0) {
            tvEndtimeShowVoice.setText((endTimeShow / 1000) + "s");
            countDownTimer = new CountDownTimer(endTimeShow, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvEndtimeShowVoice.setText(millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    if (rlSmallpkVoice.getVisibility() == View.VISIBLE) {
                        rlSmallpkVoice.setVisibility(View.GONE);
                        showMyPkDialog(pkData);
                    }
                }
            };
            countDownTimer.start();
        } else {
            rlSmallpkVoice.setVisibility(View.GONE);
//            showToast("Pk已结束");
        }
    }

    private int lastX, lastY;
    int dx, dy;

    @SuppressLint("ClickableViewAccessibility")
    private void setRoomTouch() {
        rlSmallpkVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getRawX() - lastX;
                        dy = (int) event.getRawY() - lastY;

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                        int l = layoutParams.leftMargin + dx;
                        int t = layoutParams.topMargin + dy;

                        if (l < 0) { //处理按钮被移动到上下左右四个边缘时的情况，决定着按钮不会被移动到屏幕外边去
                            l = 0;
//                            r = rlBackVoice.getWidth() - v.getWidth();
                        }
                        if (t < 100) {
                            t = 100;
//                            b = rlBackVoice.getHeight() - v.getHeight();
                        }
                        if (l > rlBackVoice.getWidth() - v.getWidth()) {
                            l = rlBackVoice.getWidth() - v.getWidth();
                        }
                        if (t > rlBackVoice.getHeight() - v.getHeight() - 80) {
                            t = rlBackVoice.getHeight() - v.getHeight() - 80;
                        }

                        layoutParams.leftMargin = l;
                        layoutParams.topMargin = t;
                        v.setLayoutParams(layoutParams);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        v.postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                if (Math.abs(dx) > 2 || Math.abs(dy) > 2) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        rlSmallpkVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlSmallpkVoice.setVisibility(View.GONE);
                LogUtils.e("msg", "重新布局");
                showMyPkDialog(pkData);
            }
        });
    }

    private void initList() {
        if (!StringUtils.isEmpty(roomBean.getMark())) {
            MessageBean messageBean = new MessageBean(102, roomBean.getMark());
            String messageString = JSON.toJSONString(messageBean);
            chatShowChat.add(messageString);
        }
    }

    @SuppressLint("CheckResult")
    private void initVoice(String agoraRtmToken, String agoraRtcToken) {
        MyApplication.getInstance().getWorkerThread().eventHandler().addEventHandler(agEventHandler);
        switch (userRoomType) {
            case 1:
                //设置用户角色为主播
//                MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_BROADCASTER);
//                isOpenMicrophone = true;
//                isOpenReceiver = true;
                isMicShow = true;
                isMicCan = true;
                Observable.just(0).delay(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Integer>() {
                                       @Override
                                       public void accept(Integer aLong) throws Exception {
                                           setMicisOpen();//设置是否开启麦克风
                                       }
                                   }
                        );
//                ivMicVoice.setImageResource(R.drawable.selector_mic);
//                ivMicVoice.setSelected(true);
//                ivReceiverVoice.setSelected(true);
                ivExpressVoice.setVisibility(View.VISIBLE);
                setHomeAnimation(isOpenMicrophone);
                break;
            case 2:
                //设置用户角色为观众(默认为观众，无需设置)
//                MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_AUDIENCE);
//                isOpenMicrophone = false;
//                isOpenReceiver = true;
                isMicShow = false;
                ivMicVoice.setImageResource(R.drawable.bottom_mic_add);
//                ivReceiverVoice.setSelected(true);
                break;
            case 3:
                //设置用户角色为观众
//                MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_AUDIENCE);
//                isOpenMicrophone = false;
//                isOpenReceiver = true;
                isMicShow = false;
                ivMicVoice.setImageResource(R.drawable.bottom_mic_add);
//                ivReceiverVoice.setSelected(true);
                break;
        }
        //登录 Agora 信令系统
//        token = "00635e826bd63df4998823c36aa5c62bf8cIACSW8Yqg8IxG/XUQzwz+pM7NSzDoKPhW8RRYvJhMrk1B/9BTooAAAAAEACyCuRRl3lOXQEAAQCPeU5d";
        if (!Const.RoomId.equals(roomId)) {  //如果进入的房间和之前的房间不同，则加入频道
            if (!StringUtils.isEmpty(Const.RoomId)) {
                //如果不是同一频道，先退出之前频道
                voicePresenter.getChatRoom(userToken, Const.RoomId);
                MyApplication.getInstance().getWorkerThread().leaveChannel(Const.RoomId);
                Const.RoomId = "";
            }

            YySignaling.getInstans().loginRtmClient(String.valueOf(userToken), agoraRtmToken, roomId);
            //直播加入频道
            MyApplication.getInstance().getWorkerThread().joinChannel(roomId, userToken, agoraRtcToken);

            //储存房间信息
            Const.RoomName = roomBean.getRoomName();
            Const.RoomId = roomId;
            Const.RoomIdLiang = roomBean.getLiang();
            Const.RoomImg = roomImg;
            Const.RoomNum = roomBean.getLineNum() + "";
            isOpenReceiver = true;//进入房间开启扬声器

            //显示房间官方话语
            initList();
            if (!StringUtils.isEmpty(roomBean.getRoomHint())) {  //判空
                //添加房间欢迎语
                MessageBean messageBean = new MessageBean(103, roomBean.getRoomHint());
                String mesString = JSON.toJSONString(messageBean);
                if (!StringUtils.isEmpty(mesString)) {
                    chatShowChat.add(mesString);
//                    chatRecyclerAdapter.addData(mesString);
                }
            }

            if (!isVD) {
                //显示座驾信息
                setCarShow();
                //显示进房特效
                setGradeShow();
            }


        } else { //如果是同一个房间
            if (Const.MusicShow.isHave && Const.MusicShow.musicPlayState == Const.IntShow.TWO) { //如果音乐在播放中
                // add by 20201106 for 处理“播放音乐最小化再返回房间不能暂停再继续（而且没有声音）”
                if (!TextUtils.isEmpty(Const.MusicShow.musicPath)) {
                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.MUSIC_PLAY, Const.MusicShow.musicPath);
                }
                showMusicAnimation(true);
            }
        }

        //更新扬声器显示
        openReceiverObservable = Observable.just(isOpenReceiver);
        receiverConsumer = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                ivReceiverVoice.setSelected(aBoolean);
            }
        };

        openReceiverObservable.subscribe(receiverConsumer);


        //启用说话者音量提示3秒回调一次
        MyApplication.getInstance().getWorkerThread().getRtcEngine().enableAudioVolumeIndication(3000, 3, true);

        MessageUtils.getInstans().addChatShows(chatMessage);

        voicePresenter.getChatShow(userToken, roomId);//初始化用户信息后再调用麦上用户接口更新用户


        if (roll_bottom) {
            tvHasMessageVoice.setVisibility(View.INVISIBLE);
            mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
        }

    }

    private void setGradeShow() {
        if (goldNum > 0) {  //1级开启
            ChatMessageBean.DataBean dataBean = new ChatMessageBean.DataBean(goldNum, userName, userToken);
            ChatMessageBean chatMessageBean = new ChatMessageBean(117, dataBean);
            String carShowString = JSON.toJSONString(chatMessageBean);
            setSendMessage(carShowString, roomId, String.valueOf(userToken));
        }
    }

    /**
     * 判断该用户是否有座驾并显示座驾
     */
    private void setCarShow() {
        String carHeader = (String) SharedPreferenceUtils.get(this, Const.User.CAR_H, "");
        String carUrl = (String) SharedPreferenceUtils.get(this, Const.User.CAR, "");
        if (!StringUtils.isEmpty(carHeader) && !carHeader.equals("x")) {
            CarShowMessageBean.DataBean dataBean = new CarShowMessageBean.DataBean(userName, carHeader, carUrl, goldNum, userToken);
            CarShowMessageBean carShowMessageBean = new CarShowMessageBean();
            carShowMessageBean.setCode(116);
            carShowMessageBean.setData(dataBean);
            String carShowString = JSON.toJSONString(carShowMessageBean);
            setSendMessage(carShowString, roomId, String.valueOf(userToken));
        }
    }

    //频道消息回调(所有信息)
    ChatMessage chatMessage = new ChatMessage() {
        @SuppressLint("CheckResult")
        @Override
        public void setMessageShow(String channelID, String account, int uid, final String msg) {
//            chatRecyclerAdapter.addData(msg);
            if (channelID.equals(roomId)) {
                if (consumer == null) {
                    initConsumer();
                }
                observable = Observable.just(msg);
                subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer);
            }
        }

        /**
         * 频道信息改变
         * @param s  频道名
         * @param s1 属性名
         * @param s2 属性值
         * @param s3 变化类型:
         */
        @Override
        public void setChannelAttrUpdated(String s, String s1, String s2, String s3) {
            if (s.equals(roomId)) {
                if (s1.equals(Const.Agora.ATTR_MICS)) {
                    observable = Observable.just(s2);
                    subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    chatUserList = JSON.parseArray(s, VoiceMicBean.DataBean.class);
                                    updateChatShow();
                                }
                            });
                } else if (s1.equals(Const.Agora.ATTR_XGFJ)) {
                    observable = Observable.just(s2);
                    subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    roomBean = JSON.parseObject(s, VoiceHomeBean.DataBean.RoomBean.class);
                                    // add by 20201117 处理魅力值混乱的问题。添加判断只有送礼物增加魅力值时刷新大头麦位的魅力值，用户进出房间不刷新魅力值
                                    if (TextUtils.isEmpty(roomBean.getMark())) {
                                        setRoomShow();
                                    }
                                }
                            });
                } else if (s1.equals(Const.Agora.ATTR_PK) || s1.equals(Const.Agora.ATTR_PKTP)) {
                    observable = Observable.just(s2);
                    subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    pkData = JSON.parseObject(s, PkSetBean.DataBean.class);
                                    roomPid = pkData.getId();
                                    setPkShow();
                                }
                            });
                } else if (s1.equals(Const.Agora.ATTR_PACKET)) {
                    observable = Observable.just(s2);
                    subscribe = observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    LogUtils.e("msg", s);
                                    OpenPacketBean openPacketBean = JSON.parseObject(s, OpenPacketBean.class);
                                    setPacketList(openPacketBean);
                                }
                            });
                }

            }
        }
    };

    private void setPacketList(OpenPacketBean openPacketBean) {
        for (OpenPacketBean.RedNumBean openPacketNow :
                openPacketBean.getRedNum()) {
            if (openPacketNow.getUid() == userToken) {
                isHavePacket = true;
                packetNumber++;
                setPacketShow();
                showPacketDialog(openPacketNow.getId(), openPacketNow.getRedId(),
                        openPacketBean.getImg(), openPacketBean.getName(), openPacketBean.getRedNum().size());
                break;
            }
        }
    }

    /**
     * 回调消息处理
     */
    private void initConsumer() {
        consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (TextUtils.isEmpty(s)) {//不明消息屏蔽
                    return;
                }
                MessageBean messageBean = MyUtils.mGson.fromJson(s, MessageBean.class);
                if (messageBean.getData() == null || TextUtils.isEmpty(messageBean.getData().toString())) {//不明消息屏蔽
                    return;
                }
                if (messageBean.getCode() == 118) { //房间属性变化
                    voicePresenter.getChatShow(userToken, roomId);
                    return;
                } else if (messageBean.getCode() == 119) {   //全平台消息
                    LogUtils.i("平台消息");
                    AlltopicBean alltopicBean = JSON.parseObject(s, AlltopicBean.class);
//                    List<AlltopicBean.DataEntity> data = alltopicBean.getData();
                    topicList.addAll(alltopicBean.getData());
                    setTopicAnimation();

//                        if (data.size() > 0&& tvAlltopicVoice.getText().toString().length()>0)
//                        {
//                            tvAlltopicVoice.setText(data.get(0).getUserName() + "：" + data.get(0).getContent() + "  ");
//                            tvAlltopicVoice.requestFocus();
//                            tvAlltopicVoice.setVisibility(View.VISIBLE);
//                        }else {
//                            tvAlltopicVoice.setText(" ");
//                        }

//                    voicePresenter.getAllTopic(userToken);
                    return;
                } else if (messageBean.getCode() == 121) { //全服礼物通知
                    GiftAllModel giftAllModel = MyUtils.mGson.fromJson(s, GiftAllModel.class);
                    ArrayList<GiftAllModel.DataBean> ral = new ArrayList<>();
                    ral.add(giftAllModel.getData());
//                    showGiftAllDialog(ral);
                    LogUtils.i("全服礼物通知动画");
                    giftAllList.addAll(ral);
                    if (!isVD)
                        showGiftAllAnimation();
//                    showGiftAllDialog((ArrayList<GiftAllModel.DataBean>) giftAllModel.getData());
                    return;
                } else if (messageBean.getCode() == 122) { //全服活动中奖通知
//                    WinPrizeGiftModel giftAllModel = new Gson().fromJson(s, WinPrizeGiftModel.class);
//                    //     全服中奖超过1000才显示
//                    if (giftAllModel != null && giftAllModel.getData().getCost() >= 10000) {
//                        //                        showWinPrizeGiftAllDialog(giftAllModel);
//                        LogUtils.i("探险通知动画");
//                        prizeAllList.add(giftAllModel.getData());
//                        showPrizeGiftAllAnimation();
//                    }

                } else if (messageBean.getCode() == 888) { //更新 房间信息
//                    voicePresenter.getCall(userToken, roomId);
                } else if (messageBean.getCode() == 999) { //更新 麦位信息
//                    voicePresenter.getChatShow(userToken, roomId);
                }

                if((goldNum < 2 && messageBean.getCode() == 122) || messageBean.getCode() == 888 || messageBean.getCode() == 999){

                }else{
                    chatShowChat.add(s);
                }

                chatRecyclerAdapter.notifyDataSetChanged();
//                chatRecyclerAdapter.addData(s);
                if (chatRecyclerAdapter.getData().size() > 1000) {
                    chatRecyclerAdapter.remove(0);
                    chatShowChat.remove(0);
                }
                ChatMessageBean chatMessageBean;
                switch (messageBean.getCode()) {
                    case 101: //101 礼物消息
                        GiftSendMessage giftSendMessage = MyUtils.mGson.fromJson(s, GiftSendMessage.class);
                        GiftSendMessage.DataBean data = giftSendMessage.getData();
                        //判断是否屏蔽低价值礼物特效
                        if (roomBean.getIsState() == 2 || giftMinShow > data.getGoodGold() * data.getNum()) {
                            return;
                        }
                        if (!isVD)
                            setGiftDialogShow(giftSendMessage.getData());

//                        String sendIds = giftSendMessage.getData().getSendId();
//                        String[] allIds = sendIds.split(",");
//                        for (int i = 0; i < allIds.length; i++) {
//                            int nowId = Integer.parseInt(allIds[i]);
//                            if (nowId == userToken) {
//                                showMyGiftShowDialog(giftSendMessage.getData().getShowImg(), giftSendMessage.getData().getNum());
//                            }
//                        }
                        break;
                    case 105: //105 设置管理员(取消管理员)
                        chatMessageBean = MyUtils.mGson.fromJson(s, ChatMessageBean.class);
                        if (userToken == chatMessageBean.getData().getUid()) {
                            userRoomType = Const.IntShow.TWO;
                        }
                        break;
                    case 106: //106 等级提升
                        chatMessageBean = MyUtils.mGson.fromJson(s, ChatMessageBean.class);
                        if (chatMessageBean.getData().getUid() == userToken) { //如果是自己，则覆盖等级
                            goldNum = chatMessageBean.getData().getGrade();
                        }
                        SharedPreferenceUtils.put(VoiceActivity.this, Const.User.GRADE_T, goldNum);
                        break;
                    case 107:  //踢出房间
                        GetOutBean getOutBean = MyUtils.mGson.fromJson(s, GetOutBean.class);
                        if (getOutBean.getData().getBuid() == userToken) { //当前用户被踢出房间
                            showToast("您已被房主或管理员踢出房间");
                            voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
                        }
                        break;
                    case 108:  //108 表情消息
                        EmojiMessageBean emojiMessageBean = MyUtils.mGson.fromJson(s, EmojiMessageBean.class);
                        if (emojiMessageBean.getData().getUid() == roomUserId) { //房主显示
                            if (!isVD)
                                setEmojiShow(emojiMessageBean);
                        } else {
                            for (int i = 0; i < chatUserList.size(); i++) {
                                VoiceMicBean.DataBean usetListOne = chatUserList.get(i);
                                if (usetListOne.getUserModel() != null) {
                                    if (usetListOne.getUserModel().getId() == emojiMessageBean.getData().getUid()) {
                                        usetListOne.getUserModel().setShowImg(emojiMessageBean.getData().getEmojiCode());
                                        usetListOne.getUserModel().setImgOver(true);
                                        usetListOne.getUserModel().setNumberShow(emojiMessageBean.getData().getNumberShow());
                                        if (placeRecyclerAdapter != null) {
                                            if (placeRecyclerAdapter.getItem(i) != null) {
                                                if (!isVD)
                                                    placeRecyclerAdapter.setData(i, usetListOne);
                                            }
                                            if (!isVD)
                                                addTypeList(new VoiceTypeModel(2, i, 3));
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 110://上麦（下麦）
                        voicePresenter.getChatShow(userToken, roomId);
                        break;
                    case 112://被加入黑名单(调用该用户退出房间)
                        chatMessageBean = MyUtils.mGson.fromJson(s, ChatMessageBean.class);
                        int blackId = chatMessageBean.getData().getUid();
                        if (blackId == userToken) {
                            showToast("您已被房主或管理员加入黑名单");
                            voicePresenter.getChatRoom(blackId, roomId, Const.IntShow.TWO);
                        }
                        break;
                    case 115://送礼物全屏通知

                        break;
                    case 116://座驾消息
                        CarShowMessageBean carShowMessageBean = MyUtils.mGson.fromJson(s, CarShowMessageBean.class);
                        String carUrl = carShowMessageBean.getData().getCarUrl();
//                        showMyGiftShowDialog(carUrl, 0);
                        GiftSendMessage.DataBean dataBean = new GiftSendMessage.DataBean(carUrl, 0);
                        if (!isVD)
                            setGiftDialogShow(dataBean);
                        break;
                    case 117://进房通知消息
                        chatMessageBean = MyUtils.mGson.fromJson(s, ChatMessageBean.class);
                        if (accessList == null) {
                            accessList = new ArrayList<>();
                        }
                        accessList.add(chatMessageBean.getData());
                        if (!accessRoomShow) {
                            tvAccessVoice.setVisibility(View.VISIBLE);
                            chooseOneAccess = 0;
                            openAccessShow();
                        }
                        break;
                }

                if (mRecyclerViewChatVoice.canScrollVertically(1)) {
                    tvHasMessageVoice.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
                }
            }
        };
    }

    ArrayList<AlltopicBean.DataEntity> topicList = new ArrayList<>();
    Disposable subTopic;

    private void setTopicAnimation() {
        if (subTopic != null && !subTopic.isDisposed()) {
            return;
        }
        subTopic = Observable.interval(0, 10000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   topicShow();
                               }
                           }
                );
    }

    private void topicShow() {
        if (topicList.size() == 0) {
            alltopic_voice_ll.setVisibility(View.GONE);
            if (!subTopic.isDisposed()) {
                subTopic.dispose();
            }
            return;
        }

        AlltopicBean.DataEntity dataEntity = topicList.get(0);
        tvAlltopicVoice.setTag(dataEntity.getRid());
        tvAlltopicVoice.setText("" + dataEntity.getMessageShow() + "  ");
        ImageUtils.loadImage(tongzhi_img, dataEntity.getUserImg(), -1, R.drawable.default_head);

        tvAlltopicVoice.requestFocus();
        alltopic_voice_ll.setVisibility(View.VISIBLE);

        topicList.remove(dataEntity);

        Animation animation = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_right_enter10);
        alltopic_voice_ll.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束的时候触发
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation1 = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_left_out);
                alltopic_voice_ll.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    Disposable subPrize;
    ArrayList<WinPrizeGiftModel.DataBean> prizeAllList = new ArrayList<>();

    private void showPrizeGiftAllAnimation() {
        if (subPrize != null && !subPrize.isDisposed()) {
            return;
        }
        ll_giftall_voice.removeAllViews();
        ll_giftall_voice.setVisibility(View.GONE);
        subPrize = Observable.interval(0, 4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   loadPrizeView();
                               }
                           }
                );
    }

    private void loadPrizeView() {
        if (prizeAllList.size() == 0) {
            ll_giftall_voice.setVisibility(View.GONE);
            if (!subPrize.isDisposed()) {
                subPrize.dispose();
            }
            return;
        }
        ll_giftall_voice.removeAllViews();
        ll_giftall_voice.setVisibility(View.VISIBLE);

        WinPrizeGiftModel.DataBean giftAllModel = prizeAllList.get(0);
        Log.e("mmp", "渲染一次探险动画");
        View giftViewAnimator = View.inflate(this, R.layout.dialog_winprize_giftallshow, null);
        ImageView iv_one_giftall = giftViewAnimator.findViewById(R.id.iv_one_giftall);
        ImageView iv_gift_giftall = giftViewAnimator.findViewById(R.id.iv_gift_giftall);
        TextView tv_one_giftall = giftViewAnimator.findViewById(R.id.tv_one_giftall);
        TextView tv_number_giftall = giftViewAnimator.findViewById(R.id.tv_number_giftall);
        TextView gif_name = giftViewAnimator.findViewById(R.id.gif_name);
        TextView gif_jiazhi = giftViewAnimator.findViewById(R.id.gif_jiazhi);

        ImageUtils.loadImage(iv_one_giftall, giftAllModel.getUserImg(), -1, -1);
        tv_one_giftall.setText(giftAllModel.getNickname());
        ImageUtils.loadImage(iv_gift_giftall, giftAllModel.getGiftImg(), 0, -1);
        tv_number_giftall.setText("X" + giftAllModel.getGiftNum());
        gif_name.setText(giftAllModel.getGiftName());
        gif_jiazhi.setText("(" + giftAllModel.getCost() * giftAllModel.getGiftNum() + ")");


        prizeAllList.remove(giftAllModel);
        ll_giftall_voice.addView(giftViewAnimator);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_right_enter);
        giftViewAnimator.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束的时候触发
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation1 = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_left_out);
                giftViewAnimator.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    Disposable subGift;

    @SuppressLint("CheckResult")
    private void showGiftAllAnimation() {
        if (subGift != null && !subGift.isDisposed()) {
            return;
        }
        ll_giftall_voice.removeAllViews();
        ll_giftall_voice.setVisibility(View.GONE);
        subGift = Observable.interval(0, 4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   loadGiftView();
                               }
                           }
                );
    }

    private void loadGiftView() {
        if (giftAllList.size() == 0) {
            ll_giftall_voice.setVisibility(View.GONE);
            if (!subGift.isDisposed()) {
                subGift.dispose();
            }
            return;
        }
        ll_giftall_voice.removeAllViews();
        ll_giftall_voice.setVisibility(View.VISIBLE);
        Log.e("mmp", "渲染一次送礼动画");

        View giftViewAnimator = View.inflate(this, R.layout.dialog_giftallshow, null);
        ImageView iv_one_giftall = giftViewAnimator.findViewById(R.id.iv_one_giftall);
        TextView tv_one_giftall = giftViewAnimator.findViewById(R.id.tv_one_giftall);
        TextView tv_two_giftall = giftViewAnimator.findViewById(R.id.tv_two_giftall);
        ImageView iv_two_giftall = giftViewAnimator.findViewById(R.id.iv_two_giftall);
        ImageView iv_gift_giftall = giftViewAnimator.findViewById(R.id.iv_gift_giftall);
        TextView tv_number_giftall = giftViewAnimator.findViewById(R.id.tv_number_giftall);
        GiftAllModel.DataBean giftAllModel = giftAllList.get(0);
//        String roomNow = giftAllModel.getRid();
        ImageUtils.loadImage(iv_one_giftall, giftAllModel.getSimg(), -1, -1);
        tv_one_giftall.setText(giftAllModel.getSnickname());
        ImageUtils.loadImage(iv_two_giftall, giftAllModel.getBimg(), -1, -1);
        tv_two_giftall.setText(giftAllModel.getBnickname());
        ImageUtils.loadImage(iv_gift_giftall, giftAllModel.getImg(), 0, -1);
        tv_number_giftall.setText("X" + giftAllModel.getNum());

        giftAllList.remove(giftAllModel);
        ll_giftall_voice.addView(giftViewAnimator);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_right_enter);
        giftViewAnimator.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束的时候触发
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation1 = AnimationUtils.loadAnimation(VoiceActivity.this, R.anim.view_left_out);
                giftViewAnimator.startAnimation(animation1);
                Log.e("mmp", "飘进动画结束");
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e("mmp", "飘出动画开始");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e("mmp", "飘出动画结束");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    /**
     * 显示全服中奖通知
     */
    WinPrizeGiftAllDialog prizeGiftAllDialog;

    private void showWinPrizeGiftAllDialog(WinPrizeGiftModel giftAllModel) {
        if (prizeGiftAllDialog != null && prizeGiftAllDialog.isShowing()) {
            prizeGiftAllDialog.setGiftAllModel(giftAllModel);
        } else {
            if (prizeGiftAllDialog != null) {
                prizeGiftAllDialog.dismiss();
            }
            prizeGiftAllDialog = new WinPrizeGiftAllDialog(this, giftAllModel);
            prizeGiftAllDialog.show();
            prizeGiftAllDialog.getButton().setOnClickListener(v -> {
                toOtherRoom(myGiftAllshowDialog.getRoomId());
            });
        }
    }

    /**
     * 表情显示
     *
     * @param
     */
    private void setEmojiShow(EmojiMessageBean emojiMessageBean) {
        int emojiCode = emojiMessageBean.getData().getEmojiCode();
        int numberShow = emojiMessageBean.getData().getNumberShow();
        AnimationDrawable animationDrawable;
        if (emojiCode >= 128604) {
            ivShowsvgaVoice.setVisibility(View.VISIBLE);
//            SVGAParser parser = new SVGAParser(mContext);
//            parser.decodeFromAssets(ImageShowUtils.getInstans().getAssetsName(emojiCode), new SVGAParser.ParseCompletion() {
//                @Override
//                public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
//                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
//                    ivShowsvgaVoice.setImageDrawable(drawable);
//                    ivShowsvgaVoice.startAnimation();
//                }
//
//                @Override
//                public void onError() {
//                    ivShowsvgaVoice.setVisibility(View.GONE);
//                    LogUtils.e("svga", "图片加载出错");
//                }
//            });
            ivShowimgVoice.setVisibility(View.VISIBLE);
            ImageUtils.loadGif(ivShowsvgaVoice, ImageShowUtils.getInstans().getResId(emojiCode, numberShow), this);

        } else if (emojiCode >= 128564) { //gif动画
            ivShowimgVoice.setVisibility(View.VISIBLE);
            ImageUtils.loadImage(ivShowimgVoice, ImageShowUtils.getInstans().getResId(emojiCode, numberShow), 0, -1);
        } else if (emojiCode >= 128552) {
            ivShowimgVoice.setVisibility(View.VISIBLE);
            if (emojiCode == 128552) { //排麦序
                ivShowimgVoice.setImageResource(R.drawable.pai_voice);
                // 1. 设置动画
                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                int maiDrawable = getMaiDrawable(emojiMessageBean.getData().getNumberShow());
                if (maiDrawable != 0) {
                    animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                }
                // 2. 获取动画对象
                animationDrawable.start();
            } else if (emojiCode == 128553) { //爆灯
                ivShowimgVoice.setImageResource(R.drawable.deng_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                animationDrawable.start();
            } else if (emojiCode == 128554) { //举手
                ivShowimgVoice.setImageResource(R.drawable.hand_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                animationDrawable.start();
            } else if (emojiCode == 128555) { //石头剪刀布
                ivShowimgVoice.setImageResource(R.drawable.cai_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, numberShow);
                if (maiDrawable != 0) {
                    animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                }
                animationDrawable.start();
            } else if (emojiCode == 128562) { //骰子
                ivShowimgVoice.setImageResource(R.drawable.zhi_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, numberShow);
                if (maiDrawable != 0) {
                    animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                }
                animationDrawable.start();
            } else if (emojiCode == 128563) { //硬币
                ivShowimgVoice.setImageResource(R.drawable.yingbi_voice);

                animationDrawable = (AnimationDrawable) ivShowimgVoice.getDrawable();
                int maiDrawable = ImageShowUtils.getInstans().getResId(emojiCode, numberShow);
                if (maiDrawable != 0) {
                    animationDrawable.addFrame(Objects.requireNonNull(ContextCompat.getDrawable(mContext, maiDrawable)), 200);
                }
                animationDrawable.start();
            } else {
                ivShowimgVoice.setImageResource(ImageShowUtils.getInstans().getResId(emojiCode, numberShow));
            }
        } else {
            tvShowimgVoice.setText(new String(Character.toChars(emojiMessageBean.getData().getEmojiCode())));
            tvShowimgVoice.setVisibility(View.VISIBLE);
        }
        addTypeList(new VoiceTypeModel(2, 8, 3));
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                observable = Observable.just("0");
//                subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                if (tvShowimgVoice != null) {
//                                    tvShowimgVoice.setVisibility(View.GONE);
//                                }
//                                if (ivShowimgVoice != null) {
//                                    ivShowimgVoice.setVisibility(View.GONE);
//                                }
//                            }
//                        });
//            }
//        };
//        timer.schedule(timerTask, 3000);
    }

    /**
     * 礼物或座驾展示
     *
     * @param dataBean
     */
    private void setGiftDialogShow(GiftSendMessage.DataBean dataBean) {
        if (TextUtils.isEmpty(dataBean.getShowImg())) {
            return;
        }
        if (giftList == null) {
            giftList = new ArrayList<>();
        }
        giftList.add(dataBean);
        initShowGift(giftList.get(giftNumberShow).getNum(), giftList.get(giftNumberShow).getShowImg());
    }

    /**
     * 进房通知动画展示
     */
    int chooseOneAccess;
    SpannableStringBuilder stringBuilder;

    private void openAccessShow() {
        if (chooseOneAccess < accessList.size()) {
            ChatMessageBean.DataBean dataBean = accessList.get(chooseOneAccess);
            accessRoomShow = true;
            chooseOneAccess++;
            stringBuilder = new SpannableStringBuilder("欢迎 ");
//            stringBuilder.append("img");
//            int resShowid = ImageShowUtils.getGrade(dataBean.getGrade());
//            ImageSpan imageSpan = new ImageSpan(mContext, resShowid);
//            stringBuilder.setSpan(imageSpan, stringBuilder.length() - 3, stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            stringBuilder.append(" ");
            String accessName = dataBean.getName();
            stringBuilder.append(accessName);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.FFE795));
            stringBuilder.setSpan(colorSpan, stringBuilder.length() - accessName.length(),
                    stringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvAccessVoice.setText(stringBuilder);
            tvAccessVoice.requestFocus();

            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(VoiceActivity.this, R.animator.view_access_enter);
            animatorSet.setTarget(tvAccessVoice);
            animatorSet.setInterpolator(new LinearInterpolator());
            animatorSet.start();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    openAccessShow();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            accessList.clear();
            accessRoomShow = false;
            chooseOneAccess = 0;
            tvAccessVoice.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取排麦结果图片
     *
     * @param nuberShow
     * @return
     */
    private int getMaiDrawable(int nuberShow) {
        switch (nuberShow) {
            case 1:
                return R.drawable.s_number_1;
            case 2:
                return R.drawable.s_number_2;
            case 3:
                return R.drawable.s_number_3;
            case 4:
                return R.drawable.s_number_4;
            case 5:
                return R.drawable.s_number_5;
            case 6:
                return R.drawable.s_number_6;
            case 7:
                return R.drawable.s_number_7;
            case 8:
                return R.drawable.s_number_8;
        }
        return 0;
    }

    @BindView(R.id.lay_SVGAImageView_gift)
    RelativeLayout laySVGAImageView_gift;
    //    @BindView(R.id.mSVGAImageView_gift)
    SVGAImageView mSVGAImageViewGift;
    @BindView(R.id.tv_show_gift)
    TextView tvShowGift;
    @BindView(R.id.lay_gift_svga)
    LinearLayout lay_gift_svga;
    @BindView(R.id.iv_show_gift)
    ImageView ivShowGift;
    @BindView(R.id.iv_show_png)
    ImageView ivShowPng;

    private Timer svgaTimer;
    private SVGADrawable svgaDrawable;

    /**
     * 动图效果弹窗
     *
     * @param imgShow
     * @param number
     */
    private void initShowGift(int number, String imgShow) {
        if (laySVGAImageView_gift.getVisibility() == View.VISIBLE) {
            return;
        }
        if (mSVGAImageViewGift != null && mSVGAImageViewGift.isAnimating()) {
            return;
        }
        if (ivShowGift == null || ivShowGift.getVisibility() == View.VISIBLE) {
            return;
        }
        laySVGAImageView_gift.setVisibility(View.VISIBLE);
        if (number != 0) {
            tvShowGift.setText("X" + number);
        } else {
            tvShowGift.setText("");
        }
        if (imgShow.endsWith(".svga")) {
            if (svgaTimer != null) {
                svgaTimer.cancel();
            }
            ImageUtils.loadImage(ivShowGift, R.drawable.trans_bg, 0, -1);
            ivShowGift.setVisibility(View.GONE);
            ImageUtils.loadImage(ivShowPng, R.drawable.trans_bg, 0, -1);
            ivShowPng.setVisibility(View.GONE);
            lay_gift_svga.setVisibility(View.VISIBLE);

            mSVGAImageViewGift = new SVGAImageView(VoiceActivity.this);
            mSVGAImageViewGift.setLoops(1);
            mSVGAImageViewGift.setClearsAfterStop(true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mSVGAImageViewGift.setLayoutParams(params);
            lay_gift_svga.addView(mSVGAImageViewGift);
            mSVGAImageViewGift.setCallback(new SVGACallback() {
                @Override
                public void onPause() {
                    LogUtils.e("svga", "onPause");
                }

                @Override
                public void onFinished() {
                    LogUtils.e("msg", "svga完成");
                    handler.sendEmptyMessageDelayed(101, 1000);
                }

                @Override
                public void onRepeat() {

                }

                @Override
                public void onStep(int i, double v) {

                }
            });

            SvgaUtils.playSvgaFromUrl(imgShow, mSVGAImageViewGift, new SvgaUtils.SvgaDecodeListener() {
                @Override
                public void onError() {
                    LogUtils.e("svga", "图片加载出错");
                    handler.sendEmptyMessage(101);
                }
            });
        } else if (imgShow.endsWith(".gif")) {
            lay_gift_svga.removeAllViews();
            lay_gift_svga.setVisibility(View.GONE);
            ImageUtils.loadImage(ivShowPng, R.drawable.trans_bg, 0, -1);
            ivShowPng.setVisibility(View.GONE);
            ivShowGift.setVisibility(View.VISIBLE);
            ImageUtils.loadEffect(this, ivShowGift, imgShow, 3, new ImageUtils.LoadEffectCallback() {
                @Override
                public void onAnimationEnd() {
                    handler.sendEmptyMessage(101);
                }
            });
        } else {
            lay_gift_svga.removeAllViews();
            lay_gift_svga.setVisibility(View.GONE);
            ImageUtils.loadImage(ivShowGift, R.drawable.trans_bg, 0, -1);
            ivShowGift.setVisibility(View.GONE);
            ivShowPng.setVisibility(View.VISIBLE);
            ImageUtils.loadImage(ivShowPng, imgShow, 0, -1);

            if (svgaTimer != null) {
                svgaTimer.cancel();
            }
            svgaTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(101);
                }
            };
            svgaTimer.schedule(timerTask, 2000);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:

                    if (isFinishing()) return;

                    clearGiftShow(true);

                    if (giftNumberShow < giftList.size() - 1) {
                        handler.sendEmptyMessageDelayed(102, 1000);
                    } else {
                        giftNumberShow = 0;
                        giftList.clear();
                    }
                    break;
                case 102:
                    giftNumberShow++;
                    initShowGift(giftList.get(giftNumberShow).getNum(), giftList.get(giftNumberShow).getShowImg());
                    break;
            }
        }
    };

    /**
     * 清理礼物动画
     *
     * @param isContinue
     */
    private void clearGiftShow(boolean isContinue) {
        if (mSVGAImageViewGift != null) {
            mSVGAImageViewGift.stopAnimation();
            mSVGAImageViewGift.clearAnimation();
        }
        if (lay_gift_svga != null) {
            lay_gift_svga.removeAllViews();
            lay_gift_svga.setVisibility(View.GONE);
        }
        if (ivShowGift != null) {
            ImageUtils.loadImage(ivShowGift, R.drawable.trans_bg, 0, -1);
            ivShowGift.setVisibility(View.GONE);
        }
        if (ivShowPng != null) {
            ImageUtils.loadImage(ivShowPng, R.drawable.trans_bg, 0, -1);
            ivShowPng.setVisibility(View.GONE);
        }
        laySVGAImageView_gift.setVisibility(View.GONE);

        if (svgaTimer != null) {
            svgaTimer.cancel();
        }
        svgaDrawable = null;
        mSVGAImageViewGift = null;

        if (!isContinue) {
            giftNumberShow = 0;
            if (giftList != null) {
                giftList.clear();
            }
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }
    }

    private void initShow() {

        MyUtils.getInstans().keepScreenLongLight(this, true);
        showHeader(false);
        showTitle(false);
        setBlcakShow(false);
//        isCanSetMargin = true;

        voicePresenter = new VoicePresenter(this, new VoiceModel(this));
        //添加到manner里面
        addToPresenterManager(voicePresenter);

        if (Const.chatShow == null) {
            chatShowChat = new ArrayList<>();
        } else if (Const.RoomId.equals(String.valueOf(roomId))) {//如果有历史聊天数据则复制给当前页面
            chatShowChat = Const.chatShow;
        } else {
            chatShowChat = new ArrayList<>();
            Const.chatShow.clear();
        }


        setPlaceRecycler();
        setChatRecycler();
        setIsOpen();
//        setShow();

        setRoomTouch();
        setMsgShow();
        //判断是否有未领取红包
        packetNumber = Const.packetNumber;
        if (packetNumber > 0) {
            isHavePacket = true;
        }
        setPacketShow();


    }


    private void setMsgShow() {
        // 未读消息监视器
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
    }


    @SuppressLint("SetTextI18n")
    private void setShow(VoiceUserBean.DataBean dataBean) {
        roomImg = dataBean.getImg();
        roomUserId = dataBean.getId();
        roomUserName = dataBean.getName();
        ImageUtils.loadImage(ivTopnameVoice, roomImg, -1, R.drawable.default_head);
        if (!TextUtils.isEmpty(dataBean.getUserTh()) && dataBean.getUserTh().endsWith(".svga")) {
            ivTopnameHW.setVisibility(View.VISIBLE);
//            ImageUtils.loadUri(ivTopnameHW, dataBean.getUserTh());
            SvgaUtils.playSvgaFromUrl(dataBean.getUserTh(), ivTopnameHW, new SvgaUtils.SvgaDecodeListener() {
                @Override
                public void onError() {
                    LogUtils.e("svga", "图片加载出错");
                    ivTopnameHW.setVisibility(View.GONE);
                }
            });
        } else {
//            ivTopnameHW.setVisibility(View.GONE);
        }
    }

    /**
     * 房间显示设置
     */
    private void setRoomShow() {

        tvHomenameVoice.setText(roomBean.getName());
        if (StringUtils.isEmpty(roomBean.getLiang())) { //是否展示靓号
            tvHomeidVoice.setText("ID:" + roomId);
        } else {
            tvHomeidVoice.setText("ID:" + roomBean.getLiang());
        }


        if (TextUtils.isEmpty(roomBean.getLiang())) {
            liang_iv.setVisibility(View.GONE);
        } else {
            liang_iv.setVisibility(View.VISIBLE);
        }

        tvHomenumVoice.setText("在线：" + roomBean.getLineNum() + "人 >");//在线人数
        tvHometypeVoice.setText(roomBean.getRoomLabel());//房间标签

        if (roomBean.getStatus() == 2) {  //房间已锁定(直接退出房间)
            showToast("该房间已锁定");
            //设置没有音乐
            Const.MusicShow.isHave = false;
//            Const.RoomId = "";
            packetNumber = 0;//退出房间红包个数清零
            isOpenMicrophone = false;
            isOpenReceiver = false;
            //停止音乐
            MyApplication.getInstance().getWorkerThread().musicStop();
            //退出频道（直播）
            MyApplication.getInstance().getWorkerThread().leaveChannel(roomId);
            Const.RoomId = "";
            //退出频道（信令）
            YySignaling.getInstans().leaveChannel();
            ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
            return;
        }

        if (roomBean.getIsfz() == 1) {
            tvLeaveVoice.setVisibility(View.INVISIBLE);
        } else if (roomBean.getIsfz() == 2) {
            tvLeaveVoice.setVisibility(View.VISIBLE);
        }

        roomTopic = roomBean.getRoomTopic();
        topicCount = roomBean.getRoomCount();
        tvHomedetailsVoice.setText(roomBean.getRoomName());//房间话题

        tvGiftpointVoice.setText(roomBean.getRoomNum() + "");//房主礼物值

        int roomState = roomBean.getState();
        if (roomState == 1) {
//            ivPacketVoice.setVisibility(View.GONE);
        } else if (roomState == 2) {
//            ivPacketVoice.setVisibility(View.VISIBLE);
            String roomBackImg = roomBean.getBjImg();
            if (!StringUtils.isEmpty(roomBackImg)) {
                ImageUtils.loadImage(ivVoicebackVoice, roomBackImg, 0, -1);//背景图片

            }
        }

        if (userRoomType == 1) { //是房主显示音乐
            rlMusicVoice.setVisibility(View.VISIBLE);
        }

        if (roomBean.getIsJp() == 1) {
            ivAuctionVoice.setVisibility(View.GONE);
        } else if (roomBean.getIsJp() == 2) { //开启竞拍
            ivAuctionVoice.setVisibility(View.VISIBLE);
        }

//        if (roomBean.getIsPk()==2){//判断是否开启pk
//
//        }

        if (roomBean.getIsGp() == 1) {
            isOpenGp = true;
            ivMessageVoice.setImageResource(R.drawable.bottom_message);
        } else if (roomBean.getIsGp() == 2) {
            isOpenGp = false;
            ivMessageVoice.setImageResource(R.drawable.bottom_message_close);
        }

//        if (roomBean.getIsPk() == Const.IntShow.TWO && roomBean.getIsPkState() == Const.IntShow.TWO) {
//            voicePresenter.pkData(roomPid);
//        }
    }

    /**
     * 密码弹窗
     */
    private void showMyPasswordDialog(String agoraRtmToken, String agoraRtcToken) {
        if (roomPassDialog != null && roomPassDialog.isShowing()) {
            roomPassDialog.dismiss();
        }
        roomPassDialog = new MyRoomPassDialog(VoiceActivity.this);
        roomPassDialog.show();
        roomPassDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPass = roomPassDialog.getPassword();
                if (inputPass.equals(roomPass)) { //密码输入正确，进入房间
                    roomPassDialog.dismiss();
//                    voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.ONE);
                    openRoomAnimation(agoraRtmToken, agoraRtcToken);
                } else {
                    showToast("密码输入错误");
                }
            }
        });
        roomPassDialog.setLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomPassDialog.dismiss();
                voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
            }
        });
    }


    AGEventHandler agEventHandler = new AGEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            LogUtils.e(TAG, channel + "onJoinChannelSuccess" + uid + "  " + elapsed);
//            if (consumer == null || subscribe.isDisposed()) {
//                consumer = null;
//                initConsumer();
//            }
//            observable = Observable.just(uid + "加入频道");
//            subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(consumer);
        }

        @Override
        public void onTokenPrivilegeWillExpire(String rtcToken) {
            voicePresenter.getAgoraRtcToken(userToken, Const.RoomId);
        }

        @Override
        public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
            LogUtils.e(TAG, "离开频道回调");
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            LogUtils.e(TAG, "onUserOffline" + uid + "  " + reason);
//            if (consumer == null || subscribe.isDisposed()) {
//                consumer = null;
//                initConsumer();
//            }
//            String msgShow = null;
//            if (reason == 0) {
//                msgShow = uid + "离开频道";
//            } else if (reason == 2) {
//                msgShow = uid + "下麦成功";
//            }
//            observable = Observable.just(msgShow);
//            subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(consumer);

//            if (reason == USER_OFFLINE_DROPPED || reason == USER_OFFLINE_QUIT) { //因过长时间收不到对方数据包，超时掉线
//                voicePresenter.getChatRoom(uid, roomId, Const.IntShow.TWO);
//            }
        }

        @Override
        public void onExtraCallback(final int type, final Object... data) {
//            LogUtils.d(TAG, "onExtraCallback" + type + "  " + data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        return;
                    }
                    doHandleExtraCallback(type, data);
                }
            });
        }
    };


    private void doHandleExtraCallback(int type, Object... data) {
        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_MIC://true：麦克风已启用* false：麦克风已禁用

                break;
            case AGEventHandler.EVENT_TYPE_ON_USER_JOIN://远端用户/主播加入当前频道回调。
                LogUtils.e("远端用户/主播加入当前频道回调");
                break;
            case AGEventHandler.EVENT_TYPE_USERMUTEAUDIO://远端用户静音回调

                break;
            case AGEventHandler.EVENT_TYPE_VOLUMEINDICATION://提示频道内谁正在说话以及说话者音量的回调
                IRtcEngineEventHandler.AudioVolumeInfo[] speakers = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];
                showMicPersonAnimation(speakers);
                break;
            case AGEventHandler.EVENT_TYPE_AUDIOFINISH://音乐文件播放完毕
                if (musicDialog != null) {
                    musicDialog.nextPlay();
                }
                break;
            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR://发生错误回调
//                int errorCode = (int) data[0];
//                showToast("直播间发生错误" + errorCode);
                break;
            case AGEventHandler.EVENT_TYPE_ON_LOST_ERROR://网络链接丢失回调
//                showToast("网络链接异常，重连中...");
                break;
        }
    }

    //设置麦上用户说话动画
    private void showMicPersonAnimation(IRtcEngineEventHandler.AudioVolumeInfo[] speakers) {
        chatUserList = placeRecyclerAdapter.getData();
        for (int i = 0; i < speakers.length; i++) {
            LogUtils.e("说话者id：" + speakers[i].uid);
            if (speakers[i].uid == roomUserId) { //如果是房主，则房主在说话中
                setHomeAnimation(true);
                addTypeList(new VoiceTypeModel(3, 8, 3));
                continue;
            } else if (speakers[i].uid == 0) { //等于0是自己
                if (userRoomType != 1 && isOpenMicrophone) { //当前用户不是房主并且开启麦克风
                    speakers[i].uid = userToken;
                }
            }
            for (int j = 0; j < chatUserList.size(); j++) {
                VoiceMicBean.DataBean usetListOne = chatUserList.get(j);
                if (usetListOne.getUserModel() != null) { //其他用户说话
                    if (usetListOne.getUserModel().getId() == speakers[i].uid) {
                        usetListOne.getUserModel().setSpeak(true);
                        if (placeRecyclerAdapter != null) {
                            if (placeRecyclerAdapter.getItem(j) != null) {
                                placeRecyclerAdapter.setData(j, usetListOne);
                            }
                            addTypeList(new VoiceTypeModel(3, j, 3));
                        }
                        break;
                    }
                }
            }

//            for (VoiceMicBean.DataBean dataBean : chatUserList) {
//                if (dataBean.getUserModel() == null) {
//                    continue;
//                } else if (dataBean.getUserModel().getId() == speakers[i].uid) {
//                    dataBean.getUserModel().setSpeak(true);
//                }
//            }

        }
    }

//    ValueAnimator valueAnimator;

    //设置房主麦克风动画
    Animation animation;
    Animation animationOne;

    private void setHomeAnimation(boolean isOpenMicrophone) {
        if (isOpenMicrophone) {
            ivTopnamebackVoice.setImageResource(R.drawable.bg_round_green);
            if (animation == null) {
                animation = AnimationUtils.loadAnimation(this, R.anim.view_room);
                animation.setRepeatCount(Animation.INFINITE);
                ivTopnamebackVoice.startAnimation(animation);
            } else {
                ivTopnamebackVoice.startAnimation(animation);
            }
//            if (valueAnimator == null) {
//                valueAnimator = ValueAnimator.ofInt(ivTopnamebackVoice.getLayoutParams().width
//                        , rlTopimgVoice.getLayoutParams().width);
//                valueAnimator.setDuration(800);
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        int currentValue = (Integer) animation.getAnimatedValue();
//                        ivTopnamebackVoice.getLayoutParams().width = currentValue;
//                        ivTopnamebackVoice.getLayoutParams().height = currentValue;
//                        // 刷新视图，即重新绘制，从而实现动画效果
//                        ivTopnamebackVoice.requestLayout();
//                    }
//                });
//
//                valueAnimator.setRepeatCount(Animation.INFINITE);//设定无限循环
//                valueAnimator.setRepeatMode(ObjectAnimator.RESTART);// 循环模式
//                valueAnimator.setInterpolator(new DecelerateInterpolator());
//                valueAnimator.start();
//            } else {
//                valueAnimator.resume();
//            }

        } else {
            ivTopnamebackVoice.clearAnimation();
            ivTopnamebackVoice.setImageResource(R.drawable.bg_round_blue);

        }
    }

//    //其他用户可见房主的说话中动画
//    private void setHomeOneAnimation(boolean isShow) {
//        if (isShow) {
//            if (animationOne == null) {
//                animationOne = AnimationUtils.loadAnimation(this, R.anim.view_room);
//                animationOne.setRepeatCount(3);
//                ivTopnamebackVoice.startAnimation(animationOne);
//            } else {
//                ivTopnamebackVoice.startAnimation(animationOne);
//            }
//        } else {
//            ivTopnamebackVoice.clearAnimation();
//        }
//    }

    //设置聊天输入框打开或者关闭

    private void setIsOpen() {
        edtInputMychat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
//                //获取当前界面可视部分
                VoiceActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                //获取屏幕的高度
                int screenHeight = 0;
//                if (NavigationBarUtil.checkDeviceHasNavigationBar(VoiceActivity.this)) {
//                    if (NavigationBarUtil.checkNavigationBarShow(VoiceActivity.this, VoiceActivity.this.getWindow())) {
//                        screenHeight = VoiceActivity.this.getWindow().getDecorView().getRootView().getHeight();
//                    } else {
//                        screenHeight = NavigationBarUtil.getRealHeight(VoiceActivity.this);
//                    }
//                } else {
//                    screenHeight = VoiceActivity.this.getWindow().getDecorView().getRootView().getHeight();
//                }
                screenHeight = VoiceActivity.this.getWindow().getDecorView().getRootView().getHeight();
                setVIewShow(screenHeight, rect);
            }
        });
    }

    private void setVIewShow(int screenHeight, Rect rect) {
        //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
        int keyboardHeight = screenHeight - rect.bottom;

        LogUtils.e("msg", keyboardHeight + " " + screenHeight);
//                float pmmd = getResources().getDisplayMetrics().density;
        if (keyboardHeight == 0) {
            if (!isOpenChat && isCanOpen) {
                rlChatBack.setVisibility(View.VISIBLE);
                isOpenChat = true;
            } else if (isOpenChat && !isCanOpen) {
                rlChatBack.setVisibility(View.GONE);
                isOpenChat = false;
            }
        } else if (keyboardHeight <= screenHeight / 4) {
            rlChatBack.setVisibility(View.GONE);
            isOpenChat = false;
        } else {
            if (isCanOpen) {
                isCanOpen = false;
                rlChatBack.setVisibility(View.VISIBLE);
                isOpenChat = true;
                int keyboardNow = (int) (screenHeight - keyboardHeight);//获取软键盘已上的高度
                setBoottomMargin(keyboardNow);
                if (noShowString == null) {
                    try {
                        noShowString = readFile02("ReservedWords-utf8.txt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 读取敏感词
     *
     * @param path
     * @return
     * @throws IOException
     */
    public List<String> readFile02(String path) throws IOException {
        // 使用一个字符串集合来存储文本中的路径 ，也可用String []数组
        List<String> list = new ArrayList<String>();
//        FileInputStream fis = new FileInputStream(path);
        InputStream in = getResources().getAssets().open(path);
        // 防止路径乱码   如果utf-8 乱码  改GBK     eclipse里创建的txt  用UTF-8，在电脑上自己创建的txt  用GBK
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            // 如果 t x t文件里的路径 不包含---字符串       这里是对里面的内容进行一个筛选
            if (line.lastIndexOf("---") < 0) {
                list.add(line);
            }
        }
        br.close();
        isr.close();
        in.close();
        return list;
    }


    private void setBoottomMargin(int keyboardNow) {
        if (keyboardNow > 100) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(llChatVoice.getLayoutParams());
            int llvoiceHighe = lp.height;//获取布局的高度
            //设置聊天窗口距离顶部高度
            LogUtils.e("高度" + (keyboardNow - llvoiceHighe));
            lp.setMargins(0, keyboardNow - llvoiceHighe, 0, 0);
            llChatVoice.setLayoutParams(lp);

            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(llChattypeVoice.getLayoutParams());
            int llvoiceHighe1 = lp1.height;//获取布局的高度
            //设置聊天窗口距离顶部高度
            LogUtils.e("高度lp1" + (llvoiceHighe1));
            lp1.setMargins(0, keyboardNow - llvoiceHighe - llvoiceHighe1, 0, 0);
            llChattypeVoice.setLayoutParams(lp1);
        }
    }

    /**
     * 聊天列表适配器
     */
    private void setChatRecycler() {
        chatRecyclerAdapter = new ChatRecyclerAdapter(R.layout.item_chat_voice);
        chatRecyclerAdapter.setOnAttentionClicker(new ChatRecyclerAdapter.OnAttentionClicker() {
            @Override
            public void onClicker(int userId) {
                if (userId != 0) {
                    showOtherDialog(userToken, userId);
//                    showMyPseronDialog(userToken, userId);

//                    getAttentionCall();
                }
            }

            @Override
            public void onFollowUser(int userId) {
                getAttentionCall(userToken);
            }

            @Override
            public void onOtherRoomClicker(String RoomId) {
                toOtherRoom(RoomId);
//                if (RoomId.equals(roomId)) {
//                    showToast("您已在该房间");
//                    return;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putString(Const.ShowIntent.ROOMID, RoomId);
//                ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
//                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewChatVoice.setLayoutManager(layoutManager);
        mRecyclerViewChatVoice.setAdapter(chatRecyclerAdapter);


        chatRecyclerAdapter.setNewData(chatShowChat);

        mRecyclerViewChatVoice.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    tvHasMessageVoice.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (Const.RoomId.equals(String.valueOf(roomId))) {//如果有历史聊天数据则复制给当前页面
            mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
        }
    }

    // 点击关注请求接口
    private void getAttentionCall(int uid) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", uid);
        map.put("buid", roomUserId);
        map.put("type", 1);
        HttpManager.getInstance().post(Api.addAttention, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("关注成功");
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }


    //其他位置点击弹窗
    OtherShowDialog otherShowDialog;

    private void showOtherDialog(int userId, int otherId) {
        if (otherShowDialog != null && otherShowDialog.isShowing()) {
            otherShowDialog.dismiss();
        }
        ArrayList<String> seatList = new ArrayList<>();
        seatList.add(getString(R.string.tv_sendgift_bottomshow));
        seatList.add(getString(R.string.tv_message_bottomshow));
        seatList.add(getString(R.string.tv_sixin));
        otherShowDialog = new OtherShowDialog(this, seatList, userId, otherId);
        otherShowDialog.show();
        otherShowDialog.setOnItemClickListener((position, user) -> {
            voicePresenter.showOtherDialog(position, userId, user);
        });
    }

    /**
     * 设置麦上用户列表adapter
     */
    private void setPlaceRecycler() {
        placeRecyclerAdapter = new PlaceRecyclerAdapter(R.layout.item_place_voice);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerViewPlaceVoice.setLayoutManager(layoutManager);
        mRecyclerViewPlaceVoice.setAdapter(placeRecyclerAdapter);

        placeRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (roomBean == null)
                    return;
                VoiceMicBean.DataBean dataBean = (VoiceMicBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                voicePresenter.PlaceClicker(dataBean, position, userToken, userRoomType, roomId, roomBean.getState());
            }
        });
    }

    @Override
    public void setResume() {
//        LogUtils.e("msg", "刷新页面");

        //判断是否更新头像(房主)
        String userRoomImg = (String) SharedPreferenceUtils.get(this, Const.User.IMG, "");
        if (!userRoomImg.equals(roomImg) && userToken == roomUserId) {
            roomImg = userRoomImg;
            ImageUtils.loadImage(ivTopnameVoice, roomImg, -1, R.drawable.default_head);
        }
    }

    @Override
    public void setOnclick() {

    }

    @Override
    public void onBackPressed() {
//        if (Const.chatShowChat != null) {
//            Const.chatShowChat.clear();
//        }
        if (System.currentTimeMillis() - nextGoRoomTime < 3000) {
            return;
        }
        //如果礼物动画在执行中，点击返回取消动画
        if (laySVGAImageView_gift.getVisibility() == View.VISIBLE) {
            clearGiftShow(false);
            return;
        }
        Const.chatShow = chatShowChat;
        BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MUSIC_REPLAY);
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MUSIC_PAUSE);
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MUSIC_PLAY);
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.PACKET_OVER);
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.MSG_COUN);
//        if (valueAnimator != null) {
//            valueAnimator.cancel();
//            valueAnimator.end();
//        }

        handler.removeCallbacksAndMessages(null);

        MyApplication.getInstance().delChatRoomMessage(chatRoomMessage1);

        if (animation != null) {
            animation.cancel();
        }
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
        if (timerType != null) {
            timerType.cancel();
        }

        if (subscribe != null) {
            subscribe.dispose();//取消订阅
        }
        MyApplication.getInstance().getWorkerThread().eventHandler().removeEventHandler(agEventHandler);
        MessageUtils.getInstans().removeChatShows(chatMessage);
        Const.packetNumber = packetNumber;//记录房间红包个数

        if (chestsDialog != null) {
            chestsDialog.dismiss();
        }

        mSVGAImageViewGift = null;
        svgaDrawable = null;
        ivShowGift = null;

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        clearGiftShow(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_topimg_voice, R.id.tv_homename_voice, R.id.tv_homeid_voice,
            R.id.tv_homenum_voice, R.id.iv_homeset_voice,
            R.id.tv_homecon_voice, R.id.iv_auction_voice, R.id.rl_music_voice,
            R.id.tv_hometype_voice, R.id.tv_homedetails_voice_rl, R.id.rl_chat_back,
            R.id.chart_ll, R.id.iv_mic_voice, R.id.iv_receiver_voice,
            R.id.iv_express_voice, R.id.iv_envelope_voice, R.id.iv_packet_voice,
            R.id.iv_gift_voice, R.id.btn_send_mychat, R.id.rl_back_voice,
            R.id.edt_input_mychat, R.id.tv_has_message_voice, R.id.iv_deepSeaFishing, R.id.iv_dinghaiFishing, R.id.iv_donghaiFishing,
            R.id.iv_getpacket_voice, R.id.tv_chattype_voice, R.id.tv_chat2_voice,
            R.id.tv_chat1_voice, R.id.tv_alltopic_voice, R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_topimg_voice://点击头像
                VoiceMicBean.DataBean homeData = new VoiceMicBean.DataBean();
                homeData.setPid(roomId);
                VoiceUserBean.DataBean userShowBean = new VoiceUserBean.DataBean();
                userShowBean.setImg(roomImg);
                userShowBean.setId(roomUserId);
                userShowBean.setName(roomUserName);
                userShowBean.setUsercoding(roomId);
                homeData.setUserModel(userShowBean);
                voicePresenter.onUserClicker(homeData, userToken, userRoomType, roomId);
//                if (userRoomType == 1) {
//                    showMyPseronDialog(userToken, roomBean.getUid());
//                } else {

//                    showMyGiftDialog(userToken, roomBean.getUid(), Const.IntShow.ONE, roomBean.getName(), roomBean.getState(), false);
//                    if (giftDialog != null) {
//                        giftDialog.setSendGift(new MyGiftDialog.SendGift() {
//                            @Override
//                            public void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
//                                int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
//                                if (sendType == 1) { //1 是普通， 2是背包
//                                    if (userGold < goodGold * num * sum) {
//                                        showMyDialog(getString(R.string.hint_nogold_gift),
//                                                Const.IntShow.THREE, getString(R.string.tv_topup_packet));
//                                        if (myDialog != null) {
//                                            myDialog.setRightButton(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    if (myDialog != null && myDialog.isShowing()) {
//                                                        myDialog.dismiss();
//                                                    }
//                                                    ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
//                                                }
//                                            });
//                                        }
//                                    } else {
//                                        voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
//                                    }
//                                } else if (sendType == 2) {
//                                    voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
//                                }
//                            }
//                        });
//                    }
//                }
                break;
            case R.id.tv_homename_voice:
                showMyPseronDialog(userToken, roomBean.getUid());
                break;
            case R.id.tv_homeid_voice:
                break;
            case R.id.tv_homenum_voice://用户总数
                setUserDialogShow();
                break;
            case R.id.iv_homeset_voice://房间设置
                voicePresenter.SetClicker(userRoomType, roomBean);
//                initExitClicker();
//                setExitClicker(showMybottomDialog(bottomList));
                break;
            case R.id.tv_homecon_voice:
//                showMyRankingDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString("roomId", roomId);
                bundle2.putInt("userRoomType", userRoomType);
                RoomRankDialogFragment rankDialogFragment = new RoomRankDialogFragment();
                rankDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                rankDialogFragment.setArguments(bundle2);
                rankDialogFragment.show(getSupportFragmentManager(), "rank");
//                ActivityCollector.getActivityCollector().toOtherActivity(RoomRankActivity.class, bundle2);
                break;
            case R.id.iv_auction_voice:
                showAuction();
                break;
            case R.id.rl_music_voice:
                showMyMusicDialog();
                break;
            case R.id.tv_hometype_voice:
                break;
            case R.id.tv_homedetails_voice_rl://房间话题
                if (userRoomType == 1 || userRoomType == 2) { //房主或管理员
                    Bundle bundle = new Bundle();
                    bundle.putString(Const.ShowIntent.TOPIC, roomTopic);
                    bundle.putString(Const.ShowIntent.DATA, topicCount);
                    bundle.putString(Const.ShowIntent.ROOMID, roomId);
                    ActivityCollector.getActivityCollector().toOtherActivity(SetTopicActivity.class, bundle);
                } else {
//                    showTopicDialog();
                }
                break;
            case R.id.chart_ll://消息按钮
//                showMyChatDialog();
                if (!isOpenChat && isOpenGp) { //开启公屏并且未显示聊天框
                    rlChatBack.setVisibility(View.VISIBLE);
                    edtInputMychat.setFocusable(true);
                    edtInputMychat.setFocusableInTouchMode(true);
                    edtInputMychat.requestFocus();
                    isCanOpen = true;
                    MyUtils.getInstans().showOrHahe(this);
                } else {
                    isOpenChat = false;
                }
                break;
            case R.id.iv_mic_voice://麦克风
                if (isMicShow) { //在麦上才可以开启麦克风
                    if (isMicCan) { //未禁麦才能开启或关闭
                        if (isOpenMicrophone) {
                            isOpenMicrophone = false;
                        } else {
                            isOpenMicrophone = true;
                        }
                        setMicisOpen();
                    }
                } else { //未在麦上点击排麦
                    if (userRoomType == 1) { //房主直接在麦上
                        isMicShow = true;
                        setMicisOpen();
                        return;
                    }
                    showBottomSheat();
                }
                break;
            case R.id.iv_receiver_voice://扬声器
                if (isOpenReceiver) {
                    int isSuccess = MyApplication.getInstance().getWorkerThread().getRtcEngine().muteAllRemoteAudioStreams(true);
                    if (isSuccess == 0) {
                        isOpenReceiver = false;
                        openReceiverObservable = Observable.just(isOpenReceiver);
                        openReceiverObservable.subscribe(receiverConsumer);
//                        ivReceiverVoice.setSelected(false);
                    } else {
                        showToast("关闭音频失败，请稍后再试");
                    }
                } else {
                    int isSuccess = MyApplication.getInstance().getWorkerThread().getRtcEngine().muteAllRemoteAudioStreams(false);
                    if (isSuccess == 0) {
                        isOpenReceiver = true;
                        openReceiverObservable = Observable.just(isOpenReceiver);
                        openReceiverObservable.subscribe(receiverConsumer);
//                        ivReceiverVoice.setSelected(true);
                    } else {
                        showToast("开启音频失败，请稍后再试");
                    }
                }
                break;
            case R.id.iv_express_voice://表情
                showMyEmojiDialog();
                break;
            case R.id.iv_envelope_voice://消息
                showMessageDialog();
                break;
            case R.id.iv_packet_voice://发红包
                showRewardDialog();
                break;
            case R.id.iv_gift_voice://送礼物
                if (roomBean == null) { //判空
                    showMyGiftDialog(userToken, userToken, Const.IntShow.ONE, roomUserName, 1, true);
                } else {
                    showMyGiftDialog(userToken, userToken, Const.IntShow.ONE, roomUserName, roomBean.getState(), true);
                }
                if (giftDialog != null) {
                    giftDialog.setSendGift(new MyGiftDialog.SendGift() {
                        @Override
                        public void getSendGift(String ids, String names, int gid, String img,
                                                String showImg, int num, int sum, int goodGold, int sendType) {
                            int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
                            if (sendType == 1) { //1 是普通， 2是背包
                                if (userGold < goodGold * num * sum) {
                                    showMyDialog(getString(R.string.hint_nogold_gift),
                                            Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                                    if (myDialog != null) {
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
                                } else {
                                    voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                                }
                            } else if (sendType == 2) {
                                voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                            }

                        }

                        @Override
                        public void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
                            onGiftSendSuccess(userToken, ids, names, gid, img, showImg, num, goodGold);
                        }
                    });
                }
                break;
            case R.id.btn_send_mychat://发送房间消息或平台消息
                if (isOpenChat) {
                    String chatInputShow = edtInputMychat.getText().toString();
                    if (StringUtils.isEmpty(chatInputShow)) {
                        showToast("请输入聊天内容");
                        return;
                    }
                    if (noShowString != null) {
                        for (String noShow : noShowString) {
                            if (noShow.equals(chatInputShow)) {
                                showToast("您输入的内容带有敏感词汇");
                                return;
                            }
                        }
                    }
//                    ChatMessageBean.DataBean dataBean = new ChatMessageBean.DataBean(userToken, userName, goldNum, chatInputShow);
                    llChattypeVoice.setVisibility(View.INVISIBLE);
                    if (isAllChat) { //全平台消息
                        voicePresenter.getAllChat(userToken, roomId, chatInputShow);
                        edtInputMychat.setText("");
                    } else {
                        ChatMessageBean.DataBean dataBean = new ChatMessageBean.DataBean(goldNum, chatInputShow, userName, userToken);
                        ChatMessageBean chatMessageBean = new ChatMessageBean(100, dataBean);
                        String chatString = MyUtils.mGson.toJson(chatMessageBean);
                        setSendMessage(chatString, roomId, String.valueOf(userToken));
                        edtInputMychat.setText("");
                    }
                    if (isOpenChat) {
                        MyUtils.getInstans().hideKeyboard(llChatVoice);
                        rlChatBack.setVisibility(View.GONE);
                        isOpenChat = false;
                    }
                }
                break;
            case R.id.rl_back_voice:

                break;
            case R.id.iv_deepSeaFishing://深海捕鱼
                showChestsDialog(1);
                break;
            case R.id.iv_dinghaiFishing://定海神针
                showChestsDialog(2);
                break;
            case R.id.iv_donghaiFishing://东海龙宫
                showChestsDialog(3);
                break;
            case R.id.tv_has_message_voice://底部有新消息
                tvHasMessageVoice.setVisibility(View.INVISIBLE);
                mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
                break;
            case R.id.rl_chat_back://隐藏聊天弹窗
                if (isOpenChat) {
                    MyUtils.getInstans().hideKeyboard(llChatVoice);
                    rlChatBack.setVisibility(View.GONE);
                    isOpenChat = false;
                    isCanOpen = false;
                }
                break;
            case R.id.iv_getpacket_voice://领取红包按钮
                Bundle bundle = new Bundle();
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(ReceivePacketActivity.class, bundle);
                break;
            case R.id.tv_chattype_voice://发送消息类型
                if (llChattypeVoice.getVisibility() == View.VISIBLE) {
                    llChattypeVoice.setVisibility(View.INVISIBLE);
                } else {
                    llChattypeVoice.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.tv_chat2_voice://全平台
                isAllChat = true;
                llChattypeVoice.setVisibility(View.INVISIBLE);
                setAllChatShow();
                break;
            case R.id.tv_chat1_voice://房间
                isAllChat = false;
                llChattypeVoice.setVisibility(View.INVISIBLE);
                setAllChatShow();
                break;
            case R.id.tv_alltopic_voice://go 消息
                String rid = (String) tvAlltopicVoice.getTag();
                toOtherRoom(rid);
                break;
            case R.id.back_iv://返回
                onBackPressed();
                break;
        }
    }

    /**
     * 显示全服礼物通知
     */
    MyGiftAllshowDialog myGiftAllshowDialog;

    private void showGiftAllDialog(ArrayList<GiftAllModel.DataBean> giftAllModels) {
        if (myGiftAllshowDialog != null && myGiftAllshowDialog.isShowing()) {
            myGiftAllshowDialog.setGiftAllModel(giftAllModels);
        } else {
            if (myGiftAllshowDialog != null) {
                myGiftAllshowDialog.dismiss();
            }
            myGiftAllshowDialog = new MyGiftAllshowDialog(this, giftAllModels);
            myGiftAllshowDialog.show();
            myGiftAllshowDialog.getButton().setOnClickListener(v -> {
                toOtherRoom(myGiftAllshowDialog.getRoomId());
            });
        }
    }

    /**
     * 转跳其他房间
     *
     * @param RoomId 其他房间id
     */
    private void toOtherRoom(String RoomId) {
        if (RoomId.equals(roomId)) {
            showToast("您已在该房间");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ROOMID, RoomId);
        ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
    }

    /**
     * 设置是否全平台消息显示
     */
    private void setAllChatShow() {
        if (isAllChat) {
            tvChattypeVoice.setText(R.string.hint_chat2_voice);
        } else {
            tvChattypeVoice.setText(R.string.hint_chat1_voice);
        }
    }

    /**
     * 消息弹窗
     */
    private void showMessageDialog() {
//        if (bottomMessageDialog != null && bottomMessageDialog.isShowing()) {
//            bottomMessageDialog.dismiss();
//        }
//        bottomMessageDialog = new BottomMessageDialog(VoiceActivity.this);
//        bottomMessageDialog.show();
        messageDialogFragment = new MessageDialogFragment();
        messageDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        messageDialogFragment.show(getSupportFragmentManager(), "messageDialogFragment");
    }

    /**
     * Pk弹窗
     *
     * @param data
     */
    private void showMyPkDialog(PkSetBean.DataBean data) {
        if (pkDialog != null && pkDialog.isShowing()) {
            pkDialog.updateData(data);
        } else {
            if (this.isFinishing() || data == null) {
                return;
            }
            pkDialog = new MyPkDialog(VoiceActivity.this, userToken, data);
            pkDialog.show();
            pkDialog.setOneClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voicePresenter.setPkClicker(data.getState(), userToken, data.getId(),
                            data.getUser().getId(), data.getUser().getName());
                }
            });

            pkDialog.setTwoClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voicePresenter.setPkClicker(data.getState(), userToken, data.getId(),
                            data.getUser1().getId(), data.getUser1().getName());
                }
            });

            pkDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setPkShow();
                }
            });
        }
    }


    /**
     * 开箱探险
     */
    private void showChestsDialog(int type) {
        clearGiftShow(false);

//        if (chestsDialog != null && chestsDialog.isShowing()) {
//            chestsDialog.dismiss();
//        }
//        chestsDialog = new MyChestsOneDialog(VoiceActivity.this, userToken, roomId);
//        chestsDialog.setSendChestsMsg(new MyChestsOneDialog.SendChestsMsg() {
//            @Override
//            public void onSendChestsMsg(String msg) {
//                sendTencentMsg(msg);
//            }
//        });
//        chestsDialog.show();
//        chestsDialog.getTopup().setOnClickListener(v -> {
//            if (chestsDialog != null && chestsDialog.isShowing()) {
//                chestsDialog.dismiss();
//            }
//            showToast("期待开放");
//        });


        switch (type) {
            case 1:
                if (deepSeaFishingDialog != null && deepSeaFishingDialog.isShowing()) {
                    deepSeaFishingDialog.dismiss();
                } else {
                    deepSeaFishingDialog = new DeepSeaFishingDialog(this, userToken, roomId);
                    deepSeaFishingDialog.setSendChestsMsg(new DeepSeaFishingDialog.SendChestsMsg() {
                        @Override
                        public void onSendChestsMsg(String msg) {
                            sendTencentMsg(msg);
                        }
                    });
                    deepSeaFishingDialog.show();
                }
                break;
            case 2:
                if (dingHaiFishingDialog != null && dingHaiFishingDialog.isShowing()) {
                    dingHaiFishingDialog.dismiss();
                } else {
                    dingHaiFishingDialog = new DingHaiFishingDialog(this, userToken, roomId);
                    dingHaiFishingDialog.setSendChestsMsg(new DingHaiFishingDialog.SendChestsMsg() {
                        @Override
                        public void onSendChestsMsg(String msg) {
                            sendTencentMsg(msg);
                        }
                    });
                    dingHaiFishingDialog.show();
                }
                break;
            case 3:
                if (donghaiPalaceFishingDialog != null && donghaiPalaceFishingDialog.isShowing()) {
                    donghaiPalaceFishingDialog.dismiss();
                } else {
                    donghaiPalaceFishingDialog = new DonghaiPalaceFishingDialog(this, userToken, roomId);
                    donghaiPalaceFishingDialog.setSendChestsMsg(new DonghaiPalaceFishingDialog.SendChestsMsg() {
                        @Override
                        public void onSendChestsMsg(String msg) {
                            sendTencentMsg(msg);
                        }
                    });
                    donghaiPalaceFishingDialog.show();
                }
                break;
        }
    }

    private long lastClickTime;

    /**
     * 收红包弹窗
     *
     * @param id           数据id
     * @param packetId     红包id
     * @param packetNumber 红包个数
     */
    @SuppressLint("CheckResult")
    private void showPacketDialog(int id, int packetId, String img, String nickName, int packetNumber) {
        if (packetDialog != null && packetDialog.isShowing()) {
            packetDialog.dismiss();
        }
        packetDialog = new MyPacketDialog(VoiceActivity.this, id, packetId, userToken, img, nickName, packetNumber);
        if (!isFinishing()) {
            packetDialog.show();
            packetDialog.setOpenOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voicePresenter.openPacket(id, packetId);
                }
            });
        }

//        Observable.just("").observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//
//                    }
//                });
    }

    /**
     * 发红包弹窗
     */
    private void showRewardDialog() {
        if (rewardDialog != null && rewardDialog.isShowing()) {
            rewardDialog.dismiss();
        }
        rewardDialog = new MyRewardDialog(VoiceActivity.this, roomId, userToken, Const.IntShow.ONE);
        rewardDialog.show();
        rewardDialog.setSendRedPacket(new MyRewardDialog.SendRedPacket() {
            @Override
            public void getRedPacket(int gold, String ids, int num) {
                setSendRedPacketShow(gold, ids, num);
            }
        });
    }

    /**
     * 发红包弹窗
     */
    private void showRewardDialog(int otherId, String otherName) {
        if (rewardDialog != null && rewardDialog.isShowing()) {
            rewardDialog.dismiss();
        }
        rewardDialog = new MyRewardDialog(VoiceActivity.this, roomId, userToken, otherId, otherName, Const.IntShow.TWO);
        rewardDialog.show();
        rewardDialog.setSendRedPacket(new MyRewardDialog.SendRedPacket() {
            @Override
            public void getRedPacket(int gold, String ids, int num) {
                setSendRedPacketShow(gold, ids, num);
            }
        });
    }

    /**
     * 设置发红包显示
     *
     * @param gold
     * @param ids
     * @param num
     */
    private void setSendRedPacketShow(int gold, String ids, int num) {
        int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
        if (userGold < gold) {
            showMyDialog(getString(R.string.hint_nogold_packet),
                    Const.IntShow.THREE, getString(R.string.tv_topup_packet));
            if (myDialog != null) {
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
        } else {
            voicePresenter.sendRedPacket(userToken, roomId, gold, ids, num);
        }
    }

    /**
     * 设置打开或者关闭麦克风
     */
    private void setMicisOpen() {
        if (userRoomType == 1) { //如果是房主，开启动画开启麦克风
            Log.e("mmp", "isOpenMicrophone:" + isOpenMicrophone);
            Log.e("mmp", "isMicCan:" + isMicCan);
            setHomeAnimation(isOpenMicrophone);
        }
        if (isOpenMicrophone && isMicCan) { //打开麦克风
            int i = MyApplication.getInstance().getWorkerThread().getRtcEngine().muteLocalAudioStream(false);
            Log.e("mmp", "打开麦克风状态:" + i);

            if (i == 0) {
                LogUtils.e(LogUtils.TAG, "麦克风取消静音");
                setUpdateMic(Const.IntShow.ZERO);
            } else {
                LogUtils.e(LogUtils.TAG, "请求开启麦克风失败");
            }
        } else { //关闭麦克风
            int i = MyApplication.getInstance().getWorkerThread().getRtcEngine().muteLocalAudioStream(true);
            Log.e("mmp", "沉默麦克风状态:" + i);
            if (i == 0) {
                LogUtils.e(LogUtils.TAG, "麦克风静音");
                setUpdateMic(Const.IntShow.ONE);
            } else {
                LogUtils.e(LogUtils.TAG, "请求关闭麦克风失败");
            }
        }
    }

    /**
     * 对话框
     *
     * @param hintShow  提示内容
     * @param typeShow  点击类型 （弃用）
     * @param rightShow 提示右边显示，可为空
     */
    private void showMyDialog(String hintShow, final int typeShow, String rightShow) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(VoiceActivity.this);
        myDialog.show();
        myDialog.setHintText(hintShow);
        if (!StringUtils.isEmpty(rightShow)) {
            myDialog.setRightText(rightShow);
        }
//        myDialog.setRightButton(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (myDialog != null && myDialog.isShowing()) {
//                    myDialog.dismiss();
//                }
//                switch (typeShow) {
//                    case 1:
//                        voicePresenter.pkClose(roomPid);
//                        break;
//                    case 2:
//
//                        break;
//                    case 3:
//                        ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
//                        break;
//                }
//            }
//        });
    }


    private void showMyMusicDialog() {
        if (musicDialog != null && musicDialog.isShowing()) {
            musicDialog.dismiss();
        } else {
            musicDialog = new MyMusicDialog(VoiceActivity.this);
        }
        musicDialog.show();
    }


    /**
     * 礼物弹窗
     *
     * @param userId
     * @param otherId  //被赠送的用户id
     * @param type     //1送礼物  2pk送礼物
     * @param userName 展示的名字，可为空
     */
    private void showMyGiftDialog(int userId, int otherId, int type, String userName, int roomState, boolean isBootom) {
        if (giftDialog != null && giftDialog.isShowing()) {
            giftDialog.dismiss();
        }

        giftDialog = new MyGiftDialog(VoiceActivity.this, userId, otherId, type, userName, roomId, roomState, isBootom);
        giftDialog.show();
        giftDialog.setDataShow(v -> {
            int sendId = giftDialog.getOtherId();
            if (giftDialog != null && giftDialog.isShowing()) {
                giftDialog.dismiss();
            }
            showMyPseronDialog(userId, sendId);
        });
        giftDialog.setPacketGiftListener(v -> {
            int sendId = giftDialog.getOtherId();
            String userNamenow = giftDialog.getUserName();
            if (giftDialog != null && giftDialog.isShowing()) {
                giftDialog.dismiss();
            }
            if (sendId == userToken) {
                showToast("不能给自己发红包");
                return;
            }
            showRewardDialog(sendId, userNamenow);
        });
        giftDialog.setSendAllPacketGift((userId1, tab, giftID) -> {
            if (giftDialog != null && giftDialog.isShowing()) {
                giftDialog.dismiss();
            }

            if (tab == 5) {// 一键全刷
                voicePresenter.getSendAllGift(roomId, userToken, userId1, giftDialog);
            } else {// 转赠好友
                voicePresenter.getSendPartyGift(roomId, userToken, userId1, giftID);
            }
        });

    }

    /**
     * 发表情弹窗
     */
    private void showMyEmojiDialog() {
        if (myExpressionDialog != null && myExpressionDialog.isShowing()) {
            myExpressionDialog.dismiss();
        }
        myExpressionDialog = new MyExpressionDialog(VoiceActivity.this);
        myExpressionDialog.show();
        myExpressionDialog.setEmojiChoose(new MyExpressionDialog.EmojiChoose() {
            @Override
            public void chooseOne(EmojiList.DataBean dataBean) {
                if (dataBean != null) {
                    int numberShow = getNumberShow(dataBean.getUnicode());
                    EmojiMessageBean.DataBean emojiData =
                            new EmojiMessageBean.DataBean(goldNum,
                                    dataBean.getUnicode(), userName, userToken, numberShow, dataBean.getName());
                    EmojiMessageBean emojiMessageBean = new EmojiMessageBean(108, emojiData);
                    String chatString = MyUtils.mGson.toJson(emojiMessageBean);
                    setSendMessage(chatString, roomId, String.valueOf(userToken));
                }
            }
        });
    }

    private int getNumberShow(int uniCode) {
        Random ra = new Random();//取1-8的随机数
        int number = 0;
        switch (uniCode) {
            case 128552://排麦序
                number = ra.nextInt(8) + 1;
                break;
            case 128555://石头剪刀布
                number = ra.nextInt(3) + 1;
                break;
            case 128562://骰子
                number = ra.nextInt(6) + 1;
                break;
            case 128563://猜硬币
                number = ra.nextInt(2) + 1;
                break;
            case 128606://石头剪刀布
                number = ra.nextInt(3) + 1;
                break;
            case 128615://排麦序
                number = ra.nextInt(8) + 1;
                break;
            case 128621://股子
                number = ra.nextInt(6) + 1;
                break;
        }
        return number;
    }

    /**
     * 个人资料
     *
     * @param userId  用户id
     * @param otherId 查询对象id
     */
    private void showMyPseronDialog(int userId, int otherId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.ID, otherId);
        if (otherId == userId) {
            ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
        } else {
            ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
        }

//        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
//            myBottomPersonDialog.dismiss();
//        }
//        myBottomPersonDialog = new MyBottomPersonDialog(VoiceActivity.this, userId, otherId);
//        myBottomPersonDialog.show();
    }

    /**
     * 房间贡献榜弹框
     */
    private void showMyRankingDialog() {
        if (myRankingDialog != null && myRankingDialog.isShowing()) {
            myRankingDialog.dismiss();
        }
        myRankingDialog = new MyRankingDialog(VoiceActivity.this, roomId, userToken);
        myRankingDialog.show();
    }

    /**
     * 房间话题弹框
     */
    private void showTopicDialog() {
        if (myTopicshowDialog != null && myTopicshowDialog.isShowing()) {
            myTopicshowDialog.dismiss();
        }
        myTopicshowDialog = new MyTopicshowDialog(VoiceActivity.this, roomTopic, topicCount);
        myTopicshowDialog.show();
    }

    /**
     * 排麦人数显示
     */
    private void showBottomSheat() {
        if (bottomWheatDialog != null && bottomWheatDialog.isShowing()) {
            bottomWheatDialog.dismiss();
        }
        bottomWheatDialog = new MyBottomWheatDialog(VoiceActivity.this, roomId, userToken);
        bottomWheatDialog.show();
    }

    /**
     * 竞拍排行榜
     */
    private void showAuction() {
        if (bottomauctionDialog != null && bottomauctionDialog.isShowing()) {
            bottomauctionDialog.dismiss();
        }
        bottomauctionDialog = new MyBottomauctionDialog(VoiceActivity.this, roomId);
        bottomauctionDialog.show();
    }

    /**
     * 显示在线用户
     */
    private void setUserDialogShow() {
        if (myOnlineUserDialog != null && myOnlineUserDialog.isShowing()) {
            myOnlineUserDialog.dismiss();
        }
        myOnlineUserDialog = new MyOnlineUserDialog(VoiceActivity.this, roomId, userToken, Const.IntShow.ONE);
        myOnlineUserDialog.show();
        myOnlineUserDialog.setOnlineClicer(new MyOnlineUserDialog.OnlineClicer() {
            @Override
            public void setOnlineClicer(VoiceUserBean.DataBean dataBean, int position) {
//                showMyGiftDialog(userToken, otherId, Const.IntShow.ONE, userName, roomBean.getState());
//                voicePresenter.onLineClicker(dataBean, userRoomType, userToken, roomId, position, roomBean.getState());
                if (dataBean != null) {
                    showOtherDialog(userToken, dataBean.getId());
                }
            }
        });
    }

    //发送消息
    private void setSendMessage(String chatInputShow, String channelId, String userId) {
        if (StringUtils.isEmpty(chatInputShow)) {
            showToast("内容不能为空");
        } else {
            YySignaling.getInstans().sendChannelMessage(chatInputShow, channelId, userId);
        }
    }

    /**
     * 麦上用户数据返回
     *
     * @param chatUserList
     */
    @Override
    public void onGetChatUserCallSuccess(List<VoiceMicBean.DataBean> chatUserList) {
        this.chatUserList = chatUserList;
        updateChatShow();
    }

    /**
     * 打开房间动画
     */
    @SuppressLint("CheckResult")
    private void openRoomAnimation(String agoraRtmToken, String agoraRtcToken) {
        if (ivInputBackVoice.getVisibility() == View.VISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivInputBackVoice, "alpha", 1, 0);
            animator.setDuration(1500);
            animator.start();
//            voicePresenter.getToken(userToken);//获取声网token
            initVoice(agoraRtmToken, agoraRtcToken);//初始化用户信息
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ivInputBackVoice.setVisibility(View.GONE);
                }
            });
        } else {
            initVoice(agoraRtmToken, agoraRtcToken);//初始化用户信息
        }
    }

    /**
     * 所有用户数据返回
     *
     * @param userList
     */
    @Override
    public void onUserCallSuccess(List<VoiceUserBean.DataBean> userList) {

    }

    /**
     * 上麦（换麦）或下麦成功
     *
     * @param type 1 是上麦， 2是下麦
     * @Param sequence 麦序
     */
    @Override
    public void onMicStateChange(int uid, int type, int sequence) {
//        voicePresenter.getChatShow(userToken, roomId);
        //成功后发送频道消息，更新麦上信息
        ChatMessageBean chatMessageBean = new ChatMessageBean(118, new ChatMessageBean.DataBean());
        String carShowString = JSON.toJSONString(chatMessageBean);
        setSendMessage(carShowString, roomId, String.valueOf(userToken));
        if (uid == userToken) {
//            isOpenMicrophone = true;
            if (type == 1) { //上麦
                if (isOpenMicrophone) {
                    setUpdateMic(Const.IntShow.ZERO);
                } else {
                    setUpdateMic(Const.IntShow.ONE);
                }
                voicePresenter.getOnline(userToken, roomId);
            } else {
                isOpenMicrophone = false;
                setUpdateMic(Const.IntShow.TWO);
            }
        }

//        if (type == 1) {
//            voicePresenter.getChatShow(userToken, roomId);
//            setUpdateMic(true);
//        } else if (type == 2) {
//            voicePresenter.getChatShow(userToken, roomId);
////            VoiceMicBean.DataBean item = placeRecyclerAdapter.getItem(sequence - 1);
////            assert item != null;
////            Objects.requireNonNull(item).getUserModel().setId(0);
////            placeRecyclerAdapter.setData(sequence - 1, item);
////            placeRecyclerAdapter.notifyItemChanged(sequence - 1, item);
//            setUpdateMic(false);
//        }
    }

    /**
     * 设置用户上麦或下麦显示
     */
    private void setUpdateMic(int isUpMic) {
        if (isUpMic == 0) { //在麦位   开启语音
            MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_BROADCASTER);
            ivMicVoice.setImageResource(R.drawable.selector_mic);
            ivMicVoice.setSelected(true);
            isMicShow = true;
            ivExpressVoice.setVisibility(View.VISIBLE);
            if (userRoomType != 3)
                rlMusicVoice.setVisibility(View.VISIBLE);
        } else if (isUpMic == 1) { //在麦位   关闭语音（麦位禁麦，用户禁麦）
            MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_AUDIENCE);
            ivMicVoice.setImageResource(R.drawable.selector_mic);
            ivMicVoice.setSelected(false);
            isMicShow = true;
            ivExpressVoice.setVisibility(View.VISIBLE);
            if (userRoomType != 3)
                rlMusicVoice.setVisibility(View.VISIBLE);
        } else if (isUpMic == 2) { //不在麦位

            //设置用户角色为观众
            MyApplication.getInstance().getWorkerThread().configEngine(Constants.CLIENT_ROLE_AUDIENCE);
            if (Const.MusicShow.isHave) { //如果有歌曲,关闭歌曲
                MyApplication.getInstance().getWorkerThread().musicStop();
            }
            isMicShow = false;
            ivMicVoice.setImageResource(R.drawable.bottom_mic_add);
            ivExpressVoice.setVisibility(View.GONE);
            rlMusicVoice.setVisibility(View.GONE);
        }
    }

    /**
     * 点击设置
     *
     * @param position 下标
     */
    @Override
    public void onUserSetClicker(int position) {
        Bundle bundle = new Bundle();
        if (userRoomType == 1) {
            switch (position) {
                case 0://关闭公屏（打开公屏）
                    if (roomBean.getIsGp() == 1) { //公屏 1 开启， 2 关闭
                        voicePresenter.addOpenGp(roomId, Const.IntShow.TWO);
                    } else if (roomBean.getIsGp() == 2) {
                        voicePresenter.addOpenGp(roomId, Const.IntShow.ONE);
                    }
                    break;
                case 1://房间设置
                    clearGiftShow(false);

                    bundle.putString(Const.ShowIntent.ROOMID, roomId);
                    ActivityCollector.getActivityCollector().toOtherActivity(RoomSetActivity.class, bundle);
                    break;
//                case 2://开启竞拍
//                    if (roomBean.getState() == 2) { //牌子房间才有竞拍
//                        if (roomBean.getIsJp() == 1) { //是否开启竞拍 1是未，2 是开启
//                            voicePresenter.IsOpenJp(roomId, Const.IntShow.ONE);
//                        } else {
//                            voicePresenter.IsOpenJp(roomId, Const.IntShow.TWO);
//                        }
//                    } else {
//                        showToast("申请牌照房间开启竞拍模式");
//                    }
//                    break;
//                case 3://开启pk
//                    if (roomBean.getIsPk() == 1) { //是否开启pk 1是未，2 是开启
//                        bundle.putString(Const.User.ROOMID, roomId);
//                        ActivityCollector.getActivityCollector().toOtherActivity(RoomPkSetActivity.class, bundle);
//                    } else if (roomBean.getIsPk() == 2) {
//                        showMyDialog("确定要关闭pk模式吗？", Const.IntShow.ONE, null);
//                        if (myDialog != null) {
//                            myDialog.setRightButton(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (myDialog != null && myDialog.isShowing()) {
//                                        myDialog.dismiss();
//                                    }
//                                    voicePresenter.pkClose(roomPid);
//                                }
//                            });
//                        }
//                    }
//                    break;
                case 2://最小化
                    onBackPressed();
//                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
//                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    break;
                case 3://退出房间
                    voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
                    break;

            }
        } else if (userRoomType == 2) {
            switch (position) {
                case 0://关闭公屏（打开公屏）
                    if (roomBean.getIsGp() == 1) { //公屏 1 开启， 2 关闭
                        voicePresenter.addOpenGp(roomId, Const.IntShow.TWO);
                    } else if (roomBean.getIsGp() == 2) {
                        voicePresenter.addOpenGp(roomId, Const.IntShow.ONE);
                    }
                    break;
                case 1://房间设置
                    bundle.putString(Const.ShowIntent.ROOMID, roomId);
                    ActivityCollector.getActivityCollector().toOtherActivity(RoomSetActivity.class, bundle);
                    break;
//                case 2://开启或关闭竞拍
//                    if (roomBean.getIsJp() == 1) { //是否开启竞拍 1是未，2 是开启
//                        voicePresenter.IsOpenJp(roomId, Const.IntShow.ONE);
//                    } else {
//                        voicePresenter.IsOpenJp(roomId, Const.IntShow.TWO);
//                    }
//                    break;
//                case 3://开启pk
//                    if (roomBean.getIsPk() == 1) { //是否开启pk 1是未，2 是开启
//                        bundle.putString(Const.User.ROOMID, roomId);
//                        ActivityCollector.getActivityCollector().toOtherActivity(RoomPkSetActivity.class, bundle);
//                    } else if (roomBean.getIsPk() == 2) {
//                        showMyDialog("确定要关闭pk模式吗？", Const.IntShow.ONE, null);
//                        if (myDialog != null) {
//                            myDialog.setRightButton(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (myDialog != null && myDialog.isShowing()) {
//                                        myDialog.dismiss();
//                                    }
//                                    voicePresenter.pkClose(roomPid);
//                                }
//                            });
//                        }
//                    }
//                    break;
                case 2://最小化
                    onBackPressed();
//                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
//                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    break;
                case 3://退出房间
                    voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
                    break;

            }
        } else if (userRoomType == 3) {
            switch (position) {
                case 0://举报房间
                    voicePresenter.updateReport(userToken, roomId);
                    break;
                case 1://最小化
                    onBackPressed();
//                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
//                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    break;
                case 2:
                    voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.TWO);
                    break;
                case 3:

                    break;
            }
        }

    }

    /**
     * 普通用户点击自己麦位
     *
     * @param position 点击的下标
     * @param sequence 麦位
     */
    @Override
    public void onUserDownClicker(int position, int sequence, String name) {
        switch (position) {
            case 0://查看资料
                showMyPseronDialog(userToken, userToken);
                break;
            case 1://下麦旁听
                voicePresenter.getMicUpdateCall(userToken, roomId, sequence, Const.IntShow.TWO);
                break;
            case 6://送礼
                onMicSendGift(userToken, name);
                break;
        }
    }

    /**
     * 进入或退出房间
     *
     * @param type 类型
     */
    @Override
    public void onChatRoom(int userId, int type) {
        if (userId == userToken) {
            switch (type) {
                case 1://进入房间
                    //进入房间成功，清除以前所以消息监听
                    MessageUtils.getInstans().removeAllChats();

                    voicePresenter.getCall(userToken, roomId);
//                    voicePresenter.getChatShow(userToken, roomId);
                    addTencentChatRoom();
                    break;
                case 2://退出房间
                    //设置没有音乐
                    Const.MusicShow.isHave = false;
                    packetNumber = 0;//退出房间红包个数清零
                    isOpenMicrophone = false;
                    isOpenReceiver = false;
                    Const.RoomId = "";
                    BroadcastManager.getInstance(VoiceActivity.this).sendBroadcast(Const.BroadCast.ROOM_MIX);
                    //停止音乐
                    MyApplication.getInstance().getWorkerThread().musicStop();
                    //退出频道（直播）
                    MyApplication.getInstance().getWorkerThread().leaveChannel(roomId);

                    //退出频道（信令）
                    YySignaling.getInstans().logout();
                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    break;
            }
        }
    }

    //进入腾讯语音聊天室

    TIMConversation timConversation;

    private void addTencentChatRoom() {
        TIMGroupManager.getInstance().applyJoinGroup(Const.roomChannelMsg, "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (!Objects.requireNonNull(VoiceActivity.this).isFinishing()) {
                    LogUtils.e(i + s);
                    //重新登录
                    if (i == 6014 && StringUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
                        String userSig = (String) SharedPreferenceUtils.get(VoiceActivity.this,
                                Const.User.USER_SIG, "");
                        onRecvUserSig(userToken + "", userSig);
                    } else {
                        showToast("进入聊天室失败，请稍后再试");
                    }
                }
            }

            @Override
            public void onSuccess() {
                LogUtils.i("加入聊天室成功");
                timConversation = TIMManager.getInstance().getConversation(
                        TIMConversationType.Group, Const.roomChannelMsg);
                timConversation.setReadMessage(timConversation.getLastMsg(), null);
            }
        });
        MyApplication.getInstance().setChatRoomMessage(chatRoomMessage);
        MyApplication.getInstance().addChatRoomMessage(chatRoomMessage1);
    }

    ChatRoomMessage chatRoomMessage1 = new ChatRoomMessage() {
        @Override
        public void onNewMessage(TIMMessage timMsg) {
            if (timMsg.getElement(0) instanceof TIMCustomElem) {
                JSONObject jsonObject = null;
                try {
                    AllMsgModel allMsgModel = MyUtils.mGson.fromJson(new String(((TIMCustomElem) timMsg.getElement(0)).getData(),
                            "UTF-8"), AllMsgModel.class);
                    if (allMsgModel.getState() == 4) {
                        setCountMsg(allMsgModel.getData());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private void setCountMsg(AllmsgBean.DataEntity data) {
        AlltopicBean alltopicBean = new AlltopicBean();
        alltopicBean.setCode(119);
        AlltopicBean.DataEntity dataEntity = new AlltopicBean.DataEntity();
        dataEntity.setUid(data.getUid());
        dataEntity.setMessageShow(data.getContent());
        dataEntity.setUserImg(data.getImgTx());
        dataEntity.setRid(data.getRid());
        List<AlltopicBean.DataEntity> list = new ArrayList<>();
        list.add(dataEntity);
        alltopicBean.setData(list);
//                        sendTencentMsg(new Gson().toJson(alltopicBean));
        setTIMMessageShow(MyUtils.mGson.toJson(alltopicBean));
    }

    ChatRoomMessage chatRoomMessage = new ChatRoomMessage() {
        @Override
        public void onNewMessage(TIMMessage timMsg) {
            LogUtils.e("timMsg----------------------");
            if (timMsg.getElement(0) instanceof TIMCustomElem) {
                JSONObject jsonObject = null;
                try {
                    String msg = new String(((TIMCustomElem) timMsg.getElement(0)).getData(), "UTF-8");
                    setTIMMessageShow(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void setTIMMessageShow(String msg) {
        MessageBean messageBean = MyUtils.mGson.fromJson(msg, MessageBean.class);
        if (messageBean.getCode() == 119) {
            LogUtils.i("平台消息");
            AlltopicBean alltopicBean = JSON.parseObject(msg, AlltopicBean.class);
//                    List<AlltopicBean.DataEntity> data = alltopicBean.getData();
            topicList.addAll(alltopicBean.getData());
            setTopicAnimation();
        } else if (messageBean.getCode() == 121) {
            GiftAllModel giftAllModel = MyUtils.mGson.fromJson(msg, GiftAllModel.class);
            ArrayList<GiftAllModel.DataBean> ral = new ArrayList<>();
            ral.add(giftAllModel.getData());
//                    showGiftAllDialog(ral);
            LogUtils.i("全服礼物通知动画");
            giftAllList.addAll(ral);
            showGiftAllAnimation();
        } else if (messageBean.getCode() == 122) {
            WinPrizeGiftModel giftAllModel = MyUtils.mGson.fromJson(msg, WinPrizeGiftModel.class);

            if (giftAllModel != null && giftAllModel.getData().getCost() >= 10000) {// 全服中奖超过10000才显示
                // showWinPrizeGiftAllDialog(giftAllModel);
                LogUtils.i("探险通知动画");
                prizeAllList.add(giftAllModel.getData());
                showPrizeGiftAllAnimation();
            }

            if (giftAllModel != null) {
                List<String> msgRids = giftAllModel.getData().getRids();
                if (msgRids != null && msgRids.size() > 0 && msgRids.contains(roomId)) {
                    chatShowChat.add(msg);
                    if (mRecyclerViewChatVoice.canScrollVertically(1)) {
                        tvHasMessageVoice.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerViewChatVoice.scrollToPosition(chatRecyclerAdapter.getItemCount() - 1);
                    }
                }
            }

        } else if (messageBean.getCode() == 888) { //更新 房间信息
//            voicePresenter.getCall(userToken, roomId);
        } else if (messageBean.getCode() == 999) { //更新 麦位信息
//            voicePresenter.getChatShow(userToken, roomId);
        }else if(messageBean.getCode() == 1001){


        chatShowChat.add(msg);
        chatRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void onRecvUserSig(String userId, String userSig) {
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM 登录成功后的回调操作，一般为跳转到应用的主页（这里的主页内容为下面章节的会话列表）
                 */
                LogUtils.e(LogUtils.TAG, "登录腾讯云成功");
                addTencentChatRoom();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e(LogUtils.TAG, errCode + "登录腾讯云失败" + errMsg);
            }
        });
    }

    /**
     * 显示用户资料
     *
     * @param userId  用户id
     * @param otherId 显示的用户id
     */
    @Override
    public void onDataShow(int userId, int otherId) {
        showMyPseronDialog(userId, otherId);
    }

    /**
     * pk时点击赠送礼物
     *
     * @param pid
     * @param buid
     */
    @Override
    public void onPkGiftSend(int pid, int buid, String userName) {
        if (userToken == buid) {
            showToast("不能给自己送礼物");
            return;
        }
        showMyGiftDialog(userToken, userToken, Const.IntShow.TWO, userName, roomBean.getState(), false);
        if (giftDialog != null) {
            giftDialog.setSendGift(new MyGiftDialog.SendGift() {
                @Override
                public void getSendGift(String ids, String names, int gid, String img, String showImg,
                                        int num, int sum, int goodGold, int sendType) {
                    int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
                    if (userGold < goodGold * num * sum) {
                        showMyDialog(getString(R.string.hint_nogold_gift),
                                Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                        if (myDialog != null) {
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
                    } else {
                        voicePresenter.getTou(userToken, pid, buid, Const.IntShow.TWO, num, gid, img, showImg, names, goodGold);
                    }
                }

                @Override
                public void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
//                    onGiftSendSuccess(userToken, ids, names, gid, img, showImg, num, goodGold);
                }
            });
        }
    }

    /**
     * 用户资料
     *
     * @param buid
     */
    @Override
    public void onPersonShow(int buid) {
        showMyPseronDialog(userToken, buid);
    }

    /**
     * 返回接口请求数据
     *
     * @param res  数据
     * @param type 类型   1 pk记录数据   2pk关闭  3判断当前用户是否关注房主,
     *             4设置和取消管理员  5踢出用户成功
     */
    @Override
    public void onCallbackShow(String res, int type) {
        switch (type) {
            case 1:
//                PkSetBean pkSetBean = JSON.parseObject(res, PkSetBean.class);
//                if (pkSetBean.getCode() == Api.SUCCESS) {
//                    pkData = pkSetBean.getData();
//                    roomPid = pkData.getId();
//                    if (pkDialog != null && pkDialog.isShowing()) {
//                        pkDialog.updateData(pkData);
//                    } else {
//                        showMyPkDialog(pkData);
//                    }
//                } else {
//                    showToast(pkSetBean.getMsg());
//                }
                break;
            case 2:
//                PkSetBean pkSetBean = JSON.parseObject(res, PkSetBean.class);
//                if (pkSetBean.getCode() == Api.SUCCESS) {
//                    pkData = pkSetBean.getData();
//                    roomPid = pkData.getId();
//                    if (pkDialog != null && pkDialog.isShowing()) {
//                        pkDialog.updateData(pkData);
//                    } else {
//                        showMyPkDialog(pkData);
//                    }
//                } else {
//                    showToast(pkSetBean.getMsg());
//                }
                break;
            case 3:
                OnlineUserBean onlineUserBean = JSON.parseObject(res, OnlineUserBean.class);
                if (onlineUserBean.getCode() == Api.SUCCESS) {
                    if (onlineUserBean.getData().getState() == 1) {
                        ChatMessageBean.DataBean dataBean = new ChatMessageBean.DataBean(goldNum,
                                "喜欢房主，就关注Ta吧  ", roomBean.getName(), roomBean.getUid());
                        ChatMessageBean chatMessageBean = new ChatMessageBean(109, dataBean);
                        String mesString = JSON.toJSONString(chatMessageBean);
                        if (!StringUtils.isEmpty(mesString)) {
                            chatShowChat.add(mesString);
//                            chatRecyclerAdapter.addData(mesString);
                        }
                    }
                }
                break;
            case 4:
                BaseBean baseBean = JSON.parseObject(res, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (myOnlineUserDialog != null && myOnlineUserDialog.isShowing()) {
                        myOnlineUserDialog.updateShow();
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
                break;
            case 5:
                showToast("踢出用户成功");

                break;


        }
    }

    MyGoldSendDialog myGoldSendDialog;

    private long sendGoldCallClickTime = 0L;

    private void showSendGoldDialog(VoiceUserBean.DataBean dataBean) {
        if (myGoldSendDialog != null && myGoldSendDialog.isShowing()) {
            myGoldSendDialog.dismiss();
        }
        int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
        myGoldSendDialog = new MyGoldSendDialog(VoiceActivity.this, dataBean, userToken);
        myGoldSendDialog.show();
        myGoldSendDialog.setRightButton(v -> {
            String number = myGoldSendDialog.getNumber();
            if (StringUtils.isEmpty(number)) {
                showToast("请输入赠送浪花数量");
                return;
            }
            try {
                int goldNum = Integer.parseInt(number);
                if (goldNum > userGold) {
                    showToast("赠送浪花数多以您拥有的数量");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e("浪花转换失败");
            }

            new TUIKitDialog(VoiceActivity.this)
                    .builder()
                    .setCancelable(true)
                    .setCancelOutside(true)
                    .setTitle("您确定送出浪花?")
                    .setDialogWidth(0.75f)
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long lastClickTime = System.currentTimeMillis();
                            if (lastClickTime - sendGoldCallClickTime > 2000) {
                                sendGoldCallClickTime = lastClickTime;
                                voicePresenter.getSendGoldCall(userToken, dataBean.getId(), number, roomId);
                            }

                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();
        });
        myGoldSendDialog.setRecordButton(v -> {
//            if (myGoldSendDialog != null && myGoldSendDialog.isShowing()) {
//                myGoldSendDialog.dismiss();
//            }
            showRecordDialog();
        });
    }

    SendRecordDialog sendRecordDialog;

    private void showRecordDialog() {
        if (sendRecordDialog != null && sendRecordDialog.isShowing()) {
            sendRecordDialog.dismiss();
        }
        sendRecordDialog = new SendRecordDialog(VoiceActivity.this, userToken);
        sendRecordDialog.show();
    }

    /**
     * 麦位赠送礼物
     *
     * @param otherId  被送礼的用户id
     * @param userName 用户昵称
     */
    @Override
    public void onMicSendGift(int otherId, String userName) {
        showMyGiftDialog(userToken, userToken, Const.IntShow.ONE, userName, roomBean.getState(), false);
        if (giftDialog != null) {
            giftDialog.setSendGift(new MyGiftDialog.SendGift() {
                @Override
                public void getSendGift(String ids, String names, int gid, String img, String showImg,
                                        int num, int sum, int goodGold, int sendType) {
                    int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
                    if (sendType == 1) { //1 是普通， 2是背包
                        if (userGold < goodGold * num * sum) {
                            showMyDialog(getString(R.string.hint_nogold_gift),
                                    Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                            if (myDialog != null) {
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
                        } else {
                            voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                        }
                    } else if (sendType == 2) {
                        voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                    }

                }

                @Override
                public void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
                    onGiftSendSuccess(userToken, ids, names, gid, img, showImg, num, goodGold);
                }
            });
        }
    }

    @Override
    public void onPersonSendGift(int otherId, String userName) {
        showMyGiftDialog(userToken, userToken, Const.IntShow.FOUR, userName, roomBean.getState(), false);
        if (giftDialog != null) {
            giftDialog.setSendGift(new MyGiftDialog.SendGift() {
                @Override
                public void getSendGift(String ids, String names, int gid,
                                        String img, String showImg, int num,
                                        int sum, int goodGold, int sendType) {
                    int userGold = (int) SharedPreferenceUtils.get(VoiceActivity.this, Const.User.GOLD, 0);
                    if (sendType == 1) {
                        if (userGold < goodGold * num * sum) {
                            showMyDialog(getString(R.string.hint_nogold_gift),
                                    Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                            if (myDialog != null) {
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
                        } else {
                            voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                        }
                    } else if (sendType == 2) {
                        voicePresenter.sendGift(userToken, roomId, ids, names, gid, img, showImg, num, sum, goodGold, sendType);
                    }

                }

                @Override
                public void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType) {
                    onGiftSendSuccess(userToken, ids, names, gid, img, showImg, num, goodGold);
                }
            });
        }
    }


    /**
     * 打开红包成功
     *
     * @param packetId
     */
    @Override
    public void onPacketOpen(int packetId) {
        if (packetDialog != null && packetDialog.isShowing()) {
            packetDialog.dismiss();
        }
        packetNumber--;
        setPacketShow();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.DATA, packetId);
        ActivityCollector.getActivityCollector().toOtherActivity(PacketActivity.class, bundle);
    }

    /**
     * 设置是否显示领取红包图标
     */
    private void setPacketShow() {
        if (isHavePacket) {
            if (packetNumber > 0) {
                ivGetpacketVoice.setVisibility(View.VISIBLE);
            } else {
                ivGetpacketVoice.setVisibility(View.GONE);
            }
        } else {
            ivGetpacketVoice.setVisibility(View.GONE);
        }
    }

    /**
     * 礼物送出成功
     *
     * @param uid
     * @param ids
     * @param gid
     * @param img
     * @param showImg
     * @param num
     */
    @Override
    public void onGiftSendSuccess(int uid, String ids, String names, int gid, String img, String showImg, int num, int goodGold) {
        GiftSendMessage.DataBean dataBean = new GiftSendMessage.DataBean(userToken, userName, goldNum,
                ids, names, gid, img, showImg, num, goodGold);
        GiftSendMessage giftSendMessage = new GiftSendMessage(101, dataBean);
        String giftString = JSON.toJSONString(giftSendMessage);
        setSendMessage(giftString, roomId, String.valueOf(userToken));
        if (giftDialog != null && giftDialog.isShowing()) {
            giftDialog.getPacketRefresh();
        }
    }

    /**
     * 添加或取消黑名单
     *
     * @param userId 被拉黑的用户id
     * @param roomId 房间id
     * @param fgid   操作者id
     * @param state  操作者房间内的状态
     * @param type   1拉黑，2揭开
     */
    @Override
    public void onBlackListAdd(int userId, String roomId, int fgid, int state, int type, String userName) {
        showMyDialog("是否将" + "<font color='#2980B9'> " + userName + " </font>" + "加入黑名单？加入后，他将无法进入此房间", 0, null);
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                voicePresenter.addRoomBlack(userId, roomId, fgid, state, type);
            }
        });
    }

    /**
     * 消息展示
     *
     * @param msgShow
     */
    @Override
    public void onMsgShow(String msgShow) {
        showToast(msgShow);
    }

    /**
     * 进入房间失败回调
     *
     * @param msgShow
     */
    @Override
    public void onJoinRoomShow(String msgShow) {
        if (!isFinishing()) {
            showMyHintDialog(msgShow);
        }
    }

    /**
     * 红爸爸赠送成功
     */
    @Override
    public void onPacketSendSuccecss() {
        showToast(getString(R.string.hint_success_packet));
//        if (rewardDialog != null && rewardDialog.isShowing()) {
//            rewardDialog.dismiss();
//        }
    }

    /**
     * 踢出用户成功
     *
     * @param position
     */
    @Override
    public void onGetOutSuccecss(int position) {
        showToast("踢出用户成功");
        if (myOnlineUserDialog != null && myOnlineUserDialog.isShowing()) {
            myOnlineUserDialog.setUserGetOut(position);
        }
    }

    @Override
    public void onGetToken(String token) {
//        initVoice(token);////初始化用户信息
    }

    /**
     * 赠送浪花
     *
     * @param dataBean
     */
    @Override
    public void onGoldSend(VoiceUserBean.DataBean dataBean) {
        showSendGoldDialog(dataBean);
    }

    ChatFragmentDialog chatDialog;

    @Override
    public void onChatTo(int userId, String userName) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ID, userId + "");
        bundle.putString(Const.ShowIntent.NAME, userName);

//        if (chatDialog!=null && chatDialog.isAdded())
//            chatDialog.dismissAllowingStateLoss();

        ActivityCollector.getActivityCollector().finishActivity(ChatActivity.class);
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(TIMConversationType.C2C);
        chatInfo.setId(String.valueOf(userId));
        chatInfo.setChatName(userName);
        bundle.putSerializable(Const.ShowIntent.CHAT_INFO, chatInfo);
        bundle.putBoolean("isRoom", true);
        ActivityCollector.getActivityCollector().toOtherActivity(ChatActivity.class, bundle);
//        chatDialog = new ChatFragmentDialog();
//        chatDialog.setArguments(bundle);
//        chatDialog.show(getSupportFragmentManager(),"chat");

    }

    /**
     * 发送麦位信息变更
     *
     * @param micData
     */
    @Override
    public void onMicDataSend(String micData) {
        LogUtils.i("发送麦位信息变更消息");
        YySignaling.getInstans().sendCHannelAttrUpdate(roomId, Const.Agora.ATTR_MICS, micData);
    }

    /**
     * 发送房间信息变更
     *
     * @param roomData
     */
    @Override
    public void onRoomDataSend(String roomData) {
        LogUtils.i("发送房间信息变更消息");
        YySignaling.getInstans().sendCHannelAttrUpdate(roomId, Const.Agora.ATTR_XGFJ, roomData);
    }

    /**
     * 发送全服礼物通知变更
     *
     * @param giftData
     */
    @Override
    public void onGiftAllSend(List<GiftAllModel.DataBean> giftData) {
//        GiftAllModel giftAllModel = new GiftAllModel();
//        giftAllModel.setCode(121);
//        giftAllModel.setData(giftData);
//        YySignaling.getInstans().sendChannelMessage(roomId,JSON.toJSONString(giftAllModel));

    }

    /**
     * 声网RtmToken回调
     */
//    @Override
//    public void getAgoraRtmToken(String agoraRtmToken) {
//        System.out.println("Rtm:"+agoraRtmToken);
//    }

    /**
     * 声网RtcToken回调
     */
    @Override
    public void getAgoraRtcToken(String responseString) {
        GetAgoraTokenBean agoraTokenBean = JSON.parseObject(responseString, GetAgoraTokenBean.class);
        GetAgoraTokenBean.AgoraToken data = agoraTokenBean.getData();
        if (data != null) {
            String token = data.getToken();
            MyApplication.getInstance().getWorkerThread().renewToken(token);
        }
    }


    /**
     * 接口返回
     *
     * @param type           1 获取全国消息数据
     *                       2发送全平台消息 3,探险的开启和关闭和等级设置
     *                       5一键全刷
     *                       6赠送礼物
     *                       7清楚房主礼物值
     *                       8转赠活动礼物
     * @param responseString
     */
    @Override
    public void onCallBack(int type, String responseString) {
        switch (type) {
            case 1:
//                AlltopicBean alltopicBean = JSON.parseObject(responseString, AlltopicBean.class);
//                if (alltopicBean.getCode() == Api.SUCCESS) {
//                    StringBuilder stringBuilder = new StringBuilder();
//                    List<AlltopicBean.DataEntity> data = alltopicBean.getData();
//                    for (AlltopicBean.DataEntity dataEntity : data) {
//                        stringBuilder.append(dataEntity.getUserName())
//                                .append("：")
//                                .append(dataEntity.getContent())
//                                .append("        ");
//                    }
//                    tvAlltopicVoice.setText(stringBuilder);
//
//                    if (data.size() > 0&& tvAlltopicVoice.getText().toString().length()>0)
//                    {
//                        tvAlltopicVoice.setText(data.get(0).getUserName() + "：" + data.get(0).getContent() + "  ");
//                        tvAlltopicVoice.requestFocus();
//                        tvAlltopicVoice.setVisibility(View.VISIBLE);
//                    }else {
//                        tvAlltopicVoice.setText(" ");
//                    }
//
//
//                } else {
//                    showToast(alltopicBean.getMsg());
//                }
                break;
            case 2:
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("消息发送成功");
                    edtInputMychat.setText("");
                    isAllChat = false;
                    setAllChatShow();
                } else if (baseBean.getCode() == Const.IntShow.TWO) {
                    showMyDialog("您的余额不足，充值后才能发送消息！",
                            Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                } else {
                    showToast(baseBean.getMsg());
                }
                break;
            case 3:
                RoomCacheBean roomCacheBean = JSON.parseObject(responseString, RoomCacheBean.class);
                if (roomCacheBean.getCode() == Api.SUCCESS) {
                    if (roomCacheBean.getData().getX() == 1) {//是否开始 1否，2 是
                        if (goldNum >= roomCacheBean.getData().getY()) {
                            ivDonghaiFishing.setVisibility(View.VISIBLE);
                        } else {
                            ivDonghaiFishing.setVisibility(View.GONE);
                        }
                    } else if (roomCacheBean.getData().getX() == 2) {
                        ivDonghaiFishing.setVisibility(View.VISIBLE);
                    }
                } else {
                    showToast(roomCacheBean.getMsg());
                }
                //第一个显示后才有资格显示第二个
                if (ivDonghaiFishing.getVisibility() == View.VISIBLE) {
                    int isOpenDm = (int) SharedPreferenceUtils.get(this, Const.User.IS_OPEN_DM, 0);
                    if (isOpenDm == 0) {
                        ivDinghaiFishing.setVisibility(View.GONE);
                    } else {
                        ivDinghaiFishing.setVisibility(View.VISIBLE);
                    }
                }
                //第二个显示后才有资格显示第三个
                if (ivDinghaiFishing.getVisibility() == View.VISIBLE) {
                    int isOpenCJD = (int) SharedPreferenceUtils.get(this, Const.User.IS_OPEN_CJD, 0);
                    if (isOpenCJD == 0) {
                        ivDeepSeaFishing.setVisibility(View.GONE);
                    } else {
                        ivDeepSeaFishing.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case 4:
                BaseBean baseBean1 = JSON.parseObject(responseString, BaseBean.class);
//                if (myGoldSendDialog != null && myGoldSendDialog.isShowing()) {
//                    myGoldSendDialog.dismiss();
//                }
                if (baseBean1.getCode() == Api.SUCCESS) {
                    showToast("赠送浪花成功");
                    myGoldSendDialog.dismiss();
                } else if (baseBean1.getCode() == Const.IntShow.TWO) {
                    showMyDialog("您的余额不足，充值后才能发送消息！",
                            Const.IntShow.THREE, getString(R.string.tv_topup_packet));
                } else {
                    showToast(baseBean1.getMsg());
                }
                break;
            case 5:
                MicXgfjBean baseBean2 = JSON.parseObject(responseString, MicXgfjBean.class);
                if (giftDialog != null) {
                    giftDialog.sendAllGiftAfter();
                    if (giftDialog.isShowing()) {
                        giftDialog.dismiss();
                    }
                }
                if (baseBean2.getCode() == Api.SUCCESS) {
                    for (MicXgfjBean.DataBean.ListDataBean listDataBean : baseBean2.getData().getMsgList()) {
                        sendTencentMsg(listDataBean.getMessageChannelSend());
                    }
                }
                break;
            case 6:
                MicXgfjBean baseBean3 = JSON.parseObject(responseString, MicXgfjBean.class);
                if (baseBean3.getCode() == Api.SUCCESS) {
                    for (MicXgfjBean.DataBean.ListDataBean listDataBean : baseBean3.getData().getMsgList()) {
                        sendTencentMsg(listDataBean.getMessageChannelSend());
                    }
                }
                break;
            case 7:
                MicXgfjBean baseBean4 = JSON.parseObject(responseString, MicXgfjBean.class);
                if (baseBean4.getCode() == Api.SUCCESS) {
                    if (StringUtils.isEmpty(baseBean4.getData().getAttr_xgfj())) {
                        return;
                    }
                    YySignaling.getInstans().sendCHannelAttrUpdate(roomId, Const.Agora.ATTR_XGFJ,
                            baseBean4.getData().getAttr_xgfj());
                }
                break;
            case 8:
                GiveGiftResultBean baseBean8 = JSON.parseObject(responseString, GiveGiftResultBean.class);
                showToast(baseBean8.getMsg());
                break;

        }
    }

    private void sendTencentMsg(String msgBean) {
        if (StringUtils.isEmpty(msgBean)) {
            return;
        }
        setTIMMessageShow(msgBean);
        LogUtils.i("发送腾讯消息", msgBean);
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,    //会话类型：群聊
                Const.roomChannelMsg);

        //构造一条消息
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[广播消息]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        //向 TIMMessage 中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgBean.getBytes());      //自定义 byte[]
        elem.setDesc("[房间消息]"); //自定义描述信息
        //将 elem 添加到消息
        if (msg.addElement(elem) != 0) {
            LogUtils.i(LogUtils.TAG, "addElement failed");
            return;
        }

        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
                LogUtils.e(i + s);
                showToast("消息发送失败" + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) { //发送消息成功
                LogUtils.i("消息发送成功");
            }
        });
    }

    MyHintDialog myHintDialog;

    private void showMyHintDialog(String msgShow) {
        if (myHintDialog != null && myHintDialog.isShowing()) {
            myHintDialog.dismiss();
        }
        myHintDialog = new MyHintDialog(VoiceActivity.this);
        myHintDialog.show();
        myHintDialog.setHintText(msgShow);
        myHintDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHintDialog != null && myHintDialog.isShowing()) {
                    myHintDialog.dismiss();
                }
                ActivityCollector.getActivityCollector().finishActivity();
            }
        });
    }


    /**
     * 获取房间信息接口（包括房主和我）
     *
     * @param responseString
     */
    @Override
    public void onCallSuccess(String responseString) {
        VoiceHomeBean voiceUserBean = JSON.parseObject(responseString, VoiceHomeBean.class);
        if (voiceUserBean.getCode() == 0) {
            VoiceHomeBean.DataBean homeData = voiceUserBean.getData();
            roomBean = homeData.getRoom();
            giftMinShow = homeData.getMinGift();

            if (roomBean.getUid() != userToken) { //判断当前用户是否是房主
                addTypeList(new VoiceTypeModel(1, 8, 10));
//                Timer timer = new Timer();
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        observable = Observable.just("0");
//                        subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Consumer<String>() {
//                                    @Override
//                                    public void accept(String s) throws Exception {
//                                        if (!isFinishing()) {
//                                            //判断用户是否关注房主
//                                            voicePresenter.getUserAttention(userToken, roomBean.getUid());
//                                        }
//                                    }
//                                });
//                    }
//                };
//                timer.schedule(timerTask, 10000);
            }

            setRoomShow();
            List<VoiceUserBean.DataBean> data = homeData.getUserModel();
            setUpdateRoom(data);
        } else {
            showToast(voiceUserBean.getMsg());
        }
    }

    /**
     * 修改房间信息
     *
     * @param data
     */
    private void setUpdateRoom(List<VoiceUserBean.DataBean> data) {
        if (data.size() > 1) {
            //房主
            VoiceUserBean.DataBean homeBean = data.get(0);
            //当前用户
            VoiceUserBean.DataBean userBean = data.get(1);
            setShow(homeBean);
            if (userBean.getId() == userToken) { //当前用户
                //当前用户
                userRoomType = userBean.getType();
                userName = userBean.getName();
//                goldNum = dataOne.getTreasureGrade();
//                SharedPreferenceUtils.put(this, Const.User.GRADE_T, goldNum);
                if (isOpenRecord) { //是否开启语音
                    roomPass = roomBean.getPassword();
                    if (userRoomType == 1) { //房主
//                        voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.ONE);
                        openRoomAnimation(userBean.getRtmToken(), userBean.getRtcToken());
                    } else {
                        if (StringUtils.isEmpty(roomPass) || Const.RoomId.equals(roomId)) {
                            //密码为空或者进入过房间，直接进入
//                            voicePresenter.getChatRoom(userToken, roomId, Const.IntShow.ONE);
                            openRoomAnimation(userBean.getRtmToken(), userBean.getRtcToken());
                        } else {
                            showMyPasswordDialog(userBean.getRtmToken(), userBean.getRtcToken());
                        }
                    }
                } else {
                    showToast("请在应用权限页面开启语音录制权限");
                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                }
            }
        }
    }

    /**
     * 错误处理
     *
     * @param code
     * @param msg
     */
    @Override
    public void onError(int code, String msg) {

    }

    @Override
    public BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    public void updateUnread(int count) {
        if (count <= 0) {
            ivEnvelopeVoice.setImageResource(R.drawable.bottom_envelope);
        } else {
            ivEnvelopeVoice.setImageResource(R.drawable.bottom_envelope1);
        }
    }

    @Override
    public void updateGpState(int type) {
        roomBean.setIsGp(type);
    }
}
