package com.jingtaoi.yy.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.VoiceUserBean;
import com.jingtaoi.yy.model.GiftNumber;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.room.TopupActivity;
import com.jingtaoi.yy.ui.room.adapter.GiftUserListAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.jingtaoi.yy.view.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Toast;


/**
 * 红包打赏页面
 * Created by Administrator on 2018/3/9.
 */

public class MyRewardDialog extends Dialog {


    Context mContext;
    @BindView(R.id.tv_name_reward)
    TextView tvNameReward;
    @BindView(R.id.tv_number_reward)
    TextView tvNumberReward;
    @BindView(R.id.rl_number_reward)
    RelativeLayout rlNumberReward;
    @BindView(R.id.tv_numbershow_reward)
    TextView tvNumbershowReward;
    @BindView(R.id.btn_reward)
    Button btnReward;
    @BindView(R.id.edt_you_reward)
    EditText edtYouReward;
    @BindView(R.id.tv_you_reward)
    TextView tvYouReward;
    @BindView(R.id.tv_topup_reward)
    TextView tvTopupReward;
    @BindView(R.id.mRecyclerView_reward)
    RecyclerView mRecyclerViewReward;
    @BindView(R.id.rl_list_reward)
    RelativeLayout rlListReward;
    @BindView(R.id.ll_all_reward)
    LinearLayout llAllReward;
    @BindView(R.id.tv_allmic_reward)
    TextView tvAllmicReward;
    @BindView(R.id.iv_close_reward)
    ImageView ivCloseReward;
    @BindView(R.id.mFlow_reward)
    FlowLayout mFlowReward;
    @BindView(R.id.iv_name_reward)
    ImageView ivNameReward;
    @BindView(R.id.ll_name_reward)
    LinearLayout llNameReward;

    private List<VoiceUserBean.DataBean> userList;//麦上用户信息
    private GiftUserListAdapter giftUserListAdapter;
    private List<GiftNumber> giftNumbers;
    int chooseNumber;//判断选中用户
    String roomId;
    int userId;
    int otherId;//被赠送用户id
    String otherName;
    int type;//  1默认全麦嘉宾，2一个嘉宾

