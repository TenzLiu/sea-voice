package cn.sinata.xldutils.activitys;

import android.os.Bundle;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.adapter.ImagePagerAdapter;

import java.util.List;

public class ImagePagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    public static final String CURRENT_POSITION="position";
    public static final String URLS="urls";
    private ViewPager imagePager;
    private List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);

        initView();
        initData();
    }

    private void initView() {
        imagePager = bind(R.id.mImageViewPager);

    }

    private void initData() {
        int position = getIntent().getIntExtra(CURRENT_POSITION,0);
        urls = getIntent().getStringArrayListExtra(URLS);

        imagePager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager(),urls));
        imagePager.addOnPageChangeListener(this);
        imagePager.setCurrentItem(position);
        TextView tv_page = bind(R.id.tv_pages);
        tv_page.setText(getString(R.string.pageAndSizes,String.valueOf(position+1),String.valueOf(urls.size())));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TextView tv_page = bind(R.id.tv_pages);
        tv_page.setText(getString(R.string.pageAndSizes,String.valueOf(position+1),String.valueOf(urls.size())));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imagePager!=null) {
            imagePager.removeOnPageChangeListener(this);
        }
    }

}
