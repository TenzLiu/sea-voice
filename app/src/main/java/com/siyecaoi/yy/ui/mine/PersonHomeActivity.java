package com.siyecaoi.yy.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.bean.OnlineUserBean;
import com.siyecaoi.yy.ui.message.ChatActivity;
import com.siyecaoi.yy.ui.message.GlideBlurTransformation;
import com.siyecaoi.yy.ui.message.OtherHomeActivity;
import com.siyecaoi.yy.utils.ObsUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.GiftGetBean;
import com.siyecaoi.yy.bean.ImgEntity;
import com.siyecaoi.yy.bean.PersonalHomeBean;
import com.siyecaoi.yy.bean.UpdateOneBean;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.dialog.VoiceSignDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.adapter.PersonHomeGiftListAdapter;
import com.siyecaoi.yy.ui.mine.adapter.PersonHomeImgListAdapter;
import com.siyecaoi.yy.ui.mine.adapter.PersonHomePropListAdapter;
import com.siyecaoi.yy.ui.other.SelectPhotoActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.AudioRecoderUtils;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.OSSUtil;
import com.siyecaoi.yy.utils.SharedPreferenceUtils;
import com.siyecaoi.yy.view.ScrollInterceptScrollView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinata.xldutils.activitys.SelectPhotoDialog;
import cn.sinata.xldutils.utils.StringUtils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.siyecaoi.yy.utils.Const.RoomId;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 个人主页
 */
public class PersonHomeActivity extends MyBaseActivity {
    @BindView(R.id.iv_back_personhome)
    ImageView ivBackPersonhome;
    @BindView(R.id.tv_data_personhome)
    TextView tvDataPersonhome;
    @BindView(R.id.iv_show_personhome)
    SimpleDraweeView ivShowPersonhome;
    @BindView(R.id.iv_sex_personhome)
    ImageView ivSexPersonhome;
    @BindView(R.id.tv_name_personhome)
    TextView tvNamePersonhome;

    @BindView(R.id.iv_grade_personhome)
    ImageView ivGradePersonhome;


    @BindView(R.id.iv_gapsi)
    ImageView iv_gapsi;

    @BindView(R.id.iv_charm_personhome)
    ImageView ivCharmPersonhome;
    @BindView(R.id.tv_isattention_personhome)
    TextView tvIsattentionPersonhome;
    @BindView(R.id.tv_find_personhome)
    TextView tvFindPersonhome;
    @BindView(R.id.tv_room_personhome)
    TextView tvRoomPersonhome;
    @BindView(R.id.tv_attention_personhome)
    TextView tvAttentionPersonhome;
    @BindView(R.id.rl_attention_personhome)
    LinearLayout rlAttentionPersonhome;
    @BindView(R.id.tv_fans_personhome)
    TextView tvFansPersonhome;
    @BindView(R.id.rl_fans_personhome)
    LinearLayout rlFansPersonhome;
    @BindView(R.id.tv_cons_personhome)
    TextView tvConsPersonhome;
    @BindView(R.id.tv_roomid_personhome)
    TextView tvRoomidPersonhome;
    @BindView(R.id.mRecyclerView_personhome)
    RecyclerView mRecyclerViewPersonhome;
    @BindView(R.id.tv_signer_personhome)
    TextView tvSignerPersonhome;
    @BindView(R.id.iv_signers_personhome)
    ImageView ivSignersPersonhome;
    @BindView(R.id.ll_signers_personhome)
    RelativeLayout llSignersPersonhome;
    @BindView(R.id.mRecyclerView_gift_personhome)
    RecyclerView mRecyclerViewGiftPersonhome;

    @BindView(R.id.mRecyclerView_prop_personhome)
    RecyclerView mRecyclerViewPropPersonhome;
    @BindView(R.id.rl_chat_personhome)
    RelativeLayout rlChatPersonhome;
    @BindView(R.id.rl_addattention_personhome)
    RelativeLayout rlAddattentionPersonhome;
    @BindView(R.id.ll_bottom_personhome)
    LinearLayout llBottomPersonhome;

    int userId;
    boolean isMine;
    @BindView(R.id.iv_data_personhome)
    ImageView ivDataPersonhome;
    @BindView(R.id.tv_number_personhome)
    TextView tvNumberPersonhome;
    @BindView(R.id.tv_changesingers_personhome)
    TextView tvChangesingersPersonhome;