    public MyRewardDialog(Context context, String roomId, int userId, int type) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
        this.roomId = roomId;
        this.userId = userId;
        this.type = type;
    }


    public MyRewardDialog(Context context, String roomId, int userId, int otherId, String otherName, int type) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
        this.roomId = roomId;
        this.userId = userId;
        this.otherId = otherId;
        this.otherName = otherName;
        this.type = type;
        chooseNumber = 1;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_reward);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        initShow();
        setRecycler();


        if (type == 1) {
            getCall();
        } else if (type == 2) {
            llNameReward.setClickable(false);
            ivNameReward.setVisibility(View.GONE);
            setNumberShow();
            setFlow(giftNumbers);
        }
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        HttpManager.getInstance().post(Api.getChatroomsUser, map, new MyObserver(mContext) {
            @Override
            public void success(String responseString) {
                VoiceUserBean voiceUserBean = JSON.parseObject(responseString, VoiceUserBean.class);
                if (voiceUserBean.getCode() == Api.SUCCESS) {
                    setData(voiceUserBean.getData());
                } else {
                    showToast(voiceUserBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(List<VoiceUserBean.DataBean> data) {
        final int size = data == null ? 0 : data.size();
        for (VoiceUserBean.DataBean userBean : data) {
            if (userBean.getId() == userId) {
                continue;
            }
            userBean.setChoose(true);
            userList.add(userBean);
            chooseNumber++;
        }
        setNumberShow();
    }

    @SuppressLint("SetTextI18n")
    private void setFlow(List<GiftNumber> list) {
        mFlowReward.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.tv_reward, mFlowReward, false);
            TextView tv = view.findViewById(R.id.tv_show_reward);
            tv.setText(list.get(i).getNumberShow() + list.get(i).getDesShow());
//            final String str = tv.getText().toString();
            int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtYouReward.setText(list.get(finalI).getNumberShow() + "");
                }
            });
            mFlowReward.addView(view);
        }
    }

    private void setRecycler() {
        giftUserListAdapter = new GiftUserListAdapter(R.layout.item_giftuser);
        mRecyclerViewReward.setAdapter(giftUserListAdapter);
        mRecyclerViewReward.setLayoutManager(new LinearLayoutManager(mContext));
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
                        List<VoiceUserBean.DataBean> listNow = adapter.getData();
                        for (int i = 0; i < listNow.size(); i++) {
                            if (listNow.get(i).isChoose()) {
                                otherName = listNow.get(i).getName();
                                break;
                            }
                        }
                    }
                } else {
                    dataBean.setChoose(true);
                    chooseNumber++;
                    if (chooseNumber == 1) {
                        otherName = dataBean.getName();
                    }
                }
                adapter.setData(position, dataBean);
                setNumberShow();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initShow() {
        userList = new ArrayList<>();
        giftNumbers = new ArrayList<>();

        giftNumbers.add(new GiftNumber(1, mContext.getString(R.string.tv_number1_gift)));
        giftNumbers.add(new GiftNumber(10, mContext.getString(R.string.tv_number10_gift)));
        giftNumbers.add(new GiftNumber(38, mContext.getString(R.string.tv_number38_gift)));
        giftNumbers.add(new GiftNumber(66, mContext.getString(R.string.tv_number66_gift)));
        giftNumbers.add(new GiftNumber(188, mContext.getString(R.string.tv_number188_gift)));
        giftNumbers.add(new GiftNumber(520, mContext.getString(R.string.tv_number520_gift)));
        giftNumbers.add(new GiftNumber(1314, mContext.getString(R.string.tv_number1314_gift)));

        int gold = (int) SharedPreferenceUtils.get(mContext, Const.User.GOLD, 0);
        tvYouReward.setText(gold + mContext.getString(R.string.tv_you));

        edtYouReward.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = edtYouReward.getText().toString();
                if (!StringUtils.isEmpty(str)) {
                    if (Integer.valueOf(str) > 2000) {
                        str = "2000";
                        edtYouReward.setText(str);
                        edtYouReward.setSelection(str.length());
                    }
                }
                tvNumbershowReward.setText(str);
            }
        });

    }

    private void setNumberShow() {
        if (chooseNumber == 1) {
            tvAllmicReward.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            tvNameReward.setText(otherName);
        } else if (chooseNumber == userList.size()) {
            tvAllmicReward.setTextColor(ContextCompat.getColor(mContext, R.color.FF003F));
            tvNameReward.setText(mContext.getString(R.string.tv_all_person));
        } else {
            tvAllmicReward.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            tvNameReward.setText(chooseNumber + mContext.getString(R.string.tv_number_person));
        }
    }


    @OnClick({R.id.ll_name_reward, R.id.rl_number_reward, R.id.btn_reward,
            R.id.tv_topup_reward, R.id.tv_allmic_reward, R.id.iv_close_reward
            , R.id.rl_list_reward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_name_reward:
                if (userList.size() == 0) {
                    getCall();
                } else {
                    if (rlListReward.getVisibility() == View.VISIBLE) {
                        rlListReward.setVisibility(View.GONE);
                    } else if (rlListReward.getVisibility() == View.GONE) {
                        rlListReward.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.iv_close_reward:
                dismiss();
                break;
            case R.id.rl_number_reward:

                break;
            case R.id.tv_topup_reward:
                dismiss();
                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
                break;
            case R.id.tv_allmic_reward:
                setShowList();
                break;
            case R.id.rl_list_reward:
                rlListReward.setVisibility(View.GONE);
                break;
            case R.id.btn_reward:
                if (type == 1) {//默认全嘉宾
                    setSend();
                } else if (type == 2) {
                    String sendGold = edtYouReward.getText().toString();
                    if (StringUtils.isEmpty(sendGold)) {
                        Toast.create(mContext).show("请选择或输入打赏金额");
                        return;
                    }
                    int sendGoldint = Integer.parseInt(sendGold);
                    if (sendGoldint < chooseNumber) {
                        Toast.create(mContext).show("红包数量不可大于红包金额");
                        return;
                    }
                    dismiss();
                    if (sendRedPacket != null) {
                        sendRedPacket.getRedPacket(sendGoldint, String.valueOf(otherId), chooseNumber);
                    }
                }
                break;
        }
    }

    private void setSend() {
        if (chooseNumber == 0) {
            Toast.create(mContext).show("请选择打赏嘉宾");
            return;
        }
        userList = giftUserListAdapter.getData();
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).isChoose()) {
                if (StringUtils.isEmpty(ids)) {
                    ids.append(userList.get(i).getId());
                } else {
                    ids.append(",");
                    ids.append(userList.get(i).getId());
                }
            }
        }
        String sendGold = edtYouReward.getText().toString();
        if (StringUtils.isEmpty(sendGold)) {
            Toast.create(mContext).show("请选择或输入打赏金额");
            return;
        }
        int sendGoldint = Integer.parseInt(sendGold);
        if (sendGoldint < chooseNumber) {
            Toast.create(mContext).show("红包数量不可大于红包金额");
            return;
        }
        dismiss();
        if (sendRedPacket != null) {
            sendRedPacket.getRedPacket(sendGoldint, ids.toString(), chooseNumber);
        }
    }

    /**
     * 发红包接口回调
     */
    public interface SendRedPacket {
        void getRedPacket(int gold, String ids, int num);
    }

    SendRedPacket sendRedPacket;

    public SendRedPacket getSendRedPacket() {
        return sendRedPacket;
    }

    public void setSendRedPacket(SendRedPacket sendRedPacket) {
        this.sendRedPacket = sendRedPacket;
    }

    @SuppressLint("SetTextI18n")
    private void setShowList() {
        if (chooseNumber == userList.size()) {
            chooseNumber = 0;
            tvNameReward.setText(chooseNumber + mContext.getString(R.string.tv_number_person));
            tvAllmicReward.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            for (VoiceUserBean.DataBean dataBean :
                    userList) {
                dataBean.setChoose(false);
            }
        } else {
            chooseNumber = userList.size();
            tvNameReward.setText(mContext.getString(R.string.tv_all_person));
            tvAllmicReward.setTextColor(ContextCompat.getColor(mContext, R.color.FF003F));
            for (VoiceUserBean.DataBean dataBean : userList) {
                dataBean.setChoose(true);
            }
        }
        giftUserListAdapter.replaceData(userList);
    }
}