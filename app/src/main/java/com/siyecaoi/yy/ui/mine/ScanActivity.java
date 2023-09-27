package com.siyecaoi.yy.ui.mine;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 二维码页面
 * Created by Administrator on 2018/12/8.
 */

public class ScanActivity extends MyBaseActivity {
    @BindView(R.id.zxingview)
    ZXingView zxingview;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_scan);
    }

    @Override
    public void initView() {

        setTitleText(R.string.tv_scan_setting);
        zxingview.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        zxingview.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
        zxingview.setDelegate(delegate);
    }

    QRCodeView.Delegate delegate = new QRCodeView.Delegate() {
        @Override
        public void onScanQRCodeSuccess(String result) {
            LogUtils.e("二维码msg", result);
            if (result.startsWith(Const.bucketName)) {
                vibrate();
//                String orderNumber = result.substring(Const.bucketName.length());
//                Bundle bundle = new Bundle();
//                bundle.putString(Const.ShowIntent.ID, orderNumber);
//                ActivityCollector.getActivityCollector().toOtherActivity(SureActivity.class, bundle);
//                ActivityCollector.getActivityCollector().finishActivity();
            } else {
                showToast("无效的二维码，请重新扫描");
                zxingview.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
            }

        }

        @Override
        public void onCameraAmbientBrightnessChanged(boolean isDark) {
            // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
            String tipText = zxingview.getScanBoxView().getTipText();
            String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
            if (isDark) {
                if (!tipText.contains(ambientBrightnessTip)) {
                    zxingview.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
                }
            } else {
                if (tipText.contains(ambientBrightnessTip)) {
                    tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                    zxingview.getScanBoxView().setTipText(tipText);
                }
            }
        }

        @Override
        public void onScanQRCodeOpenCameraError() {
            Log.e(TAG, "打开相机出错");
            showToast("请在应用权限页面开启相机权限");
            ActivityCollector.getActivityCollector().finishActivity(ScanActivity.class);
        }
    };

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onStart() {
        super.onStart();

        zxingview.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        zxingview.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
    }

    @Override
    protected void onStop() {
        zxingview.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zxingview.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
