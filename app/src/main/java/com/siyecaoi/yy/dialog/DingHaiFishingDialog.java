package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.alibaba.fastjson.JSON;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.ChestOpenBean;
import com.siyecaoi.yy.bean.DoubleBean;
import com.siyecaoi.yy.bean.FishBean;
import com.siyecaoi.yy.bean.MicXgfjBean;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.WalletActivity;
import com.siyecaoi.yy.ui.room.dialog.DanJiangDialog;
import com.siyecaoi.yy.ui.room.dialog.DanJiangRealtimeDialog;
import com.siyecaoi.yy.ui.room.dialog.DanRankDialog;
import com.siyecaoi.yy.ui.room.dialog.ExplainDialog;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.AtsAnimationUtils;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.view.FrameAnimation;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 定海神针弹窗（银宝箱）
 */

public class DingHaiFishingDialog extends Dialog implements FrameAnimation.AnimationListener {

    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @BindView(R.id.tv_gold)
    TextView tvGold;
    @BindView(R.id.iv_cout1)
    ImageView ivCout1;
    @BindView(R.id.iv_cout10)
    ImageView ivCout10;
    @BindView(R.id.iv_cout100)
    ImageView ivCout100;
    @BindView(R.id.iv_skip)
    ImageView iv_skip;
    @BindView(R.id.iv_treasure)
    ImageView iv_treasure;
    @BindView(R.id.iv_anim)
    ImageView iv_anim;

    int userId;
    Activity activity;

    String roomId;
    ChestOpenBean.DataBean data;
    private int deepReal = 0;//深海奖池实时数据
    private int chooseOne;//
    private List<FishBean> fishBeans = new ArrayList<>();
    private Map<Integer,Integer> fishBeanViewMap = new HashMap<>();

    boolean isSkipAnima = false;

    public DingHaiFishingDialog(Activity activity, int userId, String roomId) {
        super(activity, R.style.CustomDialogStyle);
        this.activity = activity;
        this.userId = userId;
        this.roomId = roomId;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_ding_hai_fishing);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (ScreenUtil.getScreenWidth(activity));
        lp.height = (int) (ScreenUtil.getScreenHeight(activity) * 0.6);
        getWindow().setAttributes(lp);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });

        initFishing();
        getCall();
