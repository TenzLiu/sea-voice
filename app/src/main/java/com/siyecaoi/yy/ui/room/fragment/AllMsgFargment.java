package com.siyecaoi.yy.ui.room.fragment;

import android.annotation.SuppressLint;
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
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.AllMsgSendBean;
import com.siyecaoi.yy.bean.AllmsgBean;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.control.ChatRoomMessage;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.model.AllMsgModel;
import com.siyecaoi.yy.model.ChatRoomMsgModel;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.message.ChatActivity;
import com.siyecaoi.yy.ui.message.OtherHomeActivity;
import com.siyecaoi.yy.ui.mine.PersonHomeActivity;
import com.siyecaoi.yy.ui.room.AllMsgActivity;
import com.siyecaoi.yy.ui.room.TopupActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.ui.room.adapter.AllMsgAdapter;
import com.siyecaoi.yy.ui.room.dialog.OtherShowDialog;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
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
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import org.json.JSONArray;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;

public class AllMsgFargment extends MyBaseFragment {
    @BindView(R.id.mRecyclerView_allmsg)
    RecyclerView mRecyclerViewAllmsg;
    @BindView(R.id.tv_newdata_allmsg)
    TextView tvNewdataAllmsg;
    @BindView(R.id.edt_input_allmsg)
    EditText edtInputAllmsg;
    @BindView(R.id.tv_gold_allmsg)
    TextView tvGoldAllmsg;
    @BindView(R.id.btn_send_allmsg)
    Button btnSendAllmsg;
    @BindView(R.id.ll_chat_radiodating)
    LinearLayout llChatRadiodating;
    AllMsgAdapter adapter;

    List<String> noShowString;//敏感词汇

