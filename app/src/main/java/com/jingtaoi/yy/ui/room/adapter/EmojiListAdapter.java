package com.jingtaoi.yy.ui.room.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jingtaoi.yy.R;
import com.jingtaoi.yy.model.EmojiList;
import com.jingtaoi.yy.utils.ImageUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class EmojiListAdapter extends BaseQuickAdapter<EmojiList.DataBean, BaseViewHolder> {


    public EmojiListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmojiList.DataBean item) {
        TextView tv_emoji_expression = helper.getView(R.id.tv_emoji_expression);
        SimpleDraweeView iv_emoji_expression = helper.getView(R.id.iv_emoji_expression);

        if (item.getUnicode() >= 128564) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            ImageUtils.loadDrawableStatic(iv_emoji_expression, getShow(item.getUnicode()));
        } else if (item.getUnicode() >= 128552 && item.getUnicode() <= 128563) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            iv_emoji_expression.setImageResource(getShow(item.getUnicode()));
        } else {
            tv_emoji_expression.setText(new String(Character.toChars(item.getUnicode())));
            iv_emoji_expression.setVisibility(View.INVISIBLE);
            tv_emoji_expression.setVisibility(View.VISIBLE);
        }
        if (item.getName().equals("排麦序")) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            iv_emoji_expression.setImageResource(R.drawable.s_number_1);
        } else if (item.getName().equals("爆灯")) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            iv_emoji_expression.setImageResource(R.drawable.deng_1);
        } else if (item.getName().equals("举手")) {
            iv_emoji_expression.setVisibility(View.VISIBLE);
            tv_emoji_expression.setVisibility(View.INVISIBLE);
            iv_emoji_expression.setImageResource(R.drawable.hand_4);
        }
    }

    private int getShow(int unicode) {
        int resInt = 0;
        switch (unicode) {
            case 128552:
                resInt = R.drawable.s_number_1;
                break;
            case 128553:
                resInt = R.drawable.deng_1;
                break;
            case 128554:
                resInt = R.drawable.hand_4;
                break;
            case 128555:
                resInt = R.drawable.cai1;
                break;
            case 128556:
                resInt = R.drawable.xiaoku;
                break;
            case 128557:
                resInt = R.drawable.sese;
                break;
            case 128558:
                resInt = R.drawable.songhua;
                break;
            case 128559:
                resInt = R.drawable.taoqi;
                break;
            case 128560:
                resInt = R.drawable.yaofan;
                break;
            case 128561:
                resInt = R.drawable.qinqin;
                break;
            case 128562:
                resInt = R.drawable.zhi6;
                break;
            case 128563:
                resInt = R.drawable.yingbi1;
                break;

            case 128604:
                resInt = R.drawable.png128604;
                break;
            case 128605:
                resInt = R.drawable.png128605;
                break;
            case 128606:
                resInt = R.drawable.png128606;
                break;
            case 128607:
                resInt = R.drawable.png128607;
                break;
            case 128608:
                resInt = R.drawable.png128608;
                break;
            case 128609:
                resInt = R.drawable.png128609;
                break;
            case 128610:
                resInt = R.drawable.png128610;
                break;
            case 128611:
                resInt = R.drawable.png128611;
                break;
            case 128612:
                resInt = R.drawable.png128612;
                break;
            case 128613:
                resInt = R.drawable.png128613;
                break;
            case 128614:
                resInt = R.drawable.png128614;
                break;
            case 128615:
                resInt = R.drawable.png128615;
                break;
        }
        return resInt;
    }
}
