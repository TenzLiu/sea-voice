//package com.cityrise.uuvoice.dialog;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import androidx.viewpager.widget.ViewPager;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.cityrise.uuvoice.R;
//import com.cityrise.uuvoice.adapter.FragPagerAdapter;
//import com.cityrise.uuvoice.ui.find.fragment.PalFragment;
//import com.cityrise.uuvoice.ui.message.fragment.ChatListFragment;
//import com.flyco.tablayout.SlidingTabLayout;
//
//import java.util.ArrayList;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
///**
// * 消息弹窗
// * Created by Administrator on 2018/3/9.
// */
//
//public class BottomMessageDialog extends Dialog {
//
//    ArrayList<String> titles;
//    ArrayList<Fragment> fragments;
//
//    FragPagerAdapter fragPagerAdapter;
//    ChatListFragment chatListFragment;
//    PalFragment palFragment;
//    FragmentActivity mContext;
//
//
//    @BindView(R.id.view_message_d)
//    View viewMessageD;
//    @BindView(R.id.mTabLayout_message_d)
//    SlidingTabLayout mTabLayoutMessageD;
//    @BindView(R.id.iv_close_message_d)
//    ImageView ivCloseMessageD;
//    @BindView(R.id.mViewPager_message_d)
//    ViewPager mViewPagerMessageD;
//    @BindView(R.id.ll_back_auction)
//    LinearLayout llBackAuction;
//
//
//    public BottomMessageDialog(FragmentActivity context) {
//        super(context, R.style.CustomDialogStyle);
//        this.mContext = context;
//        titles = new ArrayList<>();
//        fragments = new ArrayList<>();
//        titles.add(mContext.getString(R.string.title_message));
//        titles.add(mContext.getString(R.string.title_plaza));
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.setCancelable(false);  // 是否可以撤销
//        setContentView(R.layout.dialog_message);
//        ButterKnife.bind(this);
//        setCanceledOnTouchOutside(true);
//        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().setGravity(Gravity.BOTTOM);
//        getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
//
//        setFrag();
//    }
//
//    private void setFrag() {
//        chatListFragment = new ChatListFragment();
//        palFragment = new PalFragment();
//        fragments.add(chatListFragment);
//        fragments.add(palFragment);
//        fragPagerAdapter = new FragPagerAdapter(mContext.getSupportFragmentManager());
//        fragPagerAdapter.setList_title(titles);
//        fragPagerAdapter.setList_fragment(fragments);
//        mViewPagerMessageD.setAdapter(fragPagerAdapter);
//        fragPagerAdapter.notifyDataSetChanged();
//        mTabLayoutMessageD.setViewPager(mViewPagerMessageD);
//        mViewPagerMessageD.setCurrentItem(0);
//    }
//
//
//    @OnClick({R.id.view_message_d, R.id.iv_close_message_d})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.view_message_d:
//                dismiss();
//                break;
//            case R.id.iv_close_message_d:
//                dismiss();
//                break;
//        }
//    }
//}