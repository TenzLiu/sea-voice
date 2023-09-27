package cn.sinata.xldutils.activitys;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.utils.DensityUtil;
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;

/**
 * 列表型activity
 */
public abstract class RecyclerListActivity extends TitleActivity{

    protected abstract RecyclerView.Adapter setAdapter();
    /**
     * 下拉刷新
     */
    protected void pullDownRefresh(){}

    /**
     * 加载更多
     */
    protected void loadMore(){}

    protected SwipeRefreshRecyclerLayout mSwipeRefreshLayout;
    private RecyclerView.ItemDecoration defaultItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_base_recyclerview);
        initView();
    }

    /**
     * @return 默认上拉下拉同时存在
     */
    protected SwipeRefreshRecyclerLayout.Mode getMode(){
        return SwipeRefreshRecyclerLayout.Mode.Both;
    }

    /**
     * @return  默认垂直布局。
     */
    protected RecyclerView.LayoutManager getLayoutManager(){
        return new LinearLayoutManager(this);
    }

    protected void initView() {

        mSwipeRefreshLayout = bind(R.id.mSwipeRefreshLayout);
        defaultItemDecoration = new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.dividing_line_color)
                .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                .size(1)
                .build();
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
        View view = addBottomView();
        if (view != null){
            addView(2,view);
        }

        mSwipeRefreshLayout.setMode(getMode());
        if (setAdapter() != null) {
            RecyclerView.Adapter adapter = setAdapter();
            mSwipeRefreshLayout.setAdapter(adapter);
        }
        setRefreshing(false);
        if (getDefaultItemDecoration() != null) {
            addItemDecoration(getDefaultItemDecoration());
        }
    }

    protected RecyclerView.ItemDecoration getDefaultItemDecoration(){
        return defaultItemDecoration;
    }

    protected View addBottomView(){
        return null;
    }

    protected void setSwipeRefreshLayoutBackground(int color){
        mSwipeRefreshLayout.setBackgroundColor(color);
    }

    /**
     * 添加分割线，可以是多种。比如网格的横竖2中分割线，不会覆盖。
     * @param itemDecoration 分割线
     */
    protected void addItemDecoration(RecyclerView.ItemDecoration itemDecoration){
        mSwipeRefreshLayout.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置刷新状态
     * @param refreshing 刷新状态
     */
    public void setRefreshing(boolean refreshing){
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    protected void setLoadMoreText(CharSequence text){
        mSwipeRefreshLayout.setLoadMoreText(text);
    }

    /**
     * 设置分割线
     * @param itemDecoration 样式
     */
    protected void setItemDecoration(RecyclerView.ItemDecoration itemDecoration){

        if (defaultItemDecoration != null) {
            mSwipeRefreshLayout.getRecyclerView().removeItemDecoration(defaultItemDecoration);
        }
        mSwipeRefreshLayout.addItemDecoration(itemDecoration);
        defaultItemDecoration = itemDecoration;
    }

    protected int DEFAULT_MARGIN_DIVIDER = 0;
    protected int DEFAULT_DIVIDER = 1;
    protected int DEFAULT_DOUBLE_LINE_DIVIDER = 2;

    protected void setItemDecoration(int type) {
        HorizontalDividerItemDecoration itemDecoration;
        if (type == DEFAULT_MARGIN_DIVIDER){
            itemDecoration = new HorizontalDividerItemDecoration.Builder(getApplicationContext())
                    .margin(DensityUtil.dip2px(this,16))
                    .size(1)
                    .color(ContextCompat.getColor(this,R.color.dividing_line_color))
                    .build();

        }else if (type == DEFAULT_DOUBLE_LINE_DIVIDER){
            itemDecoration = new HorizontalDividerItemDecoration.Builder(getApplicationContext())
                    .size(DensityUtil.dip2px(this,8))
                    .drawable(ContextCompat.getDrawable(this,R.drawable.double_line_bg))
                    .build();
        }else {
            itemDecoration = new HorizontalDividerItemDecoration.Builder(getApplicationContext())
                    .size(1)
                    .color(ContextCompat.getColor(this,R.color.dividing_line_color))
                    .build();
        }
        setItemDecoration(itemDecoration);
    }

    protected RecyclerView getRLView(){
        return mSwipeRefreshLayout.getRecyclerView();
    }
}