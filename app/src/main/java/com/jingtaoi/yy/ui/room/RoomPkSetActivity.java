package com.jingtaoi.yy.ui.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.ui.room.adapter.BottomOnlinesAdapter;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.PkSetBean;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.dialog.MyBottomShowDialog;
import com.jingtaoi.yy.dialog.MyOnlineUserDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * pk设置页面
 */
public class RoomPkSetActivity extends MyBaseActivity {
    @BindView(R.id.iv_one_roompk)
    SimpleDraweeView ivOneRoompk;
    @BindView(R.id.iv_two_roompk)
    SimpleDraweeView ivTwoRoompk;
    @BindView(R.id.tv_type_price_roompk)
    TextView tvTypePriceRoompk;
    @BindView(R.id.rl_type_roompk)
    RelativeLayout rlTypeRoompk;
    @BindView(R.id.tv_time_price_roompk)
    TextView tvTimePriceRoompk;
    @BindView(R.id.rl_time_roompk)
    RelativeLayout rlTimeRoompk;
    @BindView(R.id.btn_affirm_roompk)
    Button btnAffirmRoompk;
    @BindView(R.id.tv_pk_roompk)
    TextView tvPkRoompk;

    MyBottomShowDialog myBottomShowDialog;
    private ArrayList<String> bottomList;//弹框显示内容
    int state;//1 按人数投票，2 按礼物价值投票
    int num;//好多秒
    String roomId;//
    int chooseOne;//选择的位置 1 2
    int uid;//第一个人的id
    int buid;//第二个人的id

    MyOnlineUserDialog onlineUserDialog;

    @Override
    public void initData() {
        roomId = getBundleString(Const.ShowIntent.ROOMID);
        bottomList = new ArrayList<>();
        state = 2;
        num = 60;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_roompk);
    }

    @Override
    public void initView() {
        setTitleText(R.string.title_roompk);

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/impact.ttf");
        tvPkRoompk.setTypeface(typeface);

        setRightText(R.string.tv_record_roompk);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Const.User.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(PkHistoryActivity.class, bundle, 101);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            ActivityCollector.getActivityCollector().finishActivity(RoomPkSetActivity.class);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_one_roompk, R.id.iv_two_roompk, R.id.rl_type_roompk,
            R.id.rl_time_roompk, R.id.btn_affirm_roompk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_one_roompk:
                chooseOne = 1;
                showMyOnline();
                break;
            case R.id.iv_two_roompk:
                chooseOne = 2;
                showMyOnline();
                break;
            case R.id.rl_type_roompk:
                setTypeClicker();
                break;
            case R.id.rl_time_roompk:
                setTimeClicker();
                break;
            case R.id.btn_affirm_roompk:
                getCall();
                break;
        }
    }

    private void getCall() {
        if (uid == 0 || buid == 0) {
            showToast("请选择PK对象");
            return;
        }
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", uid);
        map.put("buid", buid);
        map.put("state", state);
        map.put("num", num);
        HttpManager.getInstance().post(Api.ChatroomsPk, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                PkSetBean pkSetBean = JSON.parseObject(responseString, PkSetBean.class);
                if (pkSetBean.getCode() == Api.SUCCESS) {
                    showToast("开启Pk成功");
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(pkSetBean.getMsg());
                }
            }
        });
    }

    private void showMyOnline() {
        if (onlineUserDialog != null && onlineUserDialog.isShowing()) {
            onlineUserDialog.dismiss();
        }
        onlineUserDialog = new MyOnlineUserDialog(this, roomId, userToken, Const.IntShow.TWO);
        onlineUserDialog.show();
        BottomOnlinesAdapter bottomOnlinesAdapter = onlineUserDialog.getAdapter();
        bottomOnlinesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onlineUserDialog != null && onlineUserDialog.isShowing()) {
                    onlineUserDialog.dismiss();
                }
                VoiceUserBean.DataBean dataBean = (VoiceUserBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                if (chooseOne == 1) {
                    uid = dataBean.getId();
                    ImageUtils.loadUri(ivOneRoompk, dataBean.getImg());
                    if (uid == buid) {
                        ImageUtils.loadDrawable(ivTwoRoompk, R.drawable.add_photo);
                        buid = 0;
                    } else if (buid != 0) {
                        btnAffirmRoompk.setAlpha((float) 1.0);
                    }
                } else if (chooseOne == 2) {
                    buid = dataBean.getId();
                    ImageUtils.loadUri(ivTwoRoompk, dataBean.getImg());
                    if (uid == buid) {
                        ImageUtils.loadDrawable(ivOneRoompk, R.drawable.add_photo);
                        uid = 0;
                    } else if (uid != 0) {
                        btnAffirmRoompk.setAlpha((float) 1.0);
                    }
                }
            }
        });
    }

    private void setTimeClicker() {
        BottomShowRecyclerAdapter bottomShowRecyclerAdapter = showMybottomDialog(initTimeShow(), this);
        bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
//                String timeShow = (String) adapter.getItem(position);

                switch (position) {
                    case 0:
                        num = 30;
                        break;
                    case 1:
                        num = 60;
                        break;
                    case 2:
                        num = 90;
                        break;
                    case 3:
                        num = 5 * 60;
                        break;
                    case 4:
                        num = 10 * 60;
                        break;
                    case 5:
                        num = 20 * 60;
                        break;
                }
//                tvTimePriceRoompk.setText(num + "S");
                if (position!=6){
                    tvTimePriceRoompk.setText(bottomList.get(position));
                }
            }
        });
    }

    private ArrayList<String> initTimeShow() {
        bottomList.clear();
        bottomList.add("30秒");
        bottomList.add("60秒");
        bottomList.add("90秒");
        bottomList.add("5分钟");
        bottomList.add("10分钟");
        bottomList.add("20分钟");
        bottomList.add(getString(R.string.tv_cancel));
        return bottomList;
    }

    private void setTypeClicker() {
        BottomShowRecyclerAdapter bottomShowRecyclerAdapter = showMybottomDialog(initTypeShow(), this);
        bottomShowRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
                switch (position) {
                    case 0:
                        state = 2;
                        tvTypePriceRoompk.setText(getString(R.string.tv_type_price_roompk));
                        break;
                    case 1:
                        state = 1;
                        tvTypePriceRoompk.setText(getString(R.string.tv_type_number_roompk));
                        break;

                }
            }
        });
    }

    private ArrayList<String> initTypeShow() {
        bottomList.clear();
        bottomList.add(getString(R.string.tv_type_price_show_roompk));
        bottomList.add(getString(R.string.tv_type_number_show_roompk));
        bottomList.add(getString(R.string.tv_cancel));
        return bottomList;
    }


    private BottomShowRecyclerAdapter showMybottomDialog(ArrayList<String> seatList, Context context) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(context, seatList);
        myBottomShowDialog.show();
        return myBottomShowDialog.getAdapter();
    }
}
