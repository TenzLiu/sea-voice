package com.jingtaoi.yy.ui.room.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.RoomRankBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.home.adapter.RoomRankAdapter;
import com.jingtaoi.yy.ui.message.ChatActivity;
import com.jingtaoi.yy.ui.message.OtherHomeActivity;
import com.jingtaoi.yy.ui.mine.PersonHomeActivity;
import com.jingtaoi.yy.ui.room.dialog.OtherShowDialog;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.fragment.BaseFragment;
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;

public class RoomRankFragment extends BaseFragment {
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshRecyclerLayout mSwipeRefreshLayout;


    Unbinder unbinder;

    private RoomRankAdapter adapter;
    private ArrayList<RoomRankBean.DataBean> users = new ArrayList<>();
    int userToken;
    String roomId;
    int userRoomType;
    View RootView;

    public View getRootView() {
        return RootView;
    }

    public void setRootView(View rootView) {
        RootView = rootView;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_base_recyclerview;
    }

    private ViewHolder viewHolder;
    private int textColor;

    @Override
    protected void onFirstVisibleToUser() {
        userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);
        type = getArguments().getInt("type");
        roomId = getArguments().getString("roomId");
        userRoomType = getArguments().getInt("userRoomType");
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_room_rank_header, mSwipeRefreshLayout,false);
        viewHolder = new ViewHolder(view);
