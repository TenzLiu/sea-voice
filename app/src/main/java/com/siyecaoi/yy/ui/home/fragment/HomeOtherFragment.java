//package com.bxtech.yoyoyuyin.ui.home.fragment;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import androidx.viewpager.widget.ViewPager;
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
//import com.bxtech.yoyoyuyin.base.MyApplication;
//import com.bxtech.yoyoyuyin.base.MyBaseFragment;
//import com.bxtech.yoyoyuyin.bean.BannerListBean;
//import com.bxtech.yoyoyuyin.bean.HomeItemData;
//import com.bxtech.yoyoyuyin.bean.HomeTuijianBean;
//import com.bxtech.yoyoyuyin.netUtls.Api;
//import com.bxtech.yoyoyuyin.netUtls.HttpManager;
//import com.bxtech.yoyoyuyin.netUtls.MyObserver;
//import com.bxtech.yoyoyuyin.ui.home.adapter.HotAdapter;
//import com.bxtech.yoyoyuyin.ui.home.adapter.SameCityAdapter;
//import com.bxtech.yoyoyuyin.ui.home.fragment.BannerFragment;
//import com.bxtech.yoyoyuyin.ui.message.OtherHomeActivity;
//import com.bxtech.yoyoyuyin.ui.other.WebActivity;
//import com.bxtech.yoyoyuyin.ui.room.VoiceActivity;
//import com.bxtech.yoyoyuyin.utils.ActivityCollector;
//import com.bxtech.yoyoyuyin.utils.Const;
//import com.bxtech.yoyoyuyin.utils.SharedPreferenceUtils;
//import com.bxtech.yoyoyuyin.utils.ViewPagerIndicator;
//import com.facebook.drawee.view.SimpleDraweeView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//import cn.sinata.xldutils.adapter.HFRecyclerAdapter;
//import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;
//
//
///**
// * 首页其他分类页面
// */
//public class HomeOtherFragment extends MyBaseFragment {
//    Unbinder unbinder;
//    @BindView(R.id.mSwipeRefreshLayout)
//    SwipeRefreshRecyclerLayout mSwipeRefreshLayout;
//
//    @Override
//    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_base_recyclerview, container, false);
//        return view;
//    }
//
//    private int type;
//    private HFRecyclerAdapter adapter;
//    private ArrayList<HomeItemData> datas = new ArrayList<>();
//    private ViewHolder viewHolder;
//    private int uid;
//
//    @Override
//    public void initView() {
//        uid = (int) SharedPreferenceUtils.get(getActivity(), Const.User.USER_TOKEN, 0);
//        type = getArguments().getInt(Const.ShowIntent.TYPE, 0);
//        if (type == 7) {
//            mSwipeRefreshLayout.setPadding(0, 8, 20, 0);
//            mSwipeRefreshLayout.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//            adapter = new SameCityAdapter(datas);
//        } else {
//            mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//            adapter = new HotAdapter(datas, false);
//        }
//        adapter.setOnItemClickListener(((view, position) -> {
//            if (type != 7) {
//                Bundle bundle = new Bundle();
//                bundle.putString(Const.ShowIntent.ROOMID, datas.get(position).getUsercoding());
//                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//            } else {
//                Bundle bundle = new Bundle();
//                bundle.putInt(Const.ShowIntent.ID, datas.get(position).getId());
//                ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
//            }
//        }));
//        mSwipeRefreshLayout.setMode(SwipeRefreshRecyclerLayout.Mode.Both);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshRecyclerLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                getData();
//                if (type == 2) {
//                    getBanner(Const.IntShow.THREE);
//                } else if (type == 3) {
//                    getBanner(Const.IntShow.TWO);
//                }
//            }
//
//            @Override
//            public void onLoadMore() {
//                page++;
//                getData();
//            }
//        });
//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_other, mSwipeRefreshLayout, false);
//        viewHolder = new ViewHolder(header);
//        if (type == 2) {
//            adapter.setHeaderView(header);
//            getBanner(Const.IntShow.THREE);
//        } else if (type == 3) {
//            adapter.setHeaderView(header);
//            viewHolder.root.setVisibility(View.GONE);
//            getBanner(Const.IntShow.TWO);
//        } else if (type == 4) {
//            adapter.setHeaderView(header);
//            viewHolder.banner_other.setVisibility(View.GONE);
//        }
//
//        mSwipeRefreshLayout.setAdapter(adapter);
//        getData();
//    }
//
//    private boolean isFirst = true;
//    Handler handler = new Handler();
//    private Boolean isDestroyed = false;
//    private int itemPosition = 0;
//
//    private void getBanner(int typeOne) {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        map.put("type", typeOne);
//        HttpManager.getInstance().post(Api.Banner, map, new MyObserver(this) {
//            @Override
//            public void success(String responseString) {
//                BannerListBean baseBean = JSON.parseObject(responseString, BannerListBean.class);
//                if (baseBean.getCode() == Api.SUCCESS) {
//                    List<BannerListBean.DataBean> data = baseBean.getData();
//                    if (data != null && !data.isEmpty()) {
//                        viewHolder.banner_other.setVisibility(View.VISIBLE);
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
//                        viewHolder.banner.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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
//                        ViewPagerIndicator viewPagerIndicator = new ViewPagerIndicator(getActivity(), viewHolder.llIndicator, R.drawable.indicator_normal, R.drawable.indicator_select, fragments.size());
//                        viewPagerIndicator.setupWithViewPager(viewHolder.banner);
//                        if (isFirst)
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (!isDestroyed)
//                                        try {
//                                            itemPosition++;
//                                            handler.postDelayed(this, 3000);
//                                            viewHolder.banner.setCurrentItem(itemPosition % fragments.size());
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                }
//                            }, 3000);
//                        isFirst = false;
//                    } else {
//                        viewHolder.banner_other.setVisibility(View.GONE);
//                    }
//                } else {
//                    showToast(baseBean.getMsg());
//                }
//            }
//        });
//    }
//
//    private void gotoRoom(String roomId) {
//        Bundle bundle = new Bundle();
//        bundle.putString(Const.ShowIntent.ROOMID, roomId);
//        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
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
//    private void getData() {
//        HashMap<String, Object> map = HttpManager.getInstance().getMap();
//        map.put("pageSize", PAGE_SIZE);
//        map.put("pageNum", page);
//        if (type == 7) {//同城
//            map.put("uid", uid);
//            //todo 定位
//            map.put("lat", MyApplication.lat);
//            map.put("lon", MyApplication.lon);
//            HttpManager.getInstance().post(Api.SAME_CITY, map, new MyObserver(this) {
//                @Override
//                public void success(String responseString) {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
//                    if (page == 1)
//                        datas.clear();
//                    datas.addAll(baseBean.getData());
//                    adapter.notifyDataSetChanged();
//                }
//            });
//
//        } else {
//            map.put("state", type == 8 ? 7 : type);
//            HttpManager.getInstance().post(Api.HOME_DATA, map, new MyObserver(this) {
//                @Override
//                public void success(String responseString) {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
//                    if (baseBean.getCode() == Api.SUCCESS) {
//                        List<HomeItemData> data = baseBean.getData();
//                        if (page == 1)
//                            datas.clear();
//                        if (page == 1 && (type == 2 || type == 4)) { //渲染头部
//                            if (data.isEmpty())
//                                viewHolder.root.setVisibility(View.GONE);
//                            else {
//                                viewHolder.root.setVisibility(View.VISIBLE);
//                                switch (data.size()) {
//                                    case 1: {
//                                        viewHolder.iv1.setImageURI(data.get(0).getImg());
//                                        viewHolder.tv_title_1.setText(data.get(0).getRoomName());
//                                        viewHolder.tvCount1.setText("房间人数：" + data.get(0).getNum());
//                                        viewHolder.iv1.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(0).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.iv2.setVisibility(View.INVISIBLE);
//                                        viewHolder.tv_title_2.setVisibility(View.INVISIBLE);
//                                        viewHolder.tvCount2.setVisibility(View.INVISIBLE);
//                                        viewHolder.iv3.setVisibility(View.INVISIBLE);
//                                        viewHolder.tv_title_3.setVisibility(View.INVISIBLE);
//                                        viewHolder.tvCount3.setVisibility(View.INVISIBLE);
//                                        break;
//                                    }
//                                    case 2: {
//                                        viewHolder.iv1.setImageURI(data.get(0).getImg());
//                                        viewHolder.iv1.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(0).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.tv_title_1.setText(data.get(0).getRoomName());
//                                        viewHolder.tvCount1.setText("房间人数：" + data.get(0).getNum());
//                                        viewHolder.iv2.setVisibility(View.VISIBLE);
//                                        viewHolder.tv_title_2.setVisibility(View.VISIBLE);
//                                        viewHolder.tvCount2.setVisibility(View.VISIBLE);
//                                        viewHolder.iv2.setImageURI(data.get(1).getImg());
//                                        viewHolder.iv2.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(1).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.tv_title_2.setText(data.get(1).getRoomName());
//                                        viewHolder.tvCount2.setText("房间人数：" + data.get(1).getNum());
//                                        viewHolder.iv3.setVisibility(View.INVISIBLE);
//                                        viewHolder.tv_title_3.setVisibility(View.INVISIBLE);
//                                        viewHolder.tvCount3.setVisibility(View.INVISIBLE);
//                                        break;
//                                    }
//                                    case 3: {
//                                        viewHolder.iv1.setImageURI(data.get(0).getImg());
//                                        viewHolder.iv1.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(0).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.tv_title_1.setText(data.get(0).getRoomName());
//                                        viewHolder.tvCount1.setText("房间人数：" + data.get(0).getNum());
//                                        viewHolder.iv2.setVisibility(View.VISIBLE);
//                                        viewHolder.tv_title_2.setVisibility(View.VISIBLE);
//                                        viewHolder.tvCount2.setVisibility(View.VISIBLE);
//                                        viewHolder.iv2.setImageURI(data.get(1).getImg());
//                                        viewHolder.iv2.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(1).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.tv_title_2.setText(data.get(1).getRoomName());
//                                        viewHolder.tvCount2.setText("房间人数：" + data.get(1).getNum());
//                                        viewHolder.iv3.setVisibility(View.VISIBLE);
//                                        viewHolder.tv_title_3.setVisibility(View.VISIBLE);
//                                        viewHolder.tvCount3.setVisibility(View.VISIBLE);
//                                        viewHolder.iv3.setImageURI(data.get(2).getImg());
//                                        viewHolder.iv3.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(2).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.tv_title_3.setText(data.get(2).getRoomName());
//                                        viewHolder.tvCount3.setText("房间人数：" + data.get(2).getNum());
//                                        break;
//                                    }
//                                    default: {
//                                        viewHolder.iv1.setImageURI(data.get(0).getImg());
//                                        viewHolder.iv1.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(0).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.tv_title_1.setText(data.get(0).getRoomName());
//                                        viewHolder.tvCount1.setText("房间人数：" + data.get(0).getNum());
//                                        viewHolder.iv2.setVisibility(View.VISIBLE);
//                                        viewHolder.tv_title_2.setVisibility(View.VISIBLE);
//                                        viewHolder.tvCount2.setVisibility(View.VISIBLE);
//                                        viewHolder.iv2.setImageURI(data.get(1).getImg());
//                                        viewHolder.iv2.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(1).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.tv_title_2.setText(data.get(1).getRoomName());
//                                        viewHolder.tvCount2.setText("房间人数：" + data.get(1).getNum());
//                                        viewHolder.iv3.setVisibility(View.VISIBLE);
//                                        viewHolder.tv_title_3.setVisibility(View.VISIBLE);
//                                        viewHolder.tvCount3.setVisibility(View.VISIBLE);
//                                        viewHolder.iv3.setImageURI(data.get(2).getImg());
//                                        viewHolder.iv3.setOnClickListener(v -> {
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString(Const.ShowIntent.ROOMID, data.get(2).getUsercoding());
//                                            ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
//                                        });
//                                        viewHolder.tv_title_3.setText(data.get(2).getRoomName());
//                                        viewHolder.tvCount3.setText("房间人数：" + data.get(2).getNum());
//                                        datas.addAll(data.subList(3, data.size()));
//                                        break;
//                                    }
//                                }
//                            }
//                        } else
//                            datas.addAll(data);
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        showToast(baseBean.getMsg());
//                    }
//                }
//            });
//        }
//    }
//
//    @Override
//    public void setResume() {
//
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
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        isDestroyed = true;
//        unbinder.unbind();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (adapter instanceof SameCityAdapter) {
//            ((SameCityAdapter) adapter).stopPaly();
//        }
//    }
//
//    class ViewHolder {
//        @BindView(R.id.banner_other)
//        View banner_other;
//        @BindView(R.id.banner)
//        ViewPager banner;
//        @BindView(R.id.ll_indicator)
//        LinearLayout llIndicator;
//        @BindView(R.id.root)
//        View root;
//        @BindView(R.id.iv_1)
//        SimpleDraweeView iv1;
//        @BindView(R.id.tv_count_1)
//        TextView tvCount1;
//        @BindView(R.id.iv_2)
//        SimpleDraweeView iv2;
//        @BindView(R.id.tv_count_2)
//        TextView tvCount2;
//        @BindView(R.id.iv_3)
//        SimpleDraweeView iv3;
//        @BindView(R.id.tv_count_3)
//        TextView tvCount3;
//        @BindView(R.id.tv_title_1)
//        TextView tv_title_1;
//        @BindView(R.id.tv_title_2)
//        TextView tv_title_2;
//        @BindView(R.id.tv_title_3)
//        TextView tv_title_3;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}
