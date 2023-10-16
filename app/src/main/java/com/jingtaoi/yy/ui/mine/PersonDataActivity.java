package com.jingtaoi.yy.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.UserBean;
import com.jingtaoi.yy.dialog.MyBirthdayDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.other.MyCropImageActivity;
import com.jingtaoi.yy.ui.other.SelectPhotoActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.MyUtils;
import com.jingtaoi.yy.utils.OSSUtil;
import com.jingtaoi.yy.utils.ObsUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.activitys.SelectPhotoDialog;
import cn.sinata.xldutils.utils.StringUtils;
import io.reactivex.functions.Consumer;

/**
 * 个人资料
 */
public class PersonDataActivity extends MyBaseActivity {
    @BindView(R.id.iv_header_data)
    SimpleDraweeView ivHeaderData;
    @BindView(R.id.ll_header_data)
    LinearLayout llHeaderData;
    @BindView(R.id.tv_nickname_data)
    TextView tvNicknameData;
    @BindView(R.id.ll_nickname_data)
    LinearLayout llNicknameData;
    @BindView(R.id.tv_sex_data)
    TextView tvSexData;
    @BindView(R.id.ll_sex_data)
    LinearLayout llSexData;
    @BindView(R.id.tv_birthday_data)
    TextView tvBirthdayData;
    @BindView(R.id.ll_bithday_data)
    LinearLayout llBithdayData;
    @BindView(R.id.tv_signer_data)
    TextView tvSignerData;


    String birthday;
    String nickName;
    String headerImg;
    String signer;
    int sex;//性别(1 男, 2 女)
    Calendar calendar;//生日
    @BindView(R.id.ll_signer_data)
    LinearLayout llSignerData;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_persondata);
    }

    @Override
    public void initView() {

        setTitleText(R.string.title_data);
        initShow();
        showLine(false);
    }

    private void initShow() {
        headerImg = (String) SharedPreferenceUtils.get(this, Const.User.IMG, "");
        birthday = (String) SharedPreferenceUtils.get(this, Const.User.DATEOFBIRTH, "");
        nickName = (String) SharedPreferenceUtils.get(this, Const.User.NICKNAME, "");
        signer = (String) SharedPreferenceUtils.get(this, Const.User.SIGNER, "");
        sex = (int) SharedPreferenceUtils.get(this, Const.User.SEX, 0);

        tvNicknameData.setText(nickName);
        ImageUtils.loadUri(ivHeaderData, headerImg);
        tvBirthdayData.setText(birthday);
        tvSignerData.setText(signer);
        if (sex == 1) {
            tvSexData.setText(R.string.tv_man);
        } else if (sex == 2) {
            tvSexData.setText(R.string.tv_woman);
        }


    }

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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Const.RequestCode.SELECTPHOTO_CODE:
                    if (data != null) {
                        String filePath = data.getStringExtra(SelectPhotoDialog.DATA);
                        cropImage(filePath);
                    }
                    break;
                case Const.RequestCode.CROPTPHOTO_CODE:
                    if (data != null) {
                        String filePath = data.getStringExtra("path");
                        updateImgCallObs(filePath);
                    }
                    break;
                case 101:
                    nickName = (String) SharedPreferenceUtils.get(this, Const.User.NICKNAME, "");
                    tvNicknameData.setText(nickName);
                    setResult(RESULT_OK);
                    break;
                case 102:
                    signer = (String) SharedPreferenceUtils.get(this, Const.User.SIGNER, "");
                    tvSignerData.setText(signer);
                    setResult(RESULT_OK);
                    break;
            }
        }
    }

    private void cropImage(String path) {
        Intent intent = new Intent(this, MyCropImageActivity.class);
        intent.putExtra("uri", path);
        intent.putExtra("mode", 1);
        startActivityForResult(intent, Const.RequestCode.CROPTPHOTO_CODE);
    }

    private void updateImgCallObs(String filePath) {
        showDialog();
        ObsUtils obsUtils = new ObsUtils();
        obsUtils.uploadFile(filePath, new ObsUtils.ObsCallback() {
            @Override
            public void onUploadFileSuccess(String url) {
                dismissDialog();
                headerImg = url;
                ImageUtils.loadUri(ivHeaderData, headerImg);
                getUpdateCall();
            }

            @Override
            public void onUploadMoreFielSuccess() {

            }

            @Override
            public void onFail(String message) {
                dismissDialog();
                LogUtils.e("obs "+message);
                showToast("上传失败");
            }
        });
    }

    private void updateImgCall(String filePath) {
        showDialog();
        OSSUtil ossUtil = new OSSUtil(this);
        ossUtil.uploadSingle(filePath, new OSSUtil.OSSUploadCallBack() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                LogUtils.e(TAG, "onFinish: " + url);
                dismissDialog();
                headerImg = url;
                ImageUtils.loadUri(ivHeaderData, headerImg);
                getUpdateCall();
            }

            @Override
            public void onFial(String message) {
                super.onFial(message);
                LogUtils.e(TAG, "onFial: " + message);
                dismissDialog();
                showToast("上传失败");
            }
        });
    }

    private void getUpdateCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", userToken);
        map.put("imgTx", headerImg);
        map.put("dateOfBirth", birthday);
        HttpManager.getInstance().post(Api.updateUser, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    showToast(getString(R.string.tv_success_data));
                    initShared();
                } else {
                    showToast(userBean.getMsg());
                }
            }
        });
    }

    private void initShared() {
        SharedPreferenceUtils.put(this, Const.User.IMG, headerImg);
        SharedPreferenceUtils.put(this, Const.User.DATEOFBIRTH, birthday);
        setResult(RESULT_OK);
    }

    @OnClick({R.id.ll_header_data, R.id.ll_nickname_data, R.id.ll_sex_data, R.id.ll_bithday_data, R.id.ll_signer_data})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_header_data:
                openSelectPhoto();
                break;
            case R.id.ll_nickname_data:
                bundle.putString(Const.ShowIntent.NAME, nickName);
                ActivityCollector.getActivityCollector().toOtherActivity(NicknameActivity.class,bundle, 101);
                break;
            case R.id.ll_sex_data:
                break;
            case R.id.ll_bithday_data:
                showBirthdayDialog();
                break;
            case R.id.ll_signer_data:
                bundle.putString(Const.ShowIntent.DATA, signer);
                ActivityCollector.getActivityCollector().toOtherActivity(SingerActivity.class, bundle, 102);
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void openSelectPhoto() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ActivityCollector.getActivityCollector()
                                    .toOtherActivity(SelectPhotoActivity.class, Const.RequestCode.SELECTPHOTO_CODE);
                        } else {
                            showToast("请在应用权限页面开启相机权限");
                        }
                    }
                });
    }

    /**
     * 选择生日
     */
    private void showBirthdayDialog() {
        long timeBir;
        if (StringUtils.isEmpty(birthday)) {
            timeBir = System.currentTimeMillis();
        } else {
            timeBir = MyUtils.getInstans().getLongTime(birthday, "yyyy-MM-dd");
        }
        final MyBirthdayDialog myBirthdayDialog = new MyBirthdayDialog(this, timeBir);
        myBirthdayDialog.show();
        myBirthdayDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = myBirthdayDialog.getDate();
                showBirthday(calendar);
                myBirthdayDialog.dismiss();
            }
        });

    }

    private void showBirthday(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        birthday = year + "-" +
                MyUtils.getInstans().formatTime(monthOfYear + 1) +
                "-" +
                MyUtils.getInstans().formatTime(dayOfMonth);
        tvBirthdayData.setText(birthday);
        getUpdateCall();
    }
}
