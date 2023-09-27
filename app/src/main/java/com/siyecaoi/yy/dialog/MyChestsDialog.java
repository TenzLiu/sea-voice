package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.ChestOpenBean;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.TopupActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.siyecaoi.yy.utils.SvgaUtils;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 开箱探险弹窗
 * Created by Administrator on 2018/3/9.
 */

@Deprecated
public class MyChestsDialog extends Dialog {


    int packetId;
    int userId;
    Context context;
    @BindView(R.id.iv_question_chests)
    ImageView ivQuestionChests;
    @BindView(R.id.iv_show_chests)
    SimpleDraweeView ivShowChests;
    @BindView(R.id.tv_show_chests)
    TextView tvShowChests;
    @BindView(R.id.btn_one_chests)
    Button btnOneChests;
    @BindView(R.id.btn_ten_chests)
    Button btnTenChests;
    @BindView(R.id.tv_question_chests)
    TextView tvQuestionChests;
    @BindView(R.id.btn_close_chests)
    Button btnCloseChests;
    @BindView(R.id.ll_question_chests)
    LinearLayout llQuestionChests;
    @BindView(R.id.tv_gold_chests)
    TextView tvGoldChests;
    @BindView(R.id.tv_topup_chests)
    TextView tvTopupChests;
    @BindView(R.id.mSVGAImageView_chests)
    SVGAImageView mSVGAImageViewChests;


    private boolean isOpenChest;//是否开启宝箱
    private int dataSize;
    String roomId;

    List<ChestOpenBean.DataBean.LotteryBean> data;
    int goldShow;
    String textShow;

    public MyChestsDialog(Context context, int userId, String roomId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.userId = userId;
        this.roomId = roomId;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_chests);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        goldShow = (int) SharedPreferenceUtils.get(context, Const.User.GOLD, 0);
        tvGoldChests.setText(goldShow + context.getString(R.string.tv_you));

        getCall();
        getRuleCall();
    }

    private void getRuleCall() {
        HttpManager.getInstance().post(Api.LotteryMark, new HashMap<>(), new MyObserver(context) {
            @Override
            public void success(String responseString) {
                                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                tvQuestionChests.setText("1." + getOneBean.getData().getM1() + "\n\n2." + getOneBean.getData().getM2() + context.getString(R.string.hint_show_chests));
            }
        });
//        HttpManager.getInstance().get(Api.LotteryMark, null, new MyObserver(context) {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void success(String responseString) {
//                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
//                tvQuestionChests.setText("1." + getOneBean.getData().getM1() + "\n\n2." + getOneBean.getData().getM2() + context.getString(R.string.hint_show_chests));
//            }
//        });
    }

    private void getCall() {
        HttpManager.getInstance().post(Api.getUserLottery, new HashMap<>(), new MyObserver(context) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    textShow = getOneBean.getData().getM();
                    tvShowChests.setText(textShow);
                } else {
                    showToast(getOneBean.getMsg());
                }
            }
        });
    }

    @OnClick({R.id.iv_question_chests, R.id.btn_one_chests, R.id.btn_ten_chests,
            R.id.btn_close_chests, R.id.ll_question_chests, R.id.tv_topup_chests})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_question_chests:
                llQuestionChests.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_one_chests:
                getOpenCall(Const.IntShow.ONE);
                break;
            case R.id.btn_ten_chests:
                if (isOpenChest) {
                    dataSize++;
//                    ImageUtils.loadUri(ivShowChests, this.data.get(dataSize).getImg());
                    String imgShow = this.data.get(dataSize).getImg();
                    if (imgShow.endsWith(".svga")) {
                        mSVGAImageViewChests.setVisibility(View.VISIBLE);
                        ivShowChests.setVisibility(View.INVISIBLE);
                        mSVGAImageViewChests.setCallback(new SVGACallback() {
                            @Override
                            public void onPause() {
                                LogUtils.e("svga", "onPause");
                            }

                            @Override
                            public void onFinished() {
                                LogUtils.e("msg", "svga完成");
//                                handler.sendEmptyMessage(101);
                            }

                            @Override
                            public void onRepeat() {

                            }

                            @Override
                            public void onStep(int i, double v) {

                            }
                        });
                        SvgaUtils.playSvgaFromUrl(imgShow, mSVGAImageViewChests, new SvgaUtils.SvgaDecodeListener() {
                            @Override
                            public void onError() {

                            }
                        });
                    } else {
                        mSVGAImageViewChests.setVisibility(View.INVISIBLE);
                        ivShowChests.setVisibility(View.VISIBLE);
                        ImageUtils.loadUri(ivShowChests, imgShow);
                    }
                    tvShowChests.setText(data.get(dataSize).getName());
                    if (dataSize == data.size() - 1) {
                        btnTenChests.setText("10次");
                        isOpenChest = false;
                        tvShowChests.setText(textShow);
                        dataSize = 0;
                    }
                } else {
                    getOpenCall(Const.IntShow.TEN);
                }
                break;
            case R.id.btn_close_chests:
                llQuestionChests.setVisibility(View.GONE);
                break;
            case R.id.ll_question_chests:

                break;
            case R.id.tv_topup_chests:
                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
                break;
        }
    }

    private MyDialog myDialog;

