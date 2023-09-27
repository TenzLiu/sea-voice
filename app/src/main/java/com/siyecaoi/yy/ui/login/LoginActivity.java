package com.siyecaoi.yy.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.siyecaoi.yy.ui.other.WebActivity;
import com.tencent.imsdk.TIMManager;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.UserBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.MainActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sinata.xldutils.utils.StringUtils;

/**
 * Created by Administrator on 2018/12/19.
 */

public class LoginActivity extends MyBaseActivity {
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.layou_bottom)
    LinearLayout layouBottom;
    @BindView(R.id.img_qq_loging)
    ImageView imgQqLoging;
    @BindView(R.id.img_wx_loging)
    ImageView imgWxLoging;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.layout_login)
    LinearLayout layoutLogin;

    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    String cityName;


    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView() {
        showTitle(false);
        showHeader(false);


        ActivityCollector.getActivityCollector().toOtherActivity(PhoneLoginActivity.class);
        finish();
//        umShareAPI = UMShareAPI.get(this);
//        initLocation();
    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = null;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高
        // 的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，
        // 反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }


    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    LogUtils.e("定位", aMapLocation.getPoiName() + "  " + aMapLocation.getCity());
                    String pointAddress = aMapLocation.getPoiName();
                    if (!StringUtils.isEmpty(pointAddress)) {
                        cityName = aMapLocation.getCity();//城市名称
                        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                    }
                }
            }
        }
    };

    @Override
    public void setResume() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.tv_agreement, R.id.img_qq_loging, R.id.img_wx_loging, R.id.iv_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement:
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.TYPE, 9);
                bundle.putString(Const.ShowIntent.TITLE, getString(R.string.hint_checktwo_register));
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                break;
            case R.id.img_qq_loging:
                break;
            case R.id.img_wx_loging:
                break;
            case R.id.iv_phone:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Const.ShowIntent.NAME, cityName);
                ActivityCollector.getActivityCollector().toOtherActivity(PhoneLoginActivity.class, bundle1);
                break;
        }
    }


    /**
     * 在收到服务器颁发的 userSig 后，调用IMSDK的 login 接口
     * userId 用户账号
     * userSig 您服务器给这个用户账号颁发的 IMSDk 鉴权认证
     */
    private void onRecvUserSig(String userId, String userSig) {
        showDialog("登录中");
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM 登录成功后的回调操作，一般为跳转到应用的主页（这里的主页内容为下面章节的会话列表）
                 */
                LogUtils.e(LogUtils.TAG, "登录腾讯云成功");
                showToast(getString(R.string.tv_login_success));
                LogUtils.e("getVersion", TIMManager.getInstance().getVersion());
                toMain();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                showToast("腾讯云登录失败，您将无法使用私聊功能");
                LogUtils.e(LogUtils.TAG, errCode + "登录腾讯云失败" + errMsg);
                toMain();
            }
        });
    }


    private void toMain() {
        ActivityCollector.getActivityCollector().toOtherActivity(MainActivity.class);
        ActivityCollector.getActivityCollector().finishActivity();
    }

    private void initShared(UserBean.DataBean dataBean) {
        SharedPreferenceUtils.put(this, Const.User.USER_TOKEN, dataBean.getId());
        SharedPreferenceUtils.put(this, Const.User.APP_TOKEN, dataBean.getApptokenid());
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
        SharedPreferenceUtils.put(this, Const.User.USER_SIG, dataBean.getToken());
        SharedPreferenceUtils.put(this, Const.User.HEADWEAR_H, dataBean.getUserThfm());
        SharedPreferenceUtils.put(this, Const.User.CAR_H, dataBean.getUserZjfm());
        SharedPreferenceUtils.put(this, Const.User.HEADWEAR, dataBean.getUserTh());
        SharedPreferenceUtils.put(this, Const.User.CAR, dataBean.getUserZj());
        SharedPreferenceUtils.put(this, Const.User.SIGNER, dataBean.getIndividuation());
        //设置别名（新的调用会覆盖之前的设置）
        JPushInterface.setAlias(this, 0, String.valueOf(dataBean.getId()));
    }
}
