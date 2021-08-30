package com.kemp.commonlib.base

import android.arch.lifecycle.ViewModel
import com.kemp.commonlib.net.HttpResult
import com.kemp.commonlib.net.RetrofitHelper
import com.kemp.commonlib.tools.observer

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.lang.reflect.ParameterizedType

open class PriceViewModel<T> : ViewModel() {
    protected val mApi: T by lazy { RetrofitHelper.create("http://192.168.15.37/webapi/test.ashx", getTClass()) }
//    protected val mApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.OP_BASE_2), getTClass()) }
//    protected val mSnsApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.MSN_BASE), getTClass()) }
//    protected val mOrderApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.OP_BASE_6), getTClass()) }
//    protected val mDealerApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.OP_BASE_8), getTClass()) }
//    protected val mNewsApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.OP_BAES_NEWS), getTClass()) }
//    protected val mSearchApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.OP_BAES_SEARCH), getTClass()) }
//    protected val mMobileActApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.ACT_MOBILE), getTClass()) }
//    protected val mDealerNewApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.NEW_DEALER), getTClass()) }
//    protected val mMSNNewApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.NEW_MSN), getTClass()) }
//    protected val mMobileNewApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.NEW_MOBILE), getTClass()) }
//    protected val mWebNewApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.NEW_WEBAPI), getTClass()) }
//    protected val mNewOrderApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.NEW_ORDER), getTClass()) }
//    protected val mTestApi: T by lazy { RetrofitHelper.create("http://172.20.11.135:9200/", getTClass()) }
//    protected val mNewUsedCarApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.NEW_UESD_CAR), getTClass()) }
//    protected val mMockApi: T by lazy { RetrofitHelper.create("http://10.168.9.55:3000/mock/27/", getTClass()) }
//    protected val mNewNewsApi: T by lazy { RetrofitHelper.create(URLConstants.getUrl(URLConstants.NEW_NEWS), getTClass()) }
//    protected val mYCApi: T by lazy { RetrofitHelper.create(URLConstants.YICHE_BAOJIA_BASE, getTClass()) }
//    protected val mNet: T by lazy { PriceNetWork.getService(getTClass()) }
//    protected val mNetBj: T by lazy { PriceNetWork.getService(URLConstants.YICHE_BJ_BASE, getTClass()) }
//    protected val mYCCarApi: T by lazy { PriceNetWork.getService(URLConstants.YICHE_CAR_BASE, getTClass()) }
//    protected val mYCCarApi2: T by lazy { RetrofitHelper.create(URLConstants.YICHE_CARAPI_BASE, getTClass()) }

    private fun getTClass(): Class<T> {
        return (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }

    fun <T> Observable<HttpResult<T>>.observer(liveData: StatusLiveData<T>, t: (StatusLiveData<T>.(T) -> Unit)? = null) {
        observer {
            onNext {
                if (it.isSuccess) {
                    if (t == null) {
                        liveData.setData(it.Data)
                    } else {
                        liveData.t(it.Data)
                    }
                } else {
                    onError?.invoke(StatusThrowable(it.Message))
                }
            }
            onError(liveData::error)
        }
    }

//    fun <T> Observable<YcHttpResult<T>>.ycObserver(liveData: StatusLiveData<T>, t: (StatusLiveData<T>.(T) -> Unit)? = null) {
//        observer {
//            onNext {
//                if (it.isSuccess()) {
//                    if (t == null) {
//                        liveData.setData(it.data)
//                    } else {
//                        liveData.t(it.data)
//                    }
//                } else {
//                    onError?.invoke(StatusThrowable(it.message))
//                }
//            }
//            onError(liveData::error)
//        }
//    }

    operator fun <F, S> Observable<F>.plus(o: Observable<S>): Observable<Pair<F, S>> {
        return Observable.zip(this, o, BiFunction<F, S, Pair<F, S>> { f, s -> f to s })
    }

    interface EmptyApi
}