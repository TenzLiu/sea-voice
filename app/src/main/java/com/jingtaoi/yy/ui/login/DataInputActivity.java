package com.jingtaoi.yy.ui.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.UserBean;
import com.jingtaoi.yy.dialog.MyBirthdayDialog;
import com.jingtaoi.yy.model.ChatMessageBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.MainActivity;
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
import com.jingtaoi.yy.utils.fresco.IDownloadResult;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;


import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sinata.xldutils.activitys.SelectPhotoDialog;
import cn.sinata.xldutils.utils.Md5;
import cn.sinata.xldutils.utils.StringUtils;
import io.reactivex.functions.Consumer;

/**
 * 基本资料填写页面
 */
public class DataInputActivity extends MyBaseActivity {
    @BindView(R.id.iv_header_datainput)
    SimpleDraweeView ivHeaderDatainput;
    @BindView(R.id.rl_birthday_datainput)
    RelativeLayout rlBirthdayDatainput;
    @BindView(R.id.tv_man_datainput)
    ImageView tvManDatainput;
    @BindView(R.id.btn_over_datainput)
    Button btnOverDatainput;

    @BindView(R.id.tv_woman_datainput)
    ImageView tvWomanDatainput;


    String birthday = "1994-07-06";
    String nickName;
    String headerImg = "";
    int sex = 1;//性别(1 男, 2 女)
    Calendar calendar;//生日
    @BindView(R.id.tv_birthday_datainput)
    TextView tvBirthdayDatainput;

    String phone;
    String code;
    String password;
    boolean isSan;//判断是否三方登录
    @BindView(R.id.edt_nickname_datainput)
    EditText edtNicknameDatainput;
    @BindView(R.id.edt_shareone_datainput)
    EditText edtShareoneDatainput;

    @BindView(R.id.back_iv)
    ImageView back_iv;

    String cityName;


