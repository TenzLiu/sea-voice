package com.siyecaoi.yy.ui.room;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.ThemeBackBean;
import com.siyecaoi.yy.bean.VoiceHomeBean;
import com.siyecaoi.yy.bean.VoiceUserBean;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.adapter.RoomBackListAdapter;
import com.siyecaoi.yy.ui.room.adapter.RoomBgListAdapter;
import com.siyecaoi.yy.ui.room.adapter.RoomsetListAdapter;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.view.FlowLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 房间设置页面
 */
public class RoomSetActivity extends MyBaseActivity {
    @BindView(R.id.edt_name_roomset)
    EditText edtNameRoomset;
    @BindView(R.id.edt_pass_roomset)
    EditText edtPassRoomset;
    @BindView(R.id.rl_topic_roomset)
    RelativeLayout rlTopicRoomset;
    @BindView(R.id.tv_default_roomset)
    TextView tvDefaultRoomset;
    @BindView(R.id.rl_back_roomset)
    RelativeLayout rlBackRoomset;
    @BindView(R.id.iv_switch_roomset)
    ImageView ivSwitchRoomset;
    @BindView(R.id.rl_shield_roomset)
    RelativeLayout rlShieldRoomset;
    @BindView(R.id.rl_hint_roomset)
    RelativeLayout rlHintRoomset;
    @BindView(R.id.rl_balck_roomset)
    RelativeLayout rlBalckRoomset;
    @BindView(R.id.tv_save_roomset)
    Button tvSaveRoomset;
    @BindView(R.id.rl_admin_roomset)
    RelativeLayout rlAdminRoomset;

    RoomsetListAdapter roomsetListAdapter;
    List<String> listMark;
    @BindView(R.id.mRecyclerView_roomset)
    RecyclerView mRecyclerViewRoomset;
    @BindView(R.id.rv_bg)
    RecyclerView rv_bg;
    String roomId;
    RoomBgListAdapter roomBackListAdapter;

    String roomTopic, topicCount;//房间话题及内容
    int giftshow;//是否屏蔽低价值礼物特效
    String roomName, roomPass, roomMark, roomback;
    String roomHint;//进入房间提示
    String roomBackImg = "";
    @BindView(R.id.rl_clear_roomset)
    RelativeLayout rlClearRoomset;
    @BindView(R.id.fl_roomSet)
    FlowLayout flRoomSet;
    private int isPay;//是否代充  1开启
    private int isGift;//是否开启礼物赠送限制  1开启

    @Override
    public void initData() {
        roomId = getBundleString(Const.ShowIntent.ROOMID);

        listMark = new ArrayList<>();
        listMark.add(getString(R.string.mark_1));
        listMark.add(getString(R.string.mark_2));
        listMark.add(getString(R.string.mark_3));
        listMark.add(getString(R.string.mark_4));
        listMark.add(getString(R.string.mark_5));
        listMark.add(getString(R.string.mark_6));
        listMark.add(getString(R.string.mark_7));
        listMark.add(getString(R.string.mark_8));
        listMark.add(getString(R.string.mark_9));
        listMark.add(getString(R.string.mark_10));
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_roomset);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_roomset);
        setTitleBarColor(R.color.white_color);
        setTitleBackGroundColor(R.color.white_color);
        showLine(false);
