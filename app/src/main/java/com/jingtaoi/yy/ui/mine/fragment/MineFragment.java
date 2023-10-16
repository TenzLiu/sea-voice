package com.jingtaoi.yy.ui.mine.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.UserBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.find.RealnameActivity;
import com.jingtaoi.yy.ui.mine.GradeActivity;
import com.jingtaoi.yy.ui.mine.MyAttentionActivity;
import com.jingtaoi.yy.ui.mine.MyFansActivity;
import com.jingtaoi.yy.ui.mine.PersonDataActivity;
import com.jingtaoi.yy.ui.mine.PersonHomeActivity;
import com.jingtaoi.yy.ui.mine.SettingActivity;
import com.jingtaoi.yy.ui.mine.StoreActivity;
import com.jingtaoi.yy.ui.mine.WalletActivity;
import com.jingtaoi.yy.ui.other.WebActivity;
import com.jingtaoi.yy.ui.room.TopupActivity;
import com.jingtaoi.yy.ui.room.VoiceActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * Created by Administrator on 2018/12/19.
 */

public class MineFragment extends MyBaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_show_mine)
    SimpleDraweeView ivShowMine;
    @BindView(R.id.tv_nickname_mine)
    TextView tvNicknameMine;
    @BindView(R.id.iv_sex_mine)
    TextView ivSexMine;
    @BindView(R.id.tv_id_mine)
    TextView tvIdMine;
    @BindView(R.id.tv_attention_mine)
    TextView tvAttentionMine;
    @BindView(R.id.tv_fans_mine)
    TextView tvFansMine;
    @BindView(R.id.iv_panit_mine)
    TextView ivPanitMine;
    @BindView(R.id.rl_mywallet_mine)
    RelativeLayout rlMywalletMine;
    @BindView(R.id.rl_topup_mine)
    RelativeLayout rlTopupMine;
    @BindView(R.id.rl_grade_mine)
    RelativeLayout rlGradeMine;
    @BindView(R.id.rl_store_mine)
    RelativeLayout rlStoreMine;
    @BindView(R.id.rl_myroom_mine)
    RelativeLayout rlMyroomMine;
    @BindView(R.id.rl_setting_mine)
    RelativeLayout rlSettingMine;
    @BindView(R.id.iv_headwear_mine)
    SimpleDraweeView ivHeadwearMine;
    @BindView(R.id.liang_iv)
    ImageView liang_iv;


    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void initView() {
        initShow();
        getUserCall();
    }

    @Override
    protected void onVisibleToUser() {
        super.onVisibleToUser();
        getUserCall();
    }

    private void getUserCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.getUserInfo, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    initShared(userBean.getData());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initShared(UserBean.DataBean dataBean) {



        SharedPreferenceUtils.put(Objects.requireNonNull(getContext()), Const.User.USER_TOKEN, dataBean.getId());
        SharedPreferenceUtils.put(getContext(), Const.User.AGE, dataBean.getAge());
        SharedPreferenceUtils.put(getContext(), Const.User.IMG, dataBean.getImgTx());
        SharedPreferenceUtils.put(getContext(), Const.User.SEX, dataBean.getSex());
        SharedPreferenceUtils.put(getContext(), Const.User.NICKNAME, dataBean.getNickname());
        SharedPreferenceUtils.put(getContext(), Const.User.ROOMID, dataBean.getUsercoding());
        SharedPreferenceUtils.put(getContext(), Const.User.CharmGrade, dataBean.getCharmGrade());
        SharedPreferenceUtils.put(getContext(), Const.User.DATEOFBIRTH, dataBean.getDateOfBirth());
        SharedPreferenceUtils.put(getContext(), Const.User.FansNum, dataBean.getFansNum());
        SharedPreferenceUtils.put(getContext(), Const.User.AttentionNum, dataBean.getAttentionNum());
        SharedPreferenceUtils.put(getContext(), Const.User.GOLD, dataBean.getGold());
        SharedPreferenceUtils.put(getContext(), Const.User.GoldNum, dataBean.getGoldNum());
        SharedPreferenceUtils.put(getContext(), Const.User.GRADE_T, dataBean.getTreasureGrade());
        SharedPreferenceUtils.put(getContext(), Const.User.PHONE, dataBean.getPhone());
        SharedPreferenceUtils.put(getContext(), Const.User.QQSID, dataBean.getQqSid());
        SharedPreferenceUtils.put(getContext(), Const.User.WECHATSID, dataBean.getWxSid());
        SharedPreferenceUtils.put(getContext(), Const.User.Ynum, dataBean.getYnum());
        SharedPreferenceUtils.put(getContext(), Const.User.Yuml, dataBean.getYuml());
        SharedPreferenceUtils.put(getContext(), Const.User.HEADWEAR_H, dataBean.getUserThfm());
        SharedPreferenceUtils.put(getContext(), Const.User.CAR_H, dataBean.getUserZjfm());
        SharedPreferenceUtils.put(getContext(), Const.User.HEADWEAR, dataBean.getUserTh());
        SharedPreferenceUtils.put(getContext(), Const.User.CAR, dataBean.getUserZj());
        SharedPreferenceUtils.put(getContext(), Const.User.SIGNER, dataBean.getIndividuation());
        SharedPreferenceUtils.put(getContext(), Const.User.USER_LiANG, dataBean.getLiang());
        SharedPreferenceUtils.put(getContext(), Const.User.PAY_PASS, dataBean.getPayPassword());
        SharedPreferenceUtils.put(getContext(), Const.User.IS_AGENT_GIVE, dataBean.getIsAgentGive());
//        SharedPreferenceUtils.put(getContext(), Const.User.USER_SIG, dataBean.getToken());
        initShow();
    }

    String imgShow;

    @SuppressLint("SetTextI18n")
    private void initShow() {
        String img = (String) SharedPreferenceUtils.get(getContext(), Const.User.IMG, "");
        String headwear = (String) SharedPreferenceUtils.get(getContext(), Const.User.HEADWEAR, "");
        String nickName = (String) SharedPreferenceUtils.get(getContext(), Const.User.NICKNAME, "");
        int attentionNumber = (int) SharedPreferenceUtils.get(getContext(), Const.User.AttentionNum, 0);
        int fansNumber = (int) SharedPreferenceUtils.get(getContext(), Const.User.FansNum, 0);
        String roomId = (String) SharedPreferenceUtils.get(getContext(), Const.User.ROOMID, "");
        int sexShow = (int) SharedPreferenceUtils.get(getContext(), Const.User.SEX, 0);
        String userLiang = (String) SharedPreferenceUtils.get(getContext(), Const.User.USER_LiANG, "");
        int age = (int) SharedPreferenceUtils.get(getContext(), Const.User.AGE, 0);

        if (TextUtils.isEmpty(userLiang)){
            liang_iv.setVisibility(View.GONE);
        }else {
            liang_iv.setVisibility(View.VISIBLE);
        }


        ivSexMine.setText(String.valueOf(age)+"岁");

        if (StringUtils.isEmpty(imgShow)) {
            imgShow = img;
            ImageUtils.loadUri(ivShowMine, imgShow);
        } else {
            if (!imgShow.equals(img)) {
                imgShow = img;
                ImageUtils.loadUri(ivShowMine, imgShow);
            }
        }
        if (!StringUtils.isEmpty(headwear)) {
            ImageUtils.loadUri(ivHeadwearMine, headwear);
        }

        if (sexShow == 1) {
            Drawable drawable = getResources().getDrawable(R.drawable.sex_male_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ivSexMine.setCompoundDrawables(drawable, null, null, null);
        } else if (sexShow == 2) {
            Drawable drawable = getResources().getDrawable(R.drawable.sex_female_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ivSexMine.setCompoundDrawables(drawable, null, null, null);
        }
        tvNicknameMine.setText(nickName);
        if (!StringUtils.isEmpty(userLiang)) {
            tvIdMine.setText("ID：" + userLiang);
        } else {
            tvIdMine.setText("ID：" + roomId);
        }

        tvAttentionMine.setText(attentionNumber + "");
        tvFansMine.setText(fansNumber + "");
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_show_mine, R.id.tv_nickname_mine, R.id.tv_attention_mine, R.id.tv_fans_mine, R.id.iv_panit_mine,
            R.id.rl_mywallet_mine, R.id.rl_topup_real, R.id.rl_topup_mine, R.id.rl_grade_mine, R.id.rl_store_mine,
            R.id.rl_myroom_mine, R.id.rl_setting_mine, R.id.rl_about_mine})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.rl_about_mine:
                bundle.putInt(Const.ShowIntent.TYPE, 14);
                bundle.putString(Const.ShowIntent.TITLE, "关于我们");
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.iv_show_mine:
                bundle.putInt(Const.ShowIntent.ID, userToken);
                ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                break;
            case R.id.tv_nickname_mine:
                bundle.putInt(Const.ShowIntent.ID, userToken);
                ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                break;
            case R.id.tv_attention_mine:
                ActivityCollector.getActivityCollector().toOtherActivity(MyAttentionActivity.class);
                break;
            case R.id.tv_fans_mine:
                ActivityCollector.getActivityCollector().toOtherActivity(MyFansActivity.class);
                break;
            case R.id.iv_panit_mine:
                ActivityCollector.getActivityCollector().toOtherActivity(PersonDataActivity.class);
                break;
            case R.id.rl_mywallet_mine:
                ActivityCollector.getActivityCollector().toOtherActivity(WalletActivity.class);
                break;
            case R.id.rl_topup_real:
                ActivityCollector.getActivityCollector().toOtherActivity(RealnameActivity.class);
                break;
            case R.id.rl_topup_mine:
                ActivityCollector.getActivityCollector().toOtherActivity(TopupActivity.class);
                break;
            case R.id.rl_grade_mine:
                ActivityCollector.getActivityCollector().toOtherActivity(GradeActivity.class);
                break;
            case R.id.rl_store_mine:
                ActivityCollector.getActivityCollector().toOtherActivity(StoreActivity.class);
                break;
            case R.id.rl_myroom_mine:
                String roomId = (String) SharedPreferenceUtils.get(getContext(), Const.User.ROOMID, "");
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                break;
            case R.id.rl_setting_mine:
                ActivityCollector.getActivityCollector().toOtherActivity(SettingActivity.class);
                break;
        }
    }
}
