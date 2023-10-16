package com.jingtaoi.yy.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.jingtaoi.yy.R;


/**
 * Created by lmw on 2016/11/25.
 */

public class ViewPagerIndicator {
    private LinearLayout container;
    private int count;
    private LayoutInflater inflater;
    private int normalRes;
    private int selectedRes;

    public ViewPagerIndicator(Context context, LinearLayout container,int normalRes,int selectedRes, int count) {
        inflater = LayoutInflater.from(context);
        this.container = container;
        this.count = count;
        this.normalRes = normalRes;
        this.selectedRes = selectedRes;
    }

    /**
     * 必须在viewpager获取数据之后再设置关联
     *
     * @param viewPager
     */
    public void setupWithViewPager(ViewPager viewPager) {
        container.removeAllViews();
        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.layout_indecator, container, false);
            View indicator = view.findViewById(R.id.view);
            if (i == 0) {
                indicator.setBackgroundResource(R.drawable.indicator_select);
            } else
                indicator.setBackgroundResource(R.drawable.indicator_normal);
            container.addView(view);
        }
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < count; i++) {
                    View view =  container.getChildAt(i).findViewById(R.id.view);
                    if (i == position % count) {
                        view.setBackgroundResource(R.drawable.indicator_select);
                    } else
                        view.setBackgroundResource(R.drawable.indicator_normal);
                }
            }
        });
    }
}
