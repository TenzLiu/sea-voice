package com.jingtaoi.yy.ui.home.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.bean.HomeItemData;
import com.jingtaoi.yy.bean.HomeTuijianBean;
import com.jingtaoi.yy.bean.SearchHistoryBean;
import com.jingtaoi.yy.bean.SearchHotKeyBean;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.ui.home.adapter.SeachResultAdapter;
import com.jingtaoi.yy.ui.message.OtherHomeActivity;
import com.jingtaoi.yy.ui.room.VoiceActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.jingtaoi.yy.view.FlowLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout;

public class SearchFragment extends MyBaseFragment {
    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.btn_action)
    TextView btnAction;
    Unbinder unbinder;
    @BindView(R.id.fl_search)
    FrameLayout flSearch;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fl_hot)
    FlowLayout flHot;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.fl_history)
    FlowLayout flHistory;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.iv_del)
    ImageView iv_del;
    @BindView(R.id.rl_content)
    SwipeRefreshRecyclerLayout rlContent;

    private SeachResultAdapter adapter;
    private ArrayList<HomeItemData> datas = new ArrayList<>();

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void initView() {
        type = getArguments().getInt("type");
        if (type == 1) {
            getHot();
        } else {
            etKey.setHint("请输入昵称/ID");
            tvTitle.setVisibility(View.GONE);
        }
        rlContent.setMode(SwipeRefreshRecyclerLayout.Mode.Both);
        rlContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new SeachResultAdapter(datas);
        adapter.setOnItemClickListener(((view, position) -> {
            Bundle bundle = new Bundle();
            if (type == 1) {//进房间
                bundle.putString(Const.ShowIntent.ROOMID, datas.get(position).getUsercoding());
                ActivityCollector.getActivityCollector().toOtherActivity(VoiceActivity.class, bundle);
            } else {
                bundle.putInt(Const.ShowIntent.ID, datas.get(position).getUid());
                ActivityCollector.getActivityCollector().toOtherActivity(OtherHomeActivity.class, bundle);
            }
        }));
        rlContent.setAdapter(adapter);
        rlContent.setOnRefreshListener(new SwipeRefreshRecyclerLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                search();
            }

            @Override
            public void onLoadMore() {
                page++;
                search();
            }
        });
        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    rlContent.setVisibility(View.GONE);
                    llSearch.setVisibility(View.VISIBLE);
                    iv_del.setVisibility(View.GONE);
                } else {
                    iv_del.setVisibility(View.VISIBLE);
                }
            }
        });
        getHistory();
    }

    private SearchHistoryBean historyBean;
    private Gson gson = new Gson();

    private void getHistory() {
        String s = (String) SharedPreferenceUtils.get(getActivity(), type == 1 ? Const.ROOM_HIS : Const.USER_HIS, "");
        if (s.isEmpty()) {
            historyBean = new SearchHistoryBean(0, new ArrayList<SearchHistoryBean.DataBean>());
        } else {
            historyBean = gson.fromJson(s, SearchHistoryBean.class);
        }
        flHistory.removeAllViews();
        for (SearchHistoryBean.DataBean item : historyBean.getData()) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_key, null);
            TextView tv = view.findViewById(R.id.tv_key);
            tv.setText(item.getName());
            tv.setOnClickListener(v -> {
                etKey.setText(item.getName());
                search();
            });
            flHistory.addView(view);
        }
    }

    private void saveHistory(String key) {
        for (SearchHistoryBean.DataBean item : historyBean.getData()) {
            if (key.equals(item.getName())) {
                historyBean.getData().remove(item);
                break;
            }
        }
        historyBean.getData().add(0, new SearchHistoryBean.DataBean(key));
        SharedPreferenceUtils.put(getActivity(), type == 1 ? Const.ROOM_HIS : Const.USER_HIS, gson.toJson(historyBean));
        getHistory();
    }

    private void getHot() {
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        HttpManager.getInstance().post(Api.HOT_WORDS, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                Gson gson = new Gson();
                SearchHotKeyBean searchHotKeyBean = gson.fromJson(responseString, SearchHotKeyBean.class);
                if (searchHotKeyBean.getCode() == 0) {
                    List<SearchHotKeyBean.DataBean> data = searchHotKeyBean.getData();
                    for (SearchHotKeyBean.DataBean item : data) {
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_key, null);
                        TextView tv = view.findViewById(R.id.tv_key);
                        tv.setText(item.getName());
                        tv.setOnClickListener(v -> {
                            etKey.setText(item.getName());
                            search();
                        });
                        flHot.addView(view);
                    }
                } else {
                    showToast(searchHotKeyBean.getMsg());
                }
            }
        });
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {
        btnAction.setOnClickListener(v -> {
            search();
        });
        iv_del.setOnClickListener(v ->
                etKey.setText(""));
        ivClear.setOnClickListener(v -> {
            SharedPreferenceUtils.put(getActivity(), type == 1 ? Const.ROOM_HIS : Const.USER_HIS, "");
            getHistory();
        });
    }

    private void search() {
        String ket = etKey.getText().toString().trim();
        if (ket.isEmpty())
            return;
        saveHistory(ket);
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("name", ket);
        map.put("state", type);
        map.put("pageSize", PAGE_SIZE);
        map.put("pageNum", page);
        HttpManager.getInstance().post(Api.SEARCH, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                if (rlContent != null) {
                    rlContent.setRefreshing(false);
                    rlContent.setVisibility(View.VISIBLE);
                }
                if (llSearch != null) {
                    llSearch.setVisibility(View.GONE);
                }
                Gson gson = new Gson();
                HomeTuijianBean result = gson.fromJson(responseString, HomeTuijianBean.class);
                if (result.getCode() == 0) {
                    List<HomeItemData> data = result.getData();
                    if (page == 1)
                        datas.clear();
                    datas.addAll(data);
                    if (datas.isEmpty())
                        rlContent.setLoadMoreText("暂无数据");
                    else
                        rlContent.setLoadMoreText("");
                    adapter.notifyDataSetChanged();
                } else
                    showToast(result.getMsg());
            }

            @Override
            protected void onError(int code, String msg) {
                super.onError(code, msg);
                rlContent.setRefreshing(false);
            }
        });

    }

    private int type;//1房间，2 是用户

    public static SearchFragment getInstance(int type) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        searchFragment.setArguments(bundle);
        return searchFragment;
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
