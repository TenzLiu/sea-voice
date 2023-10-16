package com.jingtaoi.yy.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.RankBean;
import com.jingtaoi.yy.dialog.MyBottomPersonDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.home.adapter.RankAdapter;
import com.jingtaoi.yy.ui.home.fragment.RankFragment;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;

public class RoomListActivity extends MyBaseActivity {

    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshRecyclerLayout mSwipeRefreshLayout;

    private RankAdapter adapter;
    private ArrayList<RankBean.DataBean> users = new ArrayList<>();
    int userToken;


    private ViewHolder viewHolder;
    private int textColor = Color.parseColor("#4BA6DC");


    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_room_list);
        showTitle(false);
        showHeader(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        setBlcakShow(false);
        findViewById(R.id.tv_left).setOnClickListener(v->{
            finish();
        });
        userToken = (int) SharedPreferenceUtils.get(this, Const.User.USER_TOKEN, 0);

        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new RankAdapter(Color.parseColor("#D92F9F"),users, 2, userToken);
        adapter.setModeLiang(true);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_rank_header, mSwipeRefreshLayout,false);
        ImageView  phb_iv=view.findViewById(R.id.phb_iv);

        phb_iv.setImageResource(R.drawable.fjb_bg);
        viewHolder = new ViewHolder(view);
//        if (type==1)
//        {
//            viewHolder.zan_icon_iv1.setImageResource(R.drawable.icon_xz);
//            viewHolder.zan_icon_iv2.setImageResource(R.drawable.icon_xz);
//            viewHolder.zan_icon_iv3.setImageResource(R.drawable.icon_xz);
//        }else {
//            viewHolder.zan_icon_iv1.setImageResource(R.drawable.icon_ml);
//            viewHolder.zan_icon_iv2.setImageResource(R.drawable.icon_ml);
//            viewHolder.zan_icon_iv3.setImageResource(R.drawable.icon_ml);
//        }
        viewHolder.zan_icon_iv1.setImageResource(R.drawable.icon_ml);
        viewHolder.zan_icon_iv2.setImageResource(R.drawable.icon_ml);
        viewHolder.zan_icon_iv3.setImageResource(R.drawable.icon_ml);

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

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }




    private int type=1;
    private int state = 1;

    private void getData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("type", type);
        map.put("state", state);
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.CHARM_ROOMBILLS, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);
                RankBean baseBean = JSON.parseObject(responseString, RankBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    RankBean.DataBean myData = baseBean.getData().myData;
                    viewHolder.ivMy.setImageURI(myData.getImgTx());
                    viewHolder.tvMy.setText(myData.getNickname());
                    viewHolder.tvMyNum.setText("ID:"+myData.getUsercoding());
                    viewHolder.tvMyNum.setCompoundDrawablesWithIntrinsicBounds(myData.getIsliang()==1?R.drawable.liang_id:0,
                            0,0,0);

                    viewHolder.tvMyRank.setText(myData.rankNum == 0?"未上榜":String.format(Locale.CHINA,"%02d",myData.rankNum));
                    viewHolder.tvMyRank.setTextSize(TypedValue.COMPLEX_UNIT_SP,myData.rankNum == 0?10:24);
//                    viewHolder.tvMyRank.setTextColor(Color.parseColor(myData.rankNum == 0? "#9FA3B0":"#4BA6DC"));

                    List<RankBean.DataBean> data = baseBean.getData().list;
                    users.clear();
                    if (data.isEmpty()) {
//                        viewHolder.ivChampion.setVisibility(View.INVISIBLE);
//                        viewHolder.iv2nd.setVisibility(View.INVISIBLE);
//                        viewHolder.iv3rd.setVisibility(View.INVISIBLE);
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
                        viewHolder.ivChampion.setVisibility(View.VISIBLE);
                        viewHolder.ivChampion.setImageURI(data.get(0).getImg());
//                        viewHolder.iv2nd.setVisibility(View.INVISIBLE);
//                        viewHolder.iv3rd.setVisibility(View.INVISIBLE);
                        viewHolder.iv2nd.setImageURI("");
                        viewHolder.iv3rd.setImageURI("");
                        viewHolder.tvChampion.setText(data.get(0).getName());
                        viewHolder.tv2nd.setText("暂无");
                        viewHolder.tv3rd.setText("暂无");
//                        viewHolder.tvChampion.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, type == 2 ? ImageShowUtils.getCharm(data.get(0).getGrade()) : ImageShowUtils.getGrade(data.get(0).getGrade()));
//                        viewHolder.tv2nd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                        viewHolder.tv3rd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        viewHolder.tvOffset1.setVisibility(View.INVISIBLE);
                        viewHolder.tvOffset2.setVisibility(View.INVISIBLE);
                        viewHolder.tvAccount1.setVisibility(View.VISIBLE);
                        viewHolder.tvAccount1.setText("ID:" + data.get(0).getUsercoding());

                        if (data.get(0).getIsliang() != 1){
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }

                        viewHolder.tvAccount2.setVisibility(View.INVISIBLE);
                        viewHolder.tvAccount3.setVisibility(View.INVISIBLE);
                        viewHolder.ivChampion.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(0).getId());
                        });
