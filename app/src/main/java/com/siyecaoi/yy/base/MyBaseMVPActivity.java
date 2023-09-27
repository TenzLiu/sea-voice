package com.siyecaoi.yy.base;


import androidx.annotation.NonNull;

import com.siyecaoi.yy.mvp.presenter.MyBasePresenter;

import java.util.ArrayList;
import java.util.List;

public abstract class MyBaseMVPActivity extends MyBaseActivity {
    private List<MyBasePresenter> presenterManager = null;

    /**
     * Add presenter into presenter manager.
     *
     * @param presenter presenter instance
     */
    public final void addToPresenterManager(@NonNull MyBasePresenter presenter) {
        if (presenterManager == null) {
            presenterManager = new ArrayList<>();
        }
        presenterManager.add(presenter);
    }

    /**
     * Remove presenter from presenter manager.
     *
     * @param presenter presenter instance
     */
    public final void removeFromPresenterManager(@NonNull MyBasePresenter presenter) {
        if (presenterManager != null && !presenterManager.isEmpty()) {
            presenterManager.remove(presenter);
        }
    }

    /**
     * Release presenters' resources.
     */
    public void recyclePresenterResources() {
        if (presenterManager != null && !presenterManager.isEmpty()) {
            for (MyBasePresenter presenter : presenterManager) {
                presenter.clearAllDisposables();
                presenter = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        recyclePresenterResources();
        super.onDestroy();
    }
}
