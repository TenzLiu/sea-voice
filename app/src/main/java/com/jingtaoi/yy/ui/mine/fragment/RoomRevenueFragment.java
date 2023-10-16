package com.jingtaoi.yy.ui.mine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.LotteryMyincomeBean;
import com.jingtaoi.yy.dialog.ExchangeDialogFragment;
import com.jingtaoi.yy.dialog.WithdrawDialogFragment;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RoomRevenueFragment extends MyBaseFragment {
    @BindView(R.id.left_title_tv)
    TextView left_title_tv;

    @BindView(R.id.right_title_tv)
    TextView right_title_tv;

    @BindView(R.id.left_fd_tv)
    TextView left_fd_tv;

    @BindView(R.id.right_fd_tv)
    TextView right_fd_tv;


    @BindView(R.id.jrls_tv)
    TextView jrls_tv;

    @BindView(R.id.bzls_tv)
    TextView bzls_tv;

    @BindView(R.id.bzsy_tv)
    TextView bzsy_tv;


    Unbinder unbinder;
    int type;


    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_room_revenue_fragment, container, false);
    }

    @Override
    public void initView() {

        initShow();

    }

    @Override
    public void onResume() {
        super.onResume();
        getCall();
    }

    private void initShow() {


        Bundle bundle = getArguments();
        type = bundle.getInt(Const.ShowIntent.TYPE);

        if (type==1)
        {
            left_title_tv.setText("个人收益(今日)");
            right_title_tv.setText("个人收益余额");
        }else {
            left_title_tv.setText("房间返点(今日)");
            right_title_tv.setText("房间返点余额");
        }


    }
    private String profitBalance="0";
    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type",type);
        HttpManager.getInstance().post(Api.LOTTERY_MYINCOME, map, new MyObserver(this) {



            @Override
            public void success(String responseString) {

                LotteryMyincomeBean storeListBean = JSON.parseObject(responseString, LotteryMyincomeBean.class);
                if (storeListBean.getCode() == Api.SUCCESS) {

                    if (storeListBean.getData()!=null){
                        profitBalance = storeListBean.getData().getProfitBalance();
                        left_fd_tv.setText(storeListBean.getData().getProfitDay());
                        right_fd_tv.setText(storeListBean.getData().getProfitBalance());
                        jrls_tv.setText(storeListBean.getData().getBillFlowDay());
                        bzls_tv.setText(storeListBean.getData().getBillFlowWeek());
                        bzsy_tv.setText(storeListBean.getData().getProfitWeek());
                    }
                } else {
                    showToast(storeListBean.getMsg());
                }
            }
        });
    }


    @OnClick({R.id.df_tv,R.id.tx_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tx_tv:
                WithdrawDialogFragment drawDialog = new WithdrawDialogFragment();
                drawDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                Bundle bundle = new Bundle();
                bundle.putInt("type",type);
                drawDialog.setArguments(bundle);
                drawDialog.setYE(profitBalance);
                drawDialog.setDiassDialogLiser(new WithdrawDialogFragment.DiassDialogLiser() {
                    @Override
                    public void diass() {
                        getCall();
                    }
                });
                drawDialog.show(getFragmentManager(), "drawDialog");
//                Bundle  bundle=new Bundle();
//                bundle.putInt("type",type);
//                ActivityCollector.getActivityCollector().toOtherActivity(DrawActivity.class,bundle);
                break;
            case R.id.df_tv:

                ExchangeDialogFragment exchangeDialog = new ExchangeDialogFragment();
                exchangeDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                exchangeDialog.setType(type);
                exchangeDialog.setYE(profitBalance);
                exchangeDialog.setDiassDialogLiser(new ExchangeDialogFragment.DiassDialogLiser() {
                    @Override
                    public void diass() {
                        getCall();
                    }
                });
                exchangeDialog.show(getFragmentManager(), "exchangeDialog");

//                ActivityCollector.getActivityCollector().toOtherActivity(ExchangeYouActivity.class);
                break;

        }
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
}
