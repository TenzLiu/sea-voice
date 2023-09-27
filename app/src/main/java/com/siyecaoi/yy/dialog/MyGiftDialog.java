package com.siyecaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyApplication;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.bean.GiftBean;
import com.siyecaoi.yy.bean.VoiceUserBean;
import com.siyecaoi.yy.model.GiftNumber;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.room.TopupActivity;
import com.siyecaoi.yy.ui.room.adapter.GiftGroudAdapter;
import com.siyecaoi.yy.ui.room.adapter.GiftNumberListAdapter;
import com.siyecaoi.yy.ui.room.adapter.GiftUserListAdapter;
import com.siyecaoi.yy.ui.room.adapter.ViewPagerAdapter;
import com.siyecaoi.yy.ui.room.dialog.GiveGiftDialog;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.BroadcastManager;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Toast;


/**
 * 礼物弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyGiftDialog extends Dialog {


    Context context;
    @BindView(R.id.tv_gold_gift)
    TextView tvGoldGift;
    @BindView(R.id.tv_topup_gift)
    TextView tvTopupGift;
    @BindView(R.id.tv_data_gift)
    TextView tvDataGift;
    @BindView(R.id.tv_bao_gift)
    TextView tvBaoGift;
    @BindView(R.id.tv_nomal_gift)
    TextView tvNomalGift;
    @BindView(R.id.tv_miracle_gift)
    TextView tvMiracleGift;
    @BindView(R.id.tv_exc_gift)
    TextView tvExcGift;
    @BindView(R.id.ll_indicator_gift)
    LinearLayout llIndicatorGift;
    //    @BindView(R.id.tv_send_gift)
//    TextView tvSendGift;
    @BindView(R.id.ll_send_gift)
    LinearLayout llSendGift;
    @BindView(R.id.tv_number_gift)
    TextView tvNumberGift;
    @BindView(R.id.img_number_gift)
    ImageView imgNumberGift;
    @BindView(R.id.tv_sendto_gift)
    TextView tvSendtoGift;
    @BindView(R.id.tv_allmic_gift)
    TextView tvAllmicGift;
    @BindView(R.id.mRecyclerView_gift)
    RecyclerView mRecyclerViewGift;
    @BindView(R.id.ll_allmic_gift)
    LinearLayout llAllmicGift;
    @BindView(R.id.mRecyclerView_number_gift)
    RecyclerView mRecyclerViewNumberGift;
    @BindView(R.id.tv_nodata_gift)
    TextView tvNodataGift;
    @BindView(R.id.mViewPager_gift)
    ViewPager mViewPagerGift;
    //    @BindView(R.id.rl_allmic_gift)
//    RelativeLayout rlAllmicGift;
    @BindView(R.id.iv_packet_gift)
    ImageView ivPacketGift;
    @BindView(R.id.tv_party_gift)
    TextView tvPartyGift;
    @BindView(R.id.tv_packet_gift)
    TextView tvPacketGift;
    @BindView(R.id.ll_sendto_gift)
    LinearLayout llSendtoGift;
    @BindView(R.id.tv_sendpacket_gift)
    TextView tvSendpacketGift;
    @BindView(R.id.tv_othernum_gift)
    TextView tvOthernumGift;

    @BindView(R.id.total_gifts_tv)
    TextView total_gifts_tv;
    @BindView(R.id.tv_sendall_gift)
    TextView tv_sendall_gift;
    @BindView(R.id.tv_give_gift)
    TextView tv_give_gift;

    Handler handler=new Handler();
    private int chooseOne;//被选中的title(1包裹，2普通礼物，3神奇礼物，4 专属礼物)
    //    FragPagerAdapter fragPagerAdapter;
    private ArrayList<String> titles;
    private ArrayList<Fragment> fragments;
    @BindView(R.id.ll_number_gift)
    LinearLayout llNumberGift;

    private List<View> mPagerList;//页面集合

    GiftNumberListAdapter giftNumberListAdapter;
    private List<GiftNumber> giftNumbers;

    private int chooseNumber;//被选中的人数
    private List<VoiceUserBean.DataBean> userList;//麦上用户信息
    GiftUserListAdapter giftUserListAdapter;
    private int gid;
    private String img;
    private String showImg;
    private int goodGold;
    private int sendGiftNumber;//赠送数量
    private int type;//1送礼物  2pk送礼物 3,聊天送礼物,4在线用户列表送礼物
    private String userName;//被送礼物的用户名称
    private int otherId;//被送礼物的用户id
    private String roomId;//房间id
    private int userId;//送礼物的用户id
    private int roomState;//房间类型  是否牌子房间 1否，2是
    private boolean isBootomClick;//判断是否是点击底部弹出弹窗
    private int sendType = 1;//1 是普通， 2是背包
    private int packetNumber;//背包数量

    private ViewPagerAdapter viewPagerAdapter;

    public MyGiftDialog(Context context, int userId, int otherId,
                        int type, String userName, String roomId, int roomState, boolean isBootomClick) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.userId = userId;
        this.otherId = otherId;
        this.type = type;
        this.userName = userName;
        this.roomId = roomId;
        this.roomState = roomState;
        this.isBootomClick = isBootomClick;
        if (userId != otherId) {
            chooseNumber = 1;
        }
    }

//    public MyGiftDialog(Context context, int userId, int otherId,
//                        int type, String userName, int roomState) {
//        super(context, R.style.CustomDialogStyle);
//        this.context = context;
//        this.userId = userId;
//        this.otherId = otherId;
//        this.type = type;
//        this.userName = userName;
//        this.roomState = roomState;
//        chooseNumber = 1;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_gift);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);

        iniData();
        setData();
        getGoldCall();
    }

    private void getGoldCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        HttpManager.getInstance().post(Api.getUserMoney, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    if (getOneBean.getData().getState() == 2) {
                        Toast.create(MyApplication.getInstance()).show("您的账号已锁定，请联系客服");
                        BroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(Const.BroadCast.EXIT);
                        return;
                    }

                    try {
                        int goldNum=0;
                        String gold= getOneBean.getData().getGold();
                        goldNum= (int) Double.parseDouble(gold);
                        SharedPreferenceUtils.put(getContext(), Const.User.GOLD,goldNum);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    tvGoldGift.setText(getOneBean.getData().getGold() + " >");
                } else {
                    showToast(getOneBean.getMsg());
                }
            }
        });
    }

    private void setData() {
        setRecycler();
        getCall();
        if (type == 1) {
            getPersonCall(true);
        } else {
            setNumberShow();
        }
    }

    private void getPersonCall(boolean isFrist) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.getChatroomsUser, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                VoiceUserBean voiceUserBean = JSON.parseObject(responseString, VoiceUserBean.class);
                if (voiceUserBean.getCode() == Api.SUCCESS) {
                    setPersonData(voiceUserBean.getData(), isFrist);
                } else {
                    showToast(voiceUserBean.getMsg());
                }
            }
        });
    }

    private void setPersonData(List<VoiceUserBean.DataBean> data, boolean isFrist) {
        chooseNumber = 0;
        userList.clear();
        for (VoiceUserBean.DataBean userBean : data) {
            if (userBean.getId() == userId) {
                userBean.setChoose(false);
            } else if (userBean.getId() == otherId) {
                userBean.setChoose(true);
                chooseNumber++;
            }

            userList.add(userBean);
        }

        if (!isBootomClick){
            for (int i = 0; i < userList.size(); i++) {
                  if (!TextUtils.isEmpty(userName)&&userName.equals(userList.get(i).getName())){
                      userList.get(i).setChoose(true);
                      chooseNumber++;
                  }
            }
        }

        giftUserListAdapter.replaceData(userList);
        setNumberShow();
//        setShowList();

//        if (isFrist) {
//            if (chooseNumber == 0) { //如果没有赠送对象，默认全选
//                setShowList();
//            }
//        } else {
//            if (rlAllmicGift.getVisibility() == View.GONE) {
//                rlAllmicGift.setVisibility(View.VISIBLE);
//            }
//        }
    }

    @SuppressLint("SetTextI18n")
    private void iniData() {
        chooseOne = 2;
        sendGiftNumber = 1;

        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        mPagerList = new ArrayList<>();
        groudAdapterList = new ArrayList<>();
        giftNumbers = new ArrayList<>();
        userList = new ArrayList<>();

        giftNumbers.add(new GiftNumber(1, context.getString(R.string.tv_number1_gift)));
        giftNumbers.add(new GiftNumber(10, context.getString(R.string.tv_number10_gift)));
        giftNumbers.add(new GiftNumber(38, context.getString(R.string.tv_number38_gift)));
        giftNumbers.add(new GiftNumber(66, context.getString(R.string.tv_number66_gift)));
        giftNumbers.add(new GiftNumber(188, context.getString(R.string.tv_number188_gift)));
        giftNumbers.add(new GiftNumber(520, context.getString(R.string.tv_number520_gift)));
        giftNumbers.add(new GiftNumber(1314, context.getString(R.string.tv_number1314_gift)));

        int goldYou = (int) SharedPreferenceUtils.get(context, Const.User.GOLD, 0);
        tvGoldGift.setText(goldYou + " >");

        if (roomState == 1) {
//            ivPacketGift.setVisibility(View.GONE);
        } else if (roomState == 2) {
//            ivPacketGift.setVisibility(View.VISIBLE);
        }
        if (isBootomClick) {
//            ivPacketGift.setVisibility(View.GONE);
        }
    }

    private void setRecycler() {
        giftUserListAdapter = new GiftUserListAdapter(R.layout.item_giftuser);
        mRecyclerViewGift.setAdapter(giftUserListAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mRecyclerViewGift.setLayoutManager(layout);



        giftUserListAdapter.setNewData(userList);

        giftUserListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VoiceUserBean.DataBean dataBean = (VoiceUserBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                if (dataBean.isChoose()) {
                    dataBean.setChoose(false);
                    chooseNumber--;
                } else {
                    dataBean.setChoose(true);
                    chooseNumber++;
                }
                adapter.setData(position, dataBean);
                if (chooseNumber == 1) {
                    userList = adapter.getData();
                    for (VoiceUserBean.DataBean data : userList) {
                        if (data.isChoose()) {
                            userName = data.getName();
                            otherId = data.getId();
                            break;
                        }
                    }
                }
                setNumberShow();
            }
        });


        GiftNumberListAdapter giftNumberListAdapter = new GiftNumberListAdapter(R.layout.item_giftnumber);
        mRecyclerViewNumberGift.setAdapter(giftNumberListAdapter);
        mRecyclerViewNumberGift.setLayoutManager(new LinearLayoutManager(context));
        giftNumberListAdapter.setNewData(giftNumbers);

        giftNumberListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GiftNumber giftNumber = (GiftNumber) adapter.getItem(position);
                sendGiftNumber = giftNumber.getNumberShow();
                if (llNumberGift.getVisibility() == View.VISIBLE) {
                    llNumberGift.setVisibility(View.GONE);
                }
                tvNumberGift.setText(sendGiftNumber + "");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setNumberShow() {
        if (chooseNumber == 1) {
            tvAllmicGift.setBackground(getContext().getDrawable(R.drawable.allmic_gift_bg));
            tvDataGift.setVisibility(View.GONE);
        } else if (chooseNumber == userList.size() && userList.size() != 0) {
            tvAllmicGift.setBackground(getContext().getDrawable(R.drawable.qm_bg));
            tvDataGift.setVisibility(View.GONE);
        } else {
            tvAllmicGift.setBackground(getContext().getDrawable(R.drawable.allmic_gift_bg));
            tvDataGift.setVisibility(View.GONE);
        }
    }

    /**
     * 获取资料时的id
     *
     * @return
     */
    public int getOtherId() {
        return otherId;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * 发红包点击事件
     *
     * @param onClickListener
     */
    public void setPacketGiftListener(View.OnClickListener onClickListener) {
        ivPacketGift.setOnClickListener(onClickListener);
    }

    /**
     * 更新背包礼物数据
     */
    public void getPacketRefresh() {
        if (chooseOne == 5 || chooseOne == 6) {
            getCall();
        }
    }

    private void getCall() {
        tv_sendall_gift.setVisibility(View.GONE);
        tv_give_gift.setVisibility(View.GONE);

        String reqString;
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        if (chooseOne == 5 || chooseOne == 6) {
            reqString = Api.Knapsack;
            map.put("uid", userId);
            map.put("pageSize", Integer.MAX_VALUE);
            map.put("pageNum", 1);
            if (chooseOne == 5) {
                map.put("type", 1);
            } else {
                map.put("type", 2);
            }
        } else {
            reqString = Api.SaveRoomGift;
            map.put("state", chooseOne);
        }

        HttpManager.getInstance().post(reqString, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                GiftBean giftBean = JSON.parseObject(responseString, GiftBean.class);
                if (giftBean.getCode() == Api.SUCCESS) {
                    setData(giftBean.getData());
                } else {
                    showToast(giftBean.getMsg());
                }
            }
        });
    }

    private void setData(List<GiftBean.DataBean> data) {
        // add 20201201 添加活动礼物Tab
        gid = 0;
        sendGiftNumber = 1;
        tvNumberGift.setText(sendGiftNumber + "");
        if (chooseOne == 6) {
            imgNumberGift.setVisibility(View.GONE);
        } else {
            imgNumberGift.setVisibility(View.VISIBLE);
        }
        //--------

        final int size = data == null ? 0 : data.size();
        llIndicatorGift.removeAllViews();
        mViewPagerGift.removeAllViews();
        if (viewPagerAdapter != null) {
            viewPagerAdapter.notifyDataSetChanged();
        }
        if (size > 0) {
            tvNodataGift.setVisibility(View.INVISIBLE);
            mViewPagerGift.setVisibility(View.VISIBLE);
            groudAdapterList.clear();
            mPagerList.clear();

            setFragment(data, size);

            int total = 0;
            if (chooseOne == 5 || chooseOne == 6) {
                for (int i = 0; i < data.size(); i++) {
                    total += data.get(i).getGold() * data.get(i).getNum();
                }
                total_gifts_tv.setText("礼物总值：" + total);
                total_gifts_tv.setVisibility(View.VISIBLE);
                tvGoldGift.setVisibility(View.GONE);

                tv_sendall_gift.setVisibility(View.VISIBLE);
                if (chooseOne == 5){
                    tv_give_gift.setVisibility(View.GONE);
                    tv_sendall_gift.setText("一键全刷");
                } else {
                    tv_give_gift.setVisibility(View.VISIBLE);
                    tv_sendall_gift.setText("转赠");
                }
            } else {
                total_gifts_tv.setVisibility(View.GONE);
                tvGoldGift.setVisibility(View.VISIBLE);
            }

        } else {
            if (chooseOne == 5 || chooseOne == 6) {
                total_gifts_tv.setText("礼物总值：0");
                total_gifts_tv.setVisibility(View.VISIBLE);
                tvGoldGift.setVisibility(View.GONE);
            } else {
                total_gifts_tv.setVisibility(View.GONE);
                tvGoldGift.setVisibility(View.VISIBLE);
            }
            tvNodataGift.setVisibility(View.VISIBLE);
            mViewPagerGift.setVisibility(View.INVISIBLE);
        }
    }

    List<GiftGroudAdapter> groudAdapterList;

    public List<GiftGroudAdapter> getGroudAdapterList() {
        return groudAdapterList;
    }

    private GiftGroudAdapter giftGroudAdapter;
    private GridLayoutManager layoutManager;
    private int curIndex = 0;

    private void setFragment(List<GiftBean.DataBean> data, int size) {
        int fragNumber = size / 8;
        int fragEndNum = size % 8;
//        总的页数=总数/每页数量，并取整
//        int fragNumber = (int) Math.ceil(size / Const.IntShow.EIGHT);
        for (int i = 0; i <= fragNumber; i++) {
//            GiftFragment giftFragment = new GiftFragment();
            List<GiftBean.DataBean> giftData;
            if (fragNumber == i) {
                giftData = data.subList(i * 8, i * 8 + fragEndNum);
            } else {
                giftData = data.subList(i * 8, (i + 1) * 8);
            }
            RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(context)
                    .inflate(R.layout.layout_recycler_gift, mViewPagerGift, false);
            giftGroudAdapter = new GiftGroudAdapter(R.layout.item_gift);
            giftGroudAdapter.setSendType(sendType);
            recyclerView.setAdapter(giftGroudAdapter);
            giftGroudAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    GiftBean.DataBean dataBean = (GiftBean.DataBean) adapter.getItem(position);
                    gid = dataBean.getId();
                    img = dataBean.getImgFm();
                    showImg = dataBean.getImg();
                    goodGold = dataBean.getGold();
                    if (sendType == 2) {
                        packetNumber = dataBean.getNum();
                    }
                    for (int i = 0; i < groudAdapterList.size(); i++) {
                        groudAdapterList.get(i).setGid(gid);
                        groudAdapterList.get(i).notifyDataSetChanged();
                    }
                }
            });
            layoutManager = new GridLayoutManager(context, 4);
            recyclerView.setLayoutManager(layoutManager);
            giftGroudAdapter.setNewData(giftData);
            groudAdapterList.add(giftGroudAdapter);
            mPagerList.add(recyclerView);
        }
        viewPagerAdapter = new ViewPagerAdapter(mPagerList, context);
        mViewPagerGift.setAdapter(viewPagerAdapter);
        mViewPagerGift.setCurrentItem(0);
