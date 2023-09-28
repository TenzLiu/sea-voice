package com.siyecaoi.yy.dialog;

import static com.siyecaoi.yy.utils.Const.Lottery.WHEEL_IMG_LIST;

import android.animation.ValueAnimator;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.ChestOpenBean;
import com.siyecaoi.yy.bean.DoubleBean;
import com.siyecaoi.yy.bean.FishBean;
import com.siyecaoi.yy.bean.GiftShowBean;
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
import com.siyecaoi.yy.view.wheelsruflibrary.listener.RotateListener;
import com.siyecaoi.yy.view.wheelsruflibrary.view.WheelSurfView;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 深海捕鱼弹窗（转盘）
 */

public class DeepSeaFishingDialog extends Dialog implements FrameAnimation.AnimationListener {
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
    @BindView(R.id.wheelSurfView)
    WheelSurfView mWheelSurfView;


    int userId;
    Activity activity;

    private int chooseOne;//
    String roomId;
    ChestOpenBean.DataBean data;
    private int normalReal = 0;//普通奖池实时数据

    private List<FishBean> fishBeans = new ArrayList<>();
    private Map<Integer,Integer> fishBeanViewMap = new HashMap<>();

    boolean isSkipAnima = false;
    private GiftShowBean mAllGiftShowBean;
    private boolean mIsStat = false;


    public DeepSeaFishingDialog(Activity activity, int userId, String roomId) {
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
        setContentView(R.layout.dialog_deep_sea_fishing);
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
        fishBeanViewMap.put(88888,R.mipmap.ic_deep_sea_fishing_01);
        fishBeanViewMap.put(52000,R.mipmap.ic_deep_sea_fishing_02);
        fishBeanViewMap.put(5200,R.mipmap.ic_deep_sea_fishing_05);
        fishBeanViewMap.put(13140,R.mipmap.ic_deep_sea_fishing_04);
        fishBeanViewMap.put(33440,R.mipmap.ic_deep_sea_fishing_03);
        fishBeanViewMap.put(8888,R.mipmap.ic_deep_sea_fishing_07);
        fishBeanViewMap.put(520,R.mipmap.ic_deep_sea_fishing_06);
        fishBeanViewMap.put(888,R.mipmap.ic_deep_sea_fishing_08);
        fishBeanViewMap.put(3344,R.mipmap.ic_deep_sea_fishing_14);
        fishBeanViewMap.put(188,R.mipmap.ic_deep_sea_fishing_13);
        fishBeanViewMap.put(10,R.mipmap.ic_deep_sea_fishing_10);
        fishBeanViewMap.put(30,R.mipmap.ic_deep_sea_fishing_11);
        fishBeanViewMap.put(1,R.mipmap.ic_deep_sea_fishing_09);
        fishBeanViewMap.put(66,R.mipmap.ic_deep_sea_fishing_15);
        fishBeanViewMap.put(1314,R.mipmap.ic_deep_sea_fishing_12);
        isSkipAnima = (boolean) SharedPreferenceUtils.get(getContext(),Const.User.IS_DEEP_SEA_SKIP,false);
        if (isSkipAnima) {
            iv_skip.setImageResource(R.mipmap.icon_skip_off);
        } else{
            iv_skip.setImageResource(R.mipmap.icon_skip_on);
        }
        mWheelSurfView.setRotateListener(new RotateListener() {
            @Override
            public void rotateEnd(int position, String des) {
                showChestResultTwo(data);
                mIsStat = false;
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {
                mIsStat = true;
            }

            @Override
            public void rotateBefore(View goImg) {

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
                        SharedPreferenceUtils.put(activity, Const.User.GOLD, userBean.getData().getGold());
                        tvGold.setText(userBean.getData().getGold() + "");
                    }
                }
            }
        });

