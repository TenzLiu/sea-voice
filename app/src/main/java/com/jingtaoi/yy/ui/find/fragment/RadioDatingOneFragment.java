package com.jingtaoi.yy.ui.find.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyApplication;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.ChatRoomMsgBean;
import com.jingtaoi.yy.bean.GetOneBean;
import com.jingtaoi.yy.control.ChatRoomMessage;
import com.jingtaoi.yy.control.VirifyCountDownTimer;
import com.jingtaoi.yy.model.ChatRoomMsgModel;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.find.adapter.RadioOneRecyAdapter;
import com.jingtaoi.yy.ui.message.ChatActivity;
import com.jingtaoi.yy.ui.message.OtherHomeActivity;
import com.jingtaoi.yy.ui.mine.PersonHomeActivity;
import com.jingtaoi.yy.ui.room.dialog.OtherShowDialog;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.MyUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
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
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 广播交友
 */
public class RadioDatingOneFragment extends MyBaseFragment {


    @BindView(R.id.mRecyclerView_radiodating)
    RecyclerView mRecyclerViewRadiodating;
    @BindView(R.id.tv_newdata_radiodating)
    TextView tvNewdataRadiodating;
    @BindView(R.id.edt_input_radiodating)
    EditText edtInputRadiodating;
    @BindView(R.id.btn_send_radiodating)
    Button btnSendRadiodating;
    @BindView(R.id.ll_chat_radiodating)
    LinearLayout llChatRadiodating;
    Unbinder unbinder;

    List<TIMMessage> msgs;
    RadioOneRecyAdapter radioOneRecyAdapter;

    VirifyCountDownTimer virifyCountDownTimer;

    List<String> noShowString;//敏感词汇

