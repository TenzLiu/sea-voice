package cn.sinata.xldutils.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.adapter.HFRecyclerAdapter;
import cn.sinata.xldutils.utils.DensityUtil;
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;

/**
 *
 */
public abstract class RecyclerViewFragment extends BaseFragment {

    protected SwipeRefreshRecyclerLayout mSwipeRefreshLayout;
    private RecyclerView.ItemDecoration defaultItemDecoration;
    private TextView emptyView;
    protected int DEFAULT_MARGIN_DIVIDER = 0;
    protected int DEFAULT_DIVIDER = 1;

    protected abstract RecyclerView.Adapter setAdapter();

    /**
     * 下拉刷新
     */
    protected void pullDownRefresh() {
    }

    /**
     * 加载更多
     */
    protected void loadMore() {
    }

    /**
     * @return 默认上拉下拉同时存在
     */
    protected SwipeRefreshRecyclerLayout.Mode getMode() {
        return SwipeRefreshRecyclerLayout.Mode.Both;
    }

    /**
     * @return 默认垂直布局。
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected final int getContentViewLayoutID() {
        return R.layout.fragment_vertical_linear_recyclerview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = mHolder.bind(R.id.mSwipeRefreshLayout);
        emptyView = mHolder.bind(R.id.emptyView);
        emptyView.setVisibility(View.GONE);
        emptyView.setTextColor(getLoadMoreTextColor());
        defaultItemDecoration = new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.dividing_line_color)
                .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                .size(1)
                .build();
        //默认分割线
        setItemDecoration(DEFAULT_DIVIDER);
        mSwipeRefreshLayout.setLayoutManager(getLayoutManager());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshRecyclerLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownRefresh();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        mSwipeRefreshLayout.setMode(getMode());
        if (setAdapter() != null) {
            final RecyclerView.Adapter adapter = setAdapter();

            mSwipeRefreshLayout.setAdapter(adapter);
            dataObserver = new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    RecyclerView.Adapter a = mSwipeRefreshLayout.getRecyclerView().getAdapter();
                    if (a == null) {
                        return;
                    }
                    int size = a.getItemCount();
                    if (a instanceof HFRecyclerAdapter) {
                        size = ((HFRecyclerAdapter) a).getDataItemCount();
                    }
                    if (size <= 0) {
                        if (useDefaultEmptyView()) {
                            emptyView.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        }

                    } else {
                        emptyView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    }
                }
            };
            adapter.registerAdapterDataObserver(dataObserver);

            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                    pullDownRefresh();
                }
            });
        }
    }

    RecyclerView.AdapterDataObserver dataObserver;

    @Override
    public void onDestroyView() {
        try {
            RecyclerView.Adapter adapter = getRecyclerView().getAdapter();
            if (dataObserver != null && adapter != null) {
                adapter.unregisterAdapterDataObserver(dataObserver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroyView();
    }

    protected void setEmptyViewText(CharSequence cs) {
        emptyView.setText(cs);
    }

    protected void setEmptyViewClick(View.OnClickListener listener) {
        emptyView.setOnClickListener(listener);
    }

    /**
     * 设置刷新状态
     *
     * @param refreshing 刷新状态
     */
    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    /**
     * 刷新完成
     */
    public void onRefreshCompleted() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void setLoadMoreText(CharSequence text) {
        mSwipeRefreshLayout.setLoadMoreText(text);
    }

    /**
     * 设置分割线,只能设置1种分割线
     *
     * @param itemDecoration 样式
     */
    protected void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration == null) {
            try {
                mSwipeRefreshLayout.getRecyclerView().removeItemDecoration(defaultItemDecoration);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (defaultItemDecoration != null) {
            mSwipeRefreshLayout.getRecyclerView().removeItemDecoration(defaultItemDecoration);
        }
        defaultItemDecoration = itemDecoration;
        mSwipeRefreshLayout.addItemDecoration(defaultItemDecoration);
    }

    /**
     * 添加分割线，可以是多种。比如网格的横竖2中分割线，不会覆盖。
     *
     * @param itemDecoration 分割线
     */
    protected void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration == null) {
            return;
        }
        mSwipeRefreshLayout.addItemDecoration(itemDecoration);
    }

    protected void setItemDecoration(int type) {
        HorizontalDividerItemDecoration itemDecoration;
        if (type == DEFAULT_MARGIN_DIVIDER) {
            itemDecoration = new HorizontalDividerItemDecoration.Builder(context)
                    .margin(DensityUtil.dip2px(context, 16))
                    .size(1)
                    .color(ContextCompat.getColor(context, R.color.dividing_line_color))
                    .build();

        } else {
            itemDecoration = new HorizontalDividerItemDecoration.Builder(context)
                    .size(1)
                    .color(ContextCompat.getColor(context, R.color.dividing_line_color))
                    .build();
        }
        setItemDecoration(itemDecoration);
    }

    public RecyclerView getRecyclerView() {
        return mSwipeRefreshLayout.getRecyclerView();
    }

    protected boolean useDefaultEmptyView() {
        return true;
    }

    protected int getLoadMoreTextColor() {
        return Color.WHITE;
    }
}