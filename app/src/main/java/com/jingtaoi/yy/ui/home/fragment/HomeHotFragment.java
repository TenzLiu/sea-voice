package com.jingtaoi.yy.ui.home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orient.tea.barragephoto.adapter.AdapterListener;
import com.orient.tea.barragephoto.adapter.BarrageAdapter;
import com.orient.tea.barragephoto.ui.BarrageView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.AllmsgBean;
import com.jingtaoi.yy.bean.BannerListBean;
import com.jingtaoi.yy.bean.ChatRoomMsgBean;
import com.jingtaoi.yy.bean.HomeItemData;
import com.jingtaoi.yy.bean.HomeRankBean;
import com.jingtaoi.yy.bean.HomeTuijianBean;
import com.jingtaoi.yy.bean.RankBean;
import com.jingtaoi.yy.model.ChatRoomMsgModel;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.home.BarrageData;
import com.jingtaoi.yy.ui.home.RadioDatingOneActivity;
import com.jingtaoi.yy.ui.home.RankActivity;
import com.jingtaoi.yy.ui.home.RankActivityActivity;
import com.jingtaoi.yy.ui.home.RoomListActivity;
import com.jingtaoi.yy.ui.home.SignUpActivity;
import com.jingtaoi.yy.ui.home.adapter.HomeZBAdapter;
import com.jingtaoi.yy.ui.home.adapter.HotHeaderAdapter;
import com.jingtaoi.yy.ui.other.WebActivity;
import com.jingtaoi.yy.ui.room.AllMsgActivity;
import com.jingtaoi.yy.ui.room.VoiceActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.ImageUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.jingtaoi.yy.utils.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 首页热门页面
 */

public class HomeHotFragment extends MyBaseFragment {
    @BindView(R.id.cl_banner)
    ConstraintLayout clBanner;
    @BindView(R.id.banner)
    ViewPager banner;
    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;
    @BindView(R.id.mRecyclerView_header_hot)
    RecyclerView mRecyclerViewHeaderHot;
    @BindView(R.id.mRecyclerView_hot)
    RecyclerView mRecyclerViewHot;
    @BindView(R.id.mSwipeRefreshLayout_homehot)
    SwipeRefreshLayout mSwipeRefreshLayoutHomehot;
    @BindView(R.id.dan_mu_rl)
    RelativeLayout dan_mu_rl;
    @BindView(R.id.iv_rank_1)
    ImageView iv_1;
    @BindView(R.id.iv_rank_2)
    ImageView iv_2;
    @BindView(R.id.iv_rank_3)
    ImageView iv_3;
    @BindView(R.id.iv_rank_4)
    ImageView iv_4;
    @BindView(R.id.iv_rank_5)
    ImageView iv_5;
    @BindView(R.id.iv_rank_6)
    ImageView iv_6;


    Unbinder unbinder;

    HotHeaderAdapter headerAdapter;
    HomeZBAdapter hotAdapter;
    @BindView(R.id.iv_crown)
    ImageView ivCrown;
    @BindView(R.id.iv_crown_22)
    ImageView ivCrown22;
    @BindView(R.id.iv_crown_3)
    ImageView ivCrown3;
    @BindView(R.id.iv_crown_2)
    ImageView ivCrown2;
    @BindView(R.id.iv_crown_5)
    ImageView ivCrown5;
    @BindView(R.id.iv_crown_6)
    ImageView ivCrown6;

