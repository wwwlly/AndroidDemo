package com.kemp.commonlib.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.properties.Delegates

abstract class PriceFragment : Fragment(), ILife, ILazyLife, ILifeBack, IFindView {

    private var isViewCreated = false
    private var isInit = false
    private var isInPager = false

    private var onVisibleChange: ((isVisible: Boolean) -> Unit)? = null

    fun onVisibleChange(l: (isVisible: Boolean) -> Unit) {
        onVisibleChange = l
    }

    open fun onVisibleChange(visible: Boolean) {
        if (visible) onVisible() else onInVisible()
        onVisibleChange?.invoke(visible)
    }

    protected var isUIVisible: Boolean by Delegates.observable(false, { _, old, new ->
        if (old != new) {
            dispatchVisible(new)
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun <T> findViewById(id: Int): T? = view?.findViewById<View>(id) as? T

    abstract fun getLayoutId(): Int

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewCreated = true
        onNoLazy()
        if (savedInstanceState != null) {
            val isHide = savedInstanceState.getBoolean("isHidden")
            if (isHide) {
                fragmentManager?.beginTransaction()?.hide(this)?.commitAllowingStateLoss()
            } else {
                fragmentManager?.beginTransaction()?.show(this)?.commitAllowingStateLoss()
            }
        }
    }

    /** 结合viewpager使用时会调用 */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isInPager = true
        isUIVisible = isVisibleToUser && isResumed
    }

    fun showSelf() {
        fragmentManager?.beginTransaction()?.show(this)?.commitAllowingStateLoss()
    }

    fun hideSelf() {
        fragmentManager?.beginTransaction()?.hide(this)?.commitAllowingStateLoss()
    }

    fun removeSelf() {
        fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
    }

    fun replaceSelf(fm: FragmentManager, id: Int) {
        fm.beginTransaction().replace(id, this).commitAllowingStateLoss()
    }

    fun isInitLazy() = isInit

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isUIVisible = !hidden && isResumed
    }

    private fun dispatchVisible(visible: Boolean) {
        if (isViewCreated) {
            if (visible) {
                if (!isInit) {
                    isInit = true
                    onLazy()
                }
            }
            onVisibleChange(visible)
        }
    }

    private fun onLazy() {
        lazyInitData()
        lazyFindView()
        lazyInitViews()
        lazyInitListeners()
        lazyLoadData()
    }

    protected open fun onNoLazy() {
        initData()
        findView()
        initViews()
        initListeners()
        loadData()
    }

    protected open fun onVisible() {

    }

    protected open fun onInVisible() {

    }

    override fun onResume() {
        super.onResume()
        isUIVisible = (!isInPager && !isHidden) || (isInPager && userVisibleHint)

    }

    override fun onPause() {
        super.onPause()
        isUIVisible = false
    }

    fun finishActivity(resultCode: Int? = null, data: Intent? = null) {
        if (resultCode != null) {
            activity?.setResult(resultCode, data)
        }
        activity?.finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.putBoolean("isHidden", isHidden)
        super.onSaveInstanceState(outState)
    }

    override fun initData() {
    }

    override fun findView() {
    }

    override fun initViews() {
    }

    override fun initListeners() {
    }

    override fun loadData() {
    }

    override fun lazyInitData() {
    }

    override fun lazyFindView() {
    }

    override fun lazyInitViews() {
    }

    override fun lazyInitListeners() {
    }

    override fun lazyLoadData() {
    }

    override fun onActivityBackPressed(): Boolean {
        return false
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
    }
}

interface ILife {
    fun initData()
    fun findView()
    fun initViews()
    fun initListeners()
    fun loadData()
}

interface ILazyLife {
    fun lazyInitData()
    fun lazyFindView()
    fun lazyInitViews()
    fun lazyInitListeners()
    fun lazyLoadData()
}

interface ILifeBack {
    fun onActivityBackPressed(): Boolean
}

interface IFindView {
    fun <T> findViewById(id: Int): T?
}

