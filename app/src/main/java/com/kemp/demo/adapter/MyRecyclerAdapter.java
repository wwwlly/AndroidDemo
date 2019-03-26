package com.kemp.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kemp.demo.R;

import java.util.List;

/**
 * Created by wangkp on 2018/1/3.
 */

public class MyRecyclerAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<T> datas;

    private OnItemLongClickListener mOnItemLongClickListener;

    public MyRecyclerAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_my, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv.setText(datas.get(position).toString());
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RecyclerView.ViewHolder vh);
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tv;

    MyViewHolder(View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.tv);
    }
}
