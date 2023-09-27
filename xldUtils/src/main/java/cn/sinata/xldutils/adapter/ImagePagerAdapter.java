package cn.sinata.xldutils.adapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import cn.sinata.xldutils.fragment.ImageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ImagePagerAdapter extends FragmentPagerAdapter {

    private List<String> urls ;

    public ImagePagerAdapter(FragmentManager fm, List<String> urls) {
        super(fm);
        this.urls = urls==null?new ArrayList<String>():urls;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(urls.get(position));
    }

    @Override
    public int getCount() {
        return urls.size();
    }
}
