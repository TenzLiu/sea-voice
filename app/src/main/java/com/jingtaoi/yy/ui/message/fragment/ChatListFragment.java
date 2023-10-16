package com.jingtaoi.yy.ui.message.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.ui.find.RadioDatingActivity;
import com.jingtaoi.yy.ui.message.ChatActivity;
import com.jingtaoi.yy.ui.room.AllMsgActivity;
import com.jingtaoi.yy.ui.room.VoiceActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.action.PopActionClickListener;
import com.tencent.qcloud.tim.uikit.component.action.PopDialogAdapter;
import com.tencent.qcloud.tim.uikit.component.action.PopMenuAction;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 聊天列表页面
 */
public class ChatListFragment extends MyBaseFragment {

    Unbinder unbinder;

    long timeClicker;
    boolean isRoom;
    @BindView(R.id.conversation_layout)
    ConversationLayout conversationLayout;
    private List<PopMenuAction> mConversationPopActions = new ArrayList<>();
    private ListView mConversationPopList;
    private PopupWindow mConversationPopWindow;
    private PopDialogAdapter mConversationPopAdapter;
    private ConversationListLayout listLayout;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatlist, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle1 = getArguments();
        boolean style=true;
        //这里就拿到了之前传递的参数
        if (bundle1 != null)
            style = bundle1.getBoolean("style");
        // 初始化聊天面板
        conversationLayout.initDefault();
        listLayout = conversationLayout.getConversationList();
        listLayout.getAdapter().setItemStyle(style);

        initPopMenuAction();
        listLayout.setOnItemClickListener((view, position, messageInfo) -> {
            //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
            if (messageInfo.isGroup()) {
                //如果是群组，跳转到群聊界面
//                    ChatActivity.startGroupChat(getActivity(), session.getPeer());
                long timeNow = System.currentTimeMillis();
                if (timeNow - timeClicker > 2000) {
                    timeClicker = timeNow;
                    if (messageInfo.getId().equals(Const.chatRoom)) {
                        ActivityCollector.getActivityCollector().toOtherActivity(RadioDatingActivity.class);
                    } else if (messageInfo.getId().equals(Const.allMsgRoom)) {
                        ActivityCollector.getActivityCollector().toOtherActivity(AllMsgActivity.class);
                    }
                }
            } else {
                //否则跳转到C2C单聊界面
//                    ChatActivity.startC2CChat(getActivity(), session.getPeer());
                long timeNow = System.currentTimeMillis();
                if (timeNow - timeClicker > 2000) {
                    timeClicker = timeNow;
                    Bundle bundle = new Bundle();
                    bundle.putString(Const.ShowIntent.ID, messageInfo.getId());
                    bundle.putString(Const.ShowIntent.NAME, messageInfo.getTitle());
                    ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setType(messageInfo.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C);
                    chatInfo.setId(messageInfo.getId());
                    chatInfo.setChatName(messageInfo.getTitle());
                    bundle.putSerializable(Const.ShowIntent.CHAT_INFO, chatInfo);
                    VoiceActivity activity = ActivityCollector.getActivity(VoiceActivity.class);
                    if (activity == null) {
                        bundle.putBoolean("isRoom", false);
                    } else {
                        bundle.putBoolean("isRoom", true);
                    }
                    ActivityCollector.getActivityCollector().toOtherActivity(ChatActivity.class, bundle);
                }
            }
        });

        listLayout.setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo messageInfo) {
                startPopShow(view, position, messageInfo);
            }
        });

        // 获取 TitleBarLayout
        TitleBarLayout titleBarLayout = conversationLayout.findViewById(R.id.conversation_title);
        //隐藏系统自带title
        titleBarLayout.setVisibility(View.GONE);

//        listLayout.setItemTopTextSize(16); // 设置 item 中 top 文字大小
//        listLayout.setItemBottomTextSize(12);// 设置 item 中 bottom 文字大小
//        listLayout.setItemDateTextSize(10);// 设置 item 中 timeline 文字大小
        listLayout.enableItemRoundIcon(true);// 设置 item 头像是否显示圆角，默认是方形