    PersonHomeImgListAdapter imgListAdapter;
    PersonHomeGiftListAdapter giftListAdapter;
    PersonHomePropListAdapter propListAdapter;
    @BindView(R.id.ll_find_personhome)
    LinearLayout llFindPersonhome;
    @BindView(R.id.rl_signers_personhome)
    LinearLayout rlSignersPersonhome;
    int giftState;//1 是价值， 2是数量
    @BindView(R.id.ll_show_personhome)
    LinearLayout llShowPersonhome;

    @BindView(R.id.ll_back_personshow)
    LinearLayout llBackPersonshow;

    @BindView(R.id.tv_nomal_gift)
    TextView tvNomalGift;

    @BindView(R.id.tv_packet_gift)
    TextView tvPacketGift;

    @BindView(R.id.tv_photo)
    TextView tv_photo;

    @BindView(R.id.liang_iv)
    ImageView liang_iv;

    private int mHeight;//滑动高度

    private AudioRecoderUtils mAudioRecoderUtils;
    String voiceSigner;//声音签名
    int voiceTime;
    Timer timer;
    int pageNumber;
    private String roomId;
    private int chooseOne;
    private String nickName;

    @Override
    public void initData() {
        userId = getBundleInt(Const.ShowIntent.ID, 0);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_personhome);
    }

    @Override
    public void initView() {

        showTitle(false);
        showHeader(false);
        setBlcakShow(false);

        if (userId == userToken) { //个人主页
            isMine = true;
            llSignersPersonhome.setVisibility(View.VISIBLE);
            tvDataPersonhome.setVisibility(View.VISIBLE);
        } else { //他人主页
            isMine = false;
            llBottomPersonhome.setVisibility(View.VISIBLE);
            ivDataPersonhome.setVisibility(View.VISIBLE);
            llFindPersonhome.setVisibility(View.VISIBLE);
        }

        tvSignerPersonhome.setHint(getString(R.string.hint_signer_data));

        setAudio();


        setScoll();
        setRecycler();
        setGiftRecycler();
        setPropRecycler();

        getCall();
        giftState = 2;
        getGiftCall();

        getIsBalckCall();

    }

    /**
     * 判断是否在黑名单中
     */
    private void getIsBalckCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", userId);
        HttpManager.getInstance().post(Api.getblock, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    if (getOneBean.getData().getState() == 2) {  //
                        showToast("对方已将您拉黑");
                        ActivityCollector.getActivityCollector().finishActivity();
                    } else {
                        getCall();
                        getGiftCall();
                        getFriendCall();
                    }
                } else {
                    showToast(getOneBean.getMsg());
                }
            }
        });
    }

    /**
     * 获取和当前用户的关系
     */
    private void getFriendCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", userId);
        HttpManager.getInstance().post(Api.UserAttention, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                OnlineUserBean onlineUserBean = JSON.parseObject(responseString, OnlineUserBean.class);
                if (onlineUserBean.getCode() == Api.SUCCESS) {
                    status = onlineUserBean.getData().getState();
                    setIsAttentionShow();
                }
            }
        });
    }


    private void setAudio() {
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
//                btnPlayVoice.setText("播放中");
//                btnPlayVoice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.anim_match_play,0,0,0);
//                compoundDrawable = (AnimationDrawable) btnPlayVoice.getCompoundDrawables()[0];
//                compoundDrawable.start();
            }

            @Override
            public void onFinishPlay() {
//                btnPlayVoice.setText("播放声音签名");
//                btnPlayVoice.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
////                if (compoundDrawable!=null){
////                    compoundDrawable.stop();
////                }
                ivSignersPersonhome.setImageResource(R.drawable.iv_play_voice);
            }
        });
    }

    private void setScoll() {
        //获取标题栏高度
        ViewTreeObserver viewTreeObserver = llShowPersonhome.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llShowPersonhome.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHeight = llShowPersonhome.getHeight();
                //注册滑动监听
//                mScrollPersonhome.setOnObservableScrollViewListener(onObservableScrollViewListener);
            }
        });
    }

