package com.jingtaoi.yy.ui.room.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.OnlineUserBean;
import com.jingtaoi.yy.bean.PersonalHomeBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 其他位置用户弹窗
 */
public class OtherShowDialog extends Dialog {
    private boolean isUserOpration = false;//是否点击自己
    @BindView(R.id.iv_user_header)
    SimpleDraweeView iv_user_header;


    @BindView(R.id.user_name_tv)
    TextView user_name_tv;

    @BindView(R.id.liang_iv)
    ImageView liang_iv;
    @BindView(R.id.user_id_tv)
    TextView user_id_tv;

    @BindView(R.id.daichong_tv)
    TextView daichong_tv;

    @BindView(R.id.zilaio_ll)
    LinearLayout zilaio_ll;

    @BindView(R.id.liwu_ll)
    LinearLayout liwu_ll;

    @BindView(R.id.guanli_ll)
    LinearLayout guanli_ll;


    @BindView(R.id.sixi_ll)
    LinearLayout sixi_ll;


    @BindView(R.id.xiamai_tv)
    TextView xiamai_tv;

    @BindView(R.id.jinmai_tv)
    TextView jinmai_tv;

    @BindView(R.id.lahei_tv)
    TextView lahei_tv;


    @BindView(R.id.tichu_tv)
    TextView tichu_tv;

    @BindView(R.id.guanli_tv)
    TextView guanli_tv;


    @BindView(R.id.caozuo_ll)
    LinearLayout caozuo_ll;

    @BindView(R.id.item_list)
    LinearLayout item_list;


    @BindView(R.id.user_cao_zuo_ll)
    LinearLayout user_cao_zuo_ll;

    @BindView(R.id.ziliao_tv)
    TextView ziliao_tv;

    @BindView(R.id.meili_tv)
    TextView meili_tv;

    @BindView(R.id.xmpt_tv)
    TextView xmpt_tv;
    @BindView(R.id.iv_grade_person)
    ImageView ivGradePerson;
    @BindView(R.id.iv_gradetwo_person)
    ImageView ivGradetwoPerson;
    @BindView(R.id.tv_follow)
    TextView tv_follow;
    @BindView(R.id.tv_sign)
    TextView tv_sign;

    private Context context;
    private ArrayList<String> bottomList;
    private OnItemClickListener listener;
    private int userId;
    private int otherId;
    OnlineUserBean.DataBean.UserBean user;

