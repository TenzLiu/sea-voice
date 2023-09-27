package com.siyecaoi.yy.ui.message;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.bean.GetOneBean;
import com.siyecaoi.yy.bean.GiftGetBean;
import com.siyecaoi.yy.bean.ImgEntity;
import com.siyecaoi.yy.bean.OnlineUserBean;
import com.siyecaoi.yy.bean.PersonalHomeBean;
import com.siyecaoi.yy.dialog.MyBottomShowDialog;
import com.siyecaoi.yy.dialog.MyDialog;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.ui.mine.PersonDataActivity;
import com.siyecaoi.yy.ui.mine.ShowPhotoActivity;
import com.siyecaoi.yy.ui.mine.adapter.PersonHomeGiftListAdapter;
import com.siyecaoi.yy.ui.mine.adapter.PersonHomeImgListAdapter;
import com.siyecaoi.yy.ui.mine.adapter.PersonHomePropListAdapter;
import com.siyecaoi.yy.ui.other.SelectPhotoActivity;
import com.siyecaoi.yy.ui.room.AllMsgActivity;
import com.siyecaoi.yy.ui.room.VoiceActivity;
import com.siyecaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.AudioRecoderUtils;
import com.siyecaoi.yy.utils.Const;
import com.siyecaoi.yy.utils.ImageShowUtils;
import com.siyecaoi.yy.utils.ImageUtils;
import com.siyecaoi.yy.utils.LogUtils;
import com.siyecaoi.yy.utils.OSSUtil;
import com.siyecaoi.yy.utils.ObsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

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
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.siyecaoi.yy.utils.Const.RoomId;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 他人个人主页
 */
public class OtherHomeActivity extends MyBaseActivity {
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

    @BindView(R.id.iv_charm_personhome)
    ImageView ivCharmPersonhome;
    @BindView(R.id.tv_photo)
    TextView tv_photo;
    @BindView(R.id.tv_room_personhome)
    TextView tvRoomPersonhome;
    @BindView(R.id.tv_attention_personhome)
    TextView tvAttentionPersonhome;

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

    @BindView(R.id.ll_show_personhome)
    LinearLayout llShowPersonhome;

    LinearLayout llBackPersonshow;
    @BindView(R.id.tv_isattention_personhome)
    TextView tvIsattentionPersonhome;
    @BindView(R.id.tv_find_personhome)
    TextView tv_find_personhome;
    @BindView(R.id.iv_room_personhome)
    SimpleDraweeView ivRoomPersonhome;
    @BindView(R.id.tv_roomname_personhome)
    TextView tvRoomnamePersonhome;
    @BindView(R.id.ll_room_personhome)
    LinearLayout llRoomPersonhome;
    @BindView(R.id.rl_attention_personhome)
    LinearLayout rlAttentionPersonhome;

//    @BindView(R.id.gaosi_rl)
//    RelativeLayout gaosi_rl;

    @BindView(R.id.iv_gapsi)
    ImageView iv_gapsi;

    @BindView(R.id.liang_iv)
    ImageView liang_iv;

    private int mHeight;//滑动高度
    String nickName;
    int giftState;//1 是价值， 2是数量
    int userId;
    boolean isMine;
    int status;//当前用户和这个用户的关系 1是未关注，2是已关注
    String roomId;
    String otherRoomId;//去找他的房间

    MyBottomShowDialog myBottomShowDialog;
    MyDialog myDialog;
    private ArrayList<String> bottomList;//弹框显示内容
    private AudioRecoderUtils mAudioRecoderUtils;
    String voiceSigner;
    private int chooseOne;

    @BindView(R.id.tv_nomal_gift)
    TextView tvNomalGift;

    @BindView(R.id.tv_packet_gift)
    TextView tvPacketGift;