//        // 这里设置会话列表点击的跳转逻辑，告诉添加完SessionPanel后会话被点击后该如何处理
//        conversationLayout.setSessionClick(new SessionClickListener() {
//            @Override
//            public void onSessionClick(SessionInfo session) {
//                //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
//                if (session.isGroup()) {
//                    //如果是群组，跳转到群聊界面
////                    ChatActivity.startGroupChat(getActivity(), session.getPeer());
//                    long timeNow = System.currentTimeMillis();
//                    if (timeNow - timeClicker > 2000) {
//                        timeClicker = timeNow;
//                        ActivityCollector.getActivityCollector().toOtherActivity(RadioDatingActivity.class);
//                    }
//                } else {
//                    //否则跳转到C2C单聊界面
////                    ChatActivity.startC2CChat(getActivity(), session.getPeer());
//                    long timeNow = System.currentTimeMillis();
//                    if (timeNow - timeClicker > 2000) {
//                        timeClicker = timeNow;
//                        Bundle bundle = new Bundle();
//                        bundle.putString(Const.ShowIntent.ID, session.getPeer());
//                        bundle.putString(Const.ShowIntent.NAME, session.getPeer());
//                        VoiceActivity activity = ActivityCollector.getActivity(VoiceActivity.class);
//                        if (activity == null) {
//                            bundle.putBoolean("isRoom", false);
//                        } else {
//                            bundle.putBoolean("isRoom", true);
//                        }
//                        ActivityCollector.getActivityCollector().toOtherActivity(ChatActivity.class, bundle);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void initView() {

    }

    private void startPopShow(View view, int position, ConversationInfo info) {
        showItemPopMenu(position, info, view.getX(), view.getY() + view.getHeight() / 2);
    }

    private void initPopMenuAction() {

        // 设置长按conversation显示PopAction
        List<PopMenuAction> conversationPopActions = new ArrayList<PopMenuAction>();
        PopMenuAction action = new PopMenuAction();
        action.setActionName("置顶聊天");
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                conversationLayout.setConversationTop(position, (ConversationInfo) data);
            }
        });
        conversationPopActions.add(action);
        action = new PopMenuAction();
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                conversationLayout.deleteConversation(position, (ConversationInfo) data);
            }
        });
        action.setActionName("删除聊天");
        conversationPopActions.add(action);
        mConversationPopActions.clear();
        mConversationPopActions.addAll(conversationPopActions);
    }

    public void clearUnread(){
        conversationLayout.clearUnread();
    }

    /**
     * 长按会话item弹框
     *
     * @param index            会话序列号
     * @param conversationInfo 会话数据对象
     * @param locationX        长按时X坐标
     * @param locationY        长按时Y坐标
     */
    private void showItemPopMenu(final int index, final ConversationInfo conversationInfo, float locationX, float locationY) {
        if (mConversationPopActions == null || mConversationPopActions.size() == 0)
            return;
        View itemPop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu_layout, null);
        mConversationPopList = itemPop.findViewById(R.id.pop_menu_list);
        mConversationPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopMenuAction action = mConversationPopActions.get(position);
                if (action.getActionClickListener() != null) {
                    action.getActionClickListener().onActionClick(index, conversationInfo);
                }
                mConversationPopWindow.dismiss();
            }
        });

        for (int i = 0; i < mConversationPopActions.size(); i++) {
            PopMenuAction action = mConversationPopActions.get(i);
            if (conversationInfo.isTop()) {
                if (action.getActionName().equals("置顶聊天")) {
                    action.setActionName("取消置顶");
                }
            } else {
                if (action.getActionName().equals("取消置顶")) {
                    action.setActionName("置顶聊天");
                }

            }
        }
        mConversationPopAdapter = new PopDialogAdapter();
        mConversationPopList.setAdapter(mConversationPopAdapter);
        mConversationPopAdapter.setDataSource(mConversationPopActions);
        mConversationPopWindow = PopWindowUtil.popupWindow(itemPop, conversationLayout, (int) locationX, (int) locationY);
        conversationLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConversationPopWindow.dismiss();
            }
        }, 8000); // 8s后无操作自动消失
    }

    View RootView;


    public View getRootView() {
        return RootView;
    }

    public void setRootView(View rootView) {
        RootView = rootView;
    }


    @Override
    public void setResume() {
        if (RootView != null) {
            WindowManager manager = getActivity().getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            int height = outMetrics.heightPixels / 3;

//            sessionPanel.mSessionList.setItemLongClickListener(new SessionListView.ItemLongClickListener() {
//                @Override
//                public void onItemLongClick(View view, int position, float x, float y) {
//                    y = y + height;
//                    sessionPanel.showItemPopMenu(RootView, position, sessionPanel.getSessionAdapter().getItem(position), x, y);
//                }
//            });

            if (listLayout!=null)
                listLayout.getAdapter().notifyDataSetChanged();
        }

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
    }
}
