//package com.cityrise.uuvoice.ui.find.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.cityrise.uuvoice.R;
//import com.cityrise.uuvoice.agora.YySignaling;
//import com.cityrise.uuvoice.base.MyBaseFragment;
//import com.cityrise.uuvoice.bean.BaseBean;
//import com.cityrise.uuvoice.bean.ChatRoomMsgBean;
//import com.cityrise.uuvoice.bean.GetOneBean;
//import com.cityrise.uuvoice.control.ChatMessage;
//import com.cityrise.uuvoice.control.VirifyCountDownTimer;
//import com.cityrise.uuvoice.model.ChatMessageBean;
//import com.cityrise.uuvoice.model.ChatRoomMsgModel;
//import com.cityrise.uuvoice.model.MessageBean;
//import com.cityrise.uuvoice.netUtls.Api;
//import com.cityrise.uuvoice.netUtls.HttpManager;
//import com.cityrise.uuvoice.netUtls.MyObserver;
//import com.cityrise.uuvoice.ui.find.adapter.RadioDRecyAdapter;
//import com.cityrise.uuvoice.ui.message.OtherHomeActivity;
//import com.cityrise.uuvoice.ui.mine.PersonHomeActivity;
//import com.cityrise.uuvoice.utils.ActivityCollector;
//import com.cityrise.uuvoice.utils.Const;
//import com.cityrise.uuvoice.utils.MessageUtils;
//import com.cityrise.uuvoice.utils.MyUtils;
//import com.cityrise.uuvoice.utils.SharedPreferenceUtils;
//import com.google.gson.Gson;
//
//import java.util.HashMap;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import cn.sinata.xldutils.utils.StringUtils;
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//
///**
// * 广播交友(弃用)
// */
//public class RadioDatingFragment extends MyBaseFragment {
//    @BindView(R.id.mRecyclerView_radiodating)
//    RecyclerView mRecyclerViewRadiodating;
//    @BindView(R.id.tv_newdata_radiodating)
//    TextView tvNewdataRadiodating;
//    @BindView(R.id.edt_input_radiodating)
//    EditText edtInputRadiodating;
//    @BindView(R.id.btn_send_radiodating)
//    Button btnSendRadiodating;
//    @BindView(R.id.ll_chat_radiodating)
//    LinearLayout llChatRadiodating;
//    Unbinder unbinder;
//    RadioDRecyAdapter radioDRecyAdapter;
//    String chatRoomName = "chatRoom";
//
//    //消息回调
//    private Consumer<String> consumer;
//    Observable<String> observable;
//    Disposable subscribe;
//
//    VirifyCountDownTimer virifyCountDownTimer;
//
//    @Override
//    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_radiodating, container, false);
//    }
//
//    @Override
//    public void initView() {
//        //登录 Agora 信令系统
//        YySignaling.getInstans().loginSignaling(String.valueOf(userToken));
//        //信令加入频道
//        YySignaling.getInstans().joinChannel(chatRoomName);
//        setRecycler();
//        MessageUtils.getInstans().addChatShows(chatMessage);
//        if (Const.msgSendTime != 0) { //判断是否可以发送消息
//            long timeEnd = System.currentTimeMillis() - Const.msgSendTime;
//            if (timeEnd > 60000) {
//                Const.msgSendTime = 0;
//            } else {
//                setEditShow(60000-timeEnd);
//            }
//        }
//        getCall();
//    }
//
//    private void getCall() {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        HttpManager.getInstance().post(Api.BroadList, map, new MyObserver(this) {
//            @Override
//            public void success(String responseString) {
//                ChatRoomMsgBean chatRoomMsgBean = JSON.parseObject(responseString, ChatRoomMsgBean.class);
//                if (chatRoomMsgBean.getCode() == Api.SUCCESS) {
//                    radioDRecyAdapter.setNewData(chatRoomMsgBean.getData());
//                    mRecyclerViewRadiodating.scrollToPosition(radioDRecyAdapter.getItemCount() - 1);
//                } else {
//                    showToast(chatRoomMsgBean.getMsg());
//                }
//            }
//        });
//    }
//
//    ChatMessage chatMessage = new ChatMessage() {
//        @Override
//        public void setMessageShow(String channelID, String account, int uid, String msg) {
//            if (channelID.equals(chatRoomName)) {
//                if (consumer == null) {
//                    initConsumer();
//                }
//                observable = Observable.just(msg);
//                subscribe = observable.observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(consumer);
//            }
//        }
//
//        @Override
//        public void setChannelAttrUpdated(String s, String s1, String s2, String s3) {
//
//        }
//    };
//
//    private void initConsumer() {
//        consumer = new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                MessageBean messageBean = new Gson().fromJson(s, MessageBean.class);
////                chatRecyclerAdapter.addData(s);
//                if (messageBean.getCode() == 113) {
//                    ChatRoomMsgModel chatRoomMsgModel = new Gson().fromJson(s, ChatRoomMsgModel.class);
//                    radioDRecyAdapter.addData(chatRoomMsgModel.getData());
//
//                    if (mRecyclerViewRadiodating != null && mRecyclerViewRadiodating.canScrollVertically(1)) {
//                        tvNewdataRadiodating.setVisibility(View.VISIBLE);
//                    } else {
//                        mRecyclerViewRadiodating.scrollToPosition(radioDRecyAdapter.getItemCount() - 1);
//                    }
//                }
//            }
//        };
//    }
//
//    private void setRecycler() {
//        radioDRecyAdapter = new RadioDRecyAdapter(R.layout.item_list_radioda);
//        radioDRecyAdapter.setUserId(userToken);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        mRecyclerViewRadiodating.setLayoutManager(layoutManager);
//        mRecyclerViewRadiodating.setAdapter(radioDRecyAdapter);
//
//        radioDRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                switch (view.getId()) {
//                    case R.id.iv_header_radioda:
//                        ChatRoomMsgModel.DataBean dataBean = (ChatRoomMsgModel.DataBean) adapter.getItem(position);
//                        Bundle bundle = new Bundle();
//                        assert dataBean != null;
//                        bundle.putInt(Const.ShowIntent.ID, dataBean.getUid());
//                        if (dataBean.getUid() == userToken) {
//                            ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
//                        } else {
//                            ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
//                        }
//                        break;
//                }
//            }
//        });
//
//        mRecyclerViewRadiodating.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                int lastPosition = -1;
//                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                    ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                }
//                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
//                //如果相等则说明已经滑动到最后了
//                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
//                    tvNewdataRadiodating.setVisibility(View.GONE);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void setResume() {
//
//    }
//
//    @Override
//    public void setOnclick() {
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        unbinder = ButterKnife.bind(this, rootView);
//        return rootView;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//        YySignaling.getInstans().logout();
//        if (virifyCountDownTimer != null) {
//            virifyCountDownTimer.cancel();
//        }
//    }
//
//    @OnClick({R.id.tv_newdata_radiodating, R.id.btn_send_radiodating})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_newdata_radiodating:
//                tvNewdataRadiodating.setVisibility(View.GONE);
//                mRecyclerViewRadiodating.scrollToPosition(radioDRecyAdapter.getItemCount() - 1);
//                break;
//            case R.id.btn_send_radiodating:
//                String mesShow = edtInputRadiodating.getText().toString();
//                if (StringUtils.isEmpty(mesShow)) {
//                    showToast("请输入聊天内容");
//                    return;
//                }
//                btnSendRadiodating.setClickable(false);
//                getMegCall(mesShow);
//                break;
//        }
//    }
//
//    private void getMegCall(String mesShow) {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        map.put("uid", userToken);
//        map.put("content", mesShow);
//        HttpManager.getInstance().post(Api.Broad, map, new MyObserver(this) {
//            @Override
//            public void success(String responseString) {
//                GetOneBean baseBean = JSON.parseObject(responseString, GetOneBean.class);
//                if (baseBean.getCode() == Api.SUCCESS) {
//                    if (baseBean.getData().getState() == 1) {
//                        btnSendRadiodating.setClickable(true);
//                        showToast("您当前不在麦位上");
//                    } else if (baseBean.getData().getState() == 2) {
//                        Const.msgSendTime = System.currentTimeMillis();
//                        setEditShow(60000);
//                        setMesgSend(mesShow);
//                    }
//                } else {
//                    showToast(baseBean.getMsg());
//                    btnSendRadiodating.setClickable(true);
//                }
//            }
//        });
//    }
//
//    /**
//     * 更新页面显示
//     *
//     * @param millisInfuture 倒计时毫秒值
//     */
//    private void setEditShow(long millisInfuture) {
//
//        edtInputRadiodating.setText("");
//        MyUtils.getInstans().hideKeyboard(edtInputRadiodating);
//        virifyCountDownTimer =
//                new VirifyCountDownTimer(btnSendRadiodating, getString(R.string.tv_send),
//                        millisInfuture, 1000, timerFinish);
//        virifyCountDownTimer.start();
//        btnSendRadiodating.setAlpha(0.5f);
//    }
//
//    VirifyCountDownTimer.TimerFinish timerFinish = new VirifyCountDownTimer.TimerFinish() {
//        @Override
//        public void onFinish() {
//            btnSendRadiodating.setAlpha(1.0f);
//        }
//    };
//
//    private void setMesgSend(String mesShow) {
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
//        ChatRoomMsgModel chatRoomMsgModel = new ChatRoomMsgModel(113, dataBean);
//        String chatString = new Gson().toJson(chatRoomMsgModel);
//        YySignaling.getInstans().sendChannelMessage(chatRoomName, chatString);
//    }
//}