    TIMConversation timConversation;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int type = 0;
        Bundle arguments = getArguments();
        if (arguments!=null){
            type = getArguments().getInt("type",0); //0:广播交友 1:公聊大厅
        }
        return inflater.inflate(type == 0?R.layout.fragment_radiodatingone:R.layout.fragment_radiodating_voice, container, false);
    }

    @Override
    public void initView() {

        msgs = new ArrayList<>();
        setRecycler();

        if (Const.msgSendTime != 0) { //判断是否可以发送消息
            long timeEnd = System.currentTimeMillis() - Const.msgSendTime;
            if (timeEnd > 60000) {
                Const.msgSendTime = 0;
            } else {
                setEditShow(60000 - timeEnd);
            }
        }
        addOnMessageCall();
        getCall();
        try {
            noShowString = readFile02("ReservedWords-utf8.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCall();
    }

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

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.BroadList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                ChatRoomMsgBean chatRoomMsgBean = JSON.parseObject(responseString, ChatRoomMsgBean.class);
                if (chatRoomMsgBean.getCode() == Api.SUCCESS) {
                    List<ChatRoomMsgModel.DataBean> data = chatRoomMsgBean.getData();
                    Collections.reverse(data);//对列表倒叙
                    if (radioOneRecyAdapter != null) {
                        radioOneRecyAdapter.setNewData(data);
                        if (mRecyclerViewRadiodating != null) {
                            mRecyclerViewRadiodating.scrollToPosition(radioOneRecyAdapter.getItemCount() - 1);
                        }
                    }

                } else {
                    showToast(chatRoomMsgBean.getMsg());
                }
            }
        });
    }

    /**
     * 更新页面显示
     *
     * @param millisInfuture 倒计时毫秒值
     */
    private void setEditShow(long millisInfuture) {

        edtInputRadiodating.setText("");
        MyUtils.getInstans().hideKeyboard(edtInputRadiodating);
        virifyCountDownTimer =
                new VirifyCountDownTimer(btnSendRadiodating, getString(R.string.tv_send),
                        millisInfuture, 1000, timerFinish);
        virifyCountDownTimer.start();
        btnSendRadiodating.setAlpha(0.5f);
    }

    VirifyCountDownTimer.TimerFinish timerFinish = new VirifyCountDownTimer.TimerFinish() {
        @Override
        public void onFinish() {
            btnSendRadiodating.setAlpha(1.0f);
        }
    };

    private void addOnMessageCall() {
//        TIMGroupManager.CreateGroupParam createGroupParam = new TIMGroupManager.CreateGroupParam("AVChatRoom", "广播交友");
//        TIMGroupManager.getInstance().createGroup(createGroupParam, new TIMValueCallBack<String>() {
//            @Override
//            public void onError(int i, String s) {
//                LogUtils.e(i+s);
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                LogUtils.e(s+"广播交友");
//            }
//        });
        TIMGroupManager.getInstance().applyJoinGroup(Const.chatRoom, "", new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (!Objects.requireNonNull(getActivity()).isFinishing()) {
                    LogUtils.e(i + s);
                    //重新登录
                    if (i == 6014 && StringUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
                        String userSig = (String) SharedPreferenceUtils.get(getContext(), Const.User.USER_SIG, "");
                        onRecvUserSig(userToken + "", userSig);
                    } else {
                        showToast("进入聊天室失败，请稍后再试");
                    }
                }
            }

            @Override
            public void onSuccess() {
                LogUtils.e("加入聊天室成功");
                setMegReaded();
            }
        });
        MyApplication.getInstance().addChatRoomMessage(chatRoomMessage);
    }

    private void setMegReaded() {
        timConversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group, Const.chatRoom);
        timConversation.setReadMessage(timConversation.getLastMsg(),null);
    }

    private void onRecvUserSig(String userId, String userSig) {
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM 登录成功后的回调操作，一般为跳转到应用的主页（这里的主页内容为下面章节的会话列表）
                 */
                LogUtils.e(LogUtils.TAG, "登录腾讯云成功");
                addOnMessageCall();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e(LogUtils.TAG, errCode + "登录腾讯云失败" + errMsg);
            }
        });
    }

    ChatRoomMessage chatRoomMessage = new ChatRoomMessage() {
        @Override
        public void onNewMessage(TIMMessage timMsg) {
            msgs.add(timMsg);
            if (timMsg.getElement(0) instanceof TIMCustomElem) {
                JSONObject jsonObject = null;
                try {
                    ChatRoomMsgModel chatRoomMsgModel = new Gson().fromJson(new String(((TIMCustomElem) timMsg.getElement(0)).getData(),
                            "UTF-8"), ChatRoomMsgModel.class);
                    if (chatRoomMsgModel.getState() == 3) {
                        radioOneRecyAdapter.addData(chatRoomMsgModel.getData());

                        if (mRecyclerViewRadiodating != null && mRecyclerViewRadiodating.canScrollVertically(1)) {
                            tvNewdataRadiodating.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerViewRadiodating.scrollToPosition(radioOneRecyAdapter.getItemCount() - 1);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void setRecycler() {
        radioOneRecyAdapter = new RadioOneRecyAdapter(R.layout.item_list_radioda);
        radioOneRecyAdapter.setUserId(userToken);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewRadiodating.setLayoutManager(layoutManager);
        mRecyclerViewRadiodating.setAdapter(radioOneRecyAdapter);

        radioOneRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_header_radioda:
                        ChatRoomMsgModel.DataBean dataBean = (ChatRoomMsgModel.DataBean) adapter.getItem(position);
                        Bundle bundle = new Bundle();
                        assert dataBean != null;
                        bundle.putInt(Const.ShowIntent.ID, dataBean.getUid());
                        if (dataBean.getUid() == userToken) {
                            ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                        } else {

                            showOtherDialog(userToken,dataBean.getUid());
//                            ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
                        }
                        break;
                    case R.id.iv_user_header_radioda:
                        ChatRoomMsgModel.DataBean dataBean2 = (ChatRoomMsgModel.DataBean) adapter.getItem(position);
                        Bundle bundle2 = new Bundle();
                        assert dataBean2 != null;
                        bundle2.putInt(Const.ShowIntent.ID, dataBean2.getUid());
                        ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle2);

                        break;

                }
            }
        });

        mRecyclerViewRadiodating.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;
                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if (lastPosition >= recyclerView.getLayoutManager().getItemCount() - 1) {
                    tvNewdataRadiodating.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }


    //其他位置点击弹窗
    OtherShowDialog otherShowDialog;

    private void showOtherDialog(int userId, int otherId) {
        if (otherShowDialog != null && otherShowDialog.isShowing()) {
            otherShowDialog.dismiss();
        }
        ArrayList<String> seatList = new ArrayList<>();
        seatList.add(getString(R.string.tv_message_bottomshow));
        seatList.add(getString(R.string.tv_sixin));
        otherShowDialog = new OtherShowDialog(getContext(), seatList, userId, otherId);
        otherShowDialog.show();
        otherShowDialog.setOnItemClickListener((position, user) -> {
            switch (position) {
                case 0://查看资料
                    onDataShow(userId, user.getId());


                    break;
                case 8://私信
                   onChatTo(user.getId(),user.getName());
                    break;
            }
        });
    }

    private void onChatTo(int userId, String userName) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ID, userId + "");
        bundle.putString(Const.ShowIntent.NAME, userName);
        ActivityCollector.getActivityCollector().finishActivity(ChatActivity.class);
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(TIMConversationType.C2C);
        chatInfo.setId(String.valueOf(userId));
        chatInfo.setChatName(userName);
        bundle.putSerializable(Const.ShowIntent.CHAT_INFO, chatInfo);
        bundle.putBoolean("isRoom", true);
        ActivityCollector.getActivityCollector().toOtherActivity(ChatActivity.class, bundle);
    }

    private void onDataShow(int userId, int otherId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Const.ShowIntent.ID, otherId);
        if (otherId == userId) {
            ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
        } else {
            ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (virifyCountDownTimer != null) {
            virifyCountDownTimer.cancel();
        }
        MyApplication.getInstance().delChatRoomMessage(chatRoomMessage);
    }

    @OnClick({R.id.tv_newdata_radiodating, R.id.btn_send_radiodating})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_newdata_radiodating:
                tvNewdataRadiodating.setVisibility(View.GONE);
                mRecyclerViewRadiodating.scrollToPosition(radioOneRecyAdapter.getItemCount() - 1);
                break;
            case R.id.btn_send_radiodating:
                String mesShow = edtInputRadiodating.getText().toString();
                if (StringUtils.isEmpty(mesShow)) {
                    showToast("请输入聊天内容");
                    return;
                }
                if (noShowString != null) {
                    for (String noShow : noShowString) {
                        if (noShow.equals(mesShow)) {
                            showToast("您输入的内容带有敏感词汇");
                            return;
                        }
                    }
                }
                btnSendRadiodating.setClickable(false);
                getMegCall(mesShow);
                break;
        }
    }

    private void getMegCall(String mesShow) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("content", mesShow);
        HttpManager.getInstance().post(Api.Broad, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GetOneBean baseBean = JSON.parseObject(responseString, GetOneBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (baseBean.getData().getState() == 1) {
                        btnSendRadiodating.setClickable(true);
                        showToast("发广播需要在房间的麦上哦~");
                    } else if (baseBean.getData().getState() == 2) {
                        Const.msgSendTime = System.currentTimeMillis();
                        setEditShow(15000);
                        setMesgSend(mesShow);
                    }
                } else {
                    showToast(baseBean.getMsg());
                    btnSendRadiodating.setClickable(true);
                }
            }
        });
    }

    private void setMesgSend(String mesShow) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,    //会话类型：群聊
                Const.chatRoom);

        /**
         * 构建消息体
         */
        int gradeShow = (int) SharedPreferenceUtils.get(getContext(), Const.User.GRADE_T, 0);
        int charmGrade = (int) SharedPreferenceUtils.get(getContext(), Const.User.CharmGrade, 0);
        int sex = (int) SharedPreferenceUtils.get(getContext(), Const.User.SEX, 0);
        String name = (String) SharedPreferenceUtils.get(getContext(), Const.User.NICKNAME, "");
        String header = (String) SharedPreferenceUtils.get(getContext(), Const.User.IMG, "");
        ChatRoomMsgModel.DataBean dataBean = new ChatRoomMsgModel.DataBean();
        dataBean.setGrade(gradeShow);
        dataBean.setCharm(charmGrade);
        dataBean.setUid(userToken);
        dataBean.setMessageShow(mesShow);
        dataBean.setName(name);
        dataBean.setSex(sex);
        dataBean.setHeader(header);
        ChatRoomMsgModel chatRoomMsgModel = new ChatRoomMsgModel(113, dataBean, 3);

        //构造一条消息
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[广播消息]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        String msgShow = JSON.toJSONString(chatRoomMsgModel);
        //向 TIMMessage 中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgShow.getBytes());      //自定义 byte[]
        elem.setDesc("[广播消息]"); //自定义描述信息
        //将 elem 添加到消息
        if (msg.addElement(elem) != 0) {
            LogUtils.d(LogUtils.TAG, "addElement failed");
            return;
        }
//        MessageInfo messageInfo = new MessageInfo();
//        messageInfo.setTIMMessage(msg);
//        messageInfo.setMsgType(MessageInfo.MSG_TYPE_SHARE);
//        chatPanel.sendMessage(messageInfo);

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
                radioOneRecyAdapter.addData(dataBean);
                mRecyclerViewRadiodating.scrollToPosition(radioOneRecyAdapter.getItemCount() - 1);
            }
        });
    }
}
