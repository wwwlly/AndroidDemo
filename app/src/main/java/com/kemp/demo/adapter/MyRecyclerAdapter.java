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

    public MyRecyclerAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_my, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(datas.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tv;

    MyViewHolder(View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.tv);
    }
}
