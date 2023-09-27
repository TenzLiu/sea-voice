package com.siyecaoi.yy.ui.home.fragment;

import android.os.Bundle;

import com.siyecaoi.yy.R;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.sinata.xldutils.fragment.BaseFragment;

public class BannerFragment extends BaseFragment {
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.item_banner;
    }

    @Override
    protected void onFirstVisibleToUser() {
        Bundle arguments = getArguments();
        String url = arguments.getString("url");
        SimpleDraweeView ivBanner = findViewById(R.id.iv_banner);
        ivBanner.setImageURI(url);
        ivBanner.setOnClickListener(v -> {
            if (listener!=null)
                listener.onClick();
        });
    }

    @Override
    protected void onVisibleToUser() {

    }

    @Override
    protected void onInvisibleToUser() {

    }

    public interface OnClickListner{
        void onClick();
    }

    public void setListener(OnClickListner listener) {
        this.listener = listener;
    }

    private OnClickListner listener;

    static public BannerFragment newInstance(String url){
        BannerFragment bannerFragment = new BannerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bannerFragment.setArguments(bundle);
        return  bannerFragment;
    }
}
