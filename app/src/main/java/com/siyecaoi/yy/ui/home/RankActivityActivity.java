package com.siyecaoi.yy.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.ActRankBean;
import com.siyecaoi.yy.bean.DoubleBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import java.util.HashMap;
import java.util.List;

public class RankActivityActivity extends MyBaseActivity {
    private int[] res = {R.drawable.host_1,R.drawable.host_2,R.drawable.host_3,R.drawable.host_4,R.drawable.host_5,
            R.drawable.host_6,R.drawable.host_7,R.drawable.host_8,R.drawable.host_9,R.drawable.host_10};

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_rank_activity);
    }

    @Override
    public void initView() {
        setTitleText("榜单活动");
        getTotal();
        getRank();
    }

    private void getRank() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.getCarveUpPool, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                ActRankBean getOneBean = JSON.parseObject(responseString, ActRankBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    LinearLayout ll = findViewById(R.id.ll_rank);
                    List<ActRankBean.DataBean> dataBeans;
                    if (getOneBean.data.size()>10){
                        dataBeans = getOneBean.data.subList(0, 10);
                    }else dataBeans = getOneBean.data;
                    for (int i = 0;i<dataBeans.size();i++){
                        ActRankBean.DataBean dataBean = dataBeans.get(i);
                        View inflate = getLayoutInflater().inflate(R.layout.item_rank_act, null);
                        ((ImageView)inflate.findViewById(R.id.iv_rank)).setImageResource(res[i]);
                        ((SimpleDraweeView)inflate.findViewById(R.id.iv_head)).setImageURI(dataBean.imgTx);
                        ((TextView)inflate.findViewById(R.id.tv_name)).setText(dataBean.nickname);
                        TextView tvCode = inflate.findViewById(R.id.tv_code);
                        tvCode.setText(dataBean.usercoding);
                        tvCode.setCompoundDrawablesWithIntrinsicBounds(dataBean.isliang == 1?R.drawable.liang_id:0,0,0,0);
                        ((TextView)inflate.findViewById(R.id.tv_gold)).setText("金币"+(int)(dataBean.consumeYbNum));
                        ll.addView(inflate);
                    }
                } else {
                    showToast(getOneBean.getMsg());
                }
            }
        });
    }

    private void getTotal() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.getCarveUpPoolSize, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                DoubleBean getOneBean = JSON.parseObject(responseString, DoubleBean.class);
                if (getOneBean.code == Api.SUCCESS) {
                    int data = (int) getOneBean.data;
                    TextView total = findViewById(R.id.tv_total);
                    total.setText(data+"");
                } else {
                    showToast(getOneBean.msg);
                }
            }
        });
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }
}
