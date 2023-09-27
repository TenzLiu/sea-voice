package cn.sinata.xldutils.view.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 简化findviewbyId写法
 *
 * @author sinata
 */
public class ViewHolder {

    private View mConvertView;
    private Activity mActivity;
    private SparseArray<View> mViewArray; // 包含了View引用的SparseArray

    public ViewHolder(View convertView) {
        this.mConvertView = convertView;
    }

    public ViewHolder(Activity activity) {
        this.mActivity = activity;
    }

    public <T extends View> T bind(int viewId) {// 通过ViewId得到View

        if (mActivity == null) {// ListAdapter的ViewHolder

            SparseArray<View> viewHolder = (SparseArray<View>) mConvertView
                    .getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                mConvertView.setTag(viewHolder);
            }

            View childView = viewHolder.get(viewId);
            if (childView == null) {
                childView = mConvertView.findViewById(viewId);
                viewHolder.put(viewId, childView);
            }
            return (T) childView;

        } else { // Activity的ViewHolder

            if (mViewArray == null) {
                mViewArray = new SparseArray<View>();
            }
            View childView = mViewArray.get(viewId);
            if (childView == null) {
                childView = mActivity.findViewById(viewId);
                mViewArray.put(viewId, childView);
            }
            return (T) childView;
        }
    }

    /**
     * 取消绑定
     */
    public void unBind(){
        if (mViewArray!=null) {
            mViewArray.clear();
            mViewArray = null;
        }
        this.mConvertView = null;
        this.mActivity = null;
    }

    /**
     * 通过ViewId设置Text
     *
     * @param viewId 视图id
     * @param text 文字
     */
    public void setText(int viewId, CharSequence text) {// 通过ViewId设置Text
        if (bind(viewId) != null)
            ((TextView) bind(viewId)).setText(text);
    }

    /**
     * 通过ViewId设置Text
     *
     * @param viewId 视图id
     * @param text 文字
     */
    public void setText(int viewId, Spanned text) {// 通过ViewId设置Text
        ((TextView) bind(viewId)).setText(text);
    }

    /**
     * 通过ViewId设置TextColor
     *
     * @param viewId 视图id
     * @param color 文字颜色
     */
    public void setTextColor(int viewId, int color) {// 通过ViewId设置TextColor
        ((TextView) bind(viewId)).setTextColor(color);
    }

    /**
     * 通过ViewId设置图片
     *
     * @param viewId
     * @param resId
     */
    public void setImageResource(int viewId, int resId) {// 通过ViewId设置图片
        ((ImageView) bind(viewId)).setImageResource(resId);
    }

    /**
     * 通过ViewId设置图片
     *
     * @param viewId
     * @param bm
     */
    public void setImageBitmap(int viewId, Bitmap bm) {// 通过ViewId设置图片
        ((ImageView) bind(viewId)).setImageBitmap(bm);
    }

    /**
     * 通过ViewId设置图片
     *
     * @param viewId
     * @param drawable
     */
    public void setImageDrawable(int viewId, Drawable drawable) {// 通过ViewId设置图片
        ((ImageView) bind(viewId)).setImageDrawable(drawable);
    }

    /**
     * 通过ViewId设置隐藏和显示
     *
     * @param viewId
     * @param visibility
     */
    public void setVisibility(int viewId, int visibility) {// 通过ViewId设置隐藏和显示
        bind(viewId).setVisibility(visibility);
    }

    /**
     * 通过ViewId设置点击监听
     *
     * @param viewId
     * @param l
     */
    public void setOnClickListener(int viewId, View.OnClickListener l) {// 通过ViewId设置点击监听
        bind(viewId).setOnClickListener(l);
    }
}
