package cn.sinata.xldutils.net.utils;

import java.lang.ref.WeakReference;

import cn.sinata.xldutils.activitys.BaseActivity;
import cn.sinata.xldutils.activitys.RecyclerListActivity;
import cn.sinata.xldutils.fragment.BaseFragment;
import cn.sinata.xldutils.fragment.RecyclerViewFragment;
import cn.sinata.xldutils.utils.Utils;
import cn.sinata.xldutils.xldUtils;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 *
 * Created by liaoxiang on 16/7/11.
 */
public class ResultSubscriber<T> extends DisposableSubscriber<T> {

    WeakReference<BaseActivity> activities;
    WeakReference<BaseFragment> fragments;

    public ResultSubscriber(BaseActivity activity){
        activities = new WeakReference<>(activity);
        if (activity!=null) {
            activity.compositeSubscription.add(this);
        }
    }

    public ResultSubscriber(BaseFragment fragment){
        fragments = new WeakReference<>(fragment);
        if (fragment!=null && !fragment.isDetached()) {
            fragment.addDisposable(this);
        }
    }

    @Override
    public void onError(Throwable e) {
        dismissDialog();
        if (xldUtils.DEBUG) {
            e.printStackTrace();
        }
        Utils.systemErr(e);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        dismissDialog();
    }

    private void dismissDialog() {
        if (activities != null) {
            BaseActivity activity = activities.get();
            if (activity != null) {
                activity.dismissDialog();
                if (activity instanceof RecyclerListActivity) {
                    ((RecyclerListActivity) activity).setRefreshing(false);
                }
            }
        }
        if (fragments != null) {
            BaseFragment fragment = fragments.get();
            if (fragment != null) {
                fragment.dismissDialog();
                if (fragment instanceof RecyclerViewFragment) {
                    ((RecyclerViewFragment) fragment).setRefreshing(false);
                }
            }
        }
    }
}
