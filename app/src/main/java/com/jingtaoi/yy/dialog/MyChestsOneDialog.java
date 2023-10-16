package com.jingtaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.ChestOpenBean;
import com.jingtaoi.yy.bean.DoubleBean;
import com.jingtaoi.yy.bean.MicXgfjBean;
import com.jingtaoi.yy.bean.UserBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.dialog.DanJiangDialog;
import com.jingtaoi.yy.ui.room.dialog.DanJiangRealtimeDialog;
import com.jingtaoi.yy.ui.room.dialog.DanRankDialog;
import com.jingtaoi.yy.ui.room.dialog.ExplainDialog;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.jingtaoi.yy.utils.SvgaUtils;
import com.jingtaoi.yy.view.FrameAnimation;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 探险弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyChestsOneDialog extends Dialog implements FrameAnimation.AnimationListener {


    int packetId;
    int userId;
    Activity activity;
    @BindView(R.id.tv_choose1_ch)
    TextView tvChoose1Ch;
    @BindView(R.id.rl_choose1_ch)
    RelativeLayout rlChoose1Ch;
    @BindView(R.id.tv_choose2_ch)
    TextView tvChoose2Ch;
    @BindView(R.id.rl_choose2_ch)
    RelativeLayout rlChoose2Ch;
    @BindView(R.id.tv_choose3_ch)
    TextView tvChoose3Ch;
    @BindView(R.id.rl_choose3_ch)
    RelativeLayout rlChoose3Ch;
    @BindView(R.id.tv_show_ch)
    TextView tvShowCh;
    @BindView(R.id.bg_type)
    SimpleDraweeView bg_type;
    @BindView(R.id.tv_now_ch)
    TextView tvNowCh;
    @BindView(R.id.iv_anim)
    ImageView iv_anim;
    @BindView(R.id.iv_anim_diamond)
    ImageView iv_anim_diamond;
    @BindView(R.id.tv_gold_ch)
    TextView tvGoldCh;
    @BindView(R.id.tv_method_ch)
    TextView tv_method_ch;
    @BindView(R.id.tv_list_ch)
    TextView tv_list_ch;
    @BindView(R.id.tv_jump_ch)
    TextView tv_jump_ch;
    @BindView(R.id.tv_money_1)
    TextView tv_money_1;
    @BindView(R.id.tv_money_2)
    TextView tv_money_2;
    @BindView(R.id.tv_money_3)
    TextView tv_money_3;
    @BindView(R.id.btn_topup_ch)
    Button btnTopupCh;
    @BindView(R.id.iv_back_ch)
    ImageView ivBackCh;
    @BindView(R.id.iv_show_ch)
    ImageView iv_show_ch;
    @BindView(R.id.iv_show_ball)
    ImageView iv_show_ball;
    @BindView(R.id.iv_normal)
    ImageView iv_normal;
    @BindView(R.id.iv_diamond)
    ImageView iv_diamond;
    @BindView(R.id.rl_pro)
    RelativeLayout rlPro;
    @BindView(R.id.cl_realtime)
    ConstraintLayout cl_realtime;
    @BindView(R.id.fl_real)
    FrameLayout fl_real;
    @BindView(R.id.ll_number)
    TextView ll_number;
    @BindView(R.id.fl_normal)
    FrameLayout fl_normal;
    @BindView(R.id.fl_diamond)
    FrameLayout fl_diamond;
    @BindView(R.id.mSVGAImageView_layout)
    LinearLayout mSVGAImageView_layout;

    SVGAImageView mSVGAImageView_dan;

    private int chooseOne;//
    String roomId;
    ChestOpenBean.DataBean data;
    private boolean isJumpAnimate = false;//是否跳过动画
    private SVGADrawable drawable;
    private int eggType = 1; //1普通 2钻石
    private int normalReal = 0;//普通奖池实时数据
    private int deepReal = 0;//深海奖池实时数据
    Disposable subscribe;
    public void setChooseOne(int chooseOne) {
        this.chooseOne = chooseOne;
    }

    public MyChestsOneDialog(Activity activity, int userId, String roomId) {
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
        setContentView(R.layout.dialog_chestsone);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mSVGAImageView_dan != null) {
                    mSVGAImageView_dan.stopAnimation();
                    mSVGAImageView_dan.clearAnimation();
                }
                if (mSVGAImageView_layout != null){
                    mSVGAImageView_layout.removeAllViews();
                }
                drawable = null;
                mSVGAImageView_dan = null;
                if (subscribe!=null&&!subscribe.isDisposed())
                    subscribe.dispose();
            }
        });

        chooseOne = 1;
        tvChoose1Ch.setSelected(true);