    private boolean isFirst = true;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (mAdapter != null && dmDataList.size() > 0) {
                    mAdapter.addList(dmDataList);
                }
            }
            return false;
        }
    });
    private Boolean isDestroyed = false;
    private int itemPosition = 0;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_homehot, container, false);
    }

    private final int VIDEO_DURATION = 100 * 1000;

    @Override
    public void initView() {

        setHeaderRecycler();
        setHotRecycler();
        mSwipeRefreshLayoutHomehot.post(new Runnable() {
            @Override
            public void run() {
//                getHanHuaCall();

                getRankData();
                getHeaderData();
                page = 1;
                getData();
                hotAdapter.setEnableLoadMore(false);
            }
        });
        getBanner();

        initBarrage();

        //进入公聊大厅
        dan_mu_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.getActivityCollector().toOtherActivity(RadioDatingOneActivity.class);
            }
        });
    }

    private BarrageView barrageView;
    private BarrageAdapter<BarrageData> mAdapter;

    private void initBarrage() {
        barrageView = findViewById(R.id.barrage);
    }

    class ViewHolder extends BarrageAdapter.BarrageViewHolder<BarrageData> {

        private ImageView mHeadView;
        private TextView mContent;

        ViewHolder(View itemView) {
            super(itemView);

            mHeadView = itemView.findViewById(R.id.image);
            mContent = itemView.findViewById(R.id.content);
        }

        @Override
        protected void onBind(BarrageData data) {
            Glide.with(getActivity()).load(data.getImgTx())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mHeadView);
            mContent.setText(data.getContent());
        }
    }


    List<BarrageData> dmDataList = new LinkedList<>();


    /*
     * 全国喊话
     */
    private void getHanHuaCall() {

        if (mAdapter != null)
            mAdapter.destroy();
        dmDataList.clear();
        BarrageView.Options options = new BarrageView.Options()
                .setGravity(BarrageView.GRAVITY_FULL)                // 设置弹幕的位置
                .setInterval(75)
                .setModel(BarrageView.MODEL_RANDOM)     // 设置弹幕生成模式
                .setRepeat(-1)// 循环播放 默认为1次 -1 为无限循环// 设置弹幕的发送间隔
                .setClick(true);                                     // 设置弹幕是否可以点击
        barrageView.setOptions(options);
        barrageView.setAdapter(mAdapter = new BarrageAdapter<BarrageData>(null, getContext()) {
            @Override
            public BarrageViewHolder<BarrageData> onCreateViewHolder(View root, int type) {
                switch (type) {
                    case R.layout.barrage_item_normal:
                        return new ViewHolder(root);
                    case R.layout.barrage_item_normal_gl:
                        return new ViewHolder(root);

                    default:
                        return new ViewHolder(root);

                }

            }

            @Override
            public int getItemLayout(BarrageData barrageData) {
                switch (barrageData.getType()) {
                    case 0:
                        return R.layout.barrage_item_normal;
                    case 1:
                        return R.layout.barrage_item_normal_gl;
                    default:
                        return R.layout.barrage_item_normal;
                }
            }
        });

        // 设置监听器
        mAdapter.setAdapterListener(new AdapterListener<BarrageData>() {
            @Override
            public void onItemClick(BarrageAdapter.BarrageViewHolder<BarrageData> holder, BarrageData item) {
                if (item.getType() == 0) {
                    Bundle bundle = new Bundle();
                    ActivityCollector.getActivityCollector().finishActivity(VoiceActivity.class);
                    bundle.putString(Const.ShowIntent.ROOMID, item.getRid());
                    ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
                    ActivityCollector.getActivityCollector().finishActivity(AllMsgActivity.class);
                } else {
                    ActivityCollector.getActivityCollector().toOtherActivity(RadioDatingOneActivity.class);

                }

            }
        });
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.QBgetScreen, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                AllmsgBean allmsgBean = JSON.parseObject(responseString, AllmsgBean.class);

                if (allmsgBean.getData() != null && allmsgBean.getData().size() > 0) {
                    List<AllmsgBean.DataEntity> data = allmsgBean.getData();
                    Collections.reverse(data);//对列表倒叙
                    int intdex = data.size() > 5 ? 5 : data.size();
                    for (int i = 0; i < intdex; i++) {
                        String imgTx = data.get(i).getImgTx();
                        dmDataList.add(new BarrageData(data.get(i).getContent(), 0, i, imgTx, data.get(i).getRid()));
                    }

                }

                getCall();

                //一分钟更新一次数据
                handler.postDelayed(runnableDM, 1000 * 20);
            }
        });
    }

    Runnable runnableDM = new Runnable() {
        @Override
        public void run() {
//            getHanHuaCall();
        }
    };

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.BroadList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                ChatRoomMsgBean chatRoomMsgBean = JSON.parseObject(responseString, ChatRoomMsgBean.class);
                if (chatRoomMsgBean.getCode() == Api.SUCCESS) {
                    List<ChatRoomMsgModel.DataBean> data = chatRoomMsgBean.getData();
//                    Collections.reverse(data);//对列表倒叙
                    if (data != null && data.size() > 0) {
                        int intdex = data.size() > 20 ? 20 : data.size();
                        for (int i = 0; i < intdex; i++) {
                            String imgTx = data.get(i).getHeader();
                            dmDataList.add(new BarrageData(data.get(i).getMessageShow(), 1, i, imgTx, ""));
                        }
                    }
                } else {
                    showToast(chatRoomMsgBean.getMsg());
                }

                handler.sendEmptyMessage(1);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

