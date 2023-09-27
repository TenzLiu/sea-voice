package com.siyecaoi.yy.ui.home.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.RankBean;
import com.siyecaoi.yy.dialog.MyBottomPersonDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.home.adapter.RankAdapter;
import com.siyecaoi.yy.ui.message.ChatActivity;
import com.siyecaoi.yy.ui.message.OtherHomeActivity;
import com.siyecaoi.yy.ui.mine.PersonHomeActivity;
import com.siyecaoi.yy.ui.room.dialog.OtherShowDialog;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.fragment.BaseFragment;
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;

public class RankFragment extends BaseFragment {
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshRecyclerLayout mSwipeRefreshLayout;


    Unbinder unbinder;

    private RankAdapter adapter;
    private ArrayList<RankBean.DataBean> users = new ArrayList<>();
    int userToken;
    private int textColor;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_rank_recyclerview;
    }

    private ViewHolder viewHolder;

    @Override
    protected void onFirstVisibleToUser() {
        userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);
        type = getArguments().getInt("type");
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_rank_header, mSwipeRefreshLayout, false);
        viewHolder = new ViewHolder(view);
        if (type == 1) {//2魅力榜 1土豪榜
            viewHolder.rbDay.setTextColor(getResources().getColorStateList(R.color.selector_rich_rank));
            viewHolder.rbAll.setTextColor(getResources().getColorStateList(R.color.selector_rich_rank));
            viewHolder.rbDay.setBackgroundResource(R.drawable.selector_bar_rich);
            viewHolder.rbAll.setBackgroundResource(R.drawable.selector_bar_rich);
            viewHolder.zan_icon_iv1.setImageResource(R.drawable.icon_xz);
            viewHolder.zan_icon_iv2.setImageResource(R.drawable.icon_xz);
            viewHolder.zan_icon_iv3.setImageResource(R.drawable.icon_xz);
            viewHolder.iv_top1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.flag_rich,0,0);
            textColor = Color.parseColor("#D92F9F");
            viewHolder.rl_mine.setBackgroundResource(R.drawable.bg_haoqi_mine);
        } else {
            viewHolder.zan_icon_iv1.setImageResource(R.drawable.icon_ml);
            viewHolder.zan_icon_iv2.setImageResource(R.drawable.icon_ml);
            viewHolder.zan_icon_iv3.setImageResource(R.drawable.icon_ml);
            viewHolder.rbDay.setTextColor(getResources().getColorStateList(R.color.selector_attractic_rank));
            viewHolder.rbAll.setTextColor(getResources().getColorStateList(R.color.selector_attractic_rank));
            viewHolder.rbDay.setBackgroundResource(R.drawable.selector_bar_attractic);
            viewHolder.rbAll.setBackgroundResource(R.drawable.selector_bar_attractic);
            viewHolder.iv_top1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.flag_att,0,0);
            textColor = Color.parseColor("#D92F9F");
            viewHolder.rl_mine.setBackgroundResource(R.drawable.bg_meili_mine);
        }
        adapter = new RankAdapter(textColor,users, type, userToken);
        adapter.setModeLiang(false);
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
            RankBean.DataBean dataBean = users.get(position);
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
        map.put("type", type);
        map.put("state", state);
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.HOME_RANK, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);
                RankBean baseBean = JSON.parseObject(responseString, RankBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    RankBean.DataBean myData = baseBean.getData().myData;
                    if (myData!=null){
                        viewHolder.ivMy.setImageURI(myData.getImgTx());
                        viewHolder.tvMy.setText(myData.getNickname());
                        viewHolder.tvMyNum.setText("ID:"+myData.getUsercoding());
                        viewHolder.tvMyNum.setCompoundDrawablesWithIntrinsicBounds(myData.getIsliang()==1?R.drawable.liang_id:0,
                                0,0,0);
                        viewHolder.tvMyRank.setText(myData.rankNum == 0?"未上榜":String.format(Locale.CHINA,"%02d",myData.rankNum));
                        viewHolder.tvMyRank.setTextSize(TypedValue.COMPLEX_UNIT_SP,myData.rankNum == 0?10:24);
//                        viewHolder.tvMyRank.setTextColor(myData.rankNum == 0?Color.parseColor("#9FA3B0"):textColor);
                    }
                    List<RankBean.DataBean> data = baseBean.getData().list;
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
                    } else if (data.size() == 1) {
//                        viewHolder.ivChampion.setVisibility(View.VISIBLE);
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
//                        // 排行榜只能看到自己
//                        if (userToken == data.get(0).getId()) {
//                            viewHolder.xuzanLay1.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.xuzanLay1.setVisibility(View.INVISIBLE);
//                        }
//                        viewHolder.xuzanLay2.setVisibility(View.INVISIBLE);
//                        viewHolder.xuzanLay3.setVisibility(View.INVISIBLE);
                    } else if (data.size() == 2) {
//                        viewHolder.ivChampion.setVisibility(View.VISIBLE);
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
                        if (userToken == data.get(1).getId()) {
                            viewHolder.tvOffset1.setVisibility(View.VISIBLE);
                            String s1 = "距上名\n" + data.get(1).getNum();
                            SpannableString string1 = new SpannableString(s1);
                            string1.setSpan(new ForegroundColorSpan(textColor),3,s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            viewHolder.tvOffset1.setText(string1);
//                            viewHolder.xuzanLay2.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvOffset1.setVisibility(View.INVISIBLE);

//                            viewHolder.xuzanLay2.setVisibility(View.INVISIBLE);
                        }
//                        viewHolder.xuzanLay3.setVisibility(View.INVISIBLE);
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
                        List<RankBean.DataBean> sub = data.subList(3, data.size());
                        users.addAll(sub);
//                        viewHolder.xuzanTv1.setText(data.get(0).getNum()+"");
//                        viewHolder.xuzanTv2.setText(data.get(1).getNum()+"");
//                        viewHolder.xuzanTv3.setText(data.get(2).getNum()+"");



                        // 排行榜只能看到自己
//                        if (userToken == data.get(0).getId()){
//                            viewHolder.xuzanLay1.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.xuzanLay1.setVisibility(View.INVISIBLE);
//                        }
                        if (userToken == data.get(1).getId()){
                            viewHolder.tvOffset1.setVisibility(View.VISIBLE);
                            String s1 = "距上名\n" + data.get(1).getNum();
                            SpannableString string1 = new SpannableString(s1);
                            string1.setSpan(new ForegroundColorSpan(textColor),3,s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            viewHolder.tvOffset1.setText(string1);
//                            viewHolder.xuzanLay2.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvOffset1.setVisibility(View.INVISIBLE);

//                            viewHolder.xuzanLay2.setVisibility(View.INVISIBLE);
                        }
                        if (userToken == data.get(2).getId()){
                            viewHolder.tvOffset2.setVisibility(View.VISIBLE);
                            String s2 = "距上名\n" + data.get(2).getNum();
                            SpannableString string2 = new SpannableString(s2);
                            string2.setSpan(new ForegroundColorSpan(textColor),3,s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            viewHolder.tvOffset2.setText(string2);
//                            viewHolder.xuzanLay3.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvOffset2.setVisibility(View.INVISIBLE);

//                            viewHolder.xuzanLay3.setVisibility(View.INVISIBLE);
                        }
                    }

//                    if (users!=null&&users.size()>0){
//                        viewHolder.line_view.setVisibility(View.VISIBLE);
//                    }else {
//                        viewHolder.line_view.setVisibility(View.GONE);
//                    }

                    if (data.size()>3){
                        viewHolder.cl_bg.setBackgroundColor(Color.WHITE);
                    }else
                        viewHolder.cl_bg.setBackgroundResource(R.drawable.bg_white_bottom_20dp);

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

//        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
//            myBottomPersonDialog.dismiss();
//        }
//        ArrayList<String> seatList = new ArrayList<>();
//        seatList.add(getString(R.string.tv_message_bottomshow));
//        seatList.add(getString(R.string.tv_sixin));
//        myBottomPersonDialog = new OtherShowDialog(getContext(), seatList, userId, otherId);
//        myBottomPersonDialog.show();
//        myBottomPersonDialog.setOnItemClickListener((position, user) -> {
//            switch (position) {
//                case 0://查看资料
//                    onDataShow(userId, user.getId());
//
//
//                    break;
//                case 8://私信
//                    onChatTo(user.getId(),user.getName());
//                    break;
//            }
//        });
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

    static public RankFragment newInstance(int type) { //2魅力榜 1土豪榜
        RankFragment rankFragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
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


        @BindView(R.id.iv_my)
        SimpleDraweeView ivMy;
        @BindView(R.id.tv_my)
        TextView tvMy;
        @BindView(R.id.tv_my_num)
        TextView tvMyNum;
        @BindView(R.id.tv_my_rank)
        TextView tvMyRank;
        @BindView(R.id.rl_mine)
        RelativeLayout rl_mine;
        @BindView(R.id.cl_bg)
        ConstraintLayout cl_bg;

        @BindView(R.id.line_view)
        View line_view;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
