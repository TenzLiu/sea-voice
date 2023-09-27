package cn.sinata.xldutils.adapter.util;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private View mConvertView;

    public ViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T bind(int viewId) {// 通过ViewId得到View


        SparseArray<View> viewHolder = (SparseArray<View>) mConvertView
                .getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            mConvertView.setTag(viewHolder);
        }

        View childView = viewHolder.get(viewId);
        if (childView == null) {
            childView = mConvertView.findViewById(viewId);
            viewHolder.put(viewId, childView);
        }
        return (T) childView;

    }

    /**
     * 设置TextView文字
     *
     * @param resId TextView的id
     * @param text  文字内容
     */
    public void setText(int resId, CharSequence text) {
        if (bind(resId) instanceof TextView)
            ((TextView) bind(resId)).setText(text);
    }

}