//        for (int i = 0; i < groudAdapterList.size(); i++) {
//            groudAdapterList.get(i).setGid(gid);
//            groudAdapterList.get(i).notifyDataSetChanged();
//        }
        for (int i = 0; i <= fragNumber; i++) {
            ImageView iv_indicator = (ImageView) LayoutInflater.from(context)
                    .inflate(R.layout.iv_indicator_gift, llIndicatorGift, false);
            if (i == 0) {
                iv_indicator.setSelected(true);
            }
            llIndicatorGift.addView(iv_indicator);
        }


        mViewPagerGift.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                llIndicatorGift.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(false);
                curIndex = i;
                llIndicatorGift.getChildAt(curIndex).findViewById(R.id.iv_choose_gift).setSelected(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    public void setDataShow(View.OnClickListener onClickListener) {
        tvDataGift.setOnClickListener(onClickListener);
    }


    @OnClick({R.id.tv_gold_gift, R.id.tv_topup_gift, R.id.tv_data_gift,
            R.id.tv_bao_gift, R.id.tv_nomal_gift, R.id.tv_miracle_gift, R.id.tv_exc_gift,
            R.id.mViewPager_gift, R.id.ll_indicator_gift, R.id.ll_send_gift,
            R.id.tv_number_gift_rl, R.id.tv_sendto_gift, R.id.tv_allmic_gift,
            R.id.tv_packet_gift, R.id.tv_sendpacket_gift, R.id.tv_party_gift,
            R.id.tv_othernum_gift, R.id.tv_sendall_gift, R.id.tv_give_gift})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_gold_gift:
                dismiss();
                Toast.create(context).show("期待开放");
//                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
                break;
            case R.id.tv_topup_gift:
                //充值页面
                dismiss();
                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
                break;
            case R.id.tv_data_gift:

                break;
            case R.id.tv_bao_gift://包裹
                if (chooseOne != 1) {
                    setData(null);
                    setNochoose();
                    chooseOne = 1;
                    setChoose();
                    getCall();
                }

                break;
            case R.id.tv_nomal_gift://礼物
                if (chooseOne != 2) {
                    setData(null);
                    setNochoose();
                    chooseOne = 2;
                    setChoose();
                    getCall();
                }
                break;
            case R.id.tv_miracle_gift://神奇礼物
                if (chooseOne != 3) {
                    setData(null);
                    setNochoose();
                    chooseOne = 3;
                    setChoose();
                    getCall();
                }
                break;
            case R.id.tv_exc_gift://专属礼物
                if (chooseOne != 4) {
                    setData(null);
                    setNochoose();
                    chooseOne = 4;
                    setChoose();
                    getCall();
                }
                break;
            case R.id.tv_packet_gift://背包
                if (chooseOne != 5) {
                    setNochoose();
                    chooseOne = 5;
                    setChoose();
                    getCall();
                    tvDataGift.setVisibility(View.GONE);
//                    ivPacketGift.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.tv_party_gift://活动礼物
                if (chooseOne != 6) {
                    setData(null);
                    setNochoose();
                    chooseOne = 6;
                    setChoose();
                    getCall();
                    tvDataGift.setVisibility(View.GONE);
                }
                break;
            case R.id.mViewPager_gift:
                break;
            case R.id.ll_indicator_gift:
                break;
            case R.id.ll_send_gift:
                if (type == 1) {
//                    if (rlAllmicGift.getVisibility() == View.GONE) {
//                        rlAllmicGift.setVisibility(View.VISIBLE);
//                    }
                }
//                if (type == 2 || type == 3) { //pk送礼物时无法选择用户
//                    return;
//                } else {
////                    getPersonCall(false);
//
//                }
                break;
            case R.id.tv_number_gift_rl:
                if (chooseOne != 6){
                    showNumber();
                }
                break;
            case R.id.tv_othernum_gift:
                llNumberGift.setVisibility(View.GONE);
                showNumberDialog();
                break;
            case R.id.tv_sendto_gift:
                setSend();
                break;
            case R.id.tv_allmic_gift:
                setShowList();
                break;
            case R.id.tv_sendpacket_gift:
//                showNumberDialog();
                break;
//            case R.id.rl_allmic_gift:
//                if (rlAllmicGift.getVisibility() == View.VISIBLE) {
//                    rlAllmicGift.setVisibility(View.GONE);
//                }
//                break;
            case R.id.tv_sendall_gift:
                if (chooseNumber == 1) {
                    showHintDialog();
                } else {
                    if (chooseOne == 5){
                        Toast.create(context).show("一键全刷只能选择一人");
                    } else {
                        Toast.create(context).show("转赠好友只能选择一人");
                    }
                }
                break;
            case R.id.tv_give_gift:
                GiveGiftDialog giveGiftDialog = new GiveGiftDialog(context, userId);
                giveGiftDialog.show();
                break;
        }
    }

    private void showHintDialog() {
        if (chooseOne == 6 && gid == 0){
            Toast.create(context).show("请选择礼物转赠");
            return;
        }
        MyDialog myDialog = new MyDialog(context);
        myDialog.show();
        if (chooseOne == 5){
            myDialog.setHintText("确定要将背包礼物一键全刷给 " + userName + " 吗？");
        } else {
            myDialog.setHintText("确定要将活动礼物转赠给 " + userName + " 吗？");
        }
        myDialog.setRightButton(v -> {
            myDialog.dismiss();
            tv_sendall_gift.setEnabled(false);

            if (sendAllPacketGift != null) {
                // http全刷请求服务器
                if (type == 1) {
                    userList = giftUserListAdapter.getData();
                    int sendID = -1;
                    if (userList.size() == 0 && chooseNumber == 1) {
                        sendID = otherId;
                    } else {
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).isChoose()) {
                                sendID = userList.get(i).getId();
                            }
                        }
                    }
                    if (sendID > 0){
                        sendAllPacketGift.sendAll(sendID, chooseOne, gid);
                    }
                } else {
                    sendAllPacketGift.sendAll(otherId, chooseOne, gid);
                }
            }
        });
    }

    /**
     * 请求一键全刷接口成功后,WS消息发送礼物
     */
    public void sendAllGiftAfter(){
        tv_sendall_gift.setEnabled(true);

        if (groudAdapterList != null){
            for (GiftGroudAdapter adapter : groudAdapterList){
                List<GiftBean.DataBean> giftBeans = adapter.getData();
                if (giftBeans!=null && giftBeans.size()>0){
                    for (GiftBean.DataBean bean : giftBeans){
                        gid = bean.getId();
                        img = bean.getImgFm();
                        showImg = "";
                        sendGiftNumber = bean.getNum();
                        goodGold = bean.getGold();

                        setAllSend();
                    }
                }
            }
        }
    }

    MyPacketNumDialog myPacketNumDialog;

    private void showNumberDialog() {
        if (myPacketNumDialog != null && myPacketNumDialog.isShowing()) {
            myPacketNumDialog.dismiss();
        }
        myPacketNumDialog = new MyPacketNumDialog(context, packetNumber);
        myPacketNumDialog.show();
        myPacketNumDialog.setRightButton(v -> {
            String number = myPacketNumDialog.getNumber();
            if (StringUtils.isEmpty(number)) {
                Toast.create(context).show("请输入赠送数量");
                return;
            }
            int numberShow = Integer.parseInt(number);
            if (numberShow == 0) {
                Toast.create(context).show("请输入赠送数量");
                return;
            }
//            if (numberShow > packetNumber) {
//                Toast.create(context).show("您的礼物数量不足");
//                return;
//            }
            sendGiftNumber = numberShow;
            if (myPacketNumDialog != null && myPacketNumDialog.isShowing()) {
                myPacketNumDialog.dismiss();
            }
            setSend();
        });

    }

    private void setAllSend() {
        if (chooseNumber == 0) {
            Toast.create(context).show("请选择麦上打赏嘉宾");
            return;
        }
        if (gid == 0) {
            Toast.create(context).show("请选择礼物");
            return;
        }

        if (type == 1) {
            userList = giftUserListAdapter.getData();
            StringBuffer ids = new StringBuffer();
            StringBuffer names = new StringBuffer();
            if (userList.size() == 0 && chooseNumber == 1) {
                ids.append(otherId);
                names.append(userName);
            } else {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).isChoose()) {
                        if (StringUtils.isEmpty(ids)) {
                            ids.append(userList.get(i).getId());
                            names.append(userList.get(i).getName());
                        } else {
                            ids.append(",");
                            ids.append(userList.get(i).getId());
                            names.append(",");
                            names.append(userList.get(i).getName());
                        }
                    }
                }
            }

            if (sendGift != null) {
                sendGift.getSendAllGift(ids.toString(), names.toString(), gid, img, showImg, sendGiftNumber, chooseNumber, goodGold, sendType);
            }
        } else {
            if (sendGift != null) {
                sendGift.getSendAllGift(String.valueOf(otherId), userName, gid, img, showImg, sendGiftNumber, chooseNumber, goodGold, sendType);
            }
        }

    }

    private void setSend() {
        if (chooseNumber == 0) {
            Toast.create(context).show("请选择麦上打赏嘉宾");
            return;
        }
        if (gid == 0) {
            Toast.create(context).show("请选择礼物");
            return;
        }

        new TUIKitDialog(getContext())
                .builder()
                .setCancelable(true)
                .setCancelOutside(true)
                .setTitle("您确定送出该礼物?")
                .setDialogWidth(0.75f)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 1) {
                            userList = giftUserListAdapter.getData();
                            StringBuffer ids = new StringBuffer();
                            StringBuffer names = new StringBuffer();
                            if (userList.size() == 0 && chooseNumber == 1) {
                                ids.append(otherId);
                                names.append(userName);
                            } else {
                                for (int i = 0; i < userList.size(); i++) {
                                    if (userList.get(i).isChoose()) {
                                        if (StringUtils.isEmpty(ids)) {
                                            ids.append(userList.get(i).getId());
                                            names.append(userList.get(i).getName());
                                        } else {
                                            ids.append(",");
                                            ids.append(userList.get(i).getId());
                                            names.append(",");
                                            names.append(userList.get(i).getName());
                                        }
                                    }
                                }
                            }

                            if (sendGift != null) {
                                sendGift.getSendGift(ids.toString(), names.toString(), gid, img, showImg, sendGiftNumber, chooseNumber, goodGold, sendType);
                            }
                        } else {
                            if (sendGift != null) {
                                sendGift.getSendGift(String.valueOf(otherId), userName, gid, img, showImg, sendGiftNumber, chooseNumber, goodGold, sendType);
                            }
                        }

                        handler.postDelayed(runnable,1500);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();

    }

    Runnable  runnable= new Runnable() {
        @Override
        public void run() {
            getGoldCall();
        }
    };

    public SendAllPacketGift getSendAllPacketGift() {
        return sendAllPacketGift;
    }

    public void setSendAllPacketGift(SendAllPacketGift sendAllPacketGift) {
        this.sendAllPacketGift = sendAllPacketGift;
    }

    SendAllPacketGift sendAllPacketGift;

    public interface SendAllPacketGift {
        void sendAll(int userId, int tab, int giftID);
    }

    SendGift sendGift;

    public interface SendGift {
        /**
         * @param ids      被送礼的用户id
         * @param names    用户名称
         * @param gid      礼物id
         * @param img      礼物图片
         * @param showImg  礼物动图
         * @param num      礼物数量
         * @param sum      被选中的人数
         * @param goodGold 礼物浪花单价
         * @param sendType 1 是普通， 2是背包
         */
        void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType);

        void getSendAllGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold, int sendType);
    }

    public SendGift getSendGift() {
        return sendGift;
    }

    public void setSendGift(SendGift sendGift) {
        this.sendGift = sendGift;
    }

    /**
     * 全选或取消全选用户
     */
    @SuppressLint("SetTextI18n")
    private void setShowList() {
        tvDataGift.setVisibility(View.GONE);
        if (chooseNumber == userList.size()) {
            chooseNumber = 0;
            tvAllmicGift.setBackground(getContext().getDrawable(R.drawable.allmic_gift_bg));
            for (VoiceUserBean.DataBean dataBean :
                    userList) {
                dataBean.setChoose(false);
            }
        } else {
            chooseNumber = userList.size();
            tvAllmicGift.setBackground(getContext().getDrawable(R.drawable.qm_bg));
            for (VoiceUserBean.DataBean dataBean :
                    userList) {
                dataBean.setChoose(true);
            }
        }
        giftUserListAdapter.replaceData(userList);

    }

    /**
     * 显示数量
     */
    private void showNumber() {
        if (llNumberGift.getVisibility() == View.GONE) {
            llNumberGift.setVisibility(View.VISIBLE);
        } else {
            llNumberGift.setVisibility(View.GONE);
        }
    }

    private void setChoose() {
        switch (chooseOne) {
            case 1:
//                llSendtoGift.setVisibility(View.VISIBLE);
//                tvSendpacketGift.setVisibility(View.GONE);
                sendType = 1;
                tvBaoGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvBaoGift.setTextSize(14);
                tvBaoGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);

                break;
            case 2:
//                llSendtoGift.setVisibility(View.VISIBLE);
//                tvSendpacketGift.setVisibility(View.GONE);
                sendType = 1;
                tvNomalGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvNomalGift.setTextSize(14);
                tvNomalGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);

                break;
            case 3:
