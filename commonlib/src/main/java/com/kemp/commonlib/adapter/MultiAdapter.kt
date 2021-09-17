package com.kemp.commonlib.adapter

import android.support.annotation.CallSuper
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import org.jetbrains.anko.collections.forEach
import java.lang.reflect.ParameterizedType

interface MultiItem {
    fun layoutId(): Int
}

class MultiAdapter<T> : BaseQuickAdapter<T, QuickViewHolder>(0) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mAdapters.forEach { _, itemAdapter -> itemAdapter.onAttachedToRecyclerView(recyclerView) }
        if (recyclerView?.layoutManager is GridLayoutManager) {
            setSpanSizeLookup { _, position ->
                mAdapters.get(getDefItemViewType(position))?.getSpanSize() ?: 1
            }
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

    private val mAdapters by lazy { SparseArray<ItemAdapter<*>>() }

    fun addAdapter(adapter: ItemAdapter<*>) {
        if (mAdapters.indexOfValue(adapter) < 0) {
            adapter.realAdapter = this
            mAdapters.put(adapter.layoutId, adapter)
        }
    }

    override fun convert(helper: QuickViewHolder?, item: T) {
        helper ?: return
        mAdapters.get(helper.itemViewType)?.doConvert(helper, item)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): QuickViewHolder {
        val layoutId = mAdapters[viewType]?.layoutId
                ?: return createBaseViewHolder(TextView(parent?.context))
        return createBaseViewHolder(parent, layoutId).apply {
            mAdapters.get(viewType)?.initialize(this)
        }
    }

    override fun getDefItemViewType(position: Int): Int {
        try {
            val data = mData[position] as? Any
            if (data is MultiItem) {
                return data.layoutId()
            }
            mAdapters.forEach { _, itemAdapter ->
                if (data != null && itemAdapter.tClass == data::class.java) {
                    return itemAdapter.layoutId
                }
            }
            return super.getDefItemViewType(position)
        } catch (e: Exception) {
            return 0
        }
    }

    internal fun setItemClick() {
        if (onItemClickListener != null) {
            return
        }
        setOnItemClickListener { adapter, view, position ->
            mAdapters[getDefItemViewType(position)]?.onItemClickListener?.onItemClick(adapter, view, position)
        }
    }

    override fun onViewAttachedToWindow(holder: QuickViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder ?: return
        mAdapters[holder.itemViewType]?.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: QuickViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder ?: return
        mAdapters[holder.itemViewType]?.onViewDetachedFromWindow(holder)
    }

    fun addOneData(data: T) {
        super.addData(data)
    }

    fun addOneData(position: Int, data: T) {
        super.addData(position, data)
    }

    fun addAllData(newData: Collection<out T>) {
        super.addData(newData)
    }

    fun addAllData(position: Int, newData: Collection<out T>) {
        super.addData(position, newData)
    }

    fun getRv(): RecyclerView? = recyclerView
}

abstract class ItemAdapter<T>(val layoutId: Int) : BaseQuickAdapter<T?, QuickViewHolder>(layoutId) {

    var realAdapter: BaseQuickAdapter<*, QuickViewHolder> = this

    val multiAdapter: MultiAdapter<Any?>? get() = realAdapter as? MultiAdapter<Any?>

    val QuickViewHolder.itemPosition get() = layoutPosition - realAdapter.headerLayoutCount

    internal val tClass get() = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]

    internal fun doConvert(helper: QuickViewHolder, item: Any?) {
        convert(helper, item as? T, helper.itemPosition)
    }

    override fun convert(helper: QuickViewHolder?, item: T?) {
        helper ?: return
        convert(helper, item, helper.itemPosition)
    }

    abstract fun convert(helper: QuickViewHolder, item: T?, position: Int)

    @CallSuper
    open fun initialize(helper: QuickViewHolder) {
        if (mContext == null) {
            mContext = helper.containerView?.context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickViewHolder {
        return super.onCreateViewHolder(parent, viewType).apply {
            initialize(this)
        }
    }

    override fun setOnItemClickListener(listener: OnItemClickListener?) {
        (realAdapter as? MultiAdapter)?.setItemClick()
        super.setOnItemClickListener(listener)
    }

    open fun getSpanSize() = 1

}

fun RecyclerView.addAdapter(a: ItemAdapter<*>) {
    if (adapter == null) {
        adapter = MultiAdapter<Any?>()
    }
    (adapter as? MultiAdapter<Any?>)?.addAdapter(a)
}

fun <T> RecyclerView.getMultiAdapter(): MultiAdapter<T>? {
    return adapter as? MultiAdapter<T>
}

class RvDataChangeStopObs : RecyclerView.AdapterDataObserver() {

    private var rv: RecyclerView? = null

    fun attach(rv: RecyclerView?) {
        this.rv = rv
        try {
            rv?.adapter?.unregisterAdapterDataObserver(this)
        } catch (e: Exception) {
        }
        rv?.adapter?.registerAdapterDataObserver(this)
    }

    override fun onChanged() {
        rv?.stopScroll()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        rv?.stopScroll()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        rv?.stopScroll()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        rv?.stopScroll()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        rv?.stopScroll()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        rv?.stopScroll()
    }
}