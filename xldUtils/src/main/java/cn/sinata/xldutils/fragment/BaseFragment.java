package cn.sinata.xldutils.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.utils.DensityUtil;
import cn.sinata.xldutils.utils.Toast;
import cn.sinata.xldutils.view.utils.ViewHolder;
import cn.sinata.xldutils.widget.ProgressDialog;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * fragment ，单独在activity使用请设置setUserVisibleHint(true),结合viewpager使用不需要
 */
public abstract class BaseFragment extends Fragment {

    private ProgressDialog dialog;
    protected Context context;
    protected int mScreenWidth;
    protected abstract int getContentViewLayoutID();
    protected abstract void onFirstVisibleToUser();
    protected abstract void onVisibleToUser();
    protected abstract void onInvisibleToUser();

    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;
    protected ViewHolder mHolder;
    private CompositeDisposable compositeSubscription;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        mScreenWidth = DensityUtil.getDeviceWidth(context);
        initPrepare();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compositeSubscription = new CompositeDisposable();
        mHolder = new ViewHolder(view);
    }

    public void addDisposable(Disposable disposable){
        compositeSubscription.add(disposable);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume){
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()){
            onVisibleToUser();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onInvisibleToUser();
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onVisibleToUser();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
//                onFirstUserInvisible();
            } else {
               onInvisibleToUser();
            }
        }
    }

    protected <T extends View >T bind(int resId){
        return mHolder.bind(resId);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (compositeSubscription!=null&&!compositeSubscription.isDisposed()){
            compositeSubscription.dispose();
        }
        // for bug ---> java.lang.IllegalStateException: Activity has been destroyed
        //noinspection TryWithIdenticalCatches
//        try {
//            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstVisibleToUser();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 查找	View
     * @param paramInt
     * @return
     */
    public final <T extends View>T findViewById(int paramInt) {
        if (getView()==null){
            return null;
        }
        //noinspection unchecked
        return (T)getView().findViewById(paramInt);
    }

    /**
     * 显示加载提示窗
     */
    protected void showDialog(){
        showDialog("加载中...");
    }

    /**
     * 显示加载提示窗
     * @param msg 提示文字
     */
    protected void showDialog(CharSequence msg){
        showDialog(msg,false);
    }

    /**
     * 显示加载提示窗
     * @param msg 提示文字
     * @param canCancel 是否可手动取消
     */
    protected void showDialog(CharSequence msg,boolean canCancel){
        if (context == null) {
            return;
        }
        if(dialog == null){
            dialog = new ProgressDialog(context, R.style.Theme_ProgressDialog);
        }
        dialog.setCanceledOnTouchOutside(canCancel);
        dialog.setMessage(msg);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 关闭加载窗
     */
    public void dismissDialog(){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    /**
     * 显示Toast
     * @param msg 显示文字
     */
    public void showToast(String msg){
        if (context == null) {
            return;
        }
        Toast.create(this).show(msg);
    }
}
