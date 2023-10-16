//package com.bxtech.yoyoyuyin.ui.home;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.constraint.Group;
//import android.support.constraint.Guideline;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import androidx.viewpager.widget.ViewPager;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.bxtech.yoyoyuyin.R;
//import com.bxtech.yoyoyuyin.base.MyBaseFragment;
//import com.bxtech.yoyoyuyin.bean.BannerListBean;
//import com.bxtech.yoyoyuyin.bean.HomeItemData;
//import com.bxtech.yoyoyuyin.bean.HomeTuijianBean;
//import com.bxtech.yoyoyuyin.netUtls.Api;
//import com.bxtech.yoyoyuyin.netUtls.HttpManager;
//import com.bxtech.yoyoyuyin.netUtls.MyObserver;
//import com.bxtech.yoyoyuyin.ui.home.adapter.HotAdapter;
//import com.bxtech.yoyoyuyin.ui.home.fragment.BannerFragment;
//
//import com.bxtech.yoyoyuyin.ui.other.WebActivity;
//import com.bxtech.yoyoyuyin.ui.room.VoiceActivity;
//import com.bxtech.yoyoyuyin.utils.ActivityCollector;
//import com.bxtech.yoyoyuyin.utils.Const;
//import com.bxtech.yoyoyuyin.utils.SharedPreferenceUtils;
//
//import com.bxtech.yoyoyuyin.utils.ViewPagerIndicator;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.controller.AbstractDraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
//import com.facebook.imagepipeline.request.ImageRequest;
//import com.facebook.imagepipeline.request.ImageRequestBuilder;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;
//
////import com.cityrise.uuvoice.ui.home.ReceivePacketActivity;
//
///**
// * 首页热门fragment（弃用）
// */
//public class HotHomeFragment extends MyBaseFragment {
//    @BindView(R.id.mSwipeRefreshLayout)
//    SwipeRefreshRecyclerLayout mSwipeRefreshLayout;
//    Unbinder unbinder;
//
//
//    @Override
//    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_hothome, container, false);
//        return view;
//    }
//
//    private HotAdapter adapter;
//    private View headerView;
//    private ArrayList<HomeItemData> datas = new ArrayList<HomeItemData>();
//
//    @Override
//    public void initView() {
//        mSwipeRefreshLayout.setMode(SwipeRefreshRecyclerLayout.Mode.Both);
//        mSwipeRefreshLayout.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        adapter = new HotAdapter(datas, true);
//        adapter.setOnItemClickListener(((view, position) ->
//                gotoRoom(datas.get(position).getUsercoding())));
//        mSwipeRefreshLayout.setAdapter(adapter);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshRecyclerLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                page = 1;
////                getBanner();
//                getHeaderData();
//                getData();
//            }
//
//            @Override
//            public void onLoadMore() {
//                page++;
//                getData();
//            }
//        });
//        initHeader();
//        getData();
//        getBanner();
//        getHeaderData();
//    }
//
//    private ViewHolder holder;
//    Handler handler = new Handler();
//    private boolean isFirst = true;
//
//    private void initHeader() {
//        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_hot, mSwipeRefreshLayout, false);
//        holder = new ViewHolder(headerView);
//        holder.banner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                itemPosition = position;
//            }
//        });
//        holder.tvRank.setOnClickListener(v ->
//                startActivity(new Intent(getActivity(), RankActivity.class)));
//        holder.tvSuggest.setOnClickListener(v ->
//                startActivity(new Intent(getActivity(), RecommendActivity.class)));
//        holder.tvSign.setOnClickListener(v ->
//                startActivity(new Intent(getActivity(), SignUpActivity.class)));
//        holder.tvMyRoom.setOnClickListener(v -> {
//            String roomId = (String) SharedPreferenceUtils.get(getContext(), Const.User.ROOMID, "");
//            gotoRoom(roomId);
//        });
//        adapter.setHeaderView(headerView);
//    }
//
//    private void gotoRoom(String roomId) {
//        Bundle bundle = new Bundle();
//        bundle.putString(Const.ShowIntent.ROOMID, roomId);
//        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//    }
//
//    private void showUrlBlur(SimpleDraweeView draweeView, String url) {
//        try {
//            Uri uri = Uri.parse(url);
//            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                    .setPostprocessor(new IterativeBoxBlurPostProcessor(10, 20))
//                    .build();
//            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setOldController(draweeView.getController())
//                    .setImageRequest(request)
//                    .build();
//            draweeView.setController(controller);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void setResume() {
//    }
//
//    @Override
//    public void setOnclick() {
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        unbinder = ButterKnife.bind(this, rootView);
//        return rootView;
//    }
//
//    private Boolean isDestroyed = false;
//    private int itemPosition = 0;
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        isDestroyed = true;
//        unbinder.unbind();
//    }
//
//
//    private void getBanner() {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        map.put("type", Const.IntShow.ONE);
//        HttpManager.getInstance().post(Api.Banner, map, new MyObserver(this) {
//            @Override
//            public void success(String responseString) {
//                BannerListBean baseBean = JSON.parseObject(responseString, BannerListBean.class);
//                if (baseBean.getCode() == Api.SUCCESS) {
//                    List<BannerListBean.DataBean> data = baseBean.getData();
//                    if (data != null && !data.isEmpty()) {
//                        if (data.size() == 1) {
//                            data.add(data.get(0));
//                        }
//                        ArrayList<BannerFragment> fragments = new ArrayList<>();
//                        for (BannerListBean.DataBean item : data) {
//                            BannerFragment fragment = BannerFragment.newInstance(item.getImgUrl());
//                            fragment.setListener(() -> {
//                                clickBanner(item.getId());
//                                switch (item.getUrlType()) {
//                                    case 2: {
//                                        Intent intent = new Intent();
//                                        intent.setAction("android.intent.action.VIEW");
//                                        Uri content_url = Uri.parse(item.getUrlHtml());
//                                        intent.setData(content_url);
//                                        startActivity(intent);
//                                        break;
//                                    }
//                                    case 3: {
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString(Const.ShowIntent.URL, item.getImgUrl());
//                                        bundle.putString(Const.ShowIntent.TITLE, item.getTitle());
//                                        ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
//                                        break;
//                                    }
//                                    case 4: {
//                                        gotoRoom(item.getUrlHtml());
//                                        break;
//                                    }
//                                }
//                            });
//                            fragments.add(fragment);
//                        }
//                        holder.banner.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
//                            @Override
//                            public Fragment getItem(int i) {
//                                return fragments.get(i);
//                            }
//
//                            @Override
//                            public int getCount() {
//                                return fragments.size();
//                            }
//                        });
//                        ViewPagerIndicator viewPagerIndicator = new ViewPagerIndicator(getActivity(), holder.llIndicator, R.drawable.indicator_normal, R.drawable.indicator_select, fragments.size());
//                        viewPagerIndicator.setupWithViewPager(holder.banner);
//                        if (isFirst)
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (!isDestroyed)
//                                        try {
//                                            itemPosition++;
//                                            handler.postDelayed(this, 3000);
//                                            holder.banner.setCurrentItem(itemPosition % fragments.size());
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                }
//                            }, 3000);
//                        isFirst = false;
//                    }
//                } else {
//                    showToast(baseBean.getMsg());
//                }
//            }
//        });
//    }
//
//    /**
//     * banner点击调用
//     *
//     * @param id
//     */
//    private void clickBanner(int id) {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        map.put("id", id);
//        HttpManager.getInstance().post(Api.Banner_Click, map, new MyObserver(this) {
//            @Override
//            public void success(String responseString) {
//
//            }
//        });
//    }
//
//    private void getHeaderData() {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        HttpManager.getInstance().post(Api.HOME_TUIJIAN, map, new MyObserver(this) {
//            @Override
//            public void success(String responseString) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
//                if (baseBean.getCode() == Api.SUCCESS) {
//                    List<HomeItemData> data = baseBean.getData();
//                    if (data == null || data.isEmpty()) {
//                        holder.group1.setVisibility(View.GONE);
//                        holder.group2.setVisibility(View.GONE);
//                    } else if (data.size() == 1) {
//                        holder.group1.setVisibility(View.VISIBLE);
//                        holder.group2.setVisibility(View.GONE);
//                        holder.ivHead1.setImageURI(data.get(0).getImg());
//                        showUrlBlur(holder.seatBg1, data.get(0).getImg());
//                        holder.tvTitle1.setText(data.get(0).getRoomName());
//                        holder.seatBg1.setOnClickListener(v ->
//                                gotoRoom(data.get(0).getUsercoding()));
//                        holder.tvCount1.setText("房间人数：" + data.get(0).getNum());
//                    } else {
//                        holder.group1.setVisibility(View.VISIBLE);
//                        holder.group2.setVisibility(View.VISIBLE);
//                        holder.ivHead1.setImageURI(data.get(0).getImg());
//                        showUrlBlur(holder.seatBg1, data.get(0).getImg());
//                        holder.tvTitle1.setText(data.get(0).getRoomName());
//                        holder.seatBg1.setOnClickListener(v ->
//                                gotoRoom(data.get(0).getUsercoding()));
//                        showUrlBlur(holder.seatBg2, data.get(1).getImg());
//                        holder.tvTitle2.setText(data.get(1).getRoomName());
//                        holder.seatBg2.setOnClickListener(v ->
//                                gotoRoom(data.get(1).getUsercoding()));
//                        holder.tvCount1.setText("房间人数：" + data.get(0).getNum());
//                        holder.ivHead2.setImageURI(data.get(1).getImg());
//                        showUrlBlur(holder.seatBg2, data.get(1).getImg());
//                        holder.tvCount2.setText("房间人数：" + data.get(1).getNum());
//                        List<HomeItemData> sub = data.subList(2, data.size());
//                        holder.ll_hot.removeAllViews();
//                        for (int j = 0; j < sub.size(); j++) {
//                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_hot, null);
//                            ((SimpleDraweeView) view.findViewById(R.id.iv_head)).setImageURI(sub.get(j).getImg());
//                            ((TextView) view.findViewById(R.id.tv_title)).setText(sub.get(j).getRoomName());
//                            ((TextView) view.findViewById(R.id.tv_count)).setText(String.format(Locale.CHINA, "%d人在线", sub.get(j).getNum()));
//                            TextView tag = view.findViewById(R.id.tv_tag);
//                            tag.setText(sub.get(j).getRoomLabel());
//                            int finalJ = j;
//                            view.setOnClickListener(v -> {
//                                gotoRoom(sub.get(finalJ).getUsercoding());
//                            });
//                            switch (j % 5) {
//                                case 0: {
//                                    tag.setBackgroundResource(R.drawable.bg_tag_blue);
//                                    break;
//                                }
//                                case 1: {
//                                    tag.setBackgroundResource(R.drawable.bg_tag_green);
//                                    break;
//                                }
//                                case 2: {
//                                    tag.setBackgroundResource(R.drawable.bg_tag_red);
//                                    break;
//                                }
//                                case 3: {
//                                    tag.setBackgroundResource(R.drawable.bg_tag_orange);
//                                    break;
//                                }
//                                case 4: {
//                                    tag.setBackgroundResource(R.drawable.bg_tag_pink);
//                                    break;
//                                }
//                            }
//                            holder.ll_hot.addView(view);
//                        }
//                    }
//                } else {
//                    showToast(baseBean.getMsg());
//                }
//            }
//        });
//    }
//
//    private void getData() {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        map.put("state", 1);
//        map.put("pageSize", PAGE_SIZE);
//        map.put("pageNum", page);
//        HttpManager.getInstance().post(Api.HOME_DATA, map, new MyObserver(this) {
//            @Override
//            public void success(String responseString) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
//                if (baseBean.getCode() == Api.SUCCESS) {
//                    List<HomeItemData> data = baseBean.getData();
//                    if (page == 1)
//                        datas.clear();
//                    datas.addAll(data);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    showToast(baseBean.getMsg());
//                }
//            }
//        });
//    }
//
//    class ViewHolder {
//        @BindView(R.id.banner)
//        ViewPager banner;
//        @BindView(R.id.ll_indicator)
//        LinearLayout llIndicator;
//        @BindView(R.id.ll_hot)
//        LinearLayout ll_hot;
//        @BindView(R.id.tv_rank)
//        TextView tvRank;
//        @BindView(R.id.tv_sign)
//        TextView tvSign;
//        @BindView(R.id.tv_suggest)
//        TextView tvSuggest;
//        @BindView(R.id.tv_my_room)
//        TextView tvMyRoom;
//        @BindView(R.id.tv_title)
//        TextView tvTitle;
//        @BindView(R.id.center)
//        Guideline center;
//        @BindView(R.id.seat_bg_1)
//        SimpleDraweeView seatBg1;
//        @BindView(R.id.tv_title_1)
//        TextView tvTitle1;
//        @BindView(R.id.tv_count_1)
//        TextView tvCount1;
//        @BindView(R.id.tv_count_2)
//        TextView tvCount2;
//        @BindView(R.id.iv_head_1)
//        SimpleDraweeView ivHead1;
//        @BindView(R.id.seat_bg_2)
//        SimpleDraweeView seatBg2;
//        @BindView(R.id.tv_title_2)
//        TextView tvTitle2;
//        @BindView(R.id.iv_head_2)
//        SimpleDraweeView ivHead2;
//        @BindView(R.id.group_1)
//        Group group1;
//        @BindView(R.id.group_2)
//        Group group2;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}
