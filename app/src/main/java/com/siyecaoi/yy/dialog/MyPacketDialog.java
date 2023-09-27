package com.siyecaoi.yy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.siyecaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 领取红包弹窗
 * Created by Administrator on 2018/3/9.
 */

public class MyPacketDialog extends Dialog {


    @BindView(R.id.iv_close_packet)
    ImageView ivClosePacket;
    @BindView(R.id.iv_header_packet)
    SimpleDraweeView ivHeaderPacket;
    @BindView(R.id.tv_show_packet)
    TextView tvShowPacket;
    @BindView(R.id.iv_open_packet)
    ImageView ivOpenPacket;
    @BindView(R.id.ll_packet)
    LinearLayout llPacket;
    @BindView(R.id.rl_packet)
    RelativeLayout rlPacket;
    int packetId;
    int userId;
    int id;
    Context context;
    String imgShow, nickName;
    int packetNumber;//红包个数

    public MyPacketDialog(Context context, int id, int packetId, int userId, String img, String nickName,int packetNumber) {
        super(context, R.style.CustomDialogStyle);
        this.packetId = packetId;
        this.id = id;
        this.userId = userId;
        this.context = context;
        this.imgShow = img;
        this.nickName = nickName;
        this.packetNumber = packetNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);  // 是否可以撤销
        setContentView(R.layout.dialog_packet);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        initShow();
    }

    private void initShow() {
        ImageUtils.loadUri(ivHeaderPacket, imgShow);
        if (packetNumber==1){
            tvShowPacket.setText(nickName + "给您发了一个红包");
        }else {
            tvShowPacket.setText(nickName + "发了手气红包");
        }

    }

    @OnClick({R.id.iv_close_packet, R.id.iv_open_packet, R.id.rl_packet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_packet:
                dismiss();
                break;
            case R.id.iv_open_packet:
//                getPacketCall();
                break;
            case R.id.rl_packet:
                dismiss();
                break;
        }
    }

    public void setOpenOnClicker(View.OnClickListener onClicker) {
        ivOpenPacket.setOnClickListener(onClicker);
    }

//    private void getPacketCall() {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        map.put("id", id);
//        HttpManager.getInstance().post(Api.GetRed, map, new MyObserver(context) {
//            @Override
//            public void success(String responseString) {
//                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
//                if (baseBean.getCode() == Api.SUCCESS) {
//                    dismiss();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(Const.ShowIntent.DATA, packetId);
//                    ActivityCollector.getActivityCollector().toOtherActivity(PacketActivity.class, bundle);
//                } else {
//                    showToast(baseBean.getMsg());
//                }
//            }
//        });
//    }
}