//        viewHolder.xuzanLay1.setVisibility(View.VISIBLE);
        viewHolder.iv_text.setVisibility(View.GONE);
        if (type == 1) {//2魅力榜 1土豪榜
            viewHolder.rbDay.setTextColor(getResources().getColorStateList(R.color.selector_rich_rank));
            viewHolder.rgTop.setBackgroundResource(R.drawable.top_bar_bg_rich);
            viewHolder.rbAll.setTextColor(getResources().getColorStateList(R.color.selector_rich_rank));
            viewHolder.rbDay.setBackgroundResource(R.drawable.selector_bar_rich);
            viewHolder.rbAll.setBackgroundResource(R.drawable.selector_bar_rich);
            viewHolder.zan_icon_iv1.setImageResource(R.drawable.icon_xz);
            viewHolder.zan_icon_iv2.setImageResource(R.drawable.icon_xz);
            viewHolder.zan_icon_iv3.setImageResource(R.drawable.icon_xz);
            viewHolder.iv_top1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.flag_rich,0,0);
            textColor = Color.parseColor("#9063D6");
        } else {
            viewHolder.zan_icon_iv1.setImageResource(R.drawable.icon_ml);
            viewHolder.zan_icon_iv2.setImageResource(R.drawable.icon_ml);
            viewHolder.zan_icon_iv3.setImageResource(R.drawable.icon_ml);
            viewHolder.rgTop.setBackgroundResource(R.drawable.top_bar_bg_att);
            viewHolder.rbDay.setTextColor(getResources().getColorStateList(R.color.selector_attractic_rank));
            viewHolder.rbAll.setTextColor(getResources().getColorStateList(R.color.selector_attractic_rank));
            viewHolder.rbDay.setBackgroundResource(R.drawable.selector_bar_attractic);
            viewHolder.rbAll.setBackgroundResource(R.drawable.selector_bar_attractic);
            viewHolder.iv_top1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.flag_att,0,0);
            textColor = Color.parseColor("#9063D6");

        }
        viewHolder.iv_top1.setTextColor(textColor);
        viewHolder.tvOffset1.setTextColor(textColor);
        viewHolder.tvOffset2.setTextColor(textColor);
        adapter = new RoomRankAdapter(users, type, userToken, userRoomType,textColor);
        adapter.setZHYM(false);
        adapter.setHeaderView(view);
        mSwipeRefreshLayout.setAdapter(adapter);
        mSwipeRefreshLayout.setMode(SwipeRefreshRecyclerLayout.Mode.Top);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshRecyclerLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        adapter.setOnItemClickListener((view1, position) -> {
            RoomRankBean.DataBean dataBean = users.get(position);
            showMyPseronDialog(userToken, dataBean.getId());
        });
        getData();
        viewHolder.rgTop.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_day:
                    state = 1;
                    getData();
                    break;
                case R.id.rb_week:
                    state = 2;
                    getData();
                    break;
                case R.id.rb_all:
                    state = 3;
                    getData();
                    break;
            }
        });
        getData();
    }

    private int type;
    private int state = 1;

    private void getData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("type", type);
        map.put("state", state);
        HttpManager.getInstance().post(Api.SaveRoomCFML, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);
                RoomRankBean baseBean = JSON.parseObject(responseString, RoomRankBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    List<RoomRankBean.DataBean> data = baseBean.data;
                    users.clear();
                    if (data.isEmpty()) {
                        viewHolder.ivChampion.setImageURI("");
                        viewHolder.iv2nd.setImageURI("");
                        viewHolder.iv3rd.setImageURI("");
                        viewHolder.tvChampion.setText("暂无");
                        viewHolder.tv2nd.setText("暂无");
                        viewHolder.tv3rd.setText("暂无");
                        viewHolder.tvChampion.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        viewHolder.tv2nd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        viewHolder.tv3rd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        viewHolder.tvOffset1.setVisibility(View.INVISIBLE);
                        viewHolder.tvOffset2.setVisibility(View.INVISIBLE);
                        viewHolder.tvAccount1.setVisibility(View.INVISIBLE);
                        viewHolder.tvAccount2.setVisibility(View.INVISIBLE);
                        viewHolder.tvAccount3.setVisibility(View.INVISIBLE);
                        viewHolder.xuzanTv1.setText("0");
                        viewHolder.xuzanTv2.setText("0");
                        viewHolder.xuzanTv3.setText("0");
                        viewHolder.xuzanLay1.setVisibility(View.INVISIBLE);
                        viewHolder.xuzanLay2.setVisibility(View.INVISIBLE);
                        viewHolder.xuzanLay3.setVisibility(View.INVISIBLE);
                        viewHolder.iv_top1.setText("");

                    } else if (data.size() == 1) {
                        viewHolder.ivChampion.setImageURI(data.get(0).getImg());
                        viewHolder.iv2nd.setImageURI("");
                        viewHolder.iv3rd.setImageURI("");
//                        viewHolder.iv2nd.setVisibility(View.INVISIBLE);
//                        viewHolder.iv3rd.setVisibility(View.INVISIBLE);
                        viewHolder.tvChampion.setText(data.get(0).getName());
                        viewHolder.tv2nd.setText("暂无");
                        viewHolder.tv3rd.setText("暂无");
//                        viewHolder.tvChampion.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, type == 2 ? ImageShowUtils.getCharm(data.get(0).getGrade()) : ImageShowUtils.getGrade(data.get(0).getGrade()));
//                        viewHolder.tv2nd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                        viewHolder.tv3rd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        viewHolder.tvOffset1.setVisibility(View.INVISIBLE);
                        viewHolder.tvOffset2.setVisibility(View.INVISIBLE);
                        viewHolder.tvAccount1.setVisibility(View.VISIBLE);
                        if (data.get(0).getLiang() == null || TextUtils.isEmpty(data.get(0).getLiang())){
                            viewHolder.tvAccount1.setText("ID:" + data.get(0).getUsercoding());
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount1.setText("ID:" + data.get(0).getLiang());
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount2.setVisibility(View.INVISIBLE);
                        viewHolder.tvAccount3.setVisibility(View.INVISIBLE);
                        viewHolder.ivChampion.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(0).getId());
                        });

                        viewHolder.xuzanTv1.setText(data.get(0).getNum() + "");
                        viewHolder.xuzanTv2.setText("0");
                        viewHolder.xuzanTv3.setText("0");
                        if (userToken == data.get(0).getId()||userRoomType == 1 || userRoomType == 2) {
                            viewHolder.iv_top1.setText("" + data.get(0).getNum());
                        }
                        else {
                            viewHolder.iv_top1.setText("");
                        }

                    } else if (data.size() == 2) {
                        viewHolder.ivChampion.setImageURI(data.get(0).getImg());
//                        viewHolder.iv2nd.setVisibility(View.VISIBLE);
                        viewHolder.iv2nd.setImageURI(data.get(1).getImg());
//                        viewHolder.iv3rd.setVisibility(View.INVISIBLE);
                        viewHolder.iv3rd.setImageURI("");

                        viewHolder.tvChampion.setText(data.get(0).getName());
                        viewHolder.tv2nd.setText(data.get(1).getName());
                        viewHolder.tv3rd.setText("暂无");
//                        viewHolder.tvChampion.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, type == 2 ? ImageShowUtils.getCharm(data.get(0).getGrade()) : ImageShowUtils.getGrade(data.get(0).getGrade()));
//                        viewHolder.tv2nd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, type == 2 ? ImageShowUtils.getCharm(data.get(1).getGrade()) : ImageShowUtils.getGrade(data.get(1).getGrade()));
//                        viewHolder.tv3rd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                        viewHolder.tvOffset2.setVisibility(View.INVISIBLE);
                        viewHolder.tvAccount1.setVisibility(View.VISIBLE);
                        if (data.get(0).getLiang() == null || TextUtils.isEmpty(data.get(0).getLiang())){
                            viewHolder.tvAccount1.setText("ID:" + data.get(0).getUsercoding());
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount1.setText("ID:" + data.get(0).getLiang());
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount2.setVisibility(View.VISIBLE);
                        if (data.get(1).getLiang() == null || TextUtils.isEmpty(data.get(1).getLiang())){
                            viewHolder.tvAccount2.setText("ID:" + data.get(1).getUsercoding());
                            viewHolder.tvAccount2.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount2.setText("ID:" + data.get(1).getLiang());
                            viewHolder.tvAccount2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount3.setVisibility(View.GONE);
                        viewHolder.ivChampion.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(0).getId());
                        });
                        viewHolder.iv2nd.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(1).getId());
                        });

                        viewHolder.xuzanTv1.setText(data.get(0).getNum() + "");
                        viewHolder.xuzanTv2.setText(data.get(1).getNum() + "");
                        viewHolder.xuzanTv3.setText("0");

