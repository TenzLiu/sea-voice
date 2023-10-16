package com.jingtaoi.yy.ui.find;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.ui.find.fragment.RadioDatingOneFragment;
import com.jingtaoi.yy.ui.other.WebActivity;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 广播交友页面
 */
public class RadioDatingActivity extends MyBaseActivity {
    @BindView(R.id.rl_radiodating)
    RelativeLayout rlRadiodating;

    @Override
    public void initData() {

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_radiodating);
        setTitleText(R.string.title_radioda);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
        setRightImg(getDrawable(R.drawable.explain));
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Const.ShowIntent.TYPE, 2);
                bundle.putString(Const.ShowIntent.TITLE, "广播交友说明");
                ActivityCollector.getActivityCollector().toOtherActivity(WebActivity.class, bundle);
            }
        });
        RadioDatingOneFragment radioDatingFragment = new RadioDatingOneFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_radiodating, radioDatingFragment).commitAllowingStateLoss();
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
