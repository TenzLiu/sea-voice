package cn.sinata.xldutils.widget.utils;

/**
 *
 * Created by LiaoXiang on 2015/11/18.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import cn.sinata.xldutils.activitys.BaseActivity;

import java.util.HashMap;

/**
 * 标签管理
 *
 * @author Administrator
 *
 */
public class TabManager implements TabHost.OnTabChangeListener {
    private final BaseActivity mActivity;
    private final TabHost mTabHost;
    private final int mContainerId;
    private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    TabInfo mLastTab;

    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    /**
     * 标签生成
     *
     * @author Administrator
     *
     */
    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabManager(BaseActivity activity, TabHost tabHost,
                      int containerId) {
        mActivity = activity;
        mTabHost = tabHost;
        mContainerId = containerId;
        mTabHost.setOnTabChangedListener(this);
    }

    /**
     * 添加标签
     *
     * @param tabSpec
     * @param clss
     * @param args
     */
    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mActivity));
        String tag = tabSpec.getTag();
        TabInfo info = new TabInfo(tag, clss, args);

        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state. If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        info.fragment = mActivity.getSupportFragmentManager()
                .findFragmentByTag(tag);
        if (info.fragment != null && !info.fragment.isDetached()) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager()
                    .beginTransaction();
            ft.detach(info.fragment);
            ft.commitAllowingStateLoss();
        }

        mTabs.put(tag, info);
        mTabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tabId) {
        // tab标签切换操作
        TabInfo newTab = mTabs.get(tabId);

        if (mLastTab != newTab) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager()
                    .beginTransaction();
            if (mLastTab != null) {
                // 如果当前标签的fragment不为空就隐藏
                if (mLastTab.fragment != null) {
                    // ft.detach(mLastTab.fragment);
                    ft.hide(mLastTab.fragment);// 隐藏当 前fragment
                }
            }
            if (newTab != null) {
                // 如果需要切换的标签的fragment为空就add新的fragment。反之则显示

                if (newTab.fragment == null || newTab.fragment.isDetached()) {

                    newTab.fragment = Fragment.instantiate(mActivity,
                            newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    // ft.attach(newTab.fragment);//google在FragmentTabs中使用的方法，会调用onCreateView()重绘视图
                    ft.show(newTab.fragment);// 显示fragment
                }
            }

            mLastTab = newTab;
            ft.commitAllowingStateLoss();
            mActivity.getSupportFragmentManager().executePendingTransactions();
        }
    }
}
