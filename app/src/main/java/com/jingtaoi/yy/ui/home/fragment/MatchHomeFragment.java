package com.jingtaoi.yy.ui.home.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.dialog.VoiceSignDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.home.MatchResultActivity;
import com.jingtaoi.yy.utils.AudioRecoderUtils;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 首页速配fragment
 */
public class MatchHomeFragment extends MyBaseFragment {
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.view_2)
    View view2;
    @BindView(R.id.view_3)
    View view3;
    @BindView(R.id.iv_head)
    SimpleDraweeView ivHead;
    @BindView(R.id.btn_voice)
    TextView btnVoice;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.group_sign)
    ConstraintLayout clVoice;
    Unbinder unbinder;
    @BindView(R.id.btn_play_voice)
    TextView btnPlayVoice;
    @BindView(R.id.btn_record)
    TextView btnRecord;

    private AudioRecoderUtils mAudioRecoderUtils;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchhome, container, false);
        return view;
    }

    private AnimationDrawable compoundDrawable;

    @Override
    public void initView() {
        ivHead.setImageURI((String) SharedPreferenceUtils.get(getActivity(), Const.User.IMG, ""));
        getVoice();
        mAudioRecoderUtils = new AudioRecoderUtils();
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {

            }

            @Override
            public void onStop(String filePath) {

            }

            @Override
            public void onStartPlay() {
                btnPlayVoice.setText("播放中");
                btnPlayVoice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.anim_match_play, 0, 0, 0);
                compoundDrawable = (AnimationDrawable) btnPlayVoice.getCompoundDrawables()[0];
                compoundDrawable.start();
            }

            @Override
            public void onFinishPlay() {
                btnPlayVoice.setText("播放声音签名");
                btnPlayVoice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                if (compoundDrawable!=null){
//                    compoundDrawable.stop();
//                }
            }
        });
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {
        ivHead.setOnClickListener(v -> {
                    transDown(ivHead);
                    transDown(view1);
                    transDown(view2);
                    transDown(view3);
                }
        );
        btnVoice.setOnClickListener(v -> {
            VoiceSignDialog dialog = new VoiceSignDialog();
            dialog.setCallbak(new VoiceSignDialog.UpdateCallbak() {
                @Override
                public void onUpdateVoice() {
                    getVoice();
                }
            });
            dialog.show(getFragmentManager(), "voice");
        });
        btnRecord.setOnClickListener(v -> {
            VoiceSignDialog dialog = new VoiceSignDialog();
            dialog.setCallbak(new VoiceSignDialog.UpdateCallbak() {
                @Override
                public void onUpdateVoice() {
                    getVoice();
                }
            });
            dialog.show(getFragmentManager(), "voice");
        });
        btnPlayVoice.setOnClickListener(v -> {
            mAudioRecoderUtils.startplayMusic(getContext(), path);
        });
    }

    //下移动画
    private void transDown(View view) {
        final int top = view.getTop();
        final ValueAnimator animator = ValueAnimator.ofInt(1, 240);
        if (view instanceof SimpleDraweeView) {
            ivHead.setClickable(false);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    clVoice.setVisibility(View.GONE);
                    tvHint.setVisibility(View.GONE);
                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ivHead.setOnClickListener(v -> {
                        transUp(ivHead);
                        transUp(view1);
                        transUp(view2);
                        transUp(view3);
                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.VISIBLE);
                        cancelByUser = true; //被用户取消搜索
                    });
                    transScale1();
                    new Handler().postDelayed(() -> {
                        if (!cancelByUser) {
                            transScale2();
                            getData();
                        }
                    }, 1000);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        animator.setDuration(400);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation1 -> {
            int current = (int) animator.getAnimatedValue();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            layoutParams.topMargin = top + current;
            view.setLayoutParams(layoutParams);
        });
        animator.start();
    }

    private String path; //声音路径

    private void getVoice() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", SharedPreferenceUtils.get(getActivity(), Const.User.USER_TOKEN, 0));
        HttpManager.getInstance().post(Api.VOICE, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optInt("code") == 0) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        int state = data.optInt("state");
                        if (state == 1) {//没有

                        } else {
                            btnVoice.setVisibility(View.GONE);
                            btnPlayVoice.setVisibility(View.VISIBLE);
                            btnRecord.setVisibility(View.VISIBLE);
                            path = data.optString("voice");
                        }
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

    private boolean cancelByUser = false;

    //获取匹配结果
    private void getData() {
        new CountDownTimer(4800, 4800) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (!cancelByUser) {
                    startActivity(new Intent(getActivity(), MatchResultActivity.class));
                    ivHead.callOnClick();
                    cancelByUser = false;
                }
            }
        }.start();
    }

    //上移动画
    private void transUp(View view) {
        final int top = view.getTop();
        final ValueAnimator animator = ValueAnimator.ofInt(1, 240);
        if (view instanceof SimpleDraweeView) {
            view1.clearAnimation();
            view2.clearAnimation();
            ivHead.setClickable(false);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    clVoice.setVisibility(View.VISIBLE);
                    tvHint.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    ivHead.setOnClickListener(v -> {
                        transDown(ivHead);
                        transDown(view1);
                        transDown(view2);
                        transDown(view3);
                        cancelByUser = false;
                    });
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        animator.setDuration(400);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation1 -> {
            int current = (int) animator.getAnimatedValue();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            layoutParams.topMargin = top - current;
            view.setLayoutParams(layoutParams);
        });
        animator.start();
    }

    //扩散动画
    private void transScale1() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.view_scale_1);
        view1.startAnimation(animation);
    }

    private void transScale2() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.view_scale_2);
        view2.startAnimation(animation);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAudioRecoderUtils.stopPlayMusic();
    }

    public void stopPlay() {
        if (mAudioRecoderUtils != null) {
            mAudioRecoderUtils.stopPlayMusic();
        }
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