//                        // 排行榜只能看到自己
//                        if (userToken == data.get(0).getId()) {
//                            viewHolder.xuzanLay1.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.xuzanLay1.setVisibility(View.INVISIBLE);
//                        }
                        if (userToken == data.get(0).getId()||userRoomType == 1 || userRoomType == 2) {
                            viewHolder.iv_top1.setText("" + data.get(0).getNum());
                        }
                        else {
                            viewHolder.iv_top1.setText("");
                        }
                        if (userToken == data.get(1).getId()||userRoomType == 1 || userRoomType == 2) {
                            viewHolder.tvOffset1.setVisibility(View.VISIBLE);
                            String s1 = "" + data.get(1).getNum();
                            viewHolder.tvOffset1.setText(s1);
//                            viewHolder.xuzanLay2.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvOffset1.setVisibility(View.INVISIBLE);
                        }

                    } else {
                        viewHolder.ivChampion.setVisibility(View.VISIBLE);
                        viewHolder.iv2nd.setVisibility(View.VISIBLE);
                        viewHolder.iv3rd.setVisibility(View.VISIBLE);
                        viewHolder.ivChampion.setImageURI(data.get(0).getImg());
                        viewHolder.iv2nd.setImageURI(data.get(1).getImg());
                        viewHolder.iv3rd.setImageURI(data.get(2).getImg());
                        viewHolder.tvChampion.setText(data.get(0).getName());
                        viewHolder.tv2nd.setText(data.get(1).getName());
                        viewHolder.tv3rd.setText(data.get(2).getName());

                        viewHolder.tvAccount1.setVisibility(View.VISIBLE);
                        if (data.get(0).getLiang() == null || TextUtils.isEmpty(data.get(0).getLiang())){
                            viewHolder.tvAccount1.setText("ID:" + data.get(0).getUsercoding());
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount1.setText("ID:" + data.get(0).getLiang());
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount2.setVisibility(View.VISIBLE);
                        if (data.get(1).getLiang() == null || TextUtils.isEmpty(data.get(1).getLiang())){
                            viewHolder.tvAccount2.setText("ID:" + data.get(1).getUsercoding());
                            viewHolder.tvAccount2.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount2.setText("ID:" + data.get(1).getLiang());
                            viewHolder.tvAccount2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount3.setVisibility(View.VISIBLE);
                        if (data.get(2).getLiang() == null || TextUtils.isEmpty(data.get(2).getLiang())){
                            viewHolder.tvAccount3.setText("ID:" + data.get(2).getUsercoding());
                            viewHolder.tvAccount3.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount3.setText("ID:" + data.get(2).getLiang());
                            viewHolder.tvAccount3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.ivChampion.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(0).getId());
                        });
                        viewHolder.iv2nd.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(1).getId());
                        });
                        viewHolder.iv3rd.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(2).getId());
                        });
                        List<RoomRankBean.DataBean> sub = data.subList(3, data.size());
                        users.addAll(sub);
                        if (userToken == data.get(0).getId()||userRoomType == 1 || userRoomType == 2) {
                            viewHolder.iv_top1.setText("" + data.get(0).getNum());
                        }
                        else {
                            viewHolder.iv_top1.setText("");
                        }
                        if (userToken == data.get(1).getId()||userRoomType == 1 || userRoomType == 2){
                            viewHolder.tvOffset1.setVisibility(View.VISIBLE);
                            String s1 = "" + data.get(1).getNum();
                            viewHolder.tvOffset1.setText(s1);
//                            viewHolder.xuzanLay2.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvOffset1.setVisibility(View.INVISIBLE);

//                            viewHolder.xuzanLay2.setVisibility(View.INVISIBLE);
                        }
                        if (userToken == data.get(2).getId()||userRoomType == 1 || userRoomType == 2){
                            viewHolder.tvOffset2.setVisibility(View.VISIBLE);
                            String s2 = "" + data.get(2).getNum();
                            viewHolder.tvOffset2.setText(s2);
//                            viewHolder.xuzanLay3.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvOffset2.setVisibility(View.INVISIBLE);

//                            viewHolder.xuzanLay3.setVisibility(View.INVISIBLE);
                        }

                    }

                    adapter.notifyDataSetChanged();
                } else {
                    showToast(baseBean.getMsg());
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
    OtherShowDialog myBottomPersonDialog;

    private void showMyPseronDialog(int userId, int otherId) {
        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
            myBottomPersonDialog.dismiss();
        }
        ArrayList<String> seatList = new ArrayList<>();
        seatList.add(getString(R.string.tv_message_bottomshow));
        seatList.add(getString(R.string.tv_sixin));
        myBottomPersonDialog = new OtherShowDialog(getContext(), seatList, userId, otherId);
        myBottomPersonDialog.show();
        myBottomPersonDialog.setOnItemClickListener((position, user) -> {
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
    protected void onVisibleToUser() {

    }

    @Override
    protected void onInvisibleToUser() {

    }

    static public RoomRankFragment newInstance(int type, String roomId, int userRoomType) { //2魅力榜 1土豪榜
        RoomRankFragment rankFragment = new RoomRankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("roomId", roomId);
        bundle.putInt("userRoomType", userRoomType);
        rankFragment.setArguments(bundle);
        return rankFragment;
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

    class ViewHolder {
        @BindView(R.id.rg_top)
        RadioGroup rgTop;
        @BindView(R.id.rb_day)
        RadioButton rbDay;
        @BindView(R.id.rb_week)
        RadioButton rbAll;
        @BindView(R.id.iv_champion)
        SimpleDraweeView ivChampion;
        @BindView(R.id.tv_champion)
        TextView tvChampion;
        @BindView(R.id.tv_account_1)
        TextView tvAccount1;
        @BindView(R.id.iv_2nd)
        SimpleDraweeView iv2nd;
        @BindView(R.id.tv_2nd)
        TextView tv2nd;
        @BindView(R.id.tv_offset_1)
        TextView tvOffset1;
        @BindView(R.id.tv_account_2)
        TextView tvAccount2;
        @BindView(R.id.iv_3rd)
        SimpleDraweeView iv3rd;
        @BindView(R.id.tv_3rd)
        TextView tv3rd;
        @BindView(R.id.tv_offset_2)
        TextView tvOffset2;
        @BindView(R.id.tv_account_3)
        TextView tvAccount3;

        @BindView(R.id.xuzanLay3)
        LinearLayout xuzanLay3;
        @BindView(R.id.xuzan_tv3)
        TextView xuzanTv3;

        @BindView(R.id.xuzanLay2)
        LinearLayout xuzanLay2;
        @BindView(R.id.xuzan_tv2)
        TextView xuzanTv2;

        @BindView(R.id.xuzanLay1)
        LinearLayout xuzanLay1;
        @BindView(R.id.xuzan_tv1)
        TextView xuzanTv1;

        @BindView(R.id.zan_icon_iv1)
        ImageView zan_icon_iv1;

        @BindView(R.id.zan_icon_iv2)
        ImageView zan_icon_iv2;

        @BindView(R.id.zan_icon_iv3)
        ImageView zan_icon_iv3;
        @BindView(R.id.iv_top1)
        TextView iv_top1;
        @BindView(R.id.iv_text)
        ImageView iv_text;
        @BindView(R.id.cl_bg)
        View cl_bg;


        @BindView(R.id.line_view)
        View line_view;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
