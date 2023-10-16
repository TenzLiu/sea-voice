package com.jingtaoi.yy.ui.mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.base.MyBaseActivity;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.bean.ImgEntity;
import com.jingtaoi.yy.dialog.MyDialog;
import com.jingtaoi.yy.netUtls.Api;
import com.jingtaoi.yy.netUtls.HttpManager;
import com.jingtaoi.yy.netUtls.MyObserver;
import com.jingtaoi.yy.utils.ActivityCollector;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.view.MyPhotoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowPhotoActivity extends MyBaseActivity {
    @BindView(R.id.photoshow_viewpager)
    ViewPager photoshowViewpager;

    List<ImgEntity> listimg;
    int pos;//显示的下标
    boolean isSelf;//是否是自己

    @Override
    public void initData() {
        listimg = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            listimg = (List<ImgEntity>) bundle.getSerializable(Const.ShowIntent.DATA);
            pos = bundle.getInt(Const.ShowIntent.POSITION);
            isSelf = bundle.getBoolean(Const.ShowIntent.TYPE, false);
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_photoshow);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
//        showTitle(false);
        setTitleText("查看图片 (" + (pos + 1) + "/" + listimg.size() + ")");

        if (isSelf) {
            setRightImg(getDrawable(R.drawable.clear));
            setRightButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos < listimg.size()) {
                        showMyDialog(listimg.get(pos).getId(), pos);
                    } else {
                        pos = pos - 1;
                        setviewpager();
                    }
                }
            });
        }

        setviewpager();
    }

    MyDialog myDialog;

    private void showMyDialog(int id, final int position) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        myDialog = new MyDialog(this);
        myDialog.show();
        myDialog.setHintText("确定要删除这张图片吗？");
        myDialog.setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                deleteImgCall(id, position);
            }
        });
    }

    private void deleteImgCall(int id, int position) {
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("id", id);
        HttpManager.getInstance().post(Api.DeleteImg, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == Api.SUCCESS) {
                    listimg.remove(position);
                    if (listimg.size() <= 0) {
                        ActivityCollector.getActivityCollector().finishActivity();
                    } else {
                        if (pos == listimg.size()) {
                            pos--;
                            setTitleText("查看图片 (" + (pos + 1) + "/" + listimg.size() + ")");
                        }
                        setviewpager();
                        setResult(RESULT_OK);
                    }
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    //设置可缩放的图片显示
    private void setviewpager() {
        photoshowViewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return listimg.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
//                return super.instantiateItem(container, position);
                String imageurl = listimg.get(position).getUrl();
                View view = LayoutInflater.from(ShowPhotoActivity.this).inflate(R.layout.photoshowview_item, null);
//                SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.photo_photoview);
                MyPhotoView img = (MyPhotoView) view.findViewById(R.id.photoshow_myphoto);
                img.setTag(imageurl);
                img.setImageUri(imageurl, 900, 900);
//                Uri uri = Uri.parse(imageurl);
//                DataUtils.getInstans().setImageSrc(img,uri,img.getWidth(),img.getHeight());
                container.addView(view);//把布局加到Viewpager的控件组里面
                return view;//显示要显示的布局
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                container.removeView((View) object);
            }
        });
        photoshowViewpager.setCurrentItem(pos);

        photoshowViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (v > 0) {
                    notCanMove = false;
                }
            }

            @Override
            public void onPageSelected(int i) {
                pos = i;
                setTitleText("查看图片 (" + (pos + 1) + "/" + listimg.size() + ")");
            }

            boolean isMove;
            int choosePosition;
            boolean notCanMove;//判断是否可以跳转页面(不可移动)

            @Override
            public void onPageScrollStateChanged(int i) {
                //当页面停止的时候该参数为0，页面开始滑动的时候变成了1，
                // 当手指从屏幕上抬起变为了2（无论页面是否从1跳到了2），
                // 当页面静止后又变成了0
                switch (i) {
                    case 0:
                        if (isMove && notCanMove) {
                            isMove = false;
                            notCanMove = true;
                            if (choosePosition == pos && pos == listimg.size() - 1) {
                                photoshowViewpager.setCurrentItem(0);
                            } else if (pos == 0 && choosePosition == pos) {
                                photoshowViewpager.setCurrentItem(listimg.size() - 1);
                            }
                        } else {
                            notCanMove = true;
                        }
                        break;
                    case 1:
                        isMove = true;
                        choosePosition = pos;
                        break;
                    case 2:

                        break;
                }
            }
        });
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
