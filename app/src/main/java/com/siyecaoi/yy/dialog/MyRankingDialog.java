package com.siyecaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.ui.room.adapter.RankingListAdapter;
import com.siyecaoi.yy.bean.MyRankingBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.Toast;


/**
 * 房间榜单弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyRankingDialog extends Dialog {

    String roomId;
    RankingListAdapter rankingListAdapter;

    Context context;

    int chooseTop;//1是财富，2是魅力
    int chooseState;//1是日榜，2是周榜，3是全部
    @BindView(R.id.tv_cai_myranking)
    TextView tvCaiMyranking;
    @BindView(R.id.view_cai_myranking)
    View viewCaiMyranking;
    @BindView(R.id.rl_cai_myranking)
    RelativeLayout rlCaiMyranking;
    @BindView(R.id.tv_charm_myranking)
    TextView tvCharmMyranking;
    @BindView(R.id.view_charm_myranking)
    View viewCharmMyranking;
    @BindView(R.id.rl_charm_myranking)
    RelativeLayout rlCharmMyranking;
    @BindView(R.id.iv_close_myranking)
    ImageView ivCloseMyranking;
    @BindView(R.id.tv_day_myranking)
    TextView tvDayMyranking;
    @BindView(R.id.tv_week_myranking)
    TextView tvWeekMyranking;
    @BindView(R.id.tv_all_myranking)
    TextView tvAllMyranking;
    @BindView(R.id.mRecyclerView_myranking)
    RecyclerView mRecyclerViewMyranking;
    int userToken;


    public MyRankingDialog(Context context, String roomId, int userToken) {
        super(context, R.style.CustomDialogStyle);
        this.roomId = roomId;
        this.context = context;
        this.userToken = userToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_myranking);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);

        setRecycler();
        chooseTop = 1;
        chooseState = 1;
        getCall();
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("type", chooseTop);
        map.put("state", chooseState);
        HttpManager.getInstance().post(Api.SaveRoomCFML, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                MyRankingBean myRankingBean = JSON.parseObject(responseString, MyRankingBean.class);
                if (myRankingBean.getCode() == Api.SUCCESS) {
                    rankingListAdapter.setNewData(myRankingBean.getData());
                } else {
                    Toast.create(context).show(myRankingBean.getMsg());
                }
            }
        });
    }

    private void setRecycler() {
        rankingListAdapter = new RankingListAdapter(R.layout.item_myranking);
        rankingListAdapter.setChooseTop(Const.IntShow.ONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerViewMyranking.setLayoutManager(layoutManager);
        mRecyclerViewMyranking.setAdapter(rankingListAdapter);

        rankingListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyRankingBean.DataBean dataBean = (MyRankingBean.DataBean) adapter.getItem(position);
                showMyPseronDialog(userToken, dataBean.getId());
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
        if (myBottomPersonDialog != null && myBottomPersonDialog.isShowing()) {
            myBottomPersonDialog.dismiss();
        }
        myBottomPersonDialog = new MyBottomPersonDialog(context, userId, otherId);
        myBottomPersonDialog.show();
    }


    @OnClick({R.id.rl_cai_myranking, R.id.rl_charm_myranking, R.id.iv_close_myranking,
            R.id.tv_day_myranking, R.id.tv_week_myranking, R.id.tv_all_myranking})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_cai_myranking:
                if (chooseTop != 1) {
                    chooseTop = 1;
                    tvCaiMyranking.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    tvCaiMyranking.setTextColor(ContextCompat.getColor(context, R.color.white));
                    viewCaiMyranking.setVisibility(View.VISIBLE);

                    tvCharmMyranking.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    tvCharmMyranking.setTextColor(ContextCompat.getColor(context, R.color.white));
                    viewCharmMyranking.setVisibility(View.INVISIBLE);

                    rankingListAdapter.setChooseTop(Const.IntShow.ONE);
                    getCall();
                }
                break;
            case R.id.rl_charm_myranking:
                if (chooseTop != 2) {
                    chooseTop = 2;
                    tvCaiMyranking.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    tvCaiMyranking.setTextColor(ContextCompat.getColor(context, R.color.white));
                    viewCaiMyranking.setVisibility(View.INVISIBLE);

                    tvCharmMyranking.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    tvCharmMyranking.setTextColor(ContextCompat.getColor(context, R.color.white));
                    viewCharmMyranking.setVisibility(View.VISIBLE);

                    rankingListAdapter.setChooseTop(Const.IntShow.TWO);
                    getCall();
                }
                break;
            case R.id.iv_close_myranking:
                dismiss();
                break;
            case R.id.tv_day_myranking:
                if (chooseState != 1) {
                    setNoChoose(chooseState);
                    chooseState = 1;
                    setChoose(chooseState);
                    getCall();
                }
                break;
            case R.id.tv_week_myranking:
                if (chooseState != 2) {
                    setNoChoose(chooseState);
                    chooseState = 2;
                    setChoose(chooseState);
                    getCall();
                }
                break;
            case R.id.tv_all_myranking:
                if (chooseState != 3) {
                    setNoChoose(chooseState);
                    chooseState = 3;
                    setChoose(chooseState);
                    getCall();
                }
                break;
        }
    }

    private void setNoChoose(int chooseState) {
        switch (chooseState) {
            case 1:
                tvDayMyranking.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvDayMyranking.setBackgroundResource(R.drawable.bg_left_tt_rank);
                break;
            case 2:
                tvWeekMyranking.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvWeekMyranking.setBackgroundResource(R.drawable.bg_right_tt_white_rank);
                break;
            case 3:
                tvAllMyranking.setTextColor(ContextCompat.getColor(context, R.color.black));
                tvAllMyranking.setBackgroundResource(R.drawable.bg_right_white_rank);
                break;

        }
    }

    private void setChoose(int chooseState) {
        switch (chooseState) {
            case 1:
                tvDayMyranking.setTextColor(ContextCompat.getColor(context, R.color.black));
                tvDayMyranking.setBackgroundResource(R.drawable.bg_left_white_rank);
                break;
            case 2:
                tvWeekMyranking.setTextColor(ContextCompat.getColor(context, R.color.black));
                tvWeekMyranking.setBackgroundResource(R.drawable.bg_right_white_rank);
                break;
            case 3:
                tvAllMyranking.setTextColor(ContextCompat.getColor(context, R.color.black));
                tvAllMyranking.setBackgroundResource(R.drawable.bg_right_white_rank);
                break;
        }
    }
}