//                        viewHolder.xuzanTv1.setText(data.get(0).getNum()+"");
//                        viewHolder.xuzanTv2.setText("0");
//                        viewHolder.xuzanTv3.setText("0");
//                        // 排行榜只能看到自己
//                        if (userToken == data.get(0).getId()){
//                            viewHolder.xuzanLay1.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.xuzanLay1.setVisibility(View.INVISIBLE);
//                        }
//                        viewHolder.xuzanLay2.setVisibility(View.INVISIBLE);
//                        viewHolder.xuzanLay3.setVisibility(View.INVISIBLE);
                    } else if (data.size() == 2) {
                        viewHolder.ivChampion.setVisibility(View.VISIBLE);
                        viewHolder.ivChampion.setImageURI(data.get(0).getImg());
                        viewHolder.iv2nd.setVisibility(View.VISIBLE);
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
                        viewHolder.tvAccount1.setText("ID:" + data.get(0).getUsercoding());

                        if (data.get(0).getIsliang() != 1){
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount2.setVisibility(View.VISIBLE);
                        viewHolder.tvAccount2.setText("ID:" + data.get(1).getUsercoding());

                        if (data.get(1).getIsliang() != 1){
                            viewHolder.tvAccount2.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount3.setVisibility(View.GONE);
                        viewHolder.ivChampion.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(0).getId());
                        });
                        viewHolder.iv2nd.setOnClickListener(v -> {
                            showMyPseronDialog(userToken, data.get(1).getId());
                        });

//                        viewHolder.xuzanTv1.setText(data.get(0).getNum()+"");
//                        viewHolder.xuzanTv2.setText(data.get(1).getNum()+"");
//                        viewHolder.xuzanTv3.setText("0");
                        // 排行榜只能看到自己
//                        if (userToken == data.get(0).getId()){
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
//                        viewHolder.tvChampion.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, type == 2 ? ImageShowUtils.getCharm(data.get(0).getGrade()) : ImageShowUtils.getGrade(data.get(0).getGrade()));
//                        viewHolder.tv2nd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, type == 2 ? ImageShowUtils.getCharm(data.get(1).getGrade()) : ImageShowUtils.getGrade(data.get(1).getGrade()));
//                        viewHolder.tv3rd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, type == 2 ? ImageShowUtils.getCharm(data.get(2).getGrade()) : ImageShowUtils.getGrade(data.get(2).getGrade()));
                        viewHolder.tvAccount1.setVisibility(View.VISIBLE);
                        viewHolder.tvAccount1.setText("ID:" + data.get(0).getUsercoding());

                        if (data.get(0).getIsliang() != 1){
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount2.setVisibility(View.VISIBLE);
                        viewHolder.tvAccount2.setText("ID:" + data.get(1).getUsercoding());

                        if (data.get(1).getIsliang() != 1){
                            viewHolder.tvAccount2.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
                            viewHolder.tvAccount2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liang_id, 0,0,0);
                        }
                        viewHolder.tvAccount3.setVisibility(View.VISIBLE);
                        viewHolder.tvAccount3.setText("ID:" + data.get(2).getUsercoding());

                        if (data.get(2).getIsliang() != 1){
                            viewHolder.tvAccount3.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,0);
                        }else { //
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

//                        viewHolder.xuzanTv1.setText(data.get(0).getNum()+"");
//                        viewHolder.xuzanTv2.setText(data.get(1).getNum()+"");
//                        viewHolder.xuzanTv3.setText(data.get(2).getNum()+"");
//                        // 排行榜只能看到自己
//                        if (userToken == data.get(0).getId()){
//                            viewHolder.xuzanLay1.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.xuzanLay1.setVisibility(View.INVISIBLE);
//                        }
//                        if (userToken == data.get(1).getId()){
//                            viewHolder.xuzanLay2.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.xuzanLay2.setVisibility(View.INVISIBLE);
//                        }
//                        if (userToken == data.get(2).getId()){
//                            viewHolder.xuzanLay3.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.xuzanLay3.setVisibility(View.INVISIBLE);
//                        }
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
    MyBottomPersonDialog myBottomPersonDialog;

    private void showMyPseronDialog(int userId, int otherId) {
//        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
//            myBottomPersonDialog.dismiss();
//        }
//        myBottomPersonDialog = new MyBottomPersonDialog(this, userId, otherId);
//        myBottomPersonDialog.show();
    }



    static public RankFragment newInstance(int type) { //2魅力榜 1土豪榜
        RankFragment rankFragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        rankFragment.setArguments(bundle);
        return rankFragment;
    }




    class ViewHolder {
        @BindView(R.id.rg_top)
        RadioGroup rgTop;
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


        @BindView(R.id.iv_my)
        SimpleDraweeView ivMy;
        @BindView(R.id.tv_my)
        TextView tvMy;
        @BindView(R.id.tv_my_num)
        TextView tvMyNum;
        @BindView(R.id.tv_my_rank)
        TextView tvMyRank;
        @BindView(R.id.cl_bg)
        ConstraintLayout cl_bg;

        @BindView(R.id.line_view)
        View line_view;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