//        barrageView.destroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void getBanner() {
        banner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                itemPosition = position;
            }
        });
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("type", Const.IntShow.ONE);
        HttpManager.getInstance().post(Api.Banner, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BannerListBean baseBean = JSON.parseObject(responseString, BannerListBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    List<BannerListBean.DataBean> data = baseBean.getData();
                    if (data != null && !data.isEmpty()) {
                        clBanner.setVisibility(View.VISIBLE);
                        if (data.size() == 1) {
                            data.add(data.get(0));
                        }
                        ArrayList<BannerFragment> fragments = new ArrayList<>();
                        for (BannerListBean.DataBean item : data) {
                            BannerFragment fragment = BannerFragment.newInstance(item.getImgUrl());
                            fragment.setListener(() -> {
                                clickBanner(item.getId());
                                switch (item.getUrlType()) {
                                    case 2: {
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse(item.getUrlHtml());
                                        intent.setData(content_url);
                                        startActivity(intent);
                                        break;
                                    }
                                    case 3: {
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Const.ShowIntent.URL, item.getUrlHtml());
                                        bundle.putString(Const.ShowIntent.TITLE, item.getTitle());
                                        ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
                                        break;
                                    }
                                    case 4: {
                                        gotoRoom(item.getUrlHtml());
                                        break;
                                    }
                                    case 5: {
                                        ActivityCollector.getActivityCollector().toOtherActivity(RankActivityActivity.class);
                                        break;
                                    }
                                }
                            });
                            fragments.add(fragment);
                        }
                        banner.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                            @Override
                            public Fragment getItem(int i) {
                                return fragments.get(i);
                            }

                            @Override
                            public int getCount() {
                                return fragments.size();
                            }
                        });
                        ViewPagerIndicator viewPagerIndicator = new ViewPagerIndicator(getActivity(), llIndicator, R.drawable.indicator_normal, R.drawable.indicator_select, fragments.size());
                        viewPagerIndicator.setupWithViewPager(banner);
                        if (isFirst)
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isDestroyed)
                                        try {
                                            itemPosition++;
                                            handler.postDelayed(this, 3000);
                                            banner.setCurrentItem(itemPosition % fragments.size());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                }
                            }, 3000);
                        isFirst = false;
                    }
                } else {
                    clBanner.setVisibility(View.GONE);
                    showToast(baseBean.getMsg());
                }
            }
        });
    }


    /**
     * banner点击调用
     *
     * @param id
     */
    private void clickBanner(int id) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", id);
        HttpManager.getInstance().post(Api.Banner_Click, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {

            }
        });
    }

    private void getData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", 4);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.HOME_DATA, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayoutHomehot != null && mSwipeRefreshLayoutHomehot.isRefreshing()) {
                    mSwipeRefreshLayoutHomehot.setRefreshing(false);
                    hotAdapter.setEnableLoadMore(true);
                }
                HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {

                    setData(baseBean.getData(), hotAdapter);
                } else {
                    showToast(baseBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                if (mSwipeRefreshLayoutHomehot != null && mSwipeRefreshLayoutHomehot.isRefreshing()) {
                    mSwipeRefreshLayoutHomehot.setRefreshing(false);
                    hotAdapter.setEnableLoadMore(true);
                }
            }
        });
    }

    List<HomeItemData> listData = new ArrayList<>();

    private void setData(List<HomeItemData> data, HomeZBAdapter adapter) {

        final int size = data == null ? 0 : data.size();
        if (page == 1) {
            listData.clear();
            listData.addAll(data);
            handlData();
            adapter.setNewData(listData);
        } else {
            if (size > 0) {

                listData.addAll(data);
                handlData();
//                if (listData.size() > 24) {
//                    List<HomeItemData> list = new ArrayList<>();
//                    for (int i = 0; i < 24; i++) {
//                        list.add(listData.get(i));
//                    }
//                    listData.clear();
//                    listData.addAll(list);
//                }

                adapter.notifyDataSetChanged();
            } else {
                page--;
            }
        }
        if (size < PAGE_SIZE) {
            if (page == 1) { //GridLayoutManager不显示加载更多
                adapter.loadMoreEnd(true);
            } else {
                adapter.loadMoreEnd(true);
            }
        } else {
            adapter.loadMoreComplete();
        }
    }


    public void handlData() {
        for (int i = 0; i < listData.size(); i++) {
            listData.get(i).setItemType(HomeItemData.TYPE_ITEM2);
        }
    }

    List<HomeItemData> dataHot = new ArrayList();

    private void getHeaderData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.HOME_TUIJIAN, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayoutHomehot != null && mSwipeRefreshLayoutHomehot.isRefreshing()) {
                    mSwipeRefreshLayoutHomehot.setRefreshing(false);
                }
                HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    List<HomeItemData> data = baseBean.getData();
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
//                            if (i == 0 || i == 1 || i == 2) {
                            data.get(i).setItemType(HomeItemData.TYPE_ITEM1);
//                            }
                        }
                        dataHot.clear();
                        dataHot.addAll(data);
                        headerAdapter.setNewData(data);
                        headerAdapter.loadMoreEnd(true);


