package com.kemp.commonlib.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

class StatusLiveData<T> : LiveData<StatusLiveData.Resource<T>>() {

    var tag: Any? = null

    class Resource<T>(val data: T?, var status: Int = -1, private val msg: String = "") {

        private var onNone: ((msg: String) -> Unit)? = null
        private var onError: ((error: String) -> Unit)? = null
        private var onSuccess: ((data: T) -> Unit)? = null
        private var onComplete: (() -> Unit)? = null
        private var onOther: ((String) -> Unit)? = null

        companion object STATUS {
            const val OTHER = -1
            const val NONE = 0
            const val ERROR = 1
            const val SUCCESS = 2
        }

        internal fun setStatus(status: Int) {
            this.status = status
            when (status) {
                OTHER -> onOther?.invoke(msg)
                NONE -> onNone?.invoke(msg)
                ERROR -> onError?.invoke(msg)
                SUCCESS -> {
                    if (data != null) {
                        onSuccess?.invoke(data)
                    } else {
                        onNone?.invoke(msg)
                    }
                }
            }
            onComplete?.invoke()
        }

        fun onNone(c: (msg: String) -> Unit) {
            onNone = c
        }

        fun onError(c: (error: String) -> Unit) {
            onError = c
        }

        fun onSuccess(c: (data: T) -> Unit) {
            onSuccess = c
        }

        fun onComplete(c: () -> Unit) {
            onComplete = c
        }

        fun onOther(c: (String) -> Unit) {
            onOther = c
        }

        fun dispatchError(msg: String = "") {
            onError?.invoke(msg)
        }

        fun dispatchNone(msg: String = "") {
            onNone?.invoke(msg)
        }

    }

    fun postData(value: T?) {
        super.postValue(Resource(value, Resource.SUCCESS))
    }

    fun setData(value: T?) {
        super.setValue(Resource(value, Resource.SUCCESS))
    }

    fun postNone(msg: String = "", data: T? = null) {
        super.postValue(Resource<T>(data, Resource.NONE, msg))
    }

    fun none(msg: String = "", data: T? = null) {
        super.setValue(Resource<T>(data, Resource.NONE, msg))
    }

    fun postError(msg: String = "", data: T? = null) {
        super.postValue(Resource<T>(data, Resource.ERROR, msg))
    }

    fun error(msg: String = "", data: T? = null) {
        super.setValue(Resource<T>(data, Resource.ERROR, msg))
    }

    fun postOther(other: String = "", data: T? = null) {
        super.postValue(Resource<T>(data, Resource.OTHER, other))
    }

    fun other(other: String = "", data: T? = null) {
        super.setValue(Resource<T>(data, Resource.OTHER, other))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<Resource<T>>) {
        super.observe(owner, Observer {
            observer.onChanged(it)
            it?.let { it.setStatus(it.status) }
        })
    }

    fun observeSingle(owner: LifecycleOwner, observer: (Resource<T>?) -> Unit) {
        removeObservers(owner)
        super.observe(owner, Observer {
            observer.invoke(it)
            it?.let { it.setStatus(it.status) }
        })
    }

    fun error(t: Throwable) {
//        if (LibBundle.isDebug) {
//            t.printStackTrace()
//        }
        val err = if (t is StatusThrowable) t.message ?: "" else ""
        this.error(err)
    }
}