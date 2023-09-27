package com.siyecaoi.yy.ui.room.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.bean.GiftShowBean;
import com.siyecaoi.yy.utils.ImageUtils;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;

import java.util.Map;


public class DanJackpotAdapter extends BaseQuickAdapter<GiftShowBean.DataEntity, BaseViewHolder> {
    private Map<Integer, Integer> fishBeanViewMap;

    public DanJackpotAdapter(int layoutResId, Map<Integer, Integer> fishBeanViewMap) {
        super(layoutResId);
        this.fishBeanViewMap = fishBeanViewMap;
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftShowBean.DataEntity item) {
        LinearLayout ll_container = helper.getView(R.id.ll_container);

        ViewGroup.LayoutParams containerParams = ll_container.getLayoutParams();
        int itemWidth = (int) ((ScreenUtil.getScreenWidth(mContext) - ScreenUtil.getPxByDp(80) - ScreenUtil.getPxByDp(2*2*3)) / 3);
        int itemHeight = itemWidth + ScreenUtil.getPxByDp(30);
        containerParams.width = itemWidth;
        containerParams.height = itemHeight;
        ll_container.setLayoutParams(containerParams);

        RelativeLayout rl_image = helper.getView(R.id.rl_image);
        ViewGroup.LayoutParams imageContainerParams = rl_image.getLayoutParams();
        imageContainerParams.width = itemWidth - ScreenUtil.getPxByDp(10);
        imageContainerParams.height = itemWidth - ScreenUtil.getPxByDp(10);
        rl_image.setLayoutParams(imageContainerParams);

        ImageView iv_image = helper.getView(R.id.iv_right);
        ViewGroup.LayoutParams imageParams = iv_image.getLayoutParams();
        imageParams.width = itemWidth - ScreenUtil.getPxByDp(30);
        imageParams.height = itemWidth - ScreenUtil.getPxByDp(30);
        iv_image.setLayoutParams(imageParams);

        helper.setText(R.id.tv_giftName,item.getName());
        helper.setText(R.id.tv_giftGold,item.getGold()+"");
        ImageUtils.loadImage(iv_image.getContext(),iv_image, item.getImg(),-1,false);
    }

}