//        setRecycler();
        setBgRecycler();
        getBGCall();
        setFlowLayout();
        getCall();
    }

    private int chooseOne = -1;


    private void setFlowLayout() {
        flRoomSet.removeAllViews();
        for (int i = 0; i < listMark.size() - 1; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_mark_key, null);
            TextView tv = view.findViewById(R.id.tv_key);
            tv.setText(listMark.get(i));
            int bgRes = 0;
            switch (i%7){
                case 0:
                    bgRes = R.drawable.bg_tag_purple;
                    break;
                case 1:
                    bgRes = R.drawable.bg_tag_green_17dp;
                    break;
                case 2:
                    bgRes = R.drawable.bg_tag_orange_17dp;
                    break;
                case 3:
                    bgRes = R.drawable.bg_tag_blue1_17dp;
                    break;
                case 4:
                    bgRes = R.drawable.bg_tag_blue2_17dp;
                    break;
                case 5:
                    bgRes = R.drawable.bg_tag_blue3_17dp;
                    break;
                case 6:
                    bgRes = R.drawable.bg_tag_pink_17dp;
                    break;
            }
            tv.setBackgroundResource(bgRes);
            if (chooseOne == i) {
                tv.setAlpha(1);
            } else {
                tv.setAlpha(0.3f);
            }
            int finalI = i;
            tv.setOnClickListener(v -> {
                chooseOne = finalI;
                roomMark = listMark.get(chooseOne);
                setFlowLayout();
            });
            flRoomSet.addView(view);
        }
    }

    private void setBgRecycler(){
        roomBackListAdapter = new RoomBgListAdapter(R.layout.item_roombg);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rv_bg.setLayoutManager(layoutManager);
        rv_bg.setAdapter(roomBackListAdapter);
        roomBackListAdapter.setRoomImg(roomBackImg);

        roomBackListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ThemeBackBean.DataBean dataBean = (ThemeBackBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                roomBackImg = dataBean.getImg();
                roomback = dataBean.getName();
                roomBackListAdapter.setRoomImg(roomBackImg);
            }
        });
    }

    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("pid", roomId);
        HttpManager.getInstance().post(Api.getVoiceHome, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                VoiceHomeBean voiceUserBean = JSON.parseObject(responseString, VoiceHomeBean.class);
                if (voiceUserBean.getCode() == 0) {
                    setShow(voiceUserBean.getData().getRoom());
                    for (VoiceUserBean.DataBean dataOne : voiceUserBean.getData().getUserModel()) {
                        if (dataOne.getId() == userToken) { //当前用户
                            if (dataOne.getType() == 1) {
                                rlAdminRoomset.setVisibility(View.VISIBLE);
                            } else if (dataOne.getType() == 2) {
                                rlAdminRoomset.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    showToast(voiceUserBean.getMsg());
                }
            }
        });
    }


    private void setShow(VoiceHomeBean.DataBean.RoomBean roomBean) {
        roomName = roomBean.getRoomName();
        roomPass = roomBean.getPassword();
        roomback = roomBean.getBjImg();
        roomMark = roomBean.getRoomLabel();
        isPay = roomBean.getIsPay();
        isGift = roomBean.getIsGift();
        edtNameRoomset.setText(roomName);
        edtPassRoomset.setText(roomPass);
        tvDefaultRoomset.setText(roomBean.getBjName());
        if (StringUtils.isEmpty(roomMark)) {
            roomMark = getString(R.string.mark_2);
//            roomsetListAdapter.setChooseMark(roomMark);
            chooseOne = 0;
            setFlowLayout();
        } else {
//            roomsetListAdapter.setChooseMark(roomMark);
            for (int i = 0; i < listMark.size(); i++) {
                if (listMark.get(i).equals(roomMark)) {
                    chooseOne = i;
                    break;
                }
            }
            setFlowLayout();
        }
        roomTopic = roomBean.getRoomTopic();
        topicCount = roomBean.getRoomCount();
        giftshow = roomBean.getIsState();
        roomHint = roomBean.getRoomHint();
        roomBackImg = roomBean.getBjImg();
        roomBackListAdapter.setRoomImg(roomBackImg);
        if (giftshow == 1) {
            ivSwitchRoomset.setSelected(false);
        } else if (giftshow == 2) {
            ivSwitchRoomset.setSelected(true);
        }

        rlBackRoomset.setVisibility(View.VISIBLE);
//        if (roomBean.getState() == 1) {
//            rlBackRoomset.setVisibility(View.GONE);
//        } else if (roomBean.getState() == 2) {
//            rlBackRoomset.setVisibility(View.VISIBLE);
//        }
    }

    private void setRecycler() {
        roomsetListAdapter = new RoomsetListAdapter(R.layout.item_roomset, listMark);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        mRecyclerViewRoomset.setLayoutManager(layoutManager);
        mRecyclerViewRoomset.setAdapter(roomsetListAdapter);

        roomsetListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                roomMark = (String) adapter.getItem(position);
                roomsetListAdapter.setChooseMark(roomMark);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101) {
            if (data != null) {
                String roomBackName = data.getStringExtra(Const.ShowIntent.NAME);
                tvDefaultRoomset.setText(roomBackName);
            }
        }
    }

    private void getBGCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.SaveRoomBackdrop, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                ThemeBackBean themeBackBean = JSON.parseObject(responseString, ThemeBackBean.class);
                if (themeBackBean.getCode() == Api.SUCCESS) {
                    roomBackListAdapter.setNewData(themeBackBean.getData());
                } else {
                    showToast(themeBackBean.getMsg());
                }
            }
        });
    }

    @OnClick({R.id.rl_topic_roomset, R.id.rl_back_roomset, R.id.iv_switch_roomset,
            R.id.rl_admin_roomset, R.id.rl_hint_roomset, R.id.rl_balck_roomset,
            R.id.tv_save_roomset, R.id.rl_clear_roomset})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.rl_topic_roomset:
                bundle.putString(Const.ShowIntent.TOPIC, roomTopic);
                bundle.putString(Const.ShowIntent.DATA, topicCount);
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(SetTopicActivity.class, bundle);
                break;
            case R.id.rl_back_roomset:
//                bundle.putString(Const.ShowIntent.IMG, roomBackImg);
//                bundle.putString(Const.ShowIntent.ROOMID, roomId);
//                ActivityCollector.getActivityCollector().toOtherActivity(BackSetActivity.class, bundle, 101);
                break;
            case R.id.iv_switch_roomset:
                if (giftshow == 1) {
                    ivSwitchRoomset.setSelected(true);
                    giftshow = 2;
                } else if (giftshow == 2) {
                    ivSwitchRoomset.setSelected(false);
                    giftshow = 1;
                }
                break;
            case R.id.rl_admin_roomset:
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(RoomAdminActivity.class, bundle);
                break;
            case R.id.rl_hint_roomset:
                bundle.putString(Const.ShowIntent.DATA, roomHint);
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(RoomhintActivity.class, bundle);
                break;
            case R.id.rl_balck_roomset:
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(BlackRoomActivity.class, bundle);
                break;
            case R.id.tv_save_roomset:
                getUpdateCall();
                break;
            case R.id.rl_clear_roomset:
                showClearDialog();
                break;
        }
    }

    MyDialog myDialog;

    private void showClearDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("确认清空礼物值？");
        myDialog.setRightButton(v -> {
            getClearCall();
        });
    }

    private void getClearCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.RoomDel, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {

                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    showToast("清除成功");
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void getUpdateCall() {
        roomName = edtNameRoomset.getText().toString();
        roomPass = edtPassRoomset.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", userToken);
        map.put("roomName", roomName);
        map.put("password", roomPass);
        map.put("roomLabel", roomMark);
        map.put("isState", giftshow);
        map.put("bjImg", roomBackImg);
        map.put("bjName", roomback);
        map.put("isPay", isPay);
        map.put("isGift", isGift);
        HttpManager.getInstance().post(Api.getUpRoom, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    showToast(getString(R.string.hint_save_topic));
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }
}
