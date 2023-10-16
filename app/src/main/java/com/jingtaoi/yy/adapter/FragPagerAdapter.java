package com.jingtaoi.yy.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * fargmentPagerAdapter适配器
 * Created by Administrator on 2018/1/17.
 */

public class FragPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> list_fragment;
    ArrayList<String> list_title;


    public FragPagerAdapter(FragmentManager fm) {
        super(fm);
        list_fragment = new ArrayList<>();
        list_title = new ArrayList<>();
    }

    public ArrayList<Fragment> getList_fragment() {
        return list_fragment;
    }

    public void setList_fragment(ArrayList<Fragment> list_fragment) {
        this.list_fragment = list_fragment;
    }

    public ArrayList<String> getList_title() {
        return list_title;
    }

    public void setList_title(ArrayList<String> list_title) {
        this.list_title = list_title;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_title.get(position);
    }

}