//                        addView(data);
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                if (mSwipeRefreshLayoutHomehot != null && mSwipeRefreshLayoutHomehot.isRefreshing()) {
                    mSwipeRefreshLayoutHomehot.setRefreshing(false);
                }
            }
        });
    }

    /**
     * 首页排行榜预览
     */
    private void getRankData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.HOT_RANK, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {

                HomeRankBean baseBean = JSON.parseObject(responseString, HomeRankBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    iv_1.setVisibility(View.INVISIBLE);
                    iv_2.setVisibility(View.INVISIBLE);
                    iv_3.setVisibility(View.INVISIBLE);
                    iv_4.setVisibility(View.INVISIBLE);
                    iv_5.setVisibility(View.INVISIBLE);
                    iv_6.setVisibility(View.INVISIBLE);
                    ivCrown.setVisibility(View.INVISIBLE);
                    ivCrown22.setVisibility(View.INVISIBLE);
                    ivCrown3.setVisibility(View.INVISIBLE);
                    ivCrown2.setVisibility(View.INVISIBLE);
                    ivCrown5.setVisibility(View.INVISIBLE);
                    ivCrown6.setVisibility(View.INVISIBLE);
                    HomeRankBean.HotRankData data = baseBean.getData();
                    if (data != null) {
                        List<RankBean.DataBean> list = data.list;
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                if (i == 0) {
                                    iv_1.setVisibility(View.VISIBLE);
                                    ivCrown.setVisibility(View.VISIBLE);
                                    ImageUtils.loadImage(iv_1,list.get(0).getImgTx(),-1,-1);
                                } else if (i == 1){
                                    iv_2.setVisibility(View.VISIBLE);
                                    ivCrown22.setVisibility(View.VISIBLE);
                                    ImageUtils.loadImage(iv_2,list.get(1).getImgTx(),-1,-1);
                                }
                                else if (i == 2){
                                    ImageUtils.loadImage(iv_3,list.get(2).getImgTx(),-1,-1);
                                    iv_3.setVisibility(View.VISIBLE);
                                    ivCrown3.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        List<RankBean.DataBean> roomList = data.roomList;
                        if (roomList != null) {
                            for (int i = 0; i < roomList.size(); i++) {
                                if (i == 0) {
                                    iv_4.setVisibility(View.VISIBLE);
                                    ivCrown2.setVisibility(View.VISIBLE);
                                    ImageUtils.loadImage(iv_4,roomList.get(0).getImgTx(),-1,-1);
                                } else if (i == 1){
                                    iv_5.setVisibility(View.VISIBLE);
                                    ivCrown5.setVisibility(View.VISIBLE);
                                    ImageUtils.loadImage(iv_5,roomList.get(1).getImgTx(),-1,-1);
                                }
                                else if (i == 2){
                                    ImageUtils.loadImage(iv_6,roomList.get(2).getImgTx(),-1,-1);
                                    iv_6.setVisibility(View.VISIBLE);
                                    ivCrown6.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
    }


    private void gotoRoom(String roomId) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ROOMID, roomId);
        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
    }

    private void setHotRecycler() {
        hotAdapter = new HomeZBAdapter(listData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        mRecyclerViewHot.setLayoutManager(layoutManager);
        mRecyclerViewHot.setAdapter(hotAdapter);

        hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeItemData homeItemData = (HomeItemData) adapter.getItem(position);
                assert homeItemData != null;
                gotoRoom(homeItemData.getUsercoding());
            }
        });

        hotAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getData();
            }
        }, mRecyclerViewHot);
    }

    private void setHeaderRecycler() {
        headerAdapter = new HotHeaderAdapter(dataHot);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerViewHeaderHot.setLayoutManager(layoutManager);

//        headerAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
//                if (0 == position || 1 == position || 2 == position) {
//                    return 2;
//                } else {
//                    return 3;
//                }
//            }
//        });

        mRecyclerViewHeaderHot.setAdapter(headerAdapter);

        headerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeItemData homeItemData = (HomeItemData) adapter.getItem(position);
                assert homeItemData != null;
                gotoRoom(homeItemData.getUsercoding());
            }
        });

        mSwipeRefreshLayoutHomehot.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHeaderData();
                getRankData();
                page = 1;
                getData();
                hotAdapter.setEnableLoadMore(false);
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
        handler.removeCallbacks(runnableDM);
    }

    @OnClick({R.id.phb_rl, R.id.fjb_rl})
//    @OnClick({R.id.phb_rl, R.id.tv_sign, R.id.tv_suggest, R.id.tv_my_room})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //排行榜
            case R.id.phb_rl:
                startActivity(new Intent(getActivity(), RankActivity.class));
                break;
            case R.id.fjb_rl:
                //房间榜
                startActivity(new Intent(getActivity(), RoomListActivity.class));
                break;
            case R.id.tv_sign:
                //签到
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                break;
            case R.id.tv_suggest:
                break;
            case R.id.tv_my_room:
                //我的房间
                String roomId = (String) SharedPreferenceUtils.get(getContext(), Const.User.ROOMID, "");
                gotoRoom(roomId);
                break;
        }
    }


}
