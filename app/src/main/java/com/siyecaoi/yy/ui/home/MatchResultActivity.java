package com.siyecaoi.yy.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.HomeItemData;
import com.siyecaoi.yy.bean.HomeTuijianBean;
import com.siyecaoi.yy.dialog.MatchEmptyDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.home.adapter.MatchResultAdapter;
import com.siyecaoi.yy.ui.message.OtherHomeActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.view.swipeView.SwipeFlingView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchResultActivity extends MyBaseActivity implements SwipeFlingView.OnSwipeFlingListener {
    @BindView(R.id.card_view)
    SwipeFlingView cardView;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_match_result);
    }

    private MatchResultAdapter adapter;
    private ArrayList<HomeItemData> datas = new ArrayList<>();

    @Override
    public void initView() {
        setTitleText("速配");
        setTitleBarColor(R.color.sp_color);
        setToobarColor(R.color.sp_color);
        showToolbarLine(View.GONE);
//        setRightText(R.string.voice_sign);
//        setRightButton(v -> {
//            VoiceSignDialog dialog = new VoiceSignDialog();
//            dialog.setCallbak(new VoiceSignDialog.UpdateCallbak() {
//                @Override
//                public void onUpdateVoice() {
//                }
//            });
//            dialog.show(getSupportFragmentManager(), "voice");
//        });
        cardView.setOnSwipeFlingListener(this);
        adapter = new MatchResultAdapter(this, datas);
        cardView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", SharedPreferenceUtils.get(this, Const.User.USER_TOKEN, 0));
        map.put("lat", MyApplication.lat);
        map.put("lon", MyApplication.lon);
        HttpManager.getInstance().post(Api.MATCH, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                Gson gson = new Gson();
                HomeTuijianBean homeTuijianBean = gson.fromJson(responseString, HomeTuijianBean.class);
                if (homeTuijianBean.getCode() == 0) {
                    datas.clear();
                    List<HomeItemData> data = homeTuijianBean.getData();
                    if (data.isEmpty()) {
                        MatchEmptyDialog matchEmptyDialog = new MatchEmptyDialog();
                        matchEmptyDialog.show(getSupportFragmentManager(), "nodata");
                    } else {
                        datas.addAll(data);
                        adapter.notifyDataSetChanged();
                        cardView.setmCurPositon(0);
                    }
                } else {
                    showToast(homeTuijianBean.getMsg());
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

    //关注
    private void getAttentionCall(HomeItemData data) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", data.getId());
        map.put("type", 1);
        HttpManager.getInstance().post(Api.addAttention, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    Log.e("mmp", "关注成功:" + data.getName());
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.navigation_ll, R.id.pass_ll, R.id.like_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_ll:
                Bundle bundle = new Bundle();
//                bundle.putString(Const.ShowIntent.ROOMID, datas.get(cardView.getCurPositon()).getUsercoding());
//                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                bundle.putInt(Const.ShowIntent.ID, datas.get(cardView.getCurPositon()).getId());
                ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
                break;
            case R.id.pass_ll:
                cardView.selectLeft();
                break;
            case R.id.like_ll:
                cardView.selectRight();
                break;
        }
    }


    @Override
    public void onStartDragCard() {
    }

    @Override
    public boolean canLeftCardExit() {
        return true;
    }

    @Override
    public boolean canRightCardExit() {
        return true;
    }

    @Override
    public void onPreCardExit() {
        adapter.stopVoice();
    }

    @Override
    public void onLeftCardExit(View view, Object dataObject, boolean triggerByTouchMove) {

    }

    @Override
    public void onRightCardExit(View view, Object dataObject, boolean triggerByTouchMove) {
        HomeItemData data = datas.get((int) dataObject - 1);
        getAttentionCall(data);
    }

    @Override
    public void onSuperLike(View view, Object dataObject, boolean triggerByTouchMove) {

    }

    @Override
    public void onTopCardViewFinish() {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {

    }

    @Override
    public void onAdapterEmpty() {
        getData();
    }

    @Override
    public void onScroll(View selectedView, float scrollProgressPercent) {

    }

    @Override
    public void onEndDragCard() {

    }
}
