package com.jingtaoi.yy.ui.message.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.base.MyApplication;
import com.jingtaoi.yy.bean.GetOneBean;
import com.jingtaoi.yy.dialog.MyPacketNumDialog;
import com.jingtaoi.yy.utils.BroadcastManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.GiftBean;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.model.GiftNumber;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.message.adapter.GiftChatAdapter;
import com.jingtaoi.yy.ui.room.TopupActivity;
import com.jingtaoi.yy.ui.room.adapter.GiftNumberListAdapter;
import com.jingtaoi.yy.ui.room.adapter.GiftUserListAdapter;
import com.jingtaoi.yy.ui.room.adapter.ViewPagerAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Toast;

public class GiftDialogFragment extends DialogFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_gold_gift)
    TextView tvGoldGift;
    @BindView(R.id.tv_topup_gift)
    TextView tvTopupGift;
    @BindView(R.id.tv_data_gift)
    TextView tvDataGift;
    @BindView(R.id.iv_packet_gift)
    ImageView ivPacketGift;
    @BindView(R.id.tv_bao_gift)
    TextView tvBaoGift;
    @BindView(R.id.tv_nomal_gift)
    TextView tvNomalGift;
    @BindView(R.id.tv_miracle_gift)
    TextView tvMiracleGift;
    @BindView(R.id.tv_exc_gift)
    TextView tvExcGift;
    @BindView(R.id.mViewPager_gift)
    ViewPager mViewPagerGift;
    @BindView(R.id.ll_indicator_gift)
    LinearLayout llIndicatorGift;
    @BindView(R.id.tv_send_gift)
    TextView tvSendGift;
    @BindView(R.id.ll_send_gift)
    LinearLayout llSendGift;
    @BindView(R.id.tv_number_gift)
    TextView tvNumberGift;
    @BindView(R.id.tv_sendto_gift)
    TextView tvSendtoGift;
    @BindView(R.id.tv_allmic_gift)
    TextView tvAllmicGift;
    @BindView(R.id.mRecyclerView_gift)
    RecyclerView mRecyclerViewGift;
    @BindView(R.id.ll_allmic_gift)
    LinearLayout llAllmicGift;
    @BindView(R.id.rl_allmic_gift)
    RelativeLayout rlAllmicGift;
    @BindView(R.id.mRecyclerView_number_gift)
    RecyclerView mRecyclerViewNumberGift;
    @BindView(R.id.ll_number_gift)
    LinearLayout llNumberGift;
    @BindView(R.id.tv_nodata_gift)
    TextView tvNodataGift;

    private int chooseOne;//被选中的title
    //    FragPagerAdapter fragPagerAdapter;
    private ArrayList<String> titles;
    private ArrayList<Fragment> fragments;

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
    private int type;//1送礼物  2pk送礼物 3,聊天送礼物
    private String userName;//被送礼物的用户名称
    private int otherId;//被送礼物的用户id
    private String roomId;//房间id
    private int userId;//送礼物的用户id
    private int roomState;//房间类型  是否牌子房间 1否，2是

    private ViewPagerAdapter viewPagerAdapter;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_giftchat, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMeg();
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
                    tvGoldGift.setText(getOneBean.getData().getGold() + "浪花");
                } else {
                    showToast(getOneBean.getMsg());
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewGroup.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = getResources().getDimensionPixelSize(R.dimen.dp_370);
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
    }


    private void getMeg() {
        Bundle bundle = getArguments();
        assert bundle != null;
        context = getContext();
        type = 3;//聊天送礼物
        roomState = 1;//不显示红包
        chooseNumber = 1;
        userId = bundle.getInt(Const.User.USER_TOKEN);
        otherId = bundle.getInt(Const.ShowIntent.OTHRE_ID);
        userName = bundle.getString(Const.ShowIntent.NAME);
    }

    private void setData() {
        setRecycler();
        getCall();
        if (type == 1) {
            getPersonCall();
        } else {
            setNumberShow();
        }
    }

    private void getPersonCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.getChatroomsUser, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                VoiceUserBean voiceUserBean = JSON.parseObject(responseString, VoiceUserBean.class);
                if (voiceUserBean.getCode() == Api.SUCCESS) {
                    setPersonData(voiceUserBean.getData());
                } else {
                    showToast(voiceUserBean.getMsg());
                }
            }
        });
    }

    private void setPersonData(List<VoiceUserBean.DataBean> data) {
        for (VoiceUserBean.DataBean userBean : data) {
            if (userBean.getId() == userId) {
                continue;
            } else if (userBean.getId() == otherId) {
                userBean.setChoose(true);
                chooseNumber++;
            }
            userList.add(userBean);
        }
        setNumberShow();
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
        tvGoldGift.setText(goldYou + "浪花");

        if (roomState == 1) {
            ivPacketGift.setVisibility(View.GONE);
        } else if (roomState == 2) {
            ivPacketGift.setVisibility(View.VISIBLE);
        }
    }

    private void setRecycler() {
        giftUserListAdapter = new GiftUserListAdapter(R.layout.item_giftuser);
        mRecyclerViewGift.setAdapter(giftUserListAdapter);
        mRecyclerViewGift.setLayoutManager(new LinearLayoutManager(context));
        giftUserListAdapter.setNewData(userList);

        giftUserListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VoiceUserBean.DataBean dataBean = (VoiceUserBean.DataBean) adapter.getItem(position);
                assert dataBean != null;
                if (dataBean.isChoose()) {
                    dataBean.setChoose(false);
                    chooseNumber--;
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
                } else {
                    dataBean.setChoose(true);
                    chooseNumber++;
                }
                adapter.setData(position, dataBean);
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
            tvSendGift.setText(userName);
            tvDataGift.setVisibility(View.VISIBLE);
        } else if (chooseNumber == userList.size() && userList.size() != 0) {
            tvAllmicGift.setTextColor(ContextCompat.getColor(context, R.color.FF003F));
            tvSendGift.setText(context.getString(R.string.tv_all_person));
            tvDataGift.setVisibility(View.GONE);
        } else {
            tvAllmicGift.setTextColor(ContextCompat.getColor(context, R.color.white));
            tvSendGift.setText(chooseNumber + context.getString(R.string.tv_number_person));
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

    /**
     * 发红包点击事件
     *
     * @param onClickListener
     */
    public void setPacketGiftListener(View.OnClickListener onClickListener) {
        ivPacketGift.setOnClickListener(onClickListener);
    }


    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", chooseOne);
        HttpManager.getInstance().post(Api.SaveRoomGift, map, new MyObserver(context) {
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
        final int size = data == null ? 0 : data.size();
        if (llIndicatorGift != null){
            llIndicatorGift.removeAllViews();
        }
        if (mViewPagerGift != null){
            mViewPagerGift.removeAllViews();
        }
        if (viewPagerAdapter != null) {
            viewPagerAdapter.notifyDataSetChanged();
        }
        if (size > 0) {
            tvNodataGift.setVisibility(View.INVISIBLE);
            mViewPagerGift.setVisibility(View.VISIBLE);
            groudAdapterList.clear();
            mPagerList.clear();

            setFragment(data, size);
        } else {
            tvNodataGift.setVisibility(View.VISIBLE);
            mViewPagerGift.setVisibility(View.INVISIBLE);
        }
    }

    List<GiftChatAdapter> groudAdapterList;
    private GiftChatAdapter giftChatAdapter;
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
            giftChatAdapter = new GiftChatAdapter(R.layout.item_gift);
            recyclerView.setAdapter(giftChatAdapter);
            giftChatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    GiftBean.DataBean dataBean = (GiftBean.DataBean) adapter.getItem(position);
                    gid = dataBean.getId();
                    img = dataBean.getImgFm();
                    showImg = dataBean.getImg();
                    goodGold = dataBean.getGold();
                    for (int i = 0; i < groudAdapterList.size(); i++) {
                        groudAdapterList.get(i).setGid(gid);
                        groudAdapterList.get(i).notifyDataSetChanged();
                    }
                }
            });
            layoutManager = new GridLayoutManager(context, 4);
            recyclerView.setLayoutManager(layoutManager);
            giftChatAdapter.setNewData(giftData);
            groudAdapterList.add(giftChatAdapter);
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


    @OnClick({R.id.tv_gold_gift, R.id.tv_topup_gift, R.id.tv_data_gift,
            R.id.tv_bao_gift, R.id.tv_nomal_gift, R.id.tv_miracle_gift, R.id.tv_exc_gift,
            R.id.mViewPager_gift, R.id.ll_indicator_gift, R.id.ll_send_gift,
            R.id.tv_number_gift, R.id.tv_sendto_gift, R.id.tv_allmic_gift,
            R.id.rl_allmic_gift, R.id.tv_othernum_gift})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_gold_gift:
                break;
            case R.id.tv_topup_gift:
                dismiss();
                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
                break;
            case R.id.tv_data_gift:
                if (sendGift != null) {
                    sendGift.onUserClicker();
                }
                break;
            case R.id.tv_bao_gift:
                if (chooseOne != 1) {
                    setNochoose();
                    chooseOne = 1;
                    setChoose();
                    getCall();
                }

                break;
            case R.id.tv_nomal_gift:
                if (chooseOne != 2) {
                    setNochoose();
                    chooseOne = 2;
                    setChoose();
                    getCall();
                }
                break;
            case R.id.tv_miracle_gift:
                if (chooseOne != 3) {
                    setNochoose();
                    chooseOne = 3;
                    setChoose();
                    getCall();
                }
                break;
            case R.id.tv_exc_gift:
                if (chooseOne != 4) {
                    setNochoose();
                    chooseOne = 4;
                    setChoose();
                    getCall();
                }
                break;
            case R.id.mViewPager_gift:
                break;
            case R.id.ll_indicator_gift:
                break;
            case R.id.ll_send_gift:

                break;
            case R.id.tv_number_gift:
                showNumber();
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
            case R.id.rl_allmic_gift:
                if (rlAllmicGift.getVisibility() == View.VISIBLE) {
                    rlAllmicGift.setVisibility(View.GONE);
                }
                break;
        }
    }

    MyPacketNumDialog myPacketNumDialog;

    private void showNumberDialog() {
        if (myPacketNumDialog != null && myPacketNumDialog.isShowing()) {
            myPacketNumDialog.dismiss();
        }
        myPacketNumDialog = new MyPacketNumDialog(context, 0);
        myPacketNumDialog.show();
        myPacketNumDialog.setRightButton(v -> {
            String number = myPacketNumDialog.getNumber();
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

    private void setSend() {
        if (chooseNumber == 0) {
            Toast.create(context).show("请选择麦上打赏嘉宾");
            return;
        }
        if (gid == 0) {
            Toast.create(context).show("请选择礼物");
            return;
        }
        if (type == 2 || type == 3) {
            if (sendGift != null) {
                sendGift.getSendGift(String.valueOf(otherId), userName, gid, img, showImg, sendGiftNumber, chooseNumber, goodGold);
            }
        } else {
            userList = giftUserListAdapter.getData();
            StringBuffer ids = new StringBuffer();
            StringBuffer names = new StringBuffer();
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
            if (sendGift != null) {
                sendGift.getSendGift(ids.toString(), names.toString(), gid, img, showImg, sendGiftNumber, chooseNumber, goodGold);
            }
        }
        dismiss();
    }

    SendGift sendGift;

    public void setSendGift(SendGift sendGift) {
        this.sendGift = sendGift;
    }


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
         */
        void getSendGift(String ids, String names, int gid, String img, String showImg, int num, int sum, int goodGold);


        /**
         * 资料点击事件
         */
        void onUserClicker();
    }

    /**
     * 全选或取消全选用户
     */
    @SuppressLint("SetTextI18n")
    private void setShowList() {
        tvDataGift.setVisibility(View.GONE);
        if (chooseNumber == userList.size()) {
            chooseNumber = 0;
            tvSendGift.setText(chooseNumber + context.getString(R.string.tv_number_person));
            tvAllmicGift.setTextColor(ContextCompat.getColor(context, R.color.white));
            for (VoiceUserBean.DataBean dataBean :
                    userList) {
                dataBean.setChoose(false);
            }
        } else {
            chooseNumber = userList.size();
            tvSendGift.setText(context.getString(R.string.tv_all_person));
            tvAllmicGift.setTextColor(ContextCompat.getColor(context, R.color.FF003F));
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
                tvBaoGift.setTextColor(ContextCompat.getColor(context, R.color.text_ff0));
                break;
            case 2:
                tvNomalGift.setTextColor(ContextCompat.getColor(context, R.color.text_ff0));
                break;
            case 3:
                tvMiracleGift.setTextColor(ContextCompat.getColor(context, R.color.text_ff0));
                break;
            case 4:
                tvExcGift.setTextColor(ContextCompat.getColor(context, R.color.text_ff0));
                break;
        }
    }

    private void setNochoose() {
        switch (chooseOne) {
            case 1:
                tvBaoGift.setTextColor(ContextCompat.getColor(context, R.color.white6));
                break;
            case 2:
                tvNomalGift.setTextColor(ContextCompat.getColor(context, R.color.white6));
                break;
            case 3:
                tvMiracleGift.setTextColor(ContextCompat.getColor(context, R.color.white6));
                break;
            case 4:
                tvExcGift.setTextColor(ContextCompat.getColor(context, R.color.white6));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
