package com.jingtaoi.yy.ui.room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.ObsUtils;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.utils.Toast;
import cn.sinata.xldutils.utils.Utils;
import cn.sinata.xldutils.xldUtils;
import io.reactivex.functions.Consumer;

/**
 * 背景设置
 *
 * @author xha
 * @data 2019/10/10
 */
public class BackSetActivity extends MyBaseActivity {
    @BindView(R.id.rl_choose_backset)
    RelativeLayout rlChooseBackset;
    @BindView(R.id.rl_choose1_backset)
    RelativeLayout rlChoose1Backset;
    @BindView(R.id.rl_choose2_backset)
    RelativeLayout rlChoose2Backset;
    String roomBackImg;

    String roomId;
    private File tempFile;//临时文件
    public static final String DATA = "path";

    @Override
    public void initData() {
        roomBackImg = getBundleString(Const.ShowIntent.IMG);
        roomId = getBundleString(Const.ShowIntent.ROOMID);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_backset);
    }

    @Override
    public void initView() {
        setTitleText(R.string.title_backset);
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
                case 101:
                    if (data != null) {
                        String roomBackName = data.getStringExtra(Const.ShowIntent.NAME);
                        Intent intent = new Intent();
                        intent.putExtra(Const.ShowIntent.NAME, roomBackName);
                        setResult(RESULT_OK, intent);
                    }
                    break;
                case 0:
                    if (tempFile != null && tempFile.exists()) {
                        showDialog();
                        updateImgCallObs(tempFile.getAbsolutePath());
                    }

                    break;

                case 1:
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            String path = Utils.getUrlPath(this, uri);
                            if (path != null) {
                                int typeIndex = path.lastIndexOf(".");
                                if (typeIndex != -1) {
                                    String fileType = path.substring(typeIndex + 1).toLowerCase(Locale.CHINA);
                                    //某些设备选择图片是可以选择一些非图片的文件。然后发送出去或出错。这里简单的通过匹配后缀名来判断是否是图片文件
                                    //如果是图片文件则发送。反之给出提示
                                    if (fileType.equals("jpg") || fileType.equals("gif")
                                            || fileType.equals("png") || fileType.equals("jpeg")
                                            || fileType.equals("bmp") || fileType.equals("wbmp")
                                            || fileType.equals("ico") || fileType.equals("jpe")) {
                                        updateImgCallObs(path);
                                        overridePendingTransition(0, 0);
                                    } else {
                                        Toast.create(this).show("无法识别的图片类型！");
                                    }
                                } else {
                                    Toast.create(this).show("无法识别的图片类型！");
                                }
                            } else {
                                Toast.create(this).show("无法识别的图片类型或路径！");
                            }
                        } else {
                            Toast.create(this).show("无法识别的图片类型！");
                        }
                    }
                    break;
            }

        }
    }

    private void updateImgCallObs(String filePath) {
        showDialog("图片上传中...");
        ObsUtils obsUtils = new ObsUtils();
        obsUtils.uploadFile(filePath, new ObsUtils.ObsCallback() {
            @Override
            public void onUploadFileSuccess(String url) {
                LogUtils.e(TAG, "onFinish: " + url);
                getUpdateImgCall(url);
            }

            @Override
            public void onUploadMoreFielSuccess() {

            }

            @Override
            public void onFail(String message) {
                dismissDialog();
                LogUtils.e("obs " + message);
                showToast("上传失败");
            }
        });
    }

    private void getUpdateImgCall(String url) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", userToken);
        map.put("bjImg", url);
        map.put("bjName", "自定义");
        HttpManager.getInstance().post(Api.getUpRoom, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = new Gson().fromJson(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("设置背景图片成功");
                    Intent intent = new Intent();
                    intent.putExtra(Const.ShowIntent.NAME, "自定义");
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showToast(baseBean.getMsg());
                }

            }
        });
    }


    @OnClick({R.id.rl_choose_backset, R.id.rl_choose1_backset, R.id.rl_choose2_backset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_choose_backset:
                Bundle bundle = new Bundle();
                bundle.putString(Const.ShowIntent.IMG, roomBackImg);
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().toOtherActivity(ThemeBackActivity.class, bundle, 101);
                break;
            case R.id.rl_choose1_backset:
                openSelectPhoto();
                break;
            case R.id.rl_choose2_backset:
                Intent intent1 = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
                intent1.setType("image/*");
                startActivityForResult(intent1, 1);
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
                            //检测路径是否存在，不存在就创建
                            xldUtils.initFilePath();
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            String fileName = System.currentTimeMillis() + ".jpg";
                            tempFile = new File(xldUtils.PICDIR, fileName);
                            Uri u = Uri.fromFile(tempFile);
                            //7.0崩溃问题
                            if (Build.VERSION.SDK_INT < 24) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                            } else {
                                ContentValues contentValues = new ContentValues(1);
                                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            }
                            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                            startActivityForResult(intent, 0);
                        } else {
                            showToast("请在应用权限页面开启相机权限");
                        }
                    }
                });
    }
}
