//package com.xld.youyvoice.mvp.basemvp;
//
//import android.support.annotation.NonNull;
//
//import com.xld.youyvoice.mvp.basemvp.BaseModel;
//import com.xld.youyvoice.mvp.basemvp.BaseView;
//
//import java.lang.ref.WeakReference;
//
//
//public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {
//    private WeakReference<V> viewWeakReference;
//    private M model;
//
//    public BasePresenter() {
//    }
//
//    public BasePresenter(@NonNull V view, @NonNull M model) {
//        viewWeakReference = new WeakReference<>(view);
//        this.model = model;
//    }
//
//    public M getModel() {
//        return model;
//    }
//
//    public void setModel(@NonNull M model) {
//        this.model = model;
//    }
//
//    /**
//     * You should call method {@link #isViewAttached()} before calling this method.
//     * <br>For example:
//     * <pre class="prettyprint">
//     *     if(isViewAttached()){
//     *         V view = getView();
//     *         //todo
//     *     }
//     * </pre>
//     *
//     * @return view
//     */
//    public V getView() {
//        return viewWeakReference.get();
//    }
//
//    public boolean isViewAttached() {
//        return viewWeakReference.get() != null;
//    }
//
//    public void setView(@NonNull V view) {
//        viewWeakReference = new WeakReference<>(view);
//    }
//
//    public void release() {
//        if (viewWeakReference != null) {
//            V view = viewWeakReference.get();
//            view = null;
//        }
//    }
//}
//
