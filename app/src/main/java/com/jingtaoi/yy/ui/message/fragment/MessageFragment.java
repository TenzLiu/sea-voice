package com.jingtaoi.yy.ui.message.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.adapter.FragPagerAdapter;
import com.jingtaoi.yy.base.MyBaseFragment;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.ui.message.MailListActivity;
import com.jingtaoi.yy.ui.mine.MyAttentionActivity;
import com.jingtaoi.yy.ui.mine.MyFansActivity;
import com.jingtaoi.yy.ui.mine.MyFriendsActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.LogUtils;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/12/19.
 * 消息页面
 */

public class MessageFragment extends MyBaseFragment {
    @BindView(R.id.mTabLayout_message)
    SlidingTabLayout mTabLayoutMessage;
    @BindView(R.id.mViewPager_message)
    ViewPager mViewPagerMessage;

    @BindView(R.id.txl_tv)
    TextView txl_tv;

    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.tv_fans)
    TextView tv_fans;
    @BindView(R.id.tv_follow)
    TextView tv_follow;
    @BindView(R.id.tv_friend)
    TextView tv_friend;

    Unbinder unbinder;

    ArrayList<String> titles;
    ArrayList<Fragment> fragments;

    FragPagerAdapter fragPagerAdapter;
    ChatListFragment chatListFragment;
//    FriendFragment friendFragment;
    MyDialog myDialog;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initView() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add(getString(R.string.title_message));
//        titles.add(getString(R.string.title_friend));

        setFrag();
//        setTUImessage();

        txl_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.getActivityCollector().toOtherActivity(MailListActivity.class);
            }
        });

        iv_clear.setOnClickListener(v->{
            showMyDialog();
        });
        tv_fans.setOnClickListener(v->{
            ActivityCollector.getActivityCollector().toOtherActivity(MyFansActivity.class);
        });
        tv_follow.setOnClickListener(v->{
            ActivityCollector.getActivityCollector().toOtherActivity(MyAttentionActivity.class);
        });
        tv_friend.setOnClickListener(v->{
            ActivityCollector.getActivityCollector().toOtherActivity(MyFriendsActivity.class);
        });
    }

    private void showMyDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(getContext());
        myDialog.show();

        myDialog.setHintText("是否确认清空未读消息？");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                chatListFragment.clearUnread();
            }
        });
    }

    private void setFrag() {
        chatListFragment = new ChatListFragment();
        Bundle args=new Bundle();
        args.putBoolean("style",false);
        chatListFragment.setArguments(args);
//        friendFragment = new FriendFragment();
        fragments.add(chatListFragment);
//        fragments.add(friendFragment);
        fragPagerAdapter = new FragPagerAdapter(getChildFragmentManager());
        fragPagerAdapter.setList_title(titles);
        fragPagerAdapter.setList_fragment(fragments);
        mViewPagerMessage.setAdapter(fragPagerAdapter);
        fragPagerAdapter.notifyDataSetChanged();
        mTabLayoutMessage.setViewPager(mViewPagerMessage);
        mViewPagerMessage.setCurrentItem(0);
    }


    /**
     * 设置用户资料
     */
    private void setTUImessage() {
        String nickName = (String) SharedPreferenceUtils.get(getContext(), Const.User.NICKNAME, "");
        String img = (String) SharedPreferenceUtils.get(getContext(), Const.User.IMG, "");
        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, nickName);
        profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, img);
        TIMFriendshipManager.getInstance().modifySelfProfile(profileMap, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e(LogUtils.TAG, "modifySelfProfile failed: " + i + " desc" + s);
            }

            @Override
            public void onSuccess() {
                LogUtils.e(LogUtils.TAG, "设置用户资料成功");
            }
        });

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
