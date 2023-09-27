package cn.sinata.xldutils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import cn.sinata.xldutils.adapter.util.ViewHolder;
import cn.sinata.xldutils.widget.ProgressDialog;

import java.util.List;

/**
 *
 * Created by LiaoXiang on 2015/11/4.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected List<T> mData;
    private int layoutId;
    protected Context mContext;
    private ProgressDialog dialog;

    public BaseRecyclerAdapter(List<T> mData, int layoutId) {
        this.mData = mData;
        this.layoutId = layoutId;
    }
    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    protected OnItemClickListener mOnItemClickListener;

    /**
     * 设置OnItemClickListener
     * @param mOnItemClickListener OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutId <= 0) {
            layoutId = android.R.layout.simple_list_item_1;
        }
        mContext = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        initItemClickListener(holder,position);
        T t = mData.get(position);
        if (t != null) {
            onBind(position, t, holder);
        }
    }

    private void initItemClickListener(final ViewHolder holder ,final int position){
        //如果设置了回调，则设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
//                    Utils.systemErr(holder.getAdapterPosition());
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 绑定视图与数据
     *
     * @param position 当前位置
     * @param t        数据
     * @param holder 视图绑定holder
     */
    public abstract void onBind(int position, T t, ViewHolder holder);
}
