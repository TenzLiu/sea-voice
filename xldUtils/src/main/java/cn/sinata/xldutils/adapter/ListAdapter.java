package cn.sinata.xldutils.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.utils.Toast;
import cn.sinata.xldutils.view.utils.ViewHolder;
import cn.sinata.xldutils.widget.ProgressDialog;

/**
 * 实体类list的适配器
 * @author sinata
 *
 * @param <T>
 */
public abstract class ListAdapter<T> extends BaseAdapter {

	protected Context mContext;
    protected LayoutInflater mInflater;
    private ProgressDialog dialog;

    protected List<T> mList;   // 数据集

    protected int mLayoutID;    // 布局资源ID
    /**
     * 列表适配器
     * @param ctx 上下文
     * @param list	数据list
     * @param layoutID 视图layout
     */
	public ListAdapter(Context ctx, List<T> list, int layoutID) {
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
        mList = list;
        mLayoutID = layoutID;
    }
	
	@Override
    public int getCount() {
        if (mList != null && mList.size() > 0) return mList.size();
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mList != null && mList.size() > 0) 
        	return mList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = mInflater.inflate(mLayoutID, null);

        ViewHolder holder = new ViewHolder(convertView);
        T t=getItem(position);
        if (t!=null) {
        	onBind(position, t, holder);
		}
        return convertView;
    }

    /**
     * 绑定数据
     */
    protected abstract void onBind(int position, T t, ViewHolder holder);

    protected void showDialog(){
        if (mContext == null) {
            return;
        }
        if(dialog == null){
            dialog = new ProgressDialog(mContext, R.style.Theme_ProgressDialog);
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setMessage("加载中...");
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 关闭加载窗
     */
    protected void dismissDialog(){
        if (mContext!=null && dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
    /**
     * 显示Toast
     * @param msg 显示文字
     */
    public void showToast(String msg){
        if (mContext == null) {
            return;
        }
        Toast.create(mContext).show(msg);
    }
}