    public OtherShowDialog(Context context, ArrayList<String> bottomList, int userId, int otherId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.bottomList = bottomList;
        this.userId = userId;
        this.otherId = otherId;
        if (userId == otherId) {
            this.isUserOpration = true;
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_dialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        getCall();
        getSignCall();

        meili_tv.setVisibility(View.GONE);
        setListListener();


        if (isUserOpration) {
            sixi_ll.setVisibility(View.GONE);
        }

        if (otherId == userId){ //自己
            tv_follow.setVisibility(View.GONE);
        }

        int isChong = (int) SharedPreferenceUtils.get(context, Const.User.IS_AGENT_GIVE, 0);
        if (isChong == 2) {
            int  uuId= (int) SharedPreferenceUtils.get(context, Const.User.USER_TOKEN, 0);
            if (uuId==otherId){
                daichong_tv.setVisibility(View.GONE);
                return;
            }

            daichong_tv.setVisibility(View.VISIBLE);
            daichong_tv.setOnClickListener(v -> {
                dismiss();
                if (listener != null)
                    listener.onItemClick(7, user);
            });
        } else {
            daichong_tv.setVisibility(View.GONE);
        }

        iv_user_header.setOnClickListener(v->{
            if (listener != null)
                listener.onItemClick(0,user);
        });

        tv_follow.setOnClickListener(v->{
            getAttentionCall();
        });
    }


    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("buid", otherId);
        HttpManager.getInstance().post(Api.UserAttention, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                OnlineUserBean onlineUserBean = JSON.parseObject(responseString, OnlineUserBean.class);
                if (onlineUserBean.getCode() == Api.SUCCESS) {
                    setShow(onlineUserBean.getData().getUser());
                    int status = onlineUserBean.getData().getState();
                    tv_follow.setText(status == 1?"+关注":"已关注");
                }
            }
        });
    }

    private void getSignCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", otherId);
        HttpManager.getInstance().post(Api.PersonageUser, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                PersonalHomeBean personalHomeBean = JSON.parseObject(responseString, PersonalHomeBean.class);
                if (personalHomeBean.getCode() == Api.SUCCESS) {
                    String individuation = personalHomeBean.getData().getUser().getIndividuation();
                    tv_sign.setText(individuation.isEmpty()?"这个人很神秘，还没有签名。":individuation);
                    ivGradePerson.setImageResource(ImageShowUtils.getGrade(personalHomeBean.getData().getUser().getTreasureGrade()));
                    ivGradetwoPerson.setImageResource(ImageShowUtils.getCharm(personalHomeBean.getData().getUser().getCharmGrade()));
                }
            }
        });
    }

    private void getAttentionCall() {
        String s = tv_follow.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("buid", otherId);
        map.put("type", s.equals("已关注")?2:1);
        HttpManager.getInstance().post(Api.addAttention, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("操作成功");
                    getCall();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setShow(OnlineUserBean.DataBean.UserBean user) {
        this.user = user;
        //头像
        ImageUtils.loadUri(iv_user_header, user.getImg());
        //名字
        user_name_tv.setText(user.getName());
        user_name_tv.setCompoundDrawablesWithIntrinsicBounds(0,0,
                user.getSex() == 1?R.drawable.sex_male_small:R.drawable.sex_female_small,0);
        //id
        if (TextUtils.isEmpty(user.getLiang())) {
            liang_iv.setVisibility(View.GONE);
            user_id_tv.setText("ID：" + user.getUsercoding());
        } else {
            liang_iv.setVisibility(View.VISIBLE);
            user_id_tv.setText("ID：" + user.getLiang());
        }

        //财富等级显示
//        ivGradePerson.setBackgroundResource(ImageShowUtils.getGrade(user.getTreasureGrade()));
//        ivGradePerson.setText(ImageShowUtils.getGradeText(user.getTreasureGrade()));
        ivGradePerson.setImageResource(ImageShowUtils.getGrade(user.getTreasureGrade()));

        //魅力等级显示
//        ivGradetwoPerson.setBackgroundResource(ImageShowUtils.getGrade(user.getCharmGrade()));
//        ivGradetwoPerson.setText(ImageShowUtils.getCharmText(user.getCharmGrade()));
        ivGradetwoPerson.setImageResource(ImageShowUtils.getCharm(user.getCharmGrade()));
    }


    private void setListListener() {
        for (int i = 0; i < bottomList.size(); i++) {

            if (bottomList.get(i).equals("查看资料")) {
//                zilaio_ll.setVisibility(View.VISIBLE);
//                zilaio_ll.setOnClickListener(v -> {
//                    dismiss();
//                    if (listener != null)
//                        listener.onItemClick(0, user);
//
//                });

            } else if (bottomList.get(i).contains("礼物")) {
                liwu_ll.setVisibility(View.VISIBLE);
                liwu_ll.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(6, user);
                });

            } else if (bottomList.get(i).equals(context.getString(R.string.tv_sixin))) {
                sixi_ll.setVisibility(View.VISIBLE);
                sixi_ll.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(8, user);
                });
            } else if (bottomList.get(i).contains("下麦")) {
                xiamai_tv.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(1, user);
                });


            } else if (bottomList.get(i).equals("禁麦此座位")) {
                caozuo_ll.setVisibility(View.VISIBLE);
                jinmai_tv.setText("禁麦");
                jinmai_tv.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(2, user);
                });


            } else if (bottomList.get(i).equals("解禁此座位")) {
                caozuo_ll.setVisibility(View.VISIBLE);
                jinmai_tv.setText("解麦");
                jinmai_tv.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(2, user);
                });


            } else if (bottomList.get(i).equals(context.getString(R.string.tv_addblacklist_bottomshow))) {
                lahei_tv.setText(bottomList.get(i));
                lahei_tv.setVisibility(View.VISIBLE);
                lahei_tv.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(5, user);
                });


            } else if (bottomList.get(i).equals(context.getString(R.string.tv_outhome_bottomshow))) {
                caozuo_ll.setVisibility(View.VISIBLE);
                tichu_tv.setVisibility(View.VISIBLE);
                tichu_tv.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(4, user);
                });


            } else if (bottomList.get(i).equals("浪花")) {
                daichong_tv.setOnClickListener(v -> {
                    dismiss();
                    if (listener != null)
                        listener.onItemClick(7, user);
                });

            }
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }


    public interface OnItemClickListener {
        void onItemClick(int position, OnlineUserBean.DataBean.UserBean user);
    }

}