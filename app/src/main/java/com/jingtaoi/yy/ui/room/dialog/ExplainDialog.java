package com.jingtaoi.yy.ui.room.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.bean.UserJackpotExpenseTotalBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 玩法说明
 */
public class ExplainDialog extends Dialog {


    Context mContext;

    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.tv_consumptionToday)
    TextView tvConsumptionToday;
    @BindView(R.id.tv_outputToday)
    TextView tvOutputToday;
    @BindView(R.id.tv_blastRateToday)
    TextView tvBlastRateToday;
    @BindView(R.id.ll)
    LinearLayout ll;

    private int type;

    public ExplainDialog(Context context, int type) {
        super(context, R.style.CustomDialogStyle);
        this.mContext = context;
        this.type = type;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_explain);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (ScreenUtil.getScreenWidth(mContext));
        lp.height = (int) (ScreenUtil.getScreenHeight(mContext) * 0.55);
        getWindow().setAttributes(lp);
        iv_close.setOnClickListener(view -> {
            dismiss();
        });
        if(type == 22){
            ll.setVisibility(View.GONE);
        }

        getCall();
    }

    private void getCall() {
        int userToken = (int) SharedPreferenceUtils.get(getContext(), Const.User.USER_TOKEN, 0);

        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", type);
        HttpManager.getInstance().post(Api.getUserJackpotExpenseTotal, map, new MyObserver(getContext()) {
            @Override
            public void success(String responseString) {
                UserJackpotExpenseTotalBean userJackpotExpenseTotalBean = JSON.parseObject(responseString, UserJackpotExpenseTotalBean.class);
                if (userJackpotExpenseTotalBean.getCode() == 0) {
                    if (userJackpotExpenseTotalBean.getData() != null) {
                        refreshUI(userJackpotExpenseTotalBean.getData());
                    } else {
                        tvConsumptionToday.setText("今日消耗： 0");
                        tvOutputToday.setText("今日产出： 0");
                        tvBlastRateToday.setText("今日爆率： 0.00%");
                    }
                }
            }
        });

    }

    private void refreshUI(UserJackpotExpenseTotalBean.UserJackpotExpenseTotal data) {
        tvConsumptionToday.setText("今日消耗： " + data.getExpenseMoney());
        tvOutputToday.setText("今日产出： " + data.getWinMoney());
        //产出/消耗*100 %
        double result = (double) data.getWinMoney() * 100 / data.getExpenseMoney();
        tvBlastRateToday.setText("今日爆率： " + String.format("%.2f", result) + "%");
    }

}