    @Override
    public void initData() {
        userId = getBundleInt(Const.ShowIntent.ID, 0);
        bottomList = new ArrayList<>();
        bottomList.add(getString(R.string.tv_ju_data));
        bottomList.add(getString(R.string.tv_hei_data));
        bottomList.add(getString(R.string.tv_cancel));
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

        llBottomPersonhome.setVisibility(View.VISIBLE);
//        ivDataPersonhome.setVisibility(View.VISIBLE);
        llFindPersonhome.setVisibility(View.VISIBLE);
//        tvChangesingersPersonhome.setVisibility(View.GONE);

        setScoll();
        setRecycler();
        setGiftRecycler();
        setPropRecycler();

        giftState = 2;

        getIsBalckCall();
        setAudio();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(iv_gapsi != null &&  !this.isDestroyed()){
            Glide.with(this).clear(iv_gapsi);
            iv_gapsi = null;
        }
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

    private void setIsAttentionShow() {
        if (status == 1) {  //1是未关注，2是已关注 |
            tvIsattentionPersonhome.setText(R.string.tv_attention_personhome);
            Drawable nav_up = getResources().getDrawable(R.drawable.white_add);
            tvIsattentionPersonhome.setTextColor(ContextCompat.getColor(this, R.color.white));
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            tvIsattentionPersonhome.setCompoundDrawables(nav_up, null, null, null);
        } else if (status == 2) {
            tvIsattentionPersonhome.setText(R.string.tv_attentioned_person);
            tvIsattentionPersonhome.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvIsattentionPersonhome.setCompoundDrawables(null, null, null, null);
        }
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
//                        ivDataPersonhome.setColorFilter(Color.argb(255, 255, 255, 255));
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
//                        ivDataPersonhome.setColorFilter(Color.argb(255, colorShow, colorShow, colorShow));
//                    } else {
//                        //过顶部图区域，标题栏定色
//                        llBackPersonshow.setBackgroundColor(Color.argb(255, 255, 255, 255));
//                        ivBackPersonhome.setColorFilter(Color.argb(255, 0, 0, 0));
//                        ivDataPersonhome.setColorFilter(Color.argb(255, 0, 0, 0));
//                        tvDataPersonhome.setTextColor(Color.argb(255, 0, 0, 0));
//                        setBlcakShow(true);
//                    }
//                }
//            };

    private void getGiftCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userId);
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

            nickName = userEntity.getNickname();
            roomId = userEntity.getUsercoding();
            tvNamePersonhome.setText(nickName);
            if (userEntity.getSex() == 1) {
                ivSexPersonhome.setSelected(true);
//                llShowPersonhome.setBackgroundResource(R.drawable.personal_bg2);
                llRoomPersonhome.setBackgroundResource(R.drawable.otherroom2);
            } else if (userEntity.getSex() == 2) {
                ivSexPersonhome.setSelected(false);
//                llShowPersonhome.setBackgroundResource(R.drawable.personal_bg);
                llRoomPersonhome.setBackgroundResource(R.drawable.otherroom1);
            }
            voiceSigner = userEntity.getVoice();
            if (StringUtils.isEmpty(voiceSigner)) {
                llSignersPersonhome.setVisibility(View.GONE);
            } else {
                llSignersPersonhome.setVisibility(View.VISIBLE);
//                mAudioRecoderUtils = new AudioRecoderUtils();
                voiceTime = mAudioRecoderUtils.getMusicTime(this, voiceSigner);
                tvNumberPersonhome.setText(voiceTime + "''");
            }
            if (userEntity.getIsRoom() == 1) { //判断是否开启房间
                tvRoomPersonhome.setVisibility(View.GONE);
            } else if (userEntity.getIsRoom() == 2) {
                tvRoomPersonhome.setVisibility(View.VISIBLE);
            }


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
                liang_iv.setVisibility(View.GONE);
            } else {
                tvRoomidPersonhome.setText(userEntity.getLiang());
                liang_iv.setVisibility(View.VISIBLE);
            }

            imgListAdapter.setNewData(data.getImg());

            tvSignerPersonhome.setText(userEntity.getIndividuation());

            propListAdapter.setNewData(data.getScene());

        }

        //判断是否在房间
        if (data.getRoom() != null) {
            if (!StringUtils.isEmpty(data.getRoom().getRid())) {
//                llRoomPersonhome.setVisibility(View.VISIBLE);
                otherRoomId = data.getRoom().getRid();
//                tvRoomnamePersonhome.setText(data.getRoom().getRname());
//                ImageUtils.loadUri(ivRoomPersonhome, data.getRoom().getRimg());
                tv_find_personhome.setVisibility(View.VISIBLE);
            } else {
//                llRoomPersonhome.setVisibility(View.GONE);
                tv_find_personhome.setVisibility(View.GONE);
            }
        } else {
            llRoomPersonhome.setVisibility(View.GONE);
        }
    }

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

        View view = View.inflate(this, R.layout.layout_nodata, null);
        ImageView ivNodata = view.findViewById(R.id.iv_nodata);
        TextView tvNodata = view.findViewById(R.id.tv_nodata);
        ivNodata.setVisibility(View.GONE);
        tvNodata.setText(getString(R.string.hint_img_personhome));
        imgListAdapter.setEmptyView(view);

