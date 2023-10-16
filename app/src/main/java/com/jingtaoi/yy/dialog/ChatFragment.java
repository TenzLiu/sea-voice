package com.jingtaoi.yy.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.adapter.FragPagerAdapter;
import com.jingtaoi.yy.ui.find.fragment.RadioDatingOneFragment;
import com.jingtaoi.yy.ui.message.fragment.ChatListFragment;
import com.jingtaoi.yy.ui.room.fragment.AllMsgFargment;
import com.jingtaoi.yy.utils.ViewFindUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 消息弹窗
 */
public class ChatFragment extends DialogFragment {

    Unbinder unbinder;
    @BindView(R.id.view_message_d)
    View viewMessageD;
//    @BindView(R.id.mTabLayout_message_d)
//    SlidingTabLayout mTabLayoutMessageD;
    @BindView(R.id.iv_close_message_d)
    ImageView ivCloseMessageD;
    @BindView(R.id.mViewPager_message_d)
    ViewPager mViewPagerMessageD;
    @BindView(R.id.ll_back_auction)
    LinearLayout llBackAuction;

    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    FragPagerAdapter fragPagerAdapter;
    ChatListFragment chatListFragment;
    RadioDatingOneFragment radioDatingFragment;

    View rootView;
    private SlidingTabLayout mTabLayoutMessageD;
    private AllMsgFargment allMsgFargment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_message, container);
        unbinder = ButterKnife.bind(this, view);
        rootView = view.getRootView();
        mTabLayoutMessageD = ViewFindUtils.find(rootView, R.id.mTabLayout_message_d);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFrag();
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        WindowManager manager = getActivity().getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        int height = outMetrics.heightPixels;
//        params.height = height / 3 * 2;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
//        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
        super.onResume();
    }

    private void setFrag() {
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        titles.add(getString(R.string.title_message));
        titles.add(getString(R.string.title_plaza));
//        titles.add("全服喊话");
        chatListFragment = new ChatListFragment();
        chatListFragment.setRootView(rootView);
        radioDatingFragment = new RadioDatingOneFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",1);
        radioDatingFragment.setArguments(bundle);
        allMsgFargment = new AllMsgFargment();
        fragments.add(chatListFragment);
        fragments.add(radioDatingFragment);
//        fragments.add(allMsgFargment);
        fragPagerAdapter = new FragPagerAdapter(getChildFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPagerMessageD.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();

        String[] strs = titles.toArray(new String[titles.size()]);
        mTabLayoutMessageD.setViewPager(mViewPagerMessageD,strs);
        mTabLayoutMessageD.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPagerMessageD.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPagerMessageD.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayoutMessageD.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPagerMessageD.setCurrentItem(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @OnClick({R.id.view_message_d, R.id.iv_close_message_d})
    @OnClick({R.id.view_message_d})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_message_d:
                dismiss();
                break;
            case R.id.iv_close_message_d:
                dismiss();
                break;
        }
    }
}