    @Override
    public void initData() {
        phone = getBundleString(Const.ShowIntent.PHONE);
        cityName = getBundleString(Const.ShowIntent.NAME);
        code = getBundleString(Const.ShowIntent.CODE);
        password = getBundleString(Const.ShowIntent.PASS);
        headerImg = getBundleString(Const.User.IMG);
        nickName = getBundleString(Const.User.NICKNAME);
        sex = getBundleInt(Const.User.SEX, 1);
        isSan = getBundleBoolean(Const.ShowIntent.TYPE, false);
        userToken = getBundleInt(Const.User.USER_TOKEN, 0);


    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_datainput);
    }

    @Override
    public void initView() {
        setTitleText(R.string.title_datainput);
        showTitle(false);
        showHeader(false);
        if (!StringUtils.isEmpty(headerImg)) {
//            updateImgtoShow(headerImg);
        }else {
            headerImg = "https://app-voice.obs.cn-east-3.myhuaweicloud.com/admin/21ae108a6dab404fb468e4f1f30f1262.png";
        }
        ImageUtils.loadUri(ivHeaderDatainput, headerImg);
        if (!StringUtils.isEmpty(nickName)) {
            edtNicknameDatainput.setText(nickName);
        }else {
            if (phone.length() > 3){
                String src = phone.substring(phone.length() - 5);
                edtNicknameDatainput.setText("惊涛语音" + src);
            }else {
                edtNicknameDatainput.setText("惊涛语音00000");
            }

        }
        if (!StringUtils.isEmpty(birthday)) {
            tvBirthdayDatainput.setText(birthday);
        }
        if (sex == 1) {
            tvWomanDatainput.setImageResource(R.drawable.nv_normal);
            tvManDatainput.setImageResource(R.drawable.nan_check);
        } else if (sex == 2) {
            tvWomanDatainput.setImageResource(R.drawable.nv_check);
            tvManDatainput.setImageResource(R.drawable.nan_normal);
        }
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtNicknameDatainput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setInputShow();
            }
        });
    }

    private void updateImgtoShow(String headerImg) {
        ImageUtils.downloadImage(this, headerImg, new IDownloadResult(this) {
            @Override
            public void onResult(String filePath) {
//                updateImgCall(filePath);
            }
        });
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
        setInputShow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == Const.RequestCode.SELECTPHOTO_CODE) {

                String filePath = data.getStringExtra(SelectPhotoDialog.DATA);
                cropImage(filePath);

            } else if (data != null && requestCode == Const.RequestCode.CROPTPHOTO_CODE) {
                String filePath = data.getStringExtra("path");
                showDialog();
//                updateImgCall(filePath);
                updateImgCallObs(filePath);
            }
        }
    }

    private void updateImgCallObs(String filePath) {
        showDialog();
        ObsUtils obsUtils = new ObsUtils();
        obsUtils.uploadFile(filePath, new ObsUtils.ObsCallback() {
            @Override
            public void onUploadFileSuccess(String url) {
                LogUtils.e(TAG, "onFinish: " + url);
                dismissDialog();
                headerImg = url;
                ImageUtils.loadUri(ivHeaderDatainput, headerImg);
                setInputShow();
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

    private void cropImage(String path) {
        Intent intent = new Intent(this, MyCropImageActivity.class);
        intent.putExtra("uri", path);
        intent.putExtra("mode", 1);
        startActivityForResult(intent, Const.RequestCode.CROPTPHOTO_CODE);
    }

    private void updateImgCall(String filePath) {
        OSSUtil ossUtil = new OSSUtil(this);
        ossUtil.uploadSingle(filePath, new OSSUtil.OSSUploadCallBack() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                LogUtils.e(TAG, "onFinish: " + url);
                dismissDialog();
                headerImg = url;
                ImageUtils.loadUri(ivHeaderDatainput, headerImg);
                setInputShow();
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

    private void setInputShow() {
        boolean isCanChoose = true;
        if (StringUtils.isEmpty(headerImg)) {
            isCanChoose = false;
        }
        nickName = edtNicknameDatainput.getText().toString();
        if (StringUtils.isEmpty(nickName)) {
            isCanChoose = false;
        }
        if (StringUtils.isEmpty(birthday)) {
            isCanChoose = false;
        }
        if (sex == 0) {
            isCanChoose = false;
        }
        if (isCanChoose) {
            btnOverDatainput.setEnabled(true);
            btnOverDatainput.setBackgroundResource(R.drawable.bg_mine_sex);
        } else {
            btnOverDatainput.setEnabled(false);
            btnOverDatainput.setBackgroundResource(R.drawable.btn_select);
        }
    }

    @OnClick({R.id.iv_header_datainput, R.id.rl_birthday_datainput, R.id.tv_woman_datainput, R.id.tv_man_datainput, R.id.btn_over_datainput})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header_datainput:
                openSelectPhoto();
                break;
            case R.id.rl_birthday_datainput:
                showBirthdayDialog();
                break;
            case R.id.tv_woman_datainput:
                if (sex != 2) {
                    sex = 2;
//                    tvWomanDatainput.setSelected(true);
//                    tvManDatainput.setSelected(false);
                }
                setInputShow();

                tvWomanDatainput.setImageResource(R.drawable.nv_check);
                tvManDatainput.setImageResource(R.drawable.nan_normal);
                break;
            case R.id.tv_man_datainput:
                if (sex != 1) {
                    sex = 1;
//                    tvWomanDatainput.setSelected(false);
//                    tvManDatainput.setSelected(true);
                }
                setInputShow();
                tvWomanDatainput.setImageResource(R.drawable.nv_normal);
                tvManDatainput.setImageResource(R.drawable.nan_check);
                break;
            case R.id.btn_over_datainput:
                getCall();
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

    private void getCall() {
        nickName = edtNicknameDatainput.getText().toString();
        if (StringUtils.isEmpty(headerImg)) {
//            showToast(getString(R.string.hint_img_datainput));
            headerImg = "https://app-voice.obs.cn-east-3.myhuaweicloud.com/admin/21ae108a6dab404fb468e4f1f30f1262.png";
//            return;
        }
        if (StringUtils.isEmpty(nickName)) {
            showToast(getString(R.string.hint_nickname_datainput));
            return;
        }
        if (StringUtils.isEmpty(birthday)) {
            showToast(getString(R.string.hint_birthday_datainput));
            return;
        }
        if (sex == 0) {
            showToast(getString(R.string.hint_sex_datainput));
            return;
        }
        if (isSan) {
            getSanCall();
        } else {
            getRegisterCall();
        }
    }

    private void getSanCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", userToken);
        map.put("sex", sex);
        map.put("dateOfBirth", birthday);
        map.put("imgTx", headerImg);
        map.put("nickname", nickName);
        String usercoding = edtShareoneDatainput.getText().toString();
        if (!StringUtils.isEmpty(usercoding)) {
            map.put("coding", usercoding);
        }
        HttpManager.getInstance().post(Api.updateUser, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
//                    initShared(userBean.getData());
                    //设置别名（新的调用会覆盖之前的设置）
                    JPushInterface.setAlias(DataInputActivity.this, 0, String.valueOf(userBean.getData().getId()));
                    String token = (String) SharedPreferenceUtils.get(DataInputActivity.this, Const.User.USER_SIG, "");
                    onRecvUserSig(String.valueOf(userToken), token, userBean.getData());
                } else {
                    showToast(userBean.getMsg());
                }
            }
        });
    }

    private void getRegisterCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("phone", phone);
        map.put("password", Md5.getMd5Value(password));
        map.put("smsCode", code);
        map.put("sex", sex);
        map.put("dateOfBirth", birthday);
        map.put("imgTx", headerImg);
        map.put("nickname", nickName);
        String usercoding = edtShareoneDatainput.getText().toString();
        if (!StringUtils.isEmpty(usercoding)) {
            map.put("usercoding", usercoding);
        }
        if (!StringUtils.isEmpty(cityName)) {
            map.put("city", cityName);
        }
        String uuid = MyUtils.getInstans().getUuid(this);
        map.put("uuid", uuid);
        HttpManager.getInstance().post(Api.PhoneRegistered, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UserBean userBean = JSON.parseObject(responseString, UserBean.class);
                if (userBean.getCode() == 0) {
                    initShared(userBean.getData());
                } else {
                    showToast(userBean.getMsg());
                }
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
        SharedPreferenceUtils.put(this, Const.User.USER_LiANG, dataBean.getLiang());
        SharedPreferenceUtils.put(this, Const.User.PAY_PASS, dataBean.getPayPassword());
        //设置别名（新的调用会覆盖之前的设置）
        JPushInterface.setAlias(this, 0, String.valueOf(dataBean.getId()));

        onRecvUserSig(String.valueOf(dataBean.getId()), dataBean.getToken(), dataBean);
    }


    /**
     * 在收到服务器颁发的 userSig 后，调用IMSDK的 login 接口
     * userId 用户账号
     * userSig 您服务器给这个用户账号颁发的 IMSDk 鉴权认证
     */
    private void onRecvUserSig(String userId, String userSig, UserBean.DataBean dataBean) {
        showDialog();
        TUIKit.login(userId, userSig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                /**
                 * IM 登录成功后的回调操作，一般为跳转到应用的主页（这里的主页内容为下面章节的会话列表）
                 */
                LogUtils.e(LogUtils.TAG, "登录腾讯云成功");
                showToast(getString(R.string.tv_register_success));
                LogUtils.e("getVersion", TIMManager.getInstance().getVersion());
                sendTencentMsg(dataBean);
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

    //发送注册成功消息
    private void sendTencentMsg(UserBean.DataBean dataBean) {
        String msgBean = "";
        ChatMessageBean.DataBean chatDataBean = new ChatMessageBean.DataBean(dataBean.getGoldNum(), "初来乍到，请多关照！", dataBean.getNickname(), dataBean.getId());
        ChatMessageBean chatMessageBean = new ChatMessageBean(1001, chatDataBean);

        msgBean = JSON.toJSONString(chatMessageBean);
        if (StringUtils.isEmpty(msgBean)) {
            return;
        }
        LogUtils.i("发送腾讯消息", msgBean);
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,    //会话类型：群聊
                Const.roomChannelMsg);

        //构造一条消息
        TIMMessage msg = new TIMMessage();
        TIMMessageOfflinePushSettings timMessageOfflinePushSettings = new TIMMessageOfflinePushSettings();
        timMessageOfflinePushSettings.setDescr("[广播消息]");
        msg.setOfflinePushSettings(timMessageOfflinePushSettings);
        //向 TIMMessage 中添加自定义内容
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(msgBean.getBytes());      //自定义 byte[]
        elem.setDesc("[房间消息]"); //自定义描述信息
        //将 elem 添加到消息
        if (msg.addElement(elem) != 0) {
            LogUtils.i(LogUtils.TAG, "addElement failed");
            return;
        }

        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
                LogUtils.e(i + s);
                showToast("消息发送失败" + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) { //发送消息成功
                LogUtils.i("消息发送成功");
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
        tvBirthdayDatainput.setText(birthday);
        setInputShow();
    }
}
