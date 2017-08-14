package com.smile.calendar.baseadapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingbin on 2016/11/25
 */
public abstract class BaseRecyclerViewAdapter<T, D extends ViewDataBinding> extends RecyclerView.Adapter<BaseRecyclerViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    protected List<T> data = new ArrayList<>();
    protected OnItemClickListener listener;
    protected OnItemLongClickListener onItemLongClickListener;
    protected int resLayoutId;

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        holder.onBaseBindViewHolder(data.get(position), position);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);
    }

    public BaseRecyclerViewAdapter(int resourceId) {
        this.resLayoutId = resourceId;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder view = new ViewHolder(parent, resLayoutId);
        return view;
    }

    private class ViewHolder extends BaseRecyclerViewHolder<T, D> {

        ViewHolder(ViewGroup parent, int item_android) {
            super(parent, item_android);
        }

        @Override
        public void onBindViewHolder(T object, int position, D binding) {
            onNewBindViewHolder(object, position, binding);
        }
    }

    /**
     * @param object   the data of bind
     * @param position the item position of recyclerView
     */
    public abstract void onNewBindViewHolder(T object, final int position, D binding);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void add(T object) {
        data.add(object);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void remove(T object) {
        data.remove(object);
    }
    public void remove(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }
    public void removeAll(List<T> data) {
        this.data.retainAll(data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * 长按监听
     */
    public void setOnItemLongClickListener(@NonNull OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;

    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    //长按
    @Override
    public boolean onLongClick(@NonNull View v) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(v, (Integer) v.getTag());
        }
        return true;
    }

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 实现Item长按监听
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public T getItem(int position) {
        if (position > data.size() - 1) {
            return null;
        }
        return data.get(position);
    }
}
