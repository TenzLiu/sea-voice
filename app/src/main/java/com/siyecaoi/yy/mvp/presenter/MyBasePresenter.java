package com.siyecaoi.yy.mvp.presenter;

import com.siyecaoi.yy.mvp.basemvp.BaseModel;
import com.siyecaoi.yy.mvp.basemvp.BaseView;

import java.lang.ref.WeakReference;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class MyBasePresenter<V extends BaseView, M extends BaseModel> {
    private CompositeDisposable compositeDisposable = null;
    private WeakReference<V> viewWeakReference;
    private M model;

    public MyBasePresenter() {
    }

    public MyBasePresenter(@NonNull V view, @NonNull M model) {
        viewWeakReference = new WeakReference<>(view);
        this.model = model;
    }

    /**
     * Add a disposable to manager.
     *
     * @param disposable disposable
     */
    public void add(@NonNull Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * Remove a disposable from manager.
     *
     * @param disposable disposable
     */
    public void remove(@NonNull Disposable disposable) {
        if (compositeDisposable == null) {
            return;
        }
        compositeDisposable.remove(disposable);
    }

    /**
     * Remove all disposables from manager.
     */
    public void clearAllDisposables() {
        if (compositeDisposable == null) {
            return;
        }
        compositeDisposable.dispose();
        compositeDisposable.clear();

        if (viewWeakReference != null) {
            V view = viewWeakReference.get();
            view = null;
        }
    }

    public M getModel() {
        return model;
    }

    public void setModel(@androidx.annotation.NonNull M model) {
        this.model = model;
    }

    public V getView() {
        return viewWeakReference.get();
    }

    public boolean isViewAttached() {
        return viewWeakReference.get() != null;
    }

    public void setView(@androidx.annotation.NonNull V view) {
        viewWeakReference = new WeakReference<>(view);
    }
}

