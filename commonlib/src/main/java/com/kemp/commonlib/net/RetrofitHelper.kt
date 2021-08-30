package com.kemp.commonlib.net

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.kemp.commonlib.json.SafeGsonBuilder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.Charset


/**
 * Created by yinzj on 2018/11/14.
 */
object RetrofitHelper {
    val mBuilder: Retrofit.Builder = Retrofit.Builder().addConverterFactory(GsonConverter())

            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpUtils.getInstance().okHttpClient.newBuilder()
                    .addInterceptor { chain ->
                        val req = chain.request()
                        var url = req.url
                        // OPTODO 2019/12/13 要确定一个没有参数要不要传sign
//                        if (req.method() == "GET" && url.queryParameter("sign").isNullOrEmpty() && url.querySize() > 0) {
//                            url = url.newBuilder().addQueryParameter("sign", MD5.getMD5("?${url.encodedQuery()}" + AppConstants.MD5)).build()
//                        }
                        chain.proceed(req.newBuilder().url(url).build()).apply {
                            try {
                                logNet(this)
                            } catch (e: Exception) {
                            }
                        }
                    }.build())

    private val mStringBuilder: Retrofit.Builder = Retrofit.Builder().addConverterFactory(StringConverter.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpUtils.getInstance().okHttpClient.newBuilder()
                    .addInterceptor { chain ->
                        val req = chain.request()
                        var url = req.url
//                        if (req.method() == "GET" && url.queryParameter("sign").isNullOrEmpty()) {
//                            url = url.newBuilder().addQueryParameter("sign", MD5.getMD5("?${url.encodedQuery()}" + AppConstants.MD5)).build()
//                        }
                        chain.proceed(req.newBuilder().url(url).build()).apply {
                            try {
                                logNet(this)
                            } catch (e: Exception) {
                            }
                        }
                    }.build())

    inline fun <reified T> create(baseUrl: String): T {
        return mBuilder.baseUrl(baseUrl).build().create(T::class.java)
    }

    fun <T> create(baseUrl: String, service: Class<T>): T {
        return mBuilder.baseUrl(baseUrl).build().create(service)
    }

    //返回的加密字符串
    fun <T> create(baseUrl: String, service: Class<T>, isEncryptResponse: Boolean): T {
        return if (isEncryptResponse) {
            mStringBuilder.baseUrl(baseUrl).build().create(service)
        } else {
            create(baseUrl, service)
        }
    }
}


fun logg(msg: String) {
//    if (LibBundle.isDebug) {
//        Logger.e("RetrofitHelper", msg)
//    }
}

private fun logNet(response: Response) {
//    if (LibBundle.isDebug) {
//        logg("==================start=======================")
//        val segmentSize = 3 * 1024
//        logg("url=${response.request().url()}")
//        logg("headers=\n${response.request().headers()}")
//        logg("msg=${response.message()}")
//        "${response.body()?.source()?.apply {
//            request(java.lang.Long.MAX_VALUE)
//        }?.buffer()?.clone()?.readString(Charset.forName("UTF-8"))}".apply {
//            var msg = this
//            try {
//                msg = com.yiche.price.tool.Decrypt.DESDecrypt(msg)
//            } catch (e: Exception) {
//            }
//
//            msg = GsonBuilder().setPrettyPrinting().create().toJson(JsonParser().parse(msg))
//
//            if (length <= segmentSize) {
//                logg(msg)
//            } else {
//                var index = 1
//                while (msg.length > segmentSize) {
//                    val logContent = msg.substring(0, segmentSize)
//                    logg("segment${index}-->${logContent}")
//                    msg = msg.substring(segmentSize)
//                    index++
//                }
//                logg("segment${index}-->${msg}")
//            }
//        }
//        logg("====================end=======================")
//    }
}

open class MyObserver<T> : Observer<T> {

    var onNext: ((T) -> Unit)? = null
    var onComplete: (() -> Unit)? = null
    var onError: ((Throwable) -> Unit)? = null
    var onSubscribe: ((Disposable) -> Unit)? = null

    fun onNext(l: (T) -> Unit) {
        onNext = l
    }

    fun onComplete(l: () -> Unit) {
        onComplete = l
    }

    fun onError(l: (Throwable) -> Unit) {
        onError = l

    }

    fun onSubscribe(l: (Disposable) -> Unit) {
        onSubscribe = l
    }

    override fun onError(e: Throwable) {
        onError?.invoke(e)
        onComplete()
    }

    override fun onNext(t: T) {
        onNext?.invoke(t)
    }

    override fun onComplete() {
        onComplete?.invoke()
    }

    override fun onSubscribe(d: Disposable) {
        onSubscribe?.invoke(d)
    }

}

fun <T> Observable<T>.observer(observer: MyObserver<T>.() -> Unit) {
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(MyObserver<T>().apply(observer))
}

annotation class Decrypt
annotation class JsonPost
annotation class SafeGson(val sensitive: Boolean = true)
annotation class Description(val value: String)

private class GsonConverter constructor(private var gson: Gson = Gson()) : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                       retrofit: Retrofit): Converter<ResponseBody, *> {
        var realGson = gson
        annotations.firstOrNull { it is SafeGson }?.let {
            if ((it as SafeGson).sensitive) {
                realGson = SafeGsonBuilder.default
            } else {
                realGson = SafeGsonBuilder.insensitive
            }
        }
        val adapter = realGson.getAdapter(TypeToken.get(type))
        annotations.forEach {
            when (it) {
//                is Decrypt -> return DecryptGsonResponseBodyConverter(realGson, adapter)
                is Description -> logg(it.value)
            }
        }
        return ResponseBodyConverter(realGson, adapter)
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        if (methodAnnotations.any { it is JsonPost }) {
            return JsonPostConverter(gson, gson.getAdapter(TypeToken.get(type)))
        }
        return BodySignConverter(gson, gson.getAdapter(TypeToken.get(type)))
    }
}

private class BodySignConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {
    override fun convert(value: T): RequestBody {
        val builder = FormBody.Builder()
        try {
            val jsonObject = gson.fromJson(gson.toJson(value), JsonObject::class.java)
            if (!jsonObject.has("sign")) {
//                val sign = jsonObject.entrySet().map { "${it.key}=${URLConstants.URLEncode(it.value.asString)}" }.joinToString("&") { it }
//                jsonObject.addProperty("sign", MD5.getMD5("?${sign}" + AppConstants.MD5))
            }
            jsonObject.entrySet().forEach {
                builder.addEncoded(it.key, it.value.asString)
            }
        } catch (e: Exception) {
        }
        return builder.build()
    }
}

private class JsonPostConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {
    override fun convert(value: T): RequestBody {
        return RequestBody.create("application/json; charset=UTF-8".toMediaTypeOrNull(), GsonBuilder().disableHtmlEscaping().create().toJson(value))
    }
}

private class ResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val jsonReader = gson.newJsonReader(value.charStream())
        try {
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            return result
        } finally {
            value.close()
        }
    }
}

private class StringConverter : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return ConfigurationServiceConverter()
    }

    internal inner class ConfigurationServiceConverter : Converter<ResponseBody, String> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {
            val r = BufferedReader(InputStreamReader(value.byteStream()))
            val total = StringBuilder()
            val line: String
            line = r.readLine()
            while (line != null) {
                total.append(line)
            }
            return total.toString()
        }
    }

    companion object {
        fun create(): StringConverterFactory {
            return StringConverterFactory()
        }
    }
}