package com.siyecaoi.yy.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.adapter.FragPagerAdapter;
import com.siyecaoi.yy.agora.YySignaling;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.control.ChatMessage;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.find.fragment.RadioDatingOneFragment;
import com.siyecaoi.yy.ui.home.fragment.HomeFragment;
import com.siyecaoi.yy.ui.login.LoginActivity;
import com.siyecaoi.yy.ui.message.fragment.MessageFragment;
import com.siyecaoi.yy.ui.mine.fragment.MineFragment;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.GPSUtils;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.MessageUtils;
import com.siyecaoi.yy.utils.MyUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.view.MyViewPager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;

import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;
import io.agora.rtc.utils.ThreadUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 主页
 */
public class MainActivity extends MyBaseActivity implements ConversationManagerKit.MessageUnreadWatcher {


    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.tv_find)
    TextView tvFind;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.frame_main)
    FrameLayout frameMain;
    @BindView(R.id.tv_roomname_main)
    TextView tvRoomnameMain;
    @BindView(R.id.tv_roomid_main)
    TextView tvRoomidMain;
    @BindView(R.id.iv_roomshow_main)
    ImageView ivRoomshowMain;
    @BindView(R.id.iv_closeroom_main)
    ImageView ivCloseroomMain;
    @BindView(R.id.rl_room_main)
    RelativeLayout rlRoomMain;
    @BindView(R.id.ll_bottom_main)
    LinearLayout llBottomMain;
    @BindView(R.id.tv_number_main)
    TextView tvNumberMain;

    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.vdh_main)
    RelativeLayout vdhMain;
    @BindView(R.id.myViewpager_main)
    MyViewPager myViewpagerMain;


    private ArrayList<Fragment> fragmentList;//fragment集合
    int indexShow;//当前显示的fragment
    HomeFragment homeFragment;
    RadioDatingOneFragment friendsFragment;
    //    FindFragment findFragment;
    MessageFragment messageFragment;
    MineFragment mineFragment;

    FragPagerAdapter fragPagerAdapter;

    int urlType;
    String urlHtml;

    Timer timerOnline;//每5分钟调用一次（保持在线）

    //消息回调
    private Consumer<String> consumer;
    Observable<String> observable;
    Disposable subscribe;

    @Override
    public void initData() {
        fragmentList = new ArrayList<>();
        urlType = getBundleInt(Const.ShowIntent.URLTYPE, 1);
        urlHtml = getBundleString(Const.ShowIntent.URL);
        Bundle bundle = new Bundle();
        switch (urlType) {
            case 2:
                MyUtils.getInstans().toWebView(this, urlHtml);
                break;
            case 3:
                bundle.putString(Const.ShowIntent.URL, urlHtml);
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case 4:
                bundle.putString(Const.ShowIntent.ROOMID, urlHtml);
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                break;
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        setBlcakShow(true);
        showHeader(false);
        showTitle(false);

//        setfrag();
        setViewPager();
        setRoomTouch();
        initBroadCast();

        setMegisRead();

        setRxPermission();
        setUserInput();
        showOne(indexShow);

        if (userToken != 0) {
            usetLoginTime();
            getUserCall();
        }
        setTimerOnline();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (userToken != 0) {
            getUserCall();
        }
    }

    public LinearLayout getHomeMainTab() {
        return llBottomMain;
    }


    private void getUserCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.getUserInfo, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    initShared(userBean.getData());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initShared(UserBean.DataBean dataBean) {
        new Thread(){
            @Override
            public void run() {
                SharedPreferenceUtils.put(MainActivity.this, Const.User.USER_TOKEN, dataBean.getId());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.AGE, dataBean.getAge());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.IMG, dataBean.getImgTx());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.SEX, dataBean.getSex());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.NICKNAME, dataBean.getNickname());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.ROOMID, dataBean.getUsercoding());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.CharmGrade, dataBean.getCharmGrade());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.DATEOFBIRTH, dataBean.getDateOfBirth());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.FansNum, dataBean.getFansNum());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.AttentionNum, dataBean.getAttentionNum());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.GOLD, dataBean.getGold());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.GoldNum, dataBean.getGoldNum());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.GRADE_T, dataBean.getTreasureGrade());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.PHONE, dataBean.getPhone());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.QQSID, dataBean.getQqSid());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.WECHATSID, dataBean.getWxSid());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.Ynum, dataBean.getYnum());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.Yuml, dataBean.getYuml());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.HEADWEAR_H, dataBean.getUserThfm());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.CAR_H, dataBean.getUserZjfm());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.HEADWEAR, dataBean.getUserTh());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.CAR, dataBean.getUserZj());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.IS_AGENT_GIVE, dataBean.getIsAgentGive());
                SharedPreferenceUtils.put(MainActivity.this, Const.User.IS_OPEN_DM, dataBean.isOpenDm);
                SharedPreferenceUtils.put(MainActivity.this, Const.User.IS_OPEN_CJD, dataBean.isOpenCjd);
                SharedPreferenceUtils.put(MainActivity.this, Const.User.IS_FANG_DING_HAO, dataBean.getIsFangDingHao());

            }
        }.start();

    }

    //频道消息回调(所有信息)
    ChatMessage chatMessage = new ChatMessage() {
        @SuppressLint("CheckResult")
        @Override
        public void setMessageShow(String channelID, String account, int uid, final String msg) {
//            chatRecyclerAdapter.addData(msg);
            if (channelID.equals(Const.RoomId)) {
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

        }
    };

    /**
     * 回调消息处理
     */
    private void initConsumer() {
        consumer = s -> {
//            MessageBean messageBean = new Gson().fromJson(s, MessageBean.class);
            if (Const.chatShow == null) {
                Const.chatShow = new ArrayList<>();
            }
            Const.chatShow.add(s);
            if (Const.chatShow.size() > 1000) {
                Const.chatShow.remove(0);
            }
        };
    }

    /**
     * 进入房间后每5分钟调用一次
     */
    private void setTimerOnline() {
        timerOnline = new Timer();
        TimerTask timerTask = new TimerTask() {
            @SuppressLint("CheckResult")
            @Override
            public void run() {
                Observable.just("0").observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if (!isFinishing()) {
                                    if (!StringUtils.isEmpty(Const.RoomId)) {
                                        getOnline(userToken, Const.RoomId);
                                    }
                                }
                            }
                        });
            }
        };
        timerOnline.schedule(timerTask, 1000, 100000);
    }

    /**
     * 房间在线状态
     *
     * @param userToken
     * @param roomId
     */
    private void getOnline(int userToken, String roomId) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("pid", roomId);
        HttpManager.getInstance().post(Api.addTime, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {

            }
        });
    }

    private void setViewPager() {
        homeFragment = new HomeFragment();
        friendsFragment = new RadioDatingOneFragment();
//        findFragment = new FindFragment();
        messageFragment = new MessageFragment();
        mineFragment = new MineFragment();

        fragmentList.add(homeFragment);
        fragmentList.add(friendsFragment);
//        fragmentList.add(findFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(mineFragment);

        fragPagerAdapter = new FragPagerAdapter(getSupportFragmentManager());
        fragPagerAdapter.setList_title(new ArrayList<>());
        fragPagerAdapter.setList_fragment(fragmentList);
        myViewpagerMain.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();
//        mTabLayout.setViewPager(mViewPager);
        myViewpagerMain.setOffscreenPageLimit(fragmentList.size());
        myViewpagerMain.setCurrentItem(0);
    }

    private void setRxPermission() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    GPSUtils.getInstance(MainActivity.this).getLngAndLat(new GPSUtils.OnLocationResultListener() {
                        @Override
                        public void onLocationResult(Location location) {
                            MyApplication.lat = location.getLatitude();
                            MyApplication.lon = location.getLongitude();
                            Log.e("mmp", "经纬度：" + MyApplication.lat + ";" + MyApplication.lon);
                        }

                        @Override
                        public void OnLocationChange(Location location) {

                        }
                    });
                } else {
                    showToast("请前往设置页面打开定位权限，否则部分功能不可用。");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 设置用户属性
     */
    private void setUserInput() {
        String loginUser = TIMManager.getInstance().getLoginUser();
        if (loginUser != null && loginUser.equals(String.valueOf(userToken))) {
            HashMap<String, Object> profileMap = new HashMap<>();
            String nickName = (String) SharedPreferenceUtils.get(this, Const.User.NICKNAME, "");
            String img = (String) SharedPreferenceUtils.get(this, Const.User.IMG, "");
            profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, nickName);
            profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, img);
            TIMFriendshipManager.getInstance().modifySelfProfile(profileMap, new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    LogUtils.e(i + s);
                }

                @Override
                public void onSuccess() {
                    LogUtils.e("设置用户资料成功");
                }
            });
        }
    }

    private void setMegisRead() {
        // 未读消息监视器
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
    }

    //获取用户最后登录的时间
    private void usetLoginTime() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.Logbook, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    if (getOneBean.getData() != null) {
                        long timeEnd = MyUtils.getInstans().getLongTime(getOneBean.getData().getTime());
                        long timeNow = System.currentTimeMillis();
                        long days = (timeNow - timeEnd) / (1000 * 60 * 60 * 24);
                    }
                }
            }
        });
    }


    private void initBroadCast() {
        BroadcastManager.getInstance(this).addAction(Const.BroadCast.ROOM_MIX, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setShow();
            }
        });

        BroadcastManager.getInstance(this).addAction(Const.BroadCast.EXIT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                logout();
            }
        });
    }


    private void logout() {
        showDialog();
        //登出
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {

                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                LogUtils.d(LogUtils.TAG, "logout failed. code: " + code + " errmsg: " + desc);

            }

            @Override
            public void onSuccess() {
                //登出成功
            }
        });
        setExit();
    }

    private void setExit() {
        MyApplication.getInstance().getWorkerThread().musicStop();
        MyApplication.getInstance().getWorkerThread().leaveChannel(Const.RoomId);
        Const.RoomId = "";
        YySignaling.getInstans().logout();//退出声网信令系统
//        MyApplication.getInstance().deInitWorkerThread();//退出声网直播系统
        dismissDialog();
        SharedPreferenceUtils.clear(this);
        SharedPreferenceUtils.put(this, Const.User.UUID, MyUtils.getInstans().getUuid(this));
        ActivityCollector.getActivityCollector().toOtherActivity(LoginActivity.class);
        ActivityCollector.getActivityCollector().finishAllBaseActivity();
    }

    @SuppressLint("SetTextI18n")
    private void setShow() {
        if (StringUtils.isEmpty(Const.RoomId)) {
            //清除消息监听
            MessageUtils.getInstans().removeChatShows(chatMessage);
//            if (Const.chatShow != null) {
//                Const.chatShow.clear();//清除聊天信息
//            }
            rlRoomMain.setVisibility(View.GONE);
            return;
        }
        //如果是最小化则注册消息监听，监听房间消息
        MessageUtils.getInstans().addChatShows(chatMessage);
        rlRoomMain.setVisibility(View.VISIBLE);
        if (!isSetRlRoom) {
            isSetRlRoom = true;
            setRlRoomShow();
        }

        tvRoomnameMain.setText(Const.RoomName);
        tvRoomidMain.setText(Const.RoomNum);

//        if (StringUtils.isEmpty(Const.RoomIdLiang)) {
//            tvRoomidMain.setText("ID：" + Const.RoomId);
//        } else {
//            tvRoomidMain.setText("ID：" + Const.RoomIdLiang);
//        }

        ImageUtils.loadImage(ivRoomshowMain, Const.RoomImg,-1,R.drawable.default_head);

    }

    boolean isSetRlRoom;

    /**
     * 初始化弹窗位置
     */
    private void setRlRoomShow() {
        RelativeLayout.LayoutParams rlRoomMainLayoutParams = (RelativeLayout.LayoutParams) rlRoomMain.getLayoutParams();
        Rect rect = new Rect();
        //获取当前界面可视部分
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int screenHeight = rect.bottom;
        int screenWidth = rect.width();
        rlRoomMainLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlRoomMainLayoutParams.leftMargin = screenWidth - MyUtils.getInstans().dp2px(this, 180);
        rlRoomMainLayoutParams.topMargin = screenHeight - MyUtils.getInstans().dp2px(this, 200);
        rlRoomMain.setLayoutParams(rlRoomMainLayoutParams);
        rlRoomMain.postInvalidate();
    }

    private int lastX, lastY;
    int dx, dy;
    private boolean isMove;

    @SuppressLint("ClickableViewAccessibility")
    private void setRoomTouch() {
        rlRoomMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMove = false;
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
                        }
                        if (t < 100) {
                            t = 100;
                        }
                        if (l > vdhMain.getWidth() - v.getWidth()) {
                            l = vdhMain.getWidth() - v.getWidth();
                        }
                        if (t > vdhMain.getHeight() - v.getHeight() - 80) {
                            t = vdhMain.getHeight() - v.getHeight() - 80;
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
//                LogUtils.e("dx" + dx + " dy" + dy);
                if (isMove) {
                    return true;
                } else {
                    if (Math.abs(dx) > 3 || Math.abs(dy) > 3) {
                        isMove = true;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
    }

//    /**
//     * 初始化fragment
//     */
//    private void setfrag() {
//        if (homeFragment == null) {
//            homeFragment = new HomeFragment();
//            homeFragment.setUserVisibleHint(true);
//            tvHome.setSelected(true);
//        }
//
//        switchfragment(homeFragment);
//    }

//    private void switchfragment(Fragment target) {
//        // add show hide的方式
//        if (fragmentList == null) {
//            return;
//        }
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        for (Fragment fragment : fragmentList) {
//            transaction.hide(fragment);
//        }
//        if (target.isAdded()) {
//            // 如果已经添加到FragmentManager里面，就展示
//            transaction.show(target);
//        } else {
//            // 为了方便找到Fragment，我们是可以设置Tag
//            String tag;
//            // 把类名作为tag
//            tag = target.getClass().getName();
//            // 添加Fragment并设置Tag
//            transaction.add(R.id.frame_main, target, tag);
//        }
//        transaction.commit();
//        if (!fragmentList.contains(target)) {
//            fragmentList.add(target);
//        }
//    }

    @Override
    public void setResume() {


    }


    @Override
    public void setOnclick() {

    }

    //退出
    private long clickTime = 0;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(this).destroy(Const.BroadCast.ROOM_MIX);
        if (timerOnline != null) {
            timerOnline.cancel();
        }
        super.onDestroy();
    }

    @OnClick({R.id.tv_home, R.id.tv_find, R.id.tv_message, R.id.tv_mine,
            R.id.rl_room_main, R.id.iv_closeroom_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_home:
                hideOne(indexShow);
                indexShow = 0;
                showOne(indexShow);
                setBlcakShow(true);
                break;
            case R.id.tv_find:
                hideOne(indexShow);
                indexShow = 1;
                showOne(indexShow);
                setBlcakShow(false);
                break;
            case R.id.tv_message:
                hideOne(indexShow);
                indexShow = 2;
                showOne(indexShow);
                setBlcakShow(true);
                break;
            case R.id.tv_mine:
                hideOne(indexShow);
                indexShow = 3;
                showOne(indexShow);
                setBlcakShow(true);
                break;
            case R.id.rl_room_main:
                rlRoomMain.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString(Const.ShowIntent.ROOMID, Const.RoomId);
                bundle.putBoolean(Const.ShowIntent.ROLL_BOTTOM, true);
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                break;
            case R.id.iv_closeroom_main:
                if (StringUtils.isEmpty(Const.RoomId)) {
                    rlRoomMain.setVisibility(View.GONE);
                    return;
                }
                //退出频道（直播）
                getExitRoomCall();
                break;
        }
    }


    private void getExitRoomCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("pid", Const.RoomId);
        map.put("type", Const.IntShow.TWO);
        HttpManager.getInstance().post(Api.chatRoom, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    Const.MusicShow.isHave = false;
                    MyApplication.getInstance().getWorkerThread().musicStop();
                    MyApplication.getInstance().getWorkerThread().leaveChannel(Const.RoomId);
                    rlRoomMain.setVisibility(View.GONE);
                    Const.RoomId = "";
                    Const.packetNumber = 0;
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    //显示当前选中fragment
    private void showOne(int indexShow) {
        if (indexShow == 0) {
            tvHome.setSelected(true);
            tvHome.setTextColor(ContextCompat.getColor(this, R.color.nav_selct_color));
            if (homeFragment == null) {
                homeFragment = new HomeFragment();
                homeFragment.setUserVisibleHint(true);
            }
            homeFragment.setMainTab();
//            switchfragment(homeFragment);
            myViewpagerMain.setCurrentItem(indexShow);
        } else if (indexShow == 1) {
            tvFind.setSelected(true);
            tvFind.setTextColor(ContextCompat.getColor(this, R.color.nav_selct_color));
            if (friendsFragment == null) {
                friendsFragment = new RadioDatingOneFragment();
                friendsFragment.setUserVisibleHint(true);
            }
//            switchfragment(findFragment);
            myViewpagerMain.setCurrentItem(indexShow);
        } else if (indexShow == 2) {
            tvMessage.setSelected(true);
            tvMessage.setTextColor(ContextCompat.getColor(this, R.color.nav_selct_color));
            if (messageFragment == null) {
                messageFragment = new MessageFragment();
                messageFragment.setUserVisibleHint(true);
            }
//            switchfragment(messageFragment);
            myViewpagerMain.setCurrentItem(indexShow);
        } else if (indexShow == 3) {
            tvMine.setSelected(true);
            tvMine.setTextColor(ContextCompat.getColor(this, R.color.nav_selct_color));
            if (mineFragment == null) {
                mineFragment = new MineFragment();
                mineFragment.setUserVisibleHint(true);
            }
//            switchfragment(mineFragment);
            myViewpagerMain.setCurrentItem(indexShow);
        }
    }


    private void hideOne(int indexShow) {
        switch (indexShow) {
            case 0:
                tvHome.setSelected(false);
                tvHome.setTextColor(ContextCompat.getColor(this, R.color.nav_noselct_color));
                break;
            case 1:
                tvFind.setSelected(false);
                tvFind.setTextColor(ContextCompat.getColor(this, R.color.nav_noselct_color));
                break;
            case 2:
                tvMessage.setSelected(false);
                tvMessage.setTextColor(ContextCompat.getColor(this, R.color.nav_noselct_color));

                break;
            case 3:
                tvMine.setSelected(false);
                tvMine.setTextColor(ContextCompat.getColor(this, R.color.nav_noselct_color));

                break;
        }
    }

    /**
     * 会话未读计数变化监听
     */
    @Override
    public void updateUnread(int count) {
        LogUtils.e("未读消息数" + count);
        if (count <= 0) {
            tvNumberMain.setVisibility(View.INVISIBLE);
        } else {
            tvNumberMain.setVisibility(View.VISIBLE);
        }
        String unreadStr = "" + count;
        if (count > 99) {
            unreadStr = "99+";
        }
        tvNumberMain.setText(unreadStr);
    }
}