        HashMap<String, Object> map1 = HttpManager.getInstance().getMap();
        String reqString = Api.JackpotInfoMark;
        map1.put("type", 22);
        HttpManager.getInstance().post(reqString, map1, new MyObserver(getContext()) {
            @Override
            public void success(String responseString) {
                GiftShowBean giftShowBean = JSON.parseObject(responseString, GiftShowBean.class);
                mAllGiftShowBean = giftShowBean;
                setWheelData();
            }
        });

    }

    public void setWheelData() {
        if(mAllGiftShowBean.getData().size() > 12){
            mAllGiftShowBean.setData(mAllGiftShowBean.getData().subList(0, 12));
        }
        WheelSurfView.Builder builder = new WheelSurfView.Builder();
        String[] names = new String[mAllGiftShowBean.getData().size()];
        for (int i = 0; i < mAllGiftShowBean.getData().size(); i++) {
            names[i] = String.valueOf(mAllGiftShowBean.getData().get(i).getGold());
        }
        builder.setmDeses(names);

        if (WHEEL_IMG_LIST.isEmpty()) {
            for (int i = 0; i < mAllGiftShowBean.getData().size(); i++) {
                int finalI = i;
                Glide.with(getContext()).asBitmap().load(mAllGiftShowBean.getData().get(i).getImg())
                        .into(new CustomTarget<Bitmap>() {
                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if (WHEEL_IMG_LIST.size() -1 < finalI){
                                    WHEEL_IMG_LIST.add(resource);
                                }else {
                                    WHEEL_IMG_LIST.add(finalI, resource);
                                }
                                if (WHEEL_IMG_LIST.size() == 12) {
                                    List<Bitmap> bitmaps = WheelSurfView.rotateBitmaps(WHEEL_IMG_LIST);
                                    builder.setmIcons(bitmaps);
                                    builder.build();
                                    mWheelSurfView.setConfig(builder);
                                }
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        } else {
            List<Bitmap> bitmaps = WheelSurfView.rotateBitmaps(WHEEL_IMG_LIST);
            builder.setmIcons(bitmaps);
            builder.build();
            mWheelSurfView.setConfig(builder);
        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Bitmap> list = new ArrayList<>();
//
//                for (int i =0 ; i < giftShowBean.getData().size();i++) {
//                    Bitmap bm = null;
//                    try {
//                        URL iconUrl = new URL(giftShowBean.getData().get(i).getImg());
//                        URLConnection conn = iconUrl.openConnection();
//                        HttpURLConnection http = (HttpURLConnection) conn;
//                        int length = http.getContentLength();
//                        conn.connect();
//                        // 获得图像的字符流
//                        InputStream is = conn.getInputStream();
//                        BufferedInputStream bis = new BufferedInputStream(is, length);
//                        bm = BitmapFactory.decodeStream(bis);
//                        list.add(bm);
//                        bis.close();
//                        is.close();// 关闭流
//                        if (i == giftShowBean.getData().size() - 1) {
//                            Handler handler = new Handler(Looper.getMainLooper());
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    builder.setmIcons(list);
//                                    builder.build();
//                                    mWheelSurfView.setConfig(builder);
//                                }
//                            });
//                        }
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();


    }

    /**
     * 获取实时奖池
     */
    private void getRealtimeCall() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", 3);
        HttpManager.getInstance().post(Api.jackpotSize, params, new MyObserver(activity) {
            @Override
            public void success(String responseString) {
                DoubleBean getOneBean = JSON.parseObject(responseString, DoubleBean.class);
                if (getOneBean.code == Api.SUCCESS) {
                    int data = (int) getOneBean.data;
                    normalReal = data;
                    String s = normalReal + "";
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
                } else{
                    iv_skip.setImageResource(R.mipmap.icon_skip_off);
                }
                isSkipAnima = !isSkipAnima;
                SharedPreferenceUtils.put(getContext(),Const.User.IS_DEEP_SEA_SKIP,isSkipAnima);
                break;
        }
    }


    private void showDanJiangDialog() {
        DanJiangDialog danJiangDialog = new DanJiangDialog(activity, 22,fishBeanViewMap);
        danJiangDialog.show();
    }

    private void showDanJiangRealtimeDialog() {
        DanJiangRealtimeDialog danJiangDialog = new DanJiangRealtimeDialog(activity, 22);
        danJiangDialog.show();
    }

    private void showRankDialog() {
        DanRankDialog danRankDialog = new DanRankDialog(activity, userId, 22);
        danRankDialog.show();

    }

    private void showExplainDialog() {
        ExplainDialog explainDialog = new ExplainDialog(activity,22);
        explainDialog.show();
    }

    public void setZaClicker() {
        if (!mIsStat) {
            setCountEnabled(false);
            getOpenCall();
        }

    }

    MyBottomShow1Dialog myBottomShow1Dialog;

    private void showBottom(int i) {
        if (myBottomShow1Dialog != null && myBottomShow1Dialog.isShowing()) {
            myBottomShow1Dialog.dismiss();
        }
        myBottomShow1Dialog = new MyBottomShow1Dialog(activity, i, userId, 23);
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
        HttpManager.getInstance().post(Api.GetLottery3, map, new MyObserver(activity) {
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
                        if (data.getLottery().size() == 1) {
                            long id = data.getLottery().get(0).getGold();
                            mWheelSurfView.startRotate(getGiftPosition(id));
                        } else if (data.getLottery().size() > 1) {
                            ArrayList<Long> bigList = new ArrayList<>();
                            for (ChestOpenBean.DataBean.LotteryBean bean : data.getLottery()) {
                                bigList.add(bean.getGold());
                            }
                            Long biggest = Collections.max(bigList);
                            mWheelSurfView.startRotate(getGiftPosition(biggest));
                        }
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

    public int getGiftPosition(long gold) {
        if (mAllGiftShowBean != null) {
            for (int i = 0; i < mAllGiftShowBean.getData().size(); i++) {
                if (mAllGiftShowBean.getData().get(i).getGold() == gold) {
                    return getReverse(i + 1);
                }
            }
        }
        return 0;
    }

    public int getReverse(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 12;
            case 3:
                return 11;
            case 4:
                return 10;
            case 5:
                return 9;
            case 6:
                return 8;
            case 7:
                return 7;
            case 8:
                return 6;
            case 9:
                return 5;
            case 10:
                return 4;
            case 11:
                return 3;
            case 12:
                return 2;
            default:
                return 0;
        }
    }

    private void showChestResult(ChestOpenBean chestOpenBean) {
        showChestResultTwo(chestOpenBean.getData());
    }

    private void showChestResultTwo(ChestOpenBean.DataBean dataBean) {
        setCountEnabled(true);
        int goldShow = dataBean.getUser().getGold();
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