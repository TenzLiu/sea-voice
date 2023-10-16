package com.jingtaoi.yy.ui.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.HomeItemData;
import com.jingtaoi.yy.bean.HomeTuijianBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.home.adapter.HomeCityAdapter;
import com.jingtaoi.yy.ui.home.adapter.HomeHotAdapter;
import com.jingtaoi.yy.ui.room.VoiceActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomePersonalFragment extends MyBaseFragment {
    @BindView(R.id.ll_header_other)
    LinearLayout llHeaderOther;
    @BindView(R.id.mRecyclerView_other)
    RecyclerView mRecyclerViewOther;
    @BindView(R.id.mSwipeRefreshLayout_other)
    SwipeRefreshLayout mSwipeRefreshLayoutOther;
    Unbinder unbinder;


    HomeHotAdapter hotAdapter;
    HomeCityAdapter cityAdapter;


    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other, container, false);
    }

    @Override
    public void initView() {

        setHotRecycler();
        mSwipeRefreshLayoutOther.post(new Runnable() {
            @Override
            public void run() {
                page = 1;
                getData();

            }
        });

        mSwipeRefreshLayoutOther.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();

            }
        });
    }

    private void setHotRecycler() {


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


    private void gotoRoom(String roomId) {
        Bundle bundle = new Bundle();
        bundle.putString(Const.ShowIntent.ROOMID, roomId);
        ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
    }

    private ArrayList<HomeItemData> datas = new ArrayList<>();

    private void getData() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("state", 1);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.HOME_DATA, map, new MyObserver(this){
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
