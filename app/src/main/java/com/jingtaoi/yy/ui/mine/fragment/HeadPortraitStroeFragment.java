package com.jingtaoi.yy.ui.mine.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.StoreListBean;
import com.jingtaoi.yy.dialog.MyBottomShowDialog;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.dialog.MyGiftShowDialog;
import com.jingtaoi.yy.dialog.MyHintDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.mine.SendOtherActivity;
import com.jingtaoi.yy.ui.mine.adapter.HeadPortraitStoreAdapter;
import com.jingtaoi.yy.ui.room.adapter.BottomShowRecyclerAdapter;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HeadPortraitStroeFragment extends MyBaseFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_price)
    TextView tv_price;
    Unbinder unbinder;
    int type;
    int id = 0;
    String name = "";
    int gold = 0;
    HeadPortraitStoreAdapter storeListAdapter;
    MyBottomShowDialog myBottomShowDialog;
    MyGiftShowDialog giftShowDialog;
    private ArrayList<String> bottomList;//弹框显示内容
    MyDialog myDialog;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_recycler_stroe, container, false);
    }

    @Override
    public void initView() {

        initShow();

    }

    private void initShow() {
        bottomList = new ArrayList<>();
        bottomList.add(getString(R.string.tv_forus_store));
        bottomList.add(getString(R.string.tv_send_store));
        bottomList.add(getString(R.string.tv_cancel));

        Bundle bundle = getArguments();
        type = bundle.getInt(Const.ShowIntent.TYPE);
        setRecycler();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                page = 1;
                getCall();
            }
        });
    }

    private void getCall() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        if (type == 0) {
            map.put("state", Const.IntShow.TWO);
        } else if (type == 1) {
            map.put("state", Const.IntShow.ONE);
        }
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.getSceneList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    storeListAdapter.setEnableLoadMore(false);
                }
                StoreListBean storeListBean = JSON.parseObject(responseString, StoreListBean.class);
                if (storeListBean.getCode() == Api.SUCCESS) {
                    setData(storeListBean.getData(), storeListAdapter);
                } else {
                    showToast(storeListBean.getMsg());
                }
            }
        });
    }

    private void setData(List<StoreListBean.DataEntity> data, HeadPortraitStoreAdapter adapter) {
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
            adapter.loadMoreEnd(true);
        } else {
            adapter.loadMoreComplete();
        }
    }

    private void setRecycler() {
//        mRecyclerView.setPadding(10, 0, 0, 0);
        storeListAdapter = new HeadPortraitStoreAdapter(R.layout.item_head_portrait_store);
        storeListAdapter.setType(type);
        storeListAdapter.setEnableLoadMore(false);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setAdapter(storeListAdapter);
        mRecyclerView.setLayoutManager(layoutManager);


//        View view = View.inflate(getContext(), R.layout.layout_nodata, null);
//        TextView tvNodata = view.findViewById(R.id.tv_nodata);
//        tvNodata.setText(getString(R.string.hint_prop_personhome));
//        storeListAdapter.setEmptyView(view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                storeListAdapter.setEnableLoadMore(false);
                page = 1;
                getCall();
            }
        });
//
//        storeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                page++;
//                getCall();
//            }
//        }, mRecyclerView);

        storeListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                StoreListBean.DataEntity dataEntity = (StoreListBean.DataEntity) adapter.getItem(position);
                assert dataEntity != null;
                switch (view.getId()) {
                    case R.id.rl_store:
                        id = dataEntity.getId();
                        gold = dataEntity.getGold();
                        name = dataEntity.getName();
                        storeListAdapter.setCheck(id);
                        tv_price.setText(gold + "");
//                        showMybottomDialog(dataEntity.getId(), dataEntity.getName(),dataEntity.getGold());
                        break;
                    case R.id.tv_try_store:
                        showMyGiftShowDialog(dataEntity.getImg(), Const.IntShow.ZERO);
                        break;
                }
            }
        });
    }

    private void showMyGiftShowDialog(String showImg, int num) {
        if (giftShowDialog != null && giftShowDialog.isShowing()) {
            giftShowDialog.dismiss();
        }
        giftShowDialog = new MyGiftShowDialog(getActivity(), showImg, num);
        if (!getActivity().isFinishing()) {
            giftShowDialog.show();
        }
    }

    private void showMybottomDialog(int id, String name,int gold) {
        if (myBottomShowDialog != null && myBottomShowDialog.isShowing()) {
            myBottomShowDialog.dismiss();
        }
        myBottomShowDialog = new MyBottomShowDialog(context, bottomList);
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
                        showMyDialog(id, name,gold);
                        break;
                    case 1:
                        Bundle bundle = new Bundle();
                        bundle.putInt(Const.ShowIntent.ID, id);
                        bundle.putString(Const.ShowIntent.DATA, name);
                        bundle.putInt(Const.ShowIntent.GOLD, gold);
                        ActivityCollector.getActivityCollector().toOtherActivity(SendOtherActivity.class, bundle);
                        break;
                }
            }
        });
    }

    private void showMyDialog(int id, String nameShow,int gold) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(getContext());
        myDialog.show();
        myDialog.setHintText("购买提示");

        SpannableStringBuilder spannableString = new SpannableStringBuilder("确认要购买“" + nameShow + "”吗？");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4BA6DC"));
        spannableString.setSpan(colorSpan, 5, 5+nameShow.length()+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.append("\n");
        spannableString.append("img");

        ImageSpan imageSpan = new ImageSpan( getContext(), R.drawable.gold);
        spannableString.setSpan(imageSpan, spannableString.length() - 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.append(" ");

        spannableString.append(String.valueOf(gold));
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#4BA6DC"));
        spannableString.setSpan(colorSpan1, spannableString.length()-String.valueOf(gold).length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        myDialog.setHintText(spannableString);
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                getBuyCall(id, userToken, nameShow);
            }
        });
    }

    //购买道具或座驾
    private void getBuyCall(int id, int otherId, String nameShow) {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("uid", userToken);
        map.put("buid", otherId);
        map.put("gid", id);
        HttpManager.getInstance().post(Api.addSceneList, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    showHint(nameShow);
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    MyHintDialog hintDialog;

    private void showHint(String nameShow) {
        if (hintDialog != null && hintDialog.isShowing()) {
            hintDialog.dismiss();
        }
        hintDialog = new MyHintDialog(getActivity());
        hintDialog.show();
        hintDialog.setRightText("好的");
        if (type == 0) {
            hintDialog.setHintText("购买成功，我们将自动为您带上 “" + nameShow + "”。您可以在个人资料中修改头饰。");
        } else if (type == 1) {
            hintDialog.setHintText("购买成功，我们将自动为您设置座驾为 “" + nameShow + "”。您可以在个人资料中修改座驾。");
        }
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @OnClick({R.id.tv_buy, R.id.tv_give})
    public void onViewClicked(View view) {
        if (id == 0){
            showToast("请选择道具");
            return;
        }
        switch (view.getId()) {
            case R.id.tv_buy:
                showMyDialog(id, name, gold);
                break;
            case R.id.tv_give:
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.ID, id);
                bundle.putString(Const.ShowIntent.DATA, name);
                bundle.putInt(Const.ShowIntent.GOLD, gold);
                ActivityCollector.getActivityCollector().toOtherActivity(SendOtherActivity.class, bundle);
                break;
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
}
