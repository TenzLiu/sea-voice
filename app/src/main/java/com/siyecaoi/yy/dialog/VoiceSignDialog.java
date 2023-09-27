package com.siyecaoi.yy.dialog;

import android.Manifest;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.AudioRecoderUtils;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.OSSUtil;
import com.siyecaoi.yy.utils.ObsUtils;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.utils.TimeUtils;
import io.reactivex.functions.Consumer;

public class VoiceSignDialog extends DialogFragment {
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.ll_voice)
    LinearLayout llVoice;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_play)
    TextView tvPlay;


    @BindView(R.id.gb_iv)
    ImageView gb_iv;


    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_voice_sign, container, false);
        unbinder = ButterKnife.bind(this, view);

        gb_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Dialog);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().setCanceledOnTouchOutside(true);
    }

    private AudioRecoderUtils mAudioRecoderUtils;
    private String path;
    private Long length;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAudioRecoderUtils = new AudioRecoderUtils();
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                length = time / 1000;
                if (length == 61) {
                    mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                    tvRecord.setText("按住录音");
                } else {
                    tvLength.setText(TimeUtils.getTimeMS(time));
                }
            }

            @Override
            public void onStop(String filePath) {
                updateVoiceObs(filePath);
            }

            @Override
            public void onStartPlay() {
                tvPlay.setSelected(true);
            }

            @Override
            public void onFinishPlay() {
                if (tvPlay != null)
                    tvPlay.setSelected(false);
            }
        });
        tvRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        RxPermissions rxPermissions = new RxPermissions(getActivity());
                        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        if (aBoolean) {
                                            tvRecord.setText("松开保存");
                                            mAudioRecoderUtils.startRecord();
                                        } else {
                                            Toast.makeText(getContext(), "请在应用权限页面开启录音和文件权限", Toast.LENGTH_SHORT);
                                        }
                                    }
                                });
                        break;

                    case MotionEvent.ACTION_UP:

                        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
//                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        tvRecord.setText("按住录音");
                        break;
                }
                return true;
            }
        });
        tvDelete.setOnClickListener(v -> {
            tvLength.setText("00:00");
            path = null;
        });
        tvPlay.setOnClickListener(v -> {
            if (tvPlay.isSelected()) {
                mAudioRecoderUtils.stopPlayMusic();
                tvPlay.setSelected(false);
            } else
                mAudioRecoderUtils.startplayMusic(getContext(), path);
        });
        tvUpload.setOnClickListener(v -> {
            if (path == null) {
                Toast.makeText(getContext(), "请先录音", Toast.LENGTH_SHORT).show();
            } else {
                setVoice();
            }
        });
    }

    //设置声音签名
    private void setVoice() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", SharedPreferenceUtils.get(Objects.requireNonNull(getActivity()), Const.User.USER_TOKEN, 0));
        map.put("voice", path);
        map.put("voiceTime", length);

        HttpManager.getInstance().post(Api.updateUser, map, new MyObserver(getContext()) {
            @Override
            public void success(String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optInt("code") == 0) {
                        Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        dismiss();
                        callbak.onUpdateVoice();
                    } else {
                        showToast(jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("解析错误");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateVoiceObs(String filePath) {

        ObsUtils obsUtils = new ObsUtils();
        obsUtils.uploadFile(filePath, new ObsUtils.ObsCallback() {
            @Override
            public void onUploadFileSuccess(String url) {
                LogUtils.e("onFinish: " + url);
                path = url;
            }

            @Override
            public void onUploadMoreFielSuccess() {

            }

            @Override
            public void onFail(String message) {
                LogUtils.e("obs " + message);
                Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVoice(String filePath) {
        OSSUtil ossUtil = new OSSUtil(getActivity());
        ossUtil.uploadSingle(filePath, new OSSUtil.OSSUploadCallBack() {
            @Override
            public void onFinish(String url) {
                super.onFinish(url);
                path = url;
            }

            @Override
            public void onFial(String message) {
                super.onFial(message);
                Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private UpdateCallbak callbak;

    public void setCallbak(UpdateCallbak callbak) {
        this.callbak = callbak;
    }

    public interface UpdateCallbak {
        void onUpdateVoice();
    }
}