//        View view1 = View.inflate(this, R.layout.item_img_personhome, null);
//        SimpleDraweeView ivShow = view1.findViewById(R.id.iv_img_personhome);
//        ImageUtils.loadDrawable(ivShow, R.drawable.add_img);
//        ivShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openSelectPhoto();
//            }
//        });
//        imgListAdapter.addFooterView(view1);

        imgListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ImgEntity imgEntity = (ImgEntity) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.POSITION, position);
                List<ImgEntity> listimg = imgListAdapter.getData();
                bundle.putSerializable(Const.ShowIntent.DATA, (Serializable) listimg);
                ActivityCollector.getActivityCollector().toOtherActivity(ShowPhotoActivity.class, bundle, Const.RequestCode.CLEAR_PIC);
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

    @OnClick({R.id.iv_back_personhome, R.id.tv_data_personhome, R.id.tv_find_personhome, R.id.iv_data_personhome,
            R.id.tv_room_personhome,  R.id.rl_fans_personhome,R.id.tv_photo,
            R.id.rl_attention_personhome,R.id.rl_chat_personhome,
            R.id.rl_addattention_personhome, R.id.rl_signers_personhome, R.id.ll_room_personhome,R.id.tv_nomal_gift,R.id.tv_packet_gift})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_back_personhome:
                ActivityCollector.getActivityCollector().finishActivity();
                break;
            case R.id.iv_data_personhome:
                showMybottomDialog(bottomList);
                break;
            case R.id.tv_data_personhome:
                ActivityCollector.getActivityCollector().toOtherActivity(PersonDataActivity.class, Const.RequestCode.DATA_CHANGE);
                break;
            case R.id.tv_find_personhome://去找她
                if (RoomId.equals(roomId)) {
                    showToast("您已在该房间");
                    return;
                }
                bundle.putString(Const.ShowIntent.ROOMID, otherRoomId);
                ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
//                getFindCall();
                break;
            case R.id.tv_room_personhome:
                if (RoomId.equals(roomId)) {
                    showToast("您已在该房间");
                    return;
                }
                bundle.putString(Const.ShowIntent.ROOMID, roomId);
                ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                ActivityCollector.getActivityCollector().finishActivity(AllMsgActivity.class);
                ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
                break;
            case R.id.rl_attention_personhome:
//                ActivityCollector.getActivityCollector().toOtherActivity(MyAttentionActivity.class);
                break;
            case R.id.rl_fans_personhome:
//                ActivityCollector.getActivityCollector().toOtherActivity(MyFansActivity.class);
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
            case R.id.rl_signers_personhome:
                setVoiceShow();
                break;
            case R.id.ll_room_personhome:
                if (RoomId.equals(roomId)) {
                    showToast("您已在该房间");
                    return;
                }
                bundle.putString(Const.ShowIntent.ROOMID, otherRoomId);
                ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                ActivityCollector.getActivityCollector().finishActivity(OtherHomeActivity.class);
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

    int voiceTimeadd;
    int voiceTime;
    Timer timer;

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
            showToast("无效的声音签名");
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


    private void showMyDialog(int showType, View.OnClickListener onClickListener) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(OtherHomeActivity.this);
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

    private void showMybottomDialog(ArrayList<String> bottomList) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(OtherHomeActivity.this, bottomList);
        myBottomShowDialog.show();
        BottomShowRecyclerAdapter adapter = myBottomShowDialog.getAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
                switch (position) {
                    case 0:
                        ArrayList<String> bottomListJu = new ArrayList<>();
                        bottomListJu.add(getString(R.string.tv_sensitivity_report));
                        bottomListJu.add(getString(R.string.tv_se_report));
                        bottomListJu.add(getString(R.string.tv_ad_report));
                        bottomListJu.add(getString(R.string.tv_at_report));
                        bottomListJu.add(getString(R.string.tv_cancel));
                        showMybottomDialogJu(bottomListJu);
                        break;
                    case 1:
                        showMyDialog(Const.IntShow.TWO, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                getHeiCall();
                            }
                        });

                        break;
                }
            }
        });
    }

    /**
     * 加入黑名单
     */
    private void getHeiCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", userId);
        HttpManager.getInstance().post(Api.getAddUserblock, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("拉黑成功，您可以在设置中将Ta移除黑名单");
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    private void showMybottomDialogJu(ArrayList<String> bottomList) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(OtherHomeActivity.this, bottomList);
        myBottomShowDialog.show();
        BottomShowRecyclerAdapter adapter = myBottomShowDialog.getAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
                    myBottomShowDialog.dismiss();
                }
                switch (position) {
                    case 4:

                        break;
                    default:
                        String updateString = (String) adapter.getItem(position);
                        updateCall(updateString);
                        break;
                }
            }
        });
    }

    //举报人
    private void updateCall(String updateString) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("type", Const.IntShow.ONE);
        map.put("buid", userId);
        map.put("content", updateString);
        HttpManager.getInstance().post(Api.ReportSave, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showToast("举报成功，我们会尽快为您处理");
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    /**
     * 去找他
     */
    private void getFindCall() {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", userId);
        HttpManager.getInstance().post(Api.UserRoom, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                GetOneBean getOneBean = JSON.parseObject(responseString, GetOneBean.class);
                if (getOneBean.getCode() == Api.SUCCESS) {
                    if (StringUtils.isEmpty(getOneBean.getData().getRid())) {
                        showToast("该用户不在别人房间");
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(Const.ShowIntent.ROOMID, getOneBean.getData().getRid());
                        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                    }
                } else {
                    showToast(getOneBean.getMsg());
                }
            }
        });
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
}
