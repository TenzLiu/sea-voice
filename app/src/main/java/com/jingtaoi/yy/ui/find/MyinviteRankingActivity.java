package com.jingtaoi.yy.ui.find;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.InviteRankingBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.find.adapter.InviteRankingRecyAdapter;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.MyUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 邀请奖励排行榜
 */
public class MyinviteRankingActivity extends MyBaseActivity {
    @BindView(R.id.tv_share_inviteranking)
    TextView tvShareInviteranking;
    @BindView(R.id.tv_ranking_inviteranking)
    TextView tvRankingInviteranking;
    @BindView(R.id.tv_mon_inviteranking)
    TextView tvMonInviteranking;
    @BindView(R.id.rl_show_inviteranking)
    RelativeLayout rlShowInviteranking;
    @BindView(R.id.iv_header_inviteranking)
    SimpleDraweeView ivHeaderInviteranking;
    @BindView(R.id.tv_nickname_inviteranking)
    TextView tvNicknameInviteranking;
    @BindView(R.id.mRecyclerView_inviteranking)
    RecyclerView mRecyclerViewInviteranking;

    InviteRankingRecyAdapter inviteRankingRecyAdapter;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_inviteranking);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_inviteraking);
        setRecycler();
        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.userList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                InviteRankingBean inviteRankingBean = JSON.parseObject(responseString, InviteRankingBean.class);
                if (inviteRankingBean.getCode() == Api.SUCCESS) {
                    if (inviteRankingBean.getData().getUser() != null) {
                        setHeaderShow(inviteRankingBean.getData().getUser());
                    }
                    inviteRankingRecyAdapter.setNewData(inviteRankingBean.getData().getUserList());
                } else {
                    showToast(inviteRankingBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setHeaderShow(InviteRankingBean.DataEntity.UserEntity user) {
        ImageUtils.loadUri(ivHeaderInviteranking, user.getImg());
        if (user.getNum() > 1000) {
            tvRankingInviteranking.setText("排名：1000+");
        } else {
            tvRankingInviteranking.setText("排名：" + user.getNum());
        }
        tvMonInviteranking.setText("奖励金：" + MyUtils.getInstans().doubleTwo(user.getMoney()) + "元");
        tvNicknameInviteranking.setText(user.getName());
    }

    private void setRecycler() {
        inviteRankingRecyAdapter = new InviteRankingRecyAdapter(R.layout.item_list_ranking);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewInviteranking.setAdapter(inviteRankingRecyAdapter);
        mRecyclerViewInviteranking.setLayoutManager(layoutManager);
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
