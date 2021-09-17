package com.kemp.commonlib.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.extensions.LayoutContainer

open class QuickViewHolder(override val containerView: View?) : BaseViewHolder(containerView), LayoutContainer

class PriceViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer

class ListViewHolder(override val containerView: View?) : LayoutContainer