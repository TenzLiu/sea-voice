package com.jingtaoi.yy.ui.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyApplication;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.HomeItemData;
import com.jingtaoi.yy.bean.HomeTuijianBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.home.adapter.HomeCityAdapter;
import com.jingtaoi.yy.ui.home.adapter.HomeHotAdapter;
import com.jingtaoi.yy.ui.message.OtherHomeActivity;
import com.jingtaoi.yy.ui.room.VoiceActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 其他页面（首页）
 */
public class OtherFragment extends MyBaseFragment {
    @BindView(R.id.ll_header_other)
    LinearLayout llHeaderOther;
    @BindView(R.id.mRecyclerView_other)
    RecyclerView mRecyclerViewOther;
    @BindView(R.id.mSwipeRefreshLayout_other)
    SwipeRefreshLayout mSwipeRefreshLayoutOther;
    Unbinder unbinder;

    private int type;
    //    private HFRecyclerAdapter adapter;
    HomeHotAdapter hotAdapter;
    //    private ViewHolder viewHolder;
    HomeCityAdapter cityAdapter;


    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other, container, false);
    }

    @Override
    public void initView() {
        type = getArguments().getInt(Const.ShowIntent.TYPE, 0);
        if (type == 6) {
            setCityRecycler();
        } else {
            setHotRecycler();
        }

        mSwipeRefreshLayoutOther.post(new Runnable() {
            @Override
            public void run() {
                page = 1;
                getData();
//                if (type == 2) {
//                    getBanner(Const.IntShow.THREE);
//                } else if (type == 3) {
//                    getBanner(Const.IntShow.TWO);
//                }
            }
        });

        mSwipeRefreshLayoutOther.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
//                if (type == 2) {
//                    getBanner(Const.IntShow.THREE);
//                } else if (type == 3) {
//                    getBanner(Const.IntShow.TWO);
//                }
            }
        });
    }

    private void setHotRecycler() {
//        View header = View.inflate(getActivity(),R.layout.layout_header_other,null);
        mRecyclerViewOther.setPadding(0, 0, 20, 0);
//        View header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_other, null);
//        viewHolder = new ViewHolder(header);
//        if (type == 2) {
//            llHeaderOther.addView(header);
////            getBanner(Const.IntShow.THREE);
//        } else if (type == 3) {
//            llHeaderOther.addView(header);
//            viewHolder.root.setVisibility(View.GONE);
////            getBanner(Const.IntShow.TWO);
//        } else if (type == 4) {
//            llHeaderOther.addView(header);
//            viewHolder.banner_other.setVisibility(View.GONE);
//        }

        hotAdapter = new HomeHotAdapter(R.layout.item_xh_item2);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerViewOther.setLayoutManager(layoutManager);
        mRecyclerViewOther.setAdapter(hotAdapter);

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
        }, mRecyclerViewOther);

        hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeItemData item = (HomeItemData) adapter.getItem(position);
                Bundle bundle = new Bundle();
                assert item != null;
                bundle.putString(Const.ShowIntent.ROOMID, item.getUsercoding());
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);

            }
        });
    }

    private void setCityRecycler() {
        mRecyclerViewOther.setPadding(0, 8, 20, 0);
        cityAdapter = new HomeCityAdapter(R.layout.item_same_city);
        mRecyclerViewOther.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerViewOther.setAdapter(cityAdapter);

        cityAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getData();
            }
        }, mRecyclerViewOther);

        cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeItemData item = (HomeItemData) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.ID, item.getId());
                ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
            }
        });
    }

    private boolean isFirst = true;
    Handler handler = new Handler();
    private Boolean isDestroyed = false;
    private int itemPosition = 0;

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
////                        viewHolder.banner_other.setVisibility(View.VISIBLE);
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
////                        viewHolder.banner.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
////                            @Override
////                            public Fragment getItem(int i) {
////                                return fragments.get(i);
////                            }
////
////                            @Override
////                            public int getCount() {
////                                return fragments.size();
////                            }
////                        });
////                        ViewPagerIndicator viewPagerIndicator = new ViewPagerIndicator(getActivity(), viewHolder.llIndicator, R.drawable.indicator_normal, R.drawable.indicator_select, fragments.size());
////                        viewPagerIndicator.setupWithViewPager(viewHolder.banner);
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

    private void gotoRoom(String roomId) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ROOMID, roomId);
        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
    }

    private ArrayList<HomeItemData> datas = new ArrayList<>();

    private void getData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        if (type == 6) { //同城
            map.put("uid", userToken);
            //todo 定位
            map.put("lat", MyApplication.lat);
            map.put("lon", MyApplication.lon);
            HttpManager.getInstance().post(Api.SAME_CITY, map, new MyObserver(this) {
                @Override
                public void success(String responseString) {
                    if (mSwipeRefreshLayoutOther != null) {
                        mSwipeRefreshLayoutOther.setRefreshing(false);
                        cityAdapter.setEnableLoadMore(true);
                    }
                    HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
                    setCityData(baseBean.getData(), cityAdapter);
                }

                @Override
                protected void onError(int code, String msg) {
                    super.onError(code, msg);
                    if (mSwipeRefreshLayoutOther != null) {
                        mSwipeRefreshLayoutOther.setRefreshing(false);
                        cityAdapter.setEnableLoadMore(true);
                    }
                }
            });

        } else {
            map.put("state", type == 2 ? 2 : type == 7 ? 7 : type + 1);
            HttpManager.getInstance().post(Api.HOME_DATA, map, new MyObserver(this) {
                @Override
                public void success(String responseString) {
                    if (mSwipeRefreshLayoutOther == null) {
                        return;
                    } else {
                        mSwipeRefreshLayoutOther.setRefreshing(false);
                        hotAdapter.setEnableLoadMore(true);
                    }
                    HomeTuijianBean baseBean = JSON.parseObject(responseString, HomeTuijianBean.class);
                    if (baseBean.getCode() == Api.SUCCESS) {
                        List<HomeItemData> data = baseBean.getData();
                        if (page == 1) {
                            datas.clear();
                        }
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
////                                        datas.addAll(data.subList(3, data.size()));
//                                        setData(data.subList(3, data.size()), hotAdapter);
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            //                            datas.addAll(data);
                        setData(data, hotAdapter);
//                        }
//                        adapter.notifyDataSetChanged();
                    } else {
                        showToast(baseBean.getMsg());
                    }
                }

                @Override
                protected void onError(int code, String msg) {
                    super.onError(code, msg);
                    if (mSwipeRefreshLayoutOther != null) {
                        mSwipeRefreshLayoutOther.setRefreshing(false);
                    }
                }
            });
        }
    }

    private void setCityData(List<HomeItemData> data, HomeCityAdapter adapter) {
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
            if (page == 1) { //GridLayoutManager不显示加载更多
                adapter.loadMoreEnd(true);
            } else {
                adapter.loadMoreEnd(true);
            }
        } else {
            adapter.loadMoreComplete();
        }
    }

    private void setData(List<HomeItemData> data, HomeHotAdapter adapter) {
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
            if (page == 1) { //GridLayoutManager不显示加载更多
                adapter.loadMoreEnd(true);
            } else {
                adapter.loadMoreEnd(true);
            }
        } else {
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    public void onPause() {
        super.onPause();
//        if (adapter instanceof SameCityAdapter) {
//            ((SameCityAdapter) adapter).stopPaly();
//        }
        if (cityAdapter != null) {
            cityAdapter.stopPaly();
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
        isDestroyed = true;
        unbinder.unbind();
    }

    class ViewHolder {
        @BindView(R.id.banner_other)
        View banner_other;
        @BindView(R.id.banner)
        ViewPager banner;
        @BindView(R.id.ll_indicator)
        LinearLayout llIndicator;
        @BindView(R.id.root)
        View root;
        @BindView(R.id.iv_1)
        SimpleDraweeView iv1;
        @BindView(R.id.tv_count_1)
        TextView tvCount1;
        @BindView(R.id.iv_2)
        SimpleDraweeView iv2;
        @BindView(R.id.tv_count_2)
        TextView tvCount2;
        @BindView(R.id.iv_3)
        SimpleDraweeView iv3;
        @BindView(R.id.tv_count_3)
        TextView tvCount3;
        @BindView(R.id.tv_title_1)
        TextView tv_title_1;
        @BindView(R.id.tv_title_2)
        TextView tv_title_2;
        @BindView(R.id.tv_title_3)
        TextView tv_title_3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
