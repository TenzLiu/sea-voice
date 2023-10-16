package com.jingtaoi.yy.dialog;

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
import com.jingtaoi.yy.ui.message.OtherHomeActivity;
import com.jingtaoi.yy.ui.mine.PersonHomeActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageShowUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.OnlineUserBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ImageUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.StringUtils;
import cn.sinata.xldutils.utils.Toast;


/**
 * 个人资料弹窗页面
 * Created by Administrator on 2018/3/9.
 */

public class MyBottomPersonDialog extends Dialog {

    Context context;
    @BindView(R.id.iv_close_person)
    ImageView ivClosePerson;
    @BindView(R.id.iv_grade_person)
    TextView ivGradePerson;
    @BindView(R.id.iv_gradetwo_person)
    TextView ivGradetwoPerson;
    @BindView(R.id.tv_name_person)
    TextView tvNamePerson;
    @BindView(R.id.ll_name_person)
    LinearLayout llNamePerson;
    @BindView(R.id.tv_id_person)
    TextView tvIdPerson;
    @BindView(R.id.tv_sign_person)
    TextView tvSignPerson;
    @BindView(R.id.ll_id_person)
    LinearLayout llIdPerson;
    @BindView(R.id.tv_fans_person)
    TextView tvFansPerson;
    @BindView(R.id.btn_data_person)
    Button btnDataPerson;
    @BindView(R.id.btn_attention_person)
    Button btnAttentionPerson;
    @BindView(R.id.iv_show_person)
    SimpleDraweeView ivShowPerson;
    @BindView(R.id.iv_sex_person)
    ImageView ivSexPerson;
    @BindView(R.id.view_center_person)
    View viewCenterPerson;
    @BindView(R.id.liang_iv)
    ImageView liang_iv;

    int userId;//当前用户id
    int otherId;//资料用户id

    int status;


    public MyBottomPersonDialog(Context context, int userId, int otherId) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        this.userId = userId;
        this.otherId = otherId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_person);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);

        getCall();
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
                    status = onlineUserBean.getData().getState();
                    setShow(onlineUserBean.getData().getUser());
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setShow(OnlineUserBean.DataBean.UserBean userBean) {
        ImageUtils.loadUri(ivShowPerson, userBean.getImg());
        if (userBean.getSex() == 1) {
            ivSexPerson.setSelected(true);
        } else if (userBean.getSex() == 2) {
            ivSexPerson.setSelected(false);
        }
       //财富等级显示
        ivGradePerson.setBackgroundResource(ImageShowUtils.getGrade(userBean.getTreasureGrade()));
        ivGradePerson.setText(ImageShowUtils.getGradeText(userBean.getTreasureGrade()));

        //魅力等级显示
        ivGradetwoPerson.setBackgroundResource(ImageShowUtils.getGrade(userBean.getCharmGrade()));
        ivGradetwoPerson.setText(ImageShowUtils.getCharmText(userBean.getCharmGrade()));

        tvNamePerson.setText(userBean.getName());
        if (StringUtils.isEmpty(userBean.getLiang())) {
            tvSignPerson.setText(userBean.getUsercoding() + "   " + userBean.getConstellation());
            liang_iv.setVisibility(View.GONE);
        } else {
            tvSignPerson.setText(userBean.getLiang() + "   " + userBean.getConstellation());
            liang_iv.setVisibility(View.VISIBLE);
        }

        tvFansPerson.setText(context.getString(R.string.tv_fans) + userBean.getNum());
        if (userId == otherId) {
            viewCenterPerson.setVisibility(View.GONE);
            btnAttentionPerson.setVisibility(View.GONE);
        }
        if (status == 1) { //1是未关注，2是已关注
            btnAttentionPerson.setText(R.string.tv_attention_person);
        } else {
            btnAttentionPerson.setText(R.string.tv_attentioned_person);
        }
    }


    @OnClick({R.id.iv_close_person, R.id.btn_data_person, R.id.btn_attention_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_person:
                dismiss();
                break;
            case R.id.btn_data_person:
                dismiss();
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.ID, otherId);
                if (otherId == userId) {
                    ActivityCollector.getActivityCollector().toOtherActivity(PersonHomeActivity.class, bundle);
                } else {
                    ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
                }
                break;
            case R.id.btn_attention_person:
                getAttentionCall();
                break;
        }
    }

    private void getAttentionCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        map.put("buid", otherId);
        map.put("type", status);
        HttpManager.getInstance().post(Api.addAttention, map, new MyObserver(context) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (status == 1) {
                        status = 2;
                        btnAttentionPerson.setText(R.string.tv_attentioned_person);
                        Toast.create(context).show("关注成功");
                    } else if (status == 2) {
                        status = 1;
                        btnAttentionPerson.setText(R.string.tv_attention_person);
                        Toast.create(context).show("取消关注成功");
                    }
                } else {
                    Toast.create(context).show(baseBean.getMsg());
                }
            }
        });
    }
}