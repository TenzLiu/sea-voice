package com.jingtaoi.yy.ui.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.ExchangeBean;
import com.jingtaoi.yy.bean.GetOneBean;
import com.jingtaoi.yy.bean.UserBean;
import com.jingtaoi.yy.dialog.MyChangeYouDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 兑换浪花
 */
public class ExchangeYouActivity extends MyBaseActivity {
    @BindView(R.id.edt_number_exchangeyou)
    EditText edtNumberExchangeyou;
    @BindView(R.id.tv_diamond_exchangeyou)
    TextView tvDiamondExchangeyou;
    @BindView(R.id.tv_you_exchangeyou)
    TextView tvYouExchangeyou;
    @BindView(R.id.btn_sure_exchangeyou)
    Button btnSureExchangeyou;
    @BindView(R.id.tv_show_exchangeyou)
    TextView tvShowExchangeyou;

    double Ynum;
    @BindView(R.id.tv_show1_exchangeyou)
    TextView tvShow1Exchangeyou;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_exchangeyou);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_exchangeyou);

        setShow();

        edtNumberExchangeyou.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = edtNumberExchangeyou.getText().toString();
                if (str.length() >= 1) {
                    if (str.equals("0")) {
                        str = "";
                        edtNumberExchangeyou.setText(str);
                        //设置新的光标所在位置
                        edtNumberExchangeyou.setSelection(str.length());
                        btnSureExchangeyou.setAlpha(0.5f);
                        btnSureExchangeyou.setEnabled(false);
                    } else if (str.endsWith("0")) {
                        tvShowExchangeyou.setText(str);
                        btnSureExchangeyou.setAlpha(1.0f);
                        btnSureExchangeyou.setEnabled(true);
                    } else {
                        tvShowExchangeyou.setText("");
                        btnSureExchangeyou.setAlpha(0.5f);
                        btnSureExchangeyou.setEnabled(false);
                    }
                    int endNumber = Integer.parseInt(str);
                    if (endNumber > Ynum) {
                        showToast("钻石数量少于兑换数量");
                    }
                }
            }
        });

        getSHowCall();
    }

    private void getSHowCall() {
//        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.UserConvertlist, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                tvShow1Exchangeyou.setText(getOneBean.getData().getStr());
            }
        });
    }


    @Override
    public void setResume() {

    }

    @SuppressLint("SetTextI18n")
    private void setShow() {
        int gold = (int) SharedPreferenceUtils.get(this, Const.User.GOLD, 0);
        String strYnum = (String) SharedPreferenceUtils.get(this, Const.User.Ynum, "0");
        Ynum = Double.parseDouble(strYnum);
        tvDiamondExchangeyou.setText(Ynum + "");
        tvYouExchangeyou.setText(gold + "");
    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sure_exchangeyou)
    public void onViewClicked() {
        String changeShow = edtNumberExchangeyou.getText().toString();
        if (changeShow.endsWith("0")) {
            updateCall(changeShow);
        } else {
            showToast("钻石数量必须是10的整数倍");
        }
    }

    private void updateCall(String changeShow) {
//        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("gold", changeShow);
        HttpManager.getInstance().post(Api.UserConvert, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                ExchangeBean baseBean = JSON.parseObject(responseString, ExchangeBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showMyShowDialog(baseBean);
                    getUserCall();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    MyChangeYouDialog myChangeYouDialog;

    private void showMyShowDialog(ExchangeBean baseBean) {
        if (myChangeYouDialog != null && myChangeYouDialog.isShowing()) {
            myChangeYouDialog.dismiss();
        }
        myChangeYouDialog = new MyChangeYouDialog(this, baseBean);
        myChangeYouDialog.show();
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
        SharedPreferenceUtils.put(this, Const.User.USER_TOKEN, dataBean.getId());
        SharedPreferenceUtils.put(this, Const.User.AGE, dataBean.getAge());
        SharedPreferenceUtils.put(this, Const.User.IMG, dataBean.getImgTx());
        SharedPreferenceUtils.put(this, Const.User.SEX, dataBean.getSex());
        SharedPreferenceUtils.put(this, Const.User.NICKNAME, dataBean.getNickname());
        SharedPreferenceUtils.put(this, Const.User.ROOMID, dataBean.getUsercoding());
        SharedPreferenceUtils.put(this, Const.User.CharmGrade, dataBean.getCharmGrade());
        SharedPreferenceUtils.put(this, Const.User.DATEOFBIRTH, dataBean.getDateOfBirth());
        SharedPreferenceUtils.put(this, Const.User.FansNum, dataBean.getFansNum());
        SharedPreferenceUtils.put(this, Const.User.AttentionNum, dataBean.getAttentionNum());
        SharedPreferenceUtils.put(this, Const.User.GOLD, dataBean.getGold());
        SharedPreferenceUtils.put(this, Const.User.GoldNum, dataBean.getGoldNum());
        SharedPreferenceUtils.put(this, Const.User.GRADE_T, dataBean.getTreasureGrade());
        SharedPreferenceUtils.put(this, Const.User.PHONE, dataBean.getPhone());
        SharedPreferenceUtils.put(this, Const.User.QQSID, dataBean.getQqSid());
        SharedPreferenceUtils.put(this, Const.User.WECHATSID, dataBean.getWxSid());
        SharedPreferenceUtils.put(this, Const.User.Ynum, dataBean.getYnum());
        SharedPreferenceUtils.put(this, Const.User.Yuml, dataBean.getYuml());
        SharedPreferenceUtils.put(this, Const.User.Yuml, dataBean.getYuml());
        SharedPreferenceUtils.put(this, Const.User.HEADWEAR_H, dataBean.getUserThfm());
        SharedPreferenceUtils.put(this, Const.User.CAR_H, dataBean.getUserZjfm());
        SharedPreferenceUtils.put(this, Const.User.HEADWEAR, dataBean.getUserTh());
        SharedPreferenceUtils.put(this, Const.User.CAR, dataBean.getUserZj());
        SharedPreferenceUtils.put(this, Const.User.SIGNER, dataBean.getIndividuation());
        SharedPreferenceUtils.put(this, Const.User.USER_LiANG, dataBean.getLiang());
        SharedPreferenceUtils.put(this, Const.User.PAY_PASS, dataBean.getPayPassword());
        SharedPreferenceUtils.put(this, Const.User.IS_AGENT_GIVE, dataBean.getIsAgentGive());
//        SharedPreferenceUtils.put(this, Const.User.USER_SIG, dataBean.getToken());
        setShow();
    }
}
