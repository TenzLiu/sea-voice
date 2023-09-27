package com.siyecaoi.yy.ui.room.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.siyecaoi.yy.R;
import com.siyecaoi.yy.ui.room.adapter.GiftGroudAdapter;
import com.siyecaoi.yy.base.MyBaseFragment;
import com.siyecaoi.yy.bean.GiftBean;
import com.siyecaoi.yy.utils.Const;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GiftFragment extends MyBaseFragment {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    Unbinder unbinder;

    GiftGroudAdapter giftGroudAdapter;
    List<GiftBean.DataBean> data;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_top, container, false);
        return view;
    }

    @Override
    public void initView() {
//        data = (List<GiftBean.DataBean>) getArguments().getSerializable(Const.ShowIntent.DATA);
        setRecycler();
    }

    private void setRecycler() {
        giftGroudAdapter = new GiftGroudAdapter(R.layout.item_gift);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Const.IntShow.FOUR);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(giftGroudAdapter);
        if (data != null) {
            giftGroudAdapter.setNewData(data);
        }


    }

    public void setData(List<GiftBean.DataBean> data) {
        this.data = data;
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
    }
}
