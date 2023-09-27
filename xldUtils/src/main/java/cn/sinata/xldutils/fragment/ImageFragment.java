package cn.sinata.xldutils.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import cn.sinata.xldutils.R;
import cn.sinata.xldutils.view.ZoomableDraweeView;

/**
 * 图片Fragment
 */
public class ImageFragment extends BaseFragment {

    public static ImageFragment newInstance(String url){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_image;
    }

    @Override
    protected void onFirstVisibleToUser() {
        String url;

        if (getArguments()!=null){
            url = getArguments().getString("url");
        }else {
            url = "";
        }

        //noinspection SpellCheckingInspection
        ZoomableDraweeView zoomableDraweeView = mHolder.bind(R.id.zoomDrawee);
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        Uri uri = Uri.parse(url);
        DraweeController ctrl = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .build();

        zoomableDraweeView.setHierarchy(hierarchy);
        zoomableDraweeView.setController(ctrl);

        zoomableDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
            }
        });
    }

    @Override
    protected void onVisibleToUser() {
    }

    @Override
    protected void onInvisibleToUser() {
    }

}