//        isJumpAnimate = true;
//        tv_jump_ch.setSelected(isJumpAnimate);
//        tv_jump_ch.setEnabled(false);
//        tv_jump_ch.setClickable(false);
        getCall();
        getRealtimeCall(eggType);
        if (subscribe == null)
            subscribe = Observable.interval(0, 10, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(aLong -> {
                getRealtimeCall(1);
                if (fl_diamond.getVisibility() == View.VISIBLE){
                    getRealtimeCall(2);
                }
            });
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
                        if (userBean.getData().isOpenDm == 0){
                            fl_normal.setVisibility(View.INVISIBLE);
                            fl_diamond.setVisibility(View.INVISIBLE);
                        }else {
                            fl_normal.setVisibility(View.VISIBLE);
                            fl_diamond.setVisibility(View.VISIBLE);
                            getRealtimeCall(2);
                        }
                        SharedPreferenceUtils.put(activity, Const.User.GOLD, userBean.getData().getGold());
                        tvGoldCh.setText(userBean.getData().getGold() + "");

                    }
                }
            }
        });

    }

    /**
     * 获取实时奖池
     */
    private void getRealtimeCall(int type){
        HashMap<String, Object> params = new HashMap<>();
        params.put("type",type);
        HttpManager.getInstance().post(Api.jackpotSize, params, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                DoubleBean getOneBean = JSON.parseObject(responseString, DoubleBean.class);
                if (getOneBean.code == Api.SUCCESS) {
                    int data = (int) getOneBean.data;
                    if (type == 1)
                        normalReal = data;
                    else
                        deepReal = data;
                    String s = (eggType == 1?normalReal:deepReal) + "";
                    ll_number.setText(s);
                } else {
                    showToast(getOneBean.msg);
                }
            }
        });
    }

    @OnClick({R.id.iv_back_ch,R.id.fl_normal,R.id.fl_diamond,R.id.cl_realtime,
            R.id.rl_choose1_ch, R.id.rl_choose2_ch, R.id.rl_choose3_ch,
            R.id.tv_his_ch, R.id.tv_now_ch, R.id.btn_topup_ch,
            R.id.tv_list_ch, R.id.tv_method_ch, R.id.tv_jump_ch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_ch:
                dismiss();
                break;
            case R.id.cl_realtime://钻石蛋实时奖池
                showDanJiangRealtimeDialog();
                break;
            case R.id.fl_normal:
                eggType = 1;
                switchEggType();
                break;
            case R.id.fl_diamond:
                eggType = 2;
                switchEggType();
                break;
            case R.id.rl_choose1_ch:
                chooseOne = 1;
                setZaClicker();
                break;
            case R.id.rl_choose2_ch:
                chooseOne = 2;
                setZaClicker();
                break;
            case R.id.rl_choose3_ch:
                chooseOne = 3;
                setZaClicker();
                break;
            case R.id.tv_his_ch:
                showBottom(2);
                break;
            case R.id.tv_now_ch:
                showDanJiangDialog();
                break;
            case R.id.btn_topup_ch:
                break;
            case R.id.tv_list_ch:
                showRankDialog();
                break;
            case R.id.tv_method_ch:
                showExplainDialog();
                break;
            case R.id.tv_jump_ch:
                if (isJumpAnimate) {
                    isJumpAnimate = false;
                    tv_jump_ch.setSelected(false);
                } else {
                    isJumpAnimate = true;
                    tv_jump_ch.setSelected(true);
                }
                break;
        }
    }

    private void switchEggType(){
        if (eggType == 1){
            bg_type.setActualImageResource(R.drawable.bg_ch);
            iv_show_ch.setVisibility(View.VISIBLE);
            iv_show_ball.setVisibility(View.INVISIBLE);
            tv_money_1.setText("20");
            tv_money_2.setText("200");
            tv_money_3.setText("2000");
            iv_normal.setImageResource(R.drawable.normal_check);
            iv_diamond.setImageResource(R.drawable.deep_normal);
//            cl_realtime.setVisibility(View.VISIBLE);
            cl_realtime.setBackgroundResource(R.drawable.bg_normal_dan);
            fl_real.setPadding(0, ScreenUtil.getPxByDp(8),0,0);
            ll_number.setText(normalReal+ "");
        }else {
            bg_type.setActualImageResource(R.drawable.bg_diamond_home);
            iv_show_ch.setVisibility(View.INVISIBLE);
            iv_show_ball.setVisibility(View.VISIBLE);
            tv_money_1.setText("50");
            tv_money_2.setText("500");
            tv_money_3.setText("5000");
            iv_normal.setImageResource(R.drawable.normal_normal);
            iv_diamond.setImageResource(R.drawable.deep_check);
//            cl_realtime.setVisibility(View.VISIBLE);
            cl_realtime.setBackgroundResource(R.drawable.bg_deep_dan);
            fl_real.setPadding(0,0,0,0);
            ll_number.setText(deepReal+ "");
        }
    }

    private void showDanJiangDialog() {
        DanJiangDialog danJiangDialog = new DanJiangDialog(activity,eggType, null);
        danJiangDialog.show();
    }

    private void showDanJiangRealtimeDialog() {
        DanJiangRealtimeDialog danJiangDialog = new DanJiangRealtimeDialog(activity,eggType);
        danJiangDialog.show();
    }

    private void showRankDialog() {
        DanRankDialog danRankDialog = new DanRankDialog(activity, userId,eggType);
        danRankDialog.show();

    }

    private void showExplainDialog() {
        ExplainDialog explainDialog = new ExplainDialog(activity,1);
        explainDialog.show();
    }

    public void setZaClicker() {
        setShow();
    }

    MyBottomShow1Dialog myBottomShow1Dialog;

    private void showBottom(int i) {
        if (myBottomShow1Dialog != null && myBottomShow1Dialog.isShowing()) {
            myBottomShow1Dialog.dismiss();
        }
        myBottomShow1Dialog = new MyBottomShow1Dialog(activity, i, userId,eggType);
        myBottomShow1Dialog.show();
    }

    public void setShow() {
        rlChoose1Ch.setEnabled(false);
        rlChoose2Ch.setEnabled(false);
        rlChoose3Ch.setEnabled(false);

        if (chooseOne == 1) {
            getOpenCall(1);
        } else if (chooseOne == 2) {
            getOpenCall(10);
        } else if (chooseOne == 3) {
            getOpenCall(100);
        }

    }

    public void loadResultSvga(String imgShow) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        mSVGAImageView_dan = new SVGAImageView(activity);
        mSVGAImageView_dan.setLoops(1);
        mSVGAImageView_dan.setClearsAfterStop(true);
        mSVGAImageView_dan.setLayoutParams(params);
        mSVGAImageView_layout.addView(mSVGAImageView_dan);
        mSVGAImageView_dan.setCallback(svgaCallback);
        SvgaUtils.playSvgaFromAssets(imgShow, mSVGAImageView_dan, new SvgaUtils.SvgaDecodeListener() {
            @Override
            public void onError() {

            }
        });
    }

    private SVGACallback svgaCallback = new SVGACallback() {
        @Override
        public void onPause() {}

        @Override
        public void onFinished() {
            if (mSVGAImageView_dan != null) {
                mSVGAImageView_dan.stopAnimation();
                mSVGAImageView_dan.clearAnimation();
            }
            if (mSVGAImageView_layout != null){
                mSVGAImageView_layout.removeAllViews();
            }
            drawable = null;
            mSVGAImageView_dan = null;

            showChestsShowDialog(data, chooseOne);

            LogUtils.e("msg", "svga完成");
        }

        @Override
        public void onRepeat() {}

        @Override
        public void onStep(int i, double v) {}
    };

    private void getOpenCall(int number) {
        String api = eggType==1?Api.GetLottery:Api.GetLottery2;
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("num", number);
        map.put("rid", roomId);
        HttpManager.getInstance().post(api, map, new MyObserver(activity) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {

                ChestOpenBean chestOpenBean = JSON.parseObject(responseString, ChestOpenBean.class);
                if (chestOpenBean.getCode() == Api.SUCCESS) {
                    int goldShow = chestOpenBean.getData().getUser().getGold();
                    SharedPreferenceUtils.put(activity, Const.User.GOLD, goldShow);
                    tvGoldCh.setText(goldShow + "");

                    data = chestOpenBean.getData();
                    if (sendChestsMsg != null) {
                        if (!isJumpAnimate) {
                            List<Long> giftLottery = new ArrayList<>();
                            for (ChestOpenBean.DataBean.LotteryBean bean : data.getLottery()) {
                                giftLottery.add(bean.getGold());
                            }
//                            loadResultSvga(Collections.max(giftLottery) + ".svga");
                            Long max = Collections.max(giftLottery);
                            if (max>=5000)
//                            if (max>=0)
                                ImageUtils.loadFrameAnimation(eggType == 1?iv_anim:iv_anim_diamond,eggType == 1?R.array.bg_egg:R.array.bg_egg_diamond,false,
                                       eggType == 1? Const.giftDanFrame: Const.giftDanDiamondFrame,MyChestsOneDialog.this);
                            else
                                showChestsShowDialog(data, chooseOne);
                        } else {
                            showChestsShowDialog(data, chooseOne);
                        }
                    } else {
                        rlChoose1Ch.setEnabled(true);
                        rlChoose2Ch.setEnabled(true);
                        rlChoose3Ch.setEnabled(true);
                    }

                } else {
                    showToast(chestOpenBean.getMsg());

                    rlChoose1Ch.setEnabled(true);
                    rlChoose2Ch.setEnabled(true);
                    rlChoose3Ch.setEnabled(true);
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);

                rlChoose1Ch.setEnabled(true);
                rlChoose2Ch.setEnabled(true);
                rlChoose3Ch.setEnabled(true);
            }
        });
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

        rlChoose1Ch.setEnabled(true);
        rlChoose2Ch.setEnabled(true);
        rlChoose3Ch.setEnabled(true);

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

    public Button getTopup() {
        return btnTopupCh;
    }

    @Override
    public void onAnimationStart() {
        if (eggType == 1)
            iv_anim.setVisibility(View.VISIBLE);
        else
            iv_anim_diamond.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd() {
        iv_anim.setVisibility(View.GONE);
        iv_anim_diamond.setVisibility(View.GONE);
        showChestsShowDialog(data, chooseOne);
    }

    @Override
    public void onAnimationRepeat() {

    }

    public interface SendChestsMsg {
        void onSendChestsMsg(String msg);
    }

    private SendChestsMsg sendChestsMsg;

    public void setSendChestsMsg(SendChestsMsg sendChestsMsg){
        this.sendChestsMsg = sendChestsMsg;
    }

}