package com.kemp.demo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kemp.demo.R
import com.kemp.demo.utils.gone
import com.kemp.demo.utils.visible

class MainAdapter(var mContext: Context, val datas: List<MainItemData>) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.setName(datas[position].title)
        holder.setDescription(datas[position].des)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(datas[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false))
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvDes: TextView = itemView.findViewById(R.id.tv_des)

        fun setName(text: String) {
            tvName.text = text
        }

        fun setDescription(text: String?) {
            if (text.isNullOrEmpty()) {
                tvDes.gone()
            } else {
                tvDes.visible()
                tvDes.text = text
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(mainItemData: MainItemData)
    }
}