//        getRealtimeCall();
    }

    private void initFishing() {
        fishBeanViewMap.put(99999,R.mipmap.ic_ding_hai_fishing_01);
        fishBeanViewMap.put(66666,R.mipmap.ic_ding_hai_fishing_02);
        fishBeanViewMap.put(20000,R.mipmap.ic_ding_hai_fishing_05);
        fishBeanViewMap.put(30000,R.mipmap.ic_ding_hai_fishing_04);
        fishBeanViewMap.put(10,R.mipmap.ic_ding_hai_fishing_13);
        fishBeanViewMap.put(5000,R.mipmap.ic_ding_hai_fishing_07);
        fishBeanViewMap.put(10000,R.mipmap.ic_ding_hai_fishing_06);
        fishBeanViewMap.put(3000,R.mipmap.ic_ding_hai_fishing_08);
        fishBeanViewMap.put(1000,R.mipmap.ic_ding_hai_fishing_14);
        fishBeanViewMap.put(52000,R.mipmap.ic_ding_hai_fishing_03);
        fishBeanViewMap.put(500,R.mipmap.ic_ding_hai_fishing_10);
        fishBeanViewMap.put(188,R.mipmap.ic_ding_hai_fishing_11);
        fishBeanViewMap.put(2000,R.mipmap.ic_ding_hai_fishing_09);
        fishBeanViewMap.put(52,R.mipmap.ic_ding_hai_fishing_15);
        isSkipAnima = (boolean) SharedPreferenceUtils.get(getContext(), Const.User.IS_DING_HAI_SKIP, false);
        if (isSkipAnima) {
            iv_skip.setImageResource(R.mipmap.icon_skip_off);
        } else {
            iv_skip.setImageResource(R.mipmap.icon_skip_on);
        }
    }


    private void getCall() {
        int userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);

        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.getUserInfo, map, new MyObserver(getContext()) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    if (userBean.getData() != null) {
                        SharedPreferenceUtils.put(activity, Const.User.GOLD, userBean.getData().getGold());
                        tvGold.setText(userBean.getData().getGold() + "");
                    }
                }
            }
        });

    }

    /**
     * 获取实时奖池
     */
    private void getRealtimeCall() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", 2);
        HttpManager.getInstance().post(Api.jackpotSize, params, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                DoubleBean getOneBean = JSON.parseObject(responseString, DoubleBean.class);
                if (getOneBean.code == Api.SUCCESS) {
                    int data = (int) getOneBean.data;
                    deepReal = data;
                    String s = deepReal + "";
                } else {
                    showToast(getOneBean.msg);
                }
            }
        });
    }

    @OnClick({R.id.tv_gold, R.id.iv_skip, R.id.ll_coin,
            R.id.iv_jackpot, R.id.iv_record, R.id.iv_method, R.id.iv_rankList, R.id.iv_cout1, R.id.iv_cout10, R.id.iv_cout100})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_jackpot:
                showDanJiangDialog();
                break;
            case R.id.iv_method:
                showExplainDialog();
                break;
            case R.id.iv_record:
                showBottom(2);
                break;
            case R.id.iv_rankList:
                showRankDialog();
                break;
            case R.id.iv_cout1:
                chooseOne = 1;
                setZaClicker();
                break;
            case R.id.iv_cout10:
                chooseOne = 2;
                setZaClicker();
                break;
            case R.id.iv_cout100:
                chooseOne = 3;
                setZaClicker();
                break;
            case R.id.ll_coin:
            case R.id.tv_gold:
                ActivityCollector.getActivityCollector().toOtherActivity(WalletActivity.class);
                break;
            case R.id.iv_skip:
                if (isSkipAnima) {
                    iv_skip.setImageResource(R.mipmap.icon_skip_on);
                } else {
                    iv_skip.setImageResource(R.mipmap.icon_skip_off);
                }
                isSkipAnima = !isSkipAnima;
                SharedPreferenceUtils.put(getContext(), Const.User.IS_DING_HAI_SKIP, isSkipAnima);
                break;
        }
    }


    private void showDanJiangDialog() {
        DanJiangDialog danJiangDialog = new DanJiangDialog(activity, 2, fishBeanViewMap);
        danJiangDialog.show();
    }

    private void showDanJiangRealtimeDialog() {
        DanJiangRealtimeDialog danJiangDialog = new DanJiangRealtimeDialog(activity, 2);
        danJiangDialog.show();
    }

    private void showRankDialog() {
        DanRankDialog danRankDialog = new DanRankDialog(activity, userId, 2);
        danRankDialog.show();

    }

    private void showExplainDialog() {
        ExplainDialog explainDialog = new ExplainDialog(activity,2);
        explainDialog.show();
    }

    public void setZaClicker() {
        setCountEnabled(false);
        getOpenCall();
    }

    MyBottomShow1Dialog myBottomShow1Dialog;

    private void showBottom(int i) {
        if (myBottomShow1Dialog != null && myBottomShow1Dialog.isShowing()) {
            myBottomShow1Dialog.dismiss();
        }
        myBottomShow1Dialog = new MyBottomShow1Dialog(activity, i, userId, 2);
        myBottomShow1Dialog.show();
    }


    public void loadResultSvga(String imgShow) {
    }


    private void getOpenCall() {
        int number = 1;
        if (chooseOne == 1) {
            number = 1;
        } else if (chooseOne == 2) {
            number = 10;
        } else if (chooseOne == 3) {
            number = 100;
        }
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("num", number);
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.GetLottery2, map, new MyObserver(activity) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                ChestOpenBean chestOpenBean = JSON.parseObject(responseString, ChestOpenBean.class);
                if (chestOpenBean.getCode() == Api.SUCCESS) {
                    data = chestOpenBean.getData();
                    //跳过动画
                    if (isSkipAnima){
                        showChestResult(chestOpenBean);
                    }else {
                        playAnim(chestOpenBean);
                    }

                } else {
                    setCountEnabled(true);
                    showToast(chestOpenBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                setCountEnabled(true);

            }
        });
    }

    /**
     * 播放动画
     * @param chestOpenBean
     */
    private void playAnim(ChestOpenBean chestOpenBean){
        //webp动图
        iv_treasure.setVisibility(View.GONE);
        iv_anim.setVisibility(View.VISIBLE);
        Transformation<Bitmap> transformation = new CenterInside();
        Glide.with(activity)
                .load(R.mipmap.donghaifishing)//不是本地资源就改为url即可
                .optionalTransform(transformation)
                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(transformation))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        WebpDrawable webpDrawable = (WebpDrawable) resource;
                        //需要设置为循环1次才会有onAnimationEnd回调
                        webpDrawable.setLoopCount(1);
                        webpDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationStart(Drawable drawable) {
                                super.onAnimationStart(drawable);
                            }

                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                webpDrawable.unregisterAnimationCallback(this);
                                iv_treasure.setVisibility(View.VISIBLE);
                                iv_anim.setVisibility(View.GONE);
                                showChestResult(chestOpenBean);
                            }
                        });

                        return false;
                    }
                })
                .into(iv_anim);
    }

    private void showChestResult(ChestOpenBean chestOpenBean) {
        setCountEnabled(true);
        int goldShow = chestOpenBean.getData().getUser().getGold();
        SharedPreferenceUtils.put(activity, Const.User.GOLD, goldShow);
        tvGold.setText(goldShow + "");
        //打开结果弹框
        if (sendChestsMsg != null) {
            showChestsShowDialog(data, chooseOne);
        }
    }

    private void setCountEnabled(boolean enabled) {
        ivCout1.setEnabled(enabled);
        ivCout10.setEnabled(enabled);
        ivCout100.setEnabled(enabled);
    }

    /**
     * 开箱探险结果页面
     *
     * @param data
     */
    private MyChestsShowDialog myChestsShowDialog;

    private void showChestsShowDialog(ChestOpenBean.DataBean data, int chooseOne) {
        if (myChestsShowDialog != null && myChestsShowDialog.isShowing()) {
            myChestsShowDialog.dismiss();
        }
        myChestsShowDialog = new MyChestsShowDialog(activity, data, chooseOne);
        myChestsShowDialog.show();
        myChestsShowDialog.setCancel(v -> {// 取消
            if (myChestsShowDialog != null && myChestsShowDialog.isShowing()) {
                myChestsShowDialog.dismiss();
            }
        });
        myChestsShowDialog.setButton(v -> {//再来一次
            if (myChestsShowDialog != null && myChestsShowDialog.isShowing()) {
                myChestsShowDialog.dismiss();
            }
            setZaClicker();
        });


        for (MicXgfjBean.DataBean.ListDataBean listDataBean : data.getMsgList()) {
            try {
//                WinPrizeGiftModel giftAllModel = new Gson().fromJson(listDataBean.getMessageChannelSend(), WinPrizeGiftModel.class);
                sendChestsMsg.onSendChestsMsg(listDataBean.getMessageChannelSend());
//                String txIMChannelMsg = listDataBean.getMessageChannelSend();
//                WinPrizeGiftModel giftAllModel = new Gson().fromJson(txIMChannelMsg, WinPrizeGiftModel.class);
//                List<String> msgRids = giftAllModel.getData().getRids();
//                if (msgRids!=null && msgRids.size()>0 && msgRids.contains(roomId)){
//                    sendChestsMsg.onSendChestsMsg(txIMChannelMsg);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onAnimationStart() {

    }

    @Override
    public void onAnimationEnd() {

    }

    @Override
    public void onAnimationRepeat() {

    }

    public interface SendChestsMsg {
        void onSendChestsMsg(String msg);
    }

    private SendChestsMsg sendChestsMsg;

    public void setSendChestsMsg(SendChestsMsg sendChestsMsg) {
        this.sendChestsMsg = sendChestsMsg;
    }

}