//                llSendtoGift.setVisibility(View.VISIBLE);
//                tvSendpacketGift.setVisibility(View.GONE);
                sendType = 1;
                tvMiracleGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvMiracleGift.setTextSize(14);
                tvMiracleGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);

                break;
            case 4:
//                llSendtoGift.setVisibility(View.VISIBLE);
//                tvSendpacketGift.setVisibility(View.GONE);
                sendType = 1;
                tvExcGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvExcGift.setTextSize(14);
                tvExcGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);

                break;
            case 5:
//                llSendtoGift.setVisibility(View.GONE);
//                tvSendpacketGift.setVisibility(View.VISIBLE);
                sendType = 2;
                tvPacketGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPacketGift.setTextSize(14);
                tvPacketGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);

                break;
            case 6:
                sendType = 2;
                tvPartyGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPartyGift.setTextSize(14);
                tvPartyGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);

                break;
        }
    }

    private void setNochoose() {
        switch (chooseOne) {
            case 1:
                tvBaoGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvBaoGift.setTextSize(12);
                tvBaoGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 2:
                tvNomalGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvNomalGift.setTextSize(12);
                tvNomalGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                break;
            case 3:
                tvMiracleGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvMiracleGift.setTextSize(12);
                tvMiracleGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                break;
            case 4:
                tvExcGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvExcGift.setTextSize(12);
                tvExcGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                break;
            case 5:
                tvPacketGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPacketGift.setTextSize(12);
                tvPacketGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                break;
            case 6:
                tvPartyGift.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPartyGift.setTextSize(12);
                tvPartyGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeCallbacks(runnable);
    }
}