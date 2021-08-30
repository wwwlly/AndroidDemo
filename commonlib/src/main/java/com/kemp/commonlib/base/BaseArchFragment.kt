package com.kemp.commonlib.base

import android.arch.lifecycle.*
import android.os.Bundle
import java.lang.reflect.ParameterizedType

abstract class BaseArchFragment<VM : ViewModel> : PriceFragment(), ViewModelStoreOwner {

    protected val mViewModel: VM by lazy { ViewModelProviders.of(this).get(getVMClass()) }

    private val mLifecycleRegistry = LifecycleRegistry(this)

    private val mViewModelStore = ViewModelStore()

    private fun getVMClass(): Class<VM> {
        return (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifecycleRegistry
    }

    override fun getViewModelStore(): ViewModelStore = mViewModelStore

//    fun <T> LiveData<T>.observe(observer: T?.() -> Unit) {
//        observe({ lifecycle }, observer)
//    }

    fun <T> StatusLiveData<T>.observe(observer: StatusLiveData.Resource<T>.() -> Unit) {
        observe({ lifecycle }, { it?.observer() })
    }

//    fun <T> PageLiveData<T>.observe(observer: PageLiveData.Resource<T>.() -> Unit) {
//        observe({ lifecycle }, { it?.observer() })
//    }

    override fun onNoLazy() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        super.onNoLazy()
    }

    override fun onStart() {
        super.onStart()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onVisible() {
        super.onVisible()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun onInVisible() {
        super.onInVisible()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    override fun onStop() {
        super.onStop()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        mViewModelStore.clear()
    }

    protected fun lifeSafe(life: Lifecycle.State = Lifecycle.State.CREATED, l: () -> Unit) {
        if (lifecycle.currentState.isAtLeast(life)) {
            l()
        }
    }
}