//    ScrollInterceptScrollView.OnObservableScrollViewListener onObservableScrollViewListener =
//            new ScrollInterceptScrollView.OnObservableScrollViewListener() {
//                @Override
//                public void onObservableScrollViewListener(int l, int t, int oldl, int oldt) {
//                    if (t <= 0) {
//                        //顶部图处于最顶部，标题栏透明
//                        llBackPersonshow.setBackgroundColor(Color.argb(0, 255, 255, 255));
//                        ivBackPersonhome.setColorFilter(Color.argb(255, 255, 255, 255));
//                        tvDataPersonhome.setTextColor(Color.argb(255, 255, 255, 255));
//                        setBlcakShow(false);
//                    } else if (t < mHeight) {
//                        //滑动过程中，渐变
//                        float scale = (float) t / mHeight;//算出滑动距离比例
//                        float alpha = (255 * scale);//得到透明度
//                        int colorShow = (int) (255 - alpha);
//                        llBackPersonshow.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
//                        ivBackPersonhome.setColorFilter(Color.argb(255, colorShow, colorShow, colorShow));
//                        tvDataPersonhome.setTextColor(Color.argb(255, colorShow, colorShow, colorShow));
//                    } else {
//                        //过顶部图区域，标题栏定色
//                        llBackPersonshow.setBackgroundColor(Color.argb(255, 255, 255, 255));
//                        ivBackPersonhome.setColorFilter(Color.argb(255, 0, 0, 0));
//                        tvDataPersonhome.setTextColor(Color.argb(255, 0, 0, 0));
//                        setBlcakShow(true);
//                    }
//                }
//            };

    private void getGiftCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("state", giftState);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.userScene, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GiftGetBean giftGetBean = JSON.parseObject(responseString, GiftGetBean.class);
                if (giftGetBean.getCode() == Api.SUCCESS) {
                    setData(giftGetBean.getData(), giftListAdapter);
                } else {
                    showToast(giftGetBean.getMsg());
                }
            }
        });
    }

    private void setData(List<GiftGetBean.DataEntity> data, PersonHomeGiftListAdapter adapter) {
        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            adapter.setNewData(data);
        } else {
            if (size > 0) {
                adapter.addData(data);
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            //不显示没有更多
            adapter.loadMoreEnd(true);
        } else {
            adapter.loadMoreComplete();
        }
    }

    private void getCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
        HttpManager.getInstance().post(Api.PersonageUser, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                PersonalHomeBean personalHomeBean = JSON.parseObject(responseString, PersonalHomeBean.class);
                if (personalHomeBean.getCode() == Api.SUCCESS) {
                    initShow(personalHomeBean.getData());
                } else {
                    showToast(personalHomeBean.getMsg());
                }
            }
        });
    }

    /**
     * 更新显示
     *
     * @param data
     */
    @SuppressLint("SetTextI18n")
    private void initShow(PersonalHomeBean.DataEntity data) {
        if (data.getUser() != null) {
            nickName = data.getUser().getNickname();
            PersonalHomeBean.DataEntity.UserEntity userEntity = data.getUser();
            ImageUtils.loadUri(ivShowPersonhome, userEntity.getImgTx());

            Glide.with(this)
                    .load(userEntity.getImgTx())
                    .apply(RequestOptions.bitmapTransform(new GlideBlurTransformation(this)))
                    // .apply(RequestOptions.bitmapTransform( new BlurTransformation(context, 20)))
                    .into(new ViewTarget<ImageView, Drawable>(iv_gapsi) {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            Drawable current = resource.getCurrent();
                            iv_gapsi.setImageDrawable(current);
                        }
                    });

            tvNamePersonhome.setText(userEntity.getNickname());
            if (userEntity.getSex() == 1) {
                ivSexPersonhome.setSelected(true);
//                llShowPersonhome.setBackgroundResource(R.drawable.personal_bg2);
            } else if (userEntity.getSex() == 2) {
                ivSexPersonhome.setSelected(false);
//                llShowPersonhome.setBackgroundResource(R.drawable.personal_bg);
            }
            roomId = userEntity.getUsercoding();

            //财富等级显示
//            ivGradePersonhome.setBackgroundResource(ImageShowUtils.getGrade(userEntity.getTreasureGrade()));
//            ivGradePersonhome.setText(ImageShowUtils.getGradeText(userEntity.getTreasureGrade()));
            ivGradePersonhome.setImageResource(ImageShowUtils.getGrade(userEntity.getTreasureGrade()));
            //魅力等级显示
//            ivCharmPersonhome.setBackgroundResource(ImageShowUtils.getGrade(userEntity.getCharmGrade()));
//            ivCharmPersonhome.setText(ImageShowUtils.getCharmText(userEntity.getCharmGrade()));
            ivCharmPersonhome.setImageResource(ImageShowUtils.getCharm(userEntity.getCharmGrade()));

            tvAttentionPersonhome.setText(userEntity.getAttentionNum() + "");
            tvFansPersonhome.setText(userEntity.getFansNum() + "");
            tvConsPersonhome.setText(userEntity.getConstellation());
            if (StringUtils.isEmpty(userEntity.getLiang())) {
                tvRoomidPersonhome.setText(userEntity.getUsercoding());
            } else {
                tvRoomidPersonhome.setText(userEntity.getLiang());
            }
            data.getImg().add(new ImgEntity());
            imgListAdapter.setNewData(data.getImg());
            pageNumber = data.getImg().size();

            tvSignerPersonhome.setText(userEntity.getIndividuation());


            if (TextUtils.isEmpty( userEntity.getLiang())){
                liang_iv.setVisibility(View.GONE);
            }else {
                liang_iv.setVisibility(View.VISIBLE);
            }


            propListAdapter.setNewData(data.getScene());
            voiceSigner = data.getUser().getVoice();
        }
    }

    /**
     * 道具列表
     */
    private void setPropRecycler() {
        propListAdapter = new PersonHomePropListAdapter(R.layout.item_prop_presonhome);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerViewPropPersonhome.setLayoutManager(layoutManager);
        mRecyclerViewPropPersonhome.setAdapter(propListAdapter);

        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        ivNodata.setImageResource(R.drawable.empty_prop);
        tvNodata.setText(getString(R.string.hint_prop_personhome));
        propListAdapter.setEmptyView(view);

        propListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PersonalHomeBean.DataEntity.SceneEntity sceneEntity = (PersonalHomeBean.DataEntity.SceneEntity) adapter.getItem(position);
                assert sceneEntity != null;
                showMyUpdialog(sceneEntity);
            }
        });
    }

    /**
     * 判断是否使用该座驾或头饰
     *
     * @param sceneEntity
     */
    MyDialog myDialog;

    private void showMyUpdialog(PersonalHomeBean.DataEntity.SceneEntity sceneEntity) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        int state = sceneEntity.getState();
        if (sceneEntity.getType() == 1) {  //座驾
            if (state == 1) { //未使用
                myDialog.setHintText("是否使用此座驾？");
            } else if (state == 2) {
                myDialog.setHintText("是否取消使用此座驾？");
            }
        } else if (sceneEntity.getType() == 2) {
            if (state == 1) { //未使用
                myDialog.setHintText("是否使用此头饰？");
            } else if (state == 2) {
                myDialog.setHintText("是否取消使用此头饰？");
            }
        }
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                if (state == 1) {
                    getUpCall(sceneEntity.getType(), sceneEntity.getId(), Const.IntShow.ONE);
                } else if (state == 2) {
                    getUpCall(sceneEntity.getType(), 0, Const.IntShow.TWO);
                }
            }
        });
    }

    private void getUpCall(int type, int id, int isCancel) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", userToken);
        map.put("seId", id);
        if (isCancel == 2) {
            map.put("isSeId", type);
        }
        HttpManager.getInstance().post(Api.updateUser, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                UpdateOneBean baseBean = JSON.parseObject(responseString, UpdateOneBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (type == 1) {//座驾
                        SharedPreferenceUtils.put(PersonHomeActivity.this, Const.User.CAR, baseBean.getData().getUserZj());
                        SharedPreferenceUtils.put(PersonHomeActivity.this, Const.User.CAR_H, baseBean.getData().getUserZjfm());
                    } else if (type == 2) {
                        SharedPreferenceUtils.put(PersonHomeActivity.this, Const.User.HEADWEAR, baseBean.getData().getUserTh());
                        SharedPreferenceUtils.put(PersonHomeActivity.this, Const.User.HEADWEAR_H, baseBean.getData().getUserZjfm());
                    }
                    getCall();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }


    private void setGiftRecycler() {
        giftListAdapter = new PersonHomeGiftListAdapter(R.layout.item_gift_presonhome);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerViewGiftPersonhome.setLayoutManager(layoutManager);
        mRecyclerViewGiftPersonhome.setAdapter(giftListAdapter);

        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        ivNodata.setImageResource(R.drawable.empty_gift);
        tvNodata.setText(getString(R.string.hint_gift_personhome));
        giftListAdapter.setEmptyView(view);

        giftListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getGiftCall();
            }
        }, mRecyclerViewGiftPersonhome);
    }

    private void setRecycler() {
        imgListAdapter = new PersonHomeImgListAdapter(R.layout.item_img_personhome);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerViewPersonhome.setLayoutManager(layoutManager);
        mRecyclerViewPersonhome.setAdapter(imgListAdapter);

//        View view = View.inflate(this, R.layout.layout_nodata, null);
//        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
//        TextView tvNodata = view.findViewById(R.id.tv_nodata);
//        ivNodata.setVisibility(View.GONE);
//        tvNodata.setText(getString(R.string.hint_img_personhome));
//        imgListAdapter.setEmptyView(view);

        imgListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ImgEntity imgEntity = (ImgEntity) adapter.getItem(position);
                List<ImgEntity> listimg = imgListAdapter.getData();
                if (position == listimg.size()-1){
                    if (pageNumber < 7) {
                        openSelectPhoto();
                    } else {
                        showToast("最多上传6张图片");
                    }
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putInt(Const.ShowIntent.POSITION, position);
                    ArrayList<ImgEntity> imgEntities1 = new ArrayList<>(listimg.subList(0, listimg.size() - 1));
                    bundle.putSerializable(Const.ShowIntent.DATA, (Serializable) imgEntities1);
                    bundle.putBoolean(Const.ShowIntent.TYPE, true);
                    ActivityCollector.getActivityCollector().toOtherActivity(ShowPhotoActivity.class, bundle, Const.RequestCode.CLEAR_PIC);
                }
            }
        });
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

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == Const.RequestCode.SELECTPHOTO_CODE) {
                String filePath = data.getStringExtra(SelectPhotoDialog.DATA);
                updateImgCallObs(filePath);
            } else if (requestCode == Const.RequestCode.CLEAR_PIC) {
                getCall();
            } else if (requestCode == Const.RequestCode.DATA_CHANGE) {
                getCall();
            }
        }
    }

    private void updateImgCallObs(String filePath) {
        showDialog();
        ObsUtils obsUtils = new ObsUtils();
        obsUtils.uploadFile(filePath, new ObsUtils.ObsCallback() {
            @Override
            public void onUploadFileSuccess(String url) {
                dismissDialog();
                LogUtils.e(TAG, "onFinish: " + url);
//                dismissDialog();
                uploadImgCall(url);
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
//                dismissDialog();
                uploadImgCall(url);
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

    /**
     * 图片上传
     *
     * @param url
     */
    private void uploadImgCall(String url) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("imagess", url);
        HttpManager.getInstance().post(Api.addUserImg, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    getCall();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back_personhome, R.id.tv_data_personhome, R.id.tv_find_personhome,
            R.id.tv_room_personhome, R.id.rl_attention_personhome, R.id.rl_fans_personhome,
            R.id.iv_signers_personhome,  R.id.rl_chat_personhome,R.id.tv_photo,
            R.id.rl_addattention_personhome, R.id.rl_signers_personhome, R.id.tv_changesingers_personhome,R.id.tv_nomal_gift,R.id.tv_packet_gift})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_back_personhome:
                ActivityCollector.getActivityCollector().finishActivity();
                break;
            case R.id.tv_data_personhome:
                ActivityCollector.getActivityCollector().toOtherActivity(PersonDataActivity.class, Const.RequestCode.DATA_CHANGE);
                break;
            case R.id.tv_find_personhome:
                break;
            case R.id.tv_room_personhome:
                if (RoomId.equals(roomId)) {
                    showToast("您已在该房间");
                    return;
                }
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                break;
            case R.id.rl_attention_personhome:
                ActivityCollector.getActivityCollector().toOtherActivity(MyAttentionActivity.class);
                break;
            case R.id.rl_fans_personhome:
                ActivityCollector.getActivityCollector().toOtherActivity(MyFansActivity.class);
                break;
            case R.id.iv_signers_personhome:
                setVoiceShow();
                break;
//            case R.id.tv_typeshow_personhome:
//                showDialog();
//                if (giftState == 1) { //价值
//                    giftState = 2;
//                    tvTypeshowPersonhome.setText(R.string.tv_number_personhome);
//                } else if (giftState == 2) {
//                    giftState = 1;
//                    tvTypeshowPersonhome.setText(R.string.tv_price_personhome);
//                }
//                page = 1;
//                getGiftCall();
//                break;
            case R.id.rl_chat_personhome:
                bundle.putString(Const.ShowIntent.ID, userId + "");
                bundle.putString(Const.ShowIntent.NAME, nickName);
                ActivityCollector.getActivityCollector().finishActivity(ChatActivity.class);
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(TIMConversationType.C2C);
                chatInfo.setId(String.valueOf(userId));
                chatInfo.setChatName(nickName);
                bundle.putSerializable(Const.ShowIntent.CHAT_INFO, chatInfo);
                VoiceActivity activity = ActivityCollector.getActivity(VoiceActivity.class);
                if (activity == null) {
                    bundle.putBoolean("isRoom", false);
                } else {
                    bundle.putBoolean("isRoom", true);
                }
                ActivityCollector.getActivityCollector().toOtherActivity(ChatActivity.class, bundle);

                break;
            case R.id.rl_signers_personhome:
//                setVoiceShow();
                break;
            case R.id.tv_changesingers_personhome:
                VoiceActivity activity1 = ActivityCollector.getActivity(VoiceActivity.class);
                if (activity1 != null) {
                    showToast("退出房间后才能录制声音签名");
                    return;
                }
                VoiceSignDialog dialog = new VoiceSignDialog();
                dialog.setCallbak(new VoiceSignDialog.UpdateCallbak() {
                    @Override
                    public void onUpdateVoice() {
                        dialog.dismiss();
                        getVoice();
                    }
                });
                dialog.show(getSupportFragmentManager(), "voice");
                break;
            case R.id.rl_addattention_personhome:
                showMyDialog(Const.IntShow.ONE, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myDialog != null && myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        getAttentionCall();
                    }
                });
                break;
            case R.id.tv_photo:

                if (chooseOne != 0) {
                    setNochoose();
                    chooseOne = 0;
                    setChoose();
                }
                break;
            case R.id.tv_nomal_gift:

                if (chooseOne != 1) {
                    setNochoose();
                    chooseOne = 1;
                    setChoose();
                }
                break;

            case R.id.tv_packet_gift:

                if (chooseOne != 2) {
                    setNochoose();
                    chooseOne = 2;
                    setChoose();
                }
                break;

        }
    }



    private void showMyDialog(int showType, View.OnClickListener onClickListener) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(PersonHomeActivity.this);
        myDialog.show();
        if (showType == 1) {
            if (status == 1) {
                myDialog.setHintText("是否关注好友?");
            } else if (status == 2) {
                myDialog.setHintText("是否取消关注？");
            }
        } else if (showType == 2) {
            myDialog.setHintText("加入黑名单后， 您将不再收到对方的消息。");
        }
        myDialog.setRightButton(onClickListener);
    }


    int status;//当前用户和这个用户的关系 1是未关注，2是已关注
    private void setIsAttentionShow() {
        if (status == 1) {  //1是未关注，2是已关注 |
            tvIsattentionPersonhome.setText(R.string.tv_attention_personhome);
            Drawable nav_up = getResources().getDrawable(R.drawable.icon_attention);
            tvIsattentionPersonhome.setTextColor(ContextCompat.getColor(this, R.color.white));
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            tvIsattentionPersonhome.setCompoundDrawables(nav_up, null, null, null);
        } else if (status == 2) {
            tvIsattentionPersonhome.setText(R.string.tv_attentioned_person);
            tvIsattentionPersonhome.setTextColor(ContextCompat.getColor(this, R.color.white));
            Drawable nav_up = getResources().getDrawable(R.drawable.icon_attention_selected);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            tvIsattentionPersonhome.setCompoundDrawables(nav_up, null, null, null);
        }
    }



    private void getAttentionCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", userId);
        map.put("type", status);
        HttpManager.getInstance().post(Api.addAttention, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    if (status == 1) {
                        status = 2;
                        showToast("关注成功");
                    } else if (status == 2) {
                        status = 1;
                        showToast("取消关注成功");
                    }
                    setIsAttentionShow();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void setChoose() {
        switch (chooseOne) {
            case 1:
                tvNomalGift.setTextColor(ContextCompat.getColor(this, R.color.tvCommon));
                tvNomalGift.setTextSize(14);
                tvNomalGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);
                mRecyclerViewPropPersonhome.setVisibility(View.GONE);
                mRecyclerViewGiftPersonhome.setVisibility(View.VISIBLE);
                mRecyclerViewPersonhome.setVisibility(View.GONE);
                break;
            case 2:
                tvPacketGift.setTextColor(ContextCompat.getColor(this, R.color.tvCommon));
                tvPacketGift.setTextSize(14);
                tvPacketGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);
                mRecyclerViewPropPersonhome.setVisibility(View.VISIBLE);
                mRecyclerViewGiftPersonhome.setVisibility(View.GONE);
                mRecyclerViewPersonhome.setVisibility(View.GONE);

                break;
            case 0:
                tv_photo.setTextColor(ContextCompat.getColor(this, R.color.tvCommon));
                tv_photo.setTextSize(14);
                tv_photo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.title_bar_line);
                mRecyclerViewPersonhome.setVisibility(View.VISIBLE);
                mRecyclerViewPropPersonhome.setVisibility(View.GONE);
                mRecyclerViewGiftPersonhome.setVisibility(View.GONE);
                break;

        }
    }

    private void setNochoose() {
        switch (chooseOne) {
            case 1:
                tvNomalGift.setTextColor(ContextCompat.getColor(this, R.color.tv2B));
                tvNomalGift.setTextSize(12);
                tvNomalGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 2:
                tvPacketGift.setTextColor(ContextCompat.getColor(this, R.color.tv2B));
                tvPacketGift.setTextSize(12);
                tvPacketGift.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 0:
                tv_photo.setTextColor(ContextCompat.getColor(this, R.color.tv2B));
                tv_photo.setTextSize(12);
                tv_photo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
        }
    }


    private void getVoice() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        HttpManager.getInstance().post(Api.VOICE, map, new MyObserver(this) {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optInt("code") == 0) {
                        JSONObject data = jsonObject.optJSONObject("data");
                        int state = data.optInt("state");
                        if (state == 1) {//没有

                        } else {
                            voiceSigner = data.optString("voice");
                            if (!StringUtils.isEmpty(voiceSigner)) {
                                voiceTime = mAudioRecoderUtils.getMusicTime(PersonHomeActivity.this, voiceSigner);
//                                tvNumberPersonhome.setVisibility(View.VISIBLE);
//                                rlSignersPersonhome.setVisibility(View.VISIBLE);
//                                tvNumberPersonhome.setText(voiceTime + "''");
//                                tvChangesingersPersonhome.setText(getString(R.string.tv_change_personhome));
                            }
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

    int voiceTimeadd;

    private void setVoiceShow() {
        if (!StringUtils.isEmpty(voiceSigner) && voiceTime != 0) {
            mAudioRecoderUtils.startplayMusic(this, voiceSigner);
//            if (timer != null) {
//                timer.cancel();
//            }
//            timer = new Timer();
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    handler.sendEmptyMessage(101);
//                }
//            };
//            timer.schedule(timerTask, 0, 500);
            ivSignersPersonhome.setImageResource(R.drawable.iv_pause_voice);
        } else {
            showToast("录一段声音介绍自己吧");
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
                    if (voiceTimeadd <= voiceTime * 2) {
                        voiceTimeadd++;
                        if (voiceTimeadd % 3 == 0) {
                            ivSignersPersonhome.setImageResource(R.drawable.voice1);
                        } else if (voiceTimeadd % 3 == 1) {
                            ivSignersPersonhome.setImageResource(R.drawable.voice2);
                        } else if (voiceTimeadd % 3 == 2) {
                            ivSignersPersonhome.setImageResource(R.drawable.voice3);
                        }
                    } else {
                        if (timer != null) {
                            timer.cancel();
                        }
                        voiceTimeadd = 0;
                        ivSignersPersonhome.setImageResource(R.drawable.voice3);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (mAudioRecoderUtils != null) {
            mAudioRecoderUtils.stopPlayMusic();
        }
        if (timer != null) {
            timer.cancel();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
