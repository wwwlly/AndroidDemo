package com.kemp.demo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MainAdapter(var mContext: Context, val datas: List<MainItemData>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.setText(datas.get(position).title)
        holder.itemView.setOnClickListener({
            onItemClickListener?.onItemClick(datas.get(position))
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun setText(text: String) {
            textView.setText(text)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(mainItemData: MainItemData)
    }
}