//    private void showMyDialog(int number) {
//        if (myDialog != null && myDialog.isShowing()) {
//            myDialog.dismiss();
//        }
//        myDialog = new MyDialog(context);
//        myDialog.show();
//        myDialog.setHintText(String.format(context.getString(R.string.tv_open_chests), number));
//        myDialog.setRightButton(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//                getOpenCall(number);
//            }
//        });
//    }

    private void getOpenCall(int number) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("num", number);
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.GetLottery, map, new MyObserver(context) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                ChestOpenBean chestOpenBean = JSON.parseObject(responseString, ChestOpenBean.class);
                if (chestOpenBean.getCode() == Api.SUCCESS) {
                    goldShow = chestOpenBean.getData().getUser().getGold();
                    SharedPreferenceUtils.put(context, Const.User.GOLD, goldShow);
                    tvGoldChests.setText(goldShow + context.getString(R.string.tv_you));
                    data = chestOpenBean.getData().getLottery();
                    setData(data);
                } else {
                    showToast(chestOpenBean.getMsg());
                }
            }
        });
    }

    private void setData(List<ChestOpenBean.DataBean.LotteryBean> dataBean) {
        int size = dataBean == null ? 0 : dataBean.size();
        if (size == 1) {
            String imgShow = dataBean.get(0).getImg();
            if (imgShow.endsWith(".svga")) {
                mSVGAImageViewChests.setVisibility(View.VISIBLE);
                ivShowChests.setVisibility(View.INVISIBLE);
                mSVGAImageViewChests.setCallback(new SVGACallback() {
                    @Override
                    public void onPause() {
                        LogUtils.e("svga", "onPause");
                    }

                    @Override
                    public void onFinished() {
                        LogUtils.e("msg", "svga完成");
//                        handler.sendEmptyMessage(101);
                    }

                    @Override
                    public void onRepeat() {

                    }

                    @Override
                    public void onStep(int i, double v) {

                    }
                });

                SvgaUtils.playSvgaFromUrl(imgShow, mSVGAImageViewChests, new SvgaUtils.SvgaDecodeListener() {
                    @Override
                    public void onError() {

                    }
                });
            } else {
                mSVGAImageViewChests.setVisibility(View.INVISIBLE);
                ivShowChests.setVisibility(View.VISIBLE);
                ImageUtils.loadUri(ivShowChests, dataBean.get(0).getImg());
            }

            tvShowChests.setText(dataBean.get(0).getName());
        } else if (size > 1) {
            isOpenChest = true;
            ImageUtils.loadUri(ivShowChests, dataBean.get(0).getImg());
            btnTenChests.setText("打开宝箱");
        }
    }
//
//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 101:
//
//                    break;
//            }
//        }
//    };
}