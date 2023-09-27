package cn.sinata.xldutils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.adapter.HFRecyclerAdapter;
import cn.sinata.xldutils.utils.DensityUtil;

/**
 *
 * Created by liaoxiang on 16/3/28.
 */
public class SwipeRefreshRecyclerLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private TextView loadMoreView;
    private RecyclerView.Adapter mAdapter;
    private boolean isLoadMore = false;
    private Mode mode = Mode.Both;
    private OnRefreshListener onRefreshListener;
    private int lastVisibleItem = 0;
    private int[] lastPositions ;

    public SwipeRefreshRecyclerLayout(Context context) {
        super(context);
        init();
    }

    public SwipeRefreshRecyclerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init(){
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        addView(mRecyclerView);
        setOnRefreshListener(this);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            RecyclerView.Adapter adapter =recyclerView.getAdapter();
            resetLastVisibleItem();
            if (newState == RecyclerView.SCROLL_STATE_IDLE && adapter != null
                    && lastVisibleItem + 1 == adapter.getItemCount() && !isRefreshing() && !isLoadMore) {
                if (adapter instanceof HFRecyclerAdapter){

                    if (loadMoreView!=null ){
                        loadMoreView.setText("载入更多...");
                        loadMoreView.setVisibility(View.VISIBLE);
                    }
                    isLoadMore = true;
                    if (onRefreshListener!=null){
                        onRefreshListener.onLoadMore();
                    }
                }
            }
        }
    };

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        if (mode == Mode.Both){
            initLoadMore();
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
    }

    public void setMode(Mode mode){
        this.mode = mode;
        if (mode == Mode.Both){
            mRecyclerView.addOnScrollListener(onScrollListener);
            initLoadMore();
        }else {
            mRecyclerView.removeOnScrollListener(onScrollListener);
        }
    }

    private void initLoadMore(){
        if (mAdapter instanceof HFRecyclerAdapter) {
            if (loadMoreView!=null){
                return;
            }
            loadMoreView = new TextView(getContext());
            loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                loadMoreView.setHeight(DensityUtil.dip2px(this,48));
//            loadMoreView.setBackgroundColor(Color.WHITE);
            loadMoreView.setText("载入更多...");
            loadMoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            loadMoreView.setTextColor(getResources().getColor(R.color.textColor));
            loadMoreView.setGravity(Gravity.CENTER);
            loadMoreView.setPadding(0, DensityUtil.dip2px(getContext(), 16), 0, DensityUtil.dip2px(getContext(), 16));
            ((HFRecyclerAdapter) mAdapter).setFooterView(loadMoreView);
        }
    }

    public void setLoadMoreText(CharSequence text){
        if (loadMoreView!=null){
            loadMoreView.setText(text);
            if (loadMoreView.getVisibility() != View.VISIBLE){
                loadMoreView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 重新获取可见item的位置
     */
    private void resetLastVisibleItem(){
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }else if (layoutManager!=null && layoutManager instanceof StaggeredGridLayoutManager){
            StaggeredGridLayoutManager staggeredGridLayoutManager =((StaggeredGridLayoutManager) layoutManager);
            if (lastPositions == null) {
                lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            }
            staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
            lastVisibleItem = findMax(lastPositions);
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void onRefresh() {
        if (onRefreshListener!=null){
            onRefreshListener.onRefresh();
        }
    }

    /**
     * 设置刷新状态
     * @param refreshing 刷新状态
     */
    @Override
    public void setRefreshing(boolean refreshing){
        super.setRefreshing(refreshing);
        this.isLoadMore = refreshing;
        if (loadMoreView!=null ){
            loadMoreView.setVisibility(View.GONE);
        }
    }

    public enum Mode{
        Both,Top
    }

    public interface OnRefreshListener {
        void onRefresh();
        void onLoadMore();
    }
}