    Unbinder unbinder;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_allmsg, container, false);
    }

    @Override
    public void initView() {

        setRecycler();
        addOnMessageCall();
        getCall();
        getGoldCall();
        try {
            noShowString = readFile02("ReservedWords-utf8.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = jsonObject.optJSONArray("data");
    }

    private void addOnMessageCall() {
//        TIMGroupManager.CreateGroupParam createGroupParam = new TIMGroupManager.CreateGroupParam("AVChatRoom", "全国消息");
//        TIMGroupManager.getInstance().createGroup(createGroupParam, new TIMValueCallBack<String>() {
//            @Override
//            public void onError(int i, String s) {
//                LogUtils.e("全国消息" + i + s);
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                LogUtils.e(s + "全国消息");
//            }
//        });
        TIMGroupManager.getInstance().applyJoinGroup(Const.allMsgRoom, "", new TIMCallBack() {
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


    private void setMegReaded() {
        TIMConversation timConversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group, Const.allMsgRoom);
        timConversation.setReadMessage(timConversation.getLastMsg(), null);
    }

    ChatRoomMessage chatRoomMessage = new ChatRoomMessage() {
        @Override
        public void onNewMessage(TIMMessage timMsg) {
//                msgs.add(timMsg);
            if (timMsg.getElement(0) instanceof TIMCustomElem) {
                JSONObject jsonObject = null;
                try {
                    AllMsgModel allMsgModel = new Gson().fromJson(new String(((TIMCustomElem) timMsg.getElement(0)).getData(),
                            "UTF-8"), AllMsgModel.class);
                    if (allMsgModel.getState() == 4) {
                        adapter.addData(allMsgModel.getData());

                        if (mRecyclerViewAllmsg != null && mRecyclerViewAllmsg.canScrollVertically(1)) {
                            tvNewdataAllmsg.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerViewAllmsg.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void getGoldCall() {
        showDialog();
        HttpManager.getInstance().post(Api.ScreenGold, HttpManager.getInstance().getMap(), new MyObserver(this) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                tvGoldAllmsg.setText("X" + getOneBean.getData().getGold());
            }
        });
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

    private void setRecycler() {
        adapter = new AllMsgAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewAllmsg.setAdapter(adapter);
        mRecyclerViewAllmsg.setLayoutManager(layoutManager);

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            AllmsgBean.DataEntity dataEntity = (AllmsgBean.DataEntity) adapter.getItem(position);
            assert dataEntity != null;
            switch (view.getId()) {
                case R.id.iv_header_allmsg:

                    showOtherDialog(userToken, dataEntity.getUid());

                    break;
                case R.id.iv_user_header_allmsg:
                    bundle.putInt(Const.ShowIntent.ID, dataEntity.getUid());
                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                    break;

                case R.id.rl_show_allmsg:
                    if (dataEntity.getRid().equals(Const.RoomId)) {
                        showToast("您已在此房间");
                        return;
                    }
                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    bundle.putString(Const.ShowIntent.ROOMID, dataEntity.getRid());
                    ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                    ActivityCollector.getActivityCollector().finishActivity(AllMsgActivity.class);
                    break;
            }
        });

        mRecyclerViewAllmsg.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    tvNewdataAllmsg.setVisibility(View.GONE);
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
                    onChatTo(user.getId(), user.getName());
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


    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.QBgetScreen, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                AllmsgBean allmsgBean = JSON.parseObject(responseString, AllmsgBean.class);
                if (adapter != null) {
                    adapter.setNewData(allmsgBean.getData());
                    if (mRecyclerViewAllmsg != null) {
                        mRecyclerViewAllmsg.scrollToPosition(adapter.getItemCount() - 1);
                    }
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
        MyApplication.getInstance().delChatRoomMessage(chatRoomMessage);
    }


    @OnClick({R.id.tv_newdata_allmsg, R.id.btn_send_allmsg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_newdata_allmsg:
                tvNewdataAllmsg.setVisibility(View.GONE);
                mRecyclerViewAllmsg.scrollToPosition(adapter.getItemCount() - 1);
                break;
            case R.id.btn_send_allmsg:
                String mesShow = edtInputAllmsg.getText().toString();
                if (StringUtils.isEmpty(mesShow)) {
                    showToast("请输入消息内容");
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
                new TUIKitDialog(context)
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("是否发送全服喊话并扣费100浪花?")
                        .setDialogWidth(0.75f)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btnSendAllmsg.setClickable(false);
                                getMegCall(mesShow);
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();
                break;
        }
    }

    private void getMegCall(String mesShow) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("content", mesShow);
        map.put("rid", Const.RoomId);
        HttpManager.getInstance().post(Api.AddScreen, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                btnSendAllmsg.setClickable(true);
                AllMsgSendBean allMsgSendBean = new Gson().fromJson(responseString, AllMsgSendBean.class);
                if (allMsgSendBean.getCode() == Api.SUCCESS) {
                    setMesgSend(allMsgSendBean.getData());
                } else if (allMsgSendBean.getCode() == 2) {
                    showMyDialog("您的余额不足，充值后才能发送消息！", getString(R.string.tv_topup_packet));
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                btnSendAllmsg.setClickable(true);
            }
        });
    }

    private void setMesgSend(AllmsgBean.DataEntity dataEntity) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,    //会话类型：群聊
                Const.allMsgRoom);

//        /**
//         * 构建消息体
//         */
//        int gradeShow = (int) SharedPreferenceUtils.get(getContext(), Const.User.GRADE_T, 0);
//        int charmGrade = (int) SharedPreferenceUtils.get(getContext(), Const.User.CharmGrade, 0);
//        int sex = (int) SharedPreferenceUtils.get(getContext(), Const.User.SEX, 0);
//        String name = (String) SharedPreferenceUtils.get(getContext(), Const.User.NICKNAME, "");
//        String header = (String) SharedPreferenceUtils.get(getContext(), Const.User.IMG, "");
//        ChatRoomMsgModel.DataBean dataBean = new ChatRoomMsgModel.DataBean();
//        dataBean.setGrade(gradeShow);
//        dataBean.setCharm(charmGrade);
//        dataBean.setUid(userToken);
//        dataBean.setMessageShow(mesShow);
//        dataBean.setName(name);
//        dataBean.setSex(sex);
//        dataBean.setHeader(header);
//        ChatRoomMsgModel chatRoomMsgModel = new ChatRoomMsgModel(113, dataBean, 3);

        AllMsgModel allMsgModel = new AllMsgModel(120, dataEntity, 4);

        //构造一条消息
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[全国消息]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        String msgShow = JSON.toJSONString(allMsgModel);
        //向 TIMMessage 中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgShow.getBytes());      //自定义 byte[]
        elem.setDesc("[全国消息]"); //自定义描述信息
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
                BroadcastManager.getInstance(getContext()).sendBroadcast(Const.BroadCast.MSG_COUN, new Gson().toJson(dataEntity));
                if (edtInputAllmsg == null) {
                    return;
                }
                edtInputAllmsg.setText("");
                adapter.addData(dataEntity);
                mRecyclerViewAllmsg.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    /**
     * 对话框
     *
     * @param hintShow  提示内容
     * @param typeShow  点击类型 （弃用）
     * @param rightShow 提示右边显示，可为空
     */
    MyDialog myDialog;

    private void showMyDialog(String hintShow, String rightShow) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(getContext());
        myDialog.show();
        myDialog.setHintText(hintShow);
        if (!StringUtils.isEmpty(rightShow)) {
            myDialog.setRightText(rightShow);
        }